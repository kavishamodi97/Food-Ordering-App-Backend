package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.UtilityProvider;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * This Class implements the services for Customer
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired
    private UtilityProvider utilityProvider;

    @Autowired
    private CustomerAuthDao customerAuthDao;

    /**
     * Save Customer Details in the Database
     *
     * @param customerEntity Customer Details to be saved
     * @return Customer Details
     * @throws SignUpRestrictedException Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity saveCustomer(CustomerEntity customerEntity) throws SignUpRestrictedException {
        CustomerEntity existingCustomerEntity = customerDao.getCustomerByContactNumber(customerEntity.getContactNumber());

        //Check if the Customer Data is Valid
        if (existingCustomerEntity != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number");
        }
        if (!utilityProvider.checkIfEmailIsValid(customerEntity.getEmail())) {
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
        }
        if (!utilityProvider.checkIfContactNumberIsValid(customerEntity.getContactNumber())) {
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
        }
        if (utilityProvider.checkIfPasswordIsWeak(customerEntity.getPassword())) {
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }
        if (!utilityProvider.checkIfSignupRequestIsValid(customerEntity)) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }

        // Add encoded password and salt and persist it in CustomerEntity
        String[] encryptedPassword = passwordCryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedPassword[0]);
        customerEntity.setPassword(encryptedPassword[1]);

        return customerDao.createCustomer(customerEntity);
    }

    /**
     * Authenticate the Customer using Contact Number and Password
     *
     * @param contactNumber Contact Number of the Customer
     * @param password      Password
     * @return CustomerAuthEntity
     * @throws AuthenticationFailedException Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticate(String contactNumber, String password) throws AuthenticationFailedException {
        CustomerEntity customerEntity = customerDao.getCustomerByContactNumber(contactNumber);
        if (customerEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }

        String encryptedPassword = PasswordCryptographyProvider.encrypt(password, customerEntity.getSalt());

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
        CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
        customerAuthEntity.setCustomer(customerEntity);

        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiresAt = now.plusHours(8);

        customerAuthEntity.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));
        customerAuthEntity.setLoginAt(now);
        customerAuthEntity.setExpiresAt(expiresAt);
        customerAuthEntity.setUuid(UUID.randomUUID().toString());

        return customerAuthDao.createCustomerAuth(customerAuthEntity);

    }

    /**
     * Logout the Customer
     *
     * @param accessToken Access Token of the Customer
     * @return CustomerAuthEntity
     * @throws AuthorizationFailedException Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity logout(String accessToken) throws AuthorizationFailedException {

        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthByAccessToken(accessToken);
        CustomerEntity customerEntity = getCustomer(accessToken);
        customerAuthEntity.setCustomer(customerEntity);
        customerAuthEntity.setLogoutAt(ZonedDateTime.now());
        return customerAuthDao.customerLogout(customerAuthEntity);
    }

    /**
     * Update Customer Details
     *
     * @param customerEntity CustomerEntity
     * @return CustomerEntity
     * @throws UpdateCustomerException Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(CustomerEntity customerEntity) throws UpdateCustomerException {
        CustomerEntity customerToBeUpdated = customerDao.getCustomerByUuid(customerEntity.getUuid());

        customerToBeUpdated.setFirstName(customerEntity.getFirstName());
        customerToBeUpdated.setLastName(customerEntity.getLastName());

        return customerDao.updateCustomer(customerEntity);
    }

    /**
     * Update Customer Password
     *
     * @param oldPassword    Old Password
     * @param newPassword    New Password
     * @param customerEntity CustomerEntity
     * @return CustomerEntity
     * @throws UpdateCustomerException Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerPassword(String oldPassword, String newPassword, CustomerEntity customerEntity) throws UpdateCustomerException {

        if (utilityProvider.checkIfPasswordIsWeak(newPassword)) {
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }

        String encryptedOldPassword = PasswordCryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());

        if (encryptedOldPassword.equals(customerEntity.getPassword())) {
            CustomerEntity tobeUpdatedCustomerEntity = customerDao.getCustomerByUuid(customerEntity.getUuid());

            String[] encryptedPassword = passwordCryptographyProvider.encrypt(newPassword);
            tobeUpdatedCustomerEntity.setSalt(encryptedPassword[0]);
            tobeUpdatedCustomerEntity.setPassword(encryptedPassword[1]);

            return customerDao.updateCustomer(tobeUpdatedCustomerEntity);

        } else {
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
        }
    }

    /**
     * Get Customer Details using Access Token
     *
     * @param accessToken Access Token
     * @return CustomerEntity
     * @throws AuthorizationFailedException Exception
     */
    public CustomerEntity getCustomer(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthByAccessToken(accessToken);

        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        final ZonedDateTime now = ZonedDateTime.now();

        if (customerAuthEntity.getExpiresAt().compareTo(now) <= 0) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        return customerAuthEntity.getCustomer();
    }
}