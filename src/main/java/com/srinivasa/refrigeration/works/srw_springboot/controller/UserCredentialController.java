package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ChangePasswordDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserCredentialResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.UserCredentialService;
import com.srinivasa.refrigeration.works.srw_springboot.exceptions.UserValidationException;
import com.srinivasa.refrigeration.works.srw_springboot.validations.userIdValidation.ValidUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/srw/user")
@RequiredArgsConstructor
@Validated
public class UserCredentialController {

    private final UserCredentialService userCredentialService;

    @GetMapping("/fetch-username")
    public ResponseEntity<UserCredentialResponseBody> fetchUsername(@RequestParam("userId") @ValidUserId String userId) {

        String username = userCredentialService.fetchUsername(userId);
        if(username == null) {
            throw new UserValidationException("User doesn't exist.");
        }

        UserCredentialResponseBody successResponse = new UserCredentialResponseBody(
                "We found your account. Your username is " + username + ".",
                HttpStatus.OK.value(),
                username
        );
        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/change-password")
    public ResponseEntity<UserCredentialResponseBody> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {

        userCredentialService.updatePassword(changePasswordDTO, true);

        UserCredentialResponseBody successResponse = new UserCredentialResponseBody(
                "Password has been reset successfully.",
                HttpStatus.OK.value(),
                changePasswordDTO.getUsername()
        );
        return ResponseEntity.ok(successResponse);
    }
}