package com.montage.device.dto.response;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class ProvisioningStatusResponse {
    private Integer id;
    private String name;
    private String description;
    private Boolean isActive;
    private String createdBy;
    private LocalDateTime createdOn;
    private String updatedBy;
    private LocalDateTime updatedOn;
} 