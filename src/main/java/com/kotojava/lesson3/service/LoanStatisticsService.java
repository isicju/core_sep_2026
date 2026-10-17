package com.kotojava.lesson3.service;

import com.kotojava.lesson3.model.LoanApplication;
import com.kotojava.lesson3.model.LoanStatisticsDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LoanStatisticsService {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal MEDIUM_RISK_RATIO = new BigDecimal("0.30");
    private static final BigDecimal HIGH_RISK_RATIO = new BigDecimal("0.40");

    public LoanStatisticsDto calculate(LoanApplication app) {
        Objects.requireNonNull(app, "application must not be null");

        BigDecimal loanAmount = subtract(app.getPropertyPrice(), app.getFirstPayment());
        Integer months = app.getLoanTermYears() == null ? null : app.getLoanTermYears() * 12;
        BigDecimal payment = app.getExpectedMonthlyPayment();

        BigDecimal paymentToIncomeRatio = divide(payment, app.getMonthlyIncome(), 4);
        BigDecimal totalPayout = (payment != null && months != null)
                ? payment.multiply(BigDecimal.valueOf(months))
                : null;
        BigDecimal overpayment = subtract(totalPayout, loanAmount);

        return LoanStatisticsDto.builder()
                .applicationId(app.getId())
                .applicantName(fullName(app))
                .status(app.getStatus())
                .hasAnalystComments(app.getAnalystComments() != null
                        && !app.getAnalystComments().isBlank())
                .propertyPrice(app.getPropertyPrice())
                .firstPayment(app.getFirstPayment())
                .loanAmount(loanAmount)
                .downPaymentPercent(percent(app.getFirstPayment(), app.getPropertyPrice()))
                .loanToValuePercent(percent(loanAmount, app.getPropertyPrice()))
                .loanTermYears(app.getLoanTermYears())
                .loanTermMonths(months)
                .monthlyIncome(app.getMonthlyIncome())
                .expectedMonthlyPayment(payment)
                .paymentToIncomePercent(paymentToIncomeRatio == null
                        ? null
                        : paymentToIncomeRatio.multiply(HUNDRED).setScale(2, RoundingMode.HALF_UP))
                .remainingMonthlyIncome(subtract(app.getMonthlyIncome(), payment))
                .totalPayout(totalPayout)
                .totalOverpayment(overpayment)
                .overpaymentPercent(percent(overpayment, loanAmount))
                .impliedAnnualRatePercent(impliedAnnualRate(loanAmount, payment, months))
                .riskLevel(riskLevel(paymentToIncomeRatio))
                .build();
    }

    private LoanStatisticsDto.RiskLevel riskLevel(BigDecimal ratio) {
        if (ratio == null) {
            return null;
        }
        if (ratio.compareTo(HIGH_RISK_RATIO) > 0) {
            return LoanStatisticsDto.RiskLevel.HIGH;
        }
        if (ratio.compareTo(MEDIUM_RISK_RATIO) > 0) {
            return LoanStatisticsDto.RiskLevel.MEDIUM;
        }
        return LoanStatisticsDto.RiskLevel.LOW;
    }

    /**
     * Solves the annuity formula  payment = L * r / (1 - (1 + r)^-n)  for the monthly rate r
     * by bisection, and returns the annual rate in percent.
     */
    private BigDecimal impliedAnnualRate(BigDecimal loanAmount, BigDecimal payment, Integer months) {
        if (loanAmount == null || payment == null || months == null
                || months <= 0 || loanAmount.signum() <= 0 || payment.signum() <= 0) {
            return null;
        }
        double principal = loanAmount.doubleValue();
        double monthlyPayment = payment.doubleValue();
        if (monthlyPayment * months <= principal) {
            return BigDecimal.ZERO.setScale(2);
        }

        double low = 0;
        double high = 1;
        for (int i = 0; i < 100; i++) {
            double mid = (low + high) / 2;
            double calculated = principal * mid / (1 - Math.pow(1 + mid, -months));
            if (calculated > monthlyPayment) {
                high = mid;
            } else {
                low = mid;
            }
        }
        double monthlyRate = (low + high) / 2;
        return BigDecimal.valueOf(monthlyRate * 12 * 100).setScale(2, RoundingMode.HALF_UP);
    }

    private String fullName(LoanApplication app) {
        String name = Stream.of(app.getFirstName(), app.getLastName())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "))
                .trim();
        return name.isEmpty() ? null : name;
    }

    private BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return (a == null || b == null) ? null : a.subtract(b);
    }

    private BigDecimal divide(BigDecimal a, BigDecimal b, int scale) {
        if (a == null || b == null || b.signum() == 0) {
            return null;
        }
        return a.divide(b, scale, RoundingMode.HALF_UP);
    }

    /** a / b * 100, rounded to 2 decimals. */
    private BigDecimal percent(BigDecimal a, BigDecimal b) {
        if (a == null || b == null || b.signum() == 0) {
            return null;
        }
        return a.multiply(HUNDRED).divide(b, 2, RoundingMode.HALF_UP);
    }
}