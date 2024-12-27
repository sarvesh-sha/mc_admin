package com.montage.auth.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.montage.auth.dto.UserDTO;
import com.montage.auth.entity.Role;
import com.montage.auth.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "password", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedAt", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "active", ignore = true)
//    @Mapping(target = "authProvider", ignore = true)
    UserDTO toDTO(User user);
    
    //@Mapping(target = "roles", ignore = true)
   // @Mapping(target = "permissions", ignore = true)
   // @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    //@Mapping(target = "active", constant = "true")
    User toEntity(UserDTO userDTO);

    @AfterMapping
    default void mapRoles(User user, @MappingTarget UserDTO userDTO) {
    	
        if (user.getRoles() != null) {
            userDTO.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
        }
    }

    default Set<Role> mapRoles(List<String> roleNames) {
        if (roleNames == null) {
            return null;
        }
        return roleNames.stream()
            .map(name -> {
                Role role = new Role();
                role.setName(name);
                return role;
            })
            .collect(Collectors.toSet());
    }
} 