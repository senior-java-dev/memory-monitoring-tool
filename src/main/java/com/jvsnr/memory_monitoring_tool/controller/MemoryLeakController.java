package com.jvsnr.memory_monitoring_tool.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvsnr.memory_monitoring_tool.service.MemoryLeakDetectorService;

@RestController
@RequestMapping("/memory-leak")
public class MemoryLeakController {

    private final MemoryLeakDetectorService memoryLeakDetectorService;
    
    public MemoryLeakController(MemoryLeakDetectorService memoryLeakDetectorService) {
        this.memoryLeakDetectorService = memoryLeakDetectorService;
    }

    @GetMapping("/status")
    public Map<String, Object> getMemoryLeakStatus() {
        return memoryLeakDetectorService.getLeakDetectionStatus();
    }

}
