package com.montage.device.service;

import org.springframework.data.domain.Page;
import com.montage.device.dto.request.AuditSearchRequest;
import com.montage.device.dto.response.AuditResponse;

public interface AuditService {
    Page<AuditResponse> searchAudits(AuditSearchRequest request);
    void logAuditEvent(String entityName, Integer entityId, String action, String oldValue, String newValue);
    Page<AuditResponse> getAuditsByEntity(String entityName, Integer entityId, int page, int size);
} 