package com.montage.device.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GenericMapper {
    
    private final ObjectMapper objectMapper;
    
    public <T> T convert(Object source, Class<T> targetClass) {
        return objectMapper.convertValue(source, targetClass);
    }
    
    public <T> Page<T> convertPage(Page<?> source, Class<T> targetClass) {
        return source.map(item -> convert(item, targetClass));
    }
} 