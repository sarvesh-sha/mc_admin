package com.montage.device.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.montage.common.dto.SearchRequest;
import com.montage.common.service.BaseService;
import com.montage.common.specification.GenericSpecificationBuilder;
import com.montage.device.dto.DeviceUploadResult;
import com.montage.device.entity.Device;
import com.montage.device.repository.DeviceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements BaseService<Device, Integer> {

    private final DeviceRepository deviceRepository;
    private final Validator validator;

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

    @Transactional
    public DeviceUploadResult bulkUpload(List<Device> devices) {
        List<Device> successfulDevices = new ArrayList<>();
        Map<String, String> failedDevices = new HashMap<>();

        devices.forEach(device -> {
            try {
                // Custom validation
                String validationError = validateDevice(device);
                if (validationError != null) {
                    log.error("Validation failed for device: {}. Errors: {}", device, validationError);
                    failedDevices.put(device.getDeviceType(), validationError);
                    return;
                }

                // Business validations
                if (device.getImei() != null && deviceRepository.existsByImei(device.getImei())) {
                    failedDevices.put(device.getDeviceType(), " Device with IMEI " + device.getImei() + " already exists");
                    return;
                }

                // Process valid device
                Device savedDevice = deviceRepository.save(device);
                successfulDevices.add(savedDevice);
                log.info("Successfully processed device with IMEI: {}", device.getImei());

            } catch (Exception e) {
                log.error("Error processing device: {}", device, e);
                failedDevices.put(device.getDeviceType(), "Internal processing error: " + e.getMessage());
            }
        });

        return DeviceUploadResult.builder()
            .successfulDevices(successfulDevices)
            .failedDevices(failedDevices)
            .build();
    }

    private String validateDevice(Device device) {
        List<String> errors = new ArrayList<>();

        // Required field validations
        if (isEmpty(device.getDeviceType())) {
            errors.add("Device type is required");
        }

        if (isEmpty(device.getImei())) {
            errors.add("IMEI is required");
        } else if (!device.getImei().matches("^[0-9]{15}$")) {
            errors.add("IMEI must be 15 digits");
        }

//        if (isEmpty(device.getMake())) {
//            errors.add("Make is required");
//        }
//
//        if (isEmpty(device.getModel())) {
//            errors.add("Model is required");
//        }
//
//        if (isEmpty(device.getStatus())) {
//            errors.add("Status is required");
//        }
//
//        if (device.getIsActive() == null) {
//            errors.add("isActive flag is required");
//        }
//
//        if (device.getInstallationDate() != null && 
//            device.getInstallationDate().isBefore(LocalDateTime.now())) {
//            errors.add("Installation date must be in the future");
//        }

//        // Version validations
//        if (device.getConfigVersion() == null || device.getConfigVersion() < 0) {
//            errors.add("Config version must be a non-negative number");
//        }
//
//        if (device.getFirmwareVersion() == null || device.getFirmwareVersion() < 0) {
//            errors.add("Firmware version must be a non-negative number");
//        }
//
//        if (device.getBootloaderVersion() == null || device.getBootloaderVersion() < 0) {
//            errors.add("Bootloader version must be a non-negative number");
//        }
//
//        // Health and version validations
//        if (device.getHealthValue() == null || device.getHealthValue() < 0) {
//            errors.add("Health value must be a non-negative number");
//        }
//
//        if (device.getHardwareVersion() == null || device.getHardwareVersion() < 0) {
//            errors.add("Hardware version must be a non-negative number");
//        }
//
//        if (device.getProtocolVersion() == null || device.getProtocolVersion() < 0) {
//            errors.add("Protocol version must be a non-negative number");
//        }
//
//        // Asset and reference validations
//        if (isEmpty(device.getAssetName())) {
//            errors.add("Asset name is required");
//        }
//
//        if (device.getSerialNumber() == null) {
//            errors.add("Serial number is required");
//        }
//
//        // Related entity validations
//        if (device.getCustomer() == null || device.getCustomer().getId() == null) {
//            errors.add("Customer is required");
//        }
//
//        if (device.getOtaGroup() == null || device.getOtaGroup().getId() == null) {
//            errors.add("OTA group is required");
//        }
//
//        if (device.getProvisioningStatus() == null || device.getProvisioningStatus().getId() == null) {
//            errors.add("Provisioning status is required");
//        }
//
//        if (device.getProduct() == null || device.getProduct().getId() == null) {
//            errors.add("Product is required");
//        }
//
//        if (device.getICCID() == null) {
//            errors.add("ICCID is required");
//        }

        return errors.isEmpty() ? null : String.join("; ", errors);
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
} 