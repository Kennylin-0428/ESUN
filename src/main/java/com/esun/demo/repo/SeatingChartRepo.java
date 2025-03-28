package com.esun.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.esun.demo.model.SeatingChart;

@Repository
public interface SeatingChartRepo extends JpaRepository<SeatingChart, Integer> {
    SeatingChart findByFloorNoAndSeatNo(String floorNo, String seatNo);

    List<SeatingChart> findByFloorNo(String floorNo);

    Optional<SeatingChart> findByFloorSeatSeq(Long floorSeatSeq);

    @Query("SELECT s FROM SeatingChart s LEFT JOIN Employee e ON s.floorSeatSeq = e.floorSeatSeq WHERE e.floorSeatSeq IS NULL ORDER BY s.floorNo")
    List<SeatingChart> findAvailableSeats();

}
