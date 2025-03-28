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

    public String updateSeatingAssignment(SeatAssignmentRequest request) {
        SeatingChart seat = seatingChartRepository.findByFloorNoAndSeatNo(request.getFloorNo(), request.getSeatNo());
        if (seat == null) {
            throw new RuntimeException("Seat not found");
        }

        Optional<Employee> occupantOpt = employeeRepository.findByFloorSeatSeq(seat.getFloorSeatSeq());
        if (occupantOpt.isPresent() && !occupantOpt.get().getEmpId().equals(request.getEmpId())) {
            throw new RuntimeException("Seat is occupied");
        }

        Employee emp = employeeRepository.findByEmpId(request.getEmpId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        emp.setFloorSeatSeq(seat.getFloorSeatSeq());
        employeeRepository.save(emp);

        return "Seat assignment updated successfully for Employee ID: " + emp.getEmpId();
    }

    @Override
    public void clearSeatingAssignment(SeatClearRequest request) {
        SeatingChart seat = seatingChartRepository.findByFloorNoAndSeatNo(request.getFloorNo(), request.getSeatNo());
        if (seat == null) {
            throw new RuntimeException("Seat not found");
        }
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
        for (SeatAssignmentRequest req : requests) {
            updateSeatingAssignment(req);
        }
    }

    @Override
    public List<SeatingChartDto> getAvailableSeatingCharts() {
        List<SeatingChart> seats = seatingChartRepository.findAvailableSeats();
        return seats.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private SeatingChartDto mapToDto(SeatingChart seat) {
        Optional<Employee> employeeOpt = employeeRepository.findByFloorSeatSeq(seat.getFloorSeatSeq());

        boolean occupied = employeeOpt.isPresent();
        String empId = occupied ? employeeOpt.get().getEmpId() : null;

        return new SeatingChartDto(seat.getFloorSeatSeq(), seat.getFloorNo(), seat.getSeatNo(), occupied, empId);
    }

}
