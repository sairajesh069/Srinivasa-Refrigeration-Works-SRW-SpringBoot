package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CustomerCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CustomerDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.UpdateUserStatusDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UsersResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.CustomerService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.DuplicateValueCheck;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.UserStatus;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.customerGroups.CustomerRegisterGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.customerGroups.CustomerUpdateGroup;
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
@RequestMapping("/srw/customer")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseBody<CustomerDTO>> register(
            @Validated(CustomerRegisterGroup.class) @RequestBody CustomerCredentialDTO customerCredentialDTO) {

        try {
            CustomerDTO customerDTO = customerService.addCustomer(customerCredentialDTO);

            UserResponseBody<CustomerDTO> successResponse = new UserResponseBody<>(
                    "Registered successfully. Please login",
                    HttpStatus.OK.value(),
                    customerDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            UserResponseBody<CustomerDTO> errorResponse = new UserResponseBody<>(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("customers", exception),
                    HttpStatus.CONFLICT.value(),
                    new CustomerDTO()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseBody<CustomerDTO>> fetchProfile(
            @RequestParam("customerId")
            @ValidUserId(
                    requiredMessage = "Customer ID is required.",
                    message = "Invalid customer ID format."
            )
            String customerId) {

        CustomerDTO customerDTO = (CustomerDTO) customerService.getCustomerByIdentifier(customerId, false);

        UserResponseBody<CustomerDTO> successResponse = new UserResponseBody<>(
                "Customer: " + customerId +  " profile fetched successfully.",
                HttpStatus.OK.value(),
                customerDTO
        );
        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UserResponseBody<CustomerDTO>> updateProfile(
            @Validated(CustomerUpdateGroup.class) @RequestBody CustomerCredentialDTO customerCredentialDTO) {

        try {
            CustomerDTO updatedCustomerDTO = customerService.updateCustomer(customerCredentialDTO);

            UserResponseBody<CustomerDTO> successResponse = new UserResponseBody<>(
                    "Profile updated successfully.",
                    HttpStatus.OK.value(),
                    updatedCustomerDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            UserResponseBody<CustomerDTO> errorResponse = new UserResponseBody<>(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("customers", exception),
                    HttpStatus.CONFLICT.value(),
                    customerCredentialDTO.getCustomerDTO()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<UsersResponseBody<CustomerDTO>> fetchAllCustomers() {

        List<CustomerDTO> customersDTO = customerService.getCustomerList();

        UsersResponseBody<CustomerDTO> successResponse = new UsersResponseBody<>(
                "List of all customers fetched successfully.",
                HttpStatus.OK.value(),
                customersDTO
        );
        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/update-status")
    public ResponseEntity<UserResponseBody<UpdateUserStatusDTO>> updateStatus(
            @Valid @RequestBody UpdateUserStatusDTO updateUserStatusDTO) {

        customerService.updateStatus(updateUserStatusDTO);

        UserResponseBody<UpdateUserStatusDTO> successResponse = new UserResponseBody<>(
                updateUserStatusDTO.getUserStatus().equals(UserStatus.ACTIVE) ? "Activated " : "Deactivated "
                        + updateUserStatusDTO.getUserId() + " successfully.",
                HttpStatus.OK.value(),
                updateUserStatusDTO
        );
        return ResponseEntity.ok(successResponse);
    }
}