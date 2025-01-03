package com.montage.device.service;

import org.springframework.data.domain.Page;

import com.montage.common.dto.SearchRequest;
import com.montage.device.dto.request.ProvisioningStatusRequest;
import com.montage.device.dto.response.ProvisioningStatusResponse;

public interface ProvisioningStatusService {
    // Add only specific methods that are not in BaseService
    // For example, if you need any custom business operations
    
    // The following methods are already in BaseService:
     ProvisioningStatusResponse findById(Integer id);
     Page<ProvisioningStatusResponse> search(SearchRequest searchRequest);
     ProvisioningStatusResponse create(ProvisioningStatusRequest request);
     ProvisioningStatusResponse update(Integer id, ProvisioningStatusRequest request);
     void delete(Integer id);
} 