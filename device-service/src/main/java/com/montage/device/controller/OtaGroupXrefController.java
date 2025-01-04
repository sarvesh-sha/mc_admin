package com.montage.device.controller;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.dto.request.OtaGroupRequest;
import com.montage.device.dto.response.OtaGroupResponse;
import com.montage.device.service.OtaGroupService;
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

    private final OtaGroupService otaGroupService;

    @Operation(summary = "Search OTA groups")
    @PostMapping("v1/group/search")
    public ResponseEntity<ApiResponse<Page<OtaGroupResponse>>> searchGroups(
            @RequestBody SearchRequest searchRequest) {
        log.info("Searching OTA groups with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(otaGroupService.search(searchRequest)));
    }

    @Operation(summary = "Get OTA group by ID")
    @GetMapping("v1/group/{id}")
    public ResponseEntity<ApiResponse<OtaGroupResponse>> getGroup(
            @PathVariable Integer id) {
        log.info("Finding OTA group by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(otaGroupService.findById(id)));
    }

    @Operation(summary = "Create new OTA group")
    @PostMapping("v1/group")
    public ResponseEntity<ApiResponse<OtaGroupResponse>> createGroup(
            @Valid @RequestBody OtaGroupRequest request) {
        log.info("Creating new OTA group: {}", request);
        return ResponseEntity.ok(ApiResponse.success(otaGroupService.create(request)));
    }

    @Operation(summary = "Update existing OTA group")
    @PutMapping("v1/group/{id}")
    public ResponseEntity<ApiResponse<OtaGroupResponse>> updateGroup(
            @PathVariable Integer id, 
            @Valid @RequestBody OtaGroupRequest request) {
        log.info("Updating OTA group with id {}: {}", id, request);
        return ResponseEntity.ok(ApiResponse.success(otaGroupService.updateGroup(id, request)));
    }

    @Operation(summary = "Delete OTA group")
    @DeleteMapping("v1/group/{id}")
    public ResponseEntity<ApiResponse<String>> deleteGroup(
            @PathVariable Integer id) {
        log.info("Deleting OTA group with id: {}", id);
        otaGroupService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("OTA Group with ID " + id + " successfully deleted"));
    }

    @Operation(summary = "Get OTA groups by customer ID")
    @GetMapping("v1/group/customer/{customerId}")
    public ResponseEntity<ApiResponse<Page<OtaGroupResponse>>> getGroupsByCustomer(
            @PathVariable Integer customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Finding OTA groups for customer id: {}", customerId);
        return ResponseEntity.ok(ApiResponse.success(otaGroupService.findByCustomerId(customerId, page, size)));
    }
} 