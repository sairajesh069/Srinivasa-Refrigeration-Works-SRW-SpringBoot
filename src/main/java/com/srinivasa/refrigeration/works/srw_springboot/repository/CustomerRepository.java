package com.srinivasa.refrigeration.works.srw_springboot.repository;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Customer;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("SELECT c FROM Customer c WHERE c.customerId = :identifier OR c.phoneNumber = :identifier OR c.email = :identifier")
    Customer findByIdentifier(@Param("identifier") String identifier);

    @Modifying
    @Transactional
    @Query("UPDATE Customer SET updatedAt = :updatedAt, status = :status WHERE customerId = :customerId")
    void updateStatusById(@Param("customerId") String customerId, @Param("status") UserStatus status, @Param("updatedAt") LocalDateTime updatedAt);
}