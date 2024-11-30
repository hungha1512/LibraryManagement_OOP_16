package com.hunghq.librarymanagement.Respository;

import com.hunghq.librarymanagement.Connectivity.MySQLConnection;
import com.hunghq.librarymanagement.IGeneric.IRepository;
import com.hunghq.librarymanagement.Model.Entity.Role;
import com.hunghq.librarymanagement.Model.Enum.EIsDeleted;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing users in the library management system.
 * This class handles CRUD operations and custom queries on the users table.
 */
@SuppressWarnings("rawtypes")
public class RoleDAO implements IRepository<Role> {

    private static final Connection con = MySQLConnection.getConnection();

    /**
     * Constructs a User object from the result set.
     *
     * @param reS the result set containing user data
     * @return a User object created from the result set data, or null if an error occurs
     */
    @Override
    public Role make(ResultSet reS) {
        Role role = null;

        try {
            role = new Role(
                    reS.getInt("roleId"),
                    reS.getString("title"),
                    reS.getString("slug"),
                    reS.getString("description"),
                    EIsDeleted.fromInt(reS.getInt("isDeleted")),
                    reS.getTimestamp("createdAt").toLocalDateTime(),
                    reS.getTimestamp("updatedAt") != null ? reS.getTimestamp("updatedAt").toLocalDateTime() : null
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    /**
     * Adds a new user to the database.
     *
     * @param entity the User object to add
     */
    @Override
    public void add(Role entity) {
        Role role = entity;
        String sql = "INSERT INTO roles (roleId, title, slug, description, isDeleted, createdAt, updatedAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, role.getRoleId());
            prS.setString(2, role.getTitle());
            prS.setString(3, role.getSlug());
            prS.setString(4, role.getDescription());
            prS.setString(5, role.getIsDeleted().toString());
            prS.setTimestamp(6, Timestamp.valueOf(role.getCreatedAt()));
            prS.setTimestamp(7, Timestamp.valueOf(role.getUpdatedAt()));

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Role getByStringId(String id) {
        return null;
    }

    /**
     * Retrieves a role by its integer ID.
     *
     * @param id the role ID
     * @return the Role object with the specified ID, or null if not found
     */
    @Override
    public Role getByIntId(int id) {
        String sql = "SELECT * FROM roles WHERE roleId = ?";
        Role role = null;

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, id);
            ResultSet reS = prS.executeQuery();

            if (reS.next()) {
                role = make(reS);
            } else {
                System.out.println("No role found with id: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role;
    }

    /**
     * Retrieves all roles from the database.
     *
     * @return a list of all Role objects in the database
     */
    @Override
    public ObservableList<Role> getAll() {
        String sql = "SELECT * FROM roles";
        ObservableList<Role> roles = FXCollections.observableArrayList();

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                roles.add(make(reS));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    /**
     * Searches for roles by title.
     *
     * @param name the title to search for
     * @return a list of roles whose titles match the search term
     */
    @Override
    public ObservableList<Role> findByName(String name) {
        String sql = "SELECT * FROM roles WHERE title LIKE ?";
        ObservableList<Role> roles = FXCollections.observableArrayList();

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, "%" + name + "%");
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                roles.add(make(reS));
            }
            if (roles.isEmpty()) {
                System.out.println("No role found with title: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    /**
     * Updates an existing role in the database.
     *
     * @param entity the Role object with updated information
     */
    @Override
    public boolean update(Role entity) {
        String sql = "UPDATE roles SET title = ?, slug = ?, description = ?, isDeleted = ?, " +
                "createdAt = ?, updatedAt = ? WHERE roleId = ?";

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            Role role = entity;

            // Gán giá trị cho PreparedStatement
            prS.setString(1, role.getTitle());
            prS.setString(2, role.getSlug());
            prS.setString(3, role.getDescription());
            prS.setString(4, role.getIsDeleted().toString());
            prS.setTimestamp(5, Timestamp.valueOf(role.getCreatedAt()));
            prS.setTimestamp(6, Timestamp.valueOf(role.getUpdatedAt()));
            prS.setInt(7, role.getRoleId());

            // Thực hiện cập nhật và kiểm tra số dòng bị ảnh hưởng
            int rowsAffected = prS.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có ít nhất một dòng được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu xảy ra lỗi
        }
    }


    /**
     * Deletes a role by its ID.
     *
     * @param id the role ID
     */
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM roles WHERE roleId = ?";

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, Integer.parseInt(id));

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a role to the database. If the role already exists, updates it; otherwise, adds it.
     *
     * @param entity the Role object to save
     * @return the saved Role object
     */
    @Override
    public Role save(Role entity) {
        Role role = entity;

        if (getByIntId(role.getRoleId()) != null) {
            update(role);
        } else {
            add(role);
        }

        return role;
    }

    public Role getRoleByTitle(String title) {
        String sql = "SELECT * FROM roles WHERE title LIKE ?";
        Role role = null;

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, "%" + title + "%");
            ResultSet reS = prS.executeQuery();

            if (reS.next()) {
                role = make(reS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role;
    }

}
