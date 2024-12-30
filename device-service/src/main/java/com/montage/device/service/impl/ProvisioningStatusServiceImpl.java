package com.montage.device.service.impl;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.montage.common.dto.SearchRequest;
import com.montage.common.service.BaseService;
import com.montage.common.specification.GenericSpecificationBuilder;
import com.montage.device.entity.ProvisioningStatus;
import com.montage.device.exception.BusinessException;
import com.montage.device.repository.ProvisioningStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProvisioningStatusServiceImpl implements BaseService<ProvisioningStatus, Integer> {

    private final ProvisioningStatusRepository provisioningStatusRepository;

    @Override
    @Transactional(readOnly = true)
    public ProvisioningStatus findById(Integer id) {
        log.debug("Finding provisioning status by id: {}", id);
        return provisioningStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provisioning status not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvisioningStatus> search(SearchRequest searchRequest) {
        log.debug("Searching provisioning statuses with criteria: {}", searchRequest);
        
        var specification = new GenericSpecificationBuilder<ProvisioningStatus>(searchRequest.getFilters()).build();
        var pageable = PageRequest.of(
            searchRequest.getPage(), 
            searchRequest.getSize(), 
            Sort.by(searchRequest.getSorts().stream()
                .map(sort -> Sort.Order.by(sort.getField())
                    .with(Sort.Direction.fromString(sort.getDirection())))
                .toList())
        );
        
        return provisioningStatusRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public ProvisioningStatus create(ProvisioningStatus status) {
        log.debug("Creating new provisioning status: {}", status);
        validateStatusName(status.getName());
        return provisioningStatusRepository.save(status);
    }

    @Override
    @Transactional
    public ProvisioningStatus update(Integer id, ProvisioningStatus status) {
        log.debug("Updating provisioning status with id {}: {}", id, status);
        
        var existingStatus = findById(id);
        if (!existingStatus.getName().equals(status.getName())) {
            validateStatusName(status.getName());
        }
        
        existingStatus.setName(status.getName());
        existingStatus.setDescription(status.getDescription());
        existingStatus.setIsActive(status.getIsActive());
        
        return provisioningStatusRepository.save(existingStatus);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.debug("Deleting provisioning status with id: {}", id);
        provisioningStatusRepository.deleteById(id);
    }

    private void validateStatusName(String name) {
        if (provisioningStatusRepository.existsByNameIgnoreCase(name)) {
            throw new BusinessException(
                "Provisioning status with name '" + name + "' already exists",
                "DUPLICATE_STATUS",
                HttpStatus.CONFLICT
            );
        }
    }
} 