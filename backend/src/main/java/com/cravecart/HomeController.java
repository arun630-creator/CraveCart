package com.cravecart;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> home() {
        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "message", "Welcome to CraveCart API",
                "endpoints", Map.of(
                        "products", "/api/products",
                        "categories", "/api/categories",
                        "cart", "/api/cart/{userId}",
                        "orders", "/api/orders"
                )
        ));
    }
}

