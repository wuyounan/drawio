package com.huigou.data.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;

public interface JDBCDao {
    /**
     * 正则表达式去除空格，制表符及换行符
     */
    static final Pattern pattern = Pattern.compile("\t|\r|\n");

    DataSource getDataSource();

    /**
     * 执行查询返回List<Object[]>
     * 
     * @param sql
     * @param args
     * @return
     * @throws SQLException
     */
    List<Object[]> executeQuery(String sql, Object... args);

    /**
     * 执行带参数的查询方法返回一个 List<T>
     * 
     * @param sql
     * @param args
     * @param cls
     * @return
     * @throws Exception
     */
    <T> List<T> queryToList(String sql, Class<T> cls, Object... args);

    /**
     * 执行带参数的查询方法返回一个 List<T>
     * 
     * @param sql
     * @param cls
     * @param param
     * @return
     * @throws Exception
     */
    <T> List<T> queryToListByMapParam(String sql, Class<T> cls, Map<String, Object> param);

    /**
     * 执行带参数的查询方法返回一个List,通过mapper组合查询结果
     * 
     * @param sql
     * @param mapper
     * @param args
     * @return
     * @throws Exception
     */
    List<?> queryToListByMapper(String sql, QueryRowMapper<?> mapper, Object... args);

    /**
     * 执行带参数的查询方法返回一个List,通过mapper组合查询结果
     * 
     * @author
     * @param sql
     * @param mapper
     * @param param
     * @return
     * @throws
     */
    List<?> queryToListByMapperMapParam(String sql, QueryRowMapper<?> mapper, Map<String, Object> param);

    /**
     * 执行带参数的查询方法返回一个List<Map<String,Object>>
     * 
     * @param sql
     * @param mapper
     * @param args
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> queryToListMap(String sql, Object... args);

    /**
     * 执行MAP参数的查询方法返回一个List<Map<String,Object>>
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> queryToMapListByMapParam(String sql, Map<String, Object> param);

    /**
     * 执行带参数的查询方法返回一个Map<String,Object>
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    Map<String, Object> queryToMap(String sql, Object... args);

    /**
     * 执行带参数的查询方法返回一个Map<String,Object>,只查询一条记录
     * 
     * @param sql
     * @param args
     * @return
     */
    Map<String, Object> queryOneToMap(String sql, Object... args);

    /**
     * 执行带参数的查询方法返回一个Map<String,Object>
     * 
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    Map<String, Object> queryToMapByMapParam(String sql, Map<String, Object> params);

    /**
     * 执行带参数的查询方法返回一个Map<String,Object>,只查询一条记录
     * 
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    Map<String, Object> queryOneToMapByMapParam(String sql, Map<String, Object> params);

    /**
     * 执行带参数查询方法返回一个object
     * 
     * @param sql
     * @param args
     * @param cls
     * @return
     * @throws Exception
     */
    <T> T queryToObject(String sql, Class<T> cls, Object... args);

    /**
     * 执行带参数查询方法返回一个object,只查询一条数据
     * 
     * @param sql
     * @param cls
     * @param args
     * @return
     */
    <T> T queryOneToObject(String sql, Class<T> cls, Object... args);

    /**
     * 执行带参数查询方法返回一个object
     * 
     * @param sql
     * @param cls
     * @param param
     * @return
     * @throws Exception
     */
    <T> T queryToObjectByMapParam(String sql, Class<T> cls, Map<String, Object> param);

    /**
     * 执行带参数查询方法返回一个object,只查询一条数据
     * 
     * @param sql
     * @param cls
     * @param param
     * @return
     */
    <T> T queryOneToObjectByMapParam(String sql, Class<T> cls, Map<String, Object> param);

    /**
     * 执行查询方法返回一个INT
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    int queryToInt(String sql, Object... args);

    /**
     * 查询orcale Sequence
     * 
     * @param name
     * @return
     * @throws Exception
     */
    Long getSequence(String name);

    /**
     * 执行查询方法返回一个Long
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    Long queryToLong(String sql, Object... args);

    /**
     * 执行查询方法返回一个String
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    String queryToString(String sql, Object... args);

    /**
     * 执行带参数的SQL
     * 
     * @param sql
     * @param args
     * @throws SQLException
     */
    int executeUpdate(String sql, Object... args);

    /**
     * 执行带参数的SQL
     * 
     * @param sql
     * @param param
     * @return
     */
    int executeUpdateByMapParam(String sql, Map<String, Object> param);

    /**
     * 执行批量SQL
     * 
     * @param sql
     * @throws SQLException
     */
    void batchUpdate(String... sql);

    /**
     * 批量执行一条SQL語句
     * 
     * @param sql
     * @param args
     * @throws SQLException
     * @例子： String sql = "update table1 set column1=?, column2=? where id=?";
     *      List<Object[]> dataSet = { { "value11", "value12", 1 }, 第一次執行的參數 {
     *      "value21", * "value22", 2 },第二次執行的參數 { "value31", "value32", 3 } };
     *      //第三次執行的參數
     */

    void batchUpdate(String sql, final List<Object[]> dataSet);

    /**
     * 执行储存过程
     * 
     * @param sql
     * @param declaredParameters
     * @param params
     * @return
     */
    Map<String, Object> call(String sql, List<SqlParameter> declaredParameters, Object... param);

    /**
     * 保存clob字段
     * 
     * @author
     * @param sql
     * @param data
     * @param param
     */
    void saveClob(String sql, final String data, final Object... param);

    /**
     * 从数据库中查询clob字段
     * 
     * @author
     * @param sql
     * @param clobNames
     * @param param
     */
    Map<String, Object> loadClob(String sql, final String[] clobNames, final Object... param);

    /**
     * 从数据库中查询clob字段
     * 
     * @author
     * @param sql
     * @param param
     * @return
     */
    String loadClob(String sql, final Object... param);

    String getParseSqlByMapParam(String sql, Map<String, Object> param);
}
