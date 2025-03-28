package com.esun.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @Column(name = "EMP_ID", nullable = false)
    private String empId;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "FLOOR_SEAT_SEQ")
    private Long floorSeatSeq;
}
