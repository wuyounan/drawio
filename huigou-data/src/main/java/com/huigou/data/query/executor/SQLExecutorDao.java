package com.huigou.data.query.executor;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.QueryAbstractRequest;
import com.huigou.data.exception.EntityExecutorException;
import com.huigou.data.jdbc.JDBCDao;
import com.huigou.data.jdbc.SQLQuery;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.data.query.parser.SQLBuilder;

public interface SQLExecutorDao extends JDBCDao {
    SQLBuilder getSqlBuilder();

    SQLQuery getSqlQuery();

    /**
     * 获取查询描述
     * 
     * @param queryFilePath
     *            查询文件路径
     * @param queryName
     *            查询名称
     * @return 查询描述
     */
    QueryDescriptor getQuery(String queryFilePath, String queryName);

    /**
     * 执行查询
     * 
     * @param queryDescriptor
     *            查询描述
     * @param queryRequest
     *            查询请求参数
     * @param cls
     *            返回类型
     * @return 查询结果
     * @throws EntityExecutorException
     *             实体执行异常
     */
    public <T> List<T> query(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest, Class<T> cls) throws EntityExecutorException;

    /**
     * 执行查询
     * <p>
     * 根据SQLName获取配置在实体中的sql语句
     * <p>
     * 查询操作
     * 
     * @param queryDescriptor
     *            查询描述
     * @param queryRequest
     *            查询请求参数
     * @param sqlName
     *            SQL名称
     * @return 查询结果
     * @throws EntityExecutorException
     *             实体执行异常
     */
    List<Map<String, Object>> query(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest, String sqlName) throws EntityExecutorException;

    /**
     * 执行查询
     * 
     * @param queryDescriptor
     *            查询描述
     * @param sqlName
     *            SQL名称
     * @param cls
     *            返回类型
     * @param args
     *            查询数组
     * @return
     * @throws EntityExecutorException
     *             实体执行异常
     */
    <T> T query(QueryDescriptor queryDescriptor, String sqlName, Class<T> cls, Object... args) throws EntityExecutorException;

    /**
     * 获取查询模型
     * 
     * @param queryDescriptor
     *            查询描述
     * @param queryRequest
     *            查询请求参数
     * @return
     *         查询模型
     * @throws EntityExecutorException
     *             实体执行异常
     */
    QueryModel getQueryModel(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest) throws EntityExecutorException;

    /**
     * 获取待查询模型
     * 
     * @param queryDescriptor
     *            查询描述
     * @param queryRequest
     *            查询请求参数
     * @param sqlName
     *            SQL名称
     * @return
     *         查询模型
     * @throws EntityExecutorException
     *             实体执行异常
     */
    QueryModel getQueryModel(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest, String sqlName) throws EntityExecutorException;

    /**
     * 执行查询
     * 
     * @param queryModel
     *            查询模型
     * @return 查询结果
     */
    Map<String, Object> executeQuery(QueryModel queryModel);

    /**
     * 执行查询
     * 
     * @param queryDescriptor
     *            查询描述
     * @param queryRequest
     *            查询请求参数
     * @return 查询结果
     */
    Map<String, Object> executeQuery(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest);

    /**
     * 执行查询
     * 
     * @param queryDescriptor
     *            查询描述
     * @param queryRequest
     *            查询请求参数
     * @param sqlName
     *            SQL名称
     * @return 查询结果
     */
    Map<String, Object> executeQuery(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest, String sqlName);

    /**
     * 执行分页查询
     * 
     * @param queryModel
     *            查询模型
     * @return 查询结果
     */
    Map<String, Object> executeSlicedQuery(QueryModel queryModel);

    /**
     * 执行分页查询
     * 
     * @param queryDescriptor
     *            查询描述
     * @param queryRequest
     *            查询请求参数
     * @return 查询结果
     */
    Map<String, Object> executeSlicedQuery(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest);

    /**
     * 执行分页查询
     * 
     * @param queryDescriptor
     *            查询描述
     * @param queryRequest
     *            查询请求参数
     * @param sqlName
     *            SQL名称
     * @return 查询结果
     */
    Map<String, Object> executeSlicedQuery(QueryDescriptor queryDescriptor, QueryAbstractRequest queryRequest, String sqlName);

}
