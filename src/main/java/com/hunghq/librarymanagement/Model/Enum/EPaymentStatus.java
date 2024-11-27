package com.hunghq.librarymanagement.Model.Enum;

/**
 * Enum representing the payment status of an invoice, with values for paid and unpaid states.
 */
public enum EPaymentStatus {

    /**
     * State indicating the invoice has been paid.
     */
    PAID("Paid"),

    /**
     * State indicating the invoice has not been paid.
     */
    UNPAID("Unpaid");

    /**
     * String value associated with the payment status.
     */
    private String status;

    /**
     * Constructor for EPaymentStatus enum, initializing with the specified status value.
     *
     * @param status the string representation of the payment status
     */
    EPaymentStatus(String status) {
        this.status = status;
    }

    /**
     * Retrieves the string value associated with this payment status.
     *
     * @return the string representation of the payment status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Converts a string value to the corresponding EPaymentStatus enum constant.
     *
     * @param value the string value to convert
     * @return the corresponding EPaymentStatus constant
     * @throws IllegalArgumentException if the value does not match any status
     */
    public static EPaymentStatus fromValue(String value) {
        for (EPaymentStatus s : EPaymentStatus.values()) {
            if (s.status.equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown payment status: " + value);
    }
}
