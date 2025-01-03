package com.montage.device.controller;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.entity.OtaGroupXref;
import com.montage.device.service.impl.OtaGroupXrefServiceImpl;
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
@RequestMapping("/api/ota")
@RequiredArgsConstructor
@Tag(name = "OTA Group Management", description = "APIs for managing OTA groups")
public class OtaGroupXrefController {

    private final OtaGroupXrefServiceImpl otaGroupService;

    @Operation(summary = "Search OTA groups")
    @PostMapping("v1/group/search")
    public ResponseEntity<ApiResponse<Page<OtaGroupXref>>> searchGroups(
            @RequestBody SearchRequest searchRequest) {
        log.info("Searching OTA groups with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(otaGroupService.search(searchRequest)));
    }

    @Operation(summary = "Get OTA group by ID")
    @GetMapping("v1/group/{id}")
    public ResponseEntity<ApiResponse<OtaGroupXref>> getGroup(
            @PathVariable Integer id) {
        log.info("Finding OTA group by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(otaGroupService.findById(id)));
    }

    @Operation(summary = "Create new OTA group")
    @PostMapping("v1/group")
    public ResponseEntity<ApiResponse<OtaGroupXref>> createGroup(
            @Valid @RequestBody OtaGroupXref otaGroup) {
        log.info("Creating new OTA group: {}", otaGroup);
        return ResponseEntity.ok(ApiResponse.success(otaGroupService.create(otaGroup)));
    }

    @Operation(summary = "Update existing OTA group")
    @PutMapping("v1/group/{id}")
    public ResponseEntity<ApiResponse<OtaGroupXref>> updateGroup(
            @PathVariable Integer id, 
            @Valid @RequestBody OtaGroupXref otaGroup) {
        log.info("Updating OTA group with id {}: {}", id, otaGroup);
        return ResponseEntity.ok(ApiResponse.success(otaGroupService.update(id, otaGroup)));
    }

    @Operation(summary = "Delete OTA group")
    @DeleteMapping("v1/group/{id}")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable Integer id) {
        log.info("Deleting OTA group with id: {}", id);
        otaGroupService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 