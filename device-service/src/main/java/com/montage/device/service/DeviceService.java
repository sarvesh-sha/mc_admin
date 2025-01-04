package com.montage.device.service;

import java.util.List;

import com.montage.common.dto.BulkUploadResponse;
import com.montage.device.dto.request.DeviceRequest;
import com.montage.device.dto.request.DeviceProvisioningRequest;
import com.montage.device.dto.response.DeviceResponse;
import com.montage.device.entity.Device;

public interface DeviceService extends BaseService<Device, Integer, DeviceRequest, DeviceResponse> {
    BulkUploadResponse<String> bulkUpload(List<DeviceRequest> requests);

	DeviceResponse updateDevice(Integer id, DeviceRequest request);

    DeviceResponse updateProvisioningStatus(Integer deviceId, DeviceProvisioningRequest request);
} 