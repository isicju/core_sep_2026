package org.example;

import java.time.LocalDate;

public class SalaryRecord {
    public SalaryRecord(String name, int salary, LocalDate date) {
        this.name = name;
        this.salary = salary;
        this.date = date;
    }

    @Override
    public String toString() {
        return "SalaryRecord{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                ", date=" + date +
                '}';
    }

    private String name;
    private int salary;
    private LocalDate date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
