package com.senolight.InventoryManagementSystem.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;

    private String name;

    @Column(unique = true)
    private String sku;

    private int quantity;

    private double price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) {
        this.id = id; 
        }

    public String getName() {
        return name; 
        }
    public void setName(String name) { 
        this.name = name; 
    }

    public String getSku() { 
        return sku; 
    }
    public void setSku(String sku) { 
        this.sku = sku; 
    }

    public int getQuantity() { 
        return quantity; 
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity; 
    }

    public double getPrice() {
        return price; 
    }
    public void setPrice(double price) {
        this.price = price; 
    }

    public LocalDateTime getCreatedAt() {
        return createdAt; 
    }

    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }
}