package com.montage.device.dto;

import com.montage.device.entity.Device;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DeviceUploadResult {
    private List<Device> successfulDevices;
    private Map<String, String> failedDevices;
} 