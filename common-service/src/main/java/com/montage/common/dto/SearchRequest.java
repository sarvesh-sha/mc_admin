package com.montage.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchRequest {
    private int page = 0;
    private int size = 10;
    private List<FilterCriteria> filters = new ArrayList<>();
    private List<SortCriteria> sorts = new ArrayList<>();
} 