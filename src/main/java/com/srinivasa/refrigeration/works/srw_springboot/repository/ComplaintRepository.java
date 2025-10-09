package com.srinivasa.refrigeration.works.srw_springboot.repository;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Complaint;
import com.srinivasa.refrigeration.works.srw_springboot.utils.complaintUtils.ComplaintState;
import com.srinivasa.refrigeration.works.srw_springboot.utils.complaintUtils.ComplaintStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, String> {

    List<Complaint> findByBookedById(String userId);

    List<Complaint> findByTechnicianDetailsEmployeeId(String employeeId);

    Complaint findByComplaintId(String complaintId);

    List<Complaint> findByBookedByIdAndStatus(String bookedById, ComplaintStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Complaint SET updatedAt = :updatedAt, customerFeedback = :customerFeedback WHERE complaintId = :complaintId")
    void saveUserFeedback(@Param("complaintId") String complaintId, @Param("customerFeedback") String customerFeedback, @Param("updatedAt") LocalDateTime updatedAt);

    @Modifying
    @Transactional
    @Query("""
    UPDATE Complaint c 
       SET c.complaintState = :complaintState,
           c.updatedAt = :timestamp,
           c.closedAt = CASE WHEN :complaintState = com.srinivasa.refrigeration.works.srw_springboot.utils.complaintUtils.ComplaintState.CLOSED 
                             THEN :timestamp ELSE c.closedAt END,
           c.reopenedAt = CASE WHEN :complaintState = com.srinivasa.refrigeration.works.srw_springboot.utils.complaintUtils.ComplaintState.REOPENED 
                               THEN :timestamp ELSE c.reopenedAt END
     WHERE c.complaintId = :complaintId
""")
    void updateState(@Param("complaintId") String complaintId, @Param("complaintState") ComplaintState complaintState, @Param("timestamp") LocalDateTime timestamp);
}