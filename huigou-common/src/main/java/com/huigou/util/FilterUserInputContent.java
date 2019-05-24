package com.huigou.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * 使用 jsoup 来对用户输入内容进行过滤
 * 
 * @author xx
 */
public class FilterUserInputContent {
    private final static Whitelist USER_CONTENT_FILTER = Whitelist.relaxed();
    static {
        USER_CONTENT_FILTER.addTags("embed", "object", "param", "span", "div");
        USER_CONTENT_FILTER.addAttributes(":all", "style", "class", "id", "name");
        USER_CONTENT_FILTER.addAttributes("object", "width", "height", "classid", "codebase");
        USER_CONTENT_FILTER.addAttributes("param", "name", "value");
        USER_CONTENT_FILTER.addAttributes("embed", "src", "quality", "width", "height", "allowFullScreen", "allowScriptAccess", "flashvars", "name", "type",
                                          "pluginspage");
    }

    /**
     * 对用户输入内容进行过滤
     * 
     * @param html
     * @return
     */
    public static String doFilter(String html) {
        if (StringUtil.isBlank(html)) return "";
        // jsoup不支持相对路径图片的过滤，比如<img src=”/1.png” alt=”” />会被去掉src属性，使用增加baseUri的方法避免
        return Jsoup.clean(html, "http://xxbaseuri", USER_CONTENT_FILTER).replaceAll("src=\"http://xxbaseuri", "src=\"").replaceAll("\n", "");
        // return Jsoup.clean(html, USER_CONTENT_FILTER);
    }

}
