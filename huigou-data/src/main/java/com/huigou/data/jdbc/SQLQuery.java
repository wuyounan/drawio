package com.huigou.data.jdbc;

import java.util.List;
import java.util.Map;

import com.huigou.data.query.model.QueryModel;

public interface SQLQuery {

    public JDBCDao getJDBCDao();

    /**
     * 执行分页查询
     * 
     * @param queryModel
     * @param params
     * @return
     */

    Map<String, Object> executeSlicedQuery(QueryModel queryModel);

    /**
     * 执行查询
     * 
     * @param queryModel
     *            查询模型
     * @return
     */
    Map<String, Object> executeQuery(QueryModel queryModel);

    /**
     * 导出查询
     * 
     * @author
     * @param queryModel
     * @return
     * @throws
     */
    Map<String, Object> exportExecuteQuery(QueryModel queryModel);

    /**
     * 查询记录总数
     * 
     * @param sql
     * @param param
     * @return
     */
    int getTotal(String sql, Map<String, Object> param);

    /**
     * 组合分页SQL 执行查询
     * 
     * @Title: queryListForPaging
     * @param sql
     * @param param
     * @param pageIndex
     * @param pageSize
     * @return List<Map<String,Object>>
     */
    List<Map<String, Object>> queryListForPaging(final String sql, final Map<String, Object> param, int pageIndex, int pageSize);

    /**
     * 组合查询合计SQL并执行查询
     * 
     * @param sql
     * @param totalFields
     * @param param
     * @return
     */
    Map<String, Object> queryTotalByFields(String sql, String totalFields, Map<String, Object> param);
}
