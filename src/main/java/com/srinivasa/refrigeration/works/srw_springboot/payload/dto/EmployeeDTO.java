package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.UserStatus;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.employeeGroups.EmployeeRegisterGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.employeeGroups.EmployeeUpdateGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validations.dateOfBirthValidation.ValidDateOfBirth;
import com.srinivasa.refrigeration.works.srw_springboot.validations.genderValidation.ValidGender;
import com.srinivasa.refrigeration.works.srw_springboot.validations.userIdValidation.ValidUserId;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 51L;

    @ValidUserId(
            requiredMessage = "Employee ID is required.",
            message = "Invalid employee ID format.",
            groups = {EmployeeUpdateGroup.class}
    )
    private String employeeId;

    @NotBlank(
            message = "First name is required.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    @Size(
            min = 3,
            message = "First name must be at least 3 characters.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    private String firstName;

    @NotBlank(
            message = "Last name is required.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    @Size(
            min = 3,
            message = "Last name must be at least 3 characters.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    private String lastName;

    @ValidDateOfBirth(groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class})
    private LocalDate dateOfBirth;

    @ValidGender(groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class})
    private String gender;

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
            message = "Email is required.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    @Pattern(
            regexp = FieldValidationConstants.EMAIL_REGEX,
            message = "Invalid email format.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    private String email;

    @NotBlank(
            message = "Address is required.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    @Size(
            min = 10,
            message = "Address must be at least 10 characters.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    private String address;

    @NotBlank(
            message = "National ID number is required.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    @Pattern(
            regexp = FieldValidationConstants.NATIONAL_ID_NUMBER_REGEX,
            message = "Invalid national ID number format.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    private String nationalIdNumber;

    private LocalDateTime dateOfHire;

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

    @NotNull(
            message = "Salary is required.",
            groups = {EmployeeRegisterGroup.class, EmployeeUpdateGroup.class}
    )
    @Min(
            value = 100,
            message = "Salary must be minimum 100 rupees."
    )
    private Long salary;

    private LocalDateTime dateOfExit;
    private LocalDateTime updatedAt;
    private UserStatus status;
}