package com.srinivasa.refrigeration.works.srw_springboot.repository;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Employee;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @Query("SELECT e FROM Employee e WHERE e.employeeId = :identifier OR e.phoneNumber = :identifier OR e.email = :identifier OR e.nationalIdNumber = :identifier")
    Employee findByIdentifier(@Param("identifier") String identifier);

    List<Employee> findByStatus(UserStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Employee SET updatedAt = :updatedAt, status = :status WHERE employeeId = :employeeId")
    void updateStatusById(@Param("employeeId") String employeeId, @Param("status") UserStatus status, @Param("updatedAt") LocalDateTime updatedAt);
}