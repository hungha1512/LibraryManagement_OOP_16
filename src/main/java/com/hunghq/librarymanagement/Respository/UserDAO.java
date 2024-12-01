package com.hunghq.librarymanagement.Respository;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;
import com.hunghq.librarymanagement.Global.AppProperties;
import com.hunghq.librarymanagement.Global.DialogHelper;
import com.hunghq.librarymanagement.IGeneric.IRepository;
import com.hunghq.librarymanagement.Model.Entity.Role;
import com.hunghq.librarymanagement.Model.Entity.User;
import com.hunghq.librarymanagement.Model.Enum.EGender;
import com.hunghq.librarymanagement.Model.Enum.EIsDeleted;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO class for performing CRUD operations and custom queries on the users table.
 */
@SuppressWarnings("rawtypes")
public class UserDAO implements IRepository<User> {

    private static final Connection con = MySQLConnection.getConnection();
    private ObservableList<User> users = FXCollections.observableArrayList();

    /**
     * Constructs a User object from the result set.
     *
     * @param reS the result set containing user data
     * @return a User object created from the result set data, or null if an error occurs
     */
    @Override
    public User make(ResultSet reS) {
        try {

            RoleDAO roleDAO = new RoleDAO();
            Role role = new Role();
            int roleId = reS.getInt("roleId");
            role = roleDAO.getByIntId(roleId);
            System.out.println(role.toString());
            int isDeletedValue = reS.getInt("isDeleted");
            System.out.println("Retrieved isDeleted value: " + isDeletedValue);
            EIsDeleted isDeletedStatus = EIsDeleted.fromInt(isDeletedValue);

            return new User(
                    reS.getInt("userId"),
                    reS.getString("fullName"),
                    reS.getString("passwordHash"),
                    EGender.fromValue(reS.getString("gender")),
                    reS.getString("email"),
                    reS.getString("phone"),
                    reS.getTimestamp("joinDate") != null
                            ? reS.getTimestamp("joinDate").toLocalDateTime()
                            : null,
                    reS.getTimestamp("dateOfBirth") != null
                            ? reS.getTimestamp("dateOfBirth").toLocalDateTime()
                            : null,
                    role,
                    reS.getString("otp"),
                    isDeletedStatus
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds a new user to the database.
     *
     * @param entity the User object to add
     */
    @Override
    public boolean add(User entity) {
        User user = entity;
        String sql = "INSERT INTO users (userId, fullName, passwordHash, gender, "
                + "email, phone, joinDate, dateOfBirth, role, otp, isDeleted) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement prS = con.prepareStatement(sql)) {

            prS.setInt(1, user.getUserId());
            prS.setString(2, user.getFullName());
            prS.setString(3, user.getPasswordHash());
            prS.setString(4, user.getGender().toString());
            prS.setString(5, user.getEmail());
            prS.setString(6, user.getPhone());
            prS.setTimestamp(7, user.getJoinDate() != null
                    ? Timestamp.valueOf(user.getJoinDate())
                    : null);
            prS.setTimestamp(8, user.getDateOfBirth() != null
                    ? Timestamp.valueOf(user.getDateOfBirth())
                    : null);
            prS.setInt(9, user.getRole().getRoleId());
            prS.setString(10, user.getOtp());
            prS.setInt(11, user.getEIsDeleted().getValue());

            int rowsInserted = prS.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Retrieves a user by their string ID.
     *
     * @param id the user ID
     * @return the User object with the specified ID, or null if not found
     */
    @Override
    public User getByStringId(String id) {
        return null;
    }

    /**
     * Retrieves a user by integer ID (currently not implemented).
     *
     * @param id the integer ID of the user
     * @return null since this method is not implemented
     */
    @Override
    public User getByIntId(int id) {
        String sql = "SELECT * FROM users WHERE userId = ?";
        User user = null;
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, id);
            ResultSet reS = prS.executeQuery();

            if (reS.next()) {
                user = make(reS);
            } else {
                System.out.println("No user found with id: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of all User objects in the database
     */
    @Override
    public ObservableList<User> getAll() {
        String sql = "SELECT * FROM users";
        ObservableList<User> users = FXCollections.observableArrayList();
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                users.add(make(reS));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Searches for users by name.
     *
     * @param name the name to search for
     * @return a list of users whose names match the search term
     */
    @Override
    public ObservableList<User> findByName(String name) {
        String sql = "SELECT * FROM users WHERE fullName LIKE ?";
        ObservableList<User> users = FXCollections.observableArrayList();
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, "%" + name + "%");
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                users.add(make(reS));
            }
            if (users.isEmpty()) {
                System.out.println("No user found with name: " + name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Updates an existing user in the database.
     *
     * @param entity the User object with updated information
     */
    @Override
    public boolean update(User entity) {
        String sql = "UPDATE users SET fullName = ?, gender = ?, email = ?, " +
                "phone = ?, dateOfBirth = ? WHERE userId = ?";

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            User user = entity;

            prS.setString(1, user.getFullName());
            prS.setString(2, user.getGender().toString().toLowerCase());
            prS.setString(3, user.getEmail());
            prS.setString(4, user.getPhone());
            prS.setTimestamp(5, user.getDateOfBirth() != null
                    ? Timestamp.valueOf(user.getDateOfBirth())
                    : null);

            prS.setInt(6, user.getUserId());

            int rowsAffected = prS.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Deletes a user by their ID.
     *
     * @param id the user ID
     */
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM users WHERE userId = ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, Integer.parseInt(id));

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a user to the database. If the user already exists, updates the user; otherwise, adds the user.
     *
     * @param entity the User object to save
     * @return the saved User object
     */
    @Override
    public User save(User entity) {
        User user = (User) entity;

        if (getByIntId(user.getUserId()) != null) {
            update(user);
        } else {
            add(user);
        }

        return user;
    }

    /**
     * Retrieves a user by their phone number or email.
     *
     * @param user the User object containing the phone or email to search for
     * @return the User object matching the phone or email, or null if not found
     */
    public static User getUserByPhoneOrEmail(User user) {
        User userFound = null;
        try {
            if (con == null) {
                throw new SQLException("Failed to connect database");
            }

            String sql = "";
            EIsDeleted eIsDeleted;
            if (user.getPhone() != null) {
                sql = "SELECT * FROM users WHERE phone = ? AND isDeleted = '\"0\"'";
            }
            if (user.getEmail() != null) {
                sql = "SELECT * FROM users WHERE email = ? AND isDeleted = '\"0\"'";
            }
            PreparedStatement prS = con.prepareStatement(sql);
            if (user.getPhone() != null) {
                prS.setString(1, user.getPhone());
            }
            if (user.getEmail() != null) {
                prS.setString(1, user.getEmail());
            }

            ResultSet reS = prS.executeQuery();
            if (reS.next()) {
                //TODO: remove userName in database, create otp column
                userFound = new User();
                userFound.setUserId(reS.getInt("userId"));
                userFound.setFullName(reS.getString("fullName"));
                userFound.setEmail(reS.getString("email"));
                userFound.setPhone(reS.getString("phone"));
                userFound.setPasswordHash(reS.getString("passwordHash"));
                userFound.setOtp(reS.getString("otp"));
            }
        } catch (SQLException e) {
            DialogHelper.showNotificationDialog("Error", "Server connection failed: " + e.getMessage());
        }

        return userFound;
    }

    /**
     * Updates the OTP for a user identified by their email.
     *
     * @param user the User object containing the updated OTP
     */
    public void updateOtp(User user) {
        try {
            if (con == null) {
                throw new SQLException("Failed to connect to database");
            }
            String sql = "UPDATE users SET otp = ? WHERE email = ?";
            PreparedStatement prS = con.prepareStatement(sql);
            prS.setString(1, user.getOtp());
            prS.setString(2, user.getEmail());
            prS.executeUpdate();
        } catch (SQLException e) {
            DialogHelper.showNotificationDialog("Error", "Failed to update OTP: " + e.getMessage());
        }
    }

    /**
     * Updates the password of a user identified by their ID.
     *
     * @param id          the user ID
     * @param newPassword the new password to set
     */
    public void updatePasswordById(int id, String newPassword) {
        try {
            if (con == null) {
                throw new SQLException("Failed to connect to database");
            }
            String sql = "UPDATE users SET passwordHash = ? WHERE userId = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            DialogHelper.showNotificationDialog("Success", "Password updated successfully.");
        } catch (SQLException e) {
            DialogHelper.showNotificationDialog("Error", "Failed to update password: " + e.getMessage());
        }
    }
}
