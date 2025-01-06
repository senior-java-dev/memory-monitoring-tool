package com.jvsnr.memory_monitoring_tool;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

class ServletInitializerTest {

    @Test
    void configure_ShouldSetupApplicationSourcesCorrectly() {
        // Given
        ServletInitializer servletInitializer = new ServletInitializer();
        SpringApplicationBuilder builder = mock(SpringApplicationBuilder.class);
        when(builder.sources(MemoryMonitoringToolApplication.class)).thenReturn(builder);

        // When
        SpringApplicationBuilder result = servletInitializer.configure(builder);

        // Then
        verify(builder).sources(MemoryMonitoringToolApplication.class);
        assertThat(result).isEqualTo(builder);
    }
}
