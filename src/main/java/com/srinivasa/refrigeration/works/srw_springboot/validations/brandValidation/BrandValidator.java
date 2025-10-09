package com.srinivasa.refrigeration.works.srw_springboot.validations.brandValidation;

import com.srinivasa.refrigeration.works.srw_springboot.payload.dto.ComplaintDTO;
import com.srinivasa.refrigeration.works.srw_springboot.utils.FieldValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BrandValidator implements ConstraintValidator<ValidBrand, ComplaintDTO> {

    @Override
    public boolean isValid(ComplaintDTO complaint, ConstraintValidatorContext context) {
        if (complaint == null) {
            return true;
        }

        String productType = complaint.getProductType();
        String brand = complaint.getBrand();

        if (productType == null || productType.isBlank() ||
                brand == null || brand.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Brand is required.")
                    .addConstraintViolation();
            return false;
        }

        boolean isValid = switch (productType) {
            case "Air Conditioner" -> FieldValidationConstants.AIR_CONDITIONER_BRANDS.contains(brand);
            case "Refrigerator" -> FieldValidationConstants.REFRIGERATOR_BRANDS.contains(brand);
            default -> false;
        };

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Invalid brand '" + brand + "' for product type '" + productType + "'. " +
                                    "Allowed brands: " + getAllowedBrands(productType)
                    )
                    .addPropertyNode("brand")
                    .addConstraintViolation();
        }

        return isValid;
    }

    private String getAllowedBrands(String productType) {
        return switch (productType) {
            case "Air Conditioner" -> String.join(", ", FieldValidationConstants.AIR_CONDITIONER_BRANDS);
            case "Refrigerator" -> String.join(", ", FieldValidationConstants.REFRIGERATOR_BRANDS);
            default -> "None";
        };
    }
}