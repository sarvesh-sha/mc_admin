package com.montage.common.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class BulkUploadResponse<T> {
    private int successCount;
    private int failureCount;
    private List<T> successfulRecords;
    private Map<String, String> failedRecords;
} 