package com.huigou.data.jdbc.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.huigou.data.dialect.Dialect;
import com.huigou.data.dialect.DialectUtils;
import com.huigou.data.exception.SQLParseException;
import com.huigou.data.jdbc.JDBCDao;
import com.huigou.data.jdbc.QueryRowMapper;
import com.huigou.data.jdbc.util.JDBCCallableStatementCreator;
import com.huigou.data.jdbc.util.OrcaleLobResultSetExtractor;
import com.huigou.data.jdbc.util.ParseSQLParam;
import com.huigou.data.jdbc.util.RowSetUtil;
import com.huigou.data.jdbc.util.SQLRowSetOracleResultSetExtractor;
import com.huigou.exception.ApplicationException;
import com.huigou.util.ClassHelper;
import com.huigou.util.LogHome;

public class JDBCDaoImpl implements JDBCDao {

    private JdbcTemplate jdbcTemplate;

    private LobHandler lobHandler;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setLobHandler(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    public DataSource getDataSource() {
        return jdbcTemplate.getDataSource();
    }

    public ResultSetExtractor<?> getResultSetExtractor() {
        Dialect dialect = DialectUtils.guessDialect(this.getDataSource());
        if (dialect.isOracleFamily()) {
            return new SQLRowSetOracleResultSetExtractor();
        }
        return new SqlRowSetResultSetExtractor();
    }

    /**
     * 执行查询返回List<Object[]>
     * 
     * @param sql
     * @param args
     * @return
     * @throws SQLException
     */
    public List<Object[]> executeQuery(String sql, Object... args) {
        toSqlString(sql, args);
        SqlRowSet srs = (SqlRowSet) jdbcTemplate.query(sql, args, getResultSetExtractor());
        return RowSetUtil.toArrayList(srs);
    }

    /**
     * 执行带参数的查询方法返回一个 List<?>
     * 
     * @param sql
     * @param cls
     * @param args
     * @return
     * @throws Exception
     */
    public <T> List<T> queryToList(String sql, Class<T> cls, Object... args) {
        toSqlString(sql, args);
        SqlRowSet srs = (SqlRowSet) jdbcTemplate.query(sql, args, getResultSetExtractor());
        return RowSetUtil.toList(srs, cls);
    }

    /**
     * 执行带参数的查询方法返回一个 List<?>
     * 
     * @param sql
     * @param cls
     * @param param
     * @return
     * @throws Exception
     */
    public <T> List<T> queryToListByMapParam(String sql, Class<T> cls, Map<String, Object> param) {
        ParseSQLParam parser = this.getParseSqlParam(sql, param);
        return queryToList(parser.getParseSql(), cls, parser.getValues().toArray());
    }

    /**
     * 执行带参数的查询方法返回一个List,通过mapper组合查询结果
     * 
     * @param sql
     * @param mapper
     * @param args
     * @return
     * @throws Exception
     */
    public List<?> queryToListByMapper(String sql, QueryRowMapper<?> mapper, Object... args) {
        toSqlString(sql, args);
        return jdbcTemplate.query(sql, args, mapper);
    }

    /**
     * 执行带参数的查询方法返回一个List,通过mapper组合查询结果
     * 
     * @author
     * @param sql
     * @param mapper
     * @param param
     * @return
     * @throws ApplicationException
     * @throws
     */
    public List<?> queryToListByMapperMapParam(String sql, QueryRowMapper<?> mapper, Map<String, Object> param) {
        ParseSQLParam parser = this.getParseSqlParam(sql, param);
        return queryToListByMapper(parser.getParseSql(), mapper, parser.getValues().toArray());
    }

    /**
     * 执行带参数的查询方法返回一个List<Map<String,Object>>
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> queryToListMap(String sql, Object... args) {
        toSqlString(sql, args);
        SqlRowSet srs = (SqlRowSet) jdbcTemplate.query(sql, args, getResultSetExtractor());
        return RowSetUtil.toMapList(srs);
    }

    /**
     * 执行MAP参数的查询方法返回一个List<Map<String,Object>>
     * 
     * @param sql
     * @param param
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> queryToMapListByMapParam(String sql, Map<String, Object> param) {
        ParseSQLParam parser = this.getParseSqlParam(sql, param);
        return queryToListMap(parser.getParseSql(), parser.getValues().toArray());
    }

    /**
     * 执行带参数的查询方法返回一个Map<String,Object>
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    public Map<String, Object> queryToMap(String sql, Object... args) {
        toSqlString(sql, args);
        SqlRowSet srs = (SqlRowSet) jdbcTemplate.query(sql, args, getResultSetExtractor());
        return RowSetUtil.toMap(srs);
    }

    /**
     * 执行带参数的查询方法返回一个Map<String,Object>,只查询一条记录
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    public Map<String, Object> queryOneToMap(String sql, Object... args) {
        Dialect d = DialectUtils.guessDialect(this.getDataSource());
        String pageSql = d.getQueryFirstSql(sql);
        toSqlString(pageSql, args);
        SqlRowSet srs = (SqlRowSet) jdbcTemplate.query(pageSql, args, getResultSetExtractor());
        return RowSetUtil.toMap(srs);
    }

    /**
     * 执行带参数的查询方法返回一个Map<String,Object>
     * 
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    public Map<String, Object> queryToMapByMapParam(String sql, Map<String, Object> params) {
        ParseSQLParam parser = this.getParseSqlParam(sql, params);
        return queryToMap(parser.getParseSql(), parser.getValues().toArray());
    }

    /**
     * 执行带参数的查询方法返回一个Map<String,Object>,只查询一条记录
     * 
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    public Map<String, Object> queryOneToMapByMapParam(String sql, Map<String, Object> params) {
        ParseSQLParam parser = this.getParseSqlParam(sql, params);
        return queryOneToMap(parser.getParseSql(), parser.getValues().toArray());
    }

    /**
     * 执行带参数查询方法返回一个object
     * 
     * @param sql
     * @param args
     * @param cls
     * @return
     * @throws Exception
     */
    public <T> T queryToObject(String sql, Class<T> cls, Object... args) {
        toSqlString(sql, args);
        SqlRowSet srs = (SqlRowSet) jdbcTemplate.query(sql, args, getResultSetExtractor());
        return RowSetUtil.toObject(srs, cls);
    }

    /**
     * 执行带参数查询方法返回一个object,只查询一条数据
     * 
     * @param sql
     * @param cls
     * @param args
     * @return
     */
    public <T> T queryOneToObject(String sql, Class<T> cls, Object... args) {
        Dialect d = DialectUtils.guessDialect(this.getDataSource());
        String pageSql = d.getQueryFirstSql(sql);
        toSqlString(pageSql, args);
        SqlRowSet srs = (SqlRowSet) jdbcTemplate.query(pageSql, args, getResultSetExtractor());
        return RowSetUtil.toObject(srs, cls);
    }

    /**
     * 执行带参数查询方法返回一个object
     * 
     * @param sql
     * @param cls
     * @param param
     * @return
     * @throws Exception
     */
    public <T> T queryToObjectByMapParam(String sql, Class<T> cls, Map<String, Object> param) {
        ParseSQLParam parser = this.getParseSqlParam(sql, param);
        return queryToObject(parser.getParseSql(), cls, parser.getValues().toArray());
    }

    /**
     * 执行带参数查询方法返回一个object,只查询一条数据
     * 
     * @param sql
     * @param cls
     * @param param
     * @return
     */
    public <T> T queryOneToObjectByMapParam(String sql, Class<T> cls, Map<String, Object> param) {
        ParseSQLParam parser = this.getParseSqlParam(sql, param);
        return queryOneToObject(parser.getParseSql(), cls, parser.getValues().toArray());
    }

    /**
     * 执行查询方法返回一个INT
     * 
     * @param sql
     * @param args
     * @return
     * @throws ApplicationException
     */
    public int queryToInt(String sql, Object... args) {
        int result = 0;
        Object object = queryToObject(sql, Integer.class, args);
        if (object != null && !object.toString().equals("") && !object.toString().equalsIgnoreCase("null")) {
            result = Integer.parseInt(object.toString());
        }
        return result;
    }

    /**
     * 查询orcale Sequence
     * 
     * @param name
     * @return
     * @throws Exception
     */
    @Override
    public Long getSequence(String name) {
        String sql = "select " + name + ".nextval from dual";
        SqlRowSet srs = (SqlRowSet) jdbcTemplate.query(sql, getResultSetExtractor());
        return RowSetUtil.toObject(srs, Long.class);
    }

    /**
     * 执行查询方法返回一个Long
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    public Long queryToLong(String sql, Object... args) {
        Long a = null;
        Object object = queryToObject(sql, Long.class, args);
        if (object != null && !object.toString().equals("") && !object.toString().equalsIgnoreCase("null")) {
            a = Long.parseLong(object.toString());
        }
        return a;
    }

    /**
     * 执行查询方法返回一个String
     * 
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    public String queryToString(String sql, Object... args) {
        return queryToObject(sql, String.class, args);
    }

    /**
     * 执行带参数的SQL
     * 
     * @param sql
     * @param args
     * @throws SQLException
     */
    public int executeUpdate(String sql, Object... args) {
        toSqlString(sql, args);
        return jdbcTemplate.update(sql, args);
    }

    /**
     * 执行带参数的SQL
     * 
     * @param sql
     * @param param
     * @throws SQLException
     */
    public int executeUpdateByMapParam(String sql, Map<String, Object> param) {
        ParseSQLParam parser = this.getParseSqlParam(sql, param);
        return executeUpdate(parser.getParseSql(), parser.getValues().toArray());
    }

    /**
     * 执行批量SQL
     * 
     * @param sql
     * @throws SQLException
     */
    public void batchUpdate(String... sql) {
        jdbcTemplate.batchUpdate(sql);
    }

    /**
     * 批量执行一条SQL語句
     * 
     * @param sql
     * @param dataSet
     * @throws SQLException
     * @例子： String sql = "update table1 set column1=?, column2=? where id=?";
     *      List<Object[]> dataSet = { { "value11", "value12", 1 }, 第一次执行的参数 {
     *      "value21", * "value22", 2 },第二次执行的参数 { "value31", "value32", 3 } };
     *      //第三次执行的参数
     */
    public void batchUpdate(String sql, final List<Object[]> dataSet) {
        BatchPreparedStatementSetter setter = new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return dataSet.size();
            }

            public void setValues(PreparedStatement ps, int i) {
                Object[] obj = dataSet.get(i);
                try {
                    RowSetUtil.setStatementParams(ps, obj);
                } catch (SQLException e) {
                    LogHome.getLog().error(e);
                    e.printStackTrace();
                }
            }
        };
        toSqlStringBatch(sql, dataSet);
        jdbcTemplate.batchUpdate(sql, setter);
    }

    /**
     * 执行储存过程
     * 
     * @param sql
     * @param declaredParameters
     * @param param
     * @return
     */
    public Map<String, Object> call(String sql, List<SqlParameter> declaredParameters, Object... param) {
        toSqlString(sql, param);
        return jdbcTemplate.call(new JDBCCallableStatementCreator(sql, param, declaredParameters), declaredParameters);
    }

    /**
     * 记录SQL执行日志
     * 
     * @param sql
     * @param args
     * @return
     * @throws SQLException
     */
    private String toSqlString(String sql, Object[] args) {
        String str = sql;
        try {
            if (args != null) {
                Object arg = null;
                for (int i = 0; i < args.length; i++) {
                    arg = args[i];
                    try {
                        if (arg instanceof Number) {
                            str = str.replaceFirst("\\?", ClassHelper.convert(args[i], String.class));
                        } else {
                            str = str.replaceFirst("\\?", "'" + ClassHelper.convert(args[i], String.class) + "'");
                        }
                    } catch (PatternSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            LogHome.getLog().error(e);
        }
        Matcher matcher = pattern.matcher(str);
        str = matcher.replaceAll("");
        LogHome.getLog().info(str);
        return str;
    }

    /**
     * 批量执行的SQL日志
     * 
     * @param sql
     * @param dataSet
     * @return
     * @throws Exception
     */
    private String toSqlStringBatch(String sql, List<Object[]> dataSet) {
        String str = "";
        if (dataSet != null) {
            for (@SuppressWarnings("unused")
            Object[] objects : dataSet) {
                // str = toSqlString(sql, objects) + ";" + str;
            }
        } else {
            LogHome.getLog().info(sql);
        }
        return str;
    }

    /**
     * 解析带参数格式的SQL
     * 
     * @Title: getParseSqlParam
     * @author
     * @param @param sql
     * @param @param param
     * @param @return
     * @param @throws Exception
     * @return ParseSqlParam
     * @throws
     */
    private ParseSQLParam getParseSqlParam(String sql, Map<String, Object> param) throws SQLParseException {
        ParseSQLParam parser = new ParseSQLParam();
        parser.parse(sql);
        List<String> names = parser.getParameter();
        Object obj = null;
        for (String name : names) {
            obj = param.get(name);
            if (obj == null) {
                throw new SQLParseException("未找到参数:" + name + " 对应的数据！");
            }
            parser.addValue(obj);
        }
        return parser;
    }

    public String getParseSqlByMapParam(String sql, Map<String, Object> param) {
        ParseSQLParam parser = new ParseSQLParam();
        parser.parse(sql);
        List<String> names = parser.getParameter();
        Object obj = null;
        for (String name : names) {
            obj = param.get(name);
            if (obj == null) {
                parser.addValue("");
            } else {
                parser.addValue(obj);
            }
        }
        return toSqlString(parser.getParseSql(), parser.getValues().toArray());
    }

    @Override
    public void saveClob(String sql, final String data, final Object... param) {
        jdbcTemplate.execute(sql, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
            protected void setValues(PreparedStatement pstmt, LobCreator lobCreator) throws SQLException, DataAccessException {
                lobCreator.setClobAsString(pstmt, 1, data);
                int i = 2;
                for (Object o : param) {
                    StatementCreatorUtils.setParameterValue(pstmt, i, -2147483648, o);
                    i++;
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> loadClob(String sql, final String[] clobNames, final Object... param) {
        OrcaleLobResultSetExtractor rse = new OrcaleLobResultSetExtractor(lobHandler, clobNames);
        jdbcTemplate.query(sql, rse, param);
        return rse.getMap();
    }

    @SuppressWarnings("unchecked")
    @Override
    public String loadClob(String sql, final Object... param) {
        OrcaleLobResultSetExtractor rse = new OrcaleLobResultSetExtractor(lobHandler);
        jdbcTemplate.query(sql, rse, param);
        return rse.getClobString();
    }

}
