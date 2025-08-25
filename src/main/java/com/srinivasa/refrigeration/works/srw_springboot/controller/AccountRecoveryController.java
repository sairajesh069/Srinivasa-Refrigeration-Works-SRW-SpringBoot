package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.AccountRecoveryDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.AccountRecoveryResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.UserCredentialService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AccountRecoveryResponseBody> fetchUsername(@RequestBody AccountRecoveryDTO accountRecoveryDTO) {
        try {
            String username = userCredentialService.fetchUsername(accountRecoveryDTO.getPhoneNumber());
            if(username == null) {
                throw new UserValidationException("User doesn't exist.");
            }
            AccountRecoveryResponseBody successResponse = new AccountRecoveryResponseBody(
                    "We found your account. Your username is " + username + ".",
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (UserValidationException exception) {
            AccountRecoveryResponseBody errorResponse = new AccountRecoveryResponseBody(
                    exception.getMessage(),
                    HttpStatus.NOT_FOUND.value()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        catch (Exception exception) {
            AccountRecoveryResponseBody errorResponse = new AccountRecoveryResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/validate-user")
    public ResponseEntity<AccountRecoveryResponseBody> validateUser(@RequestBody AccountRecoveryDTO accountRecoveryDTO) {
        try {
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
        catch (UserValidationException exception) {
            AccountRecoveryResponseBody errorResponse = new AccountRecoveryResponseBody(
                    exception.getMessage(),
                    HttpStatus.NOT_FOUND.value()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        catch (Exception exception) {
            AccountRecoveryResponseBody errorResponse = new AccountRecoveryResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AccountRecoveryResponseBody> resetPassword(@RequestBody AccountRecoveryDTO accountRecoveryDTO) {
        try {
            userCredentialService.updatePassword(accountRecoveryDTO, false);
            AccountRecoveryResponseBody successResponse = new AccountRecoveryResponseBody(
                    "Password has been reset successfully.",
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (Exception exception) {
            AccountRecoveryResponseBody errorResponse = new AccountRecoveryResponseBody(
                    "Error: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
