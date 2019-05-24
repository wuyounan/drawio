package com.huigou.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

/**
 * 通用工具类
 * 
 * @author gongmm
 */
public class CommonUtil {

    private static final Logger logger = Logger.getLogger(CommonUtil.class);

    public static final String TIME_PATTERN = "HH:mm:ss";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DECIMAL_PATTERN = "#.##################################";

    public static final long HOURS_PER_DAY = 24;

    public static final long SECS_PER_MIN = 60;

    public static final long MINS_PER_HOUR = 60;

    public static final long MILLIS_PER_SECOND = 1000;

    public static final long MILLIS_PER_MINUTE = SECS_PER_MIN * MILLIS_PER_SECOND;

    public static final long MILLIS_PER_HOUR = MINS_PER_HOUR * MILLIS_PER_MINUTE;

    public static final long MILLIS_PER_DAY = HOURS_PER_DAY * MILLIS_PER_HOUR;

    public static final int DEFAULT_SCALE = 20;

    static SimpleDateFormat st = null;

    public static Map<String, Object> decodeMap(String value) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        if (Util.isNotEmptyString(value)) {
            String[] valueArray = value.trim().split(";");
            for (String str : valueArray) {
                if (str.contains("=")) {
                    String[] subValueArray = str.trim().split("=");
                    if (subValueArray.length == 1) result.put(subValueArray[0].trim(), "");
                    else {
                        result.put(subValueArray[0].trim(), subValueArray[1].trim());
                    }
                }
            }
        }
        return result;
    }

    public static String encodeMap(Map<String, Object> map) {
        String result = "";
        for (String key : map.keySet()) {
            Object obj = map.get(key);
            String value = "";
            if (obj != null) {
                value = obj + "";
            }
            result = result + key + "=" + value + ";";
        }
        return result;
    }

    /**
     * 生成32位的唯一标识
     * 
     * @return
     */
    public static String createGUID() {
        return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
    }

    /**
     * 生成文件全名称
     * 
     * @param path
     *            路径
     * @param fileName
     *            文件名
     * @param fileKind
     *            文件类型
     * @return 文件名
     */
    public static String createFileFullName(String path, String fileName, String fileKind) {
        StringBuffer sb = new StringBuffer();
        sb.append(Util.isEmptyString(path) ? "" : path);
        sb.append("/");
        sb.append(Util.isEmptyString(fileName) ? "" : fileName);
        if (Util.isNotEmptyString(fileKind)) sb.append(".").append(fileKind);
        return sb.toString();
    }

    /**
     * 获取当前的日期时间
     * 
     * @return
     */
    public static Timestamp getCurrentDateTime() {
        return new Timestamp(new java.util.Date().getTime());
    }

    /**
     * 获取当前的日期
     * 
     * @return
     */
    public static java.sql.Date getCurrentDate() {
        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(new java.util.Date()));
    }

    public static Date trunc(Date value) {
        String dateStr = (new SimpleDateFormat(DATE_PATTERN)).format(value);
        try {
            return new SimpleDateFormat(DATE_PATTERN).parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDateTime(String value) {
        try {
            return new SimpleDateFormat(DATE_TIME_PATTERN).parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDate(String value, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取昨天
     * 
     * @return
     */
    public static java.sql.Date getYesterday() {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(CommonUtil.getCurrentDate());
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    /**
     * 获取明天
     * 
     * @return
     */
    public static java.sql.Date getTomorrow() {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(CommonUtil.getCurrentDate());
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    /**
     * 获取当前的时间
     * 
     * @return
     */
    public static Time getCurrentTime() {
        return Time.valueOf(new SimpleDateFormat(TIME_PATTERN).format(new java.util.Date()));
    }

    /**
     * 获取给定日期的年份
     * 
     * @param date
     *            日期时间或者日期
     * @return 年份
     */
    public static int getYear(java.util.Date date) {
        check(date != null, "getYear的参数不能为空。");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取给定日期的月份
     * 
     * @param date
     *            日期时间或者日期
     * @return 月份
     */
    public static int getMonth(java.util.Date date) {
        check(date != null, "getMonth的参数不能为空。");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取给定日期的天
     * 
     * @param date
     * @return
     */
    public static int getDay(java.util.Date date) {
        check(date != null, "getDay的参数不能为空。");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取给定时间的小时
     * 
     * @param date
     *            日期时间或者时间
     * @return 小时
     */
    public static int getHour(java.util.Date date) {
        check(date != null, "getHour的参数不能为空。");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR);
    }

    /**
     * 获取给定时间的分钟
     * 
     * @param date
     * @return
     */
    public static int getMinute(java.util.Date date) {
        check(date != null, "getMinute的参数不能为空。");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取给定日期所在年的第一天
     * 
     * @param date
     *            日期时间或者日期
     * @return 所在年的第一天
     */
    public static java.sql.Date getFirstDateOfYear(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    public static java.sql.Date getFirstDateOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    /**
     * 获取给定日期所在年的最后一天
     * 
     * @param date
     *            日期时间或者日期
     * @return 所在年的最后一天
     */
    public static java.sql.Date getLastDateOfYear(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 0);
        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    public static java.sql.Date getLastDateOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year + 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 0);
        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    /**
     * 获取给定日期所在上年的第一天
     * 
     * @param date
     *            日期时间或者日期
     * @return 所在年的第一天
     */
    public static java.sql.Date getFirstDateOfLastYear(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    /**
     * 获取给定日期所在上年的最后一天
     * 
     * @param date
     *            日期时间或者日期
     * @return 所在年的最后一天
     */
    public static java.sql.Date getLastDateOfLastYear(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 0);
        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 获取给定时间的秒
     * 
     * @param date
     *            日期时间或者日期
     * @return 给定时间的秒
     */
    public static int getSecond(java.util.Date date) {
        check(date != null, "getYear的参数不能为空！");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 获取给定日期所在月的第一天
     * 
     * @param date
     *            日期时间或者日期
     * @return 所在月的第一天
     */
    public static java.sql.Date getFirstDateOfMonth(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    /**
     * 获取给定日期所在月的最后一天
     * 
     * @param date
     *            日期时间或者日期
     * @return 所在月的最后一天
     */
    public static java.sql.Date getLastDateOfMonth(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 0);
        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    public static java.sql.Date getFirstDateOfLastMonth(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, 1);
        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    public static java.sql.Date getLastDateOfLastMonth(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.MONTH, -1);

        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DATE, lastDay);

        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    /**
     * 获取给定日期所在周的第一天
     * 
     * @param date
     *            日期时间或者日期
     * @return 所在周的第一天
     */
    public static java.sql.Date getFirstDateOfWeek(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    /**
     * 获取给定日期所在周的最后一天
     * 
     * @param date
     *            日期时间或者日期
     * @return 所在周的最后一天
     */
    public static java.sql.Date getLastDateOfWeek(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, 7);
        calendar.add(Calendar.DATE, 1);
        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    /**
     * 获取给定日期所在上周的第一天
     * 
     * @param date
     *            日期时间或者日期
     * @return 所在周的第一天
     */
    public static java.sql.Date getFirstDateOfLastWeek(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();

        java.sql.Date firstDayOfWeek = getFirstDateOfWeek(date);
        calendar.setTime(firstDayOfWeek);
        calendar.add(Calendar.DATE, -7);

        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    /**
     * 获取给定日期所在上周的最后一天
     * 
     * @param date
     *            日期时间或者日期
     * @return 所在周的最后一天
     */
    public static java.sql.Date getLastDateOfLastWeek(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();

        java.sql.Date lastDayOfWeek = getFirstDateOfWeek(date);
        calendar.setTime(lastDayOfWeek);
        calendar.add(Calendar.DATE, -1);

        return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime()));
    }

    /**
     * 获取给定日期所在周的第几天
     * 
     * @param date
     *            日期时间或者日期
     * @return 所在周的第几天
     */
    public static int getDayOfWeek(java.util.Date date) {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime(date);
        return localCalendar.get(Calendar.DAY_OF_WEEK);
    }

    public static java.sql.Date getStepDay(Date date, int step) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, step);
        return new java.sql.Date(calendar.getTime().getTime());
    }

    public static Integer getDaysBetween(Date start, Date end) {
        Assert.notNull(start, "参数start不能为空。");
        Assert.notNull(end, "参数end不能为空。");

        Long result = (end.getTime() - start.getTime()) / MILLIS_PER_DAY;
        return result.intValue();
    }

    /**
     * 将指定的值转换成字符串
     * 
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        if (obj == null) return null;
        if ((obj instanceof Timestamp)) return new SimpleDateFormat(DATE_TIME_PATTERN).format((java.util.Date) obj);
        if ((obj instanceof Time)) return new SimpleDateFormat(TIME_PATTERN).format((java.util.Date) obj);
        if ((obj instanceof java.sql.Date)) return new SimpleDateFormat(DATE_PATTERN).format((java.util.Date) obj);
        if ((obj instanceof BigDecimal)) return new DecimalFormat(DECIMAL_PATTERN).format(obj);
        return obj.toString();
    }

    /**
     * 将指定的值转换成整数
     * 
     * @param obj
     * @return
     */
    public static int toInteger(Object obj) {
        check(obj != null, "toInteger的参数不能为空。");
        if ((obj instanceof String)) return Integer.valueOf((String) obj).intValue();
        if ((obj instanceof Number)) {
            return ((Number) obj).intValue();
        }
        throw new RuntimeException("错误的toInteger参数类型：" + obj.getClass());
    }

    /**
     * 将指定的值转换成BigDecimal
     * 
     * @param obj
     * @return
     */
    public static BigDecimal toDecimal(Object obj) {
        check(obj != null, "toDecimal的参数不能为空。");
        if ((obj instanceof BigDecimal)) {
            return (BigDecimal) obj;
        }
        BigDecimal result = null;
        if ((obj instanceof String)) result = BigDecimal.valueOf(Double.valueOf((String) obj).doubleValue());
        else if ((obj instanceof Number)) result = BigDecimal.valueOf(((Number) obj).doubleValue());
        else
            throw new RuntimeException("错误的toDecimal参数类型：" + obj.getClass());
        result.setScale(20, RoundingMode.HALF_UP);
        return result;
    }

    /**
     * 将指定的值转换成long
     * 
     * @param obj
     * @return
     */
    public static long toLong(Object obj) {
        check(obj != null, "toLong的参数不能为空。");
        if ((obj instanceof String)) return Long.valueOf((String) obj).longValue();
        if ((obj instanceof Number)) {
            return ((Number) obj).longValue();
        }
        throw new RuntimeException("错误的toLong参数类型：" + obj.getClass());
    }

    /**
     * 将指定的值转换成double
     * 
     * @param obj
     * @return
     */
    public static double toDouble(Object obj) {
        check(obj != null, "toDouble的参数不能为空。");
        if ((obj instanceof String)) return Double.valueOf((String) obj).doubleValue();
        if ((obj instanceof Number)) {
            return ((Number) obj).doubleValue();
        }
        throw new RuntimeException("错误的toDouble参数类型：" + obj.getClass());
    }

    /**
     * 将指定的值转换成float
     * 
     * @param obj
     * @return
     */
    public static float toFloat(Object obj) {
        check(obj != null, "toFloat的参数不能为空。");
        if ((obj instanceof String)) return Float.valueOf((String) obj).floatValue();
        if ((obj instanceof Number)) {
            return ((Number) obj).floatValue();
        }
        throw new RuntimeException("错误的toFloat参数类型：" + obj.getClass());
    }

    public static Float roundTo(Float value, int digit) {
        BigDecimal b = new BigDecimal(value);
        return b.setScale(digit, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 将指定的值转换成Date
     * 
     * @param obj
     * @return
     */
    public static java.sql.Date toDate(Object obj) {
        check(obj != null, "toDate的参数不能为空。");
        if ((obj instanceof java.sql.Date)) return (java.sql.Date) obj;
        if ((obj instanceof String)) return java.sql.Date.valueOf((String) obj);
        if ((obj instanceof java.util.Date)) return java.sql.Date.valueOf(new SimpleDateFormat(DATE_PATTERN).format((java.util.Date) obj));
        throw new RuntimeException("错误的toDate参数类型：" + obj.getClass());
    }

    /**
     * 将指定的值转换成DateTime
     * 
     * @param obj
     * @return
     */
    public static Timestamp toDateTime(Object obj) {
        check(obj != null, "toDateTime的参数不能为空。");
        if ((obj instanceof Timestamp)) return (Timestamp) obj;
        if ((obj instanceof String)) try {
            return new Timestamp(new SimpleDateFormat(DATE_TIME_PATTERN).parse((String) obj).getTime());
        } catch (Exception localException) {
            throw new RuntimeException("将字符串：" + obj + "转换为DATETIME时出错！");
        }
        if ((obj instanceof java.util.Date)) return new Timestamp(((java.util.Date) obj).getTime());
        throw new RuntimeException("错误的toDateTime参数类型：" + obj.getClass());
    }

    /**
     * 将指定的值转换成Time
     * 
     * @param obj
     * @return
     */
    public static Time toTime(Object obj) {
        check(obj != null, "toTime的参数不能为空。");
        if ((obj instanceof Time)) return (Time) obj;
        if ((obj instanceof String)) return Time.valueOf((String) obj);
        if ((obj instanceof java.util.Date)) return Time.valueOf(new SimpleDateFormat(TIME_PATTERN).format((java.util.Date) obj));
        throw new RuntimeException("错误的toTime参数类型：" + obj.getClass());
    }

    /**
     * 将指定的数字转换成中文的数字
     * 
     * @param value
     *            num
     * @param isCapital
     *            是否大写
     * @return
     */
    public static String toChineseNumber(Number value, boolean isCapital) {
        check(value != null, "toChineseNumber的参数不能为空。");
        check(isCapital, "目前不支持转换为小写的汉字数字！");
        if ((value instanceof BigDecimal)) {
            return ChineseNumber.format((BigDecimal) value);
        }
        return ChineseNumber.format(value.doubleValue());
    }

    /**
     * 将指定的数字转换成人民币数字
     * 
     * @param value
     * @return
     */
    public static String toChineseMoney(Number value) {
        check(value != null, "toChineseMoney的参数不能为空。");
        String s = null;
        if ((value instanceof BigDecimal)) s = ChineseNumber.format((BigDecimal) value);
        else {
            s = ChineseNumber.format(value.doubleValue());
        }
        return ChineseNumber.toMoneyString(s);
    }

    /**
     * 去掉字符串左边边的空白字符
     * 
     * @param value
     * @return
     */
    public static String ltrim(String value) {
        if (value == null) return null;
        if ("".equals(value)) return "";
        int i = 0;
        while ((i < value.length()) && (value.charAt(i) <= ' '))
            i++;
        return value.substring(i);
    }

    /**
     * 去掉字符串右边的空白字符
     * 
     * @param value
     * @return
     */
    public static String rtrim(String value) {
        if (value == null) return null;
        if ("".equals(value)) return "";
        int i = value.length();
        while ((i > 0) && (value.charAt(i - 1) <= ' '))
            i--;
        return value.substring(0, i);
    }

    public static String lpad(int length, int number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }

    /**
     * 获取文件扩展名
     * 
     * @param path
     *            文件全路径格式的字符串
     * @return
     */
    public static String getExtOfFile(String path) {
        String str = getNameOfFile(path);
        if ((str == null) || ("".equals(str))) return null;
        return str.indexOf('.') != -1 ? str.substring(str.lastIndexOf('.') + 1, str.length()) : null;
    }

    /**
     * 获取文件所在的目录
     * 
     * @param path
     *            文件全路径格式的字符串
     * @return
     */
    public static String getPathOfFile(String path) {
        if ((path == null) || ("".equals(path))) return null;
        path = path.trim();
        if (path.indexOf('/') == -1) return null;
        return path.substring(0, path.lastIndexOf('/'));
    }

    /**
     * 获取带扩展名的文件名
     * 
     * @param path
     *            文件全路径格式的字符串
     * @return
     */
    public static String getNameOfFile(String path) {
        if ((path == null) || ("".equals(path))) return null;
        path = path.trim();
        if (path.indexOf('/') == -1) return path;
        return path.substring(path.lastIndexOf('/') + 1, path.length());
    }

    /**
     * 获取不带扩张名的文件名 *
     * 
     * @param path
     *            文件全路径格式的字符串
     * @return
     */
    public static String getNameNoExtOfFile(String path) {
        String fileName = getNameOfFile(path);
        if ((fileName == null) || ("".equals(fileName))) return fileName;
        return fileName.indexOf('.') != -1 ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
    }

    /**
     * 验证条件
     * 
     * @param isOK
     *            判断条件
     * @param message
     *            提示消息
     */
    private static void check(boolean isOK, String message) {
        if (!isOK) {
            RuntimeException ex = new RuntimeException(message);
            logger.error("Error in CommonUtils:", ex);
            throw ex;
        }
    }

    public static boolean isLongNull(Long value) {
        return value == null || value.intValue() == 0;
    }

    public static boolean isIntegerNull(Integer value) {
        return value == null || value.intValue() == 0;
    }

    /*
     * TODO Adjust
     * public static Long transformBizId(SDO sdo, String keyName) {
     * Long bizId = sdo.getProperty("bizId", Long.class);
     * sdo.putProperty(keyName, bizId);
     * return bizId;
     * }
     */

    // public static boolean isMoblieFormat(String value) {
    // Pattern p = null;
    // Matcher m = null;
    // boolean result = false;
    // p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
    // m = p.matcher(value);
    // result = m.matches();
    // return result;
    // }

    /**
     * 人民币大写
     * 
     * @author gongmm
     */
    static class ChineseNumber {
        private static String[] UNIT = { "", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟" };

        private static String[] BIG = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

        private static int WAN_IDX = 4;

        private static int YI_IDX = 8;

        private static int LAST_ZERO = 1;

        private static int VALID_WAN = 2;

        private static int VALID_YI = 4;

        private static int STARTED = 8;

        public static String format(double paramDouble) {
            return format(new DecimalFormat(DECIMAL_PATTERN).format(paramDouble));
        }

        public static String format(int value) {
            return format(String.valueOf(value));
        }

        public static String format(BigDecimal value) {
            return format(new DecimalFormat(DECIMAL_PATTERN).format(value));
        }

        public static String format(String value) {
            if ((value == null) || ("".equals(value))) {
                return null;
            }

            Double.valueOf(value);
            StringBuffer sb = new StringBuffer();
            if (value.charAt(0) == '-') {
                sb.append("负");
                value = value.substring(1);
            }

            String[] valueArray = value.split("[.]");
            if ((valueArray.length > 2) || (valueArray.length < 1)) {
                throw new RuntimeException("非法的数值格式：" + value);
            }
            sb.append(formatInteger(valueArray[0]));
            if (valueArray.length == 2) {
                sb.append("点");
                for (int k : valueArray[1].toCharArray())
                    sb.append(BIG[(k - 48)]);
            }
            return sb.toString();
        }

        /**
         * 格式整数
         * 
         * @param value
         * @return
         */
        private static String formatInteger(String value) {
            StringBuffer sb = new StringBuffer();
            int i = 0;
            for (int j = 0; j < value.length(); j++) {
                int k = value.length() - j - 1;
                String str = UNIT[k];
                int m = value.charAt(j) - '0';
                if (m == 0) {
                    if (((str.equals("亿")) && ((i & VALID_YI) > 0)) || ((str.equals("万")) && ((i & VALID_WAN) > 0))) {
                        sb.append(str);
                    }
                    if (((i & STARTED) == 0) && (j == value.length() - 1)) sb.append(BIG[m]);
                    i |= LAST_ZERO;
                } else {
                    if (((i & LAST_ZERO) > 0) && ((i & STARTED) > 0)) sb.append("零");
                    if (k >= YI_IDX) i |= VALID_YI;
                    else if (k >= WAN_IDX) i |= VALID_WAN;
                    i &= (LAST_ZERO ^ 0xFFFFFFFF);
                    sb.append(BIG[m]);
                    sb.append(str);
                    i |= STARTED;
                }
            }

            return sb.toString();
        }

        /**
         * 转换人民币大写
         * 
         * @param value
         * @return
         */
        public static String toMoneyString(String value) {
            CommonUtil.check((value != null) && (!"".equals(value)), "传入的数值不能为空。");
            if (value.indexOf('点') >= 0) {
                String[] valueArray = value.split("点");
                CommonUtil.check(valueArray.length == 2, "非法的数字字符串：" + value);
                CommonUtil.check(valueArray[1].length() <= 2, "超出精度的金额数值：" + value);
                StringBuffer sb = new StringBuffer(valueArray[0]);
                sb.append("元");
                sb.append(valueArray[1].charAt(0));
                sb.append("角");
                if (valueArray[1].length() == 2) {
                    sb.append(valueArray[1].charAt(1));
                    sb.append("分");
                }
                return sb.toString();
            }
            return value + "元整";
        }
    }
}