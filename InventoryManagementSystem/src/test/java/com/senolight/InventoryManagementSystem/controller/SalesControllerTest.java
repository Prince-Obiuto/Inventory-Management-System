package com.senolight.InventoryManagementSystem.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.senolight.InventoryManagementSystem.security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.context.annotation.Import;

import com.senolight.InventoryManagementSystem.model.Product;
import com.senolight.InventoryManagementSystem.model.Sales;
import com.senolight.InventoryManagementSystem.service.SalesService;

@WebMvcTest(SalesController.class)
@Import(TestSecurityConfig.class)
class SalesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SalesService salesService;

    private Sales testSales;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(25.99);

        testSales = new Sales();
        testSales.setId(1L);
        testSales.setProduct(testProduct);
        testSales.setQuantitySold(10);
        testSales.setTotalAmount(259.90);
        testSales.setTimeOfSale(LocalDateTime.now());
    }

    @Test
    @WithMockUser
    void testRecordSales() throws Exception {
        // Given
        when(salesService.recordSales(1L, 10)).thenReturn(testSales);

        // When & Then
        mockMvc.perform(post("/api/sales/record")
                        //.with(csrf())
                        .param("productId", "1")
                        .param("quantitySold", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantitySold").value(10))
                .andExpect(jsonPath("$.totalAmount").value(259.90));

        verify(salesService).recordSales(1L, 10);
    }

    @Test
    @WithMockUser
    void testGetSalesByTimeRange() throws Exception {
        // Given
        List<Sales> sales = List.of(testSales);
        when(salesService.getSalesByTimeRange(any(), any())).thenReturn(sales);

        // When & Then
        mockMvc.perform(get("/api/sales/range")
                        .param("start", "2024-01-01T00:00:00")
                        .param("end", "2024-12-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].quantitySold").value(10));

        verify(salesService).getSalesByTimeRange(any(), any());
    }
}