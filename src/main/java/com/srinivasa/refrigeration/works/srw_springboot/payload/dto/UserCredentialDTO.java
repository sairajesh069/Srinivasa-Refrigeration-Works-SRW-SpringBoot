package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import com.srinivasa.refrigeration.works.srw_springboot.validations.userIdValidation.ValidUserId;
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
public class UserCredentialDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 31L;

    @ValidUserId
    private String userId;

    @NotBlank(message = "Username is required.")
    @Pattern(
            regexp = FieldValidationConstants.USERNAME_REGEX,
            message = "Invalid username format."
    )
    private String username;

    @NotBlank(message = "Password is required.")
    @Pattern(
            regexp = FieldValidationConstants.PASSWORD_REGEX,
            message = "Invalid password format."
    )
    private String password;

    @NotBlank(message = "Phone number is required.")
    @Pattern(
            regexp = FieldValidationConstants.PHONE_NUMBER_REGEX,
            message = "Invalid phone number format."
    )
    private String phoneNumber;

    @NotBlank(message = "Email is required.")
    @Pattern(
            regexp = FieldValidationConstants.EMAIL_REGEX,
            message = "Invalid email format."
    )
    private String email;

    private short agreedToTerms;
    private LocalDateTime updatedAt;
}