package com.jvsnr.memory_monitoring_tool.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.management.NotificationEmitter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jvsnr.memory_monitoring_tool.constant.GarbageCollectionType;
import com.jvsnr.memory_monitoring_tool.dto.GarbageCollectionMetricsDTO;
import com.sun.management.GcInfo;

@ExtendWith(MockitoExtension.class)
public class GarbageCollectionMonitorServiceTest {

    @Mock
    private GarbageCollectorMXBean youngGenGC;

    @Mock
    private GarbageCollectorMXBean oldGenGC; 

    @Mock
    private NotificationEmitter youngGenEmitter;

    @Mock
    private NotificationEmitter oldGenEmitter;

    private GarbageCollectionMonitorService gcMonitorService;

    private List<GarbageCollectorMXBean> garbageCollectorMXBeans;

    @BeforeEach
    void setUp() {
        garbageCollectorMXBeans = new ArrayList<>();
        garbageCollectorMXBeans.add(youngGenGC);
        garbageCollectorMXBeans.add(oldGenGC);
    }

    @Test
    void getGCCollectionCount_ShouldReturnTotalCollectionCount() {
        // Given
        gcMonitorService = new GarbageCollectionMonitorService(garbageCollectorMXBeans);

        when(youngGenGC.getCollectionCount()).thenReturn(10L);
        when(oldGenGC.getCollectionCount()).thenReturn(2L);

        // When 
        long totalCollectionCount = gcMonitorService.getGCCollectionCount();

        // Then
        assertEquals(12L, totalCollectionCount);
    }

    @Test
    void getGCCollectionTime_ShouldReturnFormattedTotalTime() {
        // Given
        gcMonitorService = new GarbageCollectionMonitorService(garbageCollectorMXBeans);

        when(youngGenGC.getCollectionTime()).thenReturn(100L);
        when(oldGenGC.getCollectionTime()).thenReturn(500L);
        
        // When
        String totalTime = gcMonitorService.getGCCollectionTime();

        // Then
        assertEquals("600ms", totalTime); // 100ms young + 500ms old
    }

    @Test
    void determineGCType_ShouldIdentifyYoungGenGC() {
        // Given
        gcMonitorService = new GarbageCollectionMonitorService(garbageCollectorMXBeans);

        // When
        String gcType = gcMonitorService.determineGCType("PS Scavenge");

        // Then
        assertEquals(GarbageCollectionType.YOUNG_GENERATION.name().toUpperCase(), gcType);
    }

    @Test
    void determineGCType_ShouldIdentifyOldGenGC() {
        // Given
        gcMonitorService = new GarbageCollectionMonitorService(garbageCollectorMXBeans);

        // When
        String gcType = gcMonitorService.determineGCType("PS MarkSweep");

        // Then
        assertEquals(GarbageCollectionType.OLD_GENERATION.name().toUpperCase(), gcType);
    }   

    @Test
    void determineGCType_ShouldIdentifyUnknownGC() {
        // Given
        gcMonitorService = new GarbageCollectionMonitorService(garbageCollectorMXBeans);

        // When
        String gcType = gcMonitorService.determineGCType("Unknown GC");

        // Then
        assertEquals(GarbageCollectionType.UNKNOWN.name().toUpperCase(), gcType);
    }

    @Test
    void determineGCType_ShouldHandleNullGCName() {
        // Given
        gcMonitorService = new GarbageCollectionMonitorService(garbageCollectorMXBeans);

        // When
        String gcType = gcMonitorService.determineGCType(null);

        // Then
        assertEquals(GarbageCollectionType.UNKNOWN.name().toUpperCase(), gcType);
    }

    @Test
    void determineGCType_ShouldHandleEmptyGCName() {
        // Given
        gcMonitorService = new GarbageCollectionMonitorService(garbageCollectorMXBeans);

        // When
        String gcType = gcMonitorService.determineGCType("");

        // Then
        assertEquals(GarbageCollectionType.UNKNOWN.name().toUpperCase(), gcType);
    }

    @Test
    void determineGCType_ShouldIdentifyCollectorTypes() {
        // Given
        gcMonitorService = new GarbageCollectionMonitorService(garbageCollectorMXBeans);

        // When & Then
        assertEquals(GarbageCollectionType.YOUNG_GENERATION.name(), 
                    gcMonitorService.determineGCType("PS Scavenge"));
        assertEquals(GarbageCollectionType.YOUNG_GENERATION.name(), 
                    gcMonitorService.determineGCType("Copy"));
        assertEquals(GarbageCollectionType.OLD_GENERATION.name(), 
                    gcMonitorService.determineGCType("PS MarkSweep"));
        assertEquals(GarbageCollectionType.OLD_GENERATION.name(), 
                    gcMonitorService.determineGCType("CMS"));
        assertEquals(GarbageCollectionType.UNKNOWN.name(), 
                    gcMonitorService.determineGCType("Unknown Collector"));
        assertEquals(GarbageCollectionType.UNKNOWN.name(), 
                    gcMonitorService.determineGCType(null));
        assertEquals(GarbageCollectionType.UNKNOWN.name(), 
                    gcMonitorService.determineGCType(""));
    }

