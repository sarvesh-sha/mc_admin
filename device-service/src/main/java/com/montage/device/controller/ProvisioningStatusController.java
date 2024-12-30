package com.montage.device.controller;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.entity.ProvisioningStatus;
import com.montage.device.service.impl.ProvisioningStatusServiceImpl;
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
@RequestMapping("/provisioning-status")
@RequiredArgsConstructor
@Tag(name = "Provisioning Status Management", description = "APIs for managing provisioning statuses")
public class ProvisioningStatusController {

    private final ProvisioningStatusServiceImpl provisioningStatusService;

    @Operation(summary = "Search provisioning statuses")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProvisioningStatus>>> search(@RequestBody SearchRequest searchRequest) {
        log.info("Searching provisioning statuses with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(provisioningStatusService.search(searchRequest)));
    }

    @Operation(summary = "Get provisioning status by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProvisioningStatus>> findById(@PathVariable Integer id) {
        log.info("Finding provisioning status by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(provisioningStatusService.findById(id)));
    }

    @Operation(summary = "Create new provisioning status")
    @PostMapping
    public ResponseEntity<ApiResponse<ProvisioningStatus>> create(@Valid @RequestBody ProvisioningStatus status) {
        log.info("Creating new provisioning status: {}", status);
        return ResponseEntity.ok(ApiResponse.success(provisioningStatusService.create(status)));
    }

    @Operation(summary = "Update existing provisioning status")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProvisioningStatus>> update(
            @PathVariable Integer id, 
            @Valid @RequestBody ProvisioningStatus status) {
        log.info("Updating provisioning status with id {}: {}", id, status);
        return ResponseEntity.ok(ApiResponse.success(provisioningStatusService.update(id, status)));
    }

    @Operation(summary = "Delete provisioning status")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Deleting provisioning status with id: {}", id);
        provisioningStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 