package com.huigou.properties;

import java.io.Serializable;
import java.util.*;

import com.huigou.util.ConfigFileVersion;

/**
 * Properties文件读取映射模型
 * 
 * @author xx
 */
public class PropertiesModel implements Serializable, ConfigFileVersion {

    private static final long serialVersionUID = -8848043994010647581L;

    private Properties props;

    private Long versions;

    private List<String> configFilePaths;

    public PropertiesModel(Properties props) {
        this.props = props;
    }

    @Override
    public Long getVersion() {
        return versions;
    }

    public void setVersions(Long versions) {
        this.versions = versions;
    }

    public void setConfigFilePath(String configFilePath) {
        this.configFilePaths = Collections.singletonList(configFilePath);
    }

    @Override
    public String getFilePath() {
        return configFilePaths.get(0);
    }

    public void setConfigFilePaths(List<String> configFilePaths) {
        this.configFilePaths = configFilePaths;
    }

    @Override
    public List<String> getFilePaths() {
        return configFilePaths;
    }

    public Properties getProps() {
        return props;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>(props.size());
        Set<Object> keys = props.keySet();// 返回属性key的集合
        for (Object key : keys) {
            map.put(key.toString(), props.get(key).toString());
        }
        return map;
    }

}
