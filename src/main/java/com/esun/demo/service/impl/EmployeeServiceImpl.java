package com.esun.demo.service.impl;

import com.esun.demo.model.Employee;
import com.esun.demo.model.SeatingChart;
import com.esun.demo.model.dto.EmployeeSeatingDto;
import com.esun.demo.repo.EmployeeRepo;
import com.esun.demo.repo.SeatingChartRepo;
import com.esun.demo.service.EmployeeService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepository;

    @Autowired
    private SeatingChartRepo seatingChartRepository;

    @Override
    public void addEmployee(Employee employee) {
        // 檢查必填欄位，並保存
        if (employee.getName() == null || employee.getName().isEmpty()) {
            throw new RuntimeException("Employee name is required");
        }
        employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long empId) {
        // 取得員工，並清除其座位關聯
        Employee employee = employeeRepository.findByEmpId(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setFloorSeatSeq(null);
        employeeRepository.delete(employee);
    }

    @Override
    public EmployeeSeatingDto getEmployeeSeating(Long empId) {
        Employee employee = employeeRepository.findByEmpId(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        if (employee.getFloorSeatSeq() != null) {
            SeatingChart seat = seatingChartRepository.findByFloorSeatSeq(employee.getFloorSeatSeq())
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            return new EmployeeSeatingDto(employee.getEmpId(), employee.getName(), seat.getFloorNo(), seat.getSeatNo());
        } else {
            return new EmployeeSeatingDto(employee.getEmpId(), employee.getName(), null, null);
        }
    }
}
