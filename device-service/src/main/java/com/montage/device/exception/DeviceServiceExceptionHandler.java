package com.montage.device.exception;

import com.montage.common.exception.GlobalExceptionHandler;
import com.montage.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.core.Ordered;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DeviceServiceExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex) {
        log.error("Business exception occurred: {}", ex.getMessage());
        return ResponseEntity
            .status(ex.getStatus())
            .body(ApiResponse.<Object>builder()
                .status(ex.getStatus().value())
                .message(ex.getMessage())
                .success(false)
                .build());
    }
} 