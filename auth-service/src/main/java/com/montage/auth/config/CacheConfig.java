package com.montage.auth.config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.cache.CacheProperties.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    
//    @Bean
//    public CacheManager cacheManager() {
//        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
//        cacheManager.setCacheNames(Arrays.asList("permissions", "roles", "users"));
//        cacheManager.setCaffeine(caffeineCacheBuilder());
//        return cacheManager;
//    }
//    
//    private Caffeine<Object, Object> caffeineCacheBuilder() {
//        return Caffeine.newBuilder()
//            .maximumSize(1000)
//            .expireAfterWrite(30, TimeUnit.MINUTES)
//            .recordStats();
//    }
} 