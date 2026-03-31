package com.cravecart.catalog;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findBySlug(String slug);
}
