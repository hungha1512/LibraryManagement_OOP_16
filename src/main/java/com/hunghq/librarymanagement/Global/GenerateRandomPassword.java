package com.hunghq.librarymanagement.Global;

import java.security.SecureRandom;

public class GenerateRandomPassword {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final String ALL_CHARACTERS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARACTERS;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a random password of the specified length.
     *
     * @param length The length of the password to generate.
     * @return A randomly generated password.
     */
    public static String generate(int length) {
        if (length < 6) {
            throw new IllegalArgumentException("Password length must be at least 6 characters.");
        }

        StringBuilder password = new StringBuilder(length);

        password.append(getRandomCharacter(LOWERCASE));
        password.append(getRandomCharacter(UPPERCASE));
        password.append(getRandomCharacter(DIGITS));
        password.append(getRandomCharacter(SPECIAL_CHARACTERS));

        for (int i = 4; i < length; i++) {
            password.append(getRandomCharacter(ALL_CHARACTERS));
        }

        return shuffle(password.toString());
    }

    private static char getRandomCharacter(String characters) {
        return characters.charAt(RANDOM.nextInt(characters.length()));
    }

    private static String shuffle(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }

    public static void main(String[] args) {
        // Example usage
        System.out.println("Random Password: " + GenerateRandomPassword.generate(12));
    }
}
