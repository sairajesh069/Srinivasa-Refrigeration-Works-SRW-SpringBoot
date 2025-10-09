package com.srinivasa.refrigeration.works.srw_springboot.validations.technicianReassignedValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TechnicianReassignedValidator.class)
public @interface ValidTechnicianReassignment {

    String message() default "Initial assignee ID is required when reassigning technician.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}