package com.huigou.context;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huigou.cache.SystemCache;
import com.huigou.properties.PropertiesModel;
import com.huigou.properties.PropertiesReader;
import com.huigou.util.LogHome;
import com.huigou.util.StringUtil;

/**
 * ApplicationProperties配置
 * 
 * @author xx
 */
@Component("applicationProperties")
public class ApplicationProperties implements InitializingBean {
    @Autowired
    private PropertiesReader propertiesReader;

    private Map<String, String> properties;

    private String casflag;

    private String casServerUrlPrefix;

    private String casService;

    private String ldapServerUrlPrefix;

    private String ldapSystemUsername;

    private String ldapSystemPassword;

    private String ldapUserDnTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        properties = new HashMap<String, String>();
        try {
            PropertiesModel model = propertiesReader.loadConfigFile("/application.properties");
            properties.putAll(model.toMap());
        } catch (Exception e) {
            e.printStackTrace();
            LogHome.getLog(ApplicationProperties.class).error(e);
        }
        this.casflag = StringUtil.tryThese(properties.get("shiro.cas.flag"), "false");
        this.casServerUrlPrefix = StringUtil.tryThese(properties.get("shiro.cas.serverUrlPrefix"), "null");
        this.casService = StringUtil.tryThese(properties.get("shiro.cas.service"), "null");
        this.ldapServerUrlPrefix = StringUtil.tryThese(properties.get("shiro.ldap.serverUrlPrefix"), "null");
        this.ldapSystemUsername = StringUtil.tryThese(properties.get("shiro.ldap.systemUsername"), "null");
        this.ldapSystemPassword = StringUtil.tryThese(properties.get("shiro.ldap.systemPassword"), "null");
        this.ldapUserDnTemplate = StringUtil.tryThese(properties.get("shiro.ldap.userDnTemplate"), "null");
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getCasServerUrlPrefix() {
        return casServerUrlPrefix;
    }

    public String getCasService() {
        return casService;
    }

    public String getLdapServerUrlPrefix() {
        return ldapServerUrlPrefix;
    }

    public String getLdapSystemUsername() {
        return ldapSystemUsername;
    }

    public String getLdapSystemPassword() {
        return ldapSystemPassword;
    }

    public String getLdapUserDnTemplate() {
        return ldapUserDnTemplate;
    }

    public boolean isCas() {
        if (StringUtil.isBlank(casflag) || StringUtil.isBlank(casServerUrlPrefix) || StringUtil.isBlank(casService)) {
            return false;
        }
        if (!casflag.equals("true")) {
            return false;
        }
        return true;
    }

    /**
     * cas 服务器登录验证地址
     * 
     * @return
     */
    public String getCasServerLoginUrl() {
        if (!this.isCas()) {
            return null;
        }
        return String.format("%s/login", casServerUrlPrefix);
    }

    /**
     * 请求服务器验证URL
     * 
     * @param requestURI
     * @return "http://localhost:8085/cas/login?service=" + service;
     */
    public String getCasServerValidatorUrl(String requestURI) {
        if (!this.isCas()) {
            return null;
        }
        return String.format("%s?service=%s", getCasServerLoginUrl(), requestURI);
    }

    /**
     * 本系统登录地址
     * 
     * @return
     */
    public String getCasServiceLoginUrl() {
        if (!this.isCas()) {
            return null;
        }
        return String.format("%s/Login.jsp", casService);
    }

    /**
     * 其他路径验证地址
     * 
     * @param requestURI
     * @return
     */
    public String getServiceValidatorUrl(String requestURI) {
        if (!this.isCas()) {
            return null;
        }
        return String.format("%s%s", casService, requestURI.replaceAll(SystemCache.getContextPath(), ""));
    }

    /**
     * 组合CAS服务logout地址
     * 
     * @return http://localhost:8085/cas/logout?service=http://localhost:8080/yihe/Login.jsp
     */
    public String getCasServerLogoutUrl() {
        if (!this.isCas()) {
            return null;
        }
        return String.format("%s/logout?service=%s", getCasServerUrlPrefix(), getCasServiceLoginUrl());
    }

    /**
     * 返回Ldap 凭证名
     * 
     * @param userName
     * @return
     */
    public String getLdapUserDn(String userName) {
        return ldapUserDnTemplate.replaceAll("{0}", userName);
    }

    /**
     * 获取属性
     * 
     * @param key
     * @return
     */
    public String get(String key) {
        return this.properties.get(key);
    }

}
