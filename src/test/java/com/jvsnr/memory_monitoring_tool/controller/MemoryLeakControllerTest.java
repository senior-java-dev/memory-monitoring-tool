package com.jvsnr.memory_monitoring_tool.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jvsnr.memory_monitoring_tool.service.MemoryLeakDetectorService;

@WebMvcTest(MemoryLeakController.class)
class MemoryLeakControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemoryLeakDetectorService memoryLeakDetectorService;

    @Test
    void getMemoryLeakStatus_ShouldCallServiceAndReturnLeakStatus() throws Exception {
        // Given
        Map<String, Object> leakStatus = new HashMap<>();
        leakStatus.put("consistentGrowthDetected", true);
        leakStatus.put("highGCFrequencyDetected", false);
        leakStatus.put("poorReclamationDetected", true);
        leakStatus.put("memoryLeakDetected", true);
        leakStatus.put("lastCheckTime", "2025-01-06T17:50:15");
        leakStatus.put("currentHeapUsage", "100 MB");
        leakStatus.put("maxHeapMemory", "200 MB");
        leakStatus.put("usagePercentage", "50.00");

        when(memoryLeakDetectorService.getLeakDetectionStatus()).thenReturn(leakStatus);

        // When/Then
        mockMvc.perform(get("/memory-leak/status"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.consistentGrowthDetected").value(true))
            .andExpect(jsonPath("$.highGCFrequencyDetected").value(false))
            .andExpect(jsonPath("$.poorReclamationDetected").value(true))
            .andExpect(jsonPath("$.memoryLeakDetected").value(true))
            .andExpect(jsonPath("$.lastCheckTime").value("2025-01-06T17:50:15"))
            .andExpect(jsonPath("$.currentHeapUsage").value("100 MB"))
            .andExpect(jsonPath("$.maxHeapMemory").value("200 MB"))
            .andExpect(jsonPath("$.usagePercentage").value("50.00"));

        // Verify service method was called
        verify(memoryLeakDetectorService).getLeakDetectionStatus();
    }

    @Test
    void getMemoryLeakStatus_WhenNoLeakDetected_ShouldReturnNormalStatus() throws Exception {
        // Given
        Map<String, Object> leakStatus = new HashMap<>();
        leakStatus.put("consistentGrowthDetected", false);
        leakStatus.put("highGCFrequencyDetected", false);
        leakStatus.put("poorReclamationDetected", false);
        leakStatus.put("memoryLeakDetected", false);
        leakStatus.put("lastCheckTime", "No check performed yet.");
        leakStatus.put("currentHeapUsage", "50 MB");
        leakStatus.put("maxHeapMemory", "200 MB");
        leakStatus.put("usagePercentage", "25.00");

        when(memoryLeakDetectorService.getLeakDetectionStatus()).thenReturn(leakStatus);

        // When/Then
        mockMvc.perform(get("/memory-leak/status"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.consistentGrowthDetected").value(false))
            .andExpect(jsonPath("$.highGCFrequencyDetected").value(false))
            .andExpect(jsonPath("$.poorReclamationDetected").value(false))
            .andExpect(jsonPath("$.memoryLeakDetected").value(false))
            .andExpect(jsonPath("$.lastCheckTime").value("No check performed yet."))
            .andExpect(jsonPath("$.currentHeapUsage").value("50 MB"))
            .andExpect(jsonPath("$.maxHeapMemory").value("200 MB"))
            .andExpect(jsonPath("$.usagePercentage").value("25.00"));

        // Verify service method was called
        verify(memoryLeakDetectorService).getLeakDetectionStatus();
    }

    @Test
    void getMemoryLeakStatus_WhenServiceReturnsEmptyMap_ShouldReturnEmptyResponse() throws Exception {
        // Given
        when(memoryLeakDetectorService.getLeakDetectionStatus()).thenReturn(new HashMap<>());

        // When/Then
        mockMvc.perform(get("/memory-leak/status"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());

        // Verify service method was called
        verify(memoryLeakDetectorService).getLeakDetectionStatus();
    }

    @Test
    void getMemoryLeakStatus_WhenServiceReturnsNull_ShouldReturn500() throws Exception {
        // Given
        when(memoryLeakDetectorService.getLeakDetectionStatus()).thenReturn(null);

        // When/Then
        mockMvc.perform(get("/memory-leak/status"))
            .andExpect(status().isInternalServerError());

        // Verify service method was called
        verify(memoryLeakDetectorService).getLeakDetectionStatus();
    }
}
