package com.senolight.InventoryManagementSystem.controller;

import java.util.List;

import com.senolight.InventoryManagementSystem.security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senolight.InventoryManagementSystem.model.Product;
import com.senolight.InventoryManagementSystem.service.ProductService;

@WebMvcTest(ProductController.class)
@Import(TestSecurityConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setSku("TEST-001");
        testProduct.setQuantity(100);
        testProduct.setPrice(25.99);
    }

    @Test
    @WithMockUser
    void testAddProduct() throws Exception {
        // Given
        when(productService.addProduct(any(Product.class))).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(post("/api/products/add")
                        //.with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.sku").value("TEST-001"))
                .andExpect(jsonPath("$.quantity").value(100))
                .andExpect(jsonPath("$.price").value(25.99));

        verify(productService).addProduct(any(Product.class));
    }

    @Test
    @WithMockUser
    void testGetAllProducts() throws Exception {
        // Given
        List<Product> products = List.of(testProduct);
        when(productService.getAllProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Product"));

        verify(productService).getAllProducts();
    }

    @Test
    @WithMockUser
    void testGetProductById() throws Exception {
        // Given
        when(productService.getProductById(1L)).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService).getProductById(1L);
    }

    @Test
    @WithMockUser
    void testSellProduct() throws Exception {
        // Given
        testProduct.setQuantity(90);
        when(productService.updateQuantity(1L, 10)).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(put("/api/products/sell/1")
                        //.with(csrf())
                        .param("quantitySold", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(90));

        verify(productService).updateQuantity(1L, 10);
    }

    @Test
    @WithMockUser
    void testDeleteProduct() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/products/1"))
                //.with(csrf())
                .andExpect(status().isOk());

        verify(productService).deleteProduct(1L);
    }
}