package com.kotojava.lesson3.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class LoanApplication {

    private String id;
    private String firstName;
    private String lastName;
    private BigDecimal monthlyIncome;
    private BigDecimal firstPayment;
    private BigDecimal propertyPrice;
    private BigDecimal expectedMonthlyPayment;
    private Integer loanTermYears;
    private Status status;
    private String analystComments;

}
