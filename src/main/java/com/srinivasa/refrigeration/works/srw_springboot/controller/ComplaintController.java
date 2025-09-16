package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintFeedbackDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.*;
import com.srinivasa.refrigeration.works.srw_springboot.service.ComplaintService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.DuplicateValueCheck;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/srw/complaint")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping("/register")
    public ResponseEntity<ComplaintRegisterResponseBody> registerComplaint(@RequestBody ComplaintDTO complaintDTO) {
        try {
            ComplaintDTO registeredComplaint = complaintService.registerComplaint(complaintDTO);
            ComplaintRegisterResponseBody successResponse = new ComplaintRegisterResponseBody(
                    "Complaint registered successfully.",
                    HttpStatus.OK.value(),
                    registeredComplaint
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            ComplaintRegisterResponseBody errorResponse = new ComplaintRegisterResponseBody(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("complaints", exception),
                    HttpStatus.CONFLICT.value(),
                    complaintDTO
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        catch(Exception exception) {
            ComplaintRegisterResponseBody errorResponse = new ComplaintRegisterResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    complaintDTO
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/raised-by")
    public ResponseEntity<ComplaintsFetchResponseBody> fetchMyComplaints(@RequestParam("userId") String userId, HttpServletRequest request) {
        try {
            List<ComplaintDTO> myComplaints = complaintService.getComplaintsRaisedBy(userId, request);
            ComplaintsFetchResponseBody successResponse = new ComplaintsFetchResponseBody(
                    "Fetched complaints registered by " + userId + " successfully.",
                    HttpStatus.OK.value(),
                    myComplaints
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(SecurityException exception) {
            ComplaintsFetchResponseBody errorResponse = new ComplaintsFetchResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.FORBIDDEN.value(),
                    null
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        catch(Exception exception) {
            ComplaintsFetchResponseBody errorResponse = new ComplaintsFetchResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ComplaintsFetchResponseBody> fetchAllComplaints() {
        try {
            List<ComplaintDTO> allComplaints = complaintService.getComplaintList();
            ComplaintsFetchResponseBody successResponse = new ComplaintsFetchResponseBody(
                    "Fetched complaints successfully.",
                    HttpStatus.OK.value(),
                    allComplaints
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(Exception exception) {
            ComplaintsFetchResponseBody errorResponse = new ComplaintsFetchResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/assigned-to")
    public ResponseEntity<ComplaintsFetchResponseBody> fetchComplaintsAssignedTo(@RequestParam("employeeId") String employeeId, HttpServletRequest request) {
        try {
            List<ComplaintDTO> assignedComplaints = complaintService.getComplaintsAssignedTo(employeeId, request);
            ComplaintsFetchResponseBody successResponse = new ComplaintsFetchResponseBody(
                    "Fetched complaints assigned to " + employeeId + " successfully.",
                    HttpStatus.OK.value(),
                    assignedComplaints
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(SecurityException exception) {
            ComplaintsFetchResponseBody errorResponse = new ComplaintsFetchResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.FORBIDDEN.value(),
                    null
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        catch(Exception exception) {
            ComplaintsFetchResponseBody errorResponse = new ComplaintsFetchResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/by-id")
    public ResponseEntity<ComplaintFetchResponseBody> fetchComplaintById(@RequestParam("complaintId") String complaintId, HttpServletRequest request) {
        try {
            ComplaintDTO complaint = complaintService.getComplaintById(complaintId, request);
            ComplaintFetchResponseBody successResponse = new ComplaintFetchResponseBody(
                    "Fetched complaint " + complaintId + " details successfully.",
                    HttpStatus.OK.value(),
                    complaint
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(SecurityException exception) {
            ComplaintFetchResponseBody errorResponse = new ComplaintFetchResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.FORBIDDEN.value(),
                    null
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        catch(Exception exception) {
            ComplaintFetchResponseBody errorResponse = new ComplaintFetchResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ComplaintUpdateResponseBody> updateComplaint(@RequestBody ComplaintDTO complaintDTO) {
        try {
            ComplaintDTO updatedComplaint = complaintService.updateComplaint(complaintDTO);
            ComplaintUpdateResponseBody successResponse = new ComplaintUpdateResponseBody(
                    "Complaint updated successfully.",
                    HttpStatus.OK.value(),
                    updatedComplaint
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            ComplaintUpdateResponseBody errorResponse = new ComplaintUpdateResponseBody(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("complaints", exception),
                    HttpStatus.CONFLICT.value(),
                    complaintDTO
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        catch(Exception exception) {
            ComplaintUpdateResponseBody errorResponse = new ComplaintUpdateResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    complaintDTO
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/resolved-list")
    public ResponseEntity<ComplaintsFetchResponseBody> fetchResolvedComplaints(@RequestParam("userId") String userId, HttpServletRequest request) {
        try {
            List<ComplaintDTO> resolvedComplaints = complaintService.getResolvedComplaints(userId, request);
            ComplaintsFetchResponseBody successResponse = new ComplaintsFetchResponseBody(
                    "Fetched list of resolved complaints successfully.",
                    HttpStatus.OK.value(),
                    resolvedComplaints
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(SecurityException exception) {
            ComplaintsFetchResponseBody errorResponse = new ComplaintsFetchResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.FORBIDDEN.value(),
                    null
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        catch(Exception exception) {
            ComplaintsFetchResponseBody errorResponse = new ComplaintsFetchResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/user-feedback")
    public ResponseEntity<ComplaintFeedbackResponseBody> saveUserFeedback(@RequestBody ComplaintFeedbackDTO complaintFeedbackDTO) {
        try {
            complaintService.saveUserFeedback(complaintFeedbackDTO);
            ComplaintFeedbackResponseBody successResponse = new ComplaintFeedbackResponseBody(
                    "Complaint feedback saved successfully.",
                    HttpStatus.OK.value(),
                    complaintFeedbackDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(Exception exception) {
            ComplaintFeedbackResponseBody errorResponse = new ComplaintFeedbackResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    complaintFeedbackDTO
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}