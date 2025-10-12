package com.srinivasa.refrigeration.works.srw_springboot.validations.userIdentifierValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserIdentifierValidator.class)
public @interface ValidUserIdentifier {

    String message() default "Invalid user identifier. Must be a valid phone number or email.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}