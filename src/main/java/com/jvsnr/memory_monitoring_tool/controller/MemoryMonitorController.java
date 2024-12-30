package com.jvsnr.memory_monitoring_tool.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvsnr.memory_monitoring_tool.dto.MemoryMetricsDTO;
import com.jvsnr.memory_monitoring_tool.service.MemoryMonitorService;

@RestController
@RequestMapping("/memory-monitor")
public class MemoryMonitorController {
    
    MemoryMonitorService memoryMonitorService;

    public MemoryMonitorController(MemoryMonitorService memoryMonitorService) {
        this.memoryMonitorService = memoryMonitorService;
    }

    @GetMapping("/heap-memory")
    public MemoryMetricsDTO getHeapMemoryMetrics() {
        MemoryMetricsDTO memoryMetricsDTO = new MemoryMetricsDTO();
        memoryMetricsDTO.setHeapMemoryUsage(memoryMonitorService.getHeapMemoryUsage(true));
        return memoryMetricsDTO;
    } 

    @GetMapping("/non-heap-memory")
    public MemoryMetricsDTO getNonHeapMemoryMetrics() {
        MemoryMetricsDTO memoryMetricsDTO = new MemoryMetricsDTO();
        memoryMetricsDTO.setNonHeapMemoryUsage(memoryMonitorService.getNonHeapMemoryUsage(true));
        return memoryMetricsDTO;
    }

    @GetMapping("/heap-memory-max")
    public MemoryMetricsDTO getHeapMemoryMaxMetrics() {
        MemoryMetricsDTO memoryMetricsDTO = new MemoryMetricsDTO();
        memoryMetricsDTO.setHeapMemoryMax(memoryMonitorService.getHeapMemoryMax (true));
        return memoryMetricsDTO;
    }

    @GetMapping("/heap-memory-committed")
    public MemoryMetricsDTO getHeapMemoryCommittedMetrics() {
        MemoryMetricsDTO memoryMetricsDTO = new MemoryMetricsDTO();
        memoryMetricsDTO.setHeapMemoryCommitted(memoryMonitorService.getHeapMemoryCommitted(true));
        return memoryMetricsDTO;
    }

    @GetMapping("/memory-utilisation")
    public MemoryMetricsDTO getMemoryUtilisationMetrics() {
        MemoryMetricsDTO memoryMetricsDTO = new MemoryMetricsDTO();
        memoryMetricsDTO.setMemoryUtilisation(memoryMonitorService.getMemoryUtilisation());
        return memoryMetricsDTO;
    }

    @GetMapping("/all")
    public MemoryMetricsDTO getAllMetrics() {
        MemoryMetricsDTO memoryMetricsDTO = new MemoryMetricsDTO();
        memoryMetricsDTO.setHeapMemoryUsage(memoryMonitorService.getHeapMemoryUsage(true));
        memoryMetricsDTO.setNonHeapMemoryUsage(memoryMonitorService.getNonHeapMemoryUsage(true));
        memoryMetricsDTO.setHeapMemoryMax(memoryMonitorService.getHeapMemoryMax(true));
        memoryMetricsDTO.setHeapMemoryCommitted(memoryMonitorService.getHeapMemoryCommitted(true));
        memoryMetricsDTO.setMemoryUtilisation(memoryMonitorService.getMemoryUtilisation());
        return memoryMetricsDTO;
    }
   
}
