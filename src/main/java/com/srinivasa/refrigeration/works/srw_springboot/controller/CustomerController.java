package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CustomerCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CustomerDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserRegisterResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.CustomerService;
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
}
