package com.montage.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IMEIValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIMEI {
    String message() default "Invalid IMEI number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 