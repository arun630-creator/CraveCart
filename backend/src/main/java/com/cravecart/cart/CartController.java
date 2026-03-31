package com.cravecart.cart;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cravecart.cart.dto.CartItemRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItem(@Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(request.getUserId(), request.getProductId(), request.getQuantity()));
    }

    @PutMapping("/items")
    public ResponseEntity<Cart> updateItem(@Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.updateItem(request.getUserId(), request.getProductId(), request.getQuantity()));
    }

    @DeleteMapping("/items/{userId}/{productId}")
    public ResponseEntity<Cart> removeItem(@PathVariable String userId, @PathVariable String productId) {
        return ResponseEntity.ok(cartService.removeItem(userId, productId));
    }
}
