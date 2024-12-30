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
@RequestMapping("/sensors")
@RequiredArgsConstructor
@Tag(name = "Sensor Management", description = "APIs for managing sensors")
public class SensorController {

    private final SensorServiceImpl sensorService;

    @Operation(summary = "Search sensors")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<Sensor>>> search(@RequestBody SearchRequest searchRequest) {
        log.info("Searching sensors with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(sensorService.search(searchRequest)));
    }

    @Operation(summary = "Get sensor by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Sensor>> findById(@PathVariable Integer id) {
        log.info("Finding sensor by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(sensorService.findById(id)));
    }

    @Operation(summary = "Create new sensor")
    @PostMapping
    public ResponseEntity<ApiResponse<Sensor>> create(@Valid @RequestBody Sensor sensor) {
        log.info("Creating new sensor: {}", sensor);
        return ResponseEntity.ok(ApiResponse.success(sensorService.create(sensor)));
    }

    @Operation(summary = "Update existing sensor")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Sensor>> update(
            @PathVariable Integer id, 
            @Valid @RequestBody Sensor sensor) {
        log.info("Updating sensor with id {}: {}", id, sensor);
        return ResponseEntity.ok(ApiResponse.success(sensorService.update(id, sensor)));
    }

    @Operation(summary = "Delete sensor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Deleting sensor with id: {}", id);
        sensorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get sensors by device ID")
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<ApiResponse<Page<Sensor>>> findByDeviceId(
            @PathVariable Integer deviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Finding sensors for device id: {}", deviceId);
        return ResponseEntity.ok(ApiResponse.success(sensorService.findByDeviceId(deviceId, page, size)));
    }
} 