package com.srinivasa.refrigeration.works.srw_springboot.repository;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, String> {
}