package com.kotojava.lesson3.repository;

import com.kotojava.lesson3.model.LoanApplication;
import com.kotojava.lesson3.model.Status;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class LoanApplicationsRepo {

    private JdbcTemplate loanApplicationTemplate;

    private static final RowMapper<LoanApplication> ROW_MAPPER = (rs, rowNum) ->
            LoanApplication.builder()
                    .id(rs.getString("id"))
                    .firstName(rs.getString("first_name"))
                    .lastName(rs.getString("last_name"))
                    .monthlyIncome(rs.getBigDecimal("monthly_income"))
                    .propertyPrice(rs.getBigDecimal("property_price"))
                    .firstPayment(rs.getBigDecimal("first_payment"))
                    .loanTermYears(rs.getInt("loan_term_years"))
                    .status(Status.valueOf(rs.getString("status")))
                    .expectedMonthlyPayment(rs.getBigDecimal("expected_monthly_payment"))
                    .analystComments(rs.getString("analyst_comments"))
                    .build();

    public List<LoanApplication> getAllLoanApplications() {
        return loanApplicationTemplate.query("select * from mortgage_applications", ROW_MAPPER);
    }

    public LoanApplication getLoanApplicationById(String id) {
        List<LoanApplication> result = loanApplicationTemplate.query(
                "select * from mortgage_applications where id = ?", ROW_MAPPER, id);
        return result.isEmpty() ? null : result.get(0);
    }

    public LoanApplication updateLoanApplication(LoanApplication loanApplication) {
        int rows = loanApplicationTemplate.update("""
                    update mortgage_applications
                    set first_name = ?,
                        last_name = ?,
                        monthly_income = ?,
                        property_price = ?,
                        first_payment = ?,
                        loan_term_years = ?,
                        status = ?,
                        expected_monthly_payment = ?,
                        analyst_comments = ?
                    where id = ?
                    """,
                loanApplication.getFirstName(),
                loanApplication.getLastName(),
                loanApplication.getMonthlyIncome(),
                loanApplication.getPropertyPrice(),
                loanApplication.getFirstPayment(),
                loanApplication.getLoanTermYears(),
                loanApplication.getStatus().name(),
                loanApplication.getExpectedMonthlyPayment(),
                loanApplication.getAnalystComments(),
                loanApplication.getId());

        if (rows == 0) {
            throw new IllegalArgumentException("Loan application not found: " + loanApplication.getId());
        }
        return getLoanApplicationById(loanApplication.getId());
    }

}
