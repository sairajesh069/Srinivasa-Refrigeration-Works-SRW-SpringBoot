package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.AuthenticatedUserDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CredentialsDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.LogoutResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.LoginResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.CustomerService;
import com.srinivasa.refrigeration.works.srw_springboot.service.OwnerService;
import com.srinivasa.refrigeration.works.srw_springboot.service.EmployeeService;
import com.srinivasa.refrigeration.works.srw_springboot.service.UserCredentialService;
import com.srinivasa.refrigeration.works.srw_springboot.utils.JwtUtil;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserType;
import com.srinivasa.refrigeration.works.srw_springboot.utils.UserValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/srw")
@RequiredArgsConstructor
public class LoginController {

    private final JwtUtil jwtUtil;
    private final UserCredentialService userCredentialService;
    private final CustomerService customerService;
    private final OwnerService ownerService;
    private final EmployeeService employeeService;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseBody> login(@RequestBody CredentialsDTO credentials) {
        try {
            Map<String, String> userAuthDetails = userCredentialService.userValidationAndGetUserId(credentials);
            String userId = userAuthDetails.get("userId");
            String userType = userAuthDetails.get("userType");
            String token = jwtUtil.generateToken(
                    userAuthDetails.get("username"),
                    userId,
                    UserType.valueOf(userType)
            );
            AuthenticatedUserDTO authenticatedUserDTO = null;
            if(userType.equals("CUSTOMER")) {
                authenticatedUserDTO = (AuthenticatedUserDTO) customerService.getCustomerByIdentifier(userId, true);
            }
            else if(userType.equals("OWNER")) {
                authenticatedUserDTO = (AuthenticatedUserDTO) ownerService.getOwnerByIdentifier(userId, true);
            }
            else if(userType.equals("EMPLOYEE")) {
                authenticatedUserDTO = (AuthenticatedUserDTO) employeeService.getEmployeeByIdentifier(userId, true);
            }
            LoginResponseBody successResponse = new LoginResponseBody(
                    "Login success",
                    HttpStatus.OK.value(),
                    token,
                    authenticatedUserDTO,
                    jwtExpiration
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(UserValidationException exception) {
            LoginResponseBody errorResponse = new LoginResponseBody(
                    exception.getMessage(),
                    HttpStatus.UNAUTHORIZED.value(),
                    null,
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        catch(Exception exception) {
            LoginResponseBody errorResponse = new LoginResponseBody(
                    "Login failed " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null,
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseBody> logout() {
        LogoutResponseBody successResponse = new LogoutResponseBody(
                "Logout success",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(successResponse);
    }
}
