package com.montage.device.service.impl;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.montage.common.dto.SearchRequest;
import com.montage.common.specification.GenericSpecificationBuilder;
import com.montage.device.dto.request.SensorRequest;
import com.montage.device.dto.response.SensorResponse;
import com.montage.device.entity.Device;
import com.montage.device.entity.Sensor;
import com.montage.device.mapper.GenericMapper;
import com.montage.device.repository.SensorRepository;
import com.montage.device.service.SensorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final DeviceServiceImpl deviceService;
    private final GenericMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public SensorResponse findById(Integer id) {
        Sensor sensor = findSensorById(id);
        return mapper.convert(sensor, SensorResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SensorResponse> search(SearchRequest searchRequest) {
        var specification = new GenericSpecificationBuilder<Sensor>(searchRequest.getFilters()).build();
        var pageable = PageRequest.of(
            searchRequest.getPage(), 
            searchRequest.getSize(), 
            Sort.by(searchRequest.getSorts().stream()
                .map(sort -> Sort.Order.by(sort.getField())
                    .with(Sort.Direction.fromString(sort.getDirection())))
                .toList())
        );
        
        return sensorRepository.findAll(specification, pageable)
                .map(sensor -> mapper.convert(sensor, SensorResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SensorResponse> findByDeviceId(Integer deviceId, int page, int size) {
        deviceService.findById(deviceId); // Verify device exists
        return sensorRepository.findByDeviceId(deviceId, PageRequest.of(page, size))
                .map(sensor -> mapper.convert(sensor, SensorResponse.class));
    }

    @Override
    @Transactional
    public SensorResponse create(SensorRequest request) {
        Sensor sensor = mapper.convert(request, Sensor.class);
        handleNestedObjects(sensor, request);
        validateSensor(sensor);
        
        Sensor savedSensor = sensorRepository.save(sensor);
        return mapper.convert(savedSensor, SensorResponse.class);
    }

    @Override
    @Transactional
    public SensorResponse updateSensor(Integer id, SensorRequest request) {
        findSensorById(id); // Verify exists
        
        Sensor sensor = mapper.convert(request, Sensor.class);
        sensor.setId(id);
        handleNestedObjects(sensor, request);
        validateSensorUpdate(sensor);
        
        Sensor savedSensor = sensorRepository.save(sensor);
        return mapper.convert(savedSensor, SensorResponse.class);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        findSensorById(id); // Verify exists
        sensorRepository.deleteById(id);
    }

    private Sensor findSensorById(Integer id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor not found with id: " + id));
    }

    private void handleNestedObjects(Sensor sensor, SensorRequest request) {
        if (request.getDeviceId() != null) {
            Device device = new Device();
            device.setId(request.getDeviceId());
            sensor.setDevice(device);
        }
    }

    private void validateSensor(Sensor sensor) {
        if (sensor.getDevice() != null) {
            deviceService.findById(sensor.getDevice().getId());
        }
        // Add additional validations as needed
    }

    private void validateSensorUpdate(Sensor sensor) {
        validateSensor(sensor);
        // Add update-specific validations if needed
    }
} 