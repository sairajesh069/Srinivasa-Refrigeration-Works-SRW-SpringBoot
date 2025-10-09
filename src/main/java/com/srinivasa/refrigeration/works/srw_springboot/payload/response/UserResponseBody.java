package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponseBody<UserDTO> extends BaseResponseBody {

    private UserDTO userDTO;

    public UserResponseBody(String message, int status, UserDTO userDTO) {
        super(message, status);
        this.userDTO = userDTO;
    }
}