package com.esun.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatAssignmentRequest {

    private Long empId;
    private String floorNo;
    private String seatNo;
}