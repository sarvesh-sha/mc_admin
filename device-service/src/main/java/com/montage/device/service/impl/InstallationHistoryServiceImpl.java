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
import com.montage.device.entity.InstallationHistory;
import com.montage.device.repository.InstallationHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstallationHistoryServiceImpl implements BaseService<InstallationHistory, Integer> {

    private final InstallationHistoryRepository installationHistoryRepository;
    private final DeviceServiceImpl deviceService;
    private final CustomerServiceImpl customerService;

    @Override
    @Transactional(readOnly = true)
    public InstallationHistory findById(Integer id) {
        log.debug("Finding installation history by id: {}", id);
        return installationHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Installation history not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstallationHistory> search(SearchRequest searchRequest) {
        log.debug("Searching installation history with criteria: {}", searchRequest);
        
        var specification = new GenericSpecificationBuilder<InstallationHistory>(searchRequest.getFilters()).build();
        var pageable = PageRequest.of(
            searchRequest.getPage(), 
            searchRequest.getSize(), 
            Sort.by(searchRequest.getSorts().stream()
                .map(sort -> Sort.Order.by(sort.getField())
                    .with(Sort.Direction.fromString(sort.getDirection())))
                .toList())
        );
        
        return installationHistoryRepository.findAll(specification, pageable);
    }

    @Transactional(readOnly = true)
    public Page<InstallationHistory> findByDeviceId(Integer deviceId, int page, int size) {
        log.debug("Finding installation history for device id: {}", deviceId);
        deviceService.findById(deviceId); // Verify device exists
        return installationHistoryRepository.findByDeviceId(deviceId, PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Page<InstallationHistory> findByCustomerId(Integer customerId, int page, int size) {
        log.debug("Finding installation history for customer id: {}", customerId);
        customerService.findById(customerId); // Verify customer exists
        return installationHistoryRepository.findByCustomerId(customerId, PageRequest.of(page, size));
    }

    @Override
    @Transactional
    public InstallationHistory create(InstallationHistory history) {
        log.debug("Creating new installation history: {}", history);
        validateInstallationHistory(history);
        return installationHistoryRepository.save(history);
    }

    @Override
    @Transactional
    public InstallationHistory update(Integer id, InstallationHistory history) {
        throw new UnsupportedOperationException("Installation history records cannot be updated");
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        throw new UnsupportedOperationException("Installation history records cannot be deleted");
    }

    private void validateInstallationHistory(InstallationHistory history) {
        if (history.getDevice() != null) {
            deviceService.findById(history.getDevice().getId());
        }
        if (history.getCustomer() != null) {
            customerService.findById(history.getCustomer().getId());
        }
    }
} 