package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalExceptionErrorResponse {

    private String message;
    private int status;
    private String path;
    private String error;
    private LocalDateTime timestamp;

}