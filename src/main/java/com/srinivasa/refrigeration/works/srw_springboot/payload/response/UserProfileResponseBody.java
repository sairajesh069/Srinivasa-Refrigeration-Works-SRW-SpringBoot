package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserProfileResponseBody<UserDTO> {

    public UserProfileResponseBody(String message, int status, UserDTO userDTO) {
        this.message = message;
        this.status = status;
        this.userDTO = userDTO;
    }

    private String message;
    private UserDTO userDTO;
    private int status;
    private LocalDateTime timeStamp = LocalDateTime.now();
}
