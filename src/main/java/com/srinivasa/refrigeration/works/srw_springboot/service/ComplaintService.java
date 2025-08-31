package com.srinivasa.refrigeration.works.srw_springboot.service;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Complaint;
import com.srinivasa.refrigeration.works.srw_springboot.mapper.ComplaintMapper;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintDTO;
import com.srinivasa.refrigeration.works.srw_springboot.repository.ComplaintRepository;
import com.srinivasa.refrigeration.works.srw_springboot.utils.ComplaintState;
import com.srinivasa.refrigeration.works.srw_springboot.utils.ComplaintStatus;
import com.srinivasa.refrigeration.works.srw_springboot.utils.PhoneNumberFormatter;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserIdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintMapper complaintMapper;
    private final ComplaintRepository complaintRepository;

    @Transactional
    @CacheEvict(cacheNames = "complaints", allEntries = true)
    public ComplaintDTO registerComplaint(ComplaintDTO complaintDTO) {
        Complaint complaint = complaintMapper.toEntity(complaintDTO);
        complaint.setComplaintReference(UserIdGenerator.generateUniqueId(complaintDTO.getContactNumber()));
        complaint.setComplaintId("SRW" + complaint.getComplaintReference() + "COMP");
        complaint.setContactNumber(PhoneNumberFormatter.formatPhoneNumber(complaint.getContactNumber()));
        complaint.setStatus(ComplaintStatus.PENDING);
        complaint.setComplaintState(ComplaintState.SUBMITTED);
        complaintRepository.save(complaint);
        return complaintMapper.toDto(complaint);
    }

    @Cacheable(value = "complaints", key = "'raised_by-' + #userId")
    public List<ComplaintDTO> getComplaintsRaisedBy(String userId) {
        return complaintRepository
                .findByBookedById(userId)
                .stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    @Cacheable(value = "complaints", key = "'complaint_list'")
    public List<ComplaintDTO> getComplaintList() {
        return complaintRepository
                .findAll()
                .stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    @Cacheable(value = "complaints", key = "'assigned_to-' + #employeeId")
    public List<ComplaintDTO> getComplaintsAssignedTo(String employeeId) {
        List<Complaint> complaints = complaintRepository.findByTechnicianDetailsEmployeeId(employeeId);
        return complaints
                .stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    @Cacheable(value = "complaint", key = "'complaint-' + #complaintId")
    public ComplaintDTO getComplaintById(String complaintId) {
        return complaintMapper.toDto(complaintRepository.findByComplaintId(complaintId));
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "complaints", allEntries = true),
                    @CacheEvict(cacheNames = "complaint", key = "'complaint-' + #complaintDTO.complaintId")
            },
            put = @CachePut(value = "complaint", key = "'update-' + #complaintDTO.complaintId")
    )
    public ComplaintDTO updateComplaint(ComplaintDTO complaintDTO) {
        Complaint complaint = complaintMapper.toEntity(complaintDTO);
        complaint.setComplaintReference(complaintDTO.getComplaintId().replaceAll("\\D", "").trim());
        complaint.setComplaintId(complaintDTO.getComplaintId());
        complaint.setContactNumber(PhoneNumberFormatter.formatPhoneNumber(complaint.getContactNumber()));
        complaint.setUpdatedAt(LocalDateTime.now());
        if(complaintDTO.getTechnicianDetails().getEmployeeId().isEmpty()) {
            complaint.setComplaintState(ComplaintState.SUBMITTED);
        }
        if(!complaintDTO.getTechnicianDetails().getPhoneNumber().isEmpty()) {
            complaint.getTechnicianDetails().setPhoneNumber(PhoneNumberFormatter.formatPhoneNumber(
                    complaintDTO.getTechnicianDetails().getPhoneNumber())
            );
        }
        if (!complaintDTO.getTechnicianDetails().getEmployeeId().isEmpty()) {
            if (complaintDTO.getComplaintState().equals(ComplaintState.SUBMITTED)) {
                complaint.setComplaintState(ComplaintState.ASSIGNED);
            }
            if(complaintDTO.getStatus().equals(ComplaintStatus.RESOLVED)) {
                complaint.setComplaintState(ComplaintState.CLOSED);
                complaint.setClosedAt(LocalDateTime.now());
            }
        }
        complaintRepository.save(complaint);
        ComplaintDTO updatedComplaintDTO = complaintMapper.toDto(complaint);
        updatedComplaintDTO.setCreatedAt(complaintDTO.getCreatedAt());
        return updatedComplaintDTO;
    }
}