package com.huigou.data.jdbc.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import com.huigou.cache.DictUtil;
import com.huigou.exception.ApplicationException;
import com.huigou.util.ClassHelper;
import com.huigou.util.DateUtil;
import com.huigou.util.LogHome;
import com.huigou.util.StringUtil;

/**
 * 数据集工具类
 * 
 * @author gongmm
 */
public final class RowSetUtil {

    private static final Logger log = LogHome.getLog(RowSetUtil.class.getName());

    /**
     * 数据集转换为对象列表
     * 
     * @param rs
     * @param className
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(SqlRowSet rs, Class<T> cls) {
        List<T> result = new ArrayList<T>(rs.getRow());
        SqlRowSetMetaData rsmd = null;
        try {
            if (rs != null) {
                if (ClassHelper.isBaseType(cls)) {
                    while (rs.next()) {
                        result.add(ClassHelper.convert(rs.getObject(1), cls));
                    }
                } else {
                    int iCol = 0;
                    rsmd = rs.getMetaData();
                    iCol = rsmd.getColumnCount();
                    String[] propertyNameArr = new String[iCol];
                    for (int i = 0; i < iCol; i++) {
                        propertyNameArr[i] = StringUtil.getHumpName(rsmd.getColumnLabel(i + 1).trim());
                    }
                    while (rs.next()) {
                        Object obj = cls.newInstance();
                        for (int i = 0; i < iCol; i++) {
                            ClassHelper.setProperty(obj, propertyNameArr[i], rs.getObject(i + 1));
                        }
                        result.add((T) obj);
                    }
                }
            }
            rsmd = null;
        } catch (IllegalAccessException e) {
            log.error(e);
            throw new ApplicationException(e);
        } catch (InstantiationException e) {
            log.error(e);
            throw new ApplicationException(e);
        } catch (RuntimeException e) {
            log.error(e);
            throw e;
        }
        return result;
    }

    /**
     * 数据集转换为对象
     * 
     * @param rs
     * @param cls
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(SqlRowSet rs, Class<T> cls) {
        Object result = null;
        SqlRowSetMetaData rsmd = null;
        try {
            if (rs != null) {
                if (cls == Object.class) {
                    if (rs.next()) {
                        result = rs.getObject(1);
                    }
                } else if (ClassHelper.isBaseType(cls)) {
                    if (rs.next()) {
                        result = ClassHelper.convert(rs.getObject(1), cls);
                    }
                } else {
                    int iCol = 0;
                    rsmd = rs.getMetaData();
                    iCol = rsmd.getColumnCount();
                    String[] propertyNameArr = new String[iCol];
                    for (int i = 0; i < iCol; i++) {
                        propertyNameArr[i] = StringUtil.getHumpName(rsmd.getColumnLabel(i + 1).trim());
                    }
                    if (rs.next()) {
                        Object obj = cls.newInstance();
                        for (int i = 0; i < iCol; i++) {
                            ClassHelper.setProperty(obj, propertyNameArr[i], rs.getObject(i + 1));
                        }
                        result = obj;
                    }
                }
            }
            rsmd = null;
        } catch (IllegalAccessException e) {
            log.error(e);
            throw new ApplicationException(e);
        } catch (InstantiationException e) {
            log.error(e);
            throw new ApplicationException(e);
        } catch (RuntimeException e) {
            log.error(e);
            throw e;
        }
        return (T) result;
    }

    /**
     * 数据集转换为Map对象
     * 
     * @param rs
     * @return
     * @throws Exception
     */
    public static Map<String, Object> toMap(SqlRowSet rs) {
        SqlRowSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Map<String, Object> map;
        if (rs.next()) {
            map = new HashMap<String, Object>(columnCount);
            for (int i = 0; i < columnCount; i++) {
                Object value = rs.getObject(i + 1);
                String key = StringUtil.getHumpName(rsmd.getColumnLabel(i + 1).trim());
                String textView = DictUtil.getDictionaryDetailText(key, value);
                if (textView != null) {
                    map.put(key + "TextView", textView);
                }
                map.put(key, convertValue(value));
            }
        } else {
            map = new HashMap<String, Object>(0);
        }
        return map;
    }

