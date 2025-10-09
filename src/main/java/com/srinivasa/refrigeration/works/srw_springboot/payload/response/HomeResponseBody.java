package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HomeResponseBody extends BaseResponseBody {

    public HomeResponseBody(String message, int status) {
        super(message, status);
    }
}