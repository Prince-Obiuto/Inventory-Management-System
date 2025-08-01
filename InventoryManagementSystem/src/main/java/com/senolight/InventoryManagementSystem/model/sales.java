package com.senolight.InventoryManagementSystem.model;

@Entity
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Which product was sold
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantitySold;
    private double totalAmount;
}