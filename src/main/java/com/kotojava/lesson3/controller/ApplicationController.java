package com.kotojava.lesson3.controller;

import com.kotojava.lesson3.model.LoanApplication;
import com.kotojava.lesson3.model.LoanStatisticsDto;
import com.kotojava.lesson3.model.Status;
import com.kotojava.lesson3.repository.LoanApplicationsRepo;
import com.kotojava.lesson3.repository.S3Repository;
import com.kotojava.lesson3.service.LoanCalculator;
import com.kotojava.lesson3.service.LoanPdfService;
import com.kotojava.lesson3.service.LoanStatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/loanapplications")
public class ApplicationController {

    private LoanApplicationsRepo repo;
    private LoanCalculator loanCalculator;
    private S3Repository s3Repository;
    private LoanPdfService loanPdfService;
    private LoanStatisticsService loanStatisticsService;

    @GetMapping
    public ResponseEntity<List<LoanApplication>> findAll() {
        return ResponseEntity.ok(repo.getAllLoanApplications());
    }

    @PutMapping
    public ResponseEntity<LoanApplication> updateApplication(@RequestBody LoanApplication loanApplication) {
        LoanApplication application = repo.updateLoanApplication(loanApplication);

        if(loanApplication.getStatus() == Status.APPROVED || loanApplication.getStatus() == Status.REJECTED) {
            byte[] loanPdfFile = loanPdfService.generatePdfFile(loanApplication);
            String fileName = application.getFirstName() + "_" + application.getLastName() + LocalDate.now() + ".pdf";
            s3Repository.save(loanPdfFile, fileName);
        }
        return ResponseEntity.ok(application);
    }

    @PostMapping("/calc")
    public ResponseEntity<Double> findAll(@RequestBody LoanApplication loanApplication) {
        return ResponseEntity.ok(loanCalculator.calculate(loanApplication));
    }

    @PostMapping("/analize")
    public ResponseEntity<LoanStatisticsDto> getStatus(@RequestBody LoanApplication loanApplication) {
        return ResponseEntity.ok(loanStatisticsService.calculate(loanApplication));
    }

}
