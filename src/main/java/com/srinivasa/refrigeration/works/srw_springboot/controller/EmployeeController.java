package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.EmployeeDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.EmployeeCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserProfileResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserRegisterResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.EmployeeService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.DuplicateValueCheck;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/srw/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseBody<EmployeeDTO>> register(@RequestBody EmployeeCredentialDTO employeeCredentialDTO) {
        EmployeeDTO employeeDTO = employeeCredentialDTO.getEmployeeDTO();
        try {
            employeeDTO = employeeService.addEmployee(employeeCredentialDTO);
            UserRegisterResponseBody<EmployeeDTO> successResponse = new UserRegisterResponseBody<>(
                    "Registered successfully. Please login",
                    HttpStatus.OK.value(),
                    employeeDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            UserRegisterResponseBody<EmployeeDTO> errorResponse = new UserRegisterResponseBody<>(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("employees", exception),
                    HttpStatus.CONFLICT.value(),
                    employeeDTO
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        catch (UserValidationException exception) {
            UserRegisterResponseBody<EmployeeDTO> errorResponse = new UserRegisterResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.CONFLICT.value(),
                    employeeDTO
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        catch(Exception exception) {
            UserRegisterResponseBody<EmployeeDTO> errorResponse = new UserRegisterResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    employeeDTO
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseBody<EmployeeDTO>> fetchProfile(@RequestParam("employeeId") String employeeId) {
        EmployeeDTO employeeDTO = null;
        try {
            employeeDTO = (EmployeeDTO) employeeService.getEmployeeByIdentifier(employeeId, false);
            UserProfileResponseBody<EmployeeDTO> successResponse = new UserProfileResponseBody<>(
                    "User profile fetched successfully.",
                    HttpStatus.OK.value(),
                    employeeDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(Exception exception) {
            UserProfileResponseBody<EmployeeDTO> errorResponse = new UserProfileResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    employeeDTO
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}