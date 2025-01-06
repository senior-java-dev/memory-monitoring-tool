package com.jvsnr.memory_monitoring_tool.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemoryLeakDetectorServiceTest {

    @Mock
    private MemoryMonitorService memoryMonitorService;

    private MemoryLeakDetectorService memoryLeakDetector;

    @BeforeEach
    void setUp() {
        memoryLeakDetector = new MemoryLeakDetectorService(memoryMonitorService);
    }

    @Test
    void analyseMemoryUsage_ShouldAddSnapshot() {
        // Given
        when(memoryMonitorService.getHeapMemoryUsage(false)).thenReturn("1000000");
        when(memoryMonitorService.getHeapMemoryMax(false)).thenReturn("5000000");

        // When
        memoryLeakDetector.analyseMemoryUsage();

        // Then
        Map<String, Object> status = memoryLeakDetector.getLeakDetectionStatus();
        assertNotNull(status.get("currentHeapUsage"));
        assertEquals("4,77 MB", status.get("maxHeapMemory"));
        assertEquals("20,00", status.get("usagePercentage"));
        assertFalse((boolean) status.get("memoryLeakDetected"));
    }

    @Test
    void getLeakDetectionStatus_WhenNoSnapshots_ShouldReturnEmptyStatus() {
        // When
        Map<String, Object> status = memoryLeakDetector.getLeakDetectionStatus();

        // Then
        assertNotNull(status);
        assertFalse((boolean) status.get("consistentGrowthDetected"));
        assertFalse((boolean) status.get("highGCFrequencyDetected"));
        assertFalse((boolean) status.get("poorReclamationDetected"));
        assertFalse((boolean) status.get("memoryLeakDetected"));
        assertEquals("No check performed yet.", status.get("lastCheckTime"));
    }

    @Test
    void detectMemoryLeak_WithConsistentGrowthAndHighGCFrequency() throws InterruptedException {
        // Given - Memory usage starts high and keeps growing
        when(memoryMonitorService.getHeapMemoryUsage(false))
            .thenReturn("4300000")  // 86% of max
            .thenReturn("4350000")  // 87% of max
            .thenReturn("4400000")  // 88% of max
            .thenReturn("4450000")  // 89% of max
            .thenReturn("4500000"); // 90% of max

        when(memoryMonitorService.getHeapMemoryMax(false)).thenReturn("5000000"); // 5MB max

        // When - Add snapshots with increasing memory usage and high GC frequency
        for (int i = 0; i < 5; i++) {
            memoryLeakDetector.analyseMemoryUsage();
            // Simulate high GC frequency by waiting less than GC_FREQUENCY_THRESHOLD
            Thread.sleep(100); // 100ms is less than GC_FREQUENCY_THRESHOLD (10s)
        }

        // Then
        Map<String, Object> status = memoryLeakDetector.getLeakDetectionStatus();
        assertTrue((boolean) status.get("consistentGrowthDetected"), "Memory should show consistent growth");
        assertTrue((boolean) status.get("highGCFrequencyDetected"), "GC frequency should be high");
        assertTrue((boolean) status.get("poorReclamationDetected"), "Memory reclamation should be poor");
        assertTrue((boolean) status.get("memoryLeakDetected"), "Memory leak should be detected");
        assertEquals("90,00", status.get("usagePercentage"));
    }

    @Test
    void noMemoryLeak_WithStableMemoryUsage() {
        // Given
        when(memoryMonitorService.getHeapMemoryUsage(false))
            .thenReturn("1000000")  // 1MB
            .thenReturn("1000000")  // 1MB
            .thenReturn("1000000")  // 1MB
            .thenReturn("1000000")  // 1MB
            .thenReturn("1000000"); // 1MB

        when(memoryMonitorService.getHeapMemoryMax(false)).thenReturn("5000000"); // 5MB max

        // When - Add snapshots with stable memory usage
        for (int i = 0; i < 5; i++) {
            memoryLeakDetector.analyseMemoryUsage();
        }

        // Then
        Map<String, Object> status = memoryLeakDetector.getLeakDetectionStatus();
        assertFalse((boolean) status.get("consistentGrowthDetected"));
        assertFalse((boolean) status.get("poorReclamationDetected"));
        assertFalse((boolean) status.get("memoryLeakDetected"));
    }

    @Test
    void checkMemoryReclamation_WhenMemoryUsageExceedsThreshold() {
        // Given
        when(memoryMonitorService.getHeapMemoryUsage(false))
            .thenReturn("4500000") // 90% of max (all snapshots high to ensure poor reclamation)
            .thenReturn("4500000")
            .thenReturn("4500000")
            .thenReturn("4500000")
            .thenReturn("4500000");
        when(memoryMonitorService.getHeapMemoryMax(false)).thenReturn("5000000");   // 5MB max

        // When - Add enough snapshots to trigger checks
        for (int i = 0; i < 5; i++) {
            memoryLeakDetector.analyseMemoryUsage();
        }

        // Then
        Map<String, Object> status = memoryLeakDetector.getLeakDetectionStatus();
        assertTrue((boolean) status.get("poorReclamationDetected"), 
            "Poor reclamation should be detected when memory usage is above 85% threshold");
        assertEquals("90,00", status.get("usagePercentage"));
    }
}
