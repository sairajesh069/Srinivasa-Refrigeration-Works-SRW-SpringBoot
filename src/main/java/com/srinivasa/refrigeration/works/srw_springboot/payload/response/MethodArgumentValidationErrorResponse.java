package com.srinivasa.refrigeration.works.srw_springboot.payload.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodArgumentValidationErrorResponse {

    private String message;
    private int status;
    private String path;
    private String error;
    private Map<String, String> errors;
    private LocalDateTime timestamp;

}