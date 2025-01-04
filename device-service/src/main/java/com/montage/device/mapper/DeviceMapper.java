package com.montage.device.mapper;

import com.montage.device.dto.request.DeviceRequest;
import com.montage.device.dto.response.DeviceResponse;
import com.montage.device.entity.Device;

public interface DeviceMapper {
    Device toEntity(DeviceRequest request);
    DeviceResponse toResponse(Device device);
    void updateEntityFromRequest(Device device, DeviceRequest request);
} 