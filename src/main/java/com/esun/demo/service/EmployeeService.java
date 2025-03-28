package com.esun.demo.service;

import java.util.List;

import com.esun.demo.model.Employee;
import com.esun.demo.model.dto.EmployeeSeatingDto;

public interface EmployeeService {

    List<Employee> getAllEmployee();

    void addEmployee(Employee employee);

    void deleteEmployee(Long empId);

    EmployeeSeatingDto getEmployeeSeating(Long empId);
}
