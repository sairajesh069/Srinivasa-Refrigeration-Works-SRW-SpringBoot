package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserCredentialResponseBody extends BaseResponseBody {

    private String username;

    public UserCredentialResponseBody(String message, int status, String username) {
        super(message, status);
        this.username = username;
    }
}