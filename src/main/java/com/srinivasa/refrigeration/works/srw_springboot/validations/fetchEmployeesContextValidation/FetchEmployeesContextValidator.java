package com.srinivasa.refrigeration.works.srw_springboot.validations.fetchEmployeesContextValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FetchEmployeesContextValidator implements ConstraintValidator<ValidFetchEmployeesContext, String> {

    @Override
    public boolean isValid(String fetchContext, ConstraintValidatorContext context) {
        if (fetchContext == null || fetchContext.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Employees fetch context ID is required.")
                    .addConstraintViolation();
            return false;
        }
        return fetchContext.equals("update-complaint") || fetchContext.equals("all-employees");
    }
}