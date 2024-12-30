package com.jvsnr.memory_monitoring_tool.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GarbageCollectionMetricsDTO {

    private String collectionName;
    private long collectionCount;
    private String collectionTime;
    private String type; // Young/Old generation
    private double gcOverhead; // Percentage of time spent in GC
    private String lastGCDuration;
    private String lastGCStartTime;

}
