package com.senolight.InventoryManagementSystem.views;

import com.senolight.InventoryManagementSystem.service.ProductService;
import com.senolight.InventoryManagementSystem.service.StatsService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
//import com.vaadin.flow.component.charts.Chart;
//import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class DashBoardView extends Main {

    private static final Logger logger = LoggerFactory.getLogger(DashBoardView.class);

    private final StatsService statsService;
    private final ProductService productService;

    public DashBoardView(@Autowired StatsService statsService, @Autowired ProductService productService) {
        this.statsService = statsService;
        this.productService = productService;

        addClassName("dashboard-view");
        setHeightFull();

        try {
            // Safely fetch all values with defaults before building UI
            BigDecimal dailyRevenue = safeGetBigDecimal(() -> statsService.getDailySaleAmount());
            BigDecimal weeklyRevenue = safeGetBigDecimal(() -> statsService.getWeeklySaleAmount());
            BigDecimal monthlyRevenue = safeGetBigDecimal(() -> statsService.getMonthlySaleAmount());
            int productCount = safeGetProductCount();

            Board board = new Board();
            board.addRow(
                createHighlight("Today's Revenue", "₦" + dailyRevenue, VaadinIcon.MONEY.create()),
                createHighlight("Weekly Revenue", "₦" + weeklyRevenue, VaadinIcon.TRENDING_UP.create()),
                createHighlight("Monthly Revenue", "₦" + monthlyRevenue, VaadinIcon.CHART.create()),
                createHighlight("Products in Stock", String.valueOf(productCount), VaadinIcon.PACKAGE.create())
            );

            board.addRow(createRevenueChart());
            add(board);
        } catch (Exception e) {
            logger.error("Error initializing dashboard view", e);
            // Show a fallback UI
            Div errorDiv = new Div();
            errorDiv.setText("Dashboard is temporarily unavailable. Please try again later.");
            errorDiv.addClassName("error-message");
            add(errorDiv);
        }
    }

    private BigDecimal safeGetBigDecimal(java.util.function.Supplier<BigDecimal> supplier) {
        try {
            BigDecimal result = supplier.get();
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            logger.warn("Error fetching BigDecimal value, returning zero", e);
            return BigDecimal.ZERO;
        }
    }

    private int safeGetProductCount() {
        try {
            List<?> products = productService.getAllProducts();
            return products != null ? products.size() : 0;
        } catch (Exception e) {
            logger.warn("Error fetching product count, returning zero", e);
            return 0;
        }
    }

    private Component createHighlight(String title, String value, Icon icon) {
        VaadinIcon.DOLLAR.create().getStyle().set("color", "var(--lumo-success-color)");

        Span titleSpan = new Span(title);
        titleSpan.addClassNames(LumoUtility.FontWeight.MEDIUM, LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        Span valueSpan = new Span(value);
        valueSpan.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.XLARGE);

        VerticalLayout layout = new VerticalLayout(titleSpan, valueSpan);
        layout.addClassNames(LumoUtility.Padding.LARGE);
        layout.setPadding(false);
        layout.setSpacing(false);

        HorizontalLayout cardLayout = new HorizontalLayout(icon, layout);
        cardLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        cardLayout.addClassNames(LumoUtility.Border.ALL, LumoUtility.BorderRadius.LARGE, LumoUtility.Background.BASE);

        return cardLayout;
    }

    private Component createRevenueChart() {
        try {
            // Using a Div to hold the Chart.js canvas
            Div container = new Div();
            container.getElement().setProperty("innerHTML", "<canvas id='revenueChart'></canvas>");
            container.setWidthFull();

            // Get sanitized values from the service with safe defaults
            BigDecimal todayRevenue = safeGetBigDecimal(() -> statsService.getDailySaleAmount());
            BigDecimal weekRevenue = safeGetBigDecimal(() -> statsService.getWeeklySaleAmount());
            BigDecimal monthRevenue = safeGetBigDecimal(() -> statsService.getMonthlySaleAmount());

            // Convert to doubles for JS, ensuring they're never null
            double todayValue = todayRevenue.doubleValue();
            double weekValue = weekRevenue.doubleValue();
            double monthValue = monthRevenue.doubleValue();

            // Inject Chart.js script with sanitized numeric values
            container.getElement().executeJs("""
                        if (typeof Chart === 'undefined') {
                            var script = document.createElement('script');
                            script.src = 'https://cdn.jsdelivr.net/npm/chart.js';
                            script.onload = function() {
                                renderChart();
                            };
                            document.head.appendChild(script);
                        } else {
                            renderChart();
                        }
                    
                        function renderChart() {
                            const ctx = document.getElementById('revenueChart').getContext('2d');
                            new Chart(ctx, {
                                type: 'bar',
                                data: {
                                    labels: ['Today', 'This Week', 'This Month'],
                                    datasets: [{
                                        label: 'Revenue (₦)',
                                        data: [$0, $1, $2],
                                        backgroundColor: ['#4CAF50', '#2196F3', '#FFC107']
                                    }]
                                },
                                options: {
                                    responsive: true,
                                    scales: {
                                        y: {
                                            beginAtZero: true
                                        }
                                    }
                                }
                            });
                        }
                    """, todayValue, weekValue, monthValue);

            return container;
        } catch (Exception e) {
            logger.error("Error creating revenue chart", e);
            // Return a fallback component
            Div fallback = new Div();
            fallback.setText("Chart temporarily unavailable");
            fallback.addClassName("chart-fallback");
            return fallback;
        }
    }
}