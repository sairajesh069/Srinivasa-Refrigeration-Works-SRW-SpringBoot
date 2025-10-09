package com.srinivasa.refrigeration.works.srw_springboot.validations.complaintIdValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ComplaintIdValidator.class)
public @interface ValidComplaintId {

    String message() default "Invalid complaint ID format.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}