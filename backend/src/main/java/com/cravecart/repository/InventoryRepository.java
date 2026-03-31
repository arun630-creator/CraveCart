package com.cravecart.repository;

import com.cravecart.model.Inventory;
import com.cravecart.util.DataStore;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InventoryRepository {
    
    public List<Inventory> findAll() {
        return new ArrayList<>(DataStore.inventories);
    }

    public Optional<Inventory> findById(String id) {
        return DataStore.inventories.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }

    public Optional<Inventory> findByProductId(String productId) {
        return DataStore.inventories.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();
    }

    public Inventory save(Inventory inventory) {
        if (inventory.getId() == null) {
            inventory.setId(UUID.randomUUID().toString());
        }
        
        DataStore.inventories.removeIf(i -> i.getId().equals(inventory.getId()));
        DataStore.inventories.add(inventory);
        return inventory;
    }

    public void deleteById(String id) {
        DataStore.inventories.removeIf(i -> i.getId().equals(id));
    }

    public void deleteAll() {
        DataStore.inventories.clear();
    }

    public List<Inventory> saveAll(List<Inventory> inventories) {
        for (Inventory inv : inventories) {
            save(inv);
        }
        return inventories;
    }
}
