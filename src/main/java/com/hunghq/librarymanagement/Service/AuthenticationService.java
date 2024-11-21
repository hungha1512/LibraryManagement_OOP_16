package com.hunghq.librarymanagement.Service;

import com.hunghq.librarymanagement.Controller.ChangePasswordController;
import com.hunghq.librarymanagement.Global.AppProperties;
import com.hunghq.librarymanagement.Global.Format;
import com.hunghq.librarymanagement.Model.Entity.Role;
import com.hunghq.librarymanagement.Model.Entity.User;
import com.hunghq.librarymanagement.Respository.RoleDAO;
import com.hunghq.librarymanagement.Respository.UserDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

/**
 * Service class providing authentication-related functionalities
 * including login, password recovery, OTP generation, and password change.
 */
public class AuthenticationService {
    private static final UserDAO userDAO = new UserDAO();
    private static final RoleDAO roleDAO = new RoleDAO();

    /**
     * Authenticates a user by verifying the provided credentials.
     *
     * @param user the user attempting to log in
     * @return true if login is successful, false otherwise
     * @throws Exception if an error occurs during login, including invalid credentials
     */
    public static boolean login(User user) throws Exception {
        String password = user.getPasswordHash();
        User userLogged = new User();
        userLogged = UserDAO.getUserByPhoneOrEmail(user);

        try {
            boolean isLoginSuccess = BCrypt.checkpw(password, userLogged.getPasswordHash());
            if (isLoginSuccess || password.equals(userLogged.getPasswordHash())) {
                userLogged = userDAO.getByStringId(userLogged.getUserId());
                AppProperties.setProperty("user.loggedIn", "false");
                AppProperties.setProperty("user.userId", userLogged.getUserId());
                AppProperties.setProperty("user.fullName", userLogged.getFullName());
                AppProperties.setProperty("user.passwordHash", userLogged.getPasswordHash());
                AppProperties.setProperty("user.gender", userLogged.getGender().toString());
                AppProperties.setProperty("user.email", userLogged.getEmail());
                AppProperties.setProperty("user.phone", userLogged.getPhone());
                AppProperties.setProperty("user.joinDate", Format.formatDate(userLogged.getJoinDate()));
                Role roleOfUserLogged = roleDAO.getByIntId(userLogged.getRole().getRoleId());
                userLogged.setRole(roleOfUserLogged);
                AppProperties.setProperty("user.roleTitle", roleOfUserLogged.getTitle());
                AppProperties.setProperty("user.dateOfBirth", Format.formatDate(userLogged.getDateOfBirth()));
                return true;
            } else {
                System.out.println("Username or password wrong.");
                return false;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Authenticates a user based on saved credentials from a properties file.
     *
     * @param properties the properties object containing saved user information
     * @return true if authentication is successful, false otherwise
     * @throws Exception if an error occurs while reading from the properties file
     */
    public static boolean authenticateFromFile(Properties properties) throws Exception {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/application.properties";
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (Boolean.parseBoolean(properties.getProperty("user.isRemember"))) {
            User userSaved = new User();
            userSaved.setPasswordHash(properties.getProperty("user.passwordHash"));
            userSaved.setEmail(properties.getProperty("user.email"));
            if (login(userSaved)) {
                AppProperties.setProperty("user.loggedIn", "true");
                return true;
            }
        }
        return false;
    }

    /**
     * Initiates the forgot password process by generating an OTP and sending it to the user's email.
     *
     * @param email the email address of the user who forgot their password
     * @throws Exception if the email does not exist or an error occurs while sending the email
     */
    public static void forgotPassword(String email) throws Exception {
        User user = new User();
        user.setEmail(email);
        User userFound = new User();
        userFound = UserDAO.getUserByPhoneOrEmail(user);
        if (userFound == null) {
            throw new Exception("Email does not exist");
        }

        String otp = generateOTP();
        userFound.setOtp(otp);

        userDAO.updateOtp(userFound);

        String subject = "Recover password";
        String body = "Hello, your OTP is: " + otp;

        EmailService.sendEmail(email, subject, body);

        System.out.println("Send email successful to: " + email);
    }

    /**
     * Generates a random 6-digit OTP.
     *
     * @return a randomly generated 6-digit OTP as a string
     */
    private static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Verifies if the provided OTP matches the stored OTP for the user associated with the email.
     *
     * @param email the email of the user to verify
     * @param otp   the OTP provided by the user
     * @return true if the OTP is correct, false otherwise
     */
    public static boolean verifyOtp(String email, String otp) {
        User user = new User();
        user.setEmail(email);
        User userFound = UserDAO.getUserByPhoneOrEmail(user);

        if (userFound != null && userFound.getOtp().equals(otp)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Changes the password for the user associated with the provided email.
     *
     * @param email       the email of the user whose password is to be changed
     * @param newPassword the new password to set
     * @throws Exception if the email does not exist or an error occurs during the update
     */
    public static void changePassword(String email, String newPassword) throws Exception {
        User user = new User();
        user.setEmail(email);
        User userFound = UserDAO.getUserByPhoneOrEmail(user);

        if (userFound != null) {
            String pswd = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            userFound.setPasswordHash(pswd);

            userDAO.updatePasswordById(userFound.getUserId(), pswd);
        } else {
            throw new Exception("Email does not exist");
        }
    }

    /**
     * Displays the "Change Password" UI window for the user with the provided email.
     *
     * @param email the email of the user changing their password
     */
    public static void showChangePassword(String email) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AuthenticationService.class.getResource("/com/hunghq/librarymanagement/View/ChangePassword.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Change Password");
            stage.setScene(scene);

            ChangePasswordController controller = fxmlLoader.getController();
            controller.setEmail(email);

            stage.setResizable(false);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logout() {
        AppProperties.setProperty("user.loggedIn", "false");
        AppProperties.setProperty("user.userId", "");
        AppProperties.setProperty("user.fullName", "");
        AppProperties.setProperty("user.passwordHash", "");
        AppProperties.setProperty("user.gender", "");
        AppProperties.setProperty("user.email", "");
        AppProperties.setProperty("user.phone", "");
        AppProperties.setProperty("user.joinDate", "");
        AppProperties.setProperty("user.roleTitle", "");
        AppProperties.setProperty("user.dateOfBirth", "");
        AppProperties.setProperty("user.isRemember", "false");
    }
}
