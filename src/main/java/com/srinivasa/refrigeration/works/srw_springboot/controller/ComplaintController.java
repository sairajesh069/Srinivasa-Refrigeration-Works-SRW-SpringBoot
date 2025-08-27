package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.ComplaintResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.ComplaintService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.DuplicateValueCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/srw/complaint")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping("/register")
    public ResponseEntity<ComplaintResponseBody> register(@RequestBody ComplaintDTO complaintDTO) {
        try {
            ComplaintDTO registeredComplaint = complaintService.registerComplaint(complaintDTO);
            ComplaintResponseBody successResponse = new ComplaintResponseBody(
                    "Registered successfully. Please login",
                    HttpStatus.OK.value(),
                    registeredComplaint
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            ComplaintResponseBody errorResponse = new ComplaintResponseBody(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("complaints", exception),
                    HttpStatus.CONFLICT.value(),
                    complaintDTO
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        catch(Exception exception) {
            ComplaintResponseBody errorResponse = new ComplaintResponseBody(
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    complaintDTO
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
