package com.montage.device.service;

import org.springframework.data.domain.Page;
import com.montage.common.dto.SearchRequest;
import com.montage.device.dto.request.OtaGroupRequest;
import com.montage.device.dto.response.OtaGroupResponse;

public interface OtaGroupService {
    OtaGroupResponse findById(Integer id);
    Page<OtaGroupResponse> search(SearchRequest searchRequest);
    OtaGroupResponse create(OtaGroupRequest request);
    OtaGroupResponse updateGroup(Integer id, OtaGroupRequest request);
    void delete(Integer id);
    Page<OtaGroupResponse> findByCustomerId(Integer customerId, int page, int size);
} 