package com.jvsnr.memory_monitoring_tool.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jvsnr.memory_monitoring_tool.dto.GarbageCollectionMetricsDTO;
import com.jvsnr.memory_monitoring_tool.service.GarbageCollectionMonitorService;

@WebMvcTest(GarbageCollectorMonitorController.class)
class GarbageCollectorMonitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GarbageCollectionMonitorService garbageCollectionMonitorService;

    @Test
    void getLastGCInfo_ShouldReturnGCMetrics() throws Exception {
        // Given
        GarbageCollectionMetricsDTO metrics = new GarbageCollectionMetricsDTO();
        metrics.setCollectionName("PS Scavenge");
        metrics.setCollectionCount(10L);
        metrics.setCollectionTime("100ms");
        when(garbageCollectionMonitorService.getLastGCInfo()).thenReturn(metrics);

        // When/Then
        mockMvc.perform(get("/gc/last-gc-info"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.collectionName").value("PS Scavenge"))
            .andExpect(jsonPath("$.collectionCount").value(10))
            .andExpect(jsonPath("$.collectionTime").value("100ms"));
    }

    @Test
    void getGCCollectionCount_ShouldReturnCount() throws Exception {
        // Given
        when(garbageCollectionMonitorService.getGCCollectionCount()).thenReturn(42L);

        // When/Then
        mockMvc.perform(get("/gc/coll-count"))
            .andExpect(status().isOk())
            .andExpect(content().string("42"));
    }

    @Test
    void getGCCollectionTime_ShouldReturnFormattedTime() throws Exception {
        // Given
        when(garbageCollectionMonitorService.getGCCollectionTime()).thenReturn("500ms");

        // When/Then
        mockMvc.perform(get("/gc/coll-time"))
            .andExpect(status().isOk())
            .andExpect(content().string("500ms"));
    }

    @Test
    void getGCCollectionType_WithDefaultParam_ShouldReturnType() throws Exception {
        // Given
        when(garbageCollectionMonitorService.determineGCType("Unknown")).thenReturn("Minor GC");

        // When/Then
        mockMvc.perform(get("/gc/coll-type"))
            .andExpect(status().isOk())
            .andExpect(content().string("Minor GC"));
    }

    @Test
    void getGCCollectionType_WithSpecificParam_ShouldReturnType() throws Exception {
        // Given
        when(garbageCollectionMonitorService.determineGCType("PS Scavenge")).thenReturn("Minor GC");

        // When/Then
        mockMvc.perform(get("/gc/coll-type").param("collectionName", "PS Scavenge"))
            .andExpect(status().isOk())
            .andExpect(content().string("Minor GC"));
    }

    @Test
    void getGCMetricsByCollectionName_ShouldReturnMetricsMap() throws Exception {
        // Given
        Map<String, GarbageCollectionMetricsDTO> metricsMap = new HashMap<>();
        GarbageCollectionMetricsDTO metrics = new GarbageCollectionMetricsDTO();
        metrics.setCollectionName("PS Scavenge");
        metrics.setCollectionCount(10L);
        metrics.setCollectionTime("100ms");
        metricsMap.put("PS Scavenge", metrics);
        
        when(garbageCollectionMonitorService.getGCMetricsByCollectionName()).thenReturn(metricsMap);

        // When/Then
        mockMvc.perform(get("/gc/metrics-by-coll-name"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.['PS Scavenge'].collectionName").value("PS Scavenge"))
            .andExpect(jsonPath("$.['PS Scavenge'].collectionCount").value(10))
            .andExpect(jsonPath("$.['PS Scavenge'].collectionTime").value("100ms"));
    }
}
