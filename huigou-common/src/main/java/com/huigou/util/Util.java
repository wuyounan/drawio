package com.huigou.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.huigou.exception.ApplicationException;

public class Util {
    private static final String CHECK_ERR_MSG = "check error!";

    private static Logger logger = Logger.getLogger(Util.class);

    public static int i = 0;

    /**
     * 验证必须满足条件
     * 
     * @param isOk
     *            条件
     * @param format
     *            格式
     * @param objs
     *            参数
     */
    public static void check(boolean isOk, String format, Object[] objs) {
        if (!isOk) {
            throw new RuntimeException(String.format(format, objs));
        }
    }

    /**
     * 检查对象不能为空
     * 
     * @param message
     *            提示消息
     * @param o
     *            检查对象
     */
    public static void checkNotNull(String message, Object obj) {
        if (obj == null) {
            ApplicationException ex = new ApplicationException(String.format("%s不能为空!", new Object[] { message }));
            logger.error("检查 " + message + " 时报错！", ex);
            throw ex;
        }
    }

    /**
     * 获取行分隔符
     * 
     * @return
     */
    public static String getLineSep() {
        return System.getProperty("line.separator");
    }

    /**
     * 检查对象是否存在
     * 
     * @param message
     *            提示消息
     * @param o
     *            检查对象
     */
    public static void checkNotExist(String message, Object obj) {
        if (obj == null) {
            ApplicationException ex = new ApplicationException(String.format("%s不存在!", new Object[] { message }));
            logger.error("检查 " + message + " 时报错！", ex);
            throw ex;
        }
    }

    /**
     * 检查字符串不能为空
     * 
     * @param message
     *            提示消息
     * @param value
     *            检查字符串
     */
    public static void checkNotEmptyString(String message, String value) {
        checkNotEmptyString(message, value, logger);
    }

    /**
     * 检查字符串不能为空
     * 
     * @param message
     *            提示消息
     * @param value
     *            检查字符串
     * @param logger
     *            日志对象
     */
    public static void checkNotEmptyString(String message, String value, Logger logger) {
        if (isEmptyString(value)) {
            ApplicationException ex = new ApplicationException(String.format("%s不能为空!", new Object[] { message }));
            if (!isNull(logger)) logger.error("检查 " + message + " 时报错！", ex);
            throw ex;
        }
    }

    /**
     * 检查字对象不能为空
     * 
     * @param message
     *            提示消息
     * @param objs
     *            对象
     */
    public static void checkAllNotNull(String message, Object... objs) {
        for (Object item : objs)
            if (item == null) {
                ApplicationException ex = new ApplicationException(String.format("%s不能为空!", new Object[] { message }));
                logger.error("检查 " + message + " 时报错！", ex);
                throw ex;
            }
    }

    public static void check(boolean isOK, Object obj) {
        if (!isOK) {
            ApplicationException ex = new ApplicationException("" + obj);
            logger.error(CHECK_ERR_MSG, ex);
            throw ex;
        }
    }

    public static void check(boolean isOK, Object obj1, Object obj2) {
        if (!isOK) {
            ApplicationException ex = new ApplicationException(buildString(new Object[] { obj1, obj2 }));
            logger.error(CHECK_ERR_MSG, ex);
            throw ex;
        }
    }

    public static void check(boolean isOK, Object obj1, Object obj2, Object obj3) {
        if (!isOK) {
            ApplicationException ex = new ApplicationException(buildString(new Object[] { obj1, obj2, obj3 }));
            logger.error(CHECK_ERR_MSG, ex);
            throw ex;
        }
    }

    public static void check(boolean isOK, Object obj1, Object obj2, Object obj3, Object obj4) {
        if (!isOK) {
            ApplicationException ex = new ApplicationException(buildString(new Object[] { obj1, obj2, obj3, obj4 }));
            logger.error(CHECK_ERR_MSG, ex);
            throw ex;
        }
    }

    public static void check(boolean isOK, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
        if (!isOK) {
            ApplicationException ex = new ApplicationException(buildString(new Object[] { obj1, obj2, obj3, obj4, obj5 }));
            logger.error(CHECK_ERR_MSG, ex);
            throw ex;
        }
    }

    public static void check(boolean isOK, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        if (!isOK) {
            ApplicationException localModelException = new ApplicationException(buildString(new Object[] { obj1, obj2, obj3, obj4, obj5, obj6 }));
            logger.error(CHECK_ERR_MSG, localModelException);
            throw localModelException;
        }
    }

    private static String buildString(Object[] objs) {
        StringBuilder sb = new StringBuilder();
        for (Object item : objs)
            sb.append(item);
        return sb.toString();
    }

    public static boolean isNotEmptyString(String s) {
        return (s != null) && (s.trim().length() != 0);
    }

    public static boolean isEmptyString(String s) {
        return (null == s) || ("".equals(s));
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    /**
     * 数组转换为字符串
     * 
     * @param array
     *            数组
     * @param formatter
     *            格式化字符串
     * @param delimiter
     *            分隔符
     * @return
     */
    public static String arrayToString(Object[] array, String formatter, String delimiter) {
        StringBuffer sb = new StringBuffer();
        for (Object item : array) {
            if (sb.length() > 0) {
                sb.append(delimiter);
            }
            sb.append(String.format(formatter, new Object[] { item.toString() }));
        }
        return sb.toString();
    }

    public static List<String> stringArrayToList(String[] array) {
        List<String> result = new ArrayList<String>(array.length);
        for (int i = 0; i < array.length; i++)
            result.add(array[i]);

        return result;
    }

    public static String MD5(String value) {
        if (value == null) {
            return null;
        }
        try {
            StringBuffer sb = new StringBuffer();
            for (byte b : MessageDigest.getInstance("MD5").digest(value.getBytes()))
                sb.append(String.format("%02x", new Object[] { Byte.valueOf(b) }));
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            throw new RuntimeException("can't covert to md5!", localNoSuchAlgorithmException);
        }
    }

    public static int charCount(String paramString, char paramChar) {
        if (isEmptyString(paramString)) return 0;
        int j = 0;
        for (char c : paramString.toCharArray())
            if (c == paramChar) j++;
        return j;
    }
}