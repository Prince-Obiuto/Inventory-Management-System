package com.senolight.InventoryManagementSystem.views;

import com.senolight.InventoryManagementSystem.model.Product;
import com.senolight.InventoryManagementSystem.model.Sales;
import com.senolight.InventoryManagementSystem.service.ProductService;
import com.senolight.InventoryManagementSystem.service.SalesService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@PageTitle("Sales")
@Route(value = "sales", layout = MainLayout.class)
@PermitAll
public class SalesView extends Div {

    private Grid<Sales> grid = new Grid<>(Sales.class, false);
    private DatePicker fromDate = new DatePicker("From Date");
    private DatePicker toDate = new DatePicker("To Date");
    private Button filterButton = new Button("Filter", VaadinIcon.FILTER.create());
    private Button recordSaleButton = new Button("Record Sale", VaadinIcon.PLUS.create());

    private final SalesService salesService;
    private final ProductService productService;

    public SalesView(@Autowired SalesService salesService, @Autowired ProductService productService) {
        this.salesService = salesService;
        this.productService = productService;

        addClassName("sales-view");
        setSizeFull();

        configureGrid();
        add(getToolbar(), grid);
        updateSalesList();
    }

    private void configureGrid() {
        grid.addClassName("sales-grid");
        grid.setSizeFull();

        grid.addColumn(Sales::getId).setHeader("Sale ID").setAutoWidth(true);
        grid.addColumn(sale -> sale.getProduct().getName()).setHeader("Product").setAutoWidth(true);
        grid.addColumn(Sales::getQuantitySold).setHeader("Quantity").setAutoWidth(true);
        grid.addColumn(sale -> "₦" + String.format("%.2f", sale.getTotalAmount())).setHeader("Total Amount").setAutoWidth(true);
        grid.addColumn(sale -> sale.getTimeOfSale().format(DateTimeFormatter.ofPattern("dd/mm/yyyy hh:mm")))
                .setHeader("Date/Time").setAutoWidth(true);

        grid.addColumn(new ComponentRenderer<>(Sale -> {
            Anchor downloadLink = new Anchor();
            downloadLink.getElement().setAttribute("download", true);
            downloadLink.setHref("/api/sales/invoice/" + sale.getId());

            Button downloadButton = new Button("Download Invoice", VaadinIcon.DOWNLOAD.create());
            downloadButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            downloadLink.add(downloadButton);

            return downloadLink;
        })).setHeader("Invoice").setAutoWidth(true);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    private Component getToolbar() {
        fromDate.setValue(LocalDate.now().minusDays(7));
        toDate.setValue(LocalDate.now());

        filterButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        filterButton.addClickListener(e -> filterSales());

        recordSaleButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        recordSaleButton.addClickListener(e -> openRecordSaleDialog());

        HorizontalLayout dateFilter = new HorizontalLayout(fromDate, toDate, filterButton);
        dateFilter.setAlignItems(FlexComponent.Alignment.END);

        return new HorizontalLayout(dateFilter, recordSaleButton);
    }

    private void filterSales() {
        if (fromDate.getValue() != null && toDate.getValue() != null) {
            LocalDateTime start = fromDate.getValue().atStartOfDay();
            LocalDateTime end = toDate.getValue().atTime(23, 59, 59);
            List<Sales> filteredSales = salesService.getSalesByTimeRange(start, end);
            grid.setItems(filteredSales);
        } else {
            updateSalesList();
        }
    }

    private void updateSalesList() {
        // Get sales from the last 30 days by default
        LocalDateTime start = LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        grid.setItems(salesService.getSalesByTimeRange(start, end));
    }

    private void openRecordSaleDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Record New Sale");

        // Form components
        ComboBox<Product> productCombo = new ComboBox<>("Product");
        productCombo.setItems(productService.getAllProducts());
        productCombo.setItemLabelGenerator(product -> product.getName() + " - ₦" + product.getPrice());
        productCombo.setWidthFull();

        IntegerField quantityField = new IntegerField("Quantity");
        quantityField.setMin(1);
        quantityField.setHasControls(true);
        quantityField.setWidthFull();

        TextField totalField = new TextField("Total Amount");
        totalField.setReadOnly(true);
        totalField.setPrefixComponent(new com.vaadin.flow.component.html.Span("₦"));
        totalField.setWidthFull();

        // Calculate total when product or quantity changes
        Runnable updateTotal = () -> {
            Product selectedProduct = productCombo.getValue();
            Integer quantity = quantityField.getValue();
            if (selectedProduct != null && quantity != null && quantity > 0) {
                double total = selectedProduct.getPrice() * quantity;
                totalField.setValue(String.format("%.2f", total));
            } else {
                totalField.setValue("");
            }
        };

        productCombo.addValueChangeListener(e -> updateTotal.run());
        quantityField.addValueChangeListener(e -> updateTotal.run());

        FormLayout formLayout = new FormLayout(productCombo, quantityField, totalField);

        // Buttons
        Button saveButton = new Button("Record Sale", VaadinIcon.CHECK.create());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", VaadinIcon.CLOSE.create());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickListener(e -> {
            Product selectedProduct = productCombo.getValue();
            Integer quantity = quantityField.getValue();

            if (selectedProduct == null) {
                Notification.show("Please select a product")
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (quantity == null || quantity <= 0) {
                Notification.show("Please enter a valid quantity")
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (selectedProduct.getQuantity() < quantity) {
                Notification.show("Insufficient stock! Available: " + selectedProduct.getQuantity())
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                salesService.recordSales(selectedProduct.getId(), quantity);
                Notification.show("Sale recorded successfully!")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                dialog.close();
                updateSalesList();
            } catch (Exception ex) {
                Notification.show("Error recording sale: " + ex.getMessage())
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        cancelButton.addClickListener(e -> dialog.close());

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);

        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }
}
