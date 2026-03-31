package com.cravecart.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Payment {
    private String id;
    private String orderId;
    private String userId;
    private Double amount;
    private String paymentMethod; // STRIPE, PAYPAL, CASH_ON_DELIVERY
    private String status; // PENDING, COMPLETED, FAILED
    private String transactionId; // Payment gateway transaction ID
    private String paymentGatewayResponse; // Response from payment provider
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
