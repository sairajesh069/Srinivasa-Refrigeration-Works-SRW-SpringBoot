package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
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
public class CredentialsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 10L;

    @ValidLoginId()
    private String loginId;

    @NotBlank(message = "Password is required.")
    @Pattern(
            regexp = FieldValidationConstants.PASSWORD_REGEX,
            message = "Invalid password format."
    )
    private String password;
}