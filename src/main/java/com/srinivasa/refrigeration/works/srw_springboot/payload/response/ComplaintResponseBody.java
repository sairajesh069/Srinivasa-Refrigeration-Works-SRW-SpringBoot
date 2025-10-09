package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ComplaintResponseBody<DTO> extends BaseResponseBody {

    private DTO dto;

    public ComplaintResponseBody(String message, int status, DTO dto) {
        super(message, status);
        this.dto = dto;
    }
}