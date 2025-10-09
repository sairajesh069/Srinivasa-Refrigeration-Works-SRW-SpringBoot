package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import com.srinivasa.refrigeration.works.srw_springboot.validations.complaintIdValidation.ValidComplaintId;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
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

    @ValidComplaintId
    private String complaintId;

    @NotBlank(message = "Complaint feedback is required.")
    @Size(
            min = 10,
            message = "Complaint feedback must be at least 10 characters."
    )
    private String customerFeedback;
}