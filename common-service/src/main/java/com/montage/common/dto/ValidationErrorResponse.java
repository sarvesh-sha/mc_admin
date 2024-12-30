package com.montage.common.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ValidationErrorResponse {
    private String type;
    private String message;
    private List<String> details;
    private LocalDateTime timestamp;
} 