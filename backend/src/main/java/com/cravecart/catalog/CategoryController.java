package com.cravecart.catalog;

import java.time.Instant;
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
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<Category>> listCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable String id) {
        return ResponseEntity.ok(categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = new Category(request.getName(), request.getDescription(), request.isActive());
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable String id, @Valid @RequestBody CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.isActive());
        category.setUpdatedAt(Instant.now());

        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(category);
        return ResponseEntity.noContent().build();
    }
}
