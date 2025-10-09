package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.OwnerDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.OwnerCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.UpdateUserStatusDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UsersResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.OwnerService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.DuplicateValueCheck;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.UserStatus;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.ownerGroups.OwnerRegisterGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.ownerGroups.OwnerUpdateGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validations.userIdValidation.ValidUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/srw/owner")
@RequiredArgsConstructor
@Validated
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseBody<OwnerDTO>> register(
            @Validated(OwnerRegisterGroup.class) @RequestBody OwnerCredentialDTO ownerCredentialDTO) {

        try {
            OwnerDTO ownerDTO = ownerService.addOwner(ownerCredentialDTO);

            UserResponseBody<OwnerDTO> successResponse = new UserResponseBody<>(
                    "Registered successfully. Please login",
                    HttpStatus.OK.value(),
                    ownerDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            UserResponseBody<OwnerDTO> errorResponse = new UserResponseBody<>(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("owners", exception),
                    HttpStatus.CONFLICT.value(),
                    new OwnerDTO()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseBody<OwnerDTO>> fetchProfile(
            @RequestParam("ownerId")
            @ValidUserId(
                    requiredMessage = "Owner ID is required.",
                    message = "Invalid owner ID format."
            )
            String ownerId) {

        OwnerDTO ownerDTO = (OwnerDTO) ownerService.getOwnerByIdentifier(ownerId, false);

        UserResponseBody<OwnerDTO> successResponse = new UserResponseBody<>(
                "Owner: " + ownerId +  " profile fetched successfully.",
                HttpStatus.OK.value(),
                ownerDTO
        );
        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UserResponseBody<OwnerDTO>> updateProfile(
            @Validated(OwnerUpdateGroup.class) @RequestBody OwnerCredentialDTO ownerCredentialDTO) {

        try {
            OwnerDTO updateOwnerDTO = ownerService.updateOwner(ownerCredentialDTO);

            UserResponseBody<OwnerDTO> successResponse = new UserResponseBody<>(
                    "Profile updated successfully.",
                    HttpStatus.OK.value(),
                    updateOwnerDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            UserResponseBody<OwnerDTO> errorResponse = new UserResponseBody<>(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("owners", exception),
                    HttpStatus.CONFLICT.value(),
                    ownerCredentialDTO.getOwnerDTO()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<UsersResponseBody<OwnerDTO>> fetchAllOwners() {

        List<OwnerDTO> ownersDTO = ownerService.getOwnerList();

        UsersResponseBody<OwnerDTO> successResponse = new UsersResponseBody<>(
                "List of all owners fetched successfully.",
                HttpStatus.OK.value(),
                ownersDTO
        );
        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/update-status")
    public ResponseEntity<UserResponseBody<UpdateUserStatusDTO>> updateStatus(
            @Valid @RequestBody UpdateUserStatusDTO updateUserStatusDTO) {

        ownerService.updateStatus(updateUserStatusDTO);

        UserResponseBody<UpdateUserStatusDTO> successResponse = new UserResponseBody<>(
                updateUserStatusDTO.getUserStatus().equals(UserStatus.ACTIVE) ? "Activated " : "Deactivated "
                        + updateUserStatusDTO.getUserId() + " successfully.",
                HttpStatus.OK.value(),
                updateUserStatusDTO
        );
        return ResponseEntity.ok(successResponse);
    }
}