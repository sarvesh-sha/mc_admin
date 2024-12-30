package com.montage.common.service;

import com.montage.common.dto.SearchRequest;
import org.springframework.data.domain.Page;

public interface BaseService<T, ID> {
    T findById(ID id);
    Page<T> search(SearchRequest searchRequest);
    T create(T entity);
    T update(ID id, T entity);
    void delete(ID id);
} 