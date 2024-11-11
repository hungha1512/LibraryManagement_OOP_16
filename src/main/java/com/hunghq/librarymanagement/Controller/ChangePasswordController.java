package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * Controller for the Change Password screen, allowing users to change their password.
 */
public class ChangePasswordController {

    /** Field for entering the new password. */
    @FXML
    private PasswordField pwd_new_password;

    /** Field for confirming the new password. */
    @FXML
    private PasswordField pwd_confirm_password;

    /** The email of the user who is changing the password. */
    private String userEmail;

    /**
     * Sets the email of the user who is changing the password.
     *
     * @param email the user's email address
     */
    public void setEmail(String email) {
        this.userEmail = email;
    }

    /**
     * Handles the password change action. If the new password and confirm password match,
     * it attempts to update the password for the user. Closes the window if the change is successful.
     */
    @FXML
    private void handleChangePassword() {
        String newPassword = pwd_new_password.getText();
        String confirmPassword = pwd_confirm_password.getText();

        if (newPassword.equals(confirmPassword)) {
            try {
                AuthenticationService.changePassword(userEmail, newPassword);
                Stage stage = (Stage) pwd_new_password.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Password does not match");
        }
    }
}
