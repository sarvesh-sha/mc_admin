package com.montage.common.dto;

import lombok.Data;

@Data
public class FilterCriteria {
    private String field;
    private String operator;  // eq, ne, gt, lt, ge, le, like, in
    private Object value;
} 