package com.cravecart.model;

import lombok.Data;

@Data
public class Product {
    private String id;
    private String name;
    private String description;
    private Double price;
    private String categoryId;
    private String brand;
    private String imageUrl;
    private Double rating;
    private Integer reviews;
}
