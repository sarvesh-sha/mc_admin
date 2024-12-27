package com.montage.auth.controller;

import com.montage.auth.annotation.RequireAdmin;
import com.montage.auth.dto.UserDTO;
import com.montage.auth.service.UserService;
import com.montage.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    
    @PostMapping
    // @RequireAdmin
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        
        return ResponseEntity.ok(userMapper.toDTO(userService.createUser(userMapper.toEntity(userDTO))));
    }
    
    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id)")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userMapper.toDTO(userService.updateUser(id, userMapper.toEntity(userDTO))));
    }
    
    @DeleteMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}")
   // @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER') or @userSecurity.isOwner(#id)")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toDTO(userService.getUserById(id)));
    }
    
    @GetMapping
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable).map(userMapper::toDTO));
    }
} 