package com.huigou.uasp.client.views;

import org.springframework.util.Assert;

import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.util.StringUtil;

/**
 * JSTL标签库自定义函数库
 * 
 * @author gongmm
 */
public class JSTLFunction {
    /**
     * 根据personId判断两用户是否相同
     * 
     * @param str
     * @return int
     */
    public static boolean theSamePerson(String psmId1, String psmId2) {
        if (StringUtil.isBlank(psmId1) || StringUtil.isBlank(psmId2)) {
            return false;
        }
        String personId1 = psmId1.split("@")[0];
        String personId2 = psmId2.split("@")[0];
        return personId1.equals(personId2);
    }

    /**
     * 格式化数据显示
     * 
     * @author
     * @param value
     * @param type
     * @return String
     */
    public static String format(Object value, String type) {
        return TaglibUtil.formatData(value, type).toString();
    }

    /**
     * 星期显示
     * 
     * @author
     * @param i
     * @return String
     */
    public static String getWeekDay(Integer i) {
        String[] array = new String[] { "日", "一", "二", "三", "四", "五", "六" };
        if (i == null || i >= array.length) {
            return "";
        }
        return "星期" + array[i];
    }

    /**
     * 判断一个字符串是否包含了另一个字符串
     * 
     * @author
     * @param str1
     * @param str2
     * @return
     * @throws
     */
    public static boolean checkContains(String str1, String str2) {
        if (StringUtil.isBlank(str1) || StringUtil.isBlank(str2)) {
            return false;
        }
        String[] str = str1.split(",");
        for (String s : str) {
            if (s.equals(str2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 页面获取系统参数
     * 
     * @param code
     * @return
     */
    public static String systemParameter(String code) {
        Assert.hasText(code, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "code"));
        String paramValue = SystemCache.getParameter(code, String.class);
        if (StringUtil.isBlank(paramValue)) {
            return null;
        }
        return paramValue;
    }
}
