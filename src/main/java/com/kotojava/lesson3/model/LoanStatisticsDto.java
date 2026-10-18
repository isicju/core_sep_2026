package com.kotojava.lesson3.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LoanStatisticsDto {

    public enum RiskLevel {
        LOW, MEDIUM, HIGH
    }

    private String applicationId;
    private String applicantName;
    private Status status;
    private boolean hasAnalystComments;

    private BigDecimal propertyPrice;
    private BigDecimal firstPayment;
    private BigDecimal loanAmount;

    /** First payment as a percentage of property price. */
    private BigDecimal downPaymentPercent;

    /** Loan amount as a percentage of property price (LTV). */
    private BigDecimal loanToValuePercent;

    private Integer loanTermYears;
    private Integer loanTermMonths;

    private BigDecimal monthlyIncome;
    private BigDecimal expectedMonthlyPayment;

    /** Monthly payment as a percentage of monthly income. */
    private BigDecimal paymentToIncomePercent;

    /** What is left of the monthly income after the loan payment. */
    private BigDecimal remainingMonthlyIncome;

    /** Everything the applicant pays back over the whole term. */
    private BigDecimal totalPayout;

    private BigDecimal totalOverpayment;
    private BigDecimal overpaymentPercent;

    /** Annual interest rate implied by the loan amount, term and monthly payment (annuity). */
    private BigDecimal impliedAnnualRatePercent;

    /** LOW below 30% payment-to-income, MEDIUM up to 40%, HIGH above. */
    private RiskLevel riskLevel;
}