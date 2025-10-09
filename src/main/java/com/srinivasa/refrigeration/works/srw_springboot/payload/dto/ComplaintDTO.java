package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import com.srinivasa.refrigeration.works.srw_springboot.utils.complaintUtils.ComplaintState;
import com.srinivasa.refrigeration.works.srw_springboot.utils.complaintUtils.ComplaintStatus;
import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.complaintGroups.ComplaintRegisterGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.complaintGroups.ComplaintUpdateGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.complaintGroups.TechnicianDetailsGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validations.brandValidation.ValidBrand;
import com.srinivasa.refrigeration.works.srw_springboot.validations.complaintIdValidation.ValidComplaintId;
import com.srinivasa.refrigeration.works.srw_springboot.validations.productModelValidation.ValidProductModel;
import com.srinivasa.refrigeration.works.srw_springboot.validations.productTypeValidation.ValidProductType;
import com.srinivasa.refrigeration.works.srw_springboot.validations.technicianReassignedValidation.ValidTechnicianReassignment;
import com.srinivasa.refrigeration.works.srw_springboot.validations.userIdValidation.ValidUserId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidBrand(groups = {ComplaintRegisterGroup.class, ComplaintUpdateGroup.class})
@ValidProductModel(groups = {ComplaintRegisterGroup.class, ComplaintUpdateGroup.class})
@ValidTechnicianReassignment(groups = {ComplaintUpdateGroup.class})
public class ComplaintDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 62L;

    @ValidComplaintId(groups = {ComplaintUpdateGroup.class})
    private String complaintId;

    @ValidUserId(
            requiredMessage = "Booked by ID is required.",
            message = "Invalid booked by ID format.",
            groups = {ComplaintRegisterGroup.class, ComplaintUpdateGroup.class}
    )
    private String bookedById;

    @NotBlank(
            message = "Customer name is required.",
            groups = {ComplaintRegisterGroup.class, ComplaintUpdateGroup.class}
    )
    @Size(
            min = 4,
            message = "Customer name must be at least 4 characters.",
            groups = {ComplaintRegisterGroup.class, ComplaintUpdateGroup.class}
    )
    private String customerName;

    @NotBlank(
            message = "Contact number is required.",
            groups = {ComplaintRegisterGroup.class, ComplaintUpdateGroup.class}
    )
    @Pattern(
            regexp = FieldValidationConstants.PHONE_NUMBER_REGEX,
            message = "Invalid contact number format.",
            groups = {ComplaintRegisterGroup.class, ComplaintUpdateGroup.class}
    )
    private String contactNumber;

    private String email;

    @NotNull(
            message = "Address is required.",
            groups = {ComplaintRegisterGroup.class, ComplaintUpdateGroup.class}
    )
    @Valid
    private AddressDTO address;

    @ValidProductType(groups = {ComplaintRegisterGroup.class, ComplaintUpdateGroup.class})
    private String productType;

    private String brand;

    private String productModel;

    @NotBlank(
            message = "Description is required.",
            groups = {ComplaintRegisterGroup.class, ComplaintUpdateGroup.class}
    )
    @Size(
            min = 10,
            message = "Description must be at least 10 characters.",
            groups = {ComplaintRegisterGroup.class, ComplaintUpdateGroup.class}
    )
    private String description;

    @NotNull(
            message = "Created at is required.",
            groups = {ComplaintUpdateGroup.class}
    )
    private LocalDateTime createdAt;

    @NotNull(
            message = "Complaint status is required.",
            groups = {ComplaintUpdateGroup.class}
    )
    private ComplaintStatus status;

    private LocalDateTime updatedAt;

    @NotNull(
            message = "Technician details is required.",
            groups = {TechnicianDetailsGroup.class}
    )
    @Valid
    private TechnicianDetailsDTO technicianDetails;

    private LocalDateTime closedAt;
    private String technicianFeedback;
    private String customerFeedback;
    private LocalDateTime reopenedAt;

    @NotNull(
            message = "Complaint state is required.",
            groups = {ComplaintUpdateGroup.class}
    )
    private ComplaintState complaintState;

    private String initialAssigneeId;
}