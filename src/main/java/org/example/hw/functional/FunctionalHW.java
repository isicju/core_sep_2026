package org.example.hw.functional;


import org.example.hw.functional.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FunctionalHW {

    public Optional<Employee> findEmployeeById(List<Employee> employees, int id) {
        return null;
    }

    public List<Employee> sortEmployeesByName(List<Employee> employees) {
        return null;
    }

    public long findNumberJobsWithMaxSalaryMoreThan(List<Job> jobs, double maxSalary) {
        return 0;
    }

    public Set<String> getEmployeeNamesFromDepartment(List<Employee> employees, Department department) {
        return null;
    }

    //employeeId, Employee
    public Map<Integer, Employee> buildEmployeeMap(List<Employee> employees) {
        return null;
    }

    //find employee with the closest birthdate
    public Employee findByClosestBirthDate(List<Employee> employees, LocalDate birthDate) {
        return null;
    }

    //make map where key is department name and long is total amount of employees working in that department
    public Map<String, Long> totalSalaryReport(List<Employee> employees, List<Department> departments) {
        return null;
    }

    public Optional<Employee> findRichestEmployee(List<Employee> employees) {
        return null;
    }

}
