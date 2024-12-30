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
import com.montage.device.entity.Device;
import com.montage.device.repository.DeviceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements BaseService<Device, Integer> {

    private final DeviceRepository deviceRepository;

    @Override
    @Transactional(readOnly = true)
    public Device findById(Integer id) {
        log.debug("Finding device by id: {}", id);
        return deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Device> search(SearchRequest searchRequest) {
        log.debug("Searching devices with criteria: {}", searchRequest);
        
        var specification = new GenericSpecificationBuilder<Device>(searchRequest.getFilters()).build();
        var pageable = PageRequest.of(
            searchRequest.getPage(), 
            searchRequest.getSize(), 
            Sort.by(searchRequest.getSorts().stream()
                .map(sort -> Sort.Order.by(sort.getField())
                    .with(Sort.Direction.fromString(sort.getDirection())))
                .toList())
        );
        
        return deviceRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public Device create(Device device) {
        log.debug("Creating new device: {}", device);
        return deviceRepository.save(device);
    }

    @Override
    @Transactional
    public Device update(Integer id, Device device) {
        log.debug("Updating device with id {}: {}", id, device);
        
        var existingDevice = findById(id);
        // Update fields
        existingDevice.setDeviceType(device.getDeviceType());
        existingDevice.setImei(device.getImei());
        existingDevice.setMake(device.getMake());
        existingDevice.setModel(device.getModel());
        existingDevice.setStatus(device.getStatus());
        existingDevice.setIsActive(device.getIsActive());
        // ... update other fields
        
        return deviceRepository.save(existingDevice);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.debug("Deleting device with id: {}", id);
        deviceRepository.deleteById(id);
    }
} 