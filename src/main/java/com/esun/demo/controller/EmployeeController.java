package com.esun.demo.controller;

import com.esun.demo.model.Employee;
import com.esun.demo.model.dto.EmployeeSeatingDto;
import com.esun.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // 新增員工
    @PostMapping("/add")
    public ResponseEntity<String> addEmployee(@RequestBody Employee employee) {
        employeeService.addEmployee(employee);
        return ResponseEntity.ok("Employee added successfully.");
    }

    // 刪除員工（並清除座位佔用）
    @DeleteMapping("/delete/{empId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long empId) {
        employeeService.deleteEmployee(empId);
        return ResponseEntity.ok("Employee deleted and seat cleared.");
    }

    // 取得指定員工的座位資訊
    @GetMapping("/seating/{empId}")
    public EmployeeSeatingDto getEmployeeSeating(@PathVariable Long empId) {
        return employeeService.getEmployeeSeating(empId);
    }
}
