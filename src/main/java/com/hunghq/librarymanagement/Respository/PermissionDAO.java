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
import com.hunghq.librarymanagement.Model.Entity.Permission;
import com.hunghq.librarymanagement.Model.Enum.EIsDeleted;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@SuppressWarnings("rawtypes")
public class PermissionDAO implements IRepository<Permission>{

    private static final Connection con = MySQLConnection.getConnection();

    @Override
    public Permission make(ResultSet reS) {
        Permission permission = null;

        try {
            permission = new Permission(
                reS.getInt("permissionId"),
                reS.getString("title"),
                reS.getString("slug"),
                reS.getString("description"),
                EIsDeleted.valueOf(reS.getString("isDeleted")),
                reS.getTimestamp("createdAt").toLocalDateTime(),
                reS.getTimestamp("updatedAt") != null ? reS.getTimestamp("updatedAt").toLocalDateTime() : null
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return permission;
    }

    @Override
    public void add(Permission entity) {
        Permission permission = entity;
        String sql = "INSERT INTO permissions (permissionId, title, slug, description, isDeleted, createdAt, updatedAt) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            
            prS.setInt(1, permission.getPermissionId());
            prS.setString(2, permission.getTitle());
            prS.setString(3, permission.getSlug());
            prS.setString(4, permission.getDescription());
            prS.setString(5, permission.getIsDeleted().name());
            prS.setTimestamp(6, Timestamp.valueOf(permission.getCreatedAt()));
            prS.setTimestamp(7, permission.getUpdatedAt() != null ? Timestamp.valueOf(permission.getUpdatedAt()) : null);

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Permission getByStringId(String id) {
        return null;
    }

    @Override
    public Permission getByIntId(int id) {
        String sql = "SELECT * FROM permissions WHERE permissionId = ?";
        Permission permission = null;

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, id);
            ResultSet reS = prS.executeQuery();

            if (reS.next()) {
                permission = make(reS);
            } else {
                System.out.println("No permission found with id: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return permission;
    }

    @Override
    public ObservableList<Permission> getAll() {
        String sql = "SELECT * FROM permissions";
        ObservableList<Permission> permissions = FXCollections.observableArrayList();

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                permissions.add(make(reS));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return permissions;
    }

    @Override
    public ObservableList<Permission> findByName(String name) {
        String sql = "SELECT * FROM permissions WHERE title LIKE ?";
        ObservableList<Permission> permissions = FXCollections.observableArrayList();

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, "%" + name + "%");
            ResultSet reS = prS.executeQuery();

            while (reS.next()) {
                permissions.add(make(reS));
            }
            if (permissions.isEmpty()) {
                System.out.println("No permission found with title: " + name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return permissions;
    }

    @Override
    public boolean update(Permission entity) {
        String sql = "UPDATE permissions SET title = ?, slug = ?, description = ?, isDeleted = ?, updatedAt = ? " +
                "WHERE permissionId = ?";

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            Permission permission = (Permission) entity;

            // Gán giá trị vào PreparedStatement
            prS.setString(1, permission.getTitle());
            prS.setString(2, permission.getSlug());
            prS.setString(3, permission.getDescription());
            prS.setString(4, permission.getIsDeleted().name());
            prS.setTimestamp(5, permission.getUpdatedAt() != null ? Timestamp.valueOf(permission.getUpdatedAt()) : null);
            prS.setInt(6, permission.getPermissionId());

            // Thực hiện cập nhật và kiểm tra số dòng bị ảnh hưởng
            int rowsAffected = prS.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có ít nhất một dòng được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu xảy ra lỗi
        }
    }


    @Override
    public void delete(String id) {
        String sql = "DELETE FROM permissions WHERE permissionId = ?";

        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setInt(1, Integer.parseInt(id));
            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Permission save(Permission entity) {
        Permission permission = (Permission) entity;

        if (getByIntId(permission.getPermissionId()) != null) {
            update(permission);
        } else {
            add(permission);
        }

        return permission;
    }
    
}
