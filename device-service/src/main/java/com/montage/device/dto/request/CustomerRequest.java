package com.montage.device.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRequest {
    @NotBlank(message = "Customer name is required")
    @Size(max = 255, message = "Customer name must not exceed 255 characters")
    private String customerName;
    
    @Size(max = 100, message = "Short name must not exceed 100 characters")
    private String shortName;
    
    @Size(max = 50, message = "Account number must not exceed 50 characters")
    private String accountNumber;
    
    private Boolean isActive = true;
} 