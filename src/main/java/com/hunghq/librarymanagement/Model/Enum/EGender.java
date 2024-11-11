package com.hunghq.librarymanagement.Model.Enum;

/**
 * Enum representing gender with values for male and female.
 */
public enum EGender {

    /**
     * Gender representing a male.
     */
    MALE("Male"),

    /**
     * Gender representing a female.
     */
    FEMALE("Female");

    /**
     * String value associated with the gender.
     */
    private String gender;

    /**
     * Constructor for EGender enum, initializing with the specified gender value.
     *
     * @param gender the string representation of the gender
     */
    EGender(String gender) {
        this.gender = gender;
    }

    /**
     * Retrieves the string value associated with this gender.
     *
     * @return the string representation of the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Converts a string value to the corresponding EGender enum constant.
     *
     * @param value the string value to convert
     * @return the corresponding EGender constant, or null if no match is found
     */
    public static EGender fromValue(String value) {
        for (EGender e : EGender.values()) {
            if (e.gender.equals(value)) {
                return e;
            }
        }
        return null;
    }
}
