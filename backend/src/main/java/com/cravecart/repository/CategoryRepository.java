package com.cravecart.repository;

import com.cravecart.model.Category;
import com.cravecart.util.DataStore;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CategoryRepository {
    
    public List<Category> findAll() {
        return new ArrayList<>(DataStore.categories);
    }

    public Optional<Category> findById(String id) {
        return DataStore.categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public Category save(Category category) {
        if (category.getId() == null) {
            category.setId(UUID.randomUUID().toString());
        }
        
        DataStore.categories.removeIf(c -> c.getId().equals(category.getId()));
        DataStore.categories.add(category);
        return category;
    }

    public void deleteById(String id) {
        DataStore.categories.removeIf(c -> c.getId().equals(id));
    }

    public void deleteAll() {
        DataStore.categories.clear();
    }

    public List<Category> saveAll(List<Category> categories) {
        for (Category cat : categories) {
            save(cat);
        }
        return categories;
    }
}
