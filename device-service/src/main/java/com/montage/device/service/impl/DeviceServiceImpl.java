package com.montage.device.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.montage.common.dto.BulkUploadResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.common.specification.GenericSpecificationBuilder;
import com.montage.device.dto.request.DeviceProvisioningRequest;
import com.montage.device.dto.request.DeviceRequest;
import com.montage.device.dto.response.DeviceResponse;
import com.montage.device.entity.Customer;
import com.montage.device.entity.Device;
import com.montage.device.entity.OtaGroupXref;
import com.montage.device.entity.Product;
import com.montage.device.entity.ProvisioningStatus;
import com.montage.device.mapper.GenericMapper;
import com.montage.device.repository.DeviceRepository;
import com.montage.device.repository.ProvisioningStatusRepository;
import com.montage.device.service.DeviceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final GenericMapper mapper;

    private final ProvisioningStatusRepository provisioningStatusRepository;
    
    public DeviceServiceImpl(DeviceRepository deviceRepository, GenericMapper mapper, ProvisioningStatusRepository provisioningStatusRepository) {
        this.deviceRepository = deviceRepository;
        this.mapper = mapper;
        this.provisioningStatusRepository = provisioningStatusRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceResponse findById(Integer id) {
        log.debug("Finding device by id: {}", id);
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + id));
        return mapper.convert(device, DeviceResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceResponse> search(SearchRequest searchRequest) {
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
        
        return deviceRepository.findAll(specification, pageable)
                .map(device -> mapper.convert(device, DeviceResponse.class));
    }

    private void handleNestedObjects(Device device, DeviceRequest request) {
        
    	request.setIsActive(true);
    	 // Handle Customer relationship
        if (request.getCustomerId() != null) {
            if (device.getCustomer() == null) {
                device.setCustomer(new Customer());
            }
            device.getCustomer().setId(request.getCustomerId());
        }
    	// Handle OTA Group
        if (request.getOtaGroupId() != null) {
            if (device.getOtaGroup() == null) {
                device.setOtaGroup(new OtaGroupXref());
            }
            device.getOtaGroup().setId(request.getOtaGroupId());
        }
        
        // Handle Provisioning Status
        if (request.getProvisioningStatusId() != null) {
            if (device.getProvisioningStatus() == null) {
                device.setProvisioningStatus(new ProvisioningStatus());
            }
            device.getProvisioningStatus().setId(request.getProvisioningStatusId());
        }
        
        // Handle Product
        if (request.getProductId() != null) {
            if (device.getProduct() == null) {
                device.setProduct(new Product());
            }
            device.getProduct().setId(request.getProductId());
        }
    }

    @Override
    @Transactional
    public DeviceResponse create(DeviceRequest request) {
        log.debug("Creating new device: {}", request);
        Device device = mapper.convert(request, Device.class);
       
        handleNestedObjects(device, request);
        
        String validationError = validateDevice(device);
        if (validationError != null) {
            throw new IllegalArgumentException(validationError);
        }
        
        Device savedDevice = deviceRepository.save(device);
        return mapper.convert(savedDevice, DeviceResponse.class);
    }

    @Override
    @Transactional
    public DeviceResponse updateDevice(Integer id, DeviceRequest request) {
        log.debug("Updating device with id {}: {}", id, request);
        
        Device existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + id));
                
        // Convert request to entity and handle nested objects
        Device updatedDevice = mapper.convert(request, Device.class);
        updatedDevice.setId(id); // Preserve the ID
        handleNestedObjects(updatedDevice, request);
        
        String validationError = validateDeviceForUpdate(updatedDevice);
        if (validationError != null) {
            throw new IllegalArgumentException(validationError);
        }
        
        Device savedDevice = deviceRepository.save(updatedDevice);
        return mapper.convert(savedDevice, DeviceResponse.class);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.debug("Deleting device with id: {}", id);
        findById(id);
        deviceRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BulkUploadResponse<String> bulkUpload(List<DeviceRequest> requests) {
        List<Device> successfulDevices = new ArrayList<>();
        Map<String, String> failedRecords = new HashMap<>();
        
        requests.forEach(request -> {
            try {
                Device device = mapper.convert(request, Device.class);
                handleNestedObjects(device, request);
                
                String validationError = validateDevice(device);
                if (validationError != null) {
                    failedRecords.put(request.getImei(), validationError);
                    return;
                }

                Device savedDevice = deviceRepository.save(device);
                successfulDevices.add(savedDevice);
                log.info("Successfully processed device with IMEI: {}", device.getImei());

            } catch (Exception e) {
                log.error("Error processing device: {}", request, e);
                failedRecords.put(request.getImei() != null ? request.getImei() : "Unknown", 
                    "Internal processing error: " + e.getMessage());
            }
        });

        return BulkUploadResponse.<String>builder()
            .successCount(successfulDevices.size())
            .failureCount(failedRecords.size())
            .successfulRecords(successfulDevices.stream()
                .map(Device::getImei)
                .toList())
            .failedRecords(failedRecords)
            .build();
    }

    private String validateDevice(Device device) {
        // Check for duplicate IMEI
        if (deviceRepository.existsByImei(device.getImei())) {
            return "Device with IMEI " + device.getImei() + " already exists";
        }

        // Check for duplicate serial number
//        if (deviceRepository.existsBySerialNumber(device.getSerialNumber())) {
//            return "Device with serial number " + device.getSerialNumber() + " already exists";
//        }

        return null; // validation passed
    }
    
	private String validateDeviceForUpdate(Device device) {
		// Check for duplicate IMEI excluding current device
		if (deviceRepository.existsByImeiAndIdNot(device.getImei(), device.getId())) {
			return "Device with IMEI " + device.getImei() + " already exists";
		}
		return null; // validation passed
	}

	@Override
	public DeviceResponse update(Integer id, DeviceRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@Transactional
	public DeviceResponse updateProvisioningStatus(Integer deviceId, DeviceProvisioningRequest request) {
		log.debug("Updating provisioning status for device id: {} to status id: {}", 
			deviceId, request.getProvisioningStatusId());
		
		Device device = deviceRepository.findById(deviceId)
			.orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + deviceId));
		
		ProvisioningStatus status = provisioningStatusRepository.findById(request.getProvisioningStatusId())
			.orElseThrow(() -> new ResourceNotFoundException("Provisioning status not found with id: " + 
				request.getProvisioningStatusId()));
		
		// Update status
		device.setProvisioningStatus(status);
		Device savedDevice = deviceRepository.save(device);
		
		return mapper.convert(savedDevice, DeviceResponse.class);
	}
} 