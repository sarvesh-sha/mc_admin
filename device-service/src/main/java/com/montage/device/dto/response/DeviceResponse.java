package com.montage.device.dto.response;

import java.time.LocalDateTime;

import com.montage.device.entity.Customer;
import com.montage.device.entity.OtaGroupXref;
import com.montage.device.entity.Product;
import com.montage.device.entity.ProvisioningStatus;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeviceResponse {
    private Integer id;
    private String deviceType;
    private String imei;
    private String make;
    private String model;
    private String status;
    private Boolean isActive;
    private LocalDateTime installationDate;
    private Integer configVersion;
    private Integer firmwareVersion;
    private Integer bootloaderVersion;
    private Integer healthValue;
    private Integer hardwareVersion;
    private Integer protocolVersion;
    private String assetName;
    private Integer serialNumber;
    private Customer customer;
    private OtaGroupXref otaGroup;
    private ProvisioningStatus provisioningStatus;
    private Product product;
    private Integer iccid;
    
    // Audit fields
    private String createdBy;
    private LocalDateTime createdOn;
    private String updatedBy;
    private LocalDateTime updatedOn;
} 