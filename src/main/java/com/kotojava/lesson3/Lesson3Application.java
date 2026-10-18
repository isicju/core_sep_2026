package com.kotojava.lesson3;

import com.kotojava.lesson3.model.LoanApplication;
import com.kotojava.lesson3.model.Status;
import com.kotojava.lesson3.repository.LoanApplicationsRepo;
import com.kotojava.lesson3.repository.S3Repository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;

@SpringBootApplication
public class Lesson3Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Lesson3Application.class, args);
        System.out.println("");
//        LoanApplication loanApplication =
//                context.getBean(LoanApplicationsRepo.class).getAllLoanApplications().getFirst();

//        loanApplication.setStatus(Status.APPROVED);
//        loanApplication.setExpectedMonthlyPayment(BigDecimal.valueOf(100_000));
//        context.getBean(S3Repository.class).save(loanApplication);
//
//        loanApplication.setStatus(Status.DRAFT);
//        context.getBean(LoanApplicationsRepo.class).updateLoanApplication(loanApplication);

    }

}
