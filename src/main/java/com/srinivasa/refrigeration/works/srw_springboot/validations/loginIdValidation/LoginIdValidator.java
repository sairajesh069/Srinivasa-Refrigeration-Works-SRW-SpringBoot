package com.srinivasa.refrigeration.works.srw_springboot.validations.loginIdValidation;

import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LoginIdValidator implements ConstraintValidator<ValidLoginId, String> {

    @Override
    public boolean isValid(String loginId, ConstraintValidatorContext context) {
        if (loginId == null || loginId.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Login ID is required.")
                    .addConstraintViolation();
            return false;
        }

        return loginId.matches(FieldValidationConstants.EMAIL_REGEX) || loginId.matches(FieldValidationConstants.USERNAME_REGEX);
    }
}