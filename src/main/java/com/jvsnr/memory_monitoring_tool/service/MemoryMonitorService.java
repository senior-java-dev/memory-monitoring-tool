package com.jvsnr.memory_monitoring_tool.service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import org.springframework.stereotype.Service;

import com.jvsnr.util.MemoryUtils;

@Service
public class MemoryMonitorService {
    
    /**
     * get heap memory usage
     * 
     * @return heap memory usage in bytes
     */
    public String getHeapMemoryUsage() {
        MemoryMXBean memoryPoolMXBean = ManagementFactory.getMemoryMXBean();
        return MemoryUtils.formatBytes(memoryPoolMXBean.getHeapMemoryUsage().getUsed());
    }

    /**
     * get non heap memory usage
     * 
     * @return non heap memory usage in bytes
     */
    public String getNonHeapMemoryUsage() {
        MemoryMXBean memoryPoolMXBean = ManagementFactory.getMemoryMXBean();
        return MemoryUtils.formatBytes(memoryPoolMXBean.getNonHeapMemoryUsage().getUsed());
    }

    /*
     * get heap memory max
     * 
     * @return heap memory max in bytes
     */
    public String getHeapMemoryMax() {
        MemoryMXBean memoryPoolMXBean = ManagementFactory.getMemoryMXBean();
        return MemoryUtils.formatBytes(memoryPoolMXBean.getHeapMemoryUsage().getMax());
    }

    /*
     * get heap memory committed
     * 
     * @return heap memory committed in bytes
     */
    public String getHeapMemoryCommitted() {
        MemoryMXBean memoryPoolMXBean = ManagementFactory.getMemoryMXBean();
        return MemoryUtils.formatBytes(memoryPoolMXBean.getHeapMemoryUsage().getCommitted());
    }

    /**
     * get memory utilisation
     * 
     * @return memory utilisation in percentage
     */
    public String getMemoryUtilisation() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        long used = heapMemoryUsage.getUsed();
        long max = heapMemoryUsage.getMax();
        float percentage = ((float) used / max) * 100;
        return Double.toString(percentage);
    }

}
