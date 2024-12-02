package com.hunghq.librarymanagement.Global;

import javax.mail.internet.InternetAddress;

/**
 * Utility class for validating phone numbers and email addresses.
 * Provides methods to check if the input matches a specific format.
 */
public class Validation {

    /**
     * Validates a phone number to ensure it contains only digits and is between 10 and 15 characters long.
     *
     * @param phoneNumber the phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        String pattern = "^[0-9]{10,15}";
        return phoneNumber.matches(pattern);
    }

    /**
     * Validates an email address to ensure it follows a standard format.
     * The format includes alphanumeric characters, underscores, periods, and hyphens, with a domain and top-level domain.

     * @param email the email address to validate
     * @return true if the email address is valid, false otherwise
     */
    public static boolean isEmailValid(String email) {
        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(pattern);
    }
}
