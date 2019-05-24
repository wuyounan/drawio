package com.huigou.data.query.model;

import java.util.HashMap;
import java.util.Map;

/**
 * SQL执行模型 包括 sql及执行需要的参数
 * 
 * @author xx
 */

public class SQLModel {

    /**
     * 查询SQL
     */
    private String sql;

    /**
     * 查询参数
     */
    private Map<String, Object> queryParams;

    public SQLModel() {
        sql = "";
        queryParams = new HashMap<String, Object>(4);
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public Object getParam(String key) {
        return queryParams.get(key);
    }

    public void putParam(String key, Object obj) {
        queryParams.put(key, obj);
    }

    public void putLikeParam(String key, Object obj) {
        queryParams.put(key, "%" + obj + "%");
    }

    public void putStartWithParam(String key, String obj) {
        queryParams.put(key, obj + "%");
    }

    public void putEndWithParam(String key, String obj) {
        queryParams.put(key, "%" + obj);
    }

    public void putAll(Map<String, Object> map) {
        if (map != null) queryParams.putAll(map);
    }

    public void setQueryParams(Map<String, Object> queryParams) {
        this.queryParams = queryParams;
    }

}
