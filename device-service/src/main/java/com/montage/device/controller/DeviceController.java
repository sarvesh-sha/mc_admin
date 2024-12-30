package com.montage.device.controller;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.entity.Device;
import com.montage.device.service.impl.DeviceServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceServiceImpl deviceService;

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<Device>>> search(@RequestBody SearchRequest searchRequest) {
        log.info("Searching devices with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(deviceService.search(searchRequest)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Device>> findById(@PathVariable Integer id) {
        log.info("Finding device by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(deviceService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Device>> create(@Valid @RequestBody Device device) {
        log.info("Creating new device: {}", device);
        return ResponseEntity.ok(ApiResponse.success(deviceService.create(device)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Device>> update(
            @PathVariable Integer id, 
            @Valid @RequestBody Device device) {
        log.info("Updating device with id {}: {}", id, device);
        return ResponseEntity.ok(ApiResponse.success(deviceService.update(id, device)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Deleting device with id: {}", id);
        deviceService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 