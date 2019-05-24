package com.huigou.data.query;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xmlbeans.XmlObject;

public class XMLParseUtil {
    /**
     * 正则表达式去除空格，制表符及换行符
     */
    public static final Pattern pattern = Pattern.compile("\t|\r|\n");

    /**
     * 获取xml节点数据
     * 
     * @author
     * @param obj
     * @return String
     */
    public static String getNodeTextValue(XmlObject obj) {
        String value = obj.newCursor().getTextValue();
        Matcher matcher = pattern.matcher(value);
        return matcher.replaceAll("");
    }
}
