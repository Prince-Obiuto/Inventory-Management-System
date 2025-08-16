package com.senolight.InventoryManagementSystem.views;

import com.senolight.InventoryManagementSystem.service.StatsService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@PageTitle("Reports")
@Route(value = "reports", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class ReportsView extends Div {

    private final StatsService statsService;

    public ReportsView(@Autowired StatsService statsService) {
        this.statsService = statsService;

        addClassName("reports-view");
        setSizeFull();

        add(createHeader());
        add(createStatsCards());
        add(createCharts());
    }

    private Component createHeader() {
        H2 header = new H2("Sales Reports & Analytics");
        header.addClassNames(LumoUtility.Margin.Bottom.XLARGE, LumoUtility.Margin.Top.NONE);
        return header;
    }

    private Component createStatsCards() {
        HorizontalLayout cards = new HorizontalLayout();
        cards.addClassName("stats-cards");
        cards.setWidthFull();
        cards.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);

        VerticalLayout todayCard = createStatsCard(
                "Today's Performance",
                "Revenue: ₦" + statsService.getDailySaleAmount(),
                "Quantity Sold: " + (statsService.getDailyQuantitySold() != null ? statsService.getDailyQuantitySold() : 0),
                VaadinIcon.CALENDAR.create(),
                "var(--lumo-success-color)"
        );

        VerticalLayout weekCard = createStatsCard(
                "Weekly Performance",
                "Revenue: ₦" + statsService.getWeeklySaleAmount(),
                "Period: " + getWeekRange(),
                VaadinIcon.TRENDING_UP.create(),
                "var(--lumo-primary-color)"
        );

        VerticalLayout monthCard = createStatsCard(
                "Monthly Performance",
                "Revenue: ₦" + statsService.getMonthlySaleAmount(),
                "Month: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                VaadinIcon.CHART.create(),
                "var(--lumo-contrast-color)"
        );

        cards.add(todayCard, weekCard, monthCard);
        return cards;
    }

    private VerticalLayout createStatsCard(String title, String primaryStat, String secondaryStat, Icon icon, String iconColor) {
        icon.setColor(iconColor);
        icon.setSize("2em");

        H3 titleElement = new H3(title);
        titleElement.addClassNames(LumoUtility.Margin.NONE, LumoUtility.FontSize.MEDIUM);

        Span primarySpan = new Span(primaryStat);
        primarySpan.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.LARGE);

        Span secondarySpan = new Span(secondaryStat);
        secondarySpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        VerticalLayout content = new VerticalLayout(titleElement, primarySpan, secondarySpan);
        content.setPadding(false);
        content.setSpacing(false);

        HorizontalLayout cardLayout = new HorizontalLayout(icon, content);
        cardLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        cardLayout.addClassNames(
                LumoUtility.Padding.LARGE,
                LumoUtility.Border.ALL,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Background.BASE,
                "stats-card"
        );

        return new VerticalLayout(cardLayout);
    }

    private Component createCharts() {
        HorizontalLayout chartContainer = new HorizontalLayout();
        chartContainer.setSizeFull();
        chartContainer.add(createRevenueChart(), createSalesComparisonChart());
        return chartContainer;
    }

