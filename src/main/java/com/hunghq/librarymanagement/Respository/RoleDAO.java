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
import com.hunghq.librarymanagement.Model.Entity.Role;
import com.hunghq.librarymanagement.Model.Enum.EIsDeleted;

@SuppressWarnings("rawtypes")
public class RoleDAO implements IRepository{

    private static final Connection con = MySQLConnection.getConnection();

    @Override
    public Role make(ResultSet reS) {
        Role role = null;

        try {
            role = new Role(
                reS.getString("roleId"),
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
        return role;
    }


    @Override
    public void add(Object entity) {
        Role role = (Role) entity;
        String sql = "INSERT INTO roles (roleId, title, slug, description, isDeleted, createdAt, updatedAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, role.getRoleId());
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
    public Role getById(String id) {
        String sql = "SELECT * FROM roles WHERE roleId = ?";
        Role role = null;
        
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, id);
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

    @Override
    public List<Role> getAll() {
        String sql = "SELECT * FROM roles";
        List<Role> roles = new ArrayList<>();
        
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

    @Override
    public List<Role> findByName(String name) {
        String sql = "SELECT * FROM roles WHERE title LIKE ?";
        List<Role> roles = new ArrayList<>();
        
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

    @Override
    public void update(Object entity) {
        Role role = (Role) entity;
        String sql = "UPDATE roles SET title = ?, slug = ?, description = ?, isDeleted = ?, " +
                     "createdAt = ?, updatedAt = ? WHERE roleId = ?";
        
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, role.getTitle());
            prS.setString(2, role.getSlug());
            prS.setString(3, role.getDescription());
            prS.setString(4, role.getIsDeleted().toString());
            prS.setTimestamp(5, Timestamp.valueOf(role.getCreatedAt()));
            prS.setTimestamp(6, Timestamp.valueOf(role.getUpdatedAt()));
            prS.setString(7, role.getRoleId());
            
            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM roles WHERE roleId = ?";
        
        try (PreparedStatement prS = con.prepareStatement(sql)) {
            prS.setString(1, id);

            prS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object save(Object entity) {
        Role role = (Role) entity;
        
        if (getById(role.getRoleId()) != null) {
            update(role);
        } else {
            add(role);
        }
        
        return role;
    }
    
}
