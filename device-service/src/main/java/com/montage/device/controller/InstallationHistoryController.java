package com.montage.device.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.entity.InstallationHistory;
import com.montage.device.service.impl.InstallationHistoryServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/installation-history")
@RequiredArgsConstructor
@Tag(name = "Installation History Management", description = "APIs for managing installation history")
public class InstallationHistoryController {

    private final InstallationHistoryServiceImpl installationHistoryService;

    @Operation(summary = "Search installation history")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<InstallationHistory>>> search(@RequestBody SearchRequest searchRequest) {
        log.info("Searching installation history with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(installationHistoryService.search(searchRequest)));
    }

    @Operation(summary = "Get installation history by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InstallationHistory>> findById(@PathVariable Integer id) {
        log.info("Finding installation history by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(installationHistoryService.findById(id)));
    }

    @Operation(summary = "Create new installation history")
    @PostMapping
    public ResponseEntity<ApiResponse<InstallationHistory>> create(@Valid @RequestBody InstallationHistory history) {
        log.info("Creating new installation history: {}", history);
        return ResponseEntity.ok(ApiResponse.success(installationHistoryService.create(history)));
    }

    @Operation(summary = "Get installation history by device ID")
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<ApiResponse<Page<InstallationHistory>>> findByDeviceId(
            @PathVariable Integer deviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Finding installation history for device id: {}", deviceId);
        return ResponseEntity.ok(ApiResponse.success(
            installationHistoryService.findByDeviceId(deviceId, page, size)));
    }

    @Operation(summary = "Get installation history by customer ID")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<Page<InstallationHistory>>> findByCustomerId(
            @PathVariable Integer customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Finding installation history for customer id: {}", customerId);
        return ResponseEntity.ok(ApiResponse.success(
            installationHistoryService.findByCustomerId(customerId, page, size)));
    }
} 