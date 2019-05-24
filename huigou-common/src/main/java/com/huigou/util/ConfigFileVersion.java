package com.huigou.util;

public interface ConfigFileVersion {

    /**
     * 获取对象版本号
     * 
     * @return
     */
    Long getVersion();

    /**
     * 获取配置文件路径
     * 
     * @return
     */
    String getFilePath();
}
