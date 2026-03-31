package com.cravecart.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    private String id;
    private String userId;
    private List<CartItem> items;
    private Double totalAmount;
    private String status; // e.g., PENDING, PROCESSING, COMPLETED, CANCELLED
    private String paymentMethod; // STRIPE, PAYPAL, CASH_ON_DELIVERY
    private String paymentStatus; // PENDING, COMPLETED, FAILED
    private String transactionId; // Payment gateway transaction ID
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String shippingAddress;
    private String customerEmail;
}
