package com.senolight.InventoryManagementSystem.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.senolight.InventoryManagementSystem.model.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    //All sales for a specific product
    List<Sales> findByProductId(Long productId);

    //All sales within a specific time range
    List<Sales> findByTimeOfSaleBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sales s WHERE s.timeOfSale BETWEEN :start AND :end")
    BigDecimal calculateTotalAmountBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(s.quantitySold, 0) FROM Sales s WHERE s.timeOfSale BETWEEN :start AND :end")
    Long sumQuantitySoldBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}