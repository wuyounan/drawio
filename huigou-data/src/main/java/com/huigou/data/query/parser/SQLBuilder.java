package com.huigou.data.query.parser;

import com.huigou.data.domain.query.QueryAbstractRequest;
import com.huigou.data.query.SQLExecutor;
import com.huigou.uasp.bmp.query.QueryDocument.Query;

public interface SQLBuilder {

    /**
     * 构造查询SQL语句
     * 
     * @param entity
     *            实体文档对象
     * @param queryRequest
     *            参数
     * @return
     */
    public SQLExecutor buildQuerySql(Query query, QueryAbstractRequest queryRequest);

    /**
     * 根据SQLName获取配置在实体中的sql语句
     * 
     * @author
     * @param entity
     * @param sqlName
     * @return String
     */
    public String getSqlByName(Query query, String sqlName);

    /**
     * 根据SQLName获取配置在实体中的sql语句,并构造SQL
     * 
     * @author
     * @param entity
     * @param sqlName
     * @param queryRequest
     * @return SQLExecutor
     */
    public SQLExecutor buildSqlByName(Query query, String sqlName, QueryAbstractRequest queryRequest);
}