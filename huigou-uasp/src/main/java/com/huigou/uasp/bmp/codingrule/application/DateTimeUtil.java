package com.huigou.uasp.bmp.codingrule.application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huigou.util.StringUtil;

public class DateTimeUtil {

    private static final Log logger = LogFactory.getLog(DateTimeUtil.class);

    private static final String CHINESE_DATE_FORMAT = "yyyy年MM月dd日";

    private static final String YEAR_FORMAT = "yyyy";

    private static final String YEAR_MONTH_FORMAT = "yyyyMM";

    private static final String YEAR_MONTH_DAY_FORMAT = "yyyyMMdd";

    private static final String YEAR_NAME = "年";

    private static final String MONTH_NAME = "月";

    private static final String DAY_NAME = "日";

    public static String[] getTimeFormat(Date date) {
        String week = new SimpleDateFormat("EEEE").format(date);
        String enDateL = DateFormat.getDateInstance(1, Locale.ENGLISH).format(date);
        String enDateM = DateFormat.getDateInstance(2, Locale.ENGLISH).format(date);
        String enDateF = DateFormat.getDateInstance(0, Locale.ENGLISH).format(date);

        String enYear = enDateL.substring(enDateL.lastIndexOf(" ") + 1, enDateL.length());
        String enMonthL = enDateL.substring(0, enDateL.indexOf(" "));
        String enMonthM = enDateM.substring(0, enDateL.indexOf(" ") - 1);
        String enDay = enDateL.substring(enDateL.indexOf(" ") + 1, enDateL.indexOf(","));
        String enWeek = enDateF.substring(0, enDateF.indexOf(","));

        String zhDate = new SimpleDateFormat(CHINESE_DATE_FORMAT).format(date);

        String chineseDate = translateDateToChinese(date);

        String[] timeFormat = {
                               new SimpleDateFormat(YEAR_FORMAT).format(date),
                               new SimpleDateFormat(YEAR_MONTH_FORMAT).format(date),
                               new SimpleDateFormat(YEAR_MONTH_DAY_FORMAT).format(date),
                               zhDate,
                               zhDate.substring(0, zhDate.indexOf(MONTH_NAME) + 1),
                               zhDate.substring(zhDate.indexOf(YEAR_NAME), zhDate.length()),
                               chineseDate,
                               chineseDate.substring(0, chineseDate.indexOf(MONTH_NAME) + 1),
                               chineseDate.substring(5, chineseDate.length()),
                               week.substring(week.indexOf("期") + 1, week.length()),
                               DateFormat.getDateInstance(2).format(date),
                               DateFormat.getDateTimeInstance(2, 2).format(date)
                                         .substring(0, DateFormat.getDateTimeInstance(2, 2).format(date).lastIndexOf(":")),
                               DateFormat.getDateInstance(2).format(date)
                                               + " "
                                               + DateFormat.getDateTimeInstance(3, 3, Locale.ENGLISH)
                                                           .format(date)
                                                           .substring(DateFormat.getDateTimeInstance(3, 3, Locale.ENGLISH).format(date).indexOf(" ") + 1,
                                                                      DateFormat.getDateTimeInstance(3, 3, Locale.ENGLISH).format(date).length()),
                               DateFormat.getDateInstance(3).format(date),
                               DateFormat.getDateInstance(3).format(date).substring(3, DateFormat.getDateInstance(3).format(date).length()),
                               new SimpleDateFormat("M-dd-yy").format(date), new SimpleDateFormat("MM-dd-yy").format(date),
                               new SimpleDateFormat("yyyy-MM-dd").format(date), new SimpleDateFormat("yyyy MM dd").format(date),
                               DateFormat.getDateInstance(3, Locale.ENGLISH).format(date), enDay + "-" + enMonthM, enDay + "-" + enMonthM + "-" + enYear,
                               enMonthM + "-" + enDay, enMonthL + "-" + enDay, DateFormat.getDateInstance(0, Locale.ENGLISH).format(date),
                               enDay + " " + enMonthL + ", " + enYear, enWeek + " " + new SimpleDateFormat("yyyy MM dd").format(date),
                               new SimpleDateFormat("yy").format(date), new SimpleDateFormat("MM").format(date), new SimpleDateFormat("yyMM").format(date) };

        for (int index = 0; index < timeFormat.length; index++) {
            if ((timeFormat[index] == null) || (timeFormat[index].length() == 0)) {
                timeFormat[index] = new SimpleDateFormat("yyyy").format(date);
            }
        }
        return timeFormat;
    }

