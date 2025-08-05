package com.senolight.InventoryManagementSystem.controller;

import java.math.BigDecimal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.senolight.InventoryManagementSystem.service.StatsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
// This controller handles statistics related to sales and inventory.
public class StatsController {
    private final StatsService statsService;

    // Get total sales amount in a day
    @GetMapping("/revenue/today")
    public BigDecimal getTodayRevenue() {
        return statsService.getDailySaleAmount();
    }

    // Get total quantity sold in a week
    @GetMapping("/revenue/week")
    public BigDecimal getWeeklyRevenue() {
        return statsService.getWeeklySaleAmount();
    }

    // Get total sales amount in a month
    @GetMapping("/revenue/month")
    public BigDecimal getMonthlyRevenue() {
        return statsService.getMonthlySaleAmount();
    }

    // Get total quantity sold in a day
    @GetMapping("/quantity/today")
    public Long getTodayQuantitySold() {
        return statsService.getDailyQuantitySold();
    }
}