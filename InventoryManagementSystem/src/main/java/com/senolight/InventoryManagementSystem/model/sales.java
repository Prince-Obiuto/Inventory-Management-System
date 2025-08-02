package com.senolight.InventoryManagementSystem.model;

@Entity
// Getters and Setters using Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private LocalDateTime timeOfSale;

    public Sales() {
    }

    public Sales(Product product, int quantitySold, double totalAmount, LocalDateTime timeOfSale) {
        this.product = product;
        this.quantitySold = quantitySold;
        this.totalAmount = totalAmount;
        this.timeOfSale = timeOfSale;
    }
}