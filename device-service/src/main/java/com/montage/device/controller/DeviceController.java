package com.montage.device.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.BulkUploadResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.dto.DeviceUploadResult;
import com.montage.device.dto.request.DeviceRequest;
import com.montage.device.dto.response.DeviceResponse;
import com.montage.device.entity.Device;
import com.montage.device.mapper.DeviceMapper;
import com.montage.device.service.impl.DeviceServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@Tag(name = "Device Management", description = "APIs for managing devices")
public class DeviceController {

    private final DeviceServiceImpl deviceService;
    private final DeviceMapper deviceMapper;
    private final jakarta.validation.Validator validator;

    @Operation(summary = "Search devices")
    @PostMapping("v1/device/search")
    public ResponseEntity<ApiResponse<Page<DeviceResponse>>> searchDevices(@RequestBody SearchRequest searchRequest) {
        log.info("Searching devices with criteria: {}", searchRequest);
        Page<Device> devices = deviceService.search(searchRequest);
        Page<DeviceResponse> response = devices.map(deviceMapper::toResponse);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get device by ID")
    @GetMapping("v1/device/{id}")
    public ResponseEntity<ApiResponse<DeviceResponse>> getDevice(@PathVariable Integer id) {
        log.info("Finding device by id: {}", id);
        Device device = deviceService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(deviceMapper.toResponse(device)));
    }

    @Operation(summary = "Create new device")
    @PostMapping("v1/device")
    public ResponseEntity<ApiResponse<DeviceResponse>> createDevice(@Valid @RequestBody DeviceRequest request) {
        log.info("Creating new device: {}", request);
        Device device = deviceMapper.toEntity(request);
        Device savedDevice = deviceService.create(device);
        return ResponseEntity.ok(ApiResponse.success(deviceMapper.toResponse(savedDevice)));
    }

    @Operation(summary = "Update existing device")
    @PutMapping("v1/device/{id}")
    public ResponseEntity<ApiResponse<DeviceResponse>> updateDevice(
            @PathVariable Integer id, 
            @Valid @RequestBody DeviceRequest request) {
        log.info("Updating device with id {}: {}", id, request);
        Device device = deviceMapper.toEntity(request);
        Device updatedDevice = deviceService.update(id, device);
        return ResponseEntity.ok(ApiResponse.success(deviceMapper.toResponse(updatedDevice)));
    }

    @Operation(summary = "Delete device")
    @DeleteMapping("v1/device/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Integer id) {
        log.info("Deleting device with id: {}", id);
        deviceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Bulk upload devices")
    @PostMapping("v1/device/bulk-upload")
    public ResponseEntity<ApiResponse<BulkUploadResponse<String>>> bulkUploadDevices(
            @RequestBody List<DeviceRequest> requests) {
        log.info("Processing bulk upload for {} devices", requests.size());
        
        Map<String, String> failedRecords = new HashMap<>();
        List<Device> validDevices = new ArrayList<>();
        
        // Validate each request and collect errors
        for (DeviceRequest request : requests) {
            try {
                validDevices.add(deviceMapper.toEntity(request));
            } catch (Exception e) {
                failedRecords.put(request.getImei() != null ? request.getImei() : "Unknown", e.getMessage());
            }
        }
        
        // Process valid devices
        DeviceUploadResult result = deviceService.bulkUpload(validDevices);
        
        BulkUploadResponse<String> response = BulkUploadResponse.<String>builder()
            .successCount(result.getSuccessfulDevices().size())
            .failureCount(result.getFailedDevices().size())
            .successfulRecords(result.getSuccessfulDevices().stream()
                .map(Device::getImei)
                .toList())
            .failedRecords(result.getFailedDevices())
            .build();
            
        return ResponseEntity.ok(ApiResponse.success(response));
    }
} 