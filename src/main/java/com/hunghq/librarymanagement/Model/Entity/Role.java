package com.hunghq.librarymanagement.Model.Entity;

import java.time.LocalDateTime;

public class Role {

    private int roleId;
    private String title;
    private String slug;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String content;
    private boolean isDeleted;

    public int getRoleId() {
        return this.roleId;
    }

    public void setRoleId(int roleId) {
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

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Role() {
        this.isDeleted = false;
    }

    public Role(int roleId, String title, String slug, String description,
    LocalDateTime createdAt, LocalDateTime updatedAt, String content, boolean isDeleted) {
        this.roleId = roleId;
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public String toString() {
        return "Role[roleId=" + roleId
        + ",title" + title
        + ",slug" + slug
        + ",description" + description
        + ",createdAt" + createdAt
        + ",updatedAt" + updatedAt
        + ",content" + content
        + ",isDeleted" + isDeleted
        + "]";
    }


}
