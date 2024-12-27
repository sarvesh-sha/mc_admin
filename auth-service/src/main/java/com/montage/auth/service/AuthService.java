package com.montage.auth.service;

import com.montage.auth.dto.LoginRequest;
import com.montage.auth.dto.LoginResponse;
import com.montage.auth.dto.UserDTO;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    void logout(String token);
} 