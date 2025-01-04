package com.montage.device.service;

import org.springframework.data.domain.Page;
import com.montage.common.dto.SearchRequest;
import com.montage.device.dto.request.SensorRequest;
import com.montage.device.dto.response.SensorResponse;

public interface SensorService {
    SensorResponse findById(Integer id);
    Page<SensorResponse> search(SearchRequest searchRequest);
    SensorResponse create(SensorRequest request);
    SensorResponse updateSensor(Integer id, SensorRequest request);
    void delete(Integer id);
    Page<SensorResponse> findByDeviceId(Integer deviceId, int page, int size);
} 