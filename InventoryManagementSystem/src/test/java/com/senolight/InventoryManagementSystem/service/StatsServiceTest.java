package com.senolight.InventoryManagementSystem.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.senolight.InventoryManagementSystem.repository.SalesRepository;


@ExtendWith(MockitoExtension.class)
class StatsServiceTest {
    @Mock
    private SalesRepository salesRepository;

    @InjectMocks
    private StatsService statsService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCalculateTotalSales() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        BigDecimal expectedTotal = new BigDecimal("1000.00");
        when(salesRepository.calculateTotalAmountBetween(start, end)).thenReturn(expectedTotal);

        // When
        BigDecimal result = statsService.calculateTotalSales(start, end);

        // Then
        assertEquals(expectedTotal, result);
        verify(salesRepository).calculateTotalAmountBetween(start, end);
    }

    @Test
    void testSumQuantitySold() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        Long expectedQuantity = 50L;
        when(salesRepository.sumQuantitySoldBetween(start, end)).thenReturn(expectedQuantity);

        // When
        Long result = statsService.sumQuantitySold(start, end);

        // Then
        assertEquals(expectedQuantity, result);
        verify(salesRepository).sumQuantitySoldBetween(start, end);
    }

    @Test
    void testGetDailySaleAmount() {
        // Given
        BigDecimal expectedAmount = new BigDecimal("500.00");
        when(salesRepository.calculateTotalAmountBetween(any(), any())).thenReturn(expectedAmount);

        // When
        BigDecimal result = statsService.getDailySaleAmount();

        // Then
        assertEquals(expectedAmount, result);
        verify(salesRepository).calculateTotalAmountBetween(any(), any());
    }

    @Test
    void testGetDailyQuantitySold() {
        // Given
        Long expectedQuantity = 25L;
        when(salesRepository.sumQuantitySoldBetween(any(), any())).thenReturn(expectedQuantity);

        // When
        Long result = statsService.getDailyQuantitySold();

        // Then
        assertEquals(expectedQuantity, result);
        verify(salesRepository).sumQuantitySoldBetween(any(), any());
    }

    @Test
    void testGetWeeklySaleAmount() {
        // Given
        BigDecimal expectedAmount = new BigDecimal("2500.00");
        when(salesRepository.calculateTotalAmountBetween(any(), any())).thenReturn(expectedAmount);

        // When
        BigDecimal result = statsService.getWeeklySaleAmount();

        // Then
        assertEquals(expectedAmount, result);
        verify(salesRepository).calculateTotalAmountBetween(any(), any());
    }

    @Test
    void testGetMonthlySaleAmount() {
        // Given
        BigDecimal expectedAmount = new BigDecimal("10000.00");
        when(salesRepository.calculateTotalAmountBetween(any(), any())).thenReturn(expectedAmount);

        // When
        BigDecimal result = statsService.getMonthlySaleAmount();

        // Then
        assertEquals(expectedAmount, result);
        verify(salesRepository).calculateTotalAmountBetween(any(), any());
    }
}