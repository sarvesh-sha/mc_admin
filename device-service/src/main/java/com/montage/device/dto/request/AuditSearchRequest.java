package com.montage.device.dto.request;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.montage.common.dto.SearchRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuditSearchRequest extends SearchRequest {
    private Integer severityId;
    private Integer categoryId;
    private Integer eventTypeId;
    private String entityName;
    private Integer entityId;
    private String createdBy;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
} 