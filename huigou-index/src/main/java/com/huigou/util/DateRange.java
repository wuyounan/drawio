package com.huigou.util;

import java.text.SimpleDateFormat;

import com.huigou.util.StringUtil;

public class DateRange {

    private static final String YEAR_MONTH_PATTERN = "yyyy-MM";

    private String startYearMonth;

    private String endYearMonth;

    public static final String CURRENT_YEAR_MONTH_RANGE = "currentYearMonthRange";
    
    public static final String CURRENT_YEAR_MONTH = "yearMonth";

    public static DateRange fromKind(String kind) {
        if (StringUtil.isBlank(kind)) {
            kind = DateRange.CURRENT_YEAR_MONTH;
        }
        DateRange result = new DateRange();
        String yearMonth = new SimpleDateFormat(YEAR_MONTH_PATTERN).format(new java.util.Date());
        switch (kind) {
        case CURRENT_YEAR_MONTH_RANGE:
            result.startYearMonth = yearMonth;
            result.endYearMonth = yearMonth;
            break;
        case CURRENT_YEAR_MONTH:
            result.startYearMonth = yearMonth;
            break;
        }
        return result;
    }

    public String getStartYearMonth() {
        return startYearMonth;
    }

    public void setStartYearMonth(String startYearMonth) {
        this.startYearMonth = startYearMonth;
    }

    public String getEndYearMonth() {
        return endYearMonth;
    }

    public void setEndYearMonth(String endYearMonth) {
        this.endYearMonth = endYearMonth;
    }

}
