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
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class DashBoardView extends Main {

    private final StatsService statsService;
    private final ProductService productService;

    public DashBoardView(@Autowired StatsService statsService, @Autowired ProductService productService) {
        this.statsService = statsService;
        this.productService = productService;

        addClassName("dashboard-view");
        setHeightFull();

        Board board = new Board();
        board.addRow(createHighlight("Today's Revenue", "₦" + statsService.getDailyQuantitySold(), VaadinIcon.MONEY.create()),
                createHighlight("Weekly Revenue", "₦" + statsService.getWeeklySaleAmount(),VaadinIcon.TRENDING_UP.create()),
                createHighlight("Monthly Revenue", "₦" + statsService.getMonthlySaleAmount(),VaadinIcon.CHART.create()),
                createHighlight("Products in Stock", String.valueOf(productService.getAllProducts().size()), VaadinIcon.PACKAGE.create()));

        board.addRow(createRevenueChart());
        add(board);
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

//        Chart chart = new Chart(ChartType.COLUMN);
//
//        Configuration configuration = chart.getConfiguration();
//        configuration.getTitle().setText("Sales Overview");
//
//        XAxis xAxis = configuration.getxAxis();
//        xAxis.setCategories("Today", "This Week", "This Month");
//
//        YAxis yAxis = configuration.getyAxis();
//        yAxis.getTitle().setText("Revenue (₦)");
//
//        DataSeries series = new DataSeries("Revenue");
//        series.add(new DataSeriesItem("Today", statsService.getDailySaleAmount()));
//        series.add(new DataSeriesItem("This Week", statsService.getWeeklySaleAmount()));
//        series.add(new DataSeriesItem("This Month", statsService.getMonthlySaleAmount()));
//
//        configuration.addSeries(series);
//        return chart;

        // Using a Div to hold the Chart.js canvas
        Div container = new Div();
        container.getElement().setProperty("innerHTML", "<canvas id='revenueChart'></canvas>");
        container.setWidthFull();

        // Get your values from the service
        BigDecimal todayRevenue = statsService.getDailySaleAmount();
        BigDecimal weekRevenue = statsService.getWeeklySaleAmount();
        BigDecimal monthRevenue = statsService.getMonthlySaleAmount();

        // Inject Chart.js script
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
                                    data: [%s, %s, %s],
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
                """, todayRevenue, weekRevenue, monthRevenue);

        return container;
    }
}