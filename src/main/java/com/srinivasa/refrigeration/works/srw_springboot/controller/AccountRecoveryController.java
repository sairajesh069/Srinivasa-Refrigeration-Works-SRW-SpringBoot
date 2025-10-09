package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.AccountRecoveryDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.AccountRecoveryResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.UserCredentialService;
import com.srinivasa.refrigeration.works.srw_springboot.exceptions.UserValidationException;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.accountRecoveryGroups.ForgotPasswordGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.accountRecoveryGroups.ForgotUsernameGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.accountRecoveryGroups.ValidateUserGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/srw")
@RequiredArgsConstructor
public class AccountRecoveryController {

    private final UserCredentialService userCredentialService;

    @PostMapping("/forgot-username")
    public ResponseEntity<AccountRecoveryResponseBody> fetchUsername(
            @Validated(ForgotUsernameGroup.class) @RequestBody AccountRecoveryDTO accountRecoveryDTO) {

        String username = userCredentialService.fetchUsername(accountRecoveryDTO.getPhoneNumber());
        if(username.isEmpty()) {
            throw new UserValidationException("User doesn't exist.");
        }

        AccountRecoveryResponseBody successResponse = new AccountRecoveryResponseBody(
                "We found your account. Your username is " + username + ".",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/validate-user")
    public ResponseEntity<AccountRecoveryResponseBody> validateUser(
            @Validated(ValidateUserGroup.class) @RequestBody AccountRecoveryDTO accountRecoveryDTO) {

        boolean userExists = userCredentialService.validateUser(accountRecoveryDTO);
        if(!userExists) {
            throw new UserValidationException("User doesn't exist.");
        }

        AccountRecoveryResponseBody successResponse = new AccountRecoveryResponseBody(
                "User has been validated successfully.",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AccountRecoveryResponseBody> resetPassword(
            @Validated(ForgotPasswordGroup.class) @RequestBody AccountRecoveryDTO accountRecoveryDTO) {

        userCredentialService.updatePassword(accountRecoveryDTO, false);

        AccountRecoveryResponseBody successResponse = new AccountRecoveryResponseBody(
                "Password has been reset successfully.",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(successResponse);
    }
}