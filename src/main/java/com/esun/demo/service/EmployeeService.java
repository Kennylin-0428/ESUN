package com.esun.demo.service;

import com.esun.demo.model.Employee;
import com.esun.demo.model.dto.EmployeeSeatingDto;

public interface EmployeeService {

    void addEmployee(Employee employee);

    void deleteEmployee(Long empId);

    EmployeeSeatingDto getEmployeeSeating(Long empId);
}
