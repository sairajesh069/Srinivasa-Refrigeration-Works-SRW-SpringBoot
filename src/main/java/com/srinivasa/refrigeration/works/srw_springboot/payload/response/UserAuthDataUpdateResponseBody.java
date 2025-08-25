package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAuthDataUpdateResponseBody {

    public UserAuthDataUpdateResponseBody(String message, String username, int status) {
        this.message = message;
        this.username = username;
        this.status = status;
    }

    private String message;
    private String username;
    private int status;
    private LocalDateTime timeStamp = LocalDateTime.now();
}
