package com.kotojava.lesson3.service;

import com.kotojava.lesson3.model.LoanApplication;
import com.kotojava.lesson3.model.Status;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class LoanPdfService {

    static {
        System.setProperty("java.awt.headless", "true");
    }

    public byte[] generatePdfFile(LoanApplication loanApplication) {
        if (loanApplication.getStatus() == Status.APPROVED) {
            return generateApprovedPdf(loanApplication);
        } else if (loanApplication.getStatus() == Status.REJECTED) {
            return generateRejectedPdf(loanApplication);
        }

        throw new IllegalStateException("PDF can be generated only for APPROVED applications");
    }

    private byte[] generateRejectedPdf(LoanApplication loanApplication) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);

            Paragraph message = new Paragraph("Sorry loan wasn't offered to you " + loanApplication.getFirstName(), font);
            message.setAlignment(Element.ALIGN_CENTER);
            document.add(message);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating rejected PDF", e);
        }
    }

    private byte[] generateApprovedPdf(LoanApplication loanApplication) {
        BigDecimal monthlyPayment = loanApplication.getExpectedMonthlyPayment();
        int paymentYears = loanApplication.getLoanTermYears();
        int totalMonths = paymentYears * 12;
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(totalMonths));
        LocalDate startDate = LocalDate.now();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 40, 40, 50, 50);
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

            Paragraph title = new Paragraph("Loan Offer", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            Paragraph offer = new Paragraph(
                    "We are pleased to offer " + loanApplication.getFirstName() + " "
                            + loanApplication.getLastName() + " the following loan:", boldFont);
            offer.setSpacingAfter(15);
            document.add(offer);

            document.add(new Paragraph("Application ID: " + loanApplication.getId(), textFont));
            document.add(new Paragraph("Property price: " + money(loanApplication.getPropertyPrice()), textFont));
            document.add(new Paragraph("First payment: " + money(loanApplication.getFirstPayment()), textFont));
            document.add(new Paragraph("Loan term: " + paymentYears + " years (" + totalMonths + " months)", textFont));
            document.add(new Paragraph("Monthly payment: " + money(monthlyPayment), textFont));
            document.add(new Paragraph("Total amount to repay: " + money(totalAmount), textFont));
            document.add(new Paragraph("Start date: " + startDate, textFont));

            if (loanApplication.getAnalystComments() != null && !loanApplication.getAnalystComments().isBlank()) {
                Paragraph comments = new Paragraph("Analyst comments: " + loanApplication.getAnalystComments(), textFont);
                comments.setSpacingBefore(10);
                document.add(comments);
            }

            Paragraph chartTitle = new Paragraph("Remaining balance by month", boldFont);
            chartTitle.setSpacingBefore(20);
            chartTitle.setSpacingAfter(10);
            document.add(chartTitle);

            byte[] chartPng = buildChartPng(totalAmount, monthlyPayment, totalMonths, startDate);
            Image chartImage = Image.getInstance(chartPng);
            chartImage.scaleToFit(PageSize.A4.getWidth() - 80, 350);
            chartImage.setAlignment(Element.ALIGN_CENTER);
            document.add(chartImage);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("error while generating pdf: " + e.getMessage());
        }
    }

    private byte[] buildChartPng(BigDecimal totalAmount, BigDecimal monthlyPayment,
                                 int totalMonths, LocalDate startDate) throws IOException {
        TimeSeries series = new TimeSeries("Remaining balance");
        BigDecimal remaining = totalAmount;

        // month 0 = totalAmount, then it decreases by monthlyPayment every month
        for (int i = 0; i <= totalMonths; i++) {
            LocalDate date = startDate.plusMonths(i);
            series.add(new Month(date.getMonthValue(), date.getYear()), remaining.doubleValue());
            remaining = remaining.subtract(monthlyPayment).max(BigDecimal.ZERO);
        }

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                null, "Month", "Remaining amount",
                new TimeSeriesCollection(series), false, false, false);

        BufferedImage image = chart.createBufferedImage(900, 500);
        ByteArrayOutputStream png = new ByteArrayOutputStream();
        ChartUtils.writeBufferedImageAsPNG(png, image);
        return png.toByteArray();
    }

    private String money(BigDecimal value) {
        return value == null ? "-" : value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}