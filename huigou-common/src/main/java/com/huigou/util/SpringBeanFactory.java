package com.huigou.util;

import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 根据服务器上下文获取 Spring 管理的bean
 * 
 * @ClassName: SpringBeanFactory
 * @Description: TODO
 * @author 
 * @date 2014-1-4 上午12:03:14
 * @version V1.0
 */
public class SpringBeanFactory {
    /**
     * 根据服务器上下文获取 Spring 管理的bean
     * 
     * @Title: getBean
     * @author 
     * @Description: TODO
     * @param servletContext
     * @param name
     * @return Object
     * @throws
     */
    public static Object getBean(ServletContext servletContext, String name) {
        if (servletContext == null) return null;
        ApplicationContext d = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        return d.getBean(name);
    }

    public static <T> T getBean(ServletContext servletContext, String name, Class<T> type) {
        if (servletContext == null) return null;
        ApplicationContext d = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        return d.getBean(name, type);
    }
}
