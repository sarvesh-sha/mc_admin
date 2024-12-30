package com.montage.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SerialNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SerialNumber {
    String message() default "Invalid serial number format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 