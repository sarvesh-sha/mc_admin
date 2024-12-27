package com.montage.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        String requestId = UUID.randomUUID().toString();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        
        try {
            log.info("[{}] Request: {} {} {}", 
                    requestId,
                    request.getMethod(),
                    request.getRequestURI(),
                    new String(requestWrapper.getContentAsByteArray()));

            filterChain.doFilter(requestWrapper, responseWrapper);
            
            long duration = System.currentTimeMillis() - startTime;
            
            log.info("[{}] Response: {} {} {}ms {}", 
                    requestId,
                    request.getMethod(),
                    request.getRequestURI(),
                    duration,
                    new String(responseWrapper.getContentAsByteArray()));
        } finally {
            responseWrapper.copyBodyToResponse();
        }
    }
} 