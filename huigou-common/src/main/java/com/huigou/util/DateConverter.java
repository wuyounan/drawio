package com.huigou.util;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期转换器
 * <p>
 * 系统自动地帮助字符的日期表示转换为java.util.Date对象
 * 
 * @author xx
 */
public class DateConverter implements Converter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateConverter.class);

    private static Set<String> datePatterns = new HashSet<String>();
    // 注册一下日期的转换格式
    static {
        DateConverter.datePatterns.add("yyyy-MM-dd HH:mm");
        DateConverter.datePatterns.add("yyyy-MM-dd HH:mm:ss");
        DateConverter.datePatterns.add("yyyy-MM-dd");
        DateConverter.datePatterns.add("yyyy/MM/dd HH:mm");
        DateConverter.datePatterns.add("yyyy/MM/dd HH:mm:ss");
        DateConverter.datePatterns.add("yyyy/MM/dd");
    }

    @SuppressWarnings("rawtypes")
    public Object convert(Class type, Object value) {
        if (value == null) {
            return null;
        } else if (type == Timestamp.class || type == Date.class) {
            return convertToType(type, value);
        } else if (type == String.class) {
            return convertToString(type, value);
        }
        return null;
    }

    protected Object convertToType(Class<?> type, Object value) {
        SimpleDateFormat df = new SimpleDateFormat();
        if (value instanceof String) {
            String dateString = (String) value;
            if (StringUtil.isBlank(dateString)) {
                return null;
            }
            Date date = null;
            Iterator<String> it = datePatterns.iterator();
            while (it.hasNext()) {
                try {
                    String pattern = it.next();
                    df.applyPattern(pattern);
                    date = df.parse(dateString);
                    break;
                } catch (ParseException ex) {
                }
            }
            if (date == null) {
                throw new ConversionException("“" + dateString + "”转换日期错误。");
            }
            if (type.equals(Timestamp.class)) {
                return new Timestamp(date.getTime());
            }
            return date;
        } else if (value instanceof Date) {
            return value;
        } else if (value instanceof oracle.sql.TIMESTAMP) {// 处理ORCALE对象
            oracle.sql.TIMESTAMP t = (oracle.sql.TIMESTAMP) value;
            try {
                java.sql.Timestamp tt = t.timestampValue();
                return tt;
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
                throw new ConversionException(ex);
            }
        }
        String msg = value.getClass() + " cannot handle conversion to '" + type.getClass() + "'";
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("    " + msg);
        }
        throw new ConversionException(msg);
    }

    protected Object convertToString(Class<?> type, Object value) {
        if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (value instanceof Timestamp) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            return sdf.format(value);
        } else {
            return value.toString();
        }
    }
}
