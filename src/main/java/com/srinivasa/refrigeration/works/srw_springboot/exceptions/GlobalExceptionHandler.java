package com.srinivasa.refrigeration.works.srw_springboot.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.GlobalExceptionErrorResponse;
import com.srinivasa.refrigeration.works.srw_springboot.payload.response.MethodArgumentValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MethodArgumentValidationErrorResponse> handleFieldValidationException(MethodArgumentNotValidException exception,
                                                                                       HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        MethodArgumentValidationErrorResponse errorResponse = MethodArgumentValidationErrorResponse.builder()
                .message("Validation failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .path(request.getRequestURI())
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalExceptionErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception,
                                                                                       HttpServletRequest request) {

        GlobalExceptionErrorResponse errorResponse = GlobalExceptionErrorResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<GlobalExceptionErrorResponse> handleUserValidationException(UserValidationException exception,
                                                                                      HttpServletRequest request) {

        GlobalExceptionErrorResponse errorResponse = GlobalExceptionErrorResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource Not Found")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<GlobalExceptionErrorResponse> handleSecurityException(SecurityException exception, HttpServletRequest request) {

        GlobalExceptionErrorResponse errorResponse = GlobalExceptionErrorResponse.builder()
                .message("Error: " + exception.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Access denied")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalExceptionErrorResponse> handleInvalidEnumException(HttpMessageNotReadableException exception,
                                                                                   HttpServletRequest request) {

        String errorMessage = "Invalid value provided. Please check your input.";

        if (exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) exception.getCause();
            if (ife.getTargetType() != null && ife.getTargetType().isEnum()) {
                errorMessage = String.format("Invalid value '%s' for field '%s'. Accepted values are: %s",
                        ife.getValue(),
                        ife.getPath().get(0).getFieldName(),
                        Arrays.toString(ife.getTargetType().getEnumConstants()));
            }
        }

        GlobalExceptionErrorResponse errorResponse = GlobalExceptionErrorResponse.builder()
                .message(errorMessage)
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalExceptionErrorResponse> handleGenericException(Exception exception, HttpServletRequest request) {

        GlobalExceptionErrorResponse errorResponse = GlobalExceptionErrorResponse.builder()
                .message("Error: " + exception.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}