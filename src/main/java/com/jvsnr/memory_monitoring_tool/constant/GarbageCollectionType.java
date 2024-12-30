package com.jvsnr.memory_monitoring_tool.constant;

public enum GarbageCollectionType {
    OLD_GENERATION("Old Generation"),
    YOUNG_GENERATION("Young Generation"),
    UNKNOWN("Unknown");
    
    GarbageCollectionType(String string) {}
}
