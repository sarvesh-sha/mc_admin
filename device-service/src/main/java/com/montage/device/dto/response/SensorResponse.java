package com.montage.device.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SensorResponse {
    private Integer id;
    private String macAddress;
    private String serialNumber;
    private String status;
    private Double healthValue;
    private String location;
    private Boolean isActive;
    private Integer deviceId;
    private String deviceName;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
} 