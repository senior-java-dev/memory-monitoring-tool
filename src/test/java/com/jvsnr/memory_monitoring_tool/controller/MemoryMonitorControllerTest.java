package com.jvsnr.memory_monitoring_tool.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jvsnr.memory_monitoring_tool.service.MemoryMonitorService;

@WebMvcTest(MemoryMonitorController.class)
class MemoryMonitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemoryMonitorService memoryMonitorService;

    @Test
    void getHeapMemoryMetrics_ShouldReturnHeapMemoryUsage() throws Exception {
        // Given
        when(memoryMonitorService.getHeapMemoryUsage(true)).thenReturn("100 MB");

        // When/Then
        mockMvc.perform(get("/memory-monitor/heap-memory"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.heapMemoryUsage").value("100 MB"));
    }

    @Test
    void getNonHeapMemoryMetrics_ShouldReturnNonHeapMemoryUsage() throws Exception {
        // Given
        when(memoryMonitorService.getNonHeapMemoryUsage(true)).thenReturn("50 MB");

        // When/Then
        mockMvc.perform(get("/memory-monitor/non-heap-memory"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nonHeapMemoryUsage").value("50 MB"));
    }

    @Test
    void getHeapMemoryMaxMetrics_ShouldReturnHeapMemoryMax() throws Exception {
        // Given
        when(memoryMonitorService.getHeapMemoryMax(true)).thenReturn("200 MB");

        // When/Then
        mockMvc.perform(get("/memory-monitor/heap-memory-max"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.heapMemoryMax").value("200 MB"));
    }

    @Test
    void getHeapMemoryCommittedMetrics_ShouldReturnHeapMemoryCommitted() throws Exception {
        // Given
        when(memoryMonitorService.getHeapMemoryCommitted(true)).thenReturn("150 MB");

        // When/Then
        mockMvc.perform(get("/memory-monitor/heap-memory-committed"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.heapMemoryCommitted").value("150 MB"));
    }

    @Test
    void getMemoryUtilisationMetrics_ShouldReturnMemoryUtilisation() throws Exception {
        // Given
        when(memoryMonitorService.getMemoryUtilisation()).thenReturn("75%");

        // When/Then
        mockMvc.perform(get("/memory-monitor/memory-utilisation"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.memoryUtilisation").value("75%"));
    }

    @Test
    void getAllMetrics_ShouldReturnAllMemoryMetrics() throws Exception {
        // Given
        when(memoryMonitorService.getHeapMemoryUsage(true)).thenReturn("100 MB");
        when(memoryMonitorService.getNonHeapMemoryUsage(true)).thenReturn("50 MB");
        when(memoryMonitorService.getHeapMemoryMax(true)).thenReturn("200 MB");
        when(memoryMonitorService.getHeapMemoryCommitted(true)).thenReturn("150 MB");
        when(memoryMonitorService.getMemoryUtilisation()).thenReturn("50%");

        // When/Then
        mockMvc.perform(get("/memory-monitor/all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.heapMemoryUsage").value("100 MB"))
            .andExpect(jsonPath("$.nonHeapMemoryUsage").value("50 MB"))
            .andExpect(jsonPath("$.heapMemoryMax").value("200 MB"))
            .andExpect(jsonPath("$.heapMemoryCommitted").value("150 MB"))
            .andExpect(jsonPath("$.memoryUtilisation").value("50%"));
    }
}
