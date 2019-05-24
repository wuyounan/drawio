package com.huigou.util;

import org.springframework.context.ApplicationContext;

/**
 * spring 环境包装类
 * 
 * @author xx
 */
public class ApplicationContextWrapper {
    private ApplicationContext applicationContext;

    static class ApplicationContextHolder {
        static ApplicationContextWrapper instance = new ApplicationContextWrapper();
    }

    public static ApplicationContextWrapper getInstance() {
        return ApplicationContextHolder.instance;
    }

    public void setApplicationContext(ApplicationContext context) {
        this.applicationContext = context;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public synchronized static void init(ApplicationContext context) {
        getInstance().setApplicationContext(context);
    }

    public static ApplicationContext getContext() {
        return getInstance().getApplicationContext();
    }

    public static Object getBean(String beanName) {
        if (getInstance().getApplicationContext() == null) {
            return null;
        }
        return getInstance().getApplicationContext().getBean(beanName);
    }

    /**
     * 根据服务器上下文获取 Spring 管理的bean
     * 
     * @param name
     * @param type
     * @return
     */
    public static <T> T getBean(String name, Class<T> type) {
        if (getInstance().getApplicationContext() == null) {
            return null;
        }
        return getInstance().getApplicationContext().getBean(name, type);
    }
}
