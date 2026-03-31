package com.cravecart.order;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private String orderNumber;
    private String userId;
    private List<OrderItem> items = new ArrayList<>();
    private double subtotal;
    private double discount;
    private double tax;
    private double deliveryFee;
    private double total;
    private String status;
    private Instant orderDate;
    private Instant createdAt;
    private Instant updatedAt;

    public Order() {
    }

    public Order(String userId, String orderNumber) {
        this.userId = userId;
        this.orderNumber = orderNumber;
        this.status = "PENDING";
        this.orderDate = Instant.now();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void calculateTotals() {
        this.subtotal = items.stream().mapToDouble(OrderItem::getLineTotal).sum();
        this.discount = 0.0;
        this.tax = Math.round(this.subtotal * 0.08 * 100.0) / 100.0;
        this.deliveryFee = 0.0;
        this.total = Math.round((subtotal + tax + deliveryFee - discount) * 100.0) / 100.0;
        this.updatedAt = Instant.now();
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    // getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
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
