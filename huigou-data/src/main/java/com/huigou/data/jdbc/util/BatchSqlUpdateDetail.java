package com.huigou.data.jdbc.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.util.Assert;

import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.exception.ApplicationException;
import com.huigou.util.ClassHelper;

/**
 * 报表数据批量查询
 * 
 * @author xx
 *         Map<String, Integer> paramType = new HashMap<String, Integer>(1);
 *         paramType.put("sjsjc", Types.TIMESTAMP);
 *         BatchInsertDetail batchInsertDetail = BatchInsertDetail.newInstance(this.jdbcDao.getDataSource(), fieldSql.toString(), paramType);
 *         for (Map<String, Object> map : list) {
 *         map.put("id", Utils.createGUID());
 *         map.put("serialid", serialId);
 *         batchInsertDetail.setRows(map);
 *         }
 *         batchInsertDetail.flush();
 */
public class BatchSqlUpdateDetail extends BatchSqlUpdate {

    private List<String> names;
    

    private BigDecimal dataCount;

    public static BatchSqlUpdateDetail newInstance(DataSource dataSource, String sql, Map<String, Integer> paramType) {
        ParseSQLParam parser = new ParseSQLParam();
        parser.parse(sql);
        List<String> names = parser.getParameter();
        if ((names == null) || (names.size() == 0)) {
            throw new ApplicationException("BatchInsertDetail:sql parse error,没有参数!");
        }
        BatchSqlUpdateDetail detail = new BatchSqlUpdateDetail(dataSource, parser.getParseSql());
        detail.setNames(names);
        detail.setSqlParameter(paramType);
        return detail;
    }

    BatchSqlUpdateDetail(DataSource dataSource, String sql) {
        super(dataSource, sql);
        this.setBatchSize(1000);
        dataCount = BigDecimal.ZERO;
    }

    public Long getDataCount() {
        return dataCount.longValue();
    }

    protected void setNames(List<String> names) {
        this.names = names;
    }

    protected void setSqlParameter(Map<String, Integer> paramType) {
        Integer fieldType = Types.VARCHAR;
        for (String name : this.names) {
            if (paramType != null) {
                if (paramType.containsKey(name)) {
                    fieldType = paramType.get(name);
                } else {
                    fieldType = Types.VARCHAR;
                }
            }
            this.declareParameter(new SqlParameter(fieldType));
        }
    }

    public void setRows(Map<String, Object> data) {
        List<Object> dataSets = new ArrayList<Object>(names.size());
        Object value = null;
        for (String name : names) {
            Assert.notNull(data.containsKey(name), String.format("%s属性未找到", name));
            value = data.get(name);
            // 空字符串 转换为空对象
            if (value != null && "".equals(value.toString())) {
                value = null;
            }
            dataSets.add(value);
        }
        this.update(dataSets.toArray(new Object[names.size()]));
        dataCount = dataCount.add(BigDecimal.ONE);
    }

    public void setRows(AbstractEntity entity) {
        List<Object> dataSets = new ArrayList<Object>(names.size());
        Object value = null;
        Field field = null;
        try {
            for (String name : names) {
                field = ClassHelper.getField(entity.getClass(), name);
                Assert.notNull(field, String.format("%s属性未找到", name));
                field.setAccessible(true);
                value = field.get(entity);
                dataSets.add(value);
            }
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(e);
        } catch (IllegalAccessException e) {
            throw new ApplicationException(e);
        }
        this.update(dataSets.toArray(new Object[names.size()]));
        dataCount = dataCount.add(BigDecimal.ONE);
    }

    public int[] flush() {
        if (dataCount.compareTo(BigDecimal.ZERO) > 0) {
            return super.flush();
        }
        return null;
    }
}