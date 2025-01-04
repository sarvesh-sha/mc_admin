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
import com.montage.device.dto.request.OtaGroupRequest;
import com.montage.device.dto.response.OtaGroupResponse;
import com.montage.device.entity.Customer;
import com.montage.device.entity.OtaGroupXref;
import com.montage.device.exception.BusinessException;
import com.montage.device.mapper.GenericMapper;
import com.montage.device.repository.OtaGroupXrefRepository;
import com.montage.device.service.OtaGroupService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtaGroupXrefServiceImpl implements OtaGroupService {

    private final OtaGroupXrefRepository otaGroupRepository;
    private final CustomerServiceImpl customerService;
    private final GenericMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public OtaGroupResponse findById(Integer id) {
        log.debug("Finding OTA group by id: {}", id);
        OtaGroupXref group = otaGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OTA group not found with id: " + id));
        return mapper.convert(group, OtaGroupResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OtaGroupResponse> search(SearchRequest searchRequest) {
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
        
        return otaGroupRepository.findAll(specification, pageable)
                .map(group -> mapper.convert(group, OtaGroupResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OtaGroupResponse> findByCustomerId(Integer customerId, int page, int size) {
        log.debug("Finding OTA groups for customer id: {}", customerId);
        customerService.findById(customerId); // Verify customer exists
        return otaGroupRepository.findByCustomerId(customerId, PageRequest.of(page, size))
                .map(group -> mapper.convert(group, OtaGroupResponse.class));
    }

    @Override
    @Transactional
    public OtaGroupResponse create(OtaGroupRequest request) {
        log.debug("Creating new OTA group: {}", request);
        OtaGroupXref otaGroup = mapper.convert(request, OtaGroupXref.class);
        handleCustomer(otaGroup, request.getCustomerId());
        validateOtaGroup(otaGroup);
        OtaGroupXref savedGroup = otaGroupRepository.save(otaGroup);
        return mapper.convert(savedGroup, OtaGroupResponse.class);
    }

    @Override
    @Transactional
    public OtaGroupResponse updateGroup(Integer id, OtaGroupRequest request) {
        log.debug("Updating OTA group with id {}: {}", id, request);
        
        OtaGroupXref existingGroup = findGroupById(id);
        OtaGroupXref updatedGroup = mapper.convert(request, OtaGroupXref.class);
        updatedGroup.setId(id);
        
        handleCustomer(updatedGroup, request.getCustomerId());
        
        if (!existingGroup.getGroupName().equals(updatedGroup.getGroupName())) {
            validateGroupName(updatedGroup.getGroupName(), updatedGroup.getCustomer().getId());
        }
        
        OtaGroupXref savedGroup = otaGroupRepository.save(updatedGroup);
        return mapper.convert(savedGroup, OtaGroupResponse.class);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.debug("Deleting OTA group with id: {}", id);
        OtaGroupXref group = findGroupById(id);
        
        if (otaGroupRepository.hasAssociatedDevices(id)) {
            throw new BusinessException(
                "Cannot delete OTA group with associated devices",
                "GROUP_HAS_DEVICES",
                HttpStatus.CONFLICT
            );
        }
        
        otaGroupRepository.delete(group);
    }

    private OtaGroupXref findGroupById(Integer id) {
        return otaGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OTA group not found with id: " + id));
    }

    private void handleCustomer(OtaGroupXref group, Integer customerId) {
        if (customerId != null) {
            Customer customer = new Customer();
            customer.setId(customerId);
            group.setCustomer(customer);
        }
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