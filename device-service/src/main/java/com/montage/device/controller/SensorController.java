package com.montage.device.controller;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.entity.Sensor;
import com.montage.device.service.impl.SensorServiceImpl;
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

    private final SensorServiceImpl sensorsService;

    @Operation(summary = "Search sensors")
    @PostMapping("v1/sensor/search")
    public ResponseEntity<ApiResponse<Page<Sensor>>> searchSensors(
            @RequestBody SearchRequest searchRequest) {
        log.info("Searching sensors with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(sensorsService.search(searchRequest)));
    }

    @Operation(summary = "Get sensor by ID")
    @GetMapping("v1/sensor/{id}")
    public ResponseEntity<ApiResponse<Sensor>> getSensor(
            @PathVariable Integer id) {
        log.info("Finding sensor by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(sensorsService.findById(id)));
    }

    @Operation(summary = "Create new sensor")
    @PostMapping("v1/sensor")
    public ResponseEntity<ApiResponse<Sensor>> createSensor(
            @Valid @RequestBody Sensor sensor) {
        log.info("Creating new sensor: {}", sensor);
        return ResponseEntity.ok(ApiResponse.success(sensorsService.create(sensor)));
    }

    @Operation(summary = "Update existing sensor")
    @PutMapping("v1/sensor/{id}")
    public ResponseEntity<ApiResponse<Sensor>> updateSensor(
            @PathVariable Integer id, 
            @Valid @RequestBody Sensor sensor) {
        log.info("Updating sensor with id {}: {}", id, sensor);
        return ResponseEntity.ok(ApiResponse.success(sensorsService.update(id, sensor)));
    }

    @Operation(summary = "Delete sensor")
    @DeleteMapping("v1/sensor/{id}")
    public ResponseEntity<Void> deleteSensor(
            @PathVariable Integer id) {
        log.info("Deleting sensor with id: {}", id);
        sensorsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get sensors by device ID")
    @GetMapping("v1/sensor/device/{deviceId}")
    public ResponseEntity<ApiResponse<Page<Sensor>>> getSensorsByDevice(
            @PathVariable Integer deviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Finding sensors for device id: {}", deviceId);
        return ResponseEntity.ok(ApiResponse.success(sensorsService.findByDeviceId(deviceId, page, size)));
    }
} 