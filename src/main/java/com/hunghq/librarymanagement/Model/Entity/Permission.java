package com.hunghq.librarymanagement.Model.Entity;

import com.hunghq.librarymanagement.Model.Enum.EIsDeleted;

import java.time.LocalDateTime;

public class Permission {
    private int id;
    private String title;
    private String slug;
    private String description;
    private EIsDeleted isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Permission() {
        ;
    }

    public Permission(int id, String title, String slug, String description,
                      EIsDeleted isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EIsDeleted getActive() {
        return isDeleted;
    }

    public void setActive(EIsDeleted isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Permission[id=" + id
                + ", title=" + title
                + ", slug=" + slug
                + ", description=" + description
                + ", isDeleted=" + isDeleted
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + "]";
    }
}
