package com.esun.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.esun.demo.model.dto.SeatAssignmentRequest;
import com.esun.demo.model.dto.SeatClearRequest;
import com.esun.demo.model.dto.SeatingChartDto;
import com.esun.demo.service.SeatingChartService;

@RestController
@RequestMapping("/api/seatingchart")
public class SeatingChartController {

    @Autowired
    private SeatingChartService seatingChartService;

    // 取得所有樓層座位及其狀態（空位/已佔用）
    @GetMapping("/all")
    public List<SeatingChartDto> getAllSeatingCharts() {
        return seatingChartService.getAllSeatingCharts();
    }

    // 取得指定樓層的座位資訊
    @GetMapping("/floor/{floorNo}")
    public List<SeatingChartDto> getSeatingChartsByFloor(@PathVariable String floorNo) {
        return seatingChartService.getSeatingChartsByFloor(floorNo);
    }

    // 查詢特定座位狀態
    @GetMapping("/status")
    public SeatingChartDto getSeatingChartStatus(@RequestParam String floorNo, @RequestParam String seatNo) {
        return seatingChartService.getSeatingChartStatus(floorNo, seatNo);
    }

    // 更新座位指派（指派員工到指定座位）
    @PostMapping("/assign")
    public ResponseEntity<String> updateSeatingAssignment(@RequestBody SeatAssignmentRequest request) {
        seatingChartService.updateSeatingAssignment(request);
        return ResponseEntity.ok("Seat assignment updated successfully.");
    }

    // 清除座位（使該座位變成空位）
    @PutMapping("/clear")
    public ResponseEntity<String> clearSeatingAssignment(@RequestBody SeatClearRequest request) {
        seatingChartService.clearSeatingAssignment(request);
        return ResponseEntity.ok("Seat assignment cleared successfully.");
    }

    // 批次更新座位指派
    @PutMapping("/batchUpdate")
    public ResponseEntity<String> batchUpdateSeating(@RequestBody List<SeatAssignmentRequest> requests) {
        seatingChartService.batchUpdateSeating(requests);
        return ResponseEntity.ok("Batch seating update successful.");
    }

    // 取得所有空位
    @GetMapping("/available")
    public List<SeatingChartDto> getAvailableSeatingCharts() {
        return seatingChartService.getAvailableSeatingCharts();
    }
}
