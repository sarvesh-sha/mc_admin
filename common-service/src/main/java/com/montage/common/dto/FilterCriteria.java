package com.montage.common.dto;

import lombok.Data;

@Data
public class FilterCriteria {
    private String field;
    private String operator;
    private String value;
} 