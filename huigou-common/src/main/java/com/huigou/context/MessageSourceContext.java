package com.huigou.context;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;

import com.huigou.cache.SystemCache;

/**
 * 国际化资源环境控制
 * 
 * @author xx
 */
public class MessageSourceContext {
    public static final String LOCALE_SESSION_ATTRIBUTE_NAME = MessageSourceContext.class.getName() + ".LOCALE";

    private static LocaleResolver localeResolver;

    private static MessageSource messageSource;

    @SuppressWarnings("static-access")
    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @SuppressWarnings("static-access")
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public static Locale getLocale() {
        try {
            Locale locale = (Locale) SecurityUtils.getSubject().getSession().getAttribute(LOCALE_SESSION_ATTRIBUTE_NAME);
            if (locale == null) {
                locale = LocaleContextHolder.getLocale();
            }
            return locale;
        } catch (Exception e) {
            return Locale.CHINA;
        }
    }

    public static void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        localeResolver.setLocale(request, response, locale);
        SecurityUtils.getSubject().getSession().setAttribute(LOCALE_SESSION_ATTRIBUTE_NAME, locale);
    }

    public static void setLocale(HttpServletRequest request, HttpServletResponse response, String lang, String country) {
        setLocale(request, response, new Locale(lang, country));
    }

    public static void setLocale(HttpServletRequest request, HttpServletResponse response) {
        try {
            setLocale(request, response, localeResolver.resolveLocale(request));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取缓存中的国际化资源
     * 
     * @param code
     * @param languageKey
     * @return
     */
    private static String getSystemCacheMessage(String code, String languageKey) {
        Map<String, String> map = SystemCache.getI18nProperties(languageKey);
        if (map != null && map.size() > 0) {
            if (map.containsKey(code)) {
                return map.get(code);
            }
        }
        return null;
    }

    public static String getMessage(String code, Object... args) {
        Locale locale = getLocale();
        // 首先读取缓存中数据库国际化资源
        String languageKey = String.format("%s_%s", locale.getLanguage(), locale.getCountry());
        String message = getSystemCacheMessage(code, languageKey);
        if (null != message) {
            return message;
        }
        if (!locale.getLanguage().equals(Locale.CHINA.getLanguage())) {
            // 默认读取中文配置
            languageKey = String.format("%s_%s", Locale.CHINA.getLanguage(), Locale.CHINA.getCountry());
            message = getSystemCacheMessage(code, languageKey);
            if (null != message) {
                return message;
            }
        }
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (Exception e) {
            return code;
        }
    }

    public static String getMessageAsDefault(String code, String defaultValue) {
        Locale locale = getLocale();
        // 首先读取缓存中数据库国际化资源
        String languageKey = String.format("%s_%s", locale.getLanguage(), locale.getCountry());
        String message = getSystemCacheMessage(code, languageKey);
        if (null != message) {
            return message;
        }
        if (!locale.getLanguage().equals(Locale.CHINA.getLanguage())) {
            // 默认读取中文配置
            languageKey = String.format("%s_%s", Locale.CHINA.getLanguage(), Locale.CHINA.getCountry());
            message = getSystemCacheMessage(code, languageKey);
            if (null != message) {
                return message;
            }
        }
        try {
            return messageSource.getMessage(code, new Object[] {}, locale);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getMessageAsDefault(String code) {
        return getMessageAsDefault(code, code);
    }
}