    /**
     * 数据集转换为Map对象
     * 
     * @param rs
     * @return
     * @throws SQLException
     */
    public static Map<String, Object> toMap(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Map<String, Object> map;
        if (rs.next()) {
            map = new HashMap<String, Object>(columnCount);
            for (int i = 0; i < columnCount; i++) {
                Object value = rs.getObject(i + 1);
                String key = StringUtil.getHumpName(rsmd.getColumnLabel(i + 1).trim());
                String textView = DictUtil.getDictionaryDetailText(key, value);
                if (textView != null) {
                    map.put(key + "TextView", textView);
                }
                map.put(key, convertValue(value));
            }
        } else {
            map = new HashMap<String, Object>(0);
        }
        return map;
    }

    /**
     * 数据集转换为Map对象
     * 
     * @param rs
     * @return
     * @throws SQLException
     */
    public static Map<String, Object> toMapNoNext(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Map<String, Object> map = new HashMap<String, Object>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            Object value = rs.getObject(i + 1);
            String key = StringUtil.getHumpName(rsmd.getColumnLabel(i + 1).trim());
            String textView = DictUtil.getDictionaryDetailText(key, value);
            if (textView != null) {
                map.put(key + "TextView", textView);
            }
            map.put(key, convertValue(value));
        }
        return map;
    }

    /**
     * 数据集转换为Map对象列表
     * 
     * @param rs
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> toMapList(SqlRowSet rs) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(rs.getRow());
        SqlRowSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>(columnCount);
            for (int i = 0; i < columnCount; i++) {
                Object value = rs.getObject(i + 1);
                String key = StringUtil.getHumpName(rsmd.getColumnLabel(i + 1).trim());
                String textView = DictUtil.getDictionaryDetailText(key, value);
                if (textView != null) {
                    map.put(key + "TextView", textView);
                }
                map.put(key, convertValue(value));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 结果集装换对象数组列表
     * 
     * @param rs
     *            结果集
     * @return
     */
    public static List<Object[]> toArrayList(SqlRowSet rs) {
        List<Object[]> result = new ArrayList<Object[]>(rs.getRow());
        SqlRowSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        while (rs.next()) {
            Object[] item = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                item[i] = rs.getObject(i + 1);
            }
            result.add(item);
        }
        return result;
    }

    /**
     * 数据集转换为对象数组
     * 
     * @param rs
     *            数据集
     * @return
     */
    public static Object[] toArray(SqlRowSet rs) {
        SqlRowSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Object[] result = new Object[columnCount];
        if (rs.next()) {
            for (int i = 0; i < columnCount; i++) {
                result[i] = rs.getObject(i + 1);
            }
        }
        return result;
    }

    /**
     * 设置SQL参数
     * 
     * @param ps
     * @param objs
     * @throws SQLException
     */
    public static void setStatementParams(PreparedStatement ps, Object[] objs) throws SQLException {
        for (int i = 0; i < objs.length; i++) {
            Object obj = objs[i];
            int j = i + 1;
            StatementCreatorUtils.setParameterValue(ps, j, -2147483648, obj);
        }
    }

    /**
     * 数据库类型装换
     * 
     * @param value
     * @throws SQLException
     * @d
     */
    public static Object convertValue(Object value) throws ApplicationException {
        if (value == null) return "";
        try {
            // 从数据库里读出来的是java.sql.Date赋值给了java.util.Date,转化成JSONArray时出错；
            if (value instanceof java.sql.Date) {
                return new java.util.Date(((java.sql.Date) value).getTime());
            } else if (value instanceof oracle.sql.TIMESTAMP) {// 处理ORCALE对象
                oracle.sql.TIMESTAMP t = (oracle.sql.TIMESTAMP) value;
                java.sql.Timestamp tt = t.timestampValue();
                return DateUtil.getDateFormat(3, tt);
                // return new java.util.Date(t.timestampValue().getTime());
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return value;
    }
}