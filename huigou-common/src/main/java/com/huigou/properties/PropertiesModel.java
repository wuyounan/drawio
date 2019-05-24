package com.huigou.properties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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

    private String configFilePath;

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
        this.configFilePath = configFilePath;
    }

    @Override
    public String getFilePath() {
        return configFilePath;
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
