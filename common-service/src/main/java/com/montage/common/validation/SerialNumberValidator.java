package com.montage.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SerialNumberValidator implements ConstraintValidator<SerialNumber, String> {
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches("^[A-Z]{2}\\d{6}$"); // Example format: AB123456
    }
} 