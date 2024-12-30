package com.jvsnr.memory_monitoring_tool.constant;

public enum GarbageCollectorName {
    YOUNG("YOUNG"),
    MINOR("MINOR"),
    COPY("COPY"),
    SCAVENGE("ZGC"),
    OLD("OLD"),
    MAJOR("MAJOR"),
    CMS("CMS"),
    MARK("MARK");
    
    GarbageCollectorName(String string) {}
}