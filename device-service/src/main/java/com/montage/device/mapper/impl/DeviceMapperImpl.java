package com.montage.device.mapper.impl;

import org.springframework.stereotype.Component;

import com.montage.device.dto.request.DeviceRequest;
import com.montage.device.dto.response.DeviceResponse;
import com.montage.device.entity.Device;
import com.montage.device.mapper.DeviceMapper;

@Component
public class DeviceMapperImpl implements DeviceMapper {
    
    @Override
    public Device toEntity(DeviceRequest request) {
        if (request == null) {
            return null;
        }
        
        Device device = new Device();
        device.setImei(request.getImei());
        device.setSerialNumber(request.getSerialNumber());
        device.setModel(request.getModel());
       
        // Set other fields as needed
        return device;
    }
    
    @Override
    public DeviceResponse toResponse(Device device) {
        if (device == null) {
            return null;
        }
        
        DeviceResponse response = new DeviceResponse();
        response.setId(device.getId());
        response.setImei(device.getImei());
        response.setSerialNumber(device.getSerialNumber());
        response.setModel(device.getModel());
        response.setCreatedOn(device.getCreatedOn());
        response.setUpdatedOn(device.getUpdatedOn()); 
        // Set other fields as needed
        return response;
    }
    
    @Override
    public void updateEntityFromRequest(Device device, DeviceRequest request) {
        if (request == null || device == null) {
            return;
        }
        
        device.setImei(request.getImei());
        device.setSerialNumber(request.getSerialNumber());
        device.setModel(request.getModel());
     
        // Update other fields as needed
    }
} 