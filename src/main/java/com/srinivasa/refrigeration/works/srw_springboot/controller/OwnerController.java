package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.OwnerCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.OwnerDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserRegisterResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.OwnerService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.DuplicateValueCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        catch(Exception exception) {
            UserRegisterResponseBody<OwnerDTO> errorResponse = new UserRegisterResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ownerDTO
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}