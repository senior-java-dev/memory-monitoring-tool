package com.jvsnr.memory_monitoring_tool;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

@SpringBootTest
class MemoryMonitoringToolApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }

    @Test
    void schedulingIsEnabled() {
        assertThat(applicationContext.getBean(ScheduledAnnotationBeanPostProcessor.class))
            .isNotNull()
            .as("Scheduling should be enabled via @EnableScheduling");
    }

    @Test
    void mainMethodStartsApplication() {
        // This test verifies that the main method can be called without throwing exceptions
        MemoryMonitoringToolApplication.main(new String[]{});
        assertThat(applicationContext).isNotNull();
    }
}
