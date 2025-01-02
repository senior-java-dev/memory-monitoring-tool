package com.jvsnr.memory_monitoring_tool.service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import org.springframework.stereotype.Service;

import com.jvsnr.util.MemoryUtils;

@Service
public class MemoryMonitorService {

    private final MemoryMXBean memoryMXBean;

    public MemoryMonitorService() {
        this(ManagementFactory.getMemoryMXBean());
    }

    public MemoryMonitorService(MemoryMXBean memoryMXBean) {
        this.memoryMXBean = memoryMXBean;
    }
    
    /**
     * get heap memory usage
     * 
     * @param formatted if true, return formatted bytes
     * @return heap memory usage in bytes
     */
    public String getHeapMemoryUsage(boolean formatted) {
        if (formatted) {
            return MemoryUtils.formatBytes(memoryMXBean.getHeapMemoryUsage().getUsed());
        } else {
            return String.valueOf(memoryMXBean.getHeapMemoryUsage().getUsed());
        }
    }

    /**
     * get non heap memory usage
     * 
     * @param formatted if true, return formatted bytes
     * @return non heap memory usage in bytes
     */
    public String getNonHeapMemoryUsage(boolean formatted) {
        if (formatted) {
            return MemoryUtils.formatBytes(memoryMXBean.getNonHeapMemoryUsage().getUsed());
        } else {
            return String.valueOf(memoryMXBean.getNonHeapMemoryUsage().getUsed());
        }
    }

    /**
     * get heap memory max
     * 
     * @param formatted if true, return formatted bytes
     * @return heap memory max in bytes
     */
    public String getHeapMemoryMax(boolean formatted) {
        if (formatted) {
            return MemoryUtils.formatBytes(memoryMXBean.getHeapMemoryUsage().getMax());
        } else {
            return String.valueOf(memoryMXBean.getHeapMemoryUsage().getMax());
        }
    }

    /**
     * get heap memory committed
     * 
     * @param formatted if true, return formatted bytes
     * @return heap memory committed in bytes
     */
    public String getHeapMemoryCommitted(boolean formatted) {
        if (formatted) {
            return MemoryUtils.formatBytes(memoryMXBean.getHeapMemoryUsage().getCommitted());
        } else {
            return String.valueOf(memoryMXBean.getHeapMemoryUsage().getCommitted());
        }
    }

    /**
     * get memory utilisation
     * 
     * @return memory utilisation in percentage
     */
    public String getMemoryUtilisation() {
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        long used = heapMemoryUsage.getUsed();
        long max = heapMemoryUsage.getMax();

        if (max <= 0) {
            return "0.0";
        }

        float percentage = ((float) used / max) * 100;
        return Double.toString(percentage);
    }

}
