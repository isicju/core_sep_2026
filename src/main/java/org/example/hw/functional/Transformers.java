package org.example.hw.functional;


import org.example.hw.functional.model.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Transformers {
    private Employee findEmployeesBornAtThisYear(Collection<Employee> employees, int year) {
        return employees.stream().filter(empl -> empl.getBirthDate().getYear() == year).findFirst().orElse(null);
    }

    private Collection<String> findAllJobTitles(Collection<Job> jobs, Collection<Job> jobs2) {
        return Stream.concat(jobs.stream(), jobs2.stream())
                .map(Job::getJobTitle).toList();
    }

    private Collection<String> findAllNameWithoutDuplicatesFromBothList(Collection<Employee> employees, Collection<Employee> employees2) {
        return Stream.concat(employees.stream(), employees2.stream())
                .map(Employee::getName)
                .collect(Collectors.toSet());
    }

    private Integer findAverageAgeOfEmployeesThatWorkInDepartment(Collection<Employee> employees, Department departments) {
        return (int) employees.stream().filter(empl -> empl.departmentId == departments.departmentId)
                .map(empl -> empl.getBirthDate().getYear())
                .mapToLong(value -> value)
                .average()
                .orElse(0);

    }

    private Employee findEmployeeWithNameOrUseDefaultOne(Collection<Employee> employees, String name, Employee defaultUser) {
        return employees.stream().filter(empl->empl.getName().equals(name)).findFirst().orElse(defaultUser);
    }

    private Job findJobWithMinSalaryMoreThanOrThrowException(Collection<Job> jobs, Integer minSalary) {
        return jobs.stream().filter(job->job.getMinSalary() > minSalary)
                .findAny()
                .orElseThrow(()-> new IllegalArgumentException("there is no such job"));
    }

    //try to find 2 options.
    //option 1
    private Collection<String> findAllLocationNamesButWithoutDuplicates(Collection<Location> locations) {
        return locations.stream().map(Location::getLocation)
                .distinct()
                .toList();
    }

    //option 2
    private Collection<String> findAllLocationNamesButWithoutDuplicates2(Collection<Location> locations) {
        return locations.stream().map(Location::getLocation).collect(Collectors.toSet());
    }
}
