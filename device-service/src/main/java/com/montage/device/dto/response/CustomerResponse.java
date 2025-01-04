package com.montage.device.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CustomerResponse {
    private Integer id;
    private String customerName;
    private String shortName;
    private String accountNumber;
    private Boolean isActive;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
} 