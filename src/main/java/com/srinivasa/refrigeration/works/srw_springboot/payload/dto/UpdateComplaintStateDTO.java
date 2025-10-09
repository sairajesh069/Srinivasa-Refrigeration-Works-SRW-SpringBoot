package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import com.srinivasa.refrigeration.works.srw_springboot.utils.complaintUtils.ComplaintState;
import com.srinivasa.refrigeration.works.srw_springboot.validations.complaintIdValidation.ValidComplaintId;
import com.srinivasa.refrigeration.works.srw_springboot.validations.productTypeValidation.ValidProductType;
import com.srinivasa.refrigeration.works.srw_springboot.validations.userIdValidation.ValidUserId;
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
public class UpdateComplaintStateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 68L;

    @ValidComplaintId
    private String complaintId;

    private ComplaintState complaintState;

    @ValidUserId(
            requiredMessage = "Assigned to ID is required.",
            message = "Invalid assigned to ID format."
    )
    private String assignedTo;

    @ValidUserId(
            requiredMessage = "Booked by ID is required.",
            message = "Invalid booked by ID format."
    )
    private String bookedById;

    @ValidProductType()
    private String productType;
}