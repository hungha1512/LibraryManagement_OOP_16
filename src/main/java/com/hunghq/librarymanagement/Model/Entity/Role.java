package com.hunghq.librarymanagement.Model.Entity;

import java.time.LocalDateTime;

public class Role {

<<<<<<< HEAD
    private String roleId;
=======
    private int roleId;
>>>>>>> cc31ce91e1f43171ca1bb986dee1f79adab998e0
    private String title;
    private String slug;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String content;
    private boolean isDeleted;

<<<<<<< HEAD
    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
=======
    public int getRoleId() {
        return this.roleId;
    }

    public void setRoleId(int roleId) {
>>>>>>> cc31ce91e1f43171ca1bb986dee1f79adab998e0
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

<<<<<<< HEAD
    public Role(String roleId, String title, String slug, String description,
    LocalDateTime createdAt, LocalDateTime updatedAt, String content,
    boolean isDeleted) {
=======
    public Role(int roleId, String title, String slug, String description,
    LocalDateTime createdAt, LocalDateTime updatedAt, String content, boolean isDeleted) {
>>>>>>> cc31ce91e1f43171ca1bb986dee1f79adab998e0
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
<<<<<<< HEAD
        + ", title" + title
        + ", slug" + slug
        + ", description" + description
        + ", createdAt" + createdAt
        + ", updatedAt" + updatedAt
        + ", content" + content
        + ", isDeleted" + isDeleted
=======
        + ",title" + title
        + ",slug" + slug
        + ",description" + description
        + ",createdAt" + createdAt
        + ",updatedAt" + updatedAt
        + ",content" + content
        + ",isDeleted" + isDeleted
>>>>>>> cc31ce91e1f43171ca1bb986dee1f79adab998e0
        + "]";
    }


}
