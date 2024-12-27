package com.montage.auth.service;

import org.springframework.stereotype.Service;


import com.montage.auth.dto.UserDTO;
import com.montage.auth.entity.User;
import com.montage.auth.exception.TokenRefreshException;
import com.montage.auth.mapper.UserMapper;
import com.montage.auth.model.AuthResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenRefreshService {
	
	private final UserMapper userMapper;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthResponse refreshToken(String refreshToken) {
        try   {
                return refreshJwtToken(refreshToken);
            
        } catch (Exception e) {
            throw new TokenRefreshException("Failed to refresh token: " + e.getMessage());
        }
    }

    private AuthResponse refreshJwtToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        User user = userService.getUserByEmail(username);
        UserDTO userDTO = userMapper.toDTO(user);
        
       // if (jwtService.isTokenValid(refreshToken, user)) {
            //String newToken = jwtService.generateToken(user);
        String newToken = "test"; 
        return AuthResponse.builder()
                .token(newToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    private AuthResponse refreshAzureToken(String refreshToken) {
        // Implement Azure token refresh logic using MSAL
        // This would typically involve calling Azure AD token endpoint
        throw new UnsupportedOperationException("Azure token refresh not implemented");
    }
} 