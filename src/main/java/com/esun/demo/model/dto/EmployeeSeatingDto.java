package com.esun.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSeatingDto {
    private Integer empId;
    private String name;
    private String floorNo;
    private String seatNo;
}
