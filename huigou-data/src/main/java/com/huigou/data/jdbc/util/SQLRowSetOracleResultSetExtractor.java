package com.huigou.data.jdbc.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import oracle.jdbc.rowset.OracleCachedRowSet;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * 在使用Spring中使用JdbcTemplate.queryForRowSet()方法时，抛出了SQLException:Invalid scale
 * size. Cannot be less than zero 异常。报这个异常情况如下：
 * 数据库环境为oracle而且使用了RowSet时。具体原因是由于“oracle驱动面对一个数值型的返回字段时，在得到指定的字段小数点右边的数值数量时（Gets
 * the designated column's number of digits to right of the decimal
 * point.这个是原文），居然会返回-127，而oracle本身的cacheRowSet实现不允许这种情况出现，于是就会报标题所说的异常。
 */
@SuppressWarnings("rawtypes")
public class SQLRowSetOracleResultSetExtractor implements ResultSetExtractor {
    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
        return createSqlRowSet(rs);
    }

    protected SqlRowSet createSqlRowSet(ResultSet rs) throws SQLException {
        CachedRowSet rowSet = newCachedRowSet();
        rowSet.populate(rs);
        return new ResultSetWrappingSqlRowSet(rowSet);
    }

    /**
     * Create a new CachedRowSet instance, to be populated by the
     * createSqlRowSet implementation. This implementation creates a new
     * instance of Oracle's oracle.jdbc.rowset.OracleCachedRowSet class, which
     * is their implementation of the Java 1.5 CachedRowSet interface.
     * 
     * @return a new CachedRowSet instance
     * @throws SQLException
     *             if thrown by JDBC methods
     * @see #createSqlRowSet
     * @see oracle.jdbc.rowset.OracleCachedRowSet
     */
    protected CachedRowSet newCachedRowSet() throws SQLException {
        return new OracleCachedRowSet();
    }
}
