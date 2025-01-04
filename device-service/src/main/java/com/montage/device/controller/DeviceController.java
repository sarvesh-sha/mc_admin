package com.montage.device.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.BulkUploadResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.dto.request.DeviceRequest;
import com.montage.device.dto.response.DeviceResponse;
import com.montage.device.service.DeviceService;

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

    private final DeviceService deviceService;

    @Operation(summary = "Search devices")
    @PostMapping("v1/device/search")
    public ResponseEntity<ApiResponse<Page<DeviceResponse>>> searchDevices(
            @RequestBody SearchRequest searchRequest) {
        log.info("Searching devices with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(deviceService.search(searchRequest)));
    }

    @Operation(summary = "Get device by ID")
    @GetMapping("v1/device/{id}")
    public ResponseEntity<ApiResponse<DeviceResponse>> getDevice(
            @PathVariable Integer id) {
        log.info("Finding device by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(deviceService.findById(id)));
    }

    @Operation(summary = "Create new device")
    @PostMapping("v1/device")
    public ResponseEntity<ApiResponse<DeviceResponse>> createDevice(
            @Valid @RequestBody DeviceRequest request) {
        log.info("Creating new device: {}", request);
        return ResponseEntity.ok(ApiResponse.success(deviceService.create(request)));
    }

    @Operation(summary = "Update existing device")
    @PutMapping("v1/device/{id}")
    public ResponseEntity<ApiResponse<DeviceResponse>> updateDevice(
            @PathVariable Integer id, 
            @Valid @RequestBody DeviceRequest request) {
        log.info("Updating device with id {}: {}", id, request);
        return ResponseEntity.ok(ApiResponse.success(deviceService.updateDevice(id, request)));
    }

    @Operation(summary = "Delete device")
    @DeleteMapping("v1/device/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDevice(@PathVariable Integer id) {
        log.info("Deleting device with id: {}", id);
        deviceService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Device with ID " + id + " successfully deleted"));
    }

    @Operation(summary = "Bulk upload devices")
    @PostMapping("v1/device/bulk-upload")
    public ResponseEntity<ApiResponse<BulkUploadResponse<String>>> bulkUploadDevices(
            @RequestBody List<DeviceRequest> requests) {
        log.info("Processing bulk upload for {} devices", requests.size());
        return ResponseEntity.ok(ApiResponse.success(deviceService.bulkUpload(requests)));
    }
} 