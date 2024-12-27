package com.montage.common.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public void cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(config)
//                .withCacheConfiguration("devices", 
//                    RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)))
//                .withCacheConfiguration("assets", 
//                    RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(45)))
//                .
    }
} 