package com.srinivasa.refrigeration.works.srw_springboot.validations.brandValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BrandValidator.class)
public @interface ValidBrand {

    String message() default "Invalid brand for the selected product type.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}