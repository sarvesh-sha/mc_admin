package com.montage.device.service.impl;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.montage.common.dto.SearchRequest;
import com.montage.common.service.BaseService;
import com.montage.common.specification.GenericSpecificationBuilder;
import com.montage.device.entity.Sensor;
import com.montage.device.repository.SensorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorServiceImpl implements BaseService<Sensor, Integer> {

    private final SensorRepository sensorRepository;
    private final DeviceServiceImpl deviceService;

    @Override
    @Transactional(readOnly = true)
    public Sensor findById(Integer id) {
        log.debug("Finding sensor by id: {}", id);
        return sensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sensor> search(SearchRequest searchRequest) {
        log.debug("Searching sensors with criteria: {}", searchRequest);
        
        var specification = new GenericSpecificationBuilder<Sensor>(searchRequest.getFilters()).build();
        var pageable = PageRequest.of(
            searchRequest.getPage(), 
            searchRequest.getSize(), 
            Sort.by(searchRequest.getSorts().stream()
                .map(sort -> Sort.Order.by(sort.getField())
                    .with(Sort.Direction.fromString(sort.getDirection())))
                .toList())
        );
        
        return sensorRepository.findAll(specification, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Sensor> findByDeviceId(Integer deviceId, int page, int size) {
        log.debug("Finding sensors for device id: {}", deviceId);
        // Verify device exists
        deviceService.findById(deviceId);
        return sensorRepository.findByDeviceId(deviceId, PageRequest.of(page, size));
    }

    @Override
    @Transactional
    public Sensor create(Sensor sensor) {
        log.debug("Creating new sensor: {}", sensor);
        // Verify device exists if device is set
        if (sensor.getDevice() != null) {
            deviceService.findById(sensor.getDevice().getId());
        }
        return sensorRepository.save(sensor);
    }

    @Override
    @Transactional
    public Sensor update(Integer id, Sensor sensor) {
        log.debug("Updating sensor with id {}: {}", id, sensor);
        
        var existingSensor = findById(id);
        // Update fields
        existingSensor.setStatus(sensor.getStatus());
        existingSensor.setMacAddress(sensor.getMacAddress());
        existingSensor.setSerialNumber(sensor.getSerialNumber());
        existingSensor.setHealthValue(sensor.getHealthValue());
        existingSensor.setIsActive(sensor.getIsActive());
        existingSensor.setLocation(sensor.getLocation());
        
        if (sensor.getDevice() != null) {
            deviceService.findById(sensor.getDevice().getId());
            existingSensor.setDevice(sensor.getDevice());
        }
        
        return sensorRepository.save(existingSensor);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.debug("Deleting sensor with id: {}", id);
        sensorRepository.deleteById(id);
    }
} 