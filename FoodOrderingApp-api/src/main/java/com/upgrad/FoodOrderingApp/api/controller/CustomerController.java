package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.common.Utility;
import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/")
public class CustomerController {

    @Autowired private CustomerService customerService;

    /**
     * This api endpoint is used to signup a new customer
     *
     * @param signupCustomerRequest this argument contains all the attributes required for creating a
     *     new customer in the database.
     * @return ResponseEntity<SignupCustomerResponse> type object along with HttpStatus CREATED.
     * @throws SignUpRestrictedException if customer entered details not meet the requirements.
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/customer/signup",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup(
            @RequestBody(required = true) final SignupCustomerRequest signupCustomerRequest)
            throws SignUpRestrictedException {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setEmailAddress(signupCustomerRequest.getEmailAddress());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        customerEntity.setPassword(signupCustomerRequest.getPassword());

        CustomerEntity createdCustomerEntity = customerService.saveCustomer(customerEntity);

        SignupCustomerResponse customerResponse =
                new SignupCustomerResponse()
                        .id(createdCustomerEntity.getUuid())
                        .status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(customerResponse, HttpStatus.CREATED);
    }

    /**
     * This api endpoint is used to login the customer.
     *
     * @param authorization customer contactNumber and password in 'Basic
     *     Base64<contactNumber:password>' format.
     * @return ResponseEntity<LoginResponse> type object with HttpStatus as OK.
     * @throws AuthenticationFailedException if customer contactNumber or password is wrong.
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/customer/login",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(
            @RequestHeader("authorization") final String authorization)
            throws AuthenticationFailedException {
        byte[] decode;
        String contactNumber;
        String password;
        // ArrayIndexOutOfBoundsException occurs if the username or password is left as empty or try to
        // authorize without Basic in prefix 'Basic Base64<contactNumber:password>' then it throws
        // AuthenticationFailedException with code as ATH-003
        try {
            decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
            String decodedText = new String(decode);
            String[] decodedArray = decodedText.split(":");
            contactNumber = decodedArray[0];
            password = decodedArray[1];
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException exception) {
            throw new AuthenticationFailedException(
                    "ATH-003", "Incorrect format of decoded customer name and password");
        }

        CustomerAuthEntity createdCustomerAuthEntity =
                customerService.authenticate(contactNumber, password);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(createdCustomerAuthEntity.getCustomer().getUuid());
        loginResponse.setFirstName(createdCustomerAuthEntity.getCustomer().getFirstName());
        loginResponse.setLastName(createdCustomerAuthEntity.getCustomer().getLastName());
        loginResponse.setContactNumber(createdCustomerAuthEntity.getCustomer().getContactNumber());
        loginResponse.setEmailAddress(createdCustomerAuthEntity.getCustomer().getEmailAddress());
        loginResponse.setMessage("LOGGED IN SUCCESSFULLY");

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", createdCustomerAuthEntity.getAccessToken());
        List<String> header = new ArrayList<>();
        header.add("access-token");
        headers.setAccessControlExposeHeaders(header);

        return new ResponseEntity<LoginResponse>(loginResponse, headers, HttpStatus.OK);
    }

    /**
     * This api endpoint is used to logout the customer.
     *
     * @param authorization is the access token of the customer in 'Bearer <access-token>' format.
     * @return ResponseEntity<LogoutResponse> type object along with HttpStatus as OK.
     * @throws AuthorizationFailedException if any of the validation on customer access token fails.
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/customer/logout",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(
            @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {
        final String accessToken = Utility.getTokenFromAuthorization(authorization);
        CustomerAuthEntity createdCustomerAuthEntity = customerService.logout(accessToken);
        LogoutResponse logoutResponse =
                new LogoutResponse()
                        .id(createdCustomerAuthEntity.getCustomer().getUuid())
                        .message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
    }

    /**
     * This api endpoint is used to update customer details.
     *
     * @param updateCustomerRequest this argument contains all the attributes required to update a
     *     customer in the database.
     * @param authorization customers access token in 'Bearer <access-token>' format.
     * @return ResponseEntity<UpdateCustomerResponse> type object along with HttpStatus as OK.
     * @throws AuthorizationFailedException if any validation on customer access token fails.
     * @throws UpdateCustomerException if first name is not provided in updateCustomerRequest param.
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "/customer",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateCustomerResponse> update(
            @RequestHeader("authorization") final String authorization,
            @RequestBody(required = true) final UpdateCustomerRequest updateCustomerRequest)
            throws UpdateCustomerException, AuthorizationFailedException {
        if (updateCustomerRequest.getFirstName() == null
                || updateCustomerRequest.getFirstName().isEmpty()) {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }

        String accessToken = Utility.getTokenFromAuthorization(authorization);

        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        customerEntity.setFirstName(updateCustomerRequest.getFirstName());
        customerEntity.setLastName(updateCustomerRequest.getLastName());

        CustomerEntity updatedCustomerEntity = customerService.updateCustomer(customerEntity);

        UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse();
        updateCustomerResponse.setId(updatedCustomerEntity.getUuid());
        updateCustomerResponse.setFirstName(updatedCustomerEntity.getFirstName());
        updateCustomerResponse.setLastName(updatedCustomerEntity.getLastName());
        updateCustomerResponse.status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");

        return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse, HttpStatus.OK);
    }

    /**
     * @param updatePasswordRequest this argument contains all the attributes required to update a
     *     customer's password in the database.
     * @param authorization customer access token in 'Bearer <access-token>' format.
     * @return ResponseEntity<UpdatePasswordResponse> type object along with HttpStatus as OK.
     * @throws AuthorizationFailedException if any of the validation on customer access token fails.
     * @throws UpdateCustomerException if old or new password fields are empty.
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "/customer/password",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> changePassword(
            @RequestHeader("authorization") final String authorization,
            @RequestBody(required = true) final UpdatePasswordRequest updatePasswordRequest)
            throws UpdateCustomerException, AuthorizationFailedException {

        String oldPassword = updatePasswordRequest.getOldPassword();
        String newPassword = updatePasswordRequest.getNewPassword();

        if (oldPassword != null
                && !oldPassword.isEmpty()
                && newPassword != null
                && !newPassword.isEmpty()) {
            String accessToken = Utility.getTokenFromAuthorization(authorization);
            CustomerEntity customerEntity = customerService.getCustomer(accessToken);

            CustomerEntity updatedCustomerEntity =
                    customerService.updateCustomerPassword(oldPassword, newPassword, customerEntity);

            UpdatePasswordResponse updatePasswordResponse =
                    new UpdatePasswordResponse()
                            .id(updatedCustomerEntity.getUuid())
                            .status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");

            return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);
        } else {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }
    }
}