package com.cravecart.service;

import com.cravecart.model.CartItem;
import com.cravecart.model.Inventory;
import com.cravecart.model.Product;
import com.cravecart.repository.CartItemRepository;
import com.cravecart.repository.InventoryRepository;
import com.cravecart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    public CartItem addToCart(String userId, String productId, Integer quantity) {
        // Validate product exists and has stock
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        if (inventory.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + inventory.getQuantity());
        }

        // Check if item already in cart
        List<CartItem> existingItems = cartItemRepository.findByUserId(userId).stream()
                .filter(item -> item.getProductId().equals(productId))
                .toList();

        CartItem cartItem;
        if (!existingItems.isEmpty()) {
            cartItem = existingItems.get(0);
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
        }

        cartItem.setPrice(product.getPrice());
        cartItem.setProductName(product.getName());
        cartItem.setProductImage(product.getImageUrl());

        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public CartItem updateQuantity(String itemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + itemId));

        if (quantity <= 0) {
            cartItemRepository.deleteById(itemId);
            return item;
        }

        // Validate stock
        Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (inventory.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + inventory.getQuantity());
        }

        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    public void removeFromCart(String itemId) {
        cartItemRepository.deleteById(itemId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    public Double getCartTotal(String userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
