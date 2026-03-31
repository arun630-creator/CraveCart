package com.cravecart.catalog;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ProductRequest {

    @NotBlank
    private String name;

    private String description;

    @Min(value = 0, message = "Price must be non-negative")
    private double price;

    private boolean available;
    private String categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
