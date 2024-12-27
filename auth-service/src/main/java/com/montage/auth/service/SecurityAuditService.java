package com.montage.auth.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityAuditService {

    private final JdbcTemplate jdbcTemplate;

    public void logAuthenticationAttempt(String username, String authType, boolean success, String ipAddress) {
        String sql = "INSERT INTO security_audit_log (username, auth_type, success, ip_address, attempt_time) " +
                    "VALUES (?, ?, ?, ?, NOW())";
        
        jdbcTemplate.update(sql, username, authType, success, ipAddress);
        
        if (!success) {
            checkBruteForceAttempts(username, ipAddress);
        }
    }

    private void checkBruteForceAttempts(String username, String ipAddress) {
        String sql = "SELECT COUNT(*) FROM security_audit_log " +
                    "WHERE username = ? AND success = false AND " +
                    "attempt_time > NOW() - INTERVAL '15 MINUTE'";
        
        int failedAttempts = jdbcTemplate.queryForObject(sql, Integer.class, username);
        
        if (failedAttempts >= 5) {
            log.warn("Possible brute force attack detected for user: {} from IP: {}", username, ipAddress);
            // Implement additional security measures (e.g., temporary account lock)
        }
    }
} 