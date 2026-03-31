package com.cravecart.repository;

import com.cravecart.model.Order;
import com.cravecart.util.DataStore;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {
    
    public List<Order> findAll() {
        return DataStore.orders.stream().map(o -> (Order)o).collect(Collectors.toList());
    }

    public Optional<Order> findById(String id) {
        return findAll().stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }

    public List<Order> findByUserId(String userId) {
        return findAll().stream()
                .filter(o -> o.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(UUID.randomUUID().toString());
        }
        
        DataStore.orders.removeIf(o -> ((Order)o).getId().equals(order.getId()));
        DataStore.orders.add(order);
        return order;
    }

    public void deleteById(String id) {
        DataStore.orders.removeIf(o -> ((Order)o).getId().equals(id));
    }

    public void deleteAll() {
        DataStore.orders.clear();
    }

    public List<Order> saveAll(List<Order> orders) {
        for (Order order : orders) {
            save(order);
        }
        return orders;
    }
}
