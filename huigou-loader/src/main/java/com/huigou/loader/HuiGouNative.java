package com.huigou.loader;

import com.huigou.system.common.SystemUtils;

public class HuiGouNative {
    /**
     * 默认构造器
     * 
     * @param parent
     */
    static {
        String fileName = SystemUtils.normalizeLib("huigou-loader");
        System.load(fileName);
    }

    public static native Class<?> findClass(HuiGouClassLoader classLoader, String name) throws ClassNotFoundException;
}
