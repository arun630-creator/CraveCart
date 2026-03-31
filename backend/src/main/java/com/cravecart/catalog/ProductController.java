package com.cravecart.catalog;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cravecart.common.exception.ResourceNotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<Product>> listProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        return ResponseEntity.ok(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> listByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(productRepository.findAll() // fallback if no direct category query is present
                .stream()
                .filter(product -> categoryId.equals(product.getCategoryId()))
                .toList());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest request) {
        if (request.getCategoryId() != null && !categoryRepository.existsById(request.getCategoryId())) {
            throw new ResourceNotFoundException("Category not found");
        }

        Product product = new Product(request.getName(), request.getDescription(), request.getPrice(), request.isAvailable(), request.getCategoryId());
        return ResponseEntity.ok(productRepository.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (request.getCategoryId() != null && !categoryRepository.existsById(request.getCategoryId())) {
            throw new ResourceNotFoundException("Category not found");
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setAvailable(request.isAvailable());
        product.setCategoryId(request.getCategoryId());

        return ResponseEntity.ok(productRepository.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
