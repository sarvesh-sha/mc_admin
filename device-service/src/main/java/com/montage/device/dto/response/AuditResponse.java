package com.montage.device.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AuditResponse {
    private Integer id;
    private Integer severityId;
    private Integer categoryId;
    private String eventTypeName;
    private LocalDateTime updateOn;
    private String details;
    private String entityName;
    private Integer entityId;
    private String oldValue;
    private String newValue;
    private LocalDateTime createdOn;
    private String createdBy;
} 