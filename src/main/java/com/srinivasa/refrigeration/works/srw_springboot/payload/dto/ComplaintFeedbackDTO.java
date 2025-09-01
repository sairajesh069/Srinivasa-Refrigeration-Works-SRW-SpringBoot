package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintFeedbackDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 67L;

    private String complaintId;
    private String customerFeedback;
}