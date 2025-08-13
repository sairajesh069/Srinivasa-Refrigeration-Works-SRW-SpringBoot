package com.srinivasa.refrigeration.works.srw_springboot.utils;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Map;

public class DuplicateValueCheck {
    public static String buildDuplicateValueErrorResponse(String userType, DataIntegrityViolationException exception) {
        Throwable cause = exception.getCause();
        if (cause instanceof ConstraintViolationException constraintEx) {
            String constraintName = constraintEx.getConstraintName();
            Map<String, String> errorMap = Map.of(
                    userType + ".UK_user_id", "Duplicate user id",
                    userType + ".UK_user_email", "Duplicate email address",
                    userType + ".UK_user_phone", "Duplicate phone number",
                    "user_credentials.UK_username", "Duplicate username"
            );

            return errorMap.getOrDefault(constraintName, "Duplicate value for a unique field");
        }

        return "Duplicate entry " + exception.getMessage();
    }

}
