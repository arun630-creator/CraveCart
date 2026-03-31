package com.cravecart.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cravecart.catalog.CategoryRepository;
import com.cravecart.catalog.Product;
import com.cravecart.catalog.ProductRepository;
import com.cravecart.order.OrderRepository;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;

    public AdminDashboardController(ProductRepository productRepository,
                                    CategoryRepository categoryRepository,
                                    OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummary> getSummary() {
        DashboardSummary summary = new DashboardSummary();
        summary.setProductCount(productRepository.count());
        summary.setCategoryCount(categoryRepository.count());
        summary.setOrderCount(orderRepository.count());
        summary.setTotalRevenue(orderRepository.findAll().stream().mapToDouble(order -> order.getTotal()).sum());
        summary.setAvailableProductCount(productRepository.findAll().stream().filter(Product::isAvailable).count());
        summary.setUnavailableProductCount(productRepository.findAll().stream().filter(product -> !product.isAvailable()).count());
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/recent-orders")
    public ResponseEntity<List<?>> getRecentOrders() {
        return ResponseEntity.ok(orderRepository.findAll().stream()
                .sorted((a, b) -> b.getOrderDate().compareTo(a.getOrderDate()))
                .limit(10)
                .toList());
    }

    @GetMapping("/categories")
    public ResponseEntity<Map<String, Long>> getCategoryCounts() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("categoryCount", categoryRepository.count());
        return ResponseEntity.ok(counts);
    }
}
