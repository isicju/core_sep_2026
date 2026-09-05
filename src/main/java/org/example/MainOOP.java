package org.example;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;

public class MainOOP {

    private enum TYPE {
        TXT, XML
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("File path isn't specified! Usage: java Main <file> <type>");
            System.exit(1);
        }

        File file = new File(args[0]);
        if (!file.exists()) {
            System.err.println("File path isn't specified! Usage: java Main <file> <type>");
            System.exit(1);
        }

        SalaryRecordParser salaryRecordParser = null;

        if (args[1].equals("XML")) {
            salaryRecordParser = new XmlSalaryParser();
        } else if (args[1].equals("TXT")) {
            salaryRecordParser = new TextSalaryParser();
        } else {
            System.err.println("Not supported type, available types are XML, TXT");
            System.exit(1);
        }

        List<SalaryRecord> salaryRecords = salaryRecordParser.parse(file);

        printAnalytics(salaryRecords);
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
