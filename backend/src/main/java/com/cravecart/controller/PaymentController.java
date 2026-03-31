package com.cravecart.controller;

import com.cravecart.model.Payment;
import com.cravecart.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public Payment createPayment(@RequestBody Map<String, Object> request) {
        String orderId = (String) request.get("orderId");
        String userId = (String) request.get("userId");
        Double amount = ((Number) request.get("amount")).doubleValue();
        String paymentMethod = (String) request.get("paymentMethod");

        return paymentService.createPayment(orderId, userId, amount, paymentMethod);
    }

    @PostMapping("/{paymentId}/confirm")
    public Payment confirmPayment(@PathVariable String paymentId, @RequestBody Map<String, Object> request) {
        String transactionId = (String) request.get("transactionId");
        boolean isSuccessful = (boolean) request.get("isSuccessful");

        return paymentService.processPayment(paymentId, transactionId, isSuccessful);
    }

    @GetMapping("/order/{orderId}")
    public Payment getPaymentByOrderId(@PathVariable String orderId) {
        return paymentService.getPaymentByOrderId(orderId);
    }

    @GetMapping("/{paymentId}")
    public Payment getPayment(@PathVariable String paymentId) {
        return paymentService.getPayment(paymentId);
    }
}
