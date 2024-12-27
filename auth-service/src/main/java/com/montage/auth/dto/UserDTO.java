package com.montage.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String firstName;
    private String lastName;
    private String phone;
    private String password;
    private List<String> roles;
    private List<String> permissions;
    private boolean enabled;
    private String provider;
} 