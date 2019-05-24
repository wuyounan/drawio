package com.huigou.uasp.client.views;

import com.huigou.util.DateUtil;
import com.huigou.util.StringUtil;

public class TaglibUtil {
    public static Object formatData(Object obj, String type) {
        if (StringUtil.isBlank(type)) {
            return obj;
        }
        if (obj != null) {
            if (type.toLowerCase().equals("text") || type.toLowerCase().equals("dictionary")) {
                return obj.toString();
            } else if (type.toLowerCase().equals("date") && obj instanceof java.util.Date) {
                return DateUtil.getDateFormat(1, (java.util.Date) obj);
            } else if (type.toLowerCase().equals("datetime") && obj instanceof java.util.Date) {
                return DateUtil.getDateFormat(9, (java.util.Date) obj);
            } else if (type.toLowerCase().equals("money")) {
                return StringUtil.formatToCurrency(obj.toString());
            } else if (!StringUtil.isBlank(obj.toString())) {
                try {
                    int i = 0;
                    if (type.indexOf(".") > -1) {
                        String temp = type.split("\\.")[1];
                        i = temp.length();
                    } else {
                        i = StringUtil.toInt(type);
                        if (i < 0 || i > 9) {
                            return obj;
                        }
                    }
                    return StringUtil.keepDigit(obj.toString(), i, true);
                } catch (Exception e) {
                    return obj;
                }
            }
        }
        return "";
    }

    public static String getFontAwesome(String type) {
        if (type == null) {
            return "fa-caret-down";
        }
        type = type.toLowerCase();
        if (type.equals("select")) {
            return "fa-ellipsis-h";
        } else if (type.equals("tree")) {
            return "fa-caret-down";
        } else if (type.equals("date") || type.equals("datetime")) {
            return "fa-calendar";
        } else if (type.equals("month")) {
            return "fa-calendar-o";
        } else {
            return "fa-caret-down";
        }
    }
}
