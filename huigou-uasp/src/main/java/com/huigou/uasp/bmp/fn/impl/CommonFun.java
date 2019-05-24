package com.huigou.uasp.bmp.fn.impl;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.huigou.express.AbstractFunction;
import com.huigou.express.VariableContainer;
import com.huigou.util.ClassHelper;
import com.huigou.util.CommonUtil;
import com.huigou.util.DateUtil;
import com.huigou.util.JSONUtil;
import com.huigou.util.StringUtil;

/**
 * 通用函数
 * 
 * @author Administrator
 */
@Service("commonFun")
public class CommonFun extends AbstractFunction {

    /**
     * 解码Map
     * 
     * @param value
     * @return
     */
    public Map<String, Object> decodeMap(String value) {
        return CommonUtil.decodeMap(value);
    }

    /**
     * 编码Map
     * 
     * @param map
     * @return
     */
    public String encodeMap(Map<String, Object> map) {
        return CommonUtil.encodeMap(map);
    }

    /**
     * 得到GUID
     * 
     * @return
     */
    public String GUID() {
        return CommonUtil.createGUID();
    }

    /**
     * 当前日期时间
     * 
     * @return
     */
    public Timestamp currentDateTime() {
        return CommonUtil.getCurrentDateTime();
    }

    /**
     * 当前日期
     * 
     * @return
     */
    public java.sql.Date currentDate() {
        return CommonUtil.getCurrentDate();
    }

    /**
     * 当前时间
     * 
     * @return
     */
    public Time currentTime() {
        return CommonUtil.getCurrentTime();
    }

    /**
     * 获取给定日期的年份
     * 
     * @param date
     * @return
     */
    public int year(java.util.Date date) {
        return CommonUtil.getYear(date);
    }

    /**
     * 获取给定日期的月份
     * 
     * @param date
     * @return
     */
    public int month(java.util.Date date) {
        return CommonUtil.getMonth(date);
    }

    /**
     * 获取给定日期的天
     * 
     * @param date
     * @return
     */
    public static int day(java.util.Date date) {
        return CommonUtil.getDay(date);
    }

    /**
     * 获取给定时间的小时
     * 
     * @param date
     * @return
     */
    public int hour(java.util.Date date) {
        return CommonUtil.getHour(date);
    }

    /**
     * 获取给定时间的分钟
     * 
     * @param date
     * @return
     */
    public int minute(java.util.Date date) {
        return CommonUtil.getMinute(date);
    }

    /**
     * 获取给定日期所在年的第一天
     * 
     * @param date
     * @return
     */
    public java.sql.Date firstDateOfYear(java.util.Date date) {
        return CommonUtil.getFirstDateOfYear(date);
    }

    /**
     * 获取给定日期所在年的最后一天
     * 
     * @param date
     * @return
     */
    public static java.sql.Date lastDateOfYear(java.util.Date date) {
        return CommonUtil.getLastDateOfYear(date);
    }

    /**
     * 获取给定日期所在周的第几天
     * 
     * @param date
     * @return
     */
    public int dayOfWeek(java.util.Date date) {
        return CommonUtil.getDayOfWeek(date);
    }

    /**
     * 根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串
     * 
     * @author
     * @param name
     * @return String
     */
    public String chineseLetter(String name) {
        return StringUtil.getFirstLetter(name);
    }

    /**
     * 时间间隔月数
     * 
     * @author
     * @param name
     * @return String
     */
    public int getIntervalMonth(Object start, Object end) {
        Date startDate = ClassHelper.convert(start, Date.class);
        Date endDate = ClassHelper.convert(end, Date.class);
        if (startDate == null || endDate == null) {
            return 0;
        }
        return DateUtil.getIntervalMonth(startDate, endDate);
    }

    /**
     * 用指定分割符分割参数
     * 
     * @param key
     * @param regex
     * @return
     */
    public String[] splitParam(String key, Object regex) {
        String param = VariableContainer.getVariable(key, String.class);
        if (StringUtil.isBlank(param)) {
            return null;
        }
        return param.split(regex.toString());
    }

    /**
     * 参数中取StringList
     * 
     * @param key
     * @return
     */
    public List<String> paramToStringList(String key) {
        String param = VariableContainer.getVariable(key, String.class);
        if (StringUtil.isBlank(param)) {
            return null;
        }
        return JSONUtil.toList(param, String.class);
    }

}
