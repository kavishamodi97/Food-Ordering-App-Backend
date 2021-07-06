package com.upgrad.FoodOrderingApp.service.common;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides helper methods
 */
@Component
public class UtilityProvider {

    /**
     * Check if the password provided by the customer during sign-up id weak,
     * i.e., it doesn't have at least eight characters and does not contain
     * at least one digit, one uppercase letter, and one of the following
     * characters [#@$%&*!^]
     *
     * @param password password to be validated
     * @return true ot false
     */
    public boolean checkIfPasswordIsWeak(String password) {
        boolean lowerCase = false;
        boolean upperCase = false;
        boolean number = false;
        boolean specialCharacter = false;

        if (password.length() < 8) {
            return true;
        }

        if (password.matches("(?=.*[0-9]).*")) {
            number = true;
        }

        if (password.matches("(?=.*[a-z]).*")) {
            lowerCase = true;
        }
        if (password.matches("(?=.*[A-Z]).*")) {
            upperCase = true;
        }
        if (password.matches("(?=.*[#@$%&*!^]).*")) {
            specialCharacter = true;
        }

        if (lowerCase && upperCase) {
            return !specialCharacter || !number;
        } else {
            return true;
        }
    }

    /**
     * Validate the Contact Number
     *
     * @param contactNumber Contact Number
     * @return true or false
     */
    public boolean checkIfContactNumberIsValid(String contactNumber) {
        Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        Matcher m = p.matcher(contactNumber);
        return (m.find() && m.group().equals(contactNumber));
    }

    /**
     * Validate the Email ID
     *
     * @param email Email ID
     * @return true ot false
     */
    public boolean checkIfEmailIsValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    /**
     * Validate Sign-up Request
     *
     * @param customerEntity Customer Details
     * @return tre or false
     * @throws SignUpRestrictedException Throws Exception
     */
    public boolean checkIfSignupRequestIsValid(CustomerEntity customerEntity) throws SignUpRestrictedException {
        if (customerEntity.getFirstName() == null || customerEntity.getFirstName().equals("")) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }
        if (customerEntity.getPassword() == null || customerEntity.getPassword().equals("")) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }
        if (customerEntity.getEmail() == null || customerEntity.getEmail().equals("")) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }
        if (customerEntity.getContactNumber() == null || customerEntity.getContactNumber().equals("")) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }
        return true;
    }

    /**
     * Validate Authorization Format
     *
     * @param authorization Authorization Format
     * @return true or false
     * @throws AuthenticationFailedException Throws Exception
     */
    public boolean checkIfAuthorizationFormatIsValid(String authorization) throws AuthenticationFailedException {
        try {
            byte[] decoded = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
            String decodedAuth = new String(decoded);
            String[] decodedArray = decodedAuth.split(":");
            String username = decodedArray[0];
            String password = decodedArray[1];
            return true;
        } catch (ArrayIndexOutOfBoundsException exc) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }
    }

    /**
     * Validate Customer Update Request
     *
     * @param firstName First Name
     * @return true or false
     * @throws UpdateCustomerException Throws Exception
     */
    public boolean checkIfUpdateCustomerRequestIsValid(String firstName) throws UpdateCustomerException {
        if (firstName == null || firstName.equals("")) {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }
        return true;
    }

    /**
     * Validate Password Update Request
     *
     * @param oldPassword Old Password
     * @param newPassword New Password
     * @return true or false
     * @throws UpdateCustomerException Throws Exception
     */
    public boolean checkIfUpdatePasswordRequestIsValid(String oldPassword, String newPassword) throws UpdateCustomerException {
        if (oldPassword == null || oldPassword.equals("")) {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }
        if (newPassword == null || newPassword.equals("")) {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }
        return true;
    }

    public static String getAccessTokenFromAuthorization(String authorization)
            throws AuthorizationFailedException {
        String[] authParts = authorization.split("Bearer ");
        if (authParts.length != 2) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        return authParts[1];
    }
}