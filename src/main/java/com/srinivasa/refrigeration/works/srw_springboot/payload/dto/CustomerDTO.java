package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.UserStatus;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.customerGroups.CustomerRegisterGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.customerGroups.CustomerUpdateGroup;
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
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 21L;

    @ValidUserId(
            requiredMessage = "Customer ID is required.",
            message = "Invalid customer ID format.",
            groups = {CustomerUpdateGroup.class}
    )
    private String customerId;

    @NotBlank(
            message = "First name is required.",
            groups = {CustomerRegisterGroup.class, CustomerUpdateGroup.class}
    )
    @Size(
            min = 3,
            message = "First name must be at least 3 characters.",
            groups = {CustomerRegisterGroup.class, CustomerUpdateGroup.class}
    )
    private String firstName;

    @NotBlank(
            message = "Last name is required.",
            groups = {CustomerRegisterGroup.class, CustomerUpdateGroup.class}
    )
    @Size(
            min = 3,
            message = "Last name must be at least 3 characters.",
            groups = {CustomerRegisterGroup.class, CustomerUpdateGroup.class}
    )
    private String lastName;

    @ValidGender(groups = {CustomerRegisterGroup.class, CustomerUpdateGroup.class})
    private String gender;

    @NotBlank(
            message = "Phone number is required.",
            groups = {CustomerRegisterGroup.class, CustomerUpdateGroup.class}
    )
    @Pattern(
            regexp = FieldValidationConstants.PHONE_NUMBER_REGEX,
            message = "Invalid phone number format.",
            groups = {CustomerRegisterGroup.class, CustomerUpdateGroup.class}
    )
    private String phoneNumber;

    private String phoneNumberOtp;

    @NotBlank(
            message = "Email is required.",
            groups = {CustomerRegisterGroup.class, CustomerUpdateGroup.class}
    )
    @Pattern(
            regexp = FieldValidationConstants.EMAIL_REGEX,
            message = "Invalid email format.",
            groups = {CustomerRegisterGroup.class, CustomerUpdateGroup.class}
    )
    private String email;

    private String emailOtp;

    @NotBlank(
            message = "Address is required.",
            groups = {CustomerRegisterGroup.class, CustomerUpdateGroup.class}
    )
    @Size(
            min = 10,
            message = "Address must be at least 10 characters.",
            groups = {CustomerRegisterGroup.class, CustomerUpdateGroup.class}
    )
    private String address;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserStatus status;
}