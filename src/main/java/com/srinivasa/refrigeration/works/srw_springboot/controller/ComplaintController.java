package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintFeedbackDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.UpdateComplaintStateDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.*;
import com.srinivasa.refrigeration.works.srw_springboot.service.ComplaintService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.DuplicateValueCheck;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.complaintGroups.ComplaintRegisterGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.complaintGroups.ComplaintUpdateGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.complaintGroups.TechnicianDetailsGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validations.complaintIdValidation.ValidComplaintId;
import com.srinivasa.refrigeration.works.srw_springboot.validations.userIdValidation.ValidUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/srw/complaint")
@RequiredArgsConstructor
@Validated
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping("/register")
    public ResponseEntity<ComplaintResponseBody<ComplaintDTO>> registerComplaint(
            @Validated(ComplaintRegisterGroup.class) @RequestBody ComplaintDTO complaintDTO) {

        try {
            ComplaintDTO registeredComplaint = complaintService.registerComplaint(complaintDTO);

            ComplaintResponseBody<ComplaintDTO> successResponse = new ComplaintResponseBody<ComplaintDTO>(
                    "Complaint registered successfully.",
                    HttpStatus.OK.value(),
                    registeredComplaint
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            ComplaintResponseBody<ComplaintDTO> errorResponse = new ComplaintResponseBody<ComplaintDTO>(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("complaints", exception),
                    HttpStatus.CONFLICT.value(),
                    complaintDTO
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @GetMapping("/raised-by")
    public ResponseEntity<ComplaintsResponseBody> fetchMyComplaints(@RequestParam("userId") @ValidUserId String userId) {

        List<ComplaintDTO> myComplaints = complaintService.getComplaintsRaisedBy(userId);

        ComplaintsResponseBody successResponse = new ComplaintsResponseBody(
                "Fetched complaints registered by " + userId + " successfully.",
                HttpStatus.OK.value(),
                myComplaints
        );
        return ResponseEntity.ok(successResponse);

    }

    @GetMapping("/list")
    public ResponseEntity<ComplaintsResponseBody> fetchAllComplaints() {

        List<ComplaintDTO> allComplaints = complaintService.getComplaintList();

        ComplaintsResponseBody successResponse = new ComplaintsResponseBody(
                "Fetched complaints successfully.",
                HttpStatus.OK.value(),
                allComplaints
        );
        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/assigned-to")
    public ResponseEntity<ComplaintsResponseBody> fetchComplaintsAssignedTo(
            @RequestParam("employeeId")
            @ValidUserId(
                    requiredMessage = "Employee ID is required.",
                    message = "Invalid employee ID format."
            )
            String employeeId) {

        List<ComplaintDTO> assignedComplaints = complaintService.getComplaintsAssignedTo(employeeId);

        ComplaintsResponseBody successResponse = new ComplaintsResponseBody(
                "Fetched complaints assigned to " + employeeId + " successfully.",
                HttpStatus.OK.value(),
                assignedComplaints
        );
        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/by-id")
    public ResponseEntity<ComplaintResponseBody<ComplaintDTO>> fetchComplaintById(
            @RequestParam("complaintId") @ValidComplaintId String complaintId) {

        ComplaintDTO complaint = complaintService.getComplaintById(complaintId);

        ComplaintResponseBody<ComplaintDTO> successResponse = new ComplaintResponseBody<ComplaintDTO>(
                "Fetched complaint " + complaintId + " details successfully.",
                HttpStatus.OK.value(),
                complaint
        );
        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<ComplaintResponseBody<ComplaintDTO>> updateComplaint(
            @Validated({ComplaintUpdateGroup.class, TechnicianDetailsGroup.class})
            @RequestBody ComplaintDTO complaintDTO) {

        try {
            ComplaintDTO updatedComplaint = complaintService.updateComplaint(complaintDTO);

            ComplaintResponseBody<ComplaintDTO> successResponse = new ComplaintResponseBody<>(
                    "Complaint updated successfully.",
                    HttpStatus.OK.value(),
                    updatedComplaint
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            ComplaintResponseBody<ComplaintDTO> errorResponse = new ComplaintResponseBody<>(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("complaints", exception),
                    HttpStatus.CONFLICT.value(),
                    complaintDTO
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @GetMapping("/resolved-list")
    public ResponseEntity<ComplaintsResponseBody> fetchResolvedComplaints(@RequestParam("userId") @ValidUserId String userId) {

        List<ComplaintDTO> resolvedComplaints = complaintService.getResolvedComplaints(userId);

        ComplaintsResponseBody successResponse = new ComplaintsResponseBody(
                "Fetched list of resolved complaints successfully.",
                HttpStatus.OK.value(),
                resolvedComplaints
        );
        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/user-feedback")
    public ResponseEntity<ComplaintFeedbackResponseBody> saveUserFeedback(@Valid @RequestBody ComplaintFeedbackDTO complaintFeedbackDTO) {

        complaintService.saveUserFeedback(complaintFeedbackDTO);

        ComplaintFeedbackResponseBody successResponse = new ComplaintFeedbackResponseBody(
                "Complaint feedback saved successfully.",
                HttpStatus.OK.value(),
                complaintFeedbackDTO
        );
        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/update-state")
    public ResponseEntity<ComplaintResponseBody<UpdateComplaintStateDTO>> updateComplaintState(
            @Valid @RequestBody UpdateComplaintStateDTO updateComplaintStateDTO) {

        complaintService.updateState(updateComplaintStateDTO);

        ComplaintResponseBody<UpdateComplaintStateDTO> successResponse = new ComplaintResponseBody<>(
                "Complaint state updated successfully.",
                HttpStatus.OK.value(),
                updateComplaintStateDTO
        );
        return ResponseEntity.ok(successResponse);
    }
}