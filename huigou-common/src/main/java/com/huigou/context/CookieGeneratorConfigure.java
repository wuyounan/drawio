package com.huigou.context;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.util.CookieGenerator;

import com.huigou.util.StringUtil;

/**
 * CookieGenerator 配置
 * 
 * @author xx
 */
public class CookieGeneratorConfigure implements InitializingBean {

    private CookieGenerator cookieGenerator;

    private String cookieName;

    private String cookieDomain;

    private String cookiePath;

    private Integer cookieMaxAge;

    private Boolean cookieSecure;

    private Boolean cookieHttpOnly;

    public void setCookieGenerator(CookieGenerator cookieGenerator) {
        this.cookieGenerator = cookieGenerator;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public void setCookieMaxAge(Integer cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public void setCookieSecure(Boolean cookieSecure) {
        this.cookieSecure = cookieSecure;
    }

    public void setCookieHttpOnly(Boolean cookieHttpOnly) {
        this.cookieHttpOnly = cookieHttpOnly;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtil.isNotBlank(this.cookieName)) {
            cookieGenerator.setCookieName(cookieName);
        }
        if (StringUtil.isNotBlank(this.cookieDomain)) {
            cookieGenerator.setCookieDomain(cookieDomain);
        }
        if (StringUtil.isNotBlank(this.cookiePath)) {
            cookieGenerator.setCookiePath(cookiePath);
        }
        if (this.cookieMaxAge != null) {
            cookieGenerator.setCookieMaxAge(cookieMaxAge);
        }
        if (this.cookieSecure != null) {
            cookieGenerator.setCookieSecure(cookieSecure);
        }
        if (this.cookieHttpOnly != null) {
            cookieGenerator.setCookieHttpOnly(cookieHttpOnly);
        }
    }

}
