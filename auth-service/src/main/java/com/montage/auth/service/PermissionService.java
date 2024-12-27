package com.montage.auth.service;

import com.montage.auth.dto.PermissionDTO;
import com.montage.auth.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PermissionService {
    PermissionDTO createPermission(PermissionDTO permissionDTO);
    PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO);
    void deletePermission(Long id);
    PermissionDTO getPermissionById(Long id);
    Page<PermissionDTO> getAllPermissions(Pageable pageable);
    List<PermissionDTO> getPermissionsByNames(List<String> permissionNames);
} 