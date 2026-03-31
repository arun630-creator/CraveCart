package com.cravecart.catalog;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "categories")
public class Category {

    @Id
    private String id;
    private String name;
    private String slug;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public Category() {
    }

    public Category(String name, String description, boolean active) {
        this.name = name;
        this.slug = createSlug(name);
        this.description = description;
        this.active = active;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    private String createSlug(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.slug = createSlug(name);
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
