package com.srinivasa.refrigeration.works.srw_springboot.validations.userIdValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserIdValidator.class)
public @interface ValidUserId {

    String message() default "Invalid user ID format.";
    String requiredMessage() default "User ID is required.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}