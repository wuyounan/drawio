package com.huigou.data.query;

import com.huigou.uasp.bmp.query.QueryDocument.Query;
import com.huigou.uasp.bmp.query.QueryMappingsDocument.QueryMappings;
import com.huigou.util.ConfigFileVersion;

import java.io.Serializable;
import java.util.*;
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
    private final List<Map<String, Query>> querys;

    /**
     * 版本
     */
    private Long version;

    private List<String> configFilePaths;

    public QueryXmlModel(QueryMappings queryMapping) {
        this(Collections.singletonList(queryMapping));
    }

    /**
     * @since 1.1.3
     */
    public QueryXmlModel(List<QueryMappings> queryMappings) {
        querys = queryMappings.stream()
                .filter(Objects::nonNull)
                .map(queryMapping -> Arrays.stream(queryMapping.getQueryArray()).collect(Collectors.toMap(Query::getName, query -> query)))
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Deprecated
    public Map<String, Query> getQuerys() {
        return querys.get(0);
    }

    /**
     * @deprecated 已被 {@link #getDeclaredQueries(String)} 替代。
     */
    @Deprecated
    public Query getQuery(String name) {
        return querys.stream().map(query -> query.get(name))
                .filter(Objects::nonNull)
                .findFirst().get();
    }

    /**
     * 获取所有同名的query。
     *
     * @param name queryName
     * @since 1.1.3
     */
    public List<Query> getDeclaredQueries(String name) {
        return querys.stream().map(query -> query.get(name))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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
        return configFilePaths.get(0);
    }

    /**
     * @deprecated 已被 {@link #setConfigFilePaths(List)} 替代。
     */
    @Deprecated
    public void setConfigFilePath(String configFilePath) {
        this.configFilePaths = Collections.singletonList(configFilePath);
    }

    @Override
    public List<String> getFilePaths() {
        return configFilePaths;
    }

    public void setConfigFilePaths(List<String> configFilePaths) {
        this.configFilePaths = configFilePaths;
    }
}
