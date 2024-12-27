package com.montage.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Aspect
@Component
public class ValidationAspect {

    @Before("@within(org.springframework.validation.annotation.Validated)")
    public void logValidation(JoinPoint joinPoint) {
        log.debug("Validating request for method: {}", 
            joinPoint.getSignature().getName());
    }
} 