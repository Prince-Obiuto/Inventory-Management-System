package com.senolight.InventoryManagementSystem.integration;

import com.senolight.InventoryManagementSystem.model.Product;
import com.senolight.InventoryManagementSystem.model.Sales;
import com.senolight.InventoryManagementSystem.service.ProductService;
import com.senolight.InventoryManagementSystem.service.SalesService;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class InventoryManagementIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private SalesService salesService;

    @Test
    void testCompleteInventoryFlow() {
        // Create a product
        Product product = new Product();
        product.setName("Integration Test Product");
        product.setSku("INT-001");
        product.setQuantity(100);
        product.setPrice(50.0);

        Product savedProduct = productService.addProduct(product);
        assertNotNull(savedProduct.getId());

        // Record a sale
        Sales sale = salesService.recordSales(savedProduct.getId(), 10);
        assertNotNull(sale.getId());
        assertEquals(10, sale.getQuantitySold());
        assertEquals(500.0, sale.getTotalAmount());

        // Verify inventory was updated
        Product updatedProduct = productService.getProductById(savedProduct.getId());
        assertEquals(90, updatedProduct.getQuantity());
    }
}
