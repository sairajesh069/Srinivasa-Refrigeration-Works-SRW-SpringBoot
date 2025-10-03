package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.EmployeeDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.EmployeeCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.UpdateUserStatusDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.FetchUsersResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserProfileResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserProfileUpdateResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserRegisterResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.EmployeeService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.DuplicateValueCheck;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserStatus;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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
        catch (IllegalArgumentException exception) {
            UserRegisterResponseBody<EmployeeDTO> errorResponse = new UserRegisterResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.NOT_ACCEPTABLE.value(),
                    employeeDTO
            );
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
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
        EmployeeDTO employeeDTO = new EmployeeDTO();
        try {
            employeeDTO = (EmployeeDTO) employeeService.getEmployeeByIdentifier(employeeId, false);
            UserProfileResponseBody<EmployeeDTO> successResponse = new UserProfileResponseBody<>(
                    "Employee: " + employeeId +  " profile fetched successfully.",
                    HttpStatus.OK.value(),
                    employeeDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(SecurityException exception) {
            UserProfileResponseBody<EmployeeDTO> errorResponse = new UserProfileResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.FORBIDDEN.value(),
                    employeeDTO
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
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

    @PutMapping("/update-profile")
    public ResponseEntity<UserProfileUpdateResponseBody<EmployeeDTO>> updateProfile(@RequestBody EmployeeCredentialDTO employeeCredentialDTO) {
        try {
            EmployeeDTO updatedEmployeeDTO = employeeService.updateEmployee(employeeCredentialDTO);
            UserProfileUpdateResponseBody<EmployeeDTO> successResponse = new UserProfileUpdateResponseBody<>(
                    "Profile updated successfully.",
                    HttpStatus.OK.value(),
                    updatedEmployeeDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            UserProfileUpdateResponseBody<EmployeeDTO> errorResponse = new UserProfileUpdateResponseBody<>(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("employees", exception),
                    HttpStatus.CONFLICT.value(),
                    employeeCredentialDTO.getEmployeeDTO()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        catch (UserValidationException exception) {
            UserProfileUpdateResponseBody<EmployeeDTO> errorResponse = new UserProfileUpdateResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.CONFLICT.value(),
                    employeeCredentialDTO.getEmployeeDTO()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        catch(Exception exception) {
            UserProfileUpdateResponseBody<EmployeeDTO> errorResponse = new UserProfileUpdateResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    employeeCredentialDTO.getEmployeeDTO()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/active-list")
    public ResponseEntity<FetchUsersResponseBody<?>> getActiveEmployeeList(@RequestParam("context") String context) {
        try {
            List<?> activeEmployeesInfo = employeeService.getActiveEmployeeList(context);
            FetchUsersResponseBody<?> successResponse = new FetchUsersResponseBody<>(
                    "Active employee profiles fetched successfully.",
                    HttpStatus.OK.value(),
                    activeEmployeesInfo
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(Exception exception) {
            FetchUsersResponseBody<EmployeeDTO> errorResponse = new FetchUsersResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Collections.emptyList()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<FetchUsersResponseBody<EmployeeDTO>> fetchAllEmployees() {
        try {
            List<EmployeeDTO> employeesDTO = employeeService.getEmployeeList();
            FetchUsersResponseBody<EmployeeDTO> successResponse = new FetchUsersResponseBody<>(
                    "List of all employees fetched successfully.",
                    HttpStatus.OK.value(),
                    employeesDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(Exception exception) {
            FetchUsersResponseBody<EmployeeDTO> errorResponse = new FetchUsersResponseBody<>(
                    exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Collections.emptyList()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/update-status")
    public ResponseEntity<UserProfileResponseBody<UpdateUserStatusDTO>> updateStatus(@RequestBody UpdateUserStatusDTO updateUserStatusDTO) {
        try {
            employeeService.updateStatus(updateUserStatusDTO);
            UserProfileResponseBody<UpdateUserStatusDTO> successResponse = new UserProfileResponseBody<>(
                    updateUserStatusDTO.getUserStatus().equals(UserStatus.ACTIVE) ? "Activated " : "Deactivated "
                            + updateUserStatusDTO.getUserId() + " successfully.",
                    HttpStatus.OK.value(),
                    updateUserStatusDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(Exception exception) {
            UserProfileResponseBody<UpdateUserStatusDTO> errorResponse = new UserProfileResponseBody<>(
                    "Error: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    updateUserStatusDTO
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}