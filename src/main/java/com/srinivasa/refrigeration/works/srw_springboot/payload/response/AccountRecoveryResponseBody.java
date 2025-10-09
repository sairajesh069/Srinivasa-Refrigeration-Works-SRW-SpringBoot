package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountRecoveryResponseBody extends BaseResponseBody {

    public AccountRecoveryResponseBody(String message, int status) {
        super(message, status);
    }
}