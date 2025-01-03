package com.montage.device.service.impl;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.montage.common.dto.SearchRequest;
import com.montage.common.specification.GenericSpecificationBuilder;
import com.montage.device.entity.ProvisioningStatus;
//import com.montage.device.exception.BusinessException;
import com.montage.device.repository.ProvisioningStatusRepository;
import com.montage.device.service.ProvisioningStatusService;
import com.montage.device.dto.request.ProvisioningStatusRequest;
import com.montage.device.dto.response.ProvisioningStatusResponse;
import com.montage.device.mapper.GenericMapper;
import com.montage.common.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProvisioningStatusServiceImpl implements ProvisioningStatusService {

    private final ProvisioningStatusRepository provisioningStatusRepository;
    private final GenericMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public ProvisioningStatusResponse findById(Integer id) {
        log.debug("Finding provisioning status by id: {}", id);
        ProvisioningStatus entity = provisioningStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provisioning status not found with id: " + id));
        return mapper.convert(entity, ProvisioningStatusResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvisioningStatusResponse> search(SearchRequest searchRequest) {
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
        
        Page<ProvisioningStatus> page = provisioningStatusRepository.findAll(specification, pageable);
        return mapper.convertPage(page, ProvisioningStatusResponse.class);
    }

    @Override
    @Transactional
    public ProvisioningStatusResponse create(ProvisioningStatusRequest request) {
        log.debug("Creating new provisioning status: {}", request);
        validateStatusName(request.getName());
        
        ProvisioningStatus entity = mapper.convert(request, ProvisioningStatus.class);
        ProvisioningStatus savedEntity = provisioningStatusRepository.save(entity);
        return mapper.convert(savedEntity, ProvisioningStatusResponse.class);
    }

    @Override
    @Transactional
    public ProvisioningStatusResponse update(Integer id, ProvisioningStatusRequest request) {
        log.debug("Updating provisioning status with id {}: {}", id, request);
        
        ProvisioningStatus existingStatus = provisioningStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provisioning status not found with id: " + id));
                
        if (!existingStatus.getName().equals(request.getName())) {
            validateStatusName(request.getName());
        }
        
        existingStatus.setName(request.getName());
        existingStatus.setDescription(request.getDescription());
        existingStatus.setIsActive(request.getIsActive());
        
        ProvisioningStatus updatedEntity = provisioningStatusRepository.save(existingStatus);
        return mapper.convert(existingStatus, ProvisioningStatusResponse.class);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.debug("Deleting provisioning status with id: {}", id);
        // Verify existence before delete
        findById(id);
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