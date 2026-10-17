package com.kotojava.lesson3.service;

import com.kotojava.lesson3.model.LoanApplication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LoanCalculator {

    public Double calculate(LoanApplication loanApplication) {
        BigDecimal S = loanApplication.getPropertyPrice();
        BigDecimal S0 = loanApplication.getFirstPayment();
        Integer N = loanApplication.getLoanTermYears();

        BigDecimal loanAmount = S.subtract(S0);
        BigDecimal annualRate = BigDecimal.valueOf(1.06).pow(N);
        BigDecimal totalMonths = BigDecimal.valueOf(12L * N);

        return loanAmount
                .multiply(annualRate)
                .divide(totalMonths, 2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
