package com.montage.auth.exception;

public class AzureAuthenticationException extends RuntimeException {
    
    public AzureAuthenticationException(String message) {
        super(message);
    }
    
    public AzureAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
} 