package com.jvsnr.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MemoryUtils {
    
    /**
     * format bytes
     * 
     * @param bytes
     * @return formatted bytes
     */
    public static String formatBytes(long bytes) {
        if (bytes <= 0) {
            return "0 B";
        }

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

    public static long nullifyNegativeToZero(long value) {
        if (value < 0) {
            return 0L;
        }
        return value;
    }

    public static String formatTime(long milliseconds) {
        if (milliseconds <= 0) {
            return "0ms";
        }

        if (milliseconds < 1000) {
            return milliseconds + "ms";
        }

        return String.format("%.2fs", milliseconds / 1000.0);
    }

    public static String formatDateTime(long time) {
        if (time <= 0) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.format(new Date(time));
    }

}
