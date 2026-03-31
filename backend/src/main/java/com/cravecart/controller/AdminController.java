package com.cravecart.controller;

import com.cravecart.model.Category;
import com.cravecart.model.Product;
import com.cravecart.model.Inventory;
import com.cravecart.repository.CategoryRepository;
import com.cravecart.repository.ProductRepository;
import com.cravecart.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        
        // Initialize inventory
        Inventory inventory = new Inventory();
        inventory.setProductId(savedProduct.getId());
        inventory.setQuantity(0); // default stock
        inventoryRepository.save(inventory);

        return savedProduct;
    }

    @PutMapping("/inventory/{productId}")
    public Inventory updateInventory(@PathVariable String productId, @RequestParam Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseGet(() -> {
                Inventory inv = new Inventory();
                inv.setProductId(productId);
                return inv;
            });
        inventory.setQuantity(quantity);
        return inventoryRepository.save(inventory);
    }
}
