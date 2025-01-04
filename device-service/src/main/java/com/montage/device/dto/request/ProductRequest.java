package com.montage.device.dto.request;

import lombok.Data;

@Data
public class ProductRequest {
    private String manufacturer;
    private String model;
    private String sku;
    private Boolean isActive = true;
} 