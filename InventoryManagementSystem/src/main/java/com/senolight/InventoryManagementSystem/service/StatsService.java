package com.senolight.InventoryManagementSystem.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.senolight.InventoryManagementSystem.repository.SalesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final SalesRepository salesRepository;

    public BigDecimal calculateTotalSales(LocalDateTime start, LocalDateTime end) {
        return salesRepository.calculateTotalAmountBetween(start, end);
    }

    public Long sumQuantitySold(LocalDateTime start, LocalDateTime end) {
        return salesRepository.sumQuantitySoldBetween(start, end);
    }

    public BigDecimal getDailySaleAmount() {
        LocalDate today = LocalDate.now();
        return calculateTotalSales(today.atStartOfDay(), today.plusDays(1).atStartOfDay());
    }

    public Long getDailyQuantitySold() {
        LocalDate today = LocalDate.now();
        return sumQuantitySold(today.atStartOfDay(), today.plusDays(1).atStartOfDay());
    }

    public BigDecimal getWeeklySaleAmount() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        return calculateTotalSales(startOfWeek.atStartOfDay(), today.plusDays(1).atStartOfDay());
    }

    public BigDecimal getMonthlySaleAmount() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        return calculateTotalSales(startOfMonth.atStartOfDay(), today.plusDays(1).atStartOfDay());
    }
}
// This service provides methods to calculate total sales and quantity sold within a specified time range.
// It also includes convenience methods to get daily, weekly, and monthly sales statistics.