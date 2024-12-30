package com.montage.device.config;

import org.springframework.data.domain.AuditorAware;
//import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
       // String username = SecurityContextHolder.getContext().getAuthentication() != null ?
       //         SecurityContextHolder.getContext().getAuthentication().getName() : "system";
      //  return Optional.of(username);
    	  return Optional.of("system");
    }
} 