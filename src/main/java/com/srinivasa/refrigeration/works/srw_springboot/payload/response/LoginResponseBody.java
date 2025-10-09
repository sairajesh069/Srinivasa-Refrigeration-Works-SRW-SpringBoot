package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.AuthenticatedUserDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginResponseBody extends BaseResponseBody {

    private String token;
    private AuthenticatedUserDTO userDetails;
    private long expiresIn;

    public LoginResponseBody(String message, int status, String token, AuthenticatedUserDTO userDetails, long expiresIn) {
        super(message, status);
        this.token = token;
        this.userDetails = userDetails;
        this.expiresIn = expiresIn;
    }
}