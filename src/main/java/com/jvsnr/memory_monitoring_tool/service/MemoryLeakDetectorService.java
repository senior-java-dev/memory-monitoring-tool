package com.jvsnr.memory_monitoring_tool.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jvsnr.util.MemoryUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MemoryLeakDetectorService {

    private static final int MEMORY_GROWTH_THRESHOLD = 85; // in percentage
    private static final int GC_FREQUENCY_THRESHOLD  = 10; // in seconds
    private static final int SAMPLE_SIZE = 5; // number of measurements to consider

    private boolean consistentGrowthDetected;
    private boolean highGCFrequencyDetected;
    private boolean poorReclamationDetected;
    private LocalDateTime lastCheckTime;

    private final Queue<MemorySnapshot> memorySnapshots;
    private long lastGCTime;

    private MemoryMonitorService memoryMonitorService;

    public MemoryLeakDetectorService(MemoryMonitorService memoryMonitorService) {
        this.memoryMonitorService = memoryMonitorService;
        this.memorySnapshots = new LinkedList<>();
        this.lastGCTime = System.currentTimeMillis();
    }

    /**
     * Analyzes the memory usage and detects memory leaks.
     */
    @Scheduled(fixedRate = 10000) // Run every 10 seconds
    public void analyseMemoryUsage() {
        String currentHeapUsage = memoryMonitorService.getHeapMemoryUsage(false);
        memorySnapshots.add(new MemorySnapshot(currentHeapUsage, System.currentTimeMillis()));

        if (memorySnapshots.size() > SAMPLE_SIZE) {
            memorySnapshots.poll();
        }

        checkForMemoryLeak();
    }

    /**
     * Checks if a memory leak has been detected.
     */
    private void checkForMemoryLeak() {
        if (memorySnapshots.size() < SAMPLE_SIZE) {
            return;
        }

        // Check for consistent memory growth
        consistentGrowthDetected = checkConsistentGrowth();

        // Check GC frequency
        highGCFrequencyDetected = checkGCFrequency();

        // Check memory reclamation
        poorReclamationDetected = checkMemoryReclamation();

        if (consistentGrowthDetected && (highGCFrequencyDetected || poorReclamationDetected)) {
            log.warn("Potential memory leak detected!");
            log.warn("Consistent memory growth: {}", consistentGrowthDetected);
            log.warn("High frequency GC: {}", highGCFrequencyDetected);
            log.warn("Poor memory reclamation: {}", poorReclamationDetected);
        }
    }

    /**
     * Returns the current status of the memory leak detector.
     * 
     * @return a map containing the status of the memory leak detector
     */
    public Map<String, Object> getLeakDetectionStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("consistentGrowthDetected", consistentGrowthDetected);
        status.put("highGCFrequencyDetected", highGCFrequencyDetected);
        status.put("poorReclamationDetected", poorReclamationDetected);
        status.put("lastCheckTime", lastCheckTime != null ? lastCheckTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "No check performed yet.");
        status.put("memoryLeakDetected", consistentGrowthDetected && (highGCFrequencyDetected || poorReclamationDetected));

        // Add current memory metrics
        if (!memorySnapshots.isEmpty()) {
            MemorySnapshot latest = new ArrayList<>(memorySnapshots).get(memorySnapshots.size() - 1);
            status.put("currentHeapUsage", MemoryUtils.formatBytes(Long.parseLong(latest.getMemoryUsage())));
            status.put("maxHeapMemory", MemoryUtils.formatBytes(Long.parseLong(memoryMonitorService.getHeapMemoryMax(false))));
            status.put("usagePercentage", String.format("%.2f", ((Long.parseLong(latest.getMemoryUsage()) * 100.0) / Long.parseLong(memoryMonitorService.getHeapMemoryMax(false)))));
        }

        return status;
    }

    /**
     * Checks if the memory usage is consistently growing.
     * 
     * @return true if the memory usage is consistently growing, false otherwise
     */
    private boolean checkConsistentGrowth() {
        List<MemorySnapshot> snapshots = new ArrayList<>(memorySnapshots);
        for (int i = 1; i < snapshots.size(); i++) {
            if (Long.parseLong(snapshots.get(i).getMemoryUsage()) <= Long.parseLong(snapshots.get(i-1).getMemoryUsage())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the GC frequency is high.
     * 
     * @return true if the GC frequency is high, false otherwise
     */
    private boolean checkGCFrequency() {
        long currentGCTime = System.currentTimeMillis();
        boolean highFrequency = (currentGCTime - lastGCTime) < (GC_FREQUENCY_THRESHOLD * 1000);
        lastGCTime = currentGCTime;
        return highFrequency;
    }

    /**
     * Checks if memory reclamation is occurring.
     * 
     * @return true if memory reclamation is occurring, false otherwise
     */
    private boolean checkMemoryReclamation() {
        if (memorySnapshots.isEmpty()) return false;

        long currentHeapUsage = Long.parseLong(memorySnapshots.peek().getMemoryUsage());
        long maxHeapMemory = Long.parseLong(memoryMonitorService.getHeapMemoryMax(false));

        return ((currentHeapUsage * 100) / maxHeapMemory) > MEMORY_GROWTH_THRESHOLD;
    }
 
}

/**
 * A class to represent a memory snapshot.
 */
@Data
@AllArgsConstructor
class MemorySnapshot {
    String memoryUsage;
    long timestamp;
}