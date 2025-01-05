package com.jvsnr.memory_monitoring_tool.service;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

import org.springframework.stereotype.Service;

import com.jvsnr.memory_monitoring_tool.constant.GarbageCollectionType;
import com.jvsnr.memory_monitoring_tool.constant.GarbageCollectorName;
import com.jvsnr.memory_monitoring_tool.dto.GarbageCollectionMetricsDTO;
import com.jvsnr.util.MemoryUtils;
import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GarbageCollectionMonitorService {
    
    // Use GarbageCollectorMXBean to get GC information
    private List<GarbageCollectorMXBean> garbageCollectorMXBeans;
    private Map<String, GarbageCollectionMetricsDTO> garbageCollectionMetrics;

    public GarbageCollectionMonitorService() {
        this(ManagementFactory.getGarbageCollectorMXBeans());
    }

    public GarbageCollectionMonitorService(List<GarbageCollectorMXBean> garbageCollectorMXBeans) {
        this.garbageCollectorMXBeans = garbageCollectorMXBeans;
        this.garbageCollectionMetrics = new HashMap<>();
        setUpGCMonitoring();
    }

    /**
     * Set up monitoring for garbage collection.
     * 
     * This method sets up notification listeners for each garbage collector bean.
     * When a garbage collection notification is received, the method updates the
     * garbage collection metrics for the corresponding collector.
     */
    private void setUpGCMonitoring() {
        // Set up notification listeners for each GC bean
        for (GarbageCollectorMXBean gcBean : garbageCollectorMXBeans) {
            if (gcBean instanceof NotificationEmitter) {
                NotificationEmitter notificationEmitter = (NotificationEmitter) gcBean;
                NotificationListener notificationListener = (notification, handback) -> {
                    if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                        // Get the GC notification info
                        GarbageCollectionNotificationInfo gcInfo = GarbageCollectionNotificationInfo
                            .from((CompositeData) notification.getUserData());

                        // Update metrics for this collector
                        GarbageCollectionMetricsDTO metrics = garbageCollectionMetrics
                        .computeIfAbsent(gcBean.getName(), k -> new GarbageCollectionMetricsDTO());

                        metrics.setCollectionName(gcBean.getName());
                        metrics.setCollectionCount(gcBean.getCollectionCount());
                        metrics.setCollectionTime(MemoryUtils.formatTime(gcBean.getCollectionTime()));
                        metrics.setType(determineGCType(gcBean.getName()));

                        // Add more detailed information from gcInfo
                        metrics.setLastGCDuration(MemoryUtils.formatTime(gcInfo.getGcInfo().getDuration()));

                        metrics.setLastGCStartTime(MemoryUtils.formatDateTime(System.currentTimeMillis()));

                        // Log or notify about GC events if needed
                        logGCEvent(gcInfo);
                    }
                };
                notificationEmitter.addNotificationListener(notificationListener, null, null);
            }
        }
    }

    /**
     * Log or notify about GC events if needed.
     * 
     * @param gcInfo
     */
    private void logGCEvent(GarbageCollectionNotificationInfo gcInfo) {
        // Log important GC events, especially if they might indicate problems
        if (gcInfo.getGcInfo().getDuration() > 1000) { // GC took more than 1 second
            log.warn("Long GC pause detected: {} ms for collector {}", 
                gcInfo.getGcInfo().getDuration(), 
                gcInfo.getGcName());
        }
    }

    /**
     * Get the last GC info
     *
     * @return the last GC info
     */
    public GarbageCollectionMetricsDTO getLastGCInfo() {
        return garbageCollectionMetrics.values().stream().findFirst().orElse(null);
    }

    /**
     * Get the total number of garbage collections
     *
     * @return the total number of garbage collections
     */
    public long getGCCollectionCount() {
        return garbageCollectorMXBeans.stream()
                .mapToLong(bean -> MemoryUtils.nullifyNegativeToZero(bean.getCollectionCount()))
                .sum();
    }

    /**
     * Get the total time spent in garbage collection
     *
     * @return the total time spent in garbage collection
     */
    public String getGCCollectionTime() {
        long totalTimeInMillis = garbageCollectorMXBeans.stream()
                .mapToLong(bean -> MemoryUtils.nullifyNegativeToZero(bean.getCollectionTime()))
                .sum();
        
        return MemoryUtils.formatTime(totalTimeInMillis);
    }

    /**
     * Determine the type of garbage collection
     *
     * @param collectionName the name of the garbage collector
     * @return the type of garbage collection
     */
    public String determineGCType(String collectionName) {
        if (collectionName == null) {
            return GarbageCollectionType.UNKNOWN.name();
        }
        
        String lowercaseName = collectionName.toUpperCase();
        if (lowercaseName.contains(GarbageCollectorName.YOUNG.name()) || 
            lowercaseName.contains(GarbageCollectorName.MINOR.name()) || 
            lowercaseName.contains(GarbageCollectorName.COPY.name()) || 
            lowercaseName.contains(GarbageCollectorName.SCAVENGE.name())) {
            return GarbageCollectionType.YOUNG_GENERATION.name();
        } else if (lowercaseName.contains(GarbageCollectorName.OLD.name()) || 
                   lowercaseName.contains(GarbageCollectorName.MAJOR.name()) || 
                   lowercaseName.contains(GarbageCollectorName.CMS.name()) || 
                   lowercaseName.contains(GarbageCollectorName.MARK.name())) {
            return GarbageCollectionType.OLD_GENERATION.name();
        } else {
            return GarbageCollectionType.UNKNOWN.name();
        }
    }

    /**
     * Get the garbage collection metrics by collection name
     *
     * @return the garbage collection metrics by collection name
     */
    public Map<String, GarbageCollectionMetricsDTO> getGCMetricsByCollectionName() {
        for (GarbageCollectorMXBean gxBean: garbageCollectorMXBeans) {
            GarbageCollectionMetricsDTO gcMetrics = new GarbageCollectionMetricsDTO();
            gcMetrics.setCollectionName(gxBean.getName());
            gcMetrics.setCollectionCount(gxBean.getCollectionCount());
            gcMetrics.setCollectionTime(MemoryUtils.formatTime(gxBean.getCollectionTime()));
            gcMetrics.setType(determineGCType(gxBean.getName()));
            
            // Get GC overhead
            long upTime = ManagementFactory.getRuntimeMXBean().getUptime();
            double gcOverhead = ((double) gxBean.getCollectionTime() * 100) / upTime;
            gcMetrics.setGcOverhead(Math.round(gcOverhead * 100.0) / 100.0);

            //Get last GC info
            if (gxBean instanceof com.sun.management.GarbageCollectorMXBean) {
                com.sun.management.GarbageCollectorMXBean sunGcBean = (com.sun.management.GarbageCollectorMXBean) gxBean;
                GcInfo lastGcInfo = sunGcBean.getLastGcInfo();

                if (lastGcInfo != null) {
                    gcMetrics.setLastGCDuration(MemoryUtils.formatTime(lastGcInfo.getDuration()));
                    gcMetrics.setLastGCStartTime(MemoryUtils.formatDateTime(lastGcInfo.getStartTime()));
                } else {
                    gcMetrics.setLastGCDuration("N/A");
                    gcMetrics.setLastGCStartTime("N/A");
                }
            }

            garbageCollectionMetrics.put(gcMetrics.getCollectionName(), gcMetrics);
        }

        return garbageCollectionMetrics;
    }

}
