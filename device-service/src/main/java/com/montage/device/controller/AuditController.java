package com.montage.device.controller;

import com.montage.common.dto.ApiResponse;
import com.montage.device.dto.request.AuditSearchRequest;
import com.montage.device.dto.response.AuditResponse;
import com.montage.device.service.AuditService;

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
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Tag(name = "Audit Management", description = "APIs for managing audit logs")
public class AuditController {

    private final AuditService auditService;

    @Operation(summary = "Search audit logs with filters")
    @PostMapping("v1/search")
    public ResponseEntity<ApiResponse<Page<AuditResponse>>> searchAudits(
            @Valid @RequestBody AuditSearchRequest request) {
        log.info("Searching audit logs with criteria: {}", request);
        return ResponseEntity.ok(ApiResponse.success(auditService.searchAudits(request)));
    }

    @Operation(summary = "Get audit logs by entity")
    @GetMapping("v1/entity/{entityName}/{entityId}")
    public ResponseEntity<ApiResponse<Page<AuditResponse>>> getAuditsByEntity(
            @PathVariable String entityName,
            @PathVariable Integer entityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Finding audit logs for entity: {} with id: {}", entityName, entityId);
        return ResponseEntity.ok(ApiResponse.success(
            auditService.getAuditsByEntity(entityName, entityId, page, size)));
    }
} 