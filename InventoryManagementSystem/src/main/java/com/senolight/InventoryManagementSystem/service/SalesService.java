package com.senolight.InventoryManagementSystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.senolight.InventoryManagementSystem.model.Product;
import com.senolight.InventoryManagementSystem.model.Sales;
import com.senolight.InventoryManagementSystem.repository.ProductRepository;
import com.senolight.InventoryManagementSystem.repository.SalesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesRepository salesRepository;
    private final ProductRepository productRepository;

    //Create and record a sale
    public Sales recordSales(Long productId, int quantitySold) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < quantitySold) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        //Update inventory and product stock
        product.setQuantity(product.getQuantity() - quantitySold);
        productRepository.save(product);

        double totalAmount = product.getPrice() * quantitySold;
        Sales sales = Sales.builder()
                .product(product)
                .quantitySold(quantitySold)
                .totalAmount(totalAmount)
                .timeOfSale(LocalDateTime.now())
                .build();

        return salesRepository.save(sales);
    }

    //Get all sales in a specific time range
    public List<Sales> getSalesByTimeRange(LocalDateTime start, LocalDateTime end) {
        return salesRepository.findByTimeOfSaleBetween(start, end);
    }

    public Sales getSaleById(Long id) {
        return salesRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found"));
    }
}