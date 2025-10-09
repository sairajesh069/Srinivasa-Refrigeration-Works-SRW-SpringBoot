package com.srinivasa.refrigeration.works.srw_springboot.utils;

import java.util.Arrays;
import java.util.List;

public class FieldValidationConstants {

    public static final String USER_ID_REGEX = "^SRW\\d{19}(OWNR|EMPL|CUST)$";
    public static final String COMPLAINT_ID_REGEX = "^SRW\\d{19}COMP$";

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String PHONE_NUMBER_REGEX = "^(\\+91[0-9]{10}|[0-9]{10})$";
    public static final String NATIONAL_ID_NUMBER_REGEX = "^([A-Z]{5}[0-9]{4}[A-Z]{1}|\\d{12})$";

    public static final String USERNAME_REGEX = "^[a-zA-Z0-9\\-@#!?*$.%&]{6,}$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%.*?&])[A-Za-z\\d@$!%.*?&]{8,}$";

    public static final List<String> GENDERS = Arrays.asList("Male", "Female", "Transgender", "Other", "prefer-not-to-say");

    public static final List<String> PRODUCT_TYPES = Arrays.asList("Air Conditioner", "Refrigerator", "Other");

    public static final List<String> AIR_CONDITIONER_BRANDS = Arrays.asList("Samsung", "LG", "Panasonic", "Daikin", "Haier", "Lloyd", "Hitachi", "BlueStar", "Carrier", "O'General", "Voltas", "Others");
    public static final List<String> AIR_CONDITIONER_MODELS = Arrays.asList("Inverter Technology Split AC", "Non-Inverter Split AC", "Window Air Conditioner");

    public static final List<String> REFRIGERATOR_BRANDS = Arrays.asList("Samsung", "LG", "Whirlpool", "Haier", "Godrej", "Bosch", "Hitachi", "Kelvinator", "Voltas", "Panasonic", "Others");
    public static final List<String> REFRIGERATOR_MODELS = Arrays.asList("Single-door Non-Inverter", "Single-door Inverter", "Double-door Non-Inverter", "Double-door Inverter");
}