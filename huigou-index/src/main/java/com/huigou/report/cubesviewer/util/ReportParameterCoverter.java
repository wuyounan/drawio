package com.huigou.report.cubesviewer.util;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.huigou.index.DateConstant;
import com.huigou.util.CommonUtil;

/**
 * 报表参数转换器
 * 
 * @author gongmm
 */
public class ReportParameterCoverter {
    
    private static final String YEAR_MONTH_DAY_FORMATTER = "yyyy-MM-dd";

    public static String covert(String parameterName) {
        Date currentDate = CommonUtil.getCurrentDate();
        int year, yearMonth;
        String result;
        Date coveredDateValue;
        switch (parameterName) {
        case DateConstant.THIS_YEAR_PARAM:
            year = CommonUtil.getYear(currentDate);
            result = String.valueOf(year);
            break;
        case DateConstant.THIS_YEAR_MONTH_PARAM:
            yearMonth = CommonUtil.getYear(currentDate) * 100 + CommonUtil.getMonth(currentDate);
            result = String.valueOf(yearMonth);
            break;
        case DateConstant.FIRST_DATE_OF_THIS_YEAR_PARAM:
            coveredDateValue = CommonUtil.getFirstDateOfYear(currentDate);
            result = new SimpleDateFormat(YEAR_MONTH_DAY_FORMATTER).format(coveredDateValue);
            break;
        case DateConstant.FIRST_DATE_OF_THIS_MONTH_PARAM:
            coveredDateValue = CommonUtil.getFirstDateOfMonth(currentDate);
            result = new SimpleDateFormat(YEAR_MONTH_DAY_FORMATTER).format(coveredDateValue);
            break;
        case DateConstant.TODAY_PARAM:
            result = new SimpleDateFormat(YEAR_MONTH_DAY_FORMATTER).format(currentDate);
            break;
        case DateConstant.YESTERDAY_PARAM:
            coveredDateValue = CommonUtil.getYesterday();
            result = new SimpleDateFormat(YEAR_MONTH_DAY_FORMATTER).format(coveredDateValue);
            break;
        default:
            throw new IllegalStateException(String.format("无效的自定义函数“%s”。", parameterName));
        }
        return result;
    }

}
