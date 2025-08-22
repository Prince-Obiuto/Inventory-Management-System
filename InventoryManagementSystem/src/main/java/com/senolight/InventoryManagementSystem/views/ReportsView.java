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
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@PageTitle("Reports")
@Route(value = "reports", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class ReportsView extends Div {

    private static final Logger logger = LoggerFactory.getLogger(ReportsView.class);

    private final StatsService statsService;

    public ReportsView(@Autowired StatsService statsService) {
        this.statsService = statsService;

        addClassName("reports-view");
        setSizeFull();

        try {
            add(createHeader());
            add(createStatsCards());
            add(createCharts());
        } catch (Exception e) {
            logger.error("Error initializing reports view", e);
            // Show a fallback UI
            Div errorDiv = new Div();
            errorDiv.setText("Reports are temporarily unavailable. Please try again later.");
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

    private Long safeGetLong(java.util.function.Supplier<Long> supplier) {
        try {
            Long result = supplier.get();
            return result != null ? result : 0L;
        } catch (Exception e) {
            logger.warn("Error fetching Long value, returning zero", e);
            return 0L;
        }
    }

    private Component createHeader() {
        H2 header = new H2("Sales Reports & Analytics");
        header.addClassNames(LumoUtility.Margin.Bottom.XLARGE, LumoUtility.Margin.Top.NONE);
        return header;
    }

    private Component createStatsCards() {
        try {
            HorizontalLayout cards = new HorizontalLayout();
            cards.addClassName("stats-cards");
            cards.setWidthFull();
            cards.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);

            // Safely fetch all values with defaults
            BigDecimal dailyRevenue = safeGetBigDecimal(() -> statsService.getDailySaleAmount());
            Long dailyQuantity = safeGetLong(() -> statsService.getDailyQuantitySold());
            BigDecimal weeklyRevenue = safeGetBigDecimal(() -> statsService.getWeeklySaleAmount());
            BigDecimal monthlyRevenue = safeGetBigDecimal(() -> statsService.getMonthlySaleAmount());

            VerticalLayout todayCard = createStatsCard(
                    "Today's Performance",
                    "Revenue: ₦" + String.valueOf(dailyRevenue),
                    "Quantity Sold: " + String.valueOf(dailyQuantity),
                    VaadinIcon.CALENDAR.create(),
                    "var(--lumo-success-color)"
            );

            VerticalLayout weekCard = createStatsCard(
                    "Weekly Performance",
                    "Revenue: ₦" + String.valueOf(weeklyRevenue),
                    "Period: " + getWeekRange(),
                    VaadinIcon.TRENDING_UP.create(),
                    "var(--lumo-primary-color)"
            );

            VerticalLayout monthCard = createStatsCard(
                    "Monthly Performance",
                    "Revenue: ₦" + String.valueOf(monthlyRevenue),
                    "Month: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    VaadinIcon.CHART.create(),
                    "var(--lumo-contrast-color)"
            );

            cards.add(todayCard, weekCard, monthCard);
            return cards;
        } catch (Exception e) {
            logger.error("Error creating stats cards", e);
            Div fallback = new Div();
            fallback.setText("Stats cards temporarily unavailable");
            return fallback;
        }
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

//        quantitySeries.add(new DataSeriesItem("Weekly", 0));
//        quantitySeries.add(new DataSeriesItem("Monthly", 0));
//
//        config.addSeries(revenueSeries);
//        config.addSeries(quantitySeries);
//
//        return chart;
//    }

    private Component createRevenueChart() {
        try {
            Div container = new Div();
            container.setWidth("50%");
            container.getElement().setProperty("innerHTML",
                    "<canvas id='revenueChart'></canvas>" +
                            "<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>"
            );

            // Safely fetch values with defaults
            BigDecimal daily = safeGetBigDecimal(() -> statsService.getDailySaleAmount());
            BigDecimal weekly = safeGetBigDecimal(() -> statsService.getWeeklySaleAmount());
            BigDecimal monthly = safeGetBigDecimal(() -> statsService.getMonthlySaleAmount());

            // Convert to safe double values for JS
            double dailyValue = daily.doubleValue();
            double weeklyValue = weekly.doubleValue();
            double monthlyValue = monthly.doubleValue();

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
                    dailyValue, weeklyValue, monthlyValue
            );

            return container;
        } catch (Exception e) {
            logger.error("Error creating revenue chart", e);
            Div fallback = new Div();
            fallback.setText("Revenue chart temporarily unavailable");
            fallback.setWidth("50%");
            return fallback;
        }
    }

    private Component createSalesComparisonChart() {
        try {
            Div container = new Div();
            container.setWidth("50%");
            container.getElement().setProperty("innerHTML",
                    "<canvas id='salesComparisonChart'></canvas>");

            // Safely fetch values with defaults
            BigDecimal dailyRevenue = safeGetBigDecimal(() -> statsService.getDailySaleAmount());
            BigDecimal weeklyRevenue = safeGetBigDecimal(() -> statsService.getWeeklySaleAmount());
            BigDecimal monthlyRevenue = safeGetBigDecimal(() -> statsService.getMonthlySaleAmount());
            Long dailyQty = statsService.getDailyQuantitySold();
            Double dailyQuantity = dailyQty != null ? dailyQty.doubleValue() : 0.0;

            // Convert to safe values for JS
            double dailyRevenueValue = dailyRevenue.doubleValue();
            double weeklyRevenueValue = weeklyRevenue.doubleValue();
            double monthlyRevenueValue = monthlyRevenue.doubleValue();

            container.getElement().executeJs(
                    """
                    const ctx = document.getElementById('salesComparisonChart').getContext('2d');
                            if (!ctx) return;
                                             new Chart(ctx, {
                                               type: 'bar',
                                               data: {
                                                 labels: ['Daily', 'Weekly', 'Monthly'],
                                                 datasets: [
                                                   {
                                                     type: 'bar',
                                                     label: 'Revenue (₦)',
                                                     data: [$0, $1, $2],
                                                     backgroundColor: 'rgba(54, 162, 235, 0.5)',
                                                     borderColor: 'rgba(54, 162, 235, 1)',
                                                     borderWidth: 1,
                                                     yAxisID: 'y'
                                                   },
                                                   {
                                                     type: 'line',
                                                     label: 'Quantity Sold',
                                                     data: [$3, 0, 0],
                                                     backgroundColor: 'rgba(255, 99, 132, 0.5)',
                                                     borderColor: 'rgba(255, 99, 132, 1)',
                                                     borderWidth: 2,
                                                     tension: 0.3,
                                                     yAxisID: 'y1'
                                                   }
                                                 ]
                                               },
                                               options: {
                                                 responsive: true,
                                                 interaction: { mode: 'index', intersect: false },
                                                 scales: {
                                                   y: {
                                                     beginAtZero: true,
                                                     position: 'left',
                                                     title: { display: true, text: 'Revenue (₦)' }
                                                   },
                                                   y1: {
                                                     beginAtZero: true,
                                                     position: 'right',
                                                     grid: { drawOnChartArea: false },
                                                     title: { display: true, text: 'Quantity' }
                                                   }
                                                 },
                                                 plugins: {
                                                   legend: { position: 'top' },
                                                   tooltip: { enabled: true }
                                                 }
                                               }
                                             });
                                           };
                            
                                           if (typeof Chart === 'undefined') {
                                             const existing = Array.from(document.getElementsByTagName('script'))
                                               .some(s => s.src && s.src.includes('cdn.jsdelivr.net/npm/chart.js'));
                                             if (!existing) {
                                               const s = document.createElement('script');
                                               s.src = 'https://cdn.jsdelivr.net/npm/chart.js';
                                               s.onload = render;
                                               document.head.appendChild(s);
                                             } else {
                                               // Script tag exists but Chart not initialized yet; wait a tick
                                               setTimeout(render, 100);
                                             }
                                           } else {
                                             render();
                                           }
                    """,
                    dailyRevenueValue, weeklyRevenueValue, monthlyRevenueValue, dailyQuantity
            );

            return container;
        } catch (Exception e) {
            logger.error("Error creating sales comparison chart", e);
            Div fallback = new Div();
            fallback.setText("Sales comparison chart temporarily unavailable");
            fallback.getStyle().set("color", "var(--lumo-error-text-color)");
            return fallback;
        }
    }

    private String getWeekRange() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
            LocalDate endOfWeek = startOfWeek.plusDays(6);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
            return startOfWeek.format(formatter) + " - " + endOfWeek.format(formatter);
        } catch (Exception e) {
            logger.warn("Error calculating week range, using fallback", e);
            return "This Week";
        }
    }
}
