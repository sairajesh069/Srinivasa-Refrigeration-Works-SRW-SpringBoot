package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogoutResponseBody {

    public LogoutResponseBody(String message, int status) {
        this.message = message;
        this.status = status;
    }

    private String message;
    private int status;
    private LocalDateTime timeStamp = LocalDateTime.now();
}
