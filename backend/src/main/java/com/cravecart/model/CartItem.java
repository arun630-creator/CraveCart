package com.cravecart.model;

import lombok.Data;

@Data
public class CartItem {
    private String id;
    private String userId;
    private String productId;
    private Integer quantity;
    private Double price; // Price at time of adding to cart
    private String productName; // Product name for quick reference
    private String productImage; // Product image URL
}
