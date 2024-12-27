package com.montage.auth.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.montage.auth.dto.PermissionDTO;
import com.montage.auth.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    
    PermissionDTO toDTO(Permission permission);
    
    Permission toEntity(PermissionDTO permissionDTO);
    
    List<PermissionDTO> toDTOList(List<Permission> permissions);
    
    List<Permission> toEntityList(List<PermissionDTO> permissionDTOs);
} 