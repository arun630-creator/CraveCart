package com.cravecart.controller;

import com.cravecart.model.Product;
import com.cravecart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.cravecart.model.Inventory;
import com.cravecart.repository.InventoryRepository;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable String categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product, @RequestParam(defaultValue = "0") Integer initialStock) {
        Product savedProduct = productRepository.save(product);
        
        Inventory inventory = new Inventory();
        inventory.setProductId(savedProduct.getId());
        inventory.setQuantity(initialStock);
        inventoryRepository.save(inventory);

        return savedProduct;
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable String id, @RequestBody Product productDetails) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setCategoryId(productDetails.getCategoryId());
        product.setBrand(productDetails.getBrand());
        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productRepository.deleteById(id);
        // Also delete associated inventory
        inventoryRepository.findByProductId(id).ifPresent(inv -> inventoryRepository.deleteById(inv.getId()));
    }
}
