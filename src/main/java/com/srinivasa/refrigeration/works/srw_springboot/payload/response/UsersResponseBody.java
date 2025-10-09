package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UsersResponseBody<UserDTO> extends BaseResponseBody {

    private List<UserDTO> usersDTO;

    public UsersResponseBody(String message, int status, List<UserDTO> usersDTO) {
        super(message, status);
        this.usersDTO = usersDTO;
    }
}