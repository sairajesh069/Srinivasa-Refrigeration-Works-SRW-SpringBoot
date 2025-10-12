package com.srinivasa.refrigeration.works.srw_springboot.utils;

public class FieldConsistencyValidator {

    public static void validate(String expectedValue, String providedValue, String fieldLabel) {

        if(!expectedValue.equals(providedValue)) {
            throw new IllegalArgumentException(fieldLabel + " mismatch within the request.");
        }
    }
}