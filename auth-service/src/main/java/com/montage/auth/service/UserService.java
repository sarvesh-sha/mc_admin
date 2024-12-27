package com.montage.auth.service;

import com.montage.auth.entity.User;
import com.montage.auth.entity.AuthProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    User getUserByEmail(String email);
    Page<User> getAllUsers(Pageable pageable);
    User findOrCreateAzureUser(String email, String name);
    List<User> findAllByAuthProvider(AuthProvider provider);
   
} 