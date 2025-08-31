package com.srinivasa.refrigeration.works.srw_springboot.repository;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, String> {

    List<Complaint> findByBookedById(String userId);

    List<Complaint> findByTechnicianDetailsEmployeeId(String employeeId);

    Complaint findByComplaintId(String complaintId);
}