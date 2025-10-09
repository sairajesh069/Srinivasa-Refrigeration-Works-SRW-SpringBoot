package com.srinivasa.refrigeration.works.srw_springboot.controller;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.AuthenticatedUserDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.CredentialsDTO;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.LogoutResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.LoginResponseBody;
import com.srinivasa.refrigeration.works.srw_springboot.service.*;
import com.srinivasa.refrigeration.works.srw_springboot.configuration.JwtUtil;
import com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils.UserType;
import com.srinivasa.refrigeration.works.srw_springboot.exceptions.UserValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    private final TokenBlackListService tokenBlackListService;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseBody> login(@Valid @RequestBody CredentialsDTO credentials) {
        try {
            Map<String, String> userAuthDetails = userCredentialService.userValidationAndGetUserId(credentials);

            String userId = userAuthDetails.get("userId");
            String userType = userAuthDetails.get("userType");
            String token = jwtUtil.generateToken(
                    userAuthDetails.get("username"),
                    userId,
                    UserType.valueOf(userType)
            );

            AuthenticatedUserDTO authenticatedUserDTO = switch (userType) {
                case "CUSTOMER" ->
                        (AuthenticatedUserDTO) customerService.getCustomerByIdentifier(userId, true);
                case "OWNER" ->
                        (AuthenticatedUserDTO) ownerService.getOwnerByIdentifier(userId, true);
                case "EMPLOYEE" ->
                        (AuthenticatedUserDTO) employeeService.getEmployeeByIdentifier(userId, true);
                default -> null;
            };

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
                    "",
                    new AuthenticatedUserDTO(),
                    0L
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        catch(Exception exception) {
            LoginResponseBody errorResponse = new LoginResponseBody(
                    "Login failed: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "",
                    new AuthenticatedUserDTO(),
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseBody> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                LogoutResponseBody errorResponse = new LogoutResponseBody(
                        "Invalid or missing Authorization header",
                        HttpStatus.BAD_REQUEST.value()
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            String token = authHeader.substring(7).trim();
            Date expiration = jwtUtil.getExpirationDate(token);

            tokenBlackListService.blacklistToken(token, expiration);
            SecurityContextHolder.clearContext();

            LogoutResponseBody successResponse = new LogoutResponseBody(
                    "Logout success",
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(successResponse);
        }
        catch(Exception exception) {
            LogoutResponseBody errorResponse = new LogoutResponseBody(
                    "Logout failed: " + exception.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}