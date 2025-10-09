package com.srinivasa.refrigeration.works.srw_springboot.validations.genderValidation;

import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderValidator implements ConstraintValidator<ValidGender, String> {

    @Override
    public boolean isValid(String gender, ConstraintValidatorContext context) {
        if (gender == null || gender.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Gender is required.")
                    .addConstraintViolation();
            return false;
        }

        return FieldValidationConstants.GENDERS.contains(gender);
    }
}