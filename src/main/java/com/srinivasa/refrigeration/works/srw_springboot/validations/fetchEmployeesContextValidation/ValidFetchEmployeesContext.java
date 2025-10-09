package com.srinivasa.refrigeration.works.srw_springboot.validations.fetchEmployeesContextValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FetchEmployeesContextValidator.class)
public @interface ValidFetchEmployeesContext {

    String message() default "Invalid employees fetch context.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}