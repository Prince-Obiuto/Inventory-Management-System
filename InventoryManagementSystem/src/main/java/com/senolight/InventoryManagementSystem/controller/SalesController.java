package.com.senolight.InventoryManagementSystem.controller;

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
        Sale sale = salesService.getSaleById(saleId);
        ByteArrayInputStream invoiceStream = InvoiceService.generateInvoice(sale);

        byte[] pdfBytes = invoiceStream.readAllBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + saleId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}