package com.esun.demo.repo;

import com.esun.demo.model.Employee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByFloorSeatSeq(Long floorSeatSeq);

    Optional<Employee> findByEmpId(Long empId);

}
