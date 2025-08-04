package com.senolight.InventoryManagementSystem.controller;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.senolight.InventoryManagementSystem.model.Sales;
import com.senolight.InventoryManagementSystem.service.InvoiceService;
import com.senolight.InventoryManagementSystem.service.SalesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    // Record a sale
    @PostMapping("/record")
    public Sales recordSales(@RequestParam Long productId, @RequestParam int quantitySold) {
        return salesService.recordSales(productId, quantitySold);
    }

    // Get sales by time range
    @GetMapping("/range")
    public List<Sales> getSalesByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
            return salesService.getSalesByTimeRange(start, end);
    }

    @GetMapping("/invoice/{saleId}")
    public ResponseEntity<byte[]> generateInvoice(@PathVariable Long saleId) throws Exception {
        Sales sales = salesService.getSaleById(saleId);
        ByteArrayInputStream invoiceStream = InvoiceService.generateInvoice(sales);

        byte[] pdfBytes = invoiceStream.readAllBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + saleId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}