package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
public class MainOOP {

    private static final Logger log = LoggerFactory.getLogger(MainOOP.class);

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            log.error("File path isn't specified! Usage: java Main <file> <type>");
            System.exit(1);
        }

        File file = new File(args[0]);
        if (!file.exists()) {
            log.error("File path isn't specified! Usage: java Main <file> <type>");
            System.exit(1);
        }

        SalaryRecordParser salaryRecordParser = null;

        if (args[1].equals("XML")) {
            salaryRecordParser = new XmlSalaryParser();
        } else if (args[1].equals("TXT")) {
            salaryRecordParser = new TextSalaryParser();
        } else {
            log.error("Not supported type, available types are XML, TXT");
            System.exit(1);
        }

        List<SalaryRecord> salaryRecords = salaryRecordParser.parse(file);

        printAnalytics(salaryRecords);
        drawSalaryChart(salaryRecords);
    }

    private static void drawSalaryChart(List<SalaryRecord> records) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (SalaryRecord r : records) {
            dataset.addValue(r.getSalary(), "Salary", r.getName());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Salary by Employee",   // chart title
                "Employee",             // x-axis label
                "Salary",               // y-axis label
                dataset,
                PlotOrientation.VERTICAL,
                false,                  // include legend
                true,                   // tooltips
                false                   // URLs
        );

        File outputFile = new File("charts/salary-chart.png");
        outputFile.getParentFile().mkdirs();

        try {
            ChartUtils.saveChartAsPNG(outputFile, barChart, 800, 600);
            log.info("Chart saved to {}", outputFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to save chart", e);
        }
    }

    private static void printAnalytics(List<SalaryRecord> records) {
        if (records == null || records.isEmpty()) {
            log.info("No records to analyze.");
            return;
        }

        IntSummaryStatistics stats = records.stream()
                .mapToInt(SalaryRecord::getSalary)
                .summaryStatistics();

        SalaryRecord highestPaid = records.stream()
                .max(Comparator.comparingInt(SalaryRecord::getSalary))
                .orElseThrow();

        SalaryRecord lowestPaid = records.stream()
                .min(Comparator.comparingInt(SalaryRecord::getSalary))
                .orElseThrow();

        LocalDate earliestDate = records.stream()
                .map(SalaryRecord::getDate)
                .min(LocalDate::compareTo)
                .orElseThrow();

        LocalDate latestDate = records.stream()
                .map(SalaryRecord::getDate)
                .max(LocalDate::compareTo)
                .orElseThrow();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        log.info("========== Salary Analytics ==========");
        log.info(String.format("%-25s %d", "Total records:", stats.getCount()));
        log.info(String.format("%-25s %,.2f", "Average salary:", stats.getAverage()));
        log.info(String.format("%-25s %,d", "Total salary paid:", stats.getSum()));
        log.info("---------------------------------------");
        log.info(String.format("%-25s %,d (%s)", "Max salary:", stats.getMax(), highestPaid.getName()));
        log.info(String.format("%-25s %,d (%s)", "Min salary:", stats.getMin(), lowestPaid.getName()));
        log.info("---------------------------------------");
        log.info(String.format("%-25s %s", "Earliest record date:", earliestDate.format(fmt)));
        log.info(String.format("%-25s %s", "Latest record date:", latestDate.format(fmt)));
        log.info("=======================================");
    }

}