package com.jvsnr.memory_monitoring_tool.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> getMemoryLeakStatus() {
        Map<String, Object> status = memoryLeakDetectorService.getLeakDetectionStatus();
        if (status == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(status);
    }

}
