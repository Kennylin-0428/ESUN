package com.esun.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatingChartDto {

    private Long floorSeatSeq;
    private String floorNo;
    private String seatNo;
    private Boolean occupied;
    private String empId;
}
