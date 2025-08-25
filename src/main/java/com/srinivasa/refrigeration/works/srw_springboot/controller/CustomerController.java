package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CustomerCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CustomerDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CustomerDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserProfileResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserProfileUpdateResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserRegisterResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.CustomerService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.DuplicateValueCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/srw/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseBody<CustomerDTO>> register(@RequestBody CustomerCredentialDTO customerCredentialDTO) {
        CustomerDTO customerDTO = customerCredentialDTO.getCustomerDTO();
        try {
            customerDTO = customerService.addCustomer(customerCredentialDTO);
            UserRegisterResponseBody<CustomerDTO> successResponse = new UserRegisterResponseBody<>(
                    "Registered successfully. Please login",
                    HttpStatus.OK.value(),
                    customerDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            UserRegisterResponseBody<CustomerDTO> errorResponse = new UserRegisterResponseBody<>(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("customers", exception),
                    HttpStatus.CONFLICT.value(),
                    customerDTO
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        catch(Exception exception) {
            UserRegisterResponseBody<CustomerDTO> errorResponse = new UserRegisterResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    customerDTO
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseBody<CustomerDTO>> fetchProfile(@RequestParam("customerId") String customerId) {
        CustomerDTO customerDTO = null;
        try {
            customerDTO = (CustomerDTO) customerService.getCustomerByIdentifier(customerId, false);
            UserProfileResponseBody<CustomerDTO> successResponse = new UserProfileResponseBody<>(
                    "User profile fetched successfully.",
                    HttpStatus.OK.value(),
                    customerDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(Exception exception) {
            UserProfileResponseBody<CustomerDTO> errorResponse = new UserProfileResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    customerDTO
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UserProfileUpdateResponseBody<CustomerDTO>> updateProfile(@RequestBody CustomerCredentialDTO customerCredentialDTO) {
        try {
            customerService.updateCustomer(customerCredentialDTO);
            UserProfileUpdateResponseBody<CustomerDTO> successResponse = new UserProfileUpdateResponseBody<>(
                    "Profile updated successfully.",
                    HttpStatus.OK.value(),
                    customerCredentialDTO.getCustomerDTO()
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            UserProfileUpdateResponseBody<CustomerDTO> errorResponse = new UserProfileUpdateResponseBody<>(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("customers", exception),
                    HttpStatus.CONFLICT.value(),
                    customerCredentialDTO.getCustomerDTO()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        catch(Exception exception) {
            UserProfileUpdateResponseBody<CustomerDTO> errorResponse = new UserProfileUpdateResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    customerCredentialDTO.getCustomerDTO()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
