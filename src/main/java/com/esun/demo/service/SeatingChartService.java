package com.esun.demo.service;

import java.util.List;

import com.esun.demo.model.dto.SeatAssignmentRequest;
import com.esun.demo.model.dto.SeatClearRequest;
import com.esun.demo.model.dto.SeatingChartDto;

public interface SeatingChartService {

    List<SeatingChartDto> getAllSeatingCharts();

    List<SeatingChartDto> getSeatingChartsByFloor(String floorNo);

    SeatingChartDto getSeatingChartStatus(String floorNo, String seatNo);

    void updateSeatingAssignment(SeatAssignmentRequest request);

    void clearSeatingAssignment(SeatClearRequest request);

    void batchUpdateSeating(List<SeatAssignmentRequest> requests);

    List<SeatingChartDto> getAvailableSeatingCharts();

}
