package com.srinivasa.refrigeration.works.srw_springboot.repository;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @Query("SELECT e FROM Employee e WHERE e.employeeId = :identifier OR e.phoneNumber = :identifier OR e.email = :identifier OR e.nationalIdNumber = :identifier")
    Employee findByIdentifier(@Param("identifier") String identifier);
}