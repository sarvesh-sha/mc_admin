package com.montage.device.controller;

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
import com.montage.common.dto.SearchRequest;
import com.montage.device.dto.request.ProvisioningStatusRequest;
import com.montage.device.dto.response.ProvisioningStatusResponse;
import com.montage.device.service.ProvisioningStatusService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/provisioning")
@RequiredArgsConstructor
@Tag(name = "Provisioning Status Management", description = "APIs for managing provisioning statuses")
public class ProvisioningStatusController {

    private final ProvisioningStatusService provisioningStatusService;

    @Operation(summary = "Search provisioning statuses")
    @PostMapping("v1/status/search")
    public ResponseEntity<ApiResponse<Page<ProvisioningStatusResponse>>> searchStatus(
            @RequestBody SearchRequest searchRequest) {
        log.info("Searching provisioning statuses with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(provisioningStatusService.search(searchRequest)));
    }

    @Operation(summary = "Get provisioning status by ID")
    @GetMapping("v1/status/{id}")
    public ResponseEntity<ApiResponse<ProvisioningStatusResponse>> getStatus(
            @PathVariable Integer id) {
        log.info("Finding provisioning status by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(provisioningStatusService.findById(id)));
    }

    @Operation(summary = "Create new provisioning status")
    @PostMapping("v1/status")
    public ResponseEntity<ApiResponse<ProvisioningStatusResponse>> createStatus(
            @Valid @RequestBody ProvisioningStatusRequest request) {
        log.info("Creating new provisioning status: {}", request);
        return ResponseEntity.ok(ApiResponse.success(provisioningStatusService.create(request)));
    }

    @Operation(summary = "Update existing provisioning status")
    @PutMapping("v1/status/{id}")
    public ResponseEntity<ApiResponse<ProvisioningStatusResponse>> updateStatus(
            @PathVariable Integer id, 
            @Valid @RequestBody ProvisioningStatusRequest request) {
        log.info("Updating provisioning status with id {}: {}", id, request);
        return ResponseEntity.ok(ApiResponse.success(provisioningStatusService.update(id, request)));
    }

    @Operation(summary = "Delete provisioning status")
    @DeleteMapping("v1/status/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Integer id) {
        log.info("Deleting provisioning status with id: {}", id);
        provisioningStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 