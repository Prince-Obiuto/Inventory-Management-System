package com.senolight.InventoryManagementSystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.senolight.InventoryManagementSystem.model.Product;
import com.senolight.InventoryManagementSystem.model.Sales;
import com.senolight.InventoryManagementSystem.repository.ProductRepository;
import com.senolight.InventoryManagementSystem.repository.SalesRepository;

@ExtendWith(MockitoExtension.class)
public class SalesServiceTest {

    @Mock
    private SalesRepository salesRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SalesService salesService;

    private Product testProduct;
    private Sales testSales;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setQuantity(100);
        testProduct.setPrice(25.99);

        testSales = new Sales();
        testSales.setId(1L);
        testSales.setProduct(testProduct);
        testSales.setQuantitySold(10);
        testSales.setTotalAmount(259.90);
        testSales.setTimeOfSale(LocalDateTime.now());
    }

    @Test
    void testRecordSales_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(salesRepository.save(any(Sales.class))).thenReturn(testSales);

        // When
        Sales result = salesService.recordSales(1L, 10);

        // Then
        assertNotNull(result);
        assertEquals(90, testProduct.getQuantity()); // Original 100 - 10 sold
        verify(productRepository).save(testProduct);
        verify(salesRepository).save(any(Sales.class));
    }

    @Test
    void testRecordSales_ProductNotFound_ThrowsException() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> salesService.recordSales(1L, 10));
        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void testRecordSales_InsufficientStock_ThrowsException() {
        // Given
        testProduct.setQuantity(5); // Less than requested quantity
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> salesService.recordSales(1L, 10));
        assertTrue(exception.getMessage().contains("Insufficient stock"));
    }

    @Test
    void testGetSalesByTimeRange() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<Sales> sales = List.of(testSales);
        when(salesRepository.findByTimeOfSaleBetween(start, end)).thenReturn(sales);

        // When
        List<Sales> result = salesService.getSalesByTimeRange(start, end);

        // Then
        assertEquals(1, result.size());
        assertEquals(testSales, result.get(0));
        verify(salesRepository).findByTimeOfSaleBetween(start, end);
    }

    @Test
    void testGetSaleById_Found() {
        // Given
        when(salesRepository.findById(1L)).thenReturn(Optional.of(testSales));

        // When
        Sales result = salesService.getSaleById(1L);

        // Then
        assertEquals(testSales, result);
        verify(salesRepository).findById(1L);
    }

    @Test
    void testGetSaleById_NotFound_ThrowsException() {
        // Given
        when(salesRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> salesService.getSaleById(1L));
        assertEquals("Sale not found", exception.getMessage());
    }
}