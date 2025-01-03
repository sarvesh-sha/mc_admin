package com.montage.device.service;


import java.util.List;

import com.montage.common.service.BaseService;
import com.montage.device.dto.DeviceUploadResult;
import com.montage.device.entity.Device;

public interface DeviceService extends BaseService<Device, Integer> {
    DeviceUploadResult bulkUpload(List<Device> devices);
    // Additional device-specific methods
} 