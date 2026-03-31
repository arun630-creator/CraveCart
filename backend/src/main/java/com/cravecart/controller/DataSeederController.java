package com.cravecart.controller;

import com.cravecart.util.DataStore;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seed")
@CrossOrigin(origins = "*")
public class DataSeederController {

    @PostMapping("/init")
    public String initializeDatabase() {
        try {
            // Data is already loaded from JSON in DataStore
            // Just return the status
            int categoryCount = DataStore.categories.size();
            int productCount = DataStore.products.size();
            int inventoryCount = DataStore.inventories.size();
            
            return "Database initialized successfully! " + categoryCount + " categories, " + productCount + " products, " + inventoryCount + " inventories loaded.";
        } catch (Exception e) {
            return "Error initializing database: " + e.getMessage();
        }
    }
}
