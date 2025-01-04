package com.montage.device.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SensorRequest {
    @NotBlank(message = "MAC address is required")
    @Size(max = 17, message = "MAC address must not exceed 17 characters")
    private String macAddress;
    
    @NotBlank(message = "Serial number is required")
    @Size(max = 50, message = "Serial number must not exceed 50 characters")
    private String serialNumber;
    
    private String status;
    private Double healthValue;
    private String location;
    private Integer deviceId;
    private Boolean isActive = true;
} 