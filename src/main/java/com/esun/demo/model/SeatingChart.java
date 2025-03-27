package com.esun.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seatingchart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatingChart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FLOOR_SEAT_SEQ", nullable = false)
    private Long floorSeatSeq;

    @Column(name = "FLOOR_NO", nullable = false, length = 10)
    private String floorNo;

    @Column(name = "SEAT_NO", nullable = false, length = 10)
    private String seatNo;

}
