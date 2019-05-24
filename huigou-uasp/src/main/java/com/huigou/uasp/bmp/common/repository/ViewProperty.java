package com.huigou.uasp.bmp.common.repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.huigou.uasp.annotation.ViewField;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ViewProperty {
	ViewField[] viewFields() default { @ViewField(view = "digest") };
	
}
