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
     * @param formatted if true, return formatted bytes
     * @return heap memory usage in bytes
     */
    public String getHeapMemoryUsage(boolean formatted) {
        MemoryMXBean memoryPoolMXBean = ManagementFactory.getMemoryMXBean();
        if (formatted) {
            return MemoryUtils.formatBytes(memoryPoolMXBean.getHeapMemoryUsage().getUsed());
        } else {
            return String.valueOf(memoryPoolMXBean.getHeapMemoryUsage().getUsed());
        }
    }

    /**
     * get non heap memory usage
     * 
     * @param formatted if true, return formatted bytes
     * @return non heap memory usage in bytes
     */
    public String getNonHeapMemoryUsage(boolean formatted) {
        MemoryMXBean memoryPoolMXBean = ManagementFactory.getMemoryMXBean();
        if (formatted) {
            return MemoryUtils.formatBytes(memoryPoolMXBean.getNonHeapMemoryUsage().getUsed());
        } else {
            return String.valueOf(memoryPoolMXBean.getNonHeapMemoryUsage().getUsed());
        }
    }

    /**
     * get heap memory max
     * 
     * @param formatted if true, return formatted bytes
     * @return heap memory max in bytes
     */
    public String getHeapMemoryMax(boolean formatted) {
        MemoryMXBean memoryPoolMXBean = ManagementFactory.getMemoryMXBean();
        if (formatted) {
            return MemoryUtils.formatBytes(memoryPoolMXBean.getHeapMemoryUsage().getMax());
        } else {
            return String.valueOf(memoryPoolMXBean.getHeapMemoryUsage().getMax());
        }
    }

    /**
     * get heap memory committed
     * 
     * @param formatted if true, return formatted bytes
     * @return heap memory committed in bytes
     */
    public String getHeapMemoryCommitted(boolean formatted) {
        MemoryMXBean memoryPoolMXBean = ManagementFactory.getMemoryMXBean();
        if (formatted) {
            return MemoryUtils.formatBytes(memoryPoolMXBean.getHeapMemoryUsage().getCommitted());
        } else {
            return String.valueOf(memoryPoolMXBean.getHeapMemoryUsage().getCommitted());
        }
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
