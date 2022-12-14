package com.svj.service.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.svj.model.Order;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
@ConditionalOnProperty(
        value="report_format",
        havingValue = "pdf")
public class PDFReportService implements reportService{

    public byte[] generateReport(List<Order> orders) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Chunk chunk = new Chunk("Order list for "+new Date(), font);
            document.addTitle("Order list for "+new Date());
            document.add(chunk);
            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(4);
            addTableHeader(table);
            orders.stream().forEach(order -> addRows(table, order));
            document.add(table);

            document.close();
            Path pdfPath = Paths.get("iTextHelloWorld.pdf");
            return Files.readAllBytes(pdfPath);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("ORDER_ID", "ORDER_NAME", "QUANTITY", "PRICE")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, Order order) {
        table.addCell(String.valueOf(order.getId()) );
        table.addCell(order.getName());
        table.addCell(String.valueOf(order.getQty()) );
        table.addCell(String.valueOf(order.getPrice()) );
    }
}
