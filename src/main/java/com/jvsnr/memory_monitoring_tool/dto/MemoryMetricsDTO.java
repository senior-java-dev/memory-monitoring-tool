package com.jvsnr.memory_monitoring_tool.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoryMetricsDTO {
    private long heapMemoryUsage;
    private long nonHeapMemoryUsage;
    private long heapMemoryMax;
    private long heapMemoryCommitted;
    private double memoryUtilisation;
}