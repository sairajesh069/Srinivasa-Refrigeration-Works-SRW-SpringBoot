package com.srinivasa.refrigeration.works.srw_springboot.validations.technicianReassignedValidation;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintDTO;
import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TechnicianReassignedValidator implements ConstraintValidator<ValidTechnicianReassignment, ComplaintDTO> {

    @Override
    public boolean isValid(ComplaintDTO complaint, ConstraintValidatorContext context) {
        if (complaint == null) {
            return true;
        }

        if (complaint.getTechnicianDetails() == null ||
                complaint.getTechnicianDetails().getEmployeeId() == null ||
                complaint.getTechnicianDetails().getEmployeeId().isEmpty()) {
            return true;
        }

        String currentTechnicianId = complaint.getTechnicianDetails().getEmployeeId();
        String initialAssigneeId = complaint.getInitialAssigneeId();


        if (initialAssigneeId != null && !initialAssigneeId.isEmpty()) {
            if (!currentTechnicianId.equals(initialAssigneeId)) {
                return true;
            }
            if(!currentTechnicianId.matches(FieldValidationConstants.USER_ID_REGEX)
                    || !initialAssigneeId.matches(FieldValidationConstants.USER_ID_REGEX)) {
                return true;
            }
        }

        return true;
    }
}