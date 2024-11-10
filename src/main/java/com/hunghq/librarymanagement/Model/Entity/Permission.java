package com.hunghq.librarymanagement.Model.Entity;

import java.time.LocalDateTime;

import com.hunghq.librarymanagement.Model.Enum.EIsDeleted;

public class Permission {
    private String permissionId;
    private String title;
    private String slug;
    private String description;
    private EIsDeleted isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Permission() {;}

    public Permission(String permissionId, String title, String slug, String description, EIsDeleted isDeleted,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.permissionId = permissionId;
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public String getPermissionId() {
        return this.permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
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

    public EIsDeleted getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(EIsDeleted isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Permission[permissionId=" + permissionId
                + ", title" + title
                + ", slug" + slug
                + ", description" + description
                + ", isDeleted" + isDeleted
                + ", createdAt" + createdAt
                + ", updatedAt" + updatedAt
                + "]";
    }
}
