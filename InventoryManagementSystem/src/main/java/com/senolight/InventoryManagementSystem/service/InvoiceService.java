package com.senolight.InventoryManagementSystem.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.senolight.InventoryManagementSystem.model.Sales;

@Service
public class InvoiceService {
    public ByteArrayInputStream generateInvoice(Sales sales) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        // Invoice content
        try (Document document = new Document(pdfDoc)) {
            // Invoice content
            document.add(new Paragraph("INVOICE")
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                    .setFontSize(20));
            
            document.add(new Paragraph("Product:" + sales.getProduct().getName()));
            document.add(new Paragraph("Quantity:" + sales.getQuantitySold()));
            document.add(new Paragraph("Unit Price: ₦" + sales.getProduct().getPrice()));
            document.add(new Paragraph("Total Amount: ₦ " + sales.getTotalAmount()));
            document.add(new Paragraph("Date/Time of Sale: " + sales.getTimeOfSale().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))));
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}