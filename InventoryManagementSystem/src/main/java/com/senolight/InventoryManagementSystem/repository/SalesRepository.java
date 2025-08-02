package com.senolight.InventoryManagementSystem.repository;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    //All sales for a specific product
    List<Sales> findByProductId(Long productId);

    //All sales within a specific time range
    List<Sales> findByTimeOfSaleBetween(LocalDateTime start, LocalDateTime end);
}