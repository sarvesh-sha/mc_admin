package com.montage.device.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OtaGroupRequest {
    @NotBlank(message = "Group name is required")
    private String groupName;
    
    @NotBlank(message = "Group ID is required")
    private String groupId;
    
    @NotNull(message = "Customer ID is required")
    private Integer customerId;
    
    private Boolean isActive = true;
} 