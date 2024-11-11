package com.hunghq.librarymanagement.Model.Enum;

/**
 * Enum representing the deletion status of an entity, with values for active (not deleted) and inactive (deleted) states.
 */
public enum EIsDeleted {

    /**
     * Status representing an active entity (not deleted).
     */
    ACTIVE(0),

    /**
     * Status representing an inactive entity (deleted).
     */
    INACTIVE(1);

    /**
     * Integer value associated with the status.
     */
    private int value;

    /**
     * Constructor for EIsDeleted enum, initializing with the specified integer value.
     *
     * @param value the integer value representing the status
     */
    EIsDeleted(int value) {
        this.value = value;
    }

    /**
     * Retrieves the integer value associated with this status.
     *
     * @return the integer value representing the status
     */
    public int getValue() {
        return value;
    }

    /**
     * Converts an integer value to the corresponding EIsDeleted enum constant.
     *
     * @param value the integer value to convert
     * @return the corresponding EIsDeleted constant
     * @throws IllegalArgumentException if the value does not match any enum constant
     */
    public static EIsDeleted fromInt(int value) {
        for (EIsDeleted status : EIsDeleted.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
