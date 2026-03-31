package com.cravecart.cart;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);
    void deleteByUserId(String userId);
}
