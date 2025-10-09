package com.srinivasa.refrigeration.works.srw_springboot.validations.productTypeValidation;

import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductTypeValidator implements ConstraintValidator<ValidProductType, String> {

    @Override
    public boolean isValid(String productType, ConstraintValidatorContext context) {
        if (productType == null || productType.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Product type is required.")
                    .addConstraintViolation();
            return false;
        }

        return FieldValidationConstants.PRODUCT_TYPES.contains(productType);
    }
}