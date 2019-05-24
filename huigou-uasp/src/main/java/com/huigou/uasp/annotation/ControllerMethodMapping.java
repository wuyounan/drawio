package com.huigou.uasp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * spring mvc url路径映射
 * 
 * @author xx
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerMethodMapping {
    /**
     * 是否需要结合类注解 默认false
     * 
     * @return
     */
    boolean combine() default false;

    public abstract String[] value();

    RequestMethod[] method() default {};
}
