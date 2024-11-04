package com.hunghq.librarymanagement.Respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;
import com.hunghq.librarymanagement.IGeneric.IRepository;
import com.hunghq.librarymanagement.Model.Entity.User;

@SuppressWarnings("rawtypes")
public class UserDAO implements IRepository{

    private static final Connection con = MySQLConnection.getConnection();
    @Override
    public User Make(ResultSet reS) {
        try {
            return new User(
                reS.getString("userId"),
                reS.getString("fullName"),
                reS.getString("userName"),
                reS.getString("passwordHash"),
                reS.getString("email"),
                reS.getString("phone"),
                reS.getTimestamp("joinDate").toLocalDateTime(),
                reS.getTimestamp("dateOfBirth").toLocalDateTime()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void Add(Object entity) {
        User user = (User) entity;
        String sql = "INSERT INTO users (userId, fullName, userName, passwordHash, "
                   + "email, phone, joinDate, dateOfBirth) " 
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement prS = con.prepareStatement(sql)) {

            prS.setString(1, user.getUserId());
            prS.setString(2, user.getFullName());
            prS.setString(3, user.getUserName());
            prS.setString(4, user.getPasswordHash());
            prS.setString(5, user.getEmail());
            prS.setString(6, user.getPhone());
            prS.setTimestamp(7, Timestamp.valueOf(user.getJoinDate()));
            prS.setTimestamp(8, Timestamp.valueOf(user.getDateOfBirth()));

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User GetById(String id) {
        String sql = "SELECT * FROM users WHERE userId = ?";
        User user = null;
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, id);
            ResultSet reS = prS.executeQuery();

            if (reS.next()) {
                user = Make(reS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> GetAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                users.add(Make(reS));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> FindByName(String name) {
        String sql = "SELECT * FROM users WHERE fullName LIKE ?";
        List<User> users = new ArrayList<>();
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, "%" + name + "%");
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                users.add(Make(reS));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void Update(Object entity) {
        User user = (User) entity;
        String sql = "UPDATE users SET fullName = ?, userName = ?, passwordHash = ?, email = ? " 
                   + "phone = ?, joinDate = ?, dateOfBirth = ? WHERE userId = ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {

            prS.setString(1, user.getFullName());
            prS.setString(2, user.getUserName());
            prS.setString(3, user.getPasswordHash());
            prS.setString(4, user.getEmail());
            prS.setString(5, user.getPhone());
            prS.setTimestamp(6, Timestamp.valueOf(user.getJoinDate()));
            prS.setTimestamp(7, Timestamp.valueOf(user.getDateOfBirth()));
            prS.setString(8, user.getUserId());

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Delete(int id) {
        String sql = "DELETE FROM users WHERE userId = ?";
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, id);

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User Save(Object entity) {
        User user = (User) entity;

        if (GetById(user.getUserId()) != null) {
            Update(user);
        } else {
            Add(user);
        }

        return user;
    }
}
