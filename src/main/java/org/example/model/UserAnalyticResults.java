package org.example.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserAnalyticResults {
    private Profession profession;
    private int averageSalary;
    private int averageAge;
}
