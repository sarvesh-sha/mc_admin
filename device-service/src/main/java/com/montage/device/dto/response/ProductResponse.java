package com.montage.device.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ProductResponse {
    private Integer id;
    private String manufacturer;
    private String model;
    private String sku;
    private Boolean isActive;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
} 