package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Global.GenerateRandomPassword;
import com.hunghq.librarymanagement.Global.Validation;
import com.hunghq.librarymanagement.Model.Entity.Role;
import com.hunghq.librarymanagement.Model.Entity.User;
import com.hunghq.librarymanagement.Model.Enum.EGender;
import com.hunghq.librarymanagement.Model.Enum.EIsDeleted;
import com.hunghq.librarymanagement.Respository.RoleDAO;
import com.hunghq.librarymanagement.Respository.UserDAO;
import com.hunghq.librarymanagement.Service.EmailService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.mindrot.jbcrypt.BCrypt;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

import static com.hunghq.librarymanagement.Global.Format.isValidDate;

public class AddUserController {
    @FXML
    private TextField tf_user_id;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_full_name;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_phone;
    @FXML
    private TextField tf_date_of_birth;
    @FXML
    private RadioButton rb_librarian;
    @FXML
    private RadioButton rb_user;
    @FXML
    private RadioButton rb_male;
    @FXML
    private RadioButton rb_female;
    @FXML
    private Button btn_delete_all;
    @FXML
    private Button btn_add;

    @FXML
    private ToggleGroup roleGroup;
    @FXML
    private ToggleGroup genderGroup;

    private UserDAO userDAO;
    private RoleDAO roleDAO = new RoleDAO();
    private int userId;
    private String password;
    private String passwordHash;

    public void initialize() {
        userDAO = new UserDAO();
        roleDAO = new RoleDAO();

        initializePassAndId();

        roleGroup = new ToggleGroup();
        rb_librarian.setToggleGroup(roleGroup);
        rb_user.setToggleGroup(roleGroup);

        genderGroup = new ToggleGroup();
        rb_male.setToggleGroup(genderGroup);
        rb_female.setToggleGroup(genderGroup);

        setPromptText();

        btn_delete_all.setOnAction(e -> {handleDeleteAll();});
        btn_add.setOnAction(e -> {
            try {
                handleAdd();
            } catch (MessagingException ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    private void initializePassAndId() {
        this.userId = userDAO.getMaxUserId() + 1;
        tf_user_id.setText(String.valueOf(this.userId));
        tf_user_id.setEditable(false);

        this.password = GenerateRandomPassword.generate(6);
        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        tf_password.setText(this.passwordHash);
        tf_password.setEditable(false);
    }

    private void setPromptText() {
        tf_full_name.setPromptText("Enter Full Name");
        tf_email.setPromptText("Enter Email Address");
        tf_phone.setPromptText("Enter Phone Number");
        tf_date_of_birth.setPromptText("Enter Date of Birth (YYYY-MM-DD)");
    }

    private void handleDeleteAll() {
        tf_full_name.clear();
        tf_email.clear();
        tf_phone.clear();
        tf_date_of_birth.clear();
        rb_librarian.setSelected(false);
        rb_user.setSelected(false);
        rb_male.setSelected(false);
        rb_female.setSelected(false);

        initializePassAndId();
    }

    private void handleAdd() throws MessagingException {
        if (validateInputs()) {

            String email = tf_email.getText();
            String phone = tf_phone.getText();
            String date_of_birth = tf_date_of_birth.getText();

            if (!Validation.isEmailValid(email)) {
                showAlert("Invalid Email", "The email address is not valid.");
                return;
            } else if (!Validation.isPhoneNumberValid(phone)) {
                showAlert("Invalid Phone", "The phone number is not valid.");
                return;
            }
            if (!isValidDate(date_of_birth)) {
                showAlert("Invalid Date of Birth", "The date of birth is not valid.");
                return;
            }

            User newUser = createUser();

            if (userDAO.add(newUser)) {
                showAlert("Success", "User has been successfully added.");

                String subject = "Welcome to Library Management System!";
                String body = String.format(
                        "Dear %s,\n\n" +
                                "Congratulations! Your account has been successfully created in our Library Management System.\n\n" +
                                "Here are your account details:\n" +
                                "User ID: %s\n" +
                                "Password: %s\n" +
                                "Full Name: %s\n" +
                                "Email: %s\n\n" +
                                "Phone Number: %s\n" +
                                "You can now log in to your account and enjoy our services. If you have any questions, feel free to contact us.\n\n" +
                                "Best regards,\n" +
                                "Library Management Team",
                        newUser.getFullName(), newUser.getUserId(), this.password,
                        newUser.getFullName(), newUser.getEmail(), newUser.getPhone()
                );
                EmailService.sendEmail(email, subject, body);

                initializePassAndId();
            } else {
                showAlert("Error", "Failed to add user. Please try again.");
            }

        }
    }

    private boolean validateInputs() {
        if (tf_full_name.getText().isEmpty()) {
            showAlert("Full Name is required!", "Please enter the full name.");
            return false;
        }
        if (tf_email.getText().isEmpty()) {
            showAlert("Email is required!", "Please enter the email.");
            return false;
        }
        if (tf_phone.getText().isEmpty()) {
            showAlert("Phone is required!", "Please enter the phone number.");
            return false;
        }
        if (tf_date_of_birth.getText().isEmpty()) {
            showAlert("Date of Birth is required!", "Please enter the date of birth.");
            return false;
        }
        if (roleGroup.getSelectedToggle() == null) {
            showAlert("Role is required!", "Please select a role (Librarian or User).");
            return false;
        }

        if (genderGroup.getSelectedToggle() == null) {
            showAlert("Gender is required!", "Please select a gender (Male or Female).");
            return false;
        }
        return true;
    }

    private User createUser() {
        String fullName = tf_full_name.getText();
        String email = tf_email.getText();
        String phone = tf_phone.getText();
        String dateOfBirth = tf_date_of_birth.getText();

        int roleId = rb_librarian.isSelected() ? 2 : 1;

        String gender = rb_male.isSelected() ? "male" : "female";

        User user = new User();

        user.setUserId(this.userId);
        user.setPasswordHash(this.passwordHash);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setJoinDate(LocalDateTime.now());
        user.setDateOfBirth(LocalDateTime.parse(dateOfBirth + "T00:00:00"));
        user.setRole(roleDAO.getByIntId(roleId));
        user.setGender(EGender.fromValue(gender));
        user.setEIsDeleted(EIsDeleted.ACTIVE);

        return user;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
