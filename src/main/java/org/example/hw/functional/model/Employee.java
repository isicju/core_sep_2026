package org.example.hw.functional.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Employee {
    public int id;
    public String name;
    public double salary;
    public int departmentId;
    public LocalDate birthDate;
}
