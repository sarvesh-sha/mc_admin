package com.montage.auth.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.montage.auth.annotation.RequireAdmin;
import com.montage.auth.dto.PermissionDTO;
import com.montage.auth.service.PermissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
    
    private final PermissionService permissionService;
    
    @PostMapping
    @RequireAdmin
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO permissionDTO) {
        return ResponseEntity.ok(permissionService.createPermission(permissionDTO));
    }
    
    @PutMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<PermissionDTO> updatePermission(
            @PathVariable Long id, 
            @Valid @RequestBody PermissionDTO permissionDTO) {
        return ResponseEntity.ok(permissionService.updatePermission(id, permissionDTO));
    }
    
    @DeleteMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermission(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }
    
    @GetMapping
    public ResponseEntity<Page<PermissionDTO>> getAllPermissions(Pageable pageable) {
        return ResponseEntity.ok(permissionService.getAllPermissions(pageable));
    }
} 