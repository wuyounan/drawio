package com.huigou.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期处理函数
 * 
 * @author gongmm
 */
public class DateUtil {

    private static SimpleDateFormat getSimpleDateFormat(int type) {
        SimpleDateFormat df = null;
        switch (type) {
        case 1:
            df = new SimpleDateFormat("yyyy-MM-dd");
            break;
        case 2:
            df = new SimpleDateFormat("MM/dd/yy");
            break;
        case 3:
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            break;
        case 4:
            df = new SimpleDateFormat("MM/dd/yyyy");
            break;
        case 5:
            df = new SimpleDateFormat("yyyyMMddHHmmssS");
            break;
        case 6:
            df = new SimpleDateFormat("yyyy/MM/dd");
            break;
        case 7:
            df = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
            break;
        case 8:
            df = new SimpleDateFormat("yyyy年MM月dd日");
            break;
        case 9:
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            break;
        case 10:
            df = new SimpleDateFormat("HH:mm");
            break;
        case 11:
            df = new SimpleDateFormat("yyyy.MM.dd");
            break;
        case 12:
            df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            break;
        case 13:
            df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            break;
        default:
            df = new SimpleDateFormat("yyyy-MM-dd");
            break;
        }
        return df;
    }

    /**
     * 得到当前日期(java.sql.Date类型)，注意：没有时间，只有日期
     * 
     * @return 当前日期
     */
    public static java.sql.Date getDate() {
        Calendar oneCalendar = Calendar.getInstance();
        return getDate(oneCalendar.get(Calendar.YEAR), oneCalendar.get(Calendar.MONTH) + 1, oneCalendar.get(Calendar.DATE));
    }

    public static java.sql.Timestamp getTimestamp() {
        return new java.sql.Timestamp(System.currentTimeMillis());
    }

    public static java.sql.Date getDate(Date date) {
        Calendar oneCalendar = Calendar.getInstance();
        oneCalendar.setTime(date);
        return getDate(oneCalendar.get(Calendar.YEAR), oneCalendar.get(Calendar.MONTH) + 1, oneCalendar.get(Calendar.DATE));
    }

