package com.jvsnr.util;

public class MemoryUtils {
    
    /**
     * format bytes
     * 
     * @param bytes
     * @return formatted bytes
     */
    public static String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < Math.pow(1024, 2)) {
            return String.format("%.2f KB", (double) bytes / 1024);
        } else if (bytes < Math.pow(1024, 3)) {
            return String.format("%.2f MB", (double) bytes / Math.pow(1024, 2));
        } else {
            return String.format("%.2f GB", (double) bytes / Math.pow(1024, 3));
        }
    }

}
