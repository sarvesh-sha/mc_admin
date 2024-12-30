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
import com.montage.device.entity.OtaGroupXref;
import com.montage.device.exception.BusinessException;
import com.montage.device.repository.OtaGroupXrefRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtaGroupXrefServiceImpl implements BaseService<OtaGroupXref, Integer> {

    private final OtaGroupXrefRepository otaGroupRepository;
    private final CustomerServiceImpl customerService;

    @Override
    @Transactional(readOnly = true)
    public OtaGroupXref findById(Integer id) {
        log.debug("Finding OTA group by id: {}", id);
        return otaGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OTA group not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OtaGroupXref> search(SearchRequest searchRequest) {
        log.debug("Searching OTA groups with criteria: {}", searchRequest);
        
        var specification = new GenericSpecificationBuilder<OtaGroupXref>(searchRequest.getFilters()).build();
        var pageable = PageRequest.of(
            searchRequest.getPage(), 
            searchRequest.getSize(), 
            Sort.by(searchRequest.getSorts().stream()
                .map(sort -> Sort.Order.by(sort.getField())
                    .with(Sort.Direction.fromString(sort.getDirection())))
                .toList())
        );
        
        return otaGroupRepository.findAll(specification, pageable);
    }

    @Transactional(readOnly = true)
    public Page<OtaGroupXref> findByCustomerId(Integer customerId, int page, int size) {
        log.debug("Finding OTA groups for customer id: {}", customerId);
        customerService.findById(customerId); // Verify customer exists
        return otaGroupRepository.findByCustomerId(customerId, PageRequest.of(page, size));
    }

    @Override
    @Transactional
    public OtaGroupXref create(OtaGroupXref otaGroup) {
        log.debug("Creating new OTA group: {}", otaGroup);
        validateOtaGroup(otaGroup);
        return otaGroupRepository.save(otaGroup);
    }

    @Override
    @Transactional
    public OtaGroupXref update(Integer id, OtaGroupXref otaGroup) {
        log.debug("Updating OTA group with id {}: {}", id, otaGroup);
        
        var existingGroup = findById(id);
        
        if (!existingGroup.getGroupName().equals(otaGroup.getGroupName())) {
            validateGroupName(otaGroup.getGroupName(), otaGroup.getCustomer().getId());
        }
        
        existingGroup.setGroupName(otaGroup.getGroupName());
        existingGroup.setGroupId(otaGroup.getGroupId());
        
        if (otaGroup.getCustomer() != null) {
            customerService.findById(otaGroup.getCustomer().getId());
            existingGroup.setCustomer(otaGroup.getCustomer());
        }
        
        return otaGroupRepository.save(existingGroup);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.debug("Deleting OTA group with id: {}", id);
        var group = findById(id);
        if (otaGroupRepository.hasAssociatedDevices(id)) {
            throw new BusinessException(
                "Cannot delete OTA group with associated devices",
                "GROUP_HAS_DEVICES",
                HttpStatus.CONFLICT
            );
        }
        otaGroupRepository.delete(group);
    }

    private void validateOtaGroup(OtaGroupXref otaGroup) {
        if (otaGroup.getCustomer() == null || otaGroup.getCustomer().getId() == null) {
            throw new BusinessException(
                "Customer is required for OTA group",
                "CUSTOMER_REQUIRED",
                HttpStatus.BAD_REQUEST
            );
        }
        customerService.findById(otaGroup.getCustomer().getId());
        validateGroupName(otaGroup.getGroupName(), otaGroup.getCustomer().getId());
    }

    private void validateGroupName(String groupName, Integer customerId) {
        if (otaGroupRepository.existsByGroupNameAndCustomerId(groupName, customerId)) {
            throw new BusinessException(
                "OTA group name already exists for this customer",
                "DUPLICATE_GROUP_NAME",
                HttpStatus.CONFLICT
            );
        }
    }
} 