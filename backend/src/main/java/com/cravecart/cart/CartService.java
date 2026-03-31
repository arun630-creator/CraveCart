package com.cravecart.cart;

import com.cravecart.catalog.Product;
import com.cravecart.catalog.ProductRepository;
import com.cravecart.common.exception.BadRequestException;
import com.cravecart.common.exception.ResourceNotFoundException;
import com.cravecart.inventory.Inventory;
import com.cravecart.inventory.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public CartService(CartRepository cartRepository,
                       ProductRepository productRepository,
                       InventoryRepository inventoryRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Cart getCart(String userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart cart = new Cart(userId);
            cart.recalculateTotals();
            return cart;
        });
    }

    @Transactional
    public Cart addItem(String userId, String productId, int quantity) {
        if (quantity < 1) {
            throw new BadRequestException("Quantity must be at least 1");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.isAvailable()) {
            throw new BadRequestException("Product is not available");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product"));

        if (inventory.getQuantityAvailable() < quantity) {
            throw new BadRequestException("Not enough stock available");
        }

        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(userId));
        CartItem existingItem = cart.findItem(productId);

        if (existingItem != null) {
            existingItem.updateQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(productId, product.getName(), product.getPrice(), quantity);
            cart.addItem(newItem);
        }

        cart.recalculateTotals();
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateItem(String userId, String productId, int quantity) {
        if (quantity < 1) {
            throw new BadRequestException("Quantity must be at least 1");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem existingItem = cart.findItem(productId);
        if (existingItem == null) {
            throw new ResourceNotFoundException("Cart item not found");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product"));

        if (inventory.getQuantityAvailable() < quantity) {
            throw new BadRequestException("Not enough stock available");
        }

        existingItem.updateQuantity(quantity);
        cart.recalculateTotals();
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItem(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.removeItem(productId);
        cart.recalculateTotals();
        if (!cart.hasItems()) {
            cartRepository.delete(cart);
            return new Cart(userId);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
}
