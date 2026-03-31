package com.cravecart.repository;

import com.cravecart.model.Product;
import com.cravecart.util.DataStore;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductRepository {
    
    public List<Product> findAll() {
        return new ArrayList<>(DataStore.products);
    }

    public Optional<Product> findById(String id) {
        return DataStore.products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public List<Product> findByCategoryId(String categoryId) {
        return DataStore.products.stream()
                .filter(p -> p.getCategoryId().equals(categoryId))
                .collect(java.util.stream.Collectors.toList());
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID().toString());
        }
        
        // Remove if exists and re-add
        DataStore.products.removeIf(p -> p.getId().equals(product.getId()));
        DataStore.products.add(product);
        return product;
    }

    public void deleteById(String id) {
        DataStore.products.removeIf(p -> p.getId().equals(id));
    }

    public void deleteAll() {
        DataStore.products.clear();
    }

    public List<Product> saveAll(List<Product> products) {
        for (Product product : products) {
            save(product);
        }
        return products;
    }
}
