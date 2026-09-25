package org.example.hw.functional;


import org.example.hw.functional.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FunctionalHW {

    public Optional<Employee> findEmployeeById(List<Employee> employees, int id) {
        return employees.stream().filter(emp -> emp.id == id).findFirst();
    }

    public List<Employee> sortEmployeesByName(List<Employee> employees) {
        return employees.stream().sorted(Comparator.comparing(Employee::getName)).toList();
    }

    public long findNumberJobsWithMaxSalaryMoreThan(List<Job> jobs, double maxSalary) {
        return jobs.stream().filter(job -> job.getMaxSalary() > maxSalary).count();
    }

    public Set<String> getEmployeeNamesFromDepartment(List<Employee> employees, Department department) {
        return employees.stream().filter(employee -> employee.departmentId == department.departmentId)
                .map(Employee::getName)
                .collect(Collectors.toSet());
    }

    //employeeId, Employee
    public Map<Integer, Employee> buildEmployeeMap(List<Employee> employees) {
        return employees.stream().collect(Collectors.toMap(Employee::getId, e -> e));
    }

    //find employee with the closest birthdate
    public Employee findByClosestBirthDate(List<Employee> employees, LocalDate birthDate) {
//        return employees.stream().min(new Comparator<Employee>() {
//            @Override
//            public int compare(Employee o1, Employee o2) {
//                long dinstanceO1 = Math.abs(o1.getBirthDate().toEpochDay() - birthDate.toEpochDay());
//                long dinstanceO2 = Math.abs(o2.getBirthDate().toEpochDay() - birthDate.toEpochDay());
//                return Long.compare(dinstanceO1,dinstanceO2);
//            }
//        }).stream().findFirst().orElse(null);

        return employees.stream().min(Comparator.comparingLong(e -> Math.abs(e.getBirthDate().toEpochDay() - birthDate.toEpochDay())))
                .orElse(null);
    }

    //make map where key is department name and long is total amount of employees working in that department
    public Map<String, Long> totalSalaryReport(List<Employee> employees, List<Department> departments) {
        Map<Integer, List<Employee>> departmentMap =
                employees.stream()
                        .collect(Collectors.groupingBy(Employee::getDepartmentId));

        return departments.stream()
                .collect(Collectors.toMap(
                        department -> department.getDepartmentName(),
                        empl -> {
                            List<Employee> employeesInDep = departmentMap.get(empl.getDepartmentId());
                            long employeeCount = employeesInDep == null ? 0L : (long) employeesInDep.size();
                            return employeeCount;
                        }
                ));
    }

    public Optional<Employee> findRichestEmployee(List<Employee> employees) {
        return employees.stream().max((Comparator.comparingDouble(Employee::getSalary)));
    }

}
