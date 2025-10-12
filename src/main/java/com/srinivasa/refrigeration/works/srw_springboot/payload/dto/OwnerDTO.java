package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.UserStatus;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.ownerGroups.OwnerRegisterGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.ownerGroups.OwnerUpdateGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validations.dateOfBirthValidation.ValidDateOfBirth;
import com.srinivasa.refrigeration.works.srw_springboot.validations.genderValidation.ValidGender;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 41L;

    @ValidUserId(
            requiredMessage = "Owner ID is required.",
            message = "Invalid owner ID format.",
            groups = {OwnerUpdateGroup.class}
    )
    private String ownerId;

    @NotBlank(
            message = "First name is required.",
            groups = {OwnerRegisterGroup.class, OwnerUpdateGroup.class}
    )
    @Size(
            min = 3,
            message = "First name must be at least 3 characters.",
            groups = {OwnerRegisterGroup.class, OwnerUpdateGroup.class}
    )
    private String firstName;

    @NotBlank(
            message = "Last name is required.",
            groups = {OwnerRegisterGroup.class, OwnerUpdateGroup.class}
    )
    @Size(
            min = 3,
            message = "Last name must be at least 3 characters.",
            groups = {OwnerRegisterGroup.class, OwnerUpdateGroup.class}
    )
    private String lastName;

    @ValidDateOfBirth(groups = {OwnerRegisterGroup.class, OwnerUpdateGroup.class})
    private LocalDate dateOfBirth;

    @ValidGender(groups = {OwnerRegisterGroup.class, OwnerUpdateGroup.class})
    private String gender;

    @NotBlank(
            message = "Phone number is required.",
            groups = {OwnerRegisterGroup.class, OwnerUpdateGroup.class}
    )
    @Pattern(
            regexp = FieldValidationConstants.PHONE_NUMBER_REGEX,
            message = "Invalid phone number format.",
            groups = {OwnerRegisterGroup.class, OwnerUpdateGroup.class}
    )
    private String phoneNumber;

    private String phoneNumberOtp;

    @NotBlank(
            message = "Email is required.",
            groups = {OwnerRegisterGroup.class, OwnerUpdateGroup.class}
    )
    @Pattern(
            regexp = FieldValidationConstants.EMAIL_REGEX,
            message = "Invalid email format.",
            groups = {OwnerRegisterGroup.class, OwnerUpdateGroup.class}
    )
    private String email;

    private String emailOtp;

    @NotBlank(
            message = "Address is required.",
            groups = {OwnerRegisterGroup.class, OwnerUpdateGroup.class}
    )
    @Size(
            min = 10,
            message = "Address must be at least 10 characters.",
            groups = {OwnerRegisterGroup.class, OwnerUpdateGroup.class}
    )
    private String address;

    @NotBlank(
            message = "National ID number is required.",
            groups = {OwnerRegisterGroup.class, OwnerUpdateGroup.class}
    )
    @Pattern(
            regexp = FieldValidationConstants.NATIONAL_ID_NUMBER_REGEX,
            message = "Invalid national ID number format.",
            groups = {OwnerRegisterGroup.class, OwnerUpdateGroup.class}
    )
    private String nationalIdNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserStatus status;
}