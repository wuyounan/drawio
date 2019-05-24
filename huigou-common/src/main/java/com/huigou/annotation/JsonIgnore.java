package com.huigou.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 忽略JSON序列化
 * 
 * @author gongmm
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface JsonIgnore {
    boolean value() default true;
}
