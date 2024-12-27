package com.jvsnr.memory_monitoring_tool.service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import org.springframework.stereotype.Service;

@Service
public class MemoryMonitorService {
    
    /**
     * get heap memory usage
     * 
     * @return heap memory usage in bytes
     */
    public long getHeapMemoryUsage() {
        MemoryMXBean memoryPoolMXBean = ManagementFactory.getMemoryMXBean();
        return memoryPoolMXBean.getHeapMemoryUsage().getUsed();
    }

    /**
     * get non heap memory usage
     * 
     * @return non heap memory usage in bytes
     */
    public long getNonHeapMemoryUsage() {
        MemoryMXBean memoryPoolMXBean = ManagementFactory.getMemoryMXBean();
        return memoryPoolMXBean.getNonHeapMemoryUsage().getUsed();
    }

    /*
     * get heap memory max
     * 
     * @return heap memory max in bytes
     */
    public long getHeapMemoryMax() {
        MemoryMXBean memoryPoolMXBean = ManagementFactory.getMemoryMXBean();
        return memoryPoolMXBean.getHeapMemoryUsage().getMax();
    }

    /*
     * get heap memory committed
     * 
     * @return heap memory committed in bytes
     */
    public long getHeapMemoryCommitted() {
        MemoryMXBean memoryPoolMXBean = ManagementFactory.getMemoryMXBean();
        return memoryPoolMXBean.getHeapMemoryUsage().getCommitted();
    }

    /**
     * get memory utilisation
     * 
     * @return memory utilisation in percentage
     */
    public double getMemoryUtilisation() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        long used = getHeapMemoryUsage();
        long max = heapMemoryUsage.getMax();
        return ((double) used / max) * 100;
    }

}
