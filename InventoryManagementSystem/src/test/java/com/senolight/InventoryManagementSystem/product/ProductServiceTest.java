package com.senolight.InventoryManagementSystem.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.senolight.InventoryManagementSystem.model.Product;
import com.senolight.InventoryManagementSystem.repository.ProductRepository;
import com.senolight.InventoryManagementSystem.service.ProductService;

public class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setup() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void testGetAllProducts() {
        List<Product> mockProducts = List.of(new Product(1L, "Test Product", 20, 200.0));
        when(productRepository.findAll()).thenReturn(mockProducts);

        List<Product> result = productService.getAllProducts();
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        Product product = new Product(1L, "Test", 10, 99.99);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getName());
    }

    @Test
    void testSaveProduct() {
        Product product = new Product(null, "New Product", 5, 50.0);
        when(productRepository.save(product)).thenReturn(product);

        Product saved = productService.addProduct(product);
        assertEquals("New Product", saved.getName());
    }
}
