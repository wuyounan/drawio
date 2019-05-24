package com.huigou.util;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.huigou.exception.ApplicationException;

/**
 * 日期范围
 * 
 * @author gongmm
 */
public enum DateRange {
    ALL(1, "全部"),
    TODAY(2, "今天"),
    YESTERDAY(3, "昨天"),
    THIS_WEEK(4, "本周"),
    LAST_WEEK(5, "上周"),
    DAY_7(11, "最近7天"),
    DAY_30(6, "最近30天"),
    DAY_90(7, "最近90天"),
    THIS_YEAR(8, "今年"),
    LAST_YEAR(9, "去年"),
    CUSTOM(10, "自定义");

    private final int id;

    private final String displayName;

    private DateRange(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static DateRange fromId(int id) {
        DateRange result = ALL;
        switch (id) {
        case 1:
            result = ALL;
            break;
        case 2:
            result = TODAY;
            break;
        case 3:
            result = YESTERDAY;
            break;
        case 4:
            result = THIS_WEEK;
            break;
        case 5:
            result = LAST_WEEK;
            break;
        case 6:
            result = DAY_30;
            break;
        case 7:
            result = DAY_90;
            break;
        case 8:
            result = THIS_YEAR;
            break;
        case 9:
            result = LAST_YEAR;
            break;
        case 10:
            result = CUSTOM;
            break;
        case 11:
            result = DAY_7;
            break;
        default:
            throw new ApplicationException(String.format("无效的日期范围“%s”。", new Object[] { String.valueOf(id) }));
        }
        return result;
    }

    public static Map<Integer, String> getData() {
        Map<Integer, String> result = new LinkedHashMap<Integer, String>(10);
        for (DateRange item : DateRange.values()) {
            result.put(item.getId(), item.getDisplayName());
        }
        return result;
    }

    public static Map<String, Object> getDataRange(DateRange item) {
        Map<String, Object> dataRange = new HashMap<String, Object>(2);
        Date currentDate = CommonUtil.getCurrentDate();
        Date tomorrow = CommonUtil.getTomorrow();
        switch (item) {
        case TODAY:
            dataRange.put("startDate", DateUtil.getDateTimeBegin(currentDate));
            dataRange.put("endDate", DateUtil.getDateTimeEnd(tomorrow));
            break;
        case YESTERDAY:
            dataRange.put("startDate", DateUtil.getDateTimeBegin(CommonUtil.getYesterday()));
            dataRange.put("endDate", DateUtil.getDateTimeEnd(currentDate));
            break;
        case THIS_WEEK:
            dataRange.put("startDate", DateUtil.getDateTimeBegin(CommonUtil.getFirstDateOfWeek(currentDate)));
            dataRange.put("endDate", DateUtil.getDateTimeEnd(CommonUtil.getLastDateOfWeek(currentDate)));
            break;
        case LAST_WEEK:
            dataRange.put("startDate", DateUtil.getDateTimeBegin(CommonUtil.getFirstDateOfLastWeek(currentDate)));
            dataRange.put("endDate", DateUtil.getDateTimeEnd(CommonUtil.getLastDateOfLastWeek(currentDate)));
            break;
        case DAY_7:
            dataRange.put("startDate", DateUtil.getDateTimeBegin(CommonUtil.getStepDay(currentDate, -7)));
            dataRange.put("endDate", DateUtil.getDateTimeEnd(currentDate));
        case DAY_30:
            dataRange.put("startDate", DateUtil.getDateTimeBegin(CommonUtil.getStepDay(currentDate, -30)));
            dataRange.put("endDate", DateUtil.getDateTimeEnd(currentDate));
        case DAY_90:
            dataRange.put("startDate", DateUtil.getDateTimeBegin(CommonUtil.getStepDay(currentDate, -90)));
            dataRange.put("endDate", DateUtil.getDateTimeEnd(currentDate));
            break;
        case THIS_YEAR:
            dataRange.put("startDate", DateUtil.getDateTimeBegin(CommonUtil.getFirstDateOfYear(currentDate)));
            dataRange.put("endDate", DateUtil.getDateTimeEnd(CommonUtil.getLastDateOfYear(currentDate)));
            break;
        case LAST_YEAR:
            dataRange.put("startDate", DateUtil.getDateTimeBegin(CommonUtil.getFirstDateOfLastYear(currentDate)));
            dataRange.put("endDate", DateUtil.getDateTimeEnd(CommonUtil.getLastDateOfLastYear(currentDate)));
            break;
        default:
            break;
        }
        return dataRange;
    }

}
