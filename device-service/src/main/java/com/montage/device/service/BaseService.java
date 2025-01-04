package com.montage.device.service;

import org.springframework.data.domain.Page;
import com.montage.common.dto.SearchRequest;

public interface BaseService<T, ID, REQ, RES> {
    RES findById(ID id);
    Page<RES> search(SearchRequest searchRequest);
    RES create(REQ request);
    RES update(ID id, REQ request);
    void delete(ID id);
} 