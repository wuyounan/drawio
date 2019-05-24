package com.huigou.data.i18n.aspect;

import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.huigou.data.i18n.annotation.I18n;
import com.huigou.data.i18n.annotation.I18nMapping;
import com.huigou.data.i18n.model.I18nModel;
import com.huigou.util.ClassHelper;

/**
 * 国际化处理后台返回数据切面
 * 
 * @author xx
 */
@Component
@Aspect
public class I18nAnnotationAspect extends I18nAspect {

    /**
     * 循环执行切入点
     * 
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.huigou.data.i18n.annotation.I18nMapping)")
    public Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            throw e;
        }
        // 没有返回值 或返回值为基础数据类型这里不做处理
        if (result == null || ClassHelper.isBaseType(result.getClass())) {
            return result;
        }
        MethodSignature ms = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = ms.getMethod();
        // 寻找国际化注解
        I18nMapping methodAnnotation = AnnotationUtils.findAnnotation(method, I18nMapping.class);
        if (methodAnnotation == null) {// 没有注解直接返回
            return result;
        }
        I18n[] i18nAnnotations = methodAnnotation.i18n();
        if (i18nAnnotations == null || i18nAnnotations.length == 0) {// 没有注解直接返回
            return result;
        }
        List<I18nModel> i18ns = I18nModel.initByAnnotation(i18nAnnotations);
        String keyName = methodAnnotation.keyName();
        return this.process(result, keyName, i18ns);
    }

}
