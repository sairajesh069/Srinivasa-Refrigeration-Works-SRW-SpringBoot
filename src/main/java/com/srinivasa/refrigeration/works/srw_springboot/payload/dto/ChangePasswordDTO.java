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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 32L;

    @ValidUserId
    private String userId;

    @NotBlank(message = "Username is required.")
    @Pattern(
            regexp = FieldValidationConstants.USERNAME_REGEX,
            message = "Invalid username format."
    )
    private String username;

    @NotBlank(message = "Old password is required.")
    @Pattern(
            regexp = FieldValidationConstants.PASSWORD_REGEX,
            message = "Invalid old password format."
    )
    private String oldPassword;

    @NotBlank(message = "New password is required.")
    @Pattern(
            regexp = FieldValidationConstants.PASSWORD_REGEX,
            message = "Invalid new password format."
    )
    private String newPassword;
}