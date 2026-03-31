package com.cravecart.controller;

import com.cravecart.model.Inventory;
import com.cravecart.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @GetMapping("/{productId}")
    public Inventory getProductInventory(@PathVariable String productId) {
        return inventoryRepository.findByProductId(productId).orElse(null);
    }
}
