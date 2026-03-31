package com.cravecart.repository;

import com.cravecart.model.Payment;
import com.cravecart.util.DataStore;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PaymentRepository {
    
    public List<Payment> findAll() {
        return DataStore.orders.stream()
                .filter(o -> o instanceof Payment)
                .map(o -> (Payment)o)
                .collect(Collectors.toList());
    }

    public Optional<Payment> findById(String id) {
        return findAll().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Optional<Payment> findByOrderId(String orderId) {
        return findAll().stream()
                .filter(p -> p.getOrderId() != null && p.getOrderId().equals(orderId))
                .findFirst();
    }

    public List<Payment> findByUserId(String userId) {
        return findAll().stream()
                .filter(p -> p.getUserId() != null && p.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Optional<Payment> findByTransactionId(String transactionId) {
        return findAll().stream()
                .filter(p -> p.getTransactionId() != null && p.getTransactionId().equals(transactionId))
                .findFirst();
    }

    public Payment save(Payment payment) {
        if (payment.getId() == null) {
            payment.setId(UUID.randomUUID().toString());
        }
        
        DataStore.orders.removeIf(o -> o instanceof Payment && ((Payment)o).getId().equals(payment.getId()));
        DataStore.orders.add(payment);
        return payment;
    }

    public void deleteById(String id) {
        DataStore.orders.removeIf(o -> o instanceof Payment && ((Payment)o).getId().equals(id));
    }

    public void deleteAll() {
        DataStore.orders.removeIf(o -> o instanceof Payment);
    }

    public List<Payment> saveAll(List<Payment> payments) {
        for (Payment payment : payments) {
            save(payment);
        }
        return payments;
    }
}
