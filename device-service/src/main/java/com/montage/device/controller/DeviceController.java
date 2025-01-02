package com.montage.device.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.BulkUploadResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.dto.DeviceUploadResult;
import com.montage.device.dto.request.DeviceRequest;
import com.montage.device.dto.response.DeviceResponse;
import com.montage.device.entity.Device;
import com.montage.device.mapper.DeviceMapper;
import com.montage.device.service.impl.DeviceServiceImpl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceServiceImpl deviceService;
    private final DeviceMapper deviceMapper;
    private final jakarta.validation.Validator validator;

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<DeviceResponse>>> search(@RequestBody SearchRequest searchRequest) {
        log.info("Searching devices with criteria: {}", searchRequest);
        Page<Device> devices = deviceService.search(searchRequest);
        Page<DeviceResponse> response = devices.map(deviceMapper::toResponse);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeviceResponse>> findById(@PathVariable Integer id) {
        log.info("Finding device by id: {}", id);
        Device device = deviceService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(deviceMapper.toResponse(device)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeviceResponse>> create(@Valid @RequestBody DeviceRequest request) {
        log.info("Creating new device: {}", request);
        Device device = deviceMapper.toEntity(request);
        Device savedDevice = deviceService.create(device);
        return ResponseEntity.ok(ApiResponse.success(deviceMapper.toResponse(savedDevice)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DeviceResponse>> update(
            @PathVariable Integer id, 
            @Valid @RequestBody DeviceRequest request) {
        log.info("Updating device with id {}: {}", id, request);
        Device device = deviceMapper.toEntity(request);
        Device updatedDevice = deviceService.update(id, device);
        return ResponseEntity.ok(ApiResponse.success(deviceMapper.toResponse(updatedDevice)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Deleting device with id: {}", id);
        deviceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk-upload")
    public ResponseEntity<ApiResponse<BulkUploadResponse<String>>> bulkUpload(
            @RequestBody List<DeviceRequest> requests) {
        log.info("Processing bulk upload for {} devices", requests.size());
        
        Map<String, String> failedRecords = new HashMap<>();
        List<Device> validDevices = new ArrayList<>();
        
       //  Validate each request and collect errors
        for (DeviceRequest request : requests) {
            try {
             //   Set<ConstraintViolation<DeviceRequest>> violations = validator.validate(request);
//                if (!violations.isEmpty()) {
//                    String errors = violations.stream()
//                        .map(ConstraintViolation::getMessage)
//                        .collect(Collectors.joining("; "));
//                    failedRecords.put(request.getImei() != null ? request.getImei() : "Unknown", errors);
//                    continue;
//                }
                validDevices.add(deviceMapper.toEntity(request));
            } catch (Exception e) {
                failedRecords.put(request.getImei() != null ? request.getImei() : "Unknown", e.getMessage());
            }
        }
        
        // Process valid devices
        DeviceUploadResult result = deviceService.bulkUpload(validDevices);
        
        // Add service-level validation failures to the failed records
//        result.getFailedDevices().forEach((device, errorMessage) -> 
//            failedRecords.put(device.getImei() != null ? device.getImei() : "Unknown", errorMessage)
//        );
        
        BulkUploadResponse<String> response = BulkUploadResponse.<String>builder()
            .successCount(result.getSuccessfulDevices().size())
            .failureCount(result.getFailedDevices().size())
            .successfulRecords(result.getSuccessfulDevices().stream()
                .map(Device::getImei)
                .collect(Collectors.toList()))
            .failedRecords(result.getFailedDevices())
            .build();
            
        return ResponseEntity.ok(ApiResponse.success(response));
    }
} 