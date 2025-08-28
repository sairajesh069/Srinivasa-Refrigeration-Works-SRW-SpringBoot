package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.ComplaintRegisterResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.ComplaintsFetchResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.ComplaintService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.DuplicateValueCheck;
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
    public ResponseEntity<ComplaintRegisterResponseBody> register(@RequestBody ComplaintDTO complaintDTO) {
        try {
            ComplaintDTO registeredComplaint = complaintService.registerComplaint(complaintDTO);
            ComplaintRegisterResponseBody successResponse = new ComplaintRegisterResponseBody(
                    "Registered successfully. Please login",
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

    @GetMapping("/raisedBy")
    public ResponseEntity<ComplaintsFetchResponseBody> fetchMyComplaints(@RequestParam("userId") String userId) {
        try {
            List<ComplaintDTO> myComplaints = complaintService.getComplaintsRaisedBy(userId);
            ComplaintsFetchResponseBody successResponse = new ComplaintsFetchResponseBody(
                    "Fetched complaints registered by " + userId + " successfully.",
                    HttpStatus.OK.value(),
                    myComplaints
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
}