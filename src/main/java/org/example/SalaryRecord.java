package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@AllArgsConstructor
@Data
public class SalaryRecord {

    private String name;
    private int salary;
    private LocalDate date;

}
