package com.montage.auth.service.impl;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.montage.auth.dto.LoginRequest;
import com.montage.auth.dto.LoginResponse;
import com.montage.auth.entity.User;
import com.montage.auth.mapper.UserMapper;
import com.montage.auth.service.AuthService;
import com.montage.auth.service.JwtService;
import com.montage.auth.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserMapper userMapper;
    
    @Override
    public LoginResponse login(LoginRequest request) {
    	
    	
    	
    	
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        String token = jwtService.generateToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(authentication.getName());
        
        User user = userService.getUserByEmail(authentication.getName());
        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                //.name(user.getName())
                .roles(new ArrayList<>()) // Add roles if implemented
                .permissions(new ArrayList<>()) // Add permissions if implemented
                .build();
    }
    
    @Override
    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        jwtService.invalidateToken(token);
    }
    
   
} 