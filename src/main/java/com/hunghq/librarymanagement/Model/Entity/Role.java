package com.hunghq.librarymanagement.Model.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hunghq.librarymanagement.Model.Enum.EIsDeleted;

public class Role {
    private String roleId;
    private String title;
    private String slug;
    private String description;
    private EIsDeleted isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Permission> permissions;

    public Role() {
        ;
    }

    public Role(String roleId, String title, String slug, String description, EIsDeleted isDeleted,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.roleId = roleId;
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.permissions = new ArrayList<>();
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return this.slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EIsDeleted getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(EIsDeleted isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public String toString() {
        return "Role[" + title + "]";
    }


}
