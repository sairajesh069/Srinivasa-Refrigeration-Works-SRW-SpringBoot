package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintFetchResponseBody {

    public ComplaintFetchResponseBody(String message, int status, ComplaintDTO complaintDTO) {
        this.message = message;
        this.status = status;
        this.complaintDTO = complaintDTO;
    }

    private String message;
    private ComplaintDTO complaintDTO;
    private int status;
    private LocalDateTime timeStamp = LocalDateTime.now();
}
