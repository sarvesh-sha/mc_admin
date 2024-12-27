package com.montage.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String generateToken(Authentication authentication);
    String generateToken(String username, Map<String, Object> claims);
    
    String generateRefreshToken(String username);
    boolean validateToken(String token);
    String extractUsername(String token);
    void invalidateToken(String token);
    Map<String, Object> extractClaims(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
} 