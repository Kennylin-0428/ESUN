package com.esun.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.esun.demo.model.Employee;
import com.esun.demo.model.SeatingChart;
import com.esun.demo.model.dto.SeatAssignmentRequest;
import com.esun.demo.model.dto.SeatClearRequest;
import com.esun.demo.model.dto.SeatingChartDto;
import com.esun.demo.repo.EmployeeRepo;
import com.esun.demo.repo.SeatingChartRepo;
import com.esun.demo.service.SeatingChartService;

import jakarta.transaction.Transactional;

@Service
public class SeatingChartServiceImpl implements SeatingChartService {

    @Autowired
    private SeatingChartRepo seatingChartRepository;

    @Autowired
    private EmployeeRepo employeeRepository;

    @Override
    public List<SeatingChartDto> getAllSeatingCharts() {
        List<SeatingChart> seats = seatingChartRepository.findAll();
        return seats.stream().map(seat -> mapToDto(seat)).collect(Collectors.toList());
    }

    @Override
    public List<SeatingChartDto> getSeatingChartsByFloor(String floorNo) {
        List<SeatingChart> seats = seatingChartRepository.findByFloorNo(floorNo);
        return seats.stream().map(seat -> mapToDto(seat)).collect(Collectors.toList());
    }

    @Override
    public SeatingChartDto getSeatingChartStatus(String floorNo, String seatNo) {
        SeatingChart seat = seatingChartRepository.findByFloorNoAndSeatNo(floorNo, seatNo);
        if (seat == null) {
            throw new RuntimeException("Seat not found");
        }
        return mapToDto(seat);
    }

    @Override
    public void updateSeatingAssignment(SeatAssignmentRequest request) {
        // 根據座位資訊找到 SeatingChart
        SeatingChart seat = seatingChartRepository.findByFloorNoAndSeatNo(request.getFloorNo(), request.getSeatNo());
        if (seat == null) {
            throw new RuntimeException("Seat not found");
        }
        // 更新 Employee 的座位外鍵 (假設 Employee 有一個 floorSeatSeq 欄位)
        Employee emp = employeeRepository.findByEmpId(request.getEmpId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        emp.setFloorSeatSeq(seat.getFloorSeatSeq());
        employeeRepository.save(emp);
    }

    @Override
    public void clearSeatingAssignment(SeatClearRequest request) {
        // 根據座位資訊找到 SeatingChart
        SeatingChart seat = seatingChartRepository.findByFloorNoAndSeatNo(request.getFloorNo(), request.getSeatNo());
        if (seat == null) {
            throw new RuntimeException("Seat not found");
        }
        // 找到使用該座位的員工，並清除其座位資訊
        Optional<Employee> employeeOpt = employeeRepository.findByFloorSeatSeq(seat.getFloorSeatSeq());
        if (employeeOpt.isPresent()) {
            Employee emp = employeeOpt.get();
            emp.setFloorSeatSeq(null);
            employeeRepository.save(emp);
        }
    }

    @Override
    @Transactional
    public void batchUpdateSeating(List<SeatAssignmentRequest> requests) {
        // 逐筆處理，並確保在 transaction 中
        for (SeatAssignmentRequest req : requests) {
            updateSeatingAssignment(req);
        }
    }

    @Override
    public List<SeatingChartDto> getAvailableSeatingCharts() {
        List<SeatingChart> seats = seatingChartRepository.findAvailableSeats();
        return seats.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // 輔助方法：將 SeatingChart 實體轉為 DTO
    private SeatingChartDto mapToDto(SeatingChart seat) {
        // 根據 seating chart 的 floorSeatSeq 查詢單筆 Employee
        Optional<Employee> employeeOpt = employeeRepository.findByFloorSeatSeq(seat.getFloorSeatSeq());

        // 若有查到資料，代表該座位外鍵已被佔用
        boolean occupied = employeeOpt.isPresent();
        Integer empId = occupied ? employeeOpt.get().getEmpId() : null;

        // 回傳 DTO，occupied 根據 Employee 外鍵是否存在來決定
        return new SeatingChartDto(
                seat.getFloorSeatSeq(),
                seat.getFloorNo(),
                seat.getSeatNo(),
                occupied,
                empId);
    }

}
