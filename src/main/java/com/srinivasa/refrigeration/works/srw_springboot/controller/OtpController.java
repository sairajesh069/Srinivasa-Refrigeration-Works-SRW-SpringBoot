package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.OtpGenerationDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.OtpResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.OtpService;
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
    public ResponseEntity<OtpResponseBody> sendOtp(@Valid @RequestBody OtpGenerationDTO otpGenerationDTO) {

        otpService.generateOtp(otpGenerationDTO);

        OtpResponseBody successResponse = new OtpResponseBody(
                "OTP has been sent successfully to " + otpGenerationDTO.getUserIdentifier() + ".",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(successResponse);
    }
}