    public static String getSystemDate(String format) {
        String result = "";

        int index = 0;
        try {
            index = Integer.parseInt(format);
        } catch (NumberFormatException e) {
            logger.info("CodingRule timeformat error(now use defaultformat): " + e.getMessage());
            index = Integer.parseInt("17");
        }
        String[] sysTimeFormat1 = getTimeFormat(new Date());
        String[] sysTimeFormat = new String[sysTimeFormat1.length];
        int i = 0;
        for (int n = 0; i < sysTimeFormat1.length; ++i)
            if (StringUtil.isNotBlank(sysTimeFormat1[i])) {
                sysTimeFormat[(n++)] = sysTimeFormat1[i];
            }

        result = sysTimeFormat[index];
        return result;
    }

    private static String translateDateToChinese(Date date) {
        String result = "";

        String dateStr = new SimpleDateFormat("yyyyMMdd").format(date);

        if (dateStr.substring(4, 5).equals("0")) {
            result = translateChineseNumber(dateStr.substring(0, 4)) + "年" + translateChineseNumber(dateStr.substring(5, 6)) + MONTH_NAME;
            // 20171212
            if (dateStr.substring(6, 7).equals("0")) { // 01-09
                result = result + translateChineseNumber(dateStr.substring(7, 8)) + DAY_NAME;
            } else if (dateStr.substring(7, 8).equals("0")) { //
                if (dateStr.substring(6, 7).equals("1")) { // 10
                    result = result + "十日";
                } else { // 20 30
                    result = result + translateChineseNumber(dateStr.substring(6, 7)) + "十日";
                }

            } else if (dateStr.substring(6, 7).equals("1")) {
                result = result + "十" + translateChineseNumber(dateStr.substring(7, 8)) + DAY_NAME;
            } else {
                result = result + translateChineseNumber(dateStr.substring(6, 7)) + "十" + translateChineseNumber(dateStr.substring(7, 8)) + DAY_NAME;
            }

        } else {
            result = translateChineseNumber(dateStr.substring(0, 4)) + "年十";

            if (dateStr.substring(5, 6).equals("0")) {
                result = result + MONTH_NAME;
                if (dateStr.substring(6, 7).equals("0")) {
                    result = result + translateChineseNumber(dateStr.substring(7, 8)) + DAY_NAME;
                } else if (dateStr.substring(7, 8).equals("0")) {
                    if (dateStr.substring(6, 7).equals("1")) result = result + "十日";
                    else {
                        result = result + translateChineseNumber(dateStr.substring(6, 7)) + "十日";
                    }

                } else if (dateStr.substring(6, 7).equals("1")) {
                    result = result + "十" + translateChineseNumber(dateStr.substring(7, 8)) + "";
                } else {
                    result = result + translateChineseNumber(dateStr.substring(6, 7)) + "十" + translateChineseNumber(dateStr.substring(7, 8)) + DAY_NAME;
                }

            } else {
                result = result + translateChineseNumber(dateStr.substring(5, 6)) + MONTH_NAME;

                if (dateStr.substring(6, 7).equals("0")) {
                    result = result + translateChineseNumber(dateStr.substring(7, 8)) + DAY_NAME;
                } else if (dateStr.substring(7, 8).equals("0")) {
                    if (dateStr.substring(6, 7).equals("1")) result = result + "十日";
                    else {
                        result = result + translateChineseNumber(dateStr.substring(6, 7)) + "十日";
                    }

                } else if (dateStr.substring(6, 7).equals("1")) {
                    result = result + "十" + DAY_NAME;
                } else {
                    result = result + translateChineseNumber(dateStr.substring(6, 7)) + "十" + translateChineseNumber(dateStr.substring(7, 8)) + DAY_NAME;
                }

            }

        }

        return result;
    }

    private static String translateChineseNumber(String s) {
        String result = "";
        String[] chinese = { "○", "一", "二", "三", "四", "五", "六", "七", "八", "九" };

        for (int i = 0; i < s.length(); ++i) {
            int ii = Integer.parseInt(s.substring(i, i + 1));
            result = result + chinese[ii];
        }

        return result;
    }

}
