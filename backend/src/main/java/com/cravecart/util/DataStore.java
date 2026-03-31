package com.cravecart.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cravecart.model.Category;
import com.cravecart.model.Product;
import com.cravecart.model.Inventory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Component
public class DataStore {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    // In-memory storage
    public static List<Category> categories = Collections.synchronizedList(new ArrayList<>());
    public static List<Product> products = Collections.synchronizedList(new ArrayList<>());
    public static List<Inventory> inventories = Collections.synchronizedList(new ArrayList<>());
    public static Map<String, List<Object>> cartItems = Collections.synchronizedMap(new HashMap<>()); // userId -> list of cart items
    public static List<Object> orders = Collections.synchronizedList(new ArrayList<>());

    static {
        loadDataFromJson();
    }

    private static void loadDataFromJson() {
        try {
            InputStream inputStream = DataStore.class.getResourceAsStream("/sample-data.json");
            Map<String, Object> data = objectMapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
            
            // Load categories
            if (data.containsKey("categories")) {
                List<Map<String, Object>> catList = (List<Map<String, Object>>) data.get("categories");
                for (Map<String, Object> catMap : catList) {
                    Category cat = objectMapper.convertValue(catMap, Category.class);
                    categories.add(cat);
                }
            }
            
            // Load products
            if (data.containsKey("products")) {
                List<Map<String, Object>> prodList = (List<Map<String, Object>>) data.get("products");
                for (Map<String, Object> prodMap : prodList) {
                    Product prod = objectMapper.convertValue(prodMap, Product.class);
                    products.add(prod);
                }
            }
            
            // Load inventories
            if (data.containsKey("inventories")) {
                List<Map<String, Object>> invList = (List<Map<String, Object>>) data.get("inventories");
                for (Map<String, Object> invMap : invList) {
                    Inventory inv = objectMapper.convertValue(invMap, Inventory.class);
                    inventories.add(inv);
                }
            }
            
            System.out.println("✓ Data loaded from JSON: " + categories.size() + " categories, " + products.size() + " products, " + inventories.size() + " inventories");
        } catch (Exception e) {
            System.err.println("Error loading data from JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void clear() {
        categories.clear();
        products.clear();
        inventories.clear();
        cartItems.clear();
        orders.clear();
        loadDataFromJson();
    }
}
