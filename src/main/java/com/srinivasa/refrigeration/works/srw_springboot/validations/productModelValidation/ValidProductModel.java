package com.srinivasa.refrigeration.works.srw_springboot.validations.productModelValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductModelValidator.class)
public @interface ValidProductModel {

    String message() default "Invalid product model for the selected brand.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}