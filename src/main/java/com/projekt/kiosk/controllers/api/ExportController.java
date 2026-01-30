package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.dao.SalesStatsDao;
import com.projekt.kiosk.dto.stats.SalesStatsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/export")
@Slf4j
public class ExportController {

    private final SalesStatsDao salesStatsDao;

    public ExportController(SalesStatsDao salesStatsDao) {
        this.salesStatsDao = salesStatsDao;
    }

    @Operation(summary = "Export sales statistics as CSV", description = "Downloads sales statistics for a date range as a CSV file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CSV file generated successfully")
    })
    @GetMapping("/stats/csv")
    public ResponseEntity<byte[]> exportStatsCsv(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        if (from == null) {
            from = LocalDate.now().minusDays(30);
        }
        if (to == null) {
            to = LocalDate.now();
        }

        log.info("Exporting stats CSV from {} to {}", from, to);

        List<SalesStatsDto> stats = salesStatsDao.getDailyStatsInRange(from, to);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        writer.println("Date,Order Count,Revenue (cents),Revenue (formatted)");

        for (SalesStatsDto stat : stats) {
            writer.printf("%s,%d,%d,%s%n",
                    stat.getDate(),
                    stat.getOrderCount(),
                    stat.getTotalRevenueCents(),
                    stat.getFormattedRevenue());
        }

        writer.flush();
        byte[] csvBytes = out.toByteArray();

        String filename = String.format("sales_stats_%s_to_%s.csv", from, to);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBytes);
    }

    @Operation(summary = "Export sales statistics as PDF", description = "Downloads sales statistics for a date range as a PDF file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PDF file generated successfully")
    })
    @GetMapping("/stats/pdf")
    public ResponseEntity<byte[]> exportStatsPdf(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        if (from == null) {
            from = LocalDate.now().minusDays(30);
        }
        if (to == null) {
            to = LocalDate.now();
        }

        log.info("Exporting stats PDF from {} to {}", from, to);

        List<SalesStatsDto> stats = salesStatsDao.getDailyStatsInRange(from, to);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            com.lowagie.text.Document document = new com.lowagie.text.Document();
            com.lowagie.text.pdf.PdfWriter.getInstance(document, out);

            document.open();

            com.lowagie.text.Font titleFont = com.lowagie.text.FontFactory
                    .getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 18);
            com.lowagie.text.Paragraph title = new com.lowagie.text.Paragraph("SALES STATISTICS REPORT", titleFont);
            title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            document.add(title);

            document.add(new com.lowagie.text.Paragraph(" ")); // Spacer
            document.add(new com.lowagie.text.Paragraph("Period: " + from + " to " + to));
            document.add(new com.lowagie.text.Paragraph(" ")); // Spacer

            com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(3);
            table.setWidthPercentage(100);

            table.addCell("Date");
            table.addCell("Orders");
            table.addCell("Revenue");

            int totalOrders = 0;
            long totalRevenue = 0;

            for (SalesStatsDto stat : stats) {
                table.addCell(stat.getDate().toString());
                table.addCell(String.valueOf(stat.getOrderCount()));
                table.addCell(stat.getFormattedRevenue());

                totalOrders += stat.getOrderCount();
                totalRevenue += stat.getTotalRevenueCents();
            }

            com.lowagie.text.pdf.PdfPCell totalCell = new com.lowagie.text.pdf.PdfPCell(
                    new com.lowagie.text.Phrase("TOTAL"));
            table.addCell(totalCell);

            table.addCell(String.valueOf(totalOrders));
            table.addCell(String.format("%.2f", totalRevenue / 100.0));

            document.add(table);
            document.close();

            byte[] pdfBytes = out.toByteArray();
            String filename = String.format("sales_stats_%s_to_%s.pdf", from, to);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            log.error("Error generating PDF", e);
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
