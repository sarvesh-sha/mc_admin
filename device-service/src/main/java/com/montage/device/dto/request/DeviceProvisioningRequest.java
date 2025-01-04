package com.montage.device.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceProvisioningRequest {
    @NotNull(message = "Provisioning status ID is required")
    private Integer provisioningStatusId;
} 