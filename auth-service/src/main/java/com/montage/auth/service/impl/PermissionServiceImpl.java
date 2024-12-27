package com.montage.auth.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.montage.auth.annotation.RequireAdmin;
import com.montage.auth.dto.PermissionDTO;
import com.montage.auth.entity.Permission;
import com.montage.auth.exception.ResourceNotFoundException;
import com.montage.auth.mapper.PermissionMapper;
import com.montage.auth.repository.PermissionRepository;
import com.montage.auth.service.PermissionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "permissions")
public class PermissionServiceImpl implements PermissionService {
    
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    @RequireAdmin
    @Transactional
    @CacheEvict(allEntries = true)
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
//        if (permissionRepository.existsByName(permissionDTO.getName())) {
//            throw new IllegalArgumentException("Permission name already exists");
//        }
        Permission permission = permissionMapper.toEntity(permissionDTO);
        permission = permissionRepository.save(permission);
        return permissionMapper.toDTO(permission);
    }

    @Override
    @Cacheable(key = "#id")
    public PermissionDTO getPermissionById(Long id) {
        return permissionRepository.findById(id)
            .map(permissionMapper::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Permission not found",null));
    }

    @Override
    @RequireAdmin
    @Transactional
    @CacheEvict(key = "#id")
    public PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO) {
        Permission permission = permissionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Permission not found",null));
        
        permission.setName(permissionDTO.getName());
        permission.setDescription(permissionDTO.getDescription());
        
        permission = permissionRepository.save(permission);
        return permissionMapper.toDTO(permission);
    }

    @Override
    @RequireAdmin
    @Transactional
    @CacheEvict(allEntries = true)
    public void deletePermission(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Permission not found",null);
        }
        permissionRepository.deleteById(id);
    }

    @Override
    @Cacheable(key = "'all'.concat(#pageable.toString())")
    public Page<PermissionDTO> getAllPermissions(Pageable pageable) {
        return permissionRepository.findAll(pageable)
            .map(permissionMapper::toDTO);
    }

    @Override
    @Cacheable(key = "#names")
    public List<PermissionDTO> getPermissionsByNames(List<String> names) { 
        return permissionRepository.findByNameIn(names)   
            .stream()
            .map(permissionMapper::toDTO)
            .collect(Collectors.toList());
    }
} 