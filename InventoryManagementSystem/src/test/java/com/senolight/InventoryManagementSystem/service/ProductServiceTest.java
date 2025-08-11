package com.senolight.InventoryManagementSystem.service;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.senolight.InventoryManagementSystem.model.Product;
import com.senolight.InventoryManagementSystem.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setSku("TEST-001");
        testProduct.setPrice(100.0);
        testProduct.setQuantity(5);
    }

    @Test
    void testAddProduct_Success() {
        // Given
        when(productRepository.findBySku("TEST-001")).thenReturn(null);
        when(productRepository.save(testProduct)).thenReturn(testProduct);

        // When
        Product savedProduct = productService.addProduct(testProduct);

        // Then
        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getName());
        verify(productRepository).findBySku("TEST-001");
        verify(productRepository).save(testProduct);
    }

    @Test
    void testAddProduct_DUplicateSku_ThrowsExcpetion() {
        // Given
        when(productRepository.findBySku("TEST-001")).thenReturn(testProduct);

        // When & Then
        RuntimeException e = assertThrows(RuntimeException.class, () ->
            productService.addProduct(testProduct));
        assertEquals("Product With SKU already exits", e.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void testGetAllProducts() {
        // Given
        List<Product> productList = List.of(testProduct);
        when(productRepository.findAll()).thenReturn(productList);

        // When
        List<Product> products = productService.getAllProducts();

        // Then
        assertEquals(1, products.size());
        assertEquals(testProduct, products.get(0));
        verify(productRepository).findAll();
    }

    @Test
    void testGetProductById_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When
        Product product = productService.getProductById(1L);

        // Then
        assertNotNull(product);
        assertEquals(testProduct, product);
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException e = assertThrows(RuntimeException.class, () ->
            productService.getProductById(1L));
        assertEquals("Product not found", e.getMessage());
    }

    @Test
    void testUpdateProduct_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(testProduct)).thenReturn(testProduct);

        // When
        Product updatedProduct = productService.updateQuantity(1L, 10);

        // Then
        assertEquals(90, updatedProduct.getQuantity());
        verify(productRepository).save(testProduct);
    }

    @Test
    void testGetProductBySku() {
        // Given
        when(productRepository.findBySku("TEST-001")).thenReturn(testProduct);

        // When
        Product result = productService.getProductBySku("TEST-001");

        // Then
        assertEquals(testProduct, result);
        verify(productRepository).findBySku("TEST-001");
    }

    @Test
    void testDeleteProduct() {
        // When
        productService.deleteProduct(1L);

        // Then
        verify(productRepository).deleteById(1L);
    }
}