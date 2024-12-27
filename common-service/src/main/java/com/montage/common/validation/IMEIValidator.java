package com.montage.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IMEIValidator implements ConstraintValidator<ValidIMEI, String> {

    @Override
    public boolean isValid(String imei, ConstraintValidatorContext context) {
        if (imei == null) {
            return false;
        }

        // IMEI must be exactly 15 digits
        if (!imei.matches("^[0-9]{15}$")) {
            return false;
        }

        // Luhn algorithm validation
        int sum = 0;
        boolean alternate = false;
        
        for (int i = imei.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(imei.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        
        return (sum % 10 == 0);
    }
} 