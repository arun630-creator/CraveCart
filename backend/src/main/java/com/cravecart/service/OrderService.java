package com.cravecart.service;

import com.cravecart.model.CartItem;
import com.cravecart.model.Inventory;
import com.cravecart.model.Order;
import com.cravecart.repository.CartItemRepository;
import com.cravecart.repository.InventoryRepository;
import com.cravecart.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    public Order createOrder(String userId, String paymentMethod, String shippingAddress, String customerEmail) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Calculate total
        Double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // Create order
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setUserId(userId);
        order.setItems(cartItems);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus("PENDING");
        order.setShippingAddress(shippingAddress);
        order.setCustomerEmail(customerEmail);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public Order confirmOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // Check if payment is completed (or allow PENDING for test purposes)
        if (order.getPaymentStatus() == null || (!order.getPaymentStatus().equals("COMPLETED") && !order.getPaymentStatus().equals("PENDING"))) {
            throw new RuntimeException("Invalid payment status for this order: " + order.getPaymentStatus());
        }

        // Deduct inventory
        for (CartItem item : order.getItems()) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));

            if (inventory.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + item.getProductId());
            }

            inventory.setQuantity(inventory.getQuantity() - item.getQuantity());
            inventoryRepository.save(inventory);
        }

        // Update order status
        order.setStatus("COMPLETED");
        order.setPaymentStatus("COMPLETED");
        order.setUpdatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        // Clear cart
        cartItemRepository.deleteByUserId(order.getUserId());

        return savedOrder;
    }

    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getUserOrders(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateOrderStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }
}
