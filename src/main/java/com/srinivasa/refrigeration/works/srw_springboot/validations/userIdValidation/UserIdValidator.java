package com.srinivasa.refrigeration.works.srw_springboot.validations.userIdValidation;

import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserIdValidator implements ConstraintValidator<ValidUserId, String> {

    private String requiredMessage;
    private String invalidMessage;

    @Override
    public void initialize(ValidUserId annotation) {
        this.requiredMessage = annotation.requiredMessage();
        this.invalidMessage = annotation.message();
    }

    @Override
    public boolean isValid(String userId, ConstraintValidatorContext context) {
        if (userId == null || userId.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(requiredMessage)
                    .addConstraintViolation();
            return false;
        }

        if (!userId.matches(FieldValidationConstants.USER_ID_REGEX)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(invalidMessage)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}