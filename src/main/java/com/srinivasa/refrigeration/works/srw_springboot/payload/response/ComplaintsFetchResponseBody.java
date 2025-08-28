package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ComplaintsFetchResponseBody {

    public ComplaintsFetchResponseBody(String message, int status, List<ComplaintDTO> complaintsDTO) {
        this.message = message;
        this.status = status;
        this.complaintsDTO = complaintsDTO;
    }

    private String message;
    private List<ComplaintDTO> complaintsDTO;
    private int status;
    private LocalDateTime timeStamp = LocalDateTime.now();
}
