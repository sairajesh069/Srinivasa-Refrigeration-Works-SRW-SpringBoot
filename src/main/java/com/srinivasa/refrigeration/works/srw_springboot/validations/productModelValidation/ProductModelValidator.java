package com.srinivasa.refrigeration.works.srw_springboot.validations.productModelValidation;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintDTO;
import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductModelValidator implements ConstraintValidator<ValidProductModel, ComplaintDTO> {

    @Override
    public boolean isValid(ComplaintDTO complaint, ConstraintValidatorContext context) {
        if (complaint == null) {
            return true;
        }

        String productType = complaint.getProductType();
        String productModel = complaint.getProductModel();

        if (productType == null || productType.isBlank() ||
                productModel == null || productModel.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Product model is required.")
                    .addConstraintViolation();
            return false;
        }

        boolean isValid = switch (productType) {
            case "Air Conditioner" -> FieldValidationConstants.AIR_CONDITIONER_MODELS.contains(productModel);
            case "Refrigerator" -> FieldValidationConstants.REFRIGERATOR_MODELS.contains(productModel);
            default -> false;
        };

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Invalid product model '" + productModel + "' for product type '" + productType + "'. " +
                                    "Allowed models: " + getAllowedModels(productType)
                    )
                    .addPropertyNode("productModel")
                    .addConstraintViolation();
        }

        return isValid;
    }

    private String getAllowedModels(String productType) {
        return switch (productType) {
            case "Air Conditioner" -> String.join(", ", FieldValidationConstants.AIR_CONDITIONER_MODELS);
            case "Refrigerator" -> String.join(", ", FieldValidationConstants.REFRIGERATOR_MODELS);
            default -> "None";
        };
    }
}