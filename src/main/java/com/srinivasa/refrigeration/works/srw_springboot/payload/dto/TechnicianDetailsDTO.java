package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.employeeGroups.EmployeeRegisterGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.employeeGroups.EmployeeUpdateGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validations.userIdValidation.ValidUserId;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class TechnicianDetailsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 65L;

    @ValidUserId(
            requiredMessage = "Employee ID is required.",
            message = "Invalid employee ID format.",
            groups = {EmployeeUpdateGroup.class}
    )
    private String employeeId;

    @NotBlank(
            message = "Full name is required.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    @Size(
            min = 7,
            message = "Full name must be at least 7 characters.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    private String fullName;

    @NotBlank(
            message = "Phone number is required.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    @Pattern(
            regexp = FieldValidationConstants.PHONE_NUMBER_REGEX,
            message = "Invalid phone number format.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    private String phoneNumber;

    @NotBlank(
            message = "Designation is required.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    @Size(
            min = 3,
            message = "Designation must be at least 3 characters.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    private String designation;
}