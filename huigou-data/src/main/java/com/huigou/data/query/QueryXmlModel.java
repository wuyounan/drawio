package com.huigou.data.query;

import com.huigou.uasp.bmp.query.QueryDocument.Query;
import com.huigou.uasp.bmp.query.QueryMappingsDocument.QueryMappings;
import com.huigou.util.ConfigFileVersion;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final Map<String, Query> querys;
    /**
     * @since 1.1.3
     */
    private final Map<String, Query> dialectQuerys;

    /**
     * 版本
     */
    private Long version;

    private String configFilePath;

    public QueryXmlModel(QueryMappings queryMappings, QueryMappings dialectQueryMappings) {
        if (queryMappings != null) {
            querys = Arrays.stream(queryMappings.getQueryArray())
                    .collect(Collectors.toMap(Query::getName, query -> query));
        } else {
            querys = Collections.emptyMap();
        }
        if (dialectQueryMappings != null) {
            dialectQuerys = Arrays.stream(dialectQueryMappings.getQueryArray())
                    .collect(Collectors.toMap(Query::getName, query -> query));
        } else {
            dialectQuerys = Collections.emptyMap();
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

    /**
     * @since 1.1.3
     */
    public Map<String, Query> getDialectQuerys() {
        return dialectQuerys;
    }

    public Query getQuery(String name) {
        // 优先从方言中获取
        Query query = dialectQuerys.get(name);
        if (query == null) {
            query = querys.get(name);
        }
        return query;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public String getFilePath() {
        return configFilePath;
    }

    public void setConfigFilePath(String configFilePath) {
        this.configFilePath = configFilePath;
    }

}
