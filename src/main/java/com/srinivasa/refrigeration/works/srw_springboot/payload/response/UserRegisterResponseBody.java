package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CustomerDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRegisterResponseBody<UserDTO> {

    public UserRegisterResponseBody(String message, int status, UserDTO userDTO) {
        this.message = message;
        this.status = status;
        this.userDTO = userDTO;
    }

    private String message;
    private UserDTO userDTO;
    private int status;
    private LocalDateTime timeStamp = LocalDateTime.now();
}
