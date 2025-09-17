package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintUpdateResponseBody<DTO> {

    public ComplaintUpdateResponseBody(String message, int status, DTO dto) {
        this.message = message;
        this.status = status;
        this.dto = dto;
    }

    private String message;
    private DTO dto;
    private int status;
    private LocalDateTime timeStamp = LocalDateTime.now();
}
