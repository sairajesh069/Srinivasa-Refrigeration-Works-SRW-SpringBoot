package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseResponseBody {

    private String message;
    private int status;
    private LocalDateTime timeStamp;

    public BaseResponseBody(String message, int status) {
        this.message = message;
        this.status = status;
        this.timeStamp = LocalDateTime.now();
    }

    public BaseResponseBody() {
        this.timeStamp = LocalDateTime.now();
    }
}