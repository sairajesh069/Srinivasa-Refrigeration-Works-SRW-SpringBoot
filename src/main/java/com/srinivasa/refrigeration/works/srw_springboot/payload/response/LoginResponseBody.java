package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.AuthenticatedUserDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResponseBody {

    public LoginResponseBody(String message, int status, String token, AuthenticatedUserDTO userDetails, long expiresIn) {
        this.message = message;
        this.status = status;
        this.token = token;
        this.userDetails = userDetails;
        this.expiresIn = expiresIn;
    }

    private String message;
    private int status;
    private String token;
    private AuthenticatedUserDTO userDetails;
    private long expiresIn;
    private LocalDateTime timeStamp = LocalDateTime.now();
}
