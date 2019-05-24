package com.huigou.uasp.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogInfo {

	/**
	 * 应用编码
	 */
	String appCode() default "";

	/**
	 * 日志类型
	 * 
	 * @return
	 */
	LogType logType() default LogType.BIZ;

	/**
	 * 子类型
	 * 
	 * @return
	 */
	String subType() default "";

	/**
	 * 操作类型
	 * 
	 * @return
	 */
	OperationType operaionType();

	/**
	 * 描述
	 */
	String description() default "";
}
