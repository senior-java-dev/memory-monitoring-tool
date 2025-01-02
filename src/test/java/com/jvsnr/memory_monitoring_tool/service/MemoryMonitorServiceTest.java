package com.jvsnr.memory_monitoring_tool.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemoryMonitorServiceTest {

    private MemoryMonitorService memoryMonitorService;

    @Mock
    private MemoryMXBean memoryMXBean;

    @BeforeEach
    void setUp() {
        memoryMonitorService = new MemoryMonitorService(memoryMXBean);
    }

    @Test
    void getHeapMemoryUsage_WhenFormatted_ShouldReturnFormattedValue() {
        // Given
        MemoryUsage heapMemoryUsage = new MemoryUsage(0L, 500 * (long) Math.pow(1024, 2), (long) Math.pow(1024, 3), (long) Math.pow(1024, 3));
        when(memoryMXBean.getHeapMemoryUsage()).thenReturn(heapMemoryUsage);

        // When
        String formattedHeapMemoryUsage = memoryMonitorService.getHeapMemoryUsage(true);

        // Then
        assertEquals("500,00 MB", formattedHeapMemoryUsage);
    }

    @Test
    void getHeapMemoryUsage_WhenNotFormatted_ShouldReturnRawValue() {
        // Given
        long expected = 500 * 1024 * 1024L;
        MemoryUsage heapMemoryUsage = new MemoryUsage(0L, expected, (long) Math.pow(1024, 3), (long) Math.pow(1024, 3));
        when(memoryMXBean.getHeapMemoryUsage()).thenReturn(heapMemoryUsage);

        // When
        String formattedHeapMemoryUsage = memoryMonitorService.getHeapMemoryUsage(false);

        // Then
        assertEquals(expected, Long.parseLong(formattedHeapMemoryUsage));
    }

    @Test
    void getNonHeapMemoryUsage_WhenFormatted_ShouldReturnFormattedValue() {
        // Given
        long expected = 600 * (long) Math.pow(1024, 2);
        MemoryUsage nonHeapMemoryUsage = new MemoryUsage(0L, expected, (long) Math.pow(1024, 3), (long) Math.pow(1024, 3));
        when(memoryMXBean.getNonHeapMemoryUsage()).thenReturn(nonHeapMemoryUsage);

        // When
        String formattedNonHeapMemoryUsage = memoryMonitorService.getNonHeapMemoryUsage(true);

        // Then
        assertEquals("600,00 MB", formattedNonHeapMemoryUsage);
    }

    @Test
    void getNonHeapMemoryUsage_WhenNotFormatted_ShouldReturnRawValue() {
        // Given
        long expected = 600 * (long) Math.pow(1024, 2);
        MemoryUsage nonHeapMemoryUsage = new MemoryUsage(0L, expected, (long) Math.pow(1024, 3), (long) Math.pow(1024, 3));
        when(memoryMXBean.getNonHeapMemoryUsage()).thenReturn(nonHeapMemoryUsage);

        // When
        String formattedNonHeapMemoryUsage = memoryMonitorService.getNonHeapMemoryUsage(false);

        // Then
        assertEquals(expected, Long.parseLong(formattedNonHeapMemoryUsage));
    }

    @Test
    void getHeapMemoryMax_WhenFormatted_ShouldReturnFormattedValue() {
        // Given
        long max = (long) Math.pow(1024, 3);      // 1 GB
        long committed = max;                      // Same as max
        long used = 500 * (long) Math.pow(1024, 2); // 500 MB
        MemoryUsage heapMemoryUsage = new MemoryUsage(0L, used, committed, max);
        when(memoryMXBean.getHeapMemoryUsage()).thenReturn(heapMemoryUsage);

        // When
        String formattedHeapMemoryMax = memoryMonitorService.getHeapMemoryMax(true);

        // Then
        assertEquals("1,00 GB", formattedHeapMemoryMax);
    }

    @Test
    void getHeapMemoryMax_WhenNotFormatted_ShouldReturnRawValue() {
        // Given
        long max = (long) Math.pow(1024, 3);      // 1 GB
        long committed = max;                      // Same as max
        long used = 500 * (long) Math.pow(1024, 2); // 500 MB
        MemoryUsage heapMemoryUsage = new MemoryUsage(0L, used, committed, max);
        when(memoryMXBean.getHeapMemoryUsage()).thenReturn(heapMemoryUsage);

        // When
        String formattedHeapMemoryMax = memoryMonitorService.getHeapMemoryMax(false);

        // Then
        assertEquals(max, Long.parseLong(formattedHeapMemoryMax));
    }

    
    @Test
    void getHeapMemoryCommitted_WhenFormatted_ShouldReturnFormattedValue() {
        // Given
        long expected = 700 * (long) Math.pow(1024, 2);
        MemoryUsage heapMemoryUsage = new MemoryUsage(0L, 500 * (long) Math.pow(1024, 2), expected, (long) Math.pow(1024, 3));
        when(memoryMXBean.getHeapMemoryUsage()).thenReturn(heapMemoryUsage);

        // When
        String formattedHeapMemoryCommitted = memoryMonitorService.getHeapMemoryCommitted(true);

        // Then
        assertEquals("700,00 MB", formattedHeapMemoryCommitted);
    }

    @Test
    void getHeapMemoryCommitted_WhenNotFormatted_ShouldReturnRawValue() {
        // Given
        long expected = 700 * (long) Math.pow(1024, 2);
        MemoryUsage heapMemoryUsage = new MemoryUsage(0L, 500 * (long) Math.pow(1024, 2), expected, (long) Math.pow(1024, 3));
        when(memoryMXBean.getHeapMemoryUsage()).thenReturn(heapMemoryUsage);

        // When
        String formattedHeapMemoryCommitted = memoryMonitorService.getHeapMemoryCommitted(false);

        // Then
        assertEquals(expected, Long.parseLong(formattedHeapMemoryCommitted));
    }

    @Test
    void constructor_ShouldInitializeWithDefaultMemoryMXBean() {
        // When
        MemoryMonitorService service = new MemoryMonitorService();

        // Then
        assertNotNull(service);
    }

    @Test
    void getMemoryUtilisation_WhenMaxIsZero_ShouldHandleGracefully() {
        // Given 
        long used = 0L;         // used must be <= committed
        long committed = 0L;    // committed must be <= max
        long max = 0L;         // max of 0 is our edge case
        MemoryUsage heapMemoryUsage = new MemoryUsage(0L, used, committed, max);
        when(memoryMXBean.getHeapMemoryUsage()).thenReturn(heapMemoryUsage);

        // When
        String memoryUtilisation = memoryMonitorService.getMemoryUtilisation();

        // Then
        assertEquals("0.0", memoryUtilisation);
    }

    @Test
    void getMemoryUtilisation_WhenUsedIsZero_ShouldReturnZero() {
        // Given 
        long used = 0L;
        long committed = (long) Math.pow(1024, 3); // 1 GB
        long max = (long) Math.pow(1024, 3); // 1 GB
        MemoryUsage heapMemoryUsage = new MemoryUsage(0L, used, committed, max);
        when(memoryMXBean.getHeapMemoryUsage()).thenReturn(heapMemoryUsage);

        // When
        String memoryUtilisation = memoryMonitorService.getMemoryUtilisation();

        // Then
        assertEquals("0.0", memoryUtilisation);
    }
    
    @Test
    void getMemoryUtilisation() {
        // Given 
        long used = 512 * (long) Math.pow(1024, 2); // 512 MB
        long max = (long) Math.pow(1024, 3); // 1 GB
        MemoryUsage heapMemoryUsage = new MemoryUsage(0L, used, max, max);
        when(memoryMXBean.getHeapMemoryUsage()).thenReturn(heapMemoryUsage);

        // When
        String memoryUtilisation = memoryMonitorService.getMemoryUtilisation();

        // Then
        assertEquals("50.0", memoryUtilisation);
    }

}
