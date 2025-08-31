package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.OwnerDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.OwnerCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserProfileResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserProfileUpdateResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserRegisterResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.OwnerService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.DuplicateValueCheck;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/srw/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseBody<OwnerDTO>> register(@RequestBody OwnerCredentialDTO ownerCredentialDTO) {
        OwnerDTO ownerDTO = ownerCredentialDTO.getOwnerDTO();
        try {
            ownerDTO = ownerService.addOwner(ownerCredentialDTO);
            UserRegisterResponseBody<OwnerDTO> successResponse = new UserRegisterResponseBody<>(
                    "Registered successfully. Please login",
                    HttpStatus.OK.value(),
                    ownerDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            UserRegisterResponseBody<OwnerDTO> errorResponse = new UserRegisterResponseBody<>(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("owners", exception),
                    HttpStatus.CONFLICT.value(),
                    ownerDTO
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        catch (UserValidationException exception) {
            UserRegisterResponseBody<OwnerDTO> errorResponse = new UserRegisterResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.CONFLICT.value(),
                    ownerDTO
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        catch(Exception exception) {
            UserRegisterResponseBody<OwnerDTO> errorResponse = new UserRegisterResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ownerDTO
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseBody<OwnerDTO>> fetchProfile(@RequestParam("ownerId") String ownerId) {
        OwnerDTO ownerDTO = null;
        try {
            ownerDTO = (OwnerDTO) ownerService.getOwnerByIdentifier(ownerId, false);
            UserProfileResponseBody<OwnerDTO> successResponse = new UserProfileResponseBody<>(
                    "Owner: " + ownerId +  " profile fetched successfully.",
                    HttpStatus.OK.value(),
                    ownerDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(Exception exception) {
            UserProfileResponseBody<OwnerDTO> errorResponse = new UserProfileResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ownerDTO
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UserProfileUpdateResponseBody<OwnerDTO>> updateProfile(@RequestBody OwnerCredentialDTO ownerCredentialDTO) {
        try {
            OwnerDTO updateOwnerDTO = ownerService.updateOwner(ownerCredentialDTO);
            UserProfileUpdateResponseBody<OwnerDTO> successResponse = new UserProfileUpdateResponseBody<>(
                    "Profile updated successfully.",
                    HttpStatus.OK.value(),
                    updateOwnerDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            UserProfileUpdateResponseBody<OwnerDTO> errorResponse = new UserProfileUpdateResponseBody<>(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("owners", exception),
                    HttpStatus.CONFLICT.value(),
                    ownerCredentialDTO.getOwnerDTO()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        catch (UserValidationException exception) {
            UserProfileUpdateResponseBody<OwnerDTO> errorResponse = new UserProfileUpdateResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.CONFLICT.value(),
                    ownerCredentialDTO.getOwnerDTO()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        catch(Exception exception) {
            UserProfileUpdateResponseBody<OwnerDTO> errorResponse = new UserProfileUpdateResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ownerCredentialDTO.getOwnerDTO()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}