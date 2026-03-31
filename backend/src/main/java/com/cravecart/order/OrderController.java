package com.cravecart.order;

import com.cravecart.order.dto.OrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<Order> placeOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.placeOrder(request.getUserId()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrders(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.getOrders(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }
}
