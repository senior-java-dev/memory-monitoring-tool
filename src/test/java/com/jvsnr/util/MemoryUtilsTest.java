package com.jvsnr.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

public class MemoryUtilsTest {

    @Test
    void formatBytes_WhenNegative_ShouldHandleGracefully() {
        // Given
        long bytes = -1024L;

        // When
        String result = MemoryUtils.formatBytes(bytes);

        // Then
        assertEquals("0 B", result);
    }

    @Test
    void formatBytes_WhenZero_ShouldReturnZero() {
        // Given
        long bytes = 0L;

        // When
        String formattedBytes = MemoryUtils.formatBytes(bytes);

        // Then
        assertEquals("0 B", formattedBytes);
    }

    @Test
    void formatBytes_WhenBytesLessThan1024_ShouldReturnBytes() {
        // Given
        long bytes = 500L;

        // When
        String formattedBytes = MemoryUtils.formatBytes(bytes);

        // Then
        assertEquals("500 B", formattedBytes);
    }

    @Test
    void formatBytes_WhenMoreThanBytes_ShouldReturnKilobytes() {
        // Given
        long bytes = 500 * 1024L; // 500 KB

        // When
        String formattedBytes = MemoryUtils.formatBytes(bytes);

        // Then
        assertEquals("500,00 KB", formattedBytes);
    }

    @Test
    void formatBytes_WhenMoreThanKilobytes_ShouldReturnMegabytes() {
        // Given
        long bytes = 500 * (long) Math.pow(1024, 2); // 500 MB

        // When
        String formattedBytes = MemoryUtils.formatBytes(bytes);

        // Then
        assertEquals("500,00 MB", formattedBytes);
    }

    @Test
    void formatBytes_WhenMoreThanMegabytes_ShouldReturnGigabytes() {
        // Given
        long bytes = 500 * (long) Math.pow(1024, 3); // 500 GB

        // When
        String formattedBytes = MemoryUtils.formatBytes(bytes);

        // Then
        assertEquals("500,00 GB", formattedBytes);
    }

    @Test
    void nullifyNegativeToZero_WhenValueIsNegative_ShouldReturnZero() {
        // Given
        Long value = -1L;

        // When
        Long nullifiedValue = MemoryUtils.nullifyNegativeToZero(value);

        // Then
        assertEquals(0L, nullifiedValue);
    }

    @Test
    void nullifyNegativeToZero_WhenValueIsZero_ShouldReturnZero() {
        // Given
        Long value = 0L;

        // When
        Long nullifiedValue = MemoryUtils.nullifyNegativeToZero(value);

        // Then
        assertEquals(0L, nullifiedValue);
    }

    @Test
    void formatTime_WhenZeroOrNegative_ShouldReturnZeroMs() {
        // Given
        long millis = 0L;
        long negativeMillis = -100L;

        // When
        String resultZero = MemoryUtils.formatTime(millis);
        String resultNegative = MemoryUtils.formatTime(negativeMillis);

        // Then
        assertEquals("0ms", resultZero);
        assertEquals("0ms", resultNegative);
    }

    @Test
    void formatTime_WhenMillisecondsLessThan1000_ShouldReturnMilliseconds() {
        // Given
        long milliseconds = 500L;

        // When
        String formattedTime = MemoryUtils.formatTime(milliseconds);

        // Then
        assertEquals("500ms", formattedTime);
    }   

    @Test
    void formatTime_WhenMillisecondsMoreThan1000_ShouldReturnSeconds() {
        // Given
        long milliseconds = 1500L;

        // When 
        String formattedTime = MemoryUtils.formatTime(milliseconds);

        // Then
        assertEquals("1,50s", formattedTime);
    }

    @Test
    void formatDateTime_WhenTimeIsZero_ShouldReturnEmptyString() {
        // Given
        long time = 0L;

        // When
        String formattedDateTime = MemoryUtils.formatDateTime(time);

        // Then
        assertEquals("", formattedDateTime);
    }

    @Test
    void formatDateTime_WhenTimeIsNonZero_ShouldReturnDateTime() {
        // Given
        long time = 900000L; // 15 minutes after epoch
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Use UTC to avoid timezone issues
        String expected = sdf.format(time);

        // When
        String result = MemoryUtils.formatDateTime(time);

        // Then
        assertEquals(expected, result);
    }

}
