package com.huigou.uasp.bmp.configuration.model;

/**
 * JSP标签获取国际化资源时 没有传入数据时的默认取值定义
 * 
 * @author xx
 */
public class I18nDefaultConfig {
    private String defaultName;// 默认资源文件名

    private String defaultDictionary;// 默认读取数据字典编码

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getDefaultDictionary() {
        return defaultDictionary;
    }

    public void setDefaultDictionary(String defaultDictionary) {
        this.defaultDictionary = defaultDictionary;
    }

}