//    private Component createRevenueChart() {
//        Chart chart = new Chart(ChartType.AREASPLINE);
//        chart.setWidth("50%");
//
//        Configuration config = chart.getConfiguration();
//        config.getTitle().setText("Revenue Trend");
//
//        XAxis xAxis = config.getxAxis();
//        xAxis.setCategories("Today", "This Week", "This Month");
//
//        YAxis yAxis = config.getyAxis();
//        yAxis.getTitle().setText("Revenue (₦)");
//
//        DataSeries series = new DataSeries("Revenue");
//        series.add(new DataSeriesItem("Today", statsService.getDailySaleAmount()));
//        series.add(new DataSeriesItem("This Week", statsService.getWeeklySaleAmount()));
//        series.add(new DataSeriesItem("This Month", statsService.getMonthlySaleAmount()));
//
//        config.addSeries(series);
//
//        PlotOptionsAreaspline plotOptions = new PlotOptionsAreaspline();
//        plotOptions.setFillOpacity(0.3);
//        config.setPlotOptions(plotOptions);
//
//        return chart;
//    }
//
//    private Component createSalesComparisonChart() {
//        Chart chart = new Chart(ChartType.COLUMN);
//        chart.setWidth("50%");
//
//        Configuration config = chart.getConfiguration();
//        config.getTitle().setText("Sales Performance Comparison");
//
//        XAxis xAxis = config.getxAxis();
//        xAxis.setCategories("Daily", "Weekly", "Monthly");
//
//        YAxis yAxis = config.getyAxis();
//        yAxis.getTitle().setText("Amount");
//
//        // Revenue series
//        DataSeries revenueSeries = new DataSeries("Revenue (₦)");
//        revenueSeries.add(new DataSeriesItem("Daily", statsService.getDailySaleAmount()));
//        revenueSeries.add(new DataSeriesItem("Weekly", statsService.getWeeklySaleAmount()));
//        revenueSeries.add(new DataSeriesItem("Monthly", statsService.getMonthlySaleAmount()));
//
//        // Quantity series (scaled for visibility)
//        DataSeries quantitySeries = new DataSeries("Quantity Sold (x100)");
//        Long dailyQty = statsService.getDailyQuantitySold();
//        quantitySeries.add(new DataSeriesItem("Daily",
//                dailyQty != null ? dailyQty * 100 : 0));
//        // Note: You might want to add weekly and monthly quantity methods to StatsService
//        quantitySeries.add(new DataSeriesItem("Weekly", 0));
//        quantitySeries.add(new DataSeriesItem("Monthly", 0));
//
//        config.addSeries(revenueSeries);
//        config.addSeries(quantitySeries);
//
//        return chart;
//    }

    private Component createRevenueChart() {
        Div container = new Div();
        container.setWidth("50%");
        container.getElement().setProperty("innerHTML",
                "<canvas id='revenueChart'></canvas>" +
                        "<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>"
        );

        BigDecimal daily = statsService.getDailySaleAmount();
        BigDecimal weekly = statsService.getWeeklySaleAmount();
        BigDecimal monthly = statsService.getMonthlySaleAmount();

        container.getElement().executeJs(
                """
                const ctx = document.getElementById('revenueChart').getContext('2d');
                new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: ['Today', 'This Week', 'This Month'],
                        datasets: [{
                            label: 'Revenue (₦)',
                            data: [$0, $1, $2],
                            backgroundColor: 'rgba(54, 162, 235, 0.3)',
                            borderColor: 'rgba(54, 162, 235, 1)',
                            fill: true,
                            tension: 0.4
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: { title: { display: true, text: 'Revenue Trend' } },
                        scales: { y: { title: { display: true, text: 'Revenue (₦)' } } }
                    }
                });
                """,
                daily, weekly, monthly
        );

        return container;
    }

    private Component createSalesComparisonChart() {
        Div container = new Div();
        container.setWidth("50%");
        container.getElement().setProperty("innerHTML",
                "<canvas id='salesComparisonChart'></canvas>" +
                        "<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>"
        );

        BigDecimal dailyRevenue = statsService.getDailySaleAmount();
        BigDecimal weeklyRevenue = statsService.getWeeklySaleAmount();
        BigDecimal monthlyRevenue = statsService.getMonthlySaleAmount();
        Long dailyQty = statsService.getDailyQuantitySold();
        long scaledDailyQty = dailyQty != null ? dailyQty * 100 : 0;

        container.getElement().executeJs(
                """
                const ctx = document.getElementById('salesComparisonChart').getContext('2d');
                new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: ['Daily', 'Weekly', 'Monthly'],
                        datasets: [
                            {
                                label: 'Revenue (₦)',
                                data: [$0, $1, $2],
                                backgroundColor: 'rgba(75, 192, 192, 0.7)'
                            },
                            {
                                label: 'Quantity Sold (x100)',
                                data: [$3, 0, 0],
                                backgroundColor: 'rgba(255, 159, 64, 0.7)'
                            }
                        ]
                    },
                    options: {
                        responsive: true,
                        plugins: { title: { display: true, text: 'Sales Performance Comparison' } },
                        scales: { y: { title: { display: true, text: 'Amount' } } }
                    }
                });
                """,
                dailyRevenue, weeklyRevenue, monthlyRevenue, scaledDailyQty
        );

        return container;
    }

    private String getWeekRange() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
        return startOfWeek.format(formatter) + " - " + endOfWeek.format(formatter);
    }
}