    @Test
    void getGCMetricsByCollectionName_ShouldReturnMetricsForAllCollectors() {
        // Given
        gcMonitorService = new GarbageCollectionMonitorService(garbageCollectorMXBeans);
        // Set up mock GC beans
        when(youngGenGC.getName()).thenReturn("PS Scavenge");
        when(oldGenGC.getName()).thenReturn("PS MarkSweep");

        // When
        Map<String, GarbageCollectionMetricsDTO> metrics = gcMonitorService.getGCMetricsByCollectionName();

        // Then
        assertNotNull(metrics);
        assertEquals(2, metrics.size());
        assertTrue(metrics.containsKey("PS Scavenge"));
        assertTrue(metrics.containsKey("PS MarkSweep"));
    }

    @Test
    void getLastGCInfo_ShouldReturnLastGCInfoForAllCollectors() {
        // Given
        gcMonitorService = new GarbageCollectionMonitorService(garbageCollectorMXBeans);
        // Set up mock GC beans
        when(youngGenGC.getName()).thenReturn("PS Scavenge");
        when(oldGenGC.getName()).thenReturn("PS MarkSweep");

        // Populate metrics
       gcMonitorService.getGCMetricsByCollectionName();

        // When
        GarbageCollectionMetricsDTO metricsDTO = gcMonitorService.getLastGCInfo();

        // Then
        assertNotNull(metricsDTO);
        assertTrue(metricsDTO.getCollectionName().equals("PS MarkSweep"));
    }

    @Test
    void getLastGCInfo_WhenNoMetrics_ShouldReturnNull() {
        // Given
        gcMonitorService = new GarbageCollectionMonitorService(garbageCollectorMXBeans);

        // When
        GarbageCollectionMetricsDTO metricsDTO = gcMonitorService.getLastGCInfo();

        // Then
        assertNull(metricsDTO);
    }

    @Test
    void getGCMetricsByCollectionName_ShouldCalculateGCOverhead() {
        // Given
        when(youngGenGC.getName()).thenReturn("PS Scavenge");
        when(youngGenGC.getCollectionTime()).thenReturn(100L);
        when(oldGenGC.getName()).thenReturn("PS MarkSweep");
        when(oldGenGC.getCollectionTime()).thenReturn(200L);
        gcMonitorService = new GarbageCollectionMonitorService(garbageCollectorMXBeans);

        // When
        Map<String, GarbageCollectionMetricsDTO> metrics = gcMonitorService.getGCMetricsByCollectionName();

        // Then
        assertNotNull(metrics.get("PS Scavenge").getGcOverhead());
        assertNotNull(metrics.get("PS MarkSweep").getGcOverhead());
    }

    //----------------------

    @Test
    void setUpGCMonitoring_ShouldHandleGCNotifications() {
        GarbageCollectorMXBean youngGenBean = mock(GarbageCollectorMXBean.class, withSettings()
            .extraInterfaces(NotificationEmitter.class));
        
        // when(youngGenBean.getName()).thenReturn("PS Scavenge");
        // when(youngGenBean.getCollectionCount()).thenReturn(10L);
        // when(youngGenBean.getCollectionTime()).thenReturn(100L);
        
        // When
        gcMonitorService = new GarbageCollectionMonitorService(Arrays.asList(youngGenBean));

        // Then
        verify((NotificationEmitter)youngGenBean).addNotificationListener(any(), isNull(), isNull());
    }

    @Test
    void getGCMetricsByCollectionName_ShouldHandleLastGCInfo() {
        // Given
            com.sun.management.GarbageCollectorMXBean sunGcBean = mock(com.sun.management.GarbageCollectorMXBean.class);
            GcInfo gcInfo = mock(GcInfo.class);
            when(sunGcBean.getName()).thenReturn("PS Scavenge");
            when(sunGcBean.getCollectionCount()).thenReturn(10L);
            when(sunGcBean.getCollectionTime()).thenReturn(100L);
        when(sunGcBean.getLastGcInfo()).thenReturn(gcInfo);
        when(gcInfo.getDuration()).thenReturn(50L);
        when(gcInfo.getStartTime()).thenReturn(System.currentTimeMillis());

        gcMonitorService = new GarbageCollectionMonitorService(Arrays.asList(sunGcBean));

        // When
        Map<String, GarbageCollectionMetricsDTO> metrics = gcMonitorService.getGCMetricsByCollectionName();

        // Then
        GarbageCollectionMetricsDTO dto = metrics.get("PS Scavenge");
        assertNotNull(dto);
        assertEquals("50ms", dto.getLastGCDuration());
        assertNotEquals("N/A", dto.getLastGCStartTime());
    }

    @Test
    void getGCMetricsByCollectionName_WhenLastGCInfoNull_ShouldHandleGracefully() {
        // Given
        com.sun.management.GarbageCollectorMXBean sunGcBean = mock(com.sun.management.GarbageCollectorMXBean.class);
        when(sunGcBean.getName()).thenReturn("PS Scavenge");
        when(sunGcBean.getCollectionCount()).thenReturn(10L);
        when(sunGcBean.getCollectionTime()).thenReturn(100L);
        when(sunGcBean.getLastGcInfo()).thenReturn(null);

        gcMonitorService = new GarbageCollectionMonitorService(Arrays.asList(sunGcBean));

        // When
        Map<String, GarbageCollectionMetricsDTO> metrics = gcMonitorService.getGCMetricsByCollectionName();

        // Then
        GarbageCollectionMetricsDTO dto = metrics.get("PS Scavenge");
        assertNotNull(dto);
        assertEquals("N/A", dto.getLastGCDuration());
        assertEquals("N/A", dto.getLastGCStartTime());
    }

}
