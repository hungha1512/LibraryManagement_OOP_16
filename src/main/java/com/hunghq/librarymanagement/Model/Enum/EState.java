package com.hunghq.librarymanagement.Model.Enum;

/**
 * Enum representing the state of a borrowed item, with values for borrowed, returned, and overdue states.
 */
public enum EState {

    BORROWED("Borrowed"),
    RETURNED("Returned"),
    OVERDUE("Overdue"),
    PENDING("Pending");

    /**
     * String value associated with the state.
     */
    private String state;

    /**
     * Constructor for EState enum, initializing with the specified state value.
     *
     * @param state the string representation of the state
     */
    EState(String state) {
        this.state = state;
    }

    /**
     * Retrieves the string value associated with this state.
     *
     * @return the string representation of the state
     */
    public String getState() {
        return state;
    }

    /**
     * Converts a string value to the corresponding EState enum constant.
     *
     * @param value the string value to convert
     * @return the corresponding EState constant
     * @throws IllegalArgumentException if the value does not match any state
     */
    public static EState fromValue(String value) {
        for (EState s : EState.values()) {
            if (s.state.equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown state: " + value);
    }
}