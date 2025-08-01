package com.senolight.InventoryManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.senolight.InventoryManagementSystem.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findBySku(String sku);
}