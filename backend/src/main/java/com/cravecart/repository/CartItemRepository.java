package com.cravecart.repository;

import com.cravecart.model.CartItem;
import com.cravecart.util.DataStore;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CartItemRepository {
    
    public List<CartItem> findAll() {
        List<CartItem> allItems = new ArrayList<>();
        for (List<Object> items : DataStore.cartItems.values()) {
            allItems.addAll(items.stream().map(o -> (CartItem)o).collect(Collectors.toList()));
        }
        return allItems;
    }

    public Optional<CartItem> findById(String id) {
        return findAll().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public List<CartItem> findByUserId(String userId) {
        List<Object> items = DataStore.cartItems.getOrDefault(userId, new ArrayList<>());
        return items.stream().map(o -> (CartItem)o).collect(Collectors.toList());
    }

    public CartItem save(CartItem cartItem) {
        if (cartItem.getId() == null) {
            cartItem.setId(UUID.randomUUID().toString());
        }
        
        List<Object> userCart = DataStore.cartItems.computeIfAbsent(cartItem.getUserId(), k -> Collections.synchronizedList(new ArrayList<>()));
        userCart.removeIf(c -> ((CartItem)c).getId().equals(cartItem.getId()));
        userCart.add(cartItem);
        return cartItem;
    }

    public void deleteById(String id) {
        for (List<Object> items : DataStore.cartItems.values()) {
            items.removeIf(c -> ((CartItem)c).getId().equals(id));
        }
    }

    public void deleteByUserId(String userId) {
        DataStore.cartItems.remove(userId);
    }

    public void deleteAll() {
        DataStore.cartItems.clear();
    }

    public List<CartItem> saveAll(List<CartItem> items) {
        for (CartItem item : items) {
            save(item);
        }
        return items;
    }
}
