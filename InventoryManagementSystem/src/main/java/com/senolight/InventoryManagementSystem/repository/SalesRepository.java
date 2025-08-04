package com.senolight.InventoryManagementSystem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senolight.InventoryManagementSystem.model.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    //All sales for a specific product
    List<Sales> findByProductId(Long productId);

    //All sales within a specific time range
    List<Sales> findByTimeOfSaleBetween(LocalDateTime start, LocalDateTime end);
}