package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.accountRecoveryGroups.ForgotPasswordGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.accountRecoveryGroups.ForgotUsernameGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.accountRecoveryGroups.ValidateUserGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validations.loginIdValidation.ValidLoginId;
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
public class AccountRecoveryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 12L;

    @NotBlank(
            message = "Phone number is required.",
            groups = {ForgotUsernameGroup.class, ValidateUserGroup.class, ForgotPasswordGroup.class}
    )
    @Pattern(
            regexp = FieldValidationConstants.PHONE_NUMBER_REGEX,
            message = "Invalid phone number format.",
            groups = {ForgotUsernameGroup.class, ValidateUserGroup.class, ForgotPasswordGroup.class}
    )
    private String phoneNumber;

    @NotBlank(
            message = "OTP is required.",
            groups = {ForgotUsernameGroup.class, ValidateUserGroup.class}
    )
    @Pattern(
            regexp = FieldValidationConstants.OTP_REGEX,
            message = "Invalid OTP format.",
            groups = {ForgotUsernameGroup.class, ValidateUserGroup.class}
    )
    private String otp;

    @ValidLoginId(groups = {ValidateUserGroup.class, ForgotPasswordGroup.class})
    private String loginId;

    @NotBlank(
            message = "Password is required.",
            groups = {ForgotPasswordGroup.class}
    )
    @Pattern(
            regexp = FieldValidationConstants.PASSWORD_REGEX,
            message = "Invalid password format.",
            groups = {ForgotPasswordGroup.class}
    )
    private String password;
}