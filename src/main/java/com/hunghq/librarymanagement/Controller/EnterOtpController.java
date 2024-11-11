package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the OTP verification screen. Allows users to enter and verify
 * an OTP code sent for authentication purposes (e.g., password recovery).
 */
public class EnterOtpController {

    /**
     * TextField for user to enter the OTP code.
     */
    @FXML
    private TextField txt_otp;

    /**
     * Label to display the status of OTP verification.
     */
    @FXML
    private Label lbl_status;

    /** The email of the user verifying the OTP. */
    private String userEmail;

    /**
     * Sets the email of the user who is verifying the OTP.
     *
     * @param email the user's email address
     */
    public void setEmail(String email) {
        this.userEmail = email;
    }

    /**
     * Handles the OTP verification process. Checks the entered OTP against the
     * OTP associated with the user's email. If valid, opens the password change
     * window; if invalid, displays an error message.
     */
    @FXML
    private void handleVerifyOtp() {
        String enteredOtp = txt_otp.getText();

        try {
            boolean isValidOtp = AuthenticationService.verifyOtp(userEmail, enteredOtp);

            if (isValidOtp) {
                AuthenticationService.showChangePassword(userEmail);

                Stage stage = (Stage) txt_otp.getScene().getWindow();
                stage.close();
            } else {
                lbl_status.setText("OTP Code Wrong");
            }
        } catch (Exception e) {
            lbl_status.setText("Error: " + e.getMessage());
        }
    }
}
