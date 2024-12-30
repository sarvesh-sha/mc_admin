package com.montage.common.exception;

import com.montage.common.dto.ValidationErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(ValidationException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        
        ValidationErrorResponse response = ValidationErrorResponse.builder()
            .type("Validation")
            .message("Validation failed")
            .details(ex.getErrors())
            .timestamp(LocalDateTime.now())
            .build();
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ValidationErrorResponse> handleAllUncaughtException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        
        ValidationErrorResponse response = ValidationErrorResponse.builder()
            .type("Error")
            .message("An unexpected error occurred")
            .details(Collections.singletonList(ex.getMessage()))
            .timestamp(LocalDateTime.now())
            .build();
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 