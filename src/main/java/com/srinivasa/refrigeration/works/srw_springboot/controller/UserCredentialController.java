package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ChangePasswordDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserAuthDataUpdateResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.UserCredentialService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/srw/user")
@RequiredArgsConstructor
public class UserCredentialController {

    private final UserCredentialService userCredentialService;

    @GetMapping("/fetch-username")
    public ResponseEntity<UserAuthDataUpdateResponseBody> fetchUsername(@RequestParam("userId") String userId) {
        try {
            String username = userCredentialService.fetchUsername(userId);
            if(username == null) {
                throw new UserValidationException("User doesn't exist.");
            }
            UserAuthDataUpdateResponseBody successResponse = new UserAuthDataUpdateResponseBody(
                    "We found your account. Your username is " + username + ".",
                    username,
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (UserValidationException exception) {
            UserAuthDataUpdateResponseBody errorResponse = new UserAuthDataUpdateResponseBody(
                    exception.getMessage(),
                    null,
                    HttpStatus.NOT_FOUND.value()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        catch (Exception exception) {
            UserAuthDataUpdateResponseBody errorResponse = new UserAuthDataUpdateResponseBody(
                    "Error: " + exception.getMessage(),
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<UserAuthDataUpdateResponseBody> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            userCredentialService.updatePassword(changePasswordDTO, true);
            UserAuthDataUpdateResponseBody successResponse = new UserAuthDataUpdateResponseBody(
                    "Password has been reset successfully.",
                    changePasswordDTO.getUsername(),
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (UserValidationException exception) {
            UserAuthDataUpdateResponseBody errorResponse = new UserAuthDataUpdateResponseBody(
                    exception.getMessage(),
                    changePasswordDTO.getUsername(),
                    HttpStatus.UNAUTHORIZED.value()
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        catch (Exception exception) {
            UserAuthDataUpdateResponseBody errorResponse = new UserAuthDataUpdateResponseBody(
                    "Error: " + exception.getMessage(),
                    changePasswordDTO.getUsername(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}