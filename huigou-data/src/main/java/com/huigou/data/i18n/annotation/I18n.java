package com.huigou.data.i18n.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface I18n {
    /**
     * 需要国际化的字段
     * 
     * @return
     */
    String name() default "";

    /**
     * 国际化编码
     * 
     * @return
     */
    String code() default "";

    /**
     * 未找到国际化资源时的默认值
     * 
     * @return
     */
    String defaultName() default "";
}
