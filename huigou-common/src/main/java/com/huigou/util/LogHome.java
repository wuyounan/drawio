package com.huigou.util;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

/**
 * log4j 使用工具类
 * 
 * @author gongmm
 */
public class LogHome {
    
    public static Logger getLog() {
        return Logger.getLogger(LogHome.class);
    }

    public static Logger getLog(Class<?> cls) {
        return Logger.getLogger(cls);
    }

    public static Logger getLog(Object obj) {
        return getLog(obj.getClass());
    }

    public static Logger getLog(String name, LoggerFactory loggerFactory) {
        return Logger.getLogger(name, loggerFactory);
    }

}
