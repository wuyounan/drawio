package com.huigou.data.i18n.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.huigou.data.i18n.annotation.I18n;
import com.huigou.data.i18n.annotation.I18nResource;
import com.huigou.data.i18n.model.I18nDataModel;
import com.huigou.data.i18n.model.I18nModel;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;

/**
 * 初始化后台国际化资源时使用(I18npropertiesApplicationImpl) 写入国际化资源数据库
 * 
 * @author xx
 */
@Component
@Aspect
public class I18nResourceInitAspect {

    /**
     * 循环执行切入点
     * 
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @SuppressWarnings("unchecked")
    @Around("@annotation(com.huigou.data.i18n.annotation.I18nResource)")
    public Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            throw e;
        }
        // 没有返回值 或返回值为基础数据类型这里不做处理
        if (result == null || ClassHelper.isBaseType(result.getClass())) {
            return null;
        }
        if (!ClassHelper.isInterface(result.getClass(), List.class)) {
            return null;
        }
        MethodSignature ms = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = ms.getMethod();
        // 寻找国际化注解
        I18nResource methodAnnotation = AnnotationUtils.findAnnotation(method, I18nResource.class);
        if (methodAnnotation == null) {// 没有注解直接返回
            return null;
        }
        I18n[] i18nAnnotations = methodAnnotation.i18n();
        if (i18nAnnotations == null || i18nAnnotations.length == 0) {// 没有注解直接返回
            return null;
        }
        List<I18nModel> i18ns = I18nModel.initByAnnotation(i18nAnnotations);
        return this.processList((List<Object>) result, i18ns);
    }

    /**
     * map中数据处理国际化
     * 
     * @param map
     * @param i18ns
     * @return
     */
    protected List<I18nDataModel> processMap(Map<String, Object> map, List<I18nModel> i18ns) {
        List<I18nDataModel> result = new ArrayList<I18nDataModel>(i18ns.size());
        if (map == null || map.size() == 0) {
            return result;
        }
        String name = "", code = "", nameValue = "", codeValue = "";
        for (I18nModel i18n : i18ns) {
            name = i18n.getName();
            code = i18n.getCode();
            if (StringUtil.isBlank(name)) {
                continue;
            }
            // 原始数据值
            nameValue = ClassHelper.convert(map.get(name), String.class);
            // 国际化编码
            if (StringUtil.isNotBlank(code)) {
                try {
                    codeValue = StringUtil.patternParser(code, map);
                } catch (Exception e) {
                    continue;
                }
            }
            result.add(new I18nDataModel(nameValue, codeValue));
        }
        return result;
    }

    /**
     * BEAN 数据国际化
     * 
     * @param bean
     * @param i18ns
     * @return
     */
    protected List<I18nDataModel> processBean(Object bean, List<I18nModel> i18ns) {
        List<I18nDataModel> result = new ArrayList<I18nDataModel>(i18ns.size());
        String name = "", code = "", nameValue = "", codeValue = "";
        for (I18nModel i18n : i18ns) {
            name = i18n.getName();
            code = i18n.getCode();
            if (StringUtil.isBlank(name)) {
                continue;
            }
            // 原始数据值
            nameValue = ClassHelper.getProperty(bean, name);
            // 国际化编码
            if (StringUtil.isNotBlank(code)) {
                try {
                    codeValue = StringUtil.patternParser(code, ClassHelper.toMap(bean));
                } catch (Exception e) {
                    continue;
                }
            }
            result.add(new I18nDataModel(nameValue, codeValue));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    protected Object processList(List<?> list, List<I18nModel> i18ns) {
        if (list == null || list.size() == 0) {
            return list;
        }
        List<I18nDataModel> result = new ArrayList<I18nDataModel>(list.size());
        for (Object obj : list) {
            if (ClassHelper.isBaseType(obj.getClass())) {
            } else if (ClassHelper.isInterface(obj.getClass(), Map.class)) {
                result.addAll(this.processMap((Map<String, Object>) obj, i18ns));
            } else {
                result.addAll(this.processBean(obj, i18ns));
            }
        }
        return result;
    }
}
