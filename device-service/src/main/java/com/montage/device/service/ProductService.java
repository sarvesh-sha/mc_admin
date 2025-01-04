package com.montage.device.service;

import org.springframework.data.domain.Page;
import com.montage.common.dto.SearchRequest;
import com.montage.device.dto.request.ProductRequest;
import com.montage.device.dto.response.ProductResponse;

public interface ProductService {
    ProductResponse findById(Integer id);
    Page<ProductResponse> search(SearchRequest searchRequest);
    ProductResponse create(ProductRequest request);
   // ProductResponse update(Integer id, ProductRequest request);
    void delete(Integer id);
    ProductResponse findBySku(String sku);
	ProductResponse updateProduct(Integer id, ProductRequest request);
} 