package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FetchUsersResponseBody<UserDTO> {

    public FetchUsersResponseBody(String message, int status, List<UserDTO> usersDTO) {
        this.message = message;
        this.status = status;
        this.usersDTO = usersDTO;
    }

    private String message;
    private List<UserDTO> usersDTO;
    private int status;
    private LocalDateTime timeStamp = LocalDateTime.now();
}