package com.montage.device.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class OtaGroupResponse {
    private Integer id;
    private String groupName;
    private String groupId;
    private Integer customerId;
    private String customerName;
    private Boolean isActive;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
} 