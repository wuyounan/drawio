package com.huigou.data.query.executor.impl;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.QueryAbstractRequest;
import com.huigou.data.exception.EntityExecutorException;
import com.huigou.data.jdbc.SQLQuery;
import com.huigou.data.jdbc.impl.JDBCDaoImpl;
import com.huigou.data.query.QueryXmlLoadInterface;
import com.huigou.data.query.QueryXmlModel;
import com.huigou.data.query.SQLExecutor;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.data.query.parser.SQLBuilder;

/**
 * 解析查询对象为数据操作SQL
 * 
 * @author xx
 * @date 2017-1-25 下午03:20:53
 * @version V1.0
 */
public class SQLExecutorDaoImpl extends JDBCDaoImpl implements SQLExecutorDao {

    private SQLBuilder sqlBuilder;

    private SQLQuery sqlQuery;

    private QueryXmlLoadInterface queryXmlManager;

    public void setSqlBuilder(SQLBuilder sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }

    public void setSqlQuery(SQLQuery sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public void setQueryXmlManager(QueryXmlLoadInterface queryXmlManager) {
        this.queryXmlManager = queryXmlManager;
    }

    public SQLBuilder getSqlBuilder() {
        return sqlBuilder;
    }

    public SQLQuery getSqlQuery() {
        return sqlQuery;
    }

    private QueryXmlModel getQueryXmlModel(String modelFilePath) {
        return queryXmlManager.loadConfigFile(modelFilePath);
    }

    public QueryDescriptor getQuery(String queryFilePath, String queryName) {
        QueryXmlModel model = getQueryXmlModel(queryFilePath);
        QueryDescriptor queryDescriptor = new QueryDescriptor(model.getQuery(queryName));
        return queryDescriptor;
    }

    @Override
    public <T> List<T> query(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest, Class<T> cls) throws EntityExecutorException {
        SQLExecutor executor = sqlBuilder.buildQuerySql(queryDescriptor.getQuery(), queryRequest);
        Map<String, Object> map = executor.parseParamMap();
        return this.queryToListByMapParam(executor.getSql(), cls, map);
    }

    @Override
    public List<Map<String, Object>> query(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest, String sqlName) throws EntityExecutorException {
        SQLExecutor executor = sqlBuilder.buildSqlByName(queryDescriptor.getQuery(), sqlName, queryRequest);
        Map<String, Object> map = executor.parseParamMap();
        return this.queryToMapListByMapParam(executor.getSql(), map);
    }

    @Override
    public <T> T query(QueryDescriptor queryDescriptor, String sqlName, Class<T> cls, Object... args) throws EntityExecutorException {
        return this.queryToObject(queryDescriptor.getSqlByName(sqlName), cls, args);
    }

    @Override
    public QueryModel getQueryModel(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest) throws EntityExecutorException {
        SQLExecutor executor = sqlBuilder.buildQuerySql(queryDescriptor.getQuery(), queryRequest);
        QueryModel queryModel = queryRequest.initQueryModel();
        queryModel.setQueryParams(executor.parseParamMap());
        queryModel.setSql(executor.getSql());
        return queryModel;
    }

    @Override
    public QueryModel getQueryModel(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest, String sqlName) throws EntityExecutorException {
        SQLExecutor executor = sqlBuilder.buildSqlByName(queryDescriptor.getQuery(), sqlName, queryRequest);
        QueryModel queryModel = queryRequest.initQueryModel();
        queryModel.setQueryParams(executor.parseParamMap());
        queryModel.setSql(executor.getSql());
        return queryModel;
    }

    @Override
    public Map<String, Object> executeQuery(QueryModel queryModel) {
        return sqlQuery.executeQuery(queryModel);
    }

    @Override
    public Map<String, Object> executeQuery(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest) {
        QueryModel queryModel = this.getQueryModel(queryDescriptor, queryRequest);
        return sqlQuery.executeQuery(queryModel);
    }

    @Override
    public Map<String, Object> executeQuery(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest, String sqlName) {
        QueryModel queryModel = this.getQueryModel(queryDescriptor, queryRequest, sqlName);
        return sqlQuery.executeQuery(queryModel);
    }

    @Override
    public Map<String, Object> executeSlicedQuery(QueryModel queryModel) {
        return sqlQuery.executeSlicedQuery(queryModel);
    }

    @Override
    public Map<String, Object> executeSlicedQuery(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest) {
        QueryModel queryModel = this.getQueryModel(queryDescriptor, queryRequest);
        return sqlQuery.executeSlicedQuery(queryModel);
    }

    @Override
    public Map<String, Object> executeSlicedQuery(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest, String sqlName) {
        QueryModel queryModel = this.getQueryModel(queryDescriptor, queryRequest, sqlName);
        return sqlQuery.executeSlicedQuery(queryModel);
    }
}
