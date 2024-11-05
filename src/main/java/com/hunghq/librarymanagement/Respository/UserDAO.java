package com.hunghq.librarymanagement.Respository;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;
import com.hunghq.librarymanagement.IGeneric.IRepository;
import com.hunghq.librarymanagement.Model.Entity.Role;
import com.hunghq.librarymanagement.Model.Entity.User;
import com.hunghq.librarymanagement.Model.Enum.EGender;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("rawtypes")
public class UserDAO implements IRepository {

    private static final Connection con = MySQLConnection.getConnection();

    @Override
    public User make(ResultSet reS) {
        try {
            int roleId = reS.getInt("role");
            //TODO: write function to get Role from role ID
            Role role = new Role();
            return new User(
                    reS.getString("userId"),
                    reS.getString("fullName"),
                    reS.getString("passwordHash"),
                    EGender.fromValue(reS.getString("gender")),
                    reS.getString("email"),
                    reS.getString("phone"),
                    reS.getTimestamp("joinDate").toLocalDateTime(),
                    reS.getTimestamp("dateOfBirth").toLocalDateTime(),
                    reS.getString("imagePath"),
                    role,
                    reS.getString("otp")

            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void add(Object entity) {
        User user = (User) entity;
        String sql = "INSERT INTO users (userId, fullName, passwordHash, gender,"
                + "email, phone, joinDate, dateOfBirth, imagePath, role, otp) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, user.getUserId());
            prS.setString(2, user.getFullName());
            prS.setString(3, user.getPasswordHash());
            prS.setString(4, user.getEmail());
            prS.setString(5, user.getPhone());
            prS.setTimestamp(6, Timestamp.valueOf(user.getJoinDate()));
            prS.setTimestamp(7, Timestamp.valueOf(user.getDateOfBirth()));
            prS.setString(8, user.getImagePath());
            prS.setInt(9, user.getRole().getRoleId());
            prS.setString(10, user.getOtp());
            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getById(String id) {
        String sql = "SELECT * FROM users WHERE userId = ?";
        User user = null;
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, id);
            ResultSet reS = prS.executeQuery();

            if (reS.next()) {
                user = make(reS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
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

    @Override
    public List<User> findByName(String name) {
        String sql = "SELECT * FROM users WHERE fullName LIKE ?";
        List<User> users = new ArrayList<>();
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, "%" + name + "%");
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                users.add(make(reS));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void update(Object entity) {
        User user = (User) entity;
        String sql = "UPDATE users SET fullName = ?, passwordHash = ?, email = ?, "
                + "phone = ?, joinDate = ?, dateOfBirth = ? WHERE userId = ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {

            prS.setString(1, user.getFullName());
            prS.setString(2, user.getPasswordHash());
            prS.setString(3, user.getEmail());
            prS.setString(4, user.getPhone());
            prS.setTimestamp(5, Timestamp.valueOf(user.getJoinDate()));
            prS.setTimestamp(6, Timestamp.valueOf(user.getDateOfBirth()));
            prS.setString(7, user.getUserId());

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE userId = ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, id);

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User save(Object entity) {
        User user = (User) entity;

        if (getById(user.getUserId()) != null) {
            update(user);
        } else {
            add(user);
        }

        return user;
    }
}
