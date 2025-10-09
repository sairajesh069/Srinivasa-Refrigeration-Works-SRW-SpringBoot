package com.srinivasa.refrigeration.works.srw_springboot.validations.complaintIdValidation;

import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ComplaintIdValidator implements ConstraintValidator<ValidComplaintId, String> {

    @Override
    public boolean isValid(String complaintId, ConstraintValidatorContext context) {
        if (complaintId == null || complaintId.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Complaint ID is required.")
                    .addConstraintViolation();
            return false;
        }
        return complaintId.matches(FieldValidationConstants.COMPLAINT_ID_REGEX);
    }
}