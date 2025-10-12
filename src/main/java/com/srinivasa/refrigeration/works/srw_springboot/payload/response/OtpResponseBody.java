package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OtpResponseBody extends BaseResponseBody {

    public OtpResponseBody(String message, int status) {
        super(message, status);
    }
}