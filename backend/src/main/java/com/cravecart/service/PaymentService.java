package com.cravecart.service;

import com.cravecart.model.Order;
import com.cravecart.model.Payment;
import com.cravecart.repository.OrderRepository;
import com.cravecart.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Payment createPayment(String orderId, String userId, Double amount, String paymentMethod) {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString());
        payment.setOrderId(orderId);
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("PENDING");
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public Payment processPayment(String paymentId, String transactionId, boolean isSuccessful) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));

        if (isSuccessful) {
            payment.setStatus("COMPLETED");
            payment.setTransactionId(transactionId);

            // Update order status
            Order order = orderRepository.findById(payment.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            order.setPaymentStatus("COMPLETED");
            order.setTransactionId(transactionId);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
        } else {
            payment.setStatus("FAILED");
        }

        payment.setUpdatedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));
    }

    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}
