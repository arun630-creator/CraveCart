package com.cravecart.order.dto;

import jakarta.validation.constraints.NotBlank;

public class OrderRequest {

    @NotBlank
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
