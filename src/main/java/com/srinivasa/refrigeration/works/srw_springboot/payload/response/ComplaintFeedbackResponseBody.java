package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintFeedbackDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintFeedbackResponseBody {

    public ComplaintFeedbackResponseBody(String message, int status, ComplaintFeedbackDTO complaintFeedbackDTO) {
        this.message = message;
        this.status = status;
        this.complaintFeedbackDTO = complaintFeedbackDTO;
    }

    private String message;
    private ComplaintFeedbackDTO complaintFeedbackDTO;
    private int status;
    private LocalDateTime timeStamp = LocalDateTime.now();
}