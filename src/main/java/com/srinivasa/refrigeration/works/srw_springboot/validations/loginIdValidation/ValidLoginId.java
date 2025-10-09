package com.srinivasa.refrigeration.works.srw_springboot.validations.loginIdValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginIdValidator.class)
public @interface ValidLoginId {

    String message() default "Invalid login ID. Must be a valid username or email.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}