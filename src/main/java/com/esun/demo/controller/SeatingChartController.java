package com.esun.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/all")
    public List<SeatingChartDto> getAllSeatingCharts() {
        return seatingChartService.getAllSeatingCharts();
    }

    @GetMapping("/floor/{floorNo}")
    public List<SeatingChartDto> getSeatingChartsByFloor(@PathVariable String floorNo) {
        return seatingChartService.getSeatingChartsByFloor(floorNo);
    }

    @GetMapping("/status")
    public SeatingChartDto getSeatingChartStatus(@RequestParam String floorNo, @RequestParam String seatNo) {
        return seatingChartService.getSeatingChartStatus(floorNo, seatNo);
    }

    @PostMapping("/assign")
    public ResponseEntity<String> updateSeatingAssignment(@RequestBody SeatAssignmentRequest request) {
        try {
            String resultMessage = seatingChartService.updateSeatingAssignment(request);
            return ResponseEntity.ok(resultMessage);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Seat not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seat not found, Please chose other seat");
            } else if (e.getMessage().equals("Employee not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
            } else if (e.getMessage().equals("Seat is occupied")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("This seat is already occupied by another employee, please choose another seat.");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Backend api error");
        }
    }

    @PutMapping("/clear")
    public ResponseEntity<String> clearSeatingAssignment(@RequestBody SeatClearRequest request) {
        seatingChartService.clearSeatingAssignment(request);
        return ResponseEntity.ok("Seat assignment cleared successfully.");
    }

    @PutMapping("/batchUpdate")
    public ResponseEntity<String> batchUpdateSeating(@RequestBody List<SeatAssignmentRequest> requests) {
        seatingChartService.batchUpdateSeating(requests);
        return ResponseEntity.ok("Batch seating update successful.");
    }

    @GetMapping("/available")
    public List<SeatingChartDto> getAvailableSeatingCharts() {
        return seatingChartService.getAvailableSeatingCharts();
    }
}
