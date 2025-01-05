package com.jvsnr.memory_monitoring_tool.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jvsnr.memory_monitoring_tool.dto.GarbageCollectionMetricsDTO;
import com.jvsnr.memory_monitoring_tool.service.GarbageCollectionMonitorService;

@RestController
@RequestMapping("/gc")
public class GarbageCollectorMonitorController {

    private final GarbageCollectionMonitorService garbageCollectionMonitorService;

    public GarbageCollectorMonitorController(GarbageCollectionMonitorService garbageCollectionMonitorService) {
        this.garbageCollectionMonitorService = garbageCollectionMonitorService;
    }

    @GetMapping("/last-gc-info")
    public GarbageCollectionMetricsDTO getLastGCInfo() {
        return garbageCollectionMonitorService.getLastGCInfo();
    }

    @GetMapping("/coll-count")
    public long getGCCollectionCount() {
        return garbageCollectionMonitorService.getGCCollectionCount();
    }

    @GetMapping("/coll-time")
    public String getGCCollectionTime() {
        return garbageCollectionMonitorService.getGCCollectionTime();
    }

    @GetMapping("/coll-type")
    public String getGCCollectionType(@RequestParam(defaultValue = "Unknown") String collectionName ) {
        return garbageCollectionMonitorService.determineGCType(collectionName);
    }

    @GetMapping("/metrics-by-coll-name")
    public Map<String, GarbageCollectionMetricsDTO> getGCMetricsByCollectionName() {
        return garbageCollectionMonitorService.getGCMetricsByCollectionName();
    }

}
