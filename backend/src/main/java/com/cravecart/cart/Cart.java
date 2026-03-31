package com.cravecart.cart;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "carts")
public class Cart {

    @Id
    private String id;
    private String userId;
    private List<CartItem> items = new ArrayList<>();
    private double subtotal;
    private double tax;
    private double discount;
    private double total;
    private Instant createdAt;
    private Instant updatedAt;

    public Cart() {
    }

    public Cart(String userId) {
        this.userId = userId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void recalculateTotals() {
        this.subtotal = items.stream().mapToDouble(CartItem::getLineTotal).sum();
        this.tax = Math.round(this.subtotal * 0.08 * 100.0) / 100.0; // example 8% tax
        this.discount = 0.0;
        this.total = Math.round((subtotal + tax - discount) * 100.0) / 100.0;
        this.updatedAt = Instant.now();
    }

    public CartItem findItem(String productId) {
        return items.stream().filter(item -> item.getProductId().equals(productId)).findFirst().orElse(null);
    }

    public void addItem(CartItem item) {
        items.add(item);
    }

    public void removeItem(String productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    // getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
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
