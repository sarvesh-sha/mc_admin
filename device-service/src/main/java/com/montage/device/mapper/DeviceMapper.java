package com.montage.device.mapper;


import org.springframework.stereotype.Component;

import com.montage.device.dto.request.DeviceRequest;
import com.montage.device.dto.response.DeviceResponse;
import com.montage.device.entity.Customer;
import com.montage.device.entity.Device;
import com.montage.device.entity.OtaGroupXref;
import com.montage.device.entity.Product;
import com.montage.device.entity.ProvisioningStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeviceMapper {
    
    public Device toEntity(DeviceRequest request) {
        if (request == null) return null;
        
        Device device = new Device();
        device.setDeviceType(request.getDeviceType());
        device.setImei(request.getImei());
        device.setMake(request.getMake());
        device.setModel(request.getModel());
        device.setStatus(request.getStatus());
        device.setIsActive(request.getIsActive());
        device.setInstallationDate(request.getInstallationDate());
        device.setConfigVersion(request.getConfigVersion());
        device.setFirmwareVersion(request.getFirmwareVersion());
        device.setBootloaderVersion(request.getBootloaderVersion());
        device.setHealthValue(request.getHealthValue());
        device.setHardwareVersion(request.getHardwareVersion());
        device.setProtocolVersion(request.getProtocolVersion());
        device.setAssetName(request.getAssetName());
        device.setSerialNumber(request.getSerialNumber());
        device.setICCID(request.getIccid());
        
        // Map reference entities
        if (request.getCustomerId() != null) {
            Customer customer = new Customer();
            customer.setId(request.getCustomerId());
            device.setCustomer(customer);
        }
        
        if (request.getOtaGroupId() != null) {
            OtaGroupXref otaGroup = new OtaGroupXref();
            otaGroup.setId(request.getOtaGroupId());
            device.setOtaGroup(otaGroup);
        }
        
        if (request.getProvisioningStatusId() != null) {
            ProvisioningStatus provisioningStatus = new ProvisioningStatus();
            provisioningStatus.setId(request.getProvisioningStatusId());
            device.setProvisioningStatus(provisioningStatus);
        }
        
        if (request.getProductId() != null) {
            Product product = new Product();
            product.setId(request.getProductId());
            device.setProduct(product);
        }
        
        return device;
    }
    
    public DeviceResponse toResponse(Device device) {
        if (device == null) return null;
        
        return DeviceResponse.builder()
            .id(device.getId())
            .deviceType(device.getDeviceType())
            .imei(device.getImei())
            .make(device.getMake())
            .model(device.getModel())
            .status(device.getStatus())
            .isActive(device.getIsActive())
            .installationDate(device.getInstallationDate())
            .configVersion(device.getConfigVersion())
            .firmwareVersion(device.getFirmwareVersion())
            .bootloaderVersion(device.getBootloaderVersion())
            .healthValue(device.getHealthValue())
            .hardwareVersion(device.getHardwareVersion())
            .protocolVersion(device.getProtocolVersion())
            .assetName(device.getAssetName())
            .serialNumber(device.getSerialNumber())
            .iccid(device.getICCID())
            .customer(device.getCustomer()) 
            .otaGroup(device.getOtaGroup())
            .provisioningStatus(device.getProvisioningStatus())
            .product(device.getProduct() )
            // Map reference entities with their IDs and names
            // Audit fields
            .createdBy(device.getCreatedBy())
            .createdAt(device.getCreatedOn())
            .updatedBy(device.getUpdatedBy())
            .updatedAt(device.getUpdatedOn())
            .build();
    }
} 