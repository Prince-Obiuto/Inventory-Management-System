package com.senolight.InventoryManagementSystem.service;

@Service
public class InvoiceService {
    public ByteArrayInputStream generateInvoice(Sale sale) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Invoice content
        document.add(new Paragraph("INVOICE")
            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(20));

        document.add(new Paragraph("Product:" + sale.getProduct().getName()));
        document.add(new Paragraph("Quantity:" + sale.getQuantitySold()));
        document.add(new Paragraph("Unit Price: ₦" + sale.getProduct().getPrice()));
        document.add(new Paragraph("Total Amount: ₦ " + sale.getTotalAmount()));
        document.add(new Paragraph("Date/Time of Sale: " + sale.getTimeOfSale().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))));

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}