package com.montage.auth.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class PermissionEvaluator
{
//implements CustomPermissionEvaluator {
//    
//    private final UserRepository userRepository;
//    
//    public PermissionEvaluator(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//    
//    @Override
//    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
//        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) {
//            return false;
//        }
//        
//        String username = authentication.getName();
//        User user = userRepository.findByEmail(username)
//            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//            
//        return user.getRoles().stream()
//            .flatMap(role -> role.getPermissions().stream())
//            .anyMatch(p -> p.getName().equals(permission));
//    }
//    
//    @Override
//    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
//        return false;
//    }
} 