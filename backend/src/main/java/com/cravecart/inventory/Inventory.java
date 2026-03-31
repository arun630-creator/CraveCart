package com.cravecart.inventory;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "inventory")
public class Inventory {

    @Id
    private String id;
    private String productId;
    private int quantityAvailable;
    private int quantityReserved;
    private Instant lastUpdated;

    public Inventory() {
    }

    public Inventory(String productId, int quantityAvailable) {
        this.productId = productId;
        this.quantityAvailable = quantityAvailable;
        this.quantityReserved = 0;
        this.lastUpdated = Instant.now();
    }

    public void reserve(int amount) {
        this.quantityAvailable -= amount;
        this.quantityReserved += amount;
        this.lastUpdated = Instant.now();
    }

    public void release(int amount) {
        this.quantityAvailable += amount;
        this.quantityReserved = Math.max(0, this.quantityReserved - amount);
        this.lastUpdated = Instant.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public int getQuantityReserved() {
        return quantityReserved;
    }

    public void setQuantityReserved(int quantityReserved) {
        this.quantityReserved = quantityReserved;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
