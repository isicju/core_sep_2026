package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("File path isn't specified! Usage: java Main <file>");
        }
        File file = new File(args[0]);
        if (!file.exists()) {
            System.err.println("File path isn't specified! Usage: java Main <file>");
        }

        Path filePath = Path.of(file.getAbsolutePath());
        List<String> lines = Files.readAllLines(filePath);
        List<SalaryRecord> results = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            Pattern recordPattern = Pattern.compile("^(\\w+),(\\d+),(\\d{2}-\\d{2}-\\d{4})$");
            Matcher matcher = recordPattern.matcher(line);
            try {
                if (matcher.matches()) {
                    String name = matcher.group(1);
                    int salary = Integer.parseInt(matcher.group(2));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate date = LocalDate.parse(matcher.group(3), formatter);
                    results.add(new SalaryRecord(name, salary, date));
                } else {
                    System.err.println("Invalid record format!" + line);
                }
            } catch (Exception e) {
                System.err.println("Invalid record format!" + line + " " + e.getMessage());
            }
        }
        System.out.println("total salary record numbers: " + results.size());
        printAnalytics(results);
    }

    private static void printAnalytics(List<SalaryRecord> records) {
        if (records == null || records.isEmpty()) {
            System.out.println("No records to analyze.");
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

        System.out.println("========== Salary Analytics ==========");
        System.out.printf("%-25s %d%n", "Total records:", stats.getCount());
        System.out.printf("%-25s %,.2f%n", "Average salary:", stats.getAverage());
        System.out.printf("%-25s %,d%n", "Total salary paid:", stats.getSum());
        System.out.println("---------------------------------------");
        System.out.printf("%-25s %,d (%s)%n", "Max salary:", stats.getMax(), highestPaid.getName());
        System.out.printf("%-25s %,d (%s)%n", "Min salary:", stats.getMin(), lowestPaid.getName());
        System.out.println("---------------------------------------");
        System.out.printf("%-25s %s%n", "Earliest record date:", earliestDate.format(fmt));
        System.out.printf("%-25s %s%n", "Latest record date:", latestDate.format(fmt));
        System.out.println("=======================================");
    }

}
