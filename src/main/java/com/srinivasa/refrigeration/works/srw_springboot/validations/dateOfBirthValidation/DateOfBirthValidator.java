package com.srinivasa.refrigeration.works.srw_springboot.validations.dateOfBirthValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, LocalDate> {

    @Override
    public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {
        if (dob == null) {
            setMessage(context, "Date of birth is required.");
            return false;
        }

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate minAllowed = today.minusYears(100);
        LocalDate maxAllowed = today.minusYears(18);

        if (dob.isAfter(yesterday)) {
            setMessage(context, "Date must be in the past");
            return false;
        }

        if (dob.isBefore(minAllowed)) {
            setMessage(context, "Age cannot exceed 100 years");
            return false;
        }

        if (dob.isAfter(maxAllowed)) {
            setMessage(context, "Must be at least 18 years old");
            return false;
        }

        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}