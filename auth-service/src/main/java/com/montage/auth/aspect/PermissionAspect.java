package com.montage.auth.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.montage.auth.annotation.RequirePermission;
import com.montage.auth.config.PermissionEvaluator;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {
    
    private final PermissionEvaluator permissionEvaluator;
    
    @Before("@annotation(requirePermission)")
    public void checkPermission(JoinPoint joinPoint, RequirePermission requirePermission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            throw new AccessDeniedException("Authentication is required");
        }
        
//        if (!permissionEvaluator.hasPermission(authentication, null, requirePermission.value())) {
//            throw new AccessDeniedException(String.format(
//                "Access denied. User '%s' does not have required permission: %s",
//                authentication.getName(),
//                requirePermission.value()
//            ));
//        }
    }
} 