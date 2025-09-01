package com.srinivasa.refrigeration.works.srw_springboot.repository;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Owner;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, String> {

    @Query("SELECT o FROM Owner o WHERE o.ownerId = :identifier OR o.phoneNumber = :identifier OR o.email = :identifier OR o.nationalIdNumber = :identifier")
    Owner findByIdentifier(@Param("identifier") String identifier);

    @Modifying
    @Transactional
    @Query("UPDATE Owner SET updatedAt = :updatedAt, status = :status WHERE ownerId = :ownerId")
    void updateStatusById(@Param("ownerId") String ownerId, @Param("status") UserStatus status, @Param("updatedAt") LocalDateTime updatedAt);
}