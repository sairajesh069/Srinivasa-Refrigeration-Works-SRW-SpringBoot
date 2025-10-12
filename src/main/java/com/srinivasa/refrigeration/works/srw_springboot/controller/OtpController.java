package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.response.OtpResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.OtpService;
import com.srinivasa.refrigeration.works.srw_springboot.validations.userIdentifierValidation.ValidUserIdentifier;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/srw/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<OtpResponseBody> sendOtp(@Valid
                                                   @RequestBody
                                                   @ValidUserIdentifier
                                                   String userIdentifier) {

        otpService.generateOtp(userIdentifier);

        OtpResponseBody successResponse = new OtpResponseBody(
                "OTP has been sent successfully to " + userIdentifier + ".",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(successResponse);
    }
}