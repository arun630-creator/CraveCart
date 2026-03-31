package com.cravecart.order;

import com.cravecart.cart.Cart;
import com.cravecart.cart.CartRepository;
import com.cravecart.common.exception.BadRequestException;
import com.cravecart.common.exception.ResourceNotFoundException;
import com.cravecart.catalog.Product;
import com.cravecart.catalog.ProductRepository;
import com.cravecart.inventory.Inventory;
import com.cravecart.inventory.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        ProductRepository productRepository,
                        InventoryRepository inventoryRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public Order placeOrder(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cannot place order with empty cart");
        }

        Order order = new Order(userId, generateOrderNumber());

        for (var cartItem : cart.getItems()) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + cartItem.getProductId()));

            Inventory inventory = inventoryRepository.findByProductId(product.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory record not found for product: " + product.getId()));

            if (inventory.getQuantityAvailable() < cartItem.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }
            inventory.reserve(cartItem.getQuantity());
            inventoryRepository.save(inventory);

            OrderItem orderItem = new OrderItem(product.getId(), product.getName(), product.getPrice(), cartItem.getQuantity());
            order.addItem(orderItem);
        }

        order.calculateTotals();
        order.setStatus("CONFIRMED");
        order.setUpdatedAt(Instant.now());

        Order saved = orderRepository.save(order);
        cartRepository.delete(cart);
        return saved;
    }

    public List<Order> getOrders(String userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
    }

    private String generateOrderNumber() {
        return "CC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
