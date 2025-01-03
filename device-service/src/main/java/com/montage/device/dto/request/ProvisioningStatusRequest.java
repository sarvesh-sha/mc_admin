package com.montage.device.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProvisioningStatusRequest {
    
    @NotBlank(message = "Status name is required")
    private String name;
    
    private String description;
    
    private Boolean isActive = true;
} 