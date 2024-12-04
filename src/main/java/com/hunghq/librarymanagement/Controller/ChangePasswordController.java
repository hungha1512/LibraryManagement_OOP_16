package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.LibraryApplication;
import com.hunghq.librarymanagement.Service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for the Change Password screen, allowing users to change their password.
 */
public class ChangePasswordController implements Initializable {

    @FXML
    public Button btn_change_pwd;
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

     @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pwd_confirm_password.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleChangePassword();
            }
        });

        pwd_new_password.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleChangePassword();
            }
        });

        btn_change_pwd.setOnAction(_ -> {
            handleChangePassword();
        });
    }

    /**
     * Handles the password change action. If the new password and confirm password match,
     * it attempts to update the password for the user. Closes the window if the change is successful.
     */
    @FXML
    private void handleChangePassword() {
        String newPassword = pwd_new_password.getText();
        String confirmPassword = pwd_confirm_password.getText();

        if (newPassword == null || confirmPassword == null) {
            System.out.println("New password and confirm password are null");
        }

        if (newPassword.equals(confirmPassword)) {
            try {
                AuthenticationService.changePassword(userEmail, newPassword);
                Stage stage = (Stage) pwd_new_password.getScene().getWindow();
                stage.getIcons().add(new Image(Objects.requireNonNull(LibraryApplication.class.getResourceAsStream("/com/hunghq/librarymanagement/Media/logo_uet_rm.png"))));
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Password does not match");
        }
    }
}
