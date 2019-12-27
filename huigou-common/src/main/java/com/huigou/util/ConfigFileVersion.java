package com.huigou.util;

import java.util.List;

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
     * @deprecated 请使用 {@link #getFilePaths()} 替代
     */
    @Deprecated
    String getFilePath();

    /**
     * @since 1.1.3
     */
    List<String> getFilePaths();
}