    public static Date trunc(Date value) {
        SimpleDateFormat df = getSimpleDateFormat(1);
        String dateStr = df.format(value);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 更具参数去日期年,月,日
     * 
     * @param date
     * @param type
     * @return
     */
    public static int getCalendarInt(Date date, int type) {
        Calendar oneCalendar = Calendar.getInstance();
        oneCalendar.setTime(date);
        return oneCalendar.get(type);
    }

    /**
     * 得到当前日期有时间
     * 
     * @return
     */
    public static Timestamp getDateTime() {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        return t;
    }

    /**
     * 根据所给年、月、日，得到日期(java.sql.Date类型)，注意：没有时间，只有日期。
     * 年、月、日不合法，会抛IllegalArgumentException(不需要catch)
     * 
     * @param yyyy
     *            4位年
     * @param MM
     *            月
     * @param dd
     *            日
     * @return 日期
     */
    public static java.sql.Date getDate(int yyyy, int MM, int dd) {

        if (!verityDate(yyyy, MM, dd)) {
            throw new IllegalArgumentException("This is illegimate date!");
        }

        Calendar oneCalendar = Calendar.getInstance();
        oneCalendar.clear();
        oneCalendar.set(yyyy, MM - 1, dd);
        return new java.sql.Date(oneCalendar.getTime().getTime());
    }

    /**
     * 根据所给年、月、日，检验是否为合法日期。
     * 
     * @param yyyy
     *            4位年
     * @param MM
     *            月
     * @param dd
     *            日
     * @return
     */
    public static boolean verityDate(int yyyy, int MM, int dd) {
        boolean flag = false;

        if (MM >= 0 && MM <= 12 && dd >= 1 && dd <= 31) {
            if (MM == 4 || MM == 6 || MM == 9 || MM == 11) {
                if (dd <= 30) {
                    flag = true;
                }
            } else if (MM == 2) {
                if (yyyy % 100 != 0 && yyyy % 4 == 0 || yyyy % 400 == 0) {
                    if (dd <= 29) {
                        flag = true;
                    }
                } else if (dd <= 28) {
                    flag = true;
                }

            } else {
                flag = true;
            }

        }
        return flag;
    }

    /**
     * 根据所给的起始,终止时间来计算间隔天数
     * 
     * @param startDate
     * @param endDate
     * @return 间隔天数
     */
    public static int getIntervalDay(Date startDate, Date endDate) {
        long startdate = startDate.getTime();
        long enddate = endDate.getTime();
        long interval = enddate - startdate;
        long intervalday = interval / (1000 * 60 * 60 * 24);
        return new Long(intervalday).intValue();
    }

    /**
     * 计算两个日期间相隔的周数
     * 
     * @param startDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return
     */
    public static int getIntervalWeeks(Date startDate, Date endDate) {
        int weeks = 0;
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        while (beginCalendar.before(endCalendar)) {
            // 如果开始日期和结束日期在同年、同月且当前月的同一周时结束循环
            if (beginCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) && beginCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH)
                && beginCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) == endCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH)) {
                break;
            } else {
                beginCalendar.add(Calendar.DAY_OF_YEAR, 7);
                weeks += 1;
            }
        }
        return weeks;
    }

    /**
     * 计算两个日期间相隔的月
     * 
     * @param startDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return
     */
    public static int getIntervalMonth(Date startDate, Date endDate) {
        if (endDate == null) {
            endDate = new Date();
        }
        Calendar dayBegin = Calendar.getInstance();
        dayBegin.setTime(startDate);
        Calendar dayEnd = Calendar.getInstance();
        dayEnd.setTime(endDate);
        if (dayEnd.equals(dayBegin)) return 0;
        if (dayBegin.after(dayEnd)) {
            Calendar temp = dayBegin;
            dayBegin = dayEnd;
            dayEnd = temp;
        }
        // 初始计算
        int result = dayEnd.get(Calendar.MONTH) + dayEnd.get(Calendar.YEAR) * 12 - dayBegin.get(Calendar.MONTH) - dayBegin.get(Calendar.YEAR) * 12;
        /*
         * // 赋值结束日期的月末
         * Calendar lastDayInEndMonth = Calendar.getInstance();
         * lastDayInEndMonth.set(dayEnd.get(Calendar.YEAR), dayEnd.get(Calendar.MONTH), 1);
         * lastDayInEndMonth.add(Calendar.MONTH, 1);
         * lastDayInEndMonth.add(Calendar.DATE, -1);
         * // 开始在1日，结束在月末，加一个月
         * if ((lastDayInEndMonth.get(Calendar.DATE) == dayEnd.get(Calendar.DATE)) && (dayBegin.get(Calendar.DATE) == 1)) {
         * result += 1;
         * }
         * // 如果结束的日期+1<开始日期 并且结束的日期不为月底就扣除一个月
         * if ((dayEnd.get(Calendar.DATE) + 1 < dayBegin.get(Calendar.DATE)) && !(lastDayInEndMonth.get(Calendar.DATE) == dayEnd.get(Calendar.DATE))) {
         * result -= 1;
         * }
         */
        return result;
    }

    /**
     * 根据所给的起始时间,间隔天数来计算终止时间
     * 
     * @param startDate
     * @param day
     * @return 终止时间
     */
    public static java.sql.Date getStepDay(Date date, int step) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, step);
        return new java.sql.Date(calendar.getTime().getTime());
    }

    /**
     * 根据所给的起始时间,间隔天数来计算终止时间
     * 
     * @param date
     * @param step
     * @return
     */
    public static java.sql.Date getStepWeek(java.sql.Date date, int step) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_YEAR, step);
        return new java.sql.Date(calendar.getTime().getTime());
    }

    /**
     * 得到上一月的日期
     * 
     * @return
     */
    public static java.sql.Date getLastMonth() {
        Calendar oneCalendar = Calendar.getInstance();
        oneCalendar.set(oneCalendar.get(Calendar.YEAR), oneCalendar.get(Calendar.MONTH) - 1, 1);
        return new java.sql.Date(oneCalendar.getTime().getTime());
    }

    /**
     * 取月度最后一天
     * 
     * @author
     * @return
     * @throws
     */
    public static java.sql.Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        return new java.sql.Date(calendar.getTime().getTime());
    }

    /**
     * 得到上一月的最后一天日期
     * 
     * @return
     */
    public static java.sql.Date getLastMonthDay() {

        Calendar oneCalendar = Calendar.getInstance();
        oneCalendar.set(oneCalendar.get(Calendar.YEAR), oneCalendar.get(Calendar.MONTH), 1);
        int year = oneCalendar.get(Calendar.YEAR);
        int month = oneCalendar.get(Calendar.MONTH);
        Calendar oneCalendar1 = Calendar.getInstance();
        String lastday = getLastDays(new Integer(year).toString(), new Integer(month).toString());
        java.sql.Date lastdate = getDate(oneCalendar1.get(Calendar.YEAR), oneCalendar1.get(Calendar.MONTH), new Integer(lastday).intValue());
        return lastdate;
    }

    /**
     * 取得本月第一天
     * 
     * @return
     */
    public static java.sql.Date getMonthFirstDay() {
        Calendar oneCalendar = Calendar.getInstance();
        oneCalendar.set(oneCalendar.get(Calendar.YEAR), oneCalendar.get(Calendar.MONTH), 1);
        return new java.sql.Date(oneCalendar.getTime().getTime());
    }

    /**
     * 得到输入时间月的第一天
     * 
     * @author
     * @param date
     * @return java.sql.Date
     */
    public static java.sql.Date getMonthFirstDay(Date date) {
        Calendar oneCalendar = Calendar.getInstance();
        oneCalendar.setTime(date);
        oneCalendar.set(oneCalendar.get(Calendar.YEAR), oneCalendar.get(Calendar.MONTH), 1);
        return new java.sql.Date(oneCalendar.getTime().getTime());
    }

    /**
     * 得到输入时间年的第一天
     * 
     * @author
     * @param date
     * @return java.sql.Date
     */
    public static java.sql.Date getYearFirstDay(Date date) {
        Calendar oneCalendar = Calendar.getInstance();
        oneCalendar.setTime(date);
        oneCalendar.set(oneCalendar.get(Calendar.YEAR), 0, 1);
        return new java.sql.Date(oneCalendar.getTime().getTime());
    }

    /**
     * 得到输入时间月的15号时间
     * 
     * @author
     * @param date
     * @return java.sql.Date
     */
    public static java.sql.Date getMonth15Day(Date date) {
        Calendar oneCalendar = Calendar.getInstance();
        oneCalendar.setTime(date);
        oneCalendar.set(oneCalendar.get(Calendar.YEAR), oneCalendar.get(Calendar.MONTH), 15);
        return new java.sql.Date(oneCalendar.getTime().getTime());
    }

    /**
     * 得到输入时间月的最后一天
     * 
     * @author lhai
     * @param date
     * @return java.sql.Date
     */
    public static java.sql.Date getMonthLastDay(Date date) {
        Calendar oneCalendar = Calendar.getInstance();
        oneCalendar.setTime(date);
        oneCalendar.set(oneCalendar.get(Calendar.YEAR), oneCalendar.get(Calendar.MONTH), 1);
        oneCalendar.add(Calendar.MONTH, 1);
        oneCalendar.add(Calendar.DAY_OF_YEAR, -1);
        return new java.sql.Date(oneCalendar.getTime().getTime());
    }

    /**
     * 取得本月最后一天
     * 
     * @return
     */
    public static java.sql.Date getMonthLastDay() {
        // Calendar calo = Calendar.getInstance();
        // calo.setTime(getStepMonth(getLastMonthDay(), 1));
        // return new java.sql.Date(calo.getTime().getTime());
        Calendar oneCalendar = Calendar.getInstance();
        oneCalendar.set(oneCalendar.get(Calendar.YEAR), oneCalendar.get(Calendar.MONTH), 1);
        oneCalendar.add(Calendar.MONTH, 1);
        oneCalendar.add(Calendar.DAY_OF_YEAR, -1);
        return new java.sql.Date(oneCalendar.getTime().getTime());
    }

    /**
     * 得到将date增加指定月数后的date
     * 
     * @param date
     * @param intBetween
     * @return date加上intBetween月数后的日期
     */
    public static java.sql.Date getStepMonth(Date date, int intBetween) {
        Calendar calo = Calendar.getInstance();
        calo.setTime(date);
        calo.add(Calendar.MONTH, intBetween);
        return new java.sql.Date(calo.getTime().getTime());
    }

    /**
     * 得到将date增加指定年数后的date
     * 
     * @param date
     * @param intBetween
     * @return
     */
    public static java.sql.Date getStepYear(Date date, int intBetween) {
        Calendar calo = Calendar.getInstance();
        calo.setTime(date);
        calo.add(Calendar.YEAR, intBetween);
        return new java.sql.Date(calo.getTime().getTime());
    }

    public static java.sql.Date getStepMinute(Date date, int intBetween) {
        Calendar calo = Calendar.getInstance();
        calo.setTime(date);
        calo.add(Calendar.MINUTE, intBetween);
        return new java.sql.Date(calo.getTime().getTime());
    }

    public static String getDateStr(java.sql.Date date) {
        java.text.DateFormat format = java.text.DateFormat.getDateInstance();
        String dateStr = format.format(date);
        return dateStr;
    }

    public static String getDateFormat(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat df = getSimpleDateFormat(1);
        String dateStr = df.format(date);
        return dateStr;
    }

    public static String getDateFormat(Date date, String fmt) {
        if (date == null) return "";
        SimpleDateFormat df = new SimpleDateFormat(fmt);
        String dateStr = df.format(date);
        return dateStr;
    }

    public static String getDateFormat(String format, Date date) {
        if (date == null) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String getDateFormat(int type, Date date) {
        if (date == null) return "";
        SimpleDateFormat df = getSimpleDateFormat(type);
        String dateStr = df.format(date);
        return dateStr;
    }

    public static Date getDateParse(int type, String str) throws Exception {
        if (str == null || str.equals("")) return null;
        Date date = new Date();
        SimpleDateFormat df = getSimpleDateFormat(type);
        date = df.parse(str);
        return date;
    }

    public static Date getDateParse(String str) {
        if (str == null || str.equals("")) return null;
        for (int type = 13; type > 0; type--) {
            if (type == 10) {
                continue;
            }
            if (isDate(type, str)) {
                try {
                    return getDateParse(type, str);
                } catch (Exception e) {
                    break;
                }
            }
        }
        return null;
    }

    /**
     * 判断输入字符串是否为对应的日期格式
     * 
     * @Title: isDate
     * @param type
     * @param str
     * @return
     * @return boolean
     */
    public static boolean isDate(int type, String str) {
        if (str == null || str.equals("")) return false;
        try {
            SimpleDateFormat df = getSimpleDateFormat(type);
            df.format(df.parse(str));
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    /**
     * 判断输入的字符串是否为日期格式
     * 
     * @Title: validDate
     * @param str
     * @return
     * @return boolean
     */
    public static boolean validDate(String str) {
        if (str == null || str.equals("")) return false;
        // df = "yyyy-MM-dd";
        // df6 = "yyyy/MM/dd";
        if (isDate(1, str) || isDate(6, str) || isDate(7, str) || isDate(8, str)) {
            return true;
        }
        return false;
    }

    /**
     * 判断输入的字符串是否为日期格式
     * 
     * @param str
     * @return
     */
    public static String validDateAndModify(String str) {
        if (str == null || str.equals("")) return null;

        if (isDate(1, str)) {
            // df = "yyyy-MM-dd";
            return str;
        } else if (isDate(6, str)) {
            // df6 = "yyyy/MM/dd";
            return str.replaceAll("/", "-");
        }
        return null;
    }

    /**
     * 取得月份的最后一天的日期
     * 
     * @param yy
     * @param mm
     * @return
     */
    public static String getLastDays(String yy, String mm) {
        String day = "30";
        int YY = 0;
        int MM = 0;
        boolean leapYear = false;

        YY = (new Integer(yy)).intValue();
        MM = (new Integer(mm)).intValue();
        if (YY < 1900 || YY > 2200) {
            return (day);
        }

        if (((YY % 4) != 0) && ((YY % 100) != 0)) { // 判断是否为闰年
            leapYear = false;
        } else {
            leapYear = true;
        }
        if (MM == 2) {
            if (leapYear) {
                return "29";
            } else {
                return "28";
            }
        }
        if ((MM == 1) || (MM == 3) || (MM == 5) || (MM == 7) || (MM == 8) || (MM == 10) || (MM == 12) || (MM == 0)) {
            return "31";
        }

        if ((MM == 4) || (MM == 6) || (MM == 9) || (MM == 11)) {
            return day;
        }
        return day;
    }

    /**
     * 判断是否是闰年
     */
    public static boolean IsLeapYear(int year) {

        if ((year % 100) == 0)

        return ((year % 400) == 0);

        return ((year % 4) == 0);

    } // IsLeapYear

    /**
     * 取得当前年分
     */
    public static String getCurrentYear() {
        return getCurrentTime(4);
    }

    /*
     * 得到当前时间自定义长度
     */
    public static String getCurrentTime(int length) {
        java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
        if (length > ts.toString().length()) {
            length = ts.toString().length();
        }
        String currentTime = ts.toString().substring(0, length);
        return currentTime;
    }

    /**
     * 取得当前月份
     */
    public static String getCurrentMonth() {
        return getCurrentTime(7).substring(5, 7);
    }

    /**
     * 获取当前日期是星期几
     * 
     * @param dt
     * @return
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        java.text.DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        String datestr = df.format(dt);
        return datestr + " " + weekDays[w];
    }

    public static String getTodayHms() {
        Date date = new Date();
        String dayStr = getSimpleDateFormat(3).format(date);
        return dayStr;
    }

    // 获取当前时间所在年的周数
    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);

        return c.get(Calendar.WEEK_OF_YEAR);
    }

    // 获取当前时间所在年的最大周数
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

        return getWeekOfYear(c.getTime());
    }

    // 获取某年的第几周的开始日期
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    // 获取某年的第几周的结束日期
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }

    // 获取当前时间所在周的开始日期
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    // 获取当前时间所在周的结束日期
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }

    // 获取指定日期下一月的第一天
    public static Date getFirstDayOfNextMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, 1);
        return c.getTime();
    }

    /**
     * 日期格式解析
     * 
     * @param value
     * @return
     */
    public static Object processDate(Object value) {
        if (value == null) return "";
        if (value instanceof java.sql.Timestamp) {
            return getSimpleDateFormat(3).format(new java.util.Date(((java.sql.Timestamp) value).getTime()));
        } else if (value instanceof java.sql.Date) {
            return getSimpleDateFormat(1).format(new java.util.Date(((java.sql.Date) value).getTime()));
        } else if (value instanceof java.util.Date) {
            // 2014-12-25 getSimpleDateFormat(1).format(value) -- > getSimpleDateFormat(3).format(value);
            return getSimpleDateFormat(3).format(value);
        }
        return value.toString();
    }

    /**
     * 根据输入的日期计算年龄
     * 
     * @param birthday
     *            出生日期
     * @return String 年龄（周岁年龄）
     */

    public static int getAge(Date birthday) {
        Calendar birthdays = Calendar.getInstance();
        birthdays.setTime(birthday);
        Calendar today = new GregorianCalendar();
        int age = today.get(Calendar.YEAR) - birthdays.get(Calendar.YEAR);
        birthdays.add(Calendar.YEAR, age);
        if (today.before(birthdays)) {
            age--;
        }
        return age;
    }

    /**
     * 判断2个时间月中天数
     * 
     * @author
     * @param birthday
     * @return int
     */
    public static boolean checkDay(Date birthday) {
        Calendar birthdays = Calendar.getInstance();
        birthdays.setTime(birthday);
        Calendar today = Calendar.getInstance();
        String b = birthdays.get(Calendar.MONTH) + "" + birthdays.get(Calendar.DATE);
        String t = today.get(Calendar.MONTH) + "" + today.get(Calendar.DATE);
        return Integer.parseInt(b) <= Integer.parseInt(t);
    }

    /**
     * 获取指定时间季度最后一天日期
     * 
     * @author
     * @param date
     * @return
     * @throws
     */
    public static Date getLastDayOfQuarter(Date date) {
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(Calendar.DAY_OF_MONTH, 1);
        int curMonth = cDay.get(Calendar.MONTH);
        if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {
            cDay.set(Calendar.MONTH, Calendar.MARCH);
        }
        if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {
            cDay.set(Calendar.MONTH, Calendar.JUNE);
        }
        if (curMonth >= Calendar.JULY && curMonth <= Calendar.SEPTEMBER) {
            cDay.set(Calendar.MONTH, Calendar.SEPTEMBER);
        }
        if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
            cDay.set(Calendar.MONTH, Calendar.DECEMBER);
        }
        curMonth = cDay.get(Calendar.MONTH);
        cDay.set(Calendar.MONTH, curMonth + 1);
        cDay.set(Calendar.DAY_OF_MONTH, 1);
        cDay.add(Calendar.DAY_OF_YEAR, -1);
        return cDay.getTime();
    }

    public static Date getDateTimeBegin(Date dateTime) {
        if (dateTime != null) {
            String str = String.format("%s 00:00", DateUtil.getDateFormat(dateTime));
            try {
                return DateUtil.getDateParse(9, str);
            } catch (Exception e) {
                return dateTime;
            }
        }
        return null;
    }

    public static Date getDateTimeEnd(Date dateTime) {
        if (dateTime != null) {
            String str = String.format("%s 23:59", DateUtil.getDateFormat(dateTime));
            try {
                return DateUtil.getDateParse(9, str);
            } catch (Exception e) {
                return dateTime;
            }
        }
        return null;
    }

}
