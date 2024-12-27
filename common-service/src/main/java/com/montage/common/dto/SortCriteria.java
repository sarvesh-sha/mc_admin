package com.montage.common.dto;

import lombok.Data;

@Data
public class SortCriteria {
    private String field;
    private String direction; // asc, desc
} 