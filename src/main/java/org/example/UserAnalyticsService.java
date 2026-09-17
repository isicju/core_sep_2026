package org.example;

import org.example.model.Profession;
import org.example.model.User;
import org.example.model.UserAnalyticResults;

import java.util.ArrayList;
import java.util.List;

public class UserAnalyticsService {
    public List<UserAnalyticResults> makeAnalysis(List<User> users) {
        return List.of(buildAnalyticsResults(users, Profession.IT),
                buildAnalyticsResults(users, Profession.ACCOUNTANT),
                buildAnalyticsResults(users, Profession.ARTIST)
        );
    }

    private UserAnalyticResults buildAnalyticsResults(List<User> users, Profession profession) {
        int averageAge = 0;
        int averageSalary = 0;
        for (User user : users) {
            if (user.getProfession() == Profession.IT) {
                averageSalary += user.getSalary();
                averageAge += user.getAge();
            }
        }
        return UserAnalyticResults.builder()
                .profession(profession)
                .averageSalary(averageSalary)
                .averageAge(averageAge)
                .build();
    }

}
