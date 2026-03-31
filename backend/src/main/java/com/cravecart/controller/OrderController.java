package com.cravecart.controller;

import com.cravecart.model.Order;
import com.cravecart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Order createOrder(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        String paymentMethod = (String) request.get("paymentMethod");
        String shippingAddress = (String) request.get("shippingAddress");
        String customerEmail = (String) request.get("customerEmail");

        return orderService.createOrder(userId, paymentMethod, shippingAddress, customerEmail);
    }

    @PostMapping("/{orderId}/confirm")
    public Order confirmOrder(@PathVariable String orderId) {
        return orderService.confirmOrder(orderId);
    }

    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable String orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getUserOrders(@PathVariable String userId) {
        return orderService.getUserOrders(userId);
    }

    @PutMapping("/{orderId}/status")
    public Order updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        return orderService.updateOrderStatus(orderId, status);
    }
}
