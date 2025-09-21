package com.srinivasa.refrigeration.works.srw_springboot.service;

import com.srinivasa.refrigeration.works.srw_springboot.entity.Complaint;
import com.srinivasa.refrigeration.works.srw_springboot.mapper.ComplaintMapper;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintFeedbackDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.UpdateComplaintStateDTO;
import com.srinivasa.refrigeration.works.srw_springboot.repository.ComplaintRepository;
import com.srinivasa.refrigeration.works.srw_springboot.utils.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintMapper complaintMapper;
    private final ComplaintRepository complaintRepository;
    private final AccessCheck accessCheck;
    private final NotificationService notificationService;

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
        notificationService.saveNotification(
                NotificationMessages.buildComplaintRegisteredNotification(
                        complaint.getProductType(),
                        complaint.getComplaintId(),
                        LocalDateTime.now()
                )
        );
        return complaintMapper.toDto(complaint);
    }

    @Cacheable(value = "complaints", key = "'raised_by-' + #userId + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.SecurityUtil).getCurrentUserId()")
    public List<ComplaintDTO> getComplaintsRaisedBy(String userId) {
        if(accessCheck.canAccessComplaints(userId)) {
            return complaintRepository
                    .findByBookedById(userId)
                    .stream()
                    .map(complaintMapper::toDto)
                    .toList();
        }
        else {
            notificationService.saveNotification(
                    NotificationMessages.buildUnauthorizedAccessNotification(
                            "another user's complaint list",
                            LocalDateTime.now()
                    )
            );
            throw new SecurityException("Unauthorized access: Attempt to fetch restricted complaints");
        }
    }

    @Cacheable(value = "complaints", key = "'complaint_list'")
    public List<ComplaintDTO> getComplaintList() {
        return complaintRepository
                .findAll()
                .stream()
                .map(complaintMapper::toDto)
                .toList();
    }

    @Cacheable(value = "complaints", key = "'assigned_to-' + #employeeId + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.SecurityUtil).getCurrentUserId()")
    public List<ComplaintDTO> getComplaintsAssignedTo(String employeeId) {
        if(accessCheck.canAccessComplaints(employeeId)) {
            List<Complaint> complaints = complaintRepository.findByTechnicianDetailsEmployeeId(employeeId);
            return complaints
                    .stream()
                    .map(complaintMapper::toDto)
                    .toList();
        }
        else {
            notificationService.saveNotification(
                    NotificationMessages.buildUnauthorizedAccessNotification(
                            "another technician's assigned complaint list",
                            LocalDateTime.now()
                    )
            );
            throw new SecurityException("Unauthorized access: Attempt to fetch restricted complaints");
        }
    }

    @Cacheable(value = "complaint", key = "'complaint-' + #complaintId + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.SecurityUtil).getCurrentUserId()")
    public ComplaintDTO getComplaintById(String complaintId) {
        Complaint complaint = complaintRepository.findByComplaintId(complaintId);
        String assignedToId = complaint.getTechnicianDetails() != null ? complaint.getTechnicianDetails().getEmployeeId() : "";
        if(accessCheck.canAccessComplaint(complaint.getBookedById(), assignedToId)) {
            return complaintMapper.toDto(complaint);
        }
        else {
            notificationService.saveNotification(
                    NotificationMessages.buildUnauthorizedAccessNotification(
                            "another complaint details",
                            LocalDateTime.now()
                    )
            );
            throw new SecurityException("Unauthorized access: Attempt to fetch restricted complaint");
        }
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "complaints", allEntries = true),
                    @CacheEvict(cacheNames = "complaint", key = "'complaint-' + #complaintDTO.complaintId + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.SecurityUtil).getCurrentUserId()")
            },
            put = @CachePut(value = "complaint", key = "'update-' + #complaintDTO.complaintId")
    )
    public ComplaintDTO updateComplaint(ComplaintDTO complaintDTO) {
        boolean isInfoUpdate = true;
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
        if(complaintDTO.getStatus().equals(ComplaintStatus.IN_PROGRESS)) {
            isInfoUpdate = false;
            notificationService.saveNotification(
                    NotificationMessages.buildTechnicianEnRouteNotification(
                            complaint.getBookedById(),
                            complaint.getTechnicianDetails().getFullName(),
                            complaint.getProductType(),
                            complaint.getComplaintId(),
                            LocalDateTime.now(),
                            Duration.ofHours(2),
                            complaint.getTechnicianDetails().getPhoneNumber()
                    )
            );
        }
        if (!complaintDTO.getTechnicianDetails().getEmployeeId().isEmpty()) {
            isInfoUpdate = false;
            if (complaintDTO.getComplaintState().equals(ComplaintState.SUBMITTED)) {
                complaint.setComplaintState(ComplaintState.ASSIGNED);
                notificationService.saveNotification(
                        NotificationMessages.buildComplaintAssignedNotification(
                                complaint.getTechnicianDetails().getEmployeeId(),
                                complaint.getProductType(),
                                complaint.getComplaintId(),
                                complaint.getCustomerName(),
                                complaint.getContactNumber(),
                                LocalDateTime.now()
                        )
                );
                notificationService.saveNotification(
                        NotificationMessages.buildTechnicianAssignedNotification(
                                complaint.getBookedById(),
                                complaint.getTechnicianDetails().getFullName(),
                                complaint.getProductType(),
                                complaint.getComplaintId(),
                                complaint.getTechnicianDetails().getPhoneNumber(),
                                LocalDateTime.now()
                        )
                );
            }
            if(complaintDTO.getStatus().equals(ComplaintStatus.RESOLVED)) {
                complaint.setComplaintState(ComplaintState.CLOSED);
                complaint.setClosedAt(LocalDateTime.now());
                notificationService.saveNotification(
                        NotificationMessages.buildComplaintResolvedNotification(
                                complaint.getBookedById(),
                                complaint.getProductType(),
                                complaint.getComplaintId(),
                                LocalDateTime.now(),
                                complaint.getTechnicianDetails().getFullName()
                        )
                );
                notificationService.saveNotification(
                        NotificationMessages.buildPendingFeedbackNotification(
                                complaint.getBookedById(),
                                complaint.getComplaintId(),
                                complaint.getProductType()
                        )
                );
            }
        }
        if(!(complaintDTO.getInitialAssigneeId() == null || complaintDTO.getInitialAssigneeId().isEmpty()) && !complaintDTO.getTechnicianDetails().getEmployeeId().equals(complaintDTO.getInitialAssigneeId())) {
            isInfoUpdate = false;
            notificationService.saveNotification(
                    NotificationMessages.buildTechnicianReAssignedNotification(
                            complaint.getBookedById(),
                            complaint.getTechnicianDetails().getFullName(),
                            complaint.getProductType(),
                            complaint.getComplaintId(),
                            complaint.getTechnicianDetails().getPhoneNumber()
                    )
            );
            notificationService.saveNotification(
                    NotificationMessages.buildComplaintAssignedNotification(
                            complaint.getTechnicianDetails().getEmployeeId(),
                            complaint.getProductType(),
                            complaint.getComplaintId(),
                            complaint.getCustomerName(),
                            complaint.getContactNumber(),
                            LocalDateTime.now()
                    )
            );
            notificationService.saveNotification(
                    NotificationMessages.buildComplaintTransferedNotification(
                            complaintDTO.getInitialAssigneeId(),
                            complaintDTO.getTechnicianDetails().getEmployeeId(),
                            complaint.getProductType(),
                            complaint.getComplaintId(),
                            complaintDTO.getTechnicianDetails().getFullName(),
                            LocalDateTime.now()
                    )
            );
        }
        complaintRepository.save(complaint);
        if(isInfoUpdate) {
            notificationService.saveNotification(
                    NotificationMessages.buildComplaintUpdatedNotification(
                            complaint.getProductType(),
                            complaint.getComplaintId(),
                            complaint.getBookedById(),
                            LocalDateTime.now()
                    )
            );
        }
        ComplaintDTO updatedComplaintDTO = complaintMapper.toDto(complaint);
        updatedComplaintDTO.setCreatedAt(complaintDTO.getCreatedAt());
        return updatedComplaintDTO;
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "complaints", allEntries = true),
                    @CacheEvict(cacheNames = "complaint", key = "'complaint-' + #complaintFeedbackDTO.complaintId + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.SecurityUtil).getCurrentUserId()")
            },
            put = @CachePut(value = "complaint", key = "'user_feedback-' + #complaintFeedbackDTO.complaintId")
    )
    public void saveUserFeedback(ComplaintFeedbackDTO complaintFeedbackDTO) {
        complaintRepository.saveUserFeedback(complaintFeedbackDTO.getComplaintId(),
                complaintFeedbackDTO.getCustomerFeedback(), LocalDateTime.now());
    }

    @Cacheable(value = "complaints", key = "'resolved_complaint_list-' + #userId + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.SecurityUtil).getCurrentUserId()")
    public List<ComplaintDTO> getResolvedComplaints(String userId) {
        if(accessCheck.canAccessComplaints(userId)) {
            return complaintRepository
                    .findByBookedByIdAndStatus(userId, ComplaintStatus.RESOLVED)
                    .stream()
                    .map(complaintMapper::toDto)
                    .toList();
        }
        else {
            notificationService.saveNotification(
                    NotificationMessages.buildUnauthorizedAccessNotification(
                            "another user's resolved complaint list",
                            LocalDateTime.now()
                    )
            );
            throw new SecurityException("Unauthorized access: Attempt to fetch restricted complaints");
        }
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "complaints", allEntries = true),
                    @CacheEvict(cacheNames = "complaint", key = "'complaint-' + #updateComplaintStateDTO.complaintId + '-user-' + T(com.srinivasa.refrigeration.works.srw_springboot.utils.SecurityUtil).getCurrentUserId()")
            },
            put = @CachePut(value = "complaint", key = "'update_state-' + #updateComplaintStateDTO.complaintId")
    )
    public void updateState(UpdateComplaintStateDTO updateComplaintStateDTO) {
        if(accessCheck.canAccessUpdateComplaintState(updateComplaintStateDTO.getAssignedTo())) {
            complaintRepository.updateState(updateComplaintStateDTO.getComplaintId(),
                    updateComplaintStateDTO.getComplaintState(), LocalDateTime.now());
            if(updateComplaintStateDTO.getComplaintState().equals(ComplaintState.REOPENED)) {
                notificationService.saveNotification(
                        NotificationMessages.buildComplaintReopenedNotification(
                                updateComplaintStateDTO.getProductType(),
                                updateComplaintStateDTO.getComplaintId(),
                                updateComplaintStateDTO.getBookedById(),
                                LocalDateTime.now()
                        )
                );
            }
        }
        else {
            notificationService.saveNotification(
                    NotificationMessages.buildUnauthorizedAccessNotification(
                            "update complaint state",
                            LocalDateTime.now()
                    )
            );
            throw new SecurityException("Unauthorized access: Attempt to update restricted complaint state");
        }
    }
}