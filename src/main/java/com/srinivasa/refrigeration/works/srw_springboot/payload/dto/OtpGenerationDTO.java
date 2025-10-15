package com.srinivasa.refrigeration.works.srw_springboot.payload.dto;

import com.srinivasa.refrigeration.works.srw_springboot.utils.otpUtils.IdentifierType;
import com.srinivasa.refrigeration.works.srw_springboot.utils.otpUtils.OtpPurpose;
import com.srinivasa.refrigeration.works.srw_springboot.validations.userIdentifierValidation.ValidUserIdentifier;
import jakarta.validation.constraints.NotNull;
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
public class OtpGenerationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 13L;

    @ValidUserIdentifier
    private String userIdentifier;

    @NotNull(message = "OTP purpose is required")
    private OtpPurpose otpPurpose;

    @NotNull(message = "Identifier type is required")
    private IdentifierType identifierType;
}