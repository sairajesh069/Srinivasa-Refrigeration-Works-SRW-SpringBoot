package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.EmployeeDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.EmployeeCredentialDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.UpdateUserStatusDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UsersResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.UserResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.EmployeeService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.DuplicateValueCheck;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.UserStatus;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.employeeGroups.EmployeeRegisterGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validationGroups.employeeGroups.EmployeeUpdateGroup;
import com.srinivasa.refrigeration.works.srw_springboot.validations.fetchEmployeesContextValidation.ValidFetchEmployeesContext;
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
@RequestMapping("/srw/employee")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseBody<EmployeeDTO>> register(
            @Validated(EmployeeRegisterGroup.class) @RequestBody EmployeeCredentialDTO employeeCredentialDTO) {

        try {
            EmployeeDTO employeeDTO = employeeService.addEmployee(employeeCredentialDTO);

            UserResponseBody<EmployeeDTO> successResponse = new UserResponseBody<>(
                    "Registered successfully. Please login",
                    HttpStatus.OK.value(),
                    employeeDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            UserResponseBody<EmployeeDTO> errorResponse = new UserResponseBody<>(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("employees", exception),
                    HttpStatus.CONFLICT.value(),
                    new EmployeeDTO()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseBody<EmployeeDTO>> fetchProfile(
            @RequestParam("employeeId")
            @ValidUserId(
                    requiredMessage = "Employee ID is required.",
                    message = "Invalid employee ID format."
            )
            String employeeId) {

        EmployeeDTO employeeDTO = (EmployeeDTO) employeeService.getEmployeeByIdentifier(employeeId, false);

        UserResponseBody<EmployeeDTO> successResponse = new UserResponseBody<>(
                "Employee: " + employeeId +  " profile fetched successfully.",
                HttpStatus.OK.value(),
                employeeDTO
        );
        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UserResponseBody<EmployeeDTO>> updateProfile(
            @Validated(EmployeeUpdateGroup.class) @RequestBody EmployeeCredentialDTO employeeCredentialDTO) {

        try {
            EmployeeDTO updatedEmployeeDTO = employeeService.updateEmployee(employeeCredentialDTO);

            UserResponseBody<EmployeeDTO> successResponse = new UserResponseBody<>(
                    "Profile updated successfully.",
                    HttpStatus.OK.value(),
                    updatedEmployeeDTO
            );
            return ResponseEntity.ok(successResponse);
        }
        catch (DataIntegrityViolationException exception) {
            UserResponseBody<EmployeeDTO> errorResponse = new UserResponseBody<>(
                    DuplicateValueCheck.buildDuplicateValueErrorResponse("employees", exception),
                    HttpStatus.CONFLICT.value(),
                    employeeCredentialDTO.getEmployeeDTO()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @GetMapping("/active-list")
    public ResponseEntity<UsersResponseBody<?>> getActiveEmployeeList(@RequestParam("context") @ValidFetchEmployeesContext String context) {

        List<?> activeEmployeesInfo = employeeService.getActiveEmployeeList(context);

        UsersResponseBody<?> successResponse = new UsersResponseBody<>(
                "Active employee profiles fetched successfully.",
                HttpStatus.OK.value(),
                activeEmployeesInfo
        );
        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<UsersResponseBody<EmployeeDTO>> fetchAllEmployees() {

        List<EmployeeDTO> employeesDTO = employeeService.getEmployeeList();

        UsersResponseBody<EmployeeDTO> successResponse = new UsersResponseBody<>(
                "List of all employees fetched successfully.",
                HttpStatus.OK.value(),
                employeesDTO
        );
        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/update-status")
    public ResponseEntity<UserResponseBody<UpdateUserStatusDTO>> updateStatus(
            @Valid @RequestBody UpdateUserStatusDTO updateUserStatusDTO) {

        employeeService.updateStatus(updateUserStatusDTO);

        UserResponseBody<UpdateUserStatusDTO> successResponse = new UserResponseBody<>(
                updateUserStatusDTO.getUserStatus().equals(UserStatus.ACTIVE) ? "Activated " : "Deactivated "
                        + updateUserStatusDTO.getUserId() + " successfully.",
                HttpStatus.OK.value(),
                updateUserStatusDTO
        );
        return ResponseEntity.ok(successResponse);
    }
}