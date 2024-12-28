package com.jvsnr.memory_monitoring_tool.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoryMetricsDTO {
    private String heapMemoryUsage;
    private String nonHeapMemoryUsage;
    private String heapMemoryMax;
    private String heapMemoryCommitted;
    private String memoryUtilisation;
}