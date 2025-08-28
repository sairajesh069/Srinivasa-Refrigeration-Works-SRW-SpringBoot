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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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

    @Cacheable(value = "complaints", key = "'my_complaint_list-' + #userId")
    public List<ComplaintDTO> getComplaintsRaisedBy(String userId) {
        return complaintRepository
                .findByBookedById(userId)
                .stream()
                .map(complaintMapper::toDto)
                .toList();
    }
}