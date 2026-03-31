package com.cravecart.controller;

import com.cravecart.model.CartItem;
import com.cravecart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public CartItem addToCart(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        String productId = (String) request.get("productId");
        Integer quantity = (Integer) request.get("quantity");
        return cartService.addToCart(userId, productId, quantity);
    }

    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable String userId) {
        return cartService.getCart(userId);
    }

    @GetMapping("/{userId}/total")
    public Double getCartTotal(@PathVariable String userId) {
        return cartService.getCartTotal(userId);
    }

    @PutMapping("/{itemId}")
    public CartItem updateQuantity(@PathVariable String itemId, @RequestParam Integer quantity) {
        return cartService.updateQuantity(itemId, quantity);
    }

    @DeleteMapping("/{itemId}")
    public void removeFromCart(@PathVariable String itemId) {
        cartService.removeFromCart(itemId);
    }

    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
    }
}
