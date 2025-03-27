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

    // 自定義查詢取得空位：假設空位定義為沒有對應的員工使用該座位
    @Query("SELECT s FROM SeatingChart s WHERE s.floorSeatSeq NOT IN (SELECT e.floorSeatSeq FROM Employee e WHERE e.floorSeatSeq IS NOT NULL)")
    List<SeatingChart> findAvailableSeats();

}
