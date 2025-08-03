package com.senolight.InventoryManagementSystem.repository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}