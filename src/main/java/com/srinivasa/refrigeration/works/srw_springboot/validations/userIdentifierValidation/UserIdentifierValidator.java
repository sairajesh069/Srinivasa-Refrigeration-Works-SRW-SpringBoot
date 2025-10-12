package com.srinivasa.refrigeration.works.srw_springboot.validations.userIdentifierValidation;

import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserIdentifierValidator implements ConstraintValidator<ValidUserIdentifier, String> {

    @Override
    public boolean isValid(String userIdentifier, ConstraintValidatorContext context) {
        if(userIdentifier == null || userIdentifier.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("User identifier is required.")
                    .addConstraintViolation();
            return false;
        }

        return userIdentifier.matches(FieldValidationConstants.PHONE_NUMBER_REGEX)
                || userIdentifier.matches(FieldValidationConstants.EMAIL_REGEX);
    }
}