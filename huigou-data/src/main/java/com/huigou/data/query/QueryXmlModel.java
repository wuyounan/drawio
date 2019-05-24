package com.huigou.data.query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.huigou.uasp.bmp.query.QueryDocument.Query;
import com.huigou.uasp.bmp.query.QueryMappingsDocument.QueryMappings;
import com.huigou.util.ConfigFileVersion;

/**
 * 查询模型管理类
 */
public class QueryXmlModel implements Serializable, ConfigFileVersion {

    private static final long serialVersionUID = 2459794271514778093L;

    /**
     * 名称
     */
    private String name;

    /**
     * 实体对象
     */
    private Map<String, Query> querys;

    /**
     * 版本
     */
    private Long version;

    private String configFilePath;

    public QueryXmlModel(QueryMappings queryMappings) {
        querys = new HashMap<String, Query>(queryMappings.getQueryArray().length);
        for (Query query : queryMappings.getQueryArray()) {
            querys.put(query.getName(), query);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Query> getQuerys() {
        return querys;
    }

    public Query getQuery(String name) {
        return querys.get(name);
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    public String getFilePath() {
        return configFilePath;
    }

    public void setConfigFilePath(String configFilePath) {
        this.configFilePath = configFilePath;
    }

}
