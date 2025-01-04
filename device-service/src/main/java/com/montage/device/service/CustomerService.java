package com.montage.device.service;

import org.springframework.data.domain.Page;
import com.montage.common.dto.SearchRequest;
import com.montage.device.dto.request.CustomerRequest;
import com.montage.device.dto.response.CustomerResponse;

public interface CustomerService {
    CustomerResponse findById(Integer id);
    Page<CustomerResponse> search(SearchRequest searchRequest);
    CustomerResponse create(CustomerRequest request);
    CustomerResponse updateCustomer(Integer id, CustomerRequest request);
    void delete(Integer id);
} 