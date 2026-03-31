package com.cravecart.model;

import lombok.Data;

@Data
public class Inventory {
    private String id;
    private String productId;
    private Integer quantity;
}
