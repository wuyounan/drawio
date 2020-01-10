package com.huigou.data.query.model;

import com.huigou.data.query.XMLParseUtil;
import com.huigou.exception.NotFoundException;
import com.huigou.uasp.bmp.query.QueryDocument.Query;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 查询模型描述符
 *
 * @author xx
 */

public class QueryDescriptor {

    private List<Query> queries;

    public QueryDescriptor(Query query) {
        this.queries = Collections.singletonList(query);
    }

    /**
     * @since 1.1.3
     */
    public QueryDescriptor(List<Query> queries) {
        this.queries = queries;
    }

    /**
     * 根据sqlName获取 Query对象。
     *
     * @param sqlName sql名称
     * @return Query对象
     * @since 1.1.3
     */
    public Query getQuery(String sqlName) {
        return queries.stream()
                .filter(query -> Arrays.stream(query.getSqlArray()).filter(sql -> sql.getName().equals(sqlName)).findFirst().isPresent())
                .findFirst()
                .get();
    }

    public Query getQuery() {
        // 优先从方言xml中取
        return queries.stream()
                .filter(query -> StringUtils.isNotBlank(query.getSqlQuery()))
                .findFirst().get();
    }

    public void setQuery(Query query) {
        this.queries = Collections.singletonList(query);
    }

    public String getName() {
        return getQuery().getName();
    }

    public String getLabel() {
        return getQuery().getLabel();
    }

    public String getSql() {
        return getQuery().getSqlQuery();
    }

    public String getSqlByName(String sqlName) {
        Query query = getQuery(sqlName);
        if (query == null) {
            throw new NotFoundException(String.format("实体%s中没有SQL(%s)的配置!", queries.get(0).getName(), sqlName));
        }
        return Arrays.stream(query.getSqlArray()).filter(sql -> sql.getName().equals(sqlName))
                .filter(Objects::nonNull)
                .map(XMLParseUtil::getNodeTextValue)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("实体%s中没有SQL(%s)的配置!", queries.get(0).getName(), sqlName)));
    }

}
