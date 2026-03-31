package com.cravecart.inventory;

import com.cravecart.common.exception.BadRequestException;
import com.cravecart.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Inventory getInventory(String productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory entry not found for product: " + productId));
    }

    @Transactional
    public Inventory adjustStock(String productId, int delta) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory entry not found for product: " + productId));

        int newStock = inventory.getQuantityAvailable() + delta;
        if (newStock < 0) {
            throw new BadRequestException("Insufficient inventory for product: " + productId);
        }
        inventory.setQuantityAvailable(newStock);
        inventory.setLastUpdated(java.time.Instant.now());
        return inventoryRepository.save(inventory);
    }
}
