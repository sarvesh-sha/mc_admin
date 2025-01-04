package com.montage.device.controller;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.dto.request.SensorRequest;
import com.montage.device.dto.response.SensorResponse;
import com.montage.device.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
@Tag(name = "Sensors Management", description = "APIs for managing sensors")
public class SensorController {

    private final SensorService sensorService;

    @Operation(summary = "Search sensors")
    @PostMapping("v1/sensor/search")
    public ResponseEntity<ApiResponse<Page<SensorResponse>>> searchSensors(
            @Valid @RequestBody SearchRequest searchRequest) {
        log.info("Searching sensors with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(sensorService.search(searchRequest)));
    }

    @Operation(summary = "Get sensor by ID")
    @GetMapping("v1/sensor/{id}")
    public ResponseEntity<ApiResponse<SensorResponse>> getSensor(
            @PathVariable Integer id) {
        log.info("Finding sensor by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(sensorService.findById(id)));
    }

    @Operation(summary = "Create new sensor")
    @PostMapping("v1/sensor")
    public ResponseEntity<ApiResponse<SensorResponse>> createSensor(
            @Valid @RequestBody SensorRequest request) {
        log.info("Creating new sensor: {}", request);
        return ResponseEntity.ok(ApiResponse.success(sensorService.create(request)));
    }

    @Operation(summary = "Update existing sensor")
    @PutMapping("v1/sensor/{id}")
    public ResponseEntity<ApiResponse<SensorResponse>> updateSensor(
            @PathVariable Integer id, 
            @Valid @RequestBody SensorRequest request) {
        log.info("Updating sensor with id {}: {}", id, request);
        return ResponseEntity.ok(ApiResponse.success(sensorService.updateSensor(id, request)));
    }

    @Operation(summary = "Delete sensor")
    @DeleteMapping("v1/sensor/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSensor(
            @PathVariable Integer id) {
        log.info("Deleting sensor with id: {}", id);
        sensorService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Sensor with ID " + id + " successfully deleted"));
    }

    @Operation(summary = "Get sensors by device ID")
    @GetMapping("v1/sensor/device/{deviceId}")
    public ResponseEntity<ApiResponse<Page<SensorResponse>>> getSensorsByDevice(
            @PathVariable Integer deviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Finding sensors for device id: {}", deviceId);
        return ResponseEntity.ok(ApiResponse.success(sensorService.findByDeviceId(deviceId, page, size)));
    }
} 