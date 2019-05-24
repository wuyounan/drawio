package com.huigou.data.i18n.aspect;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;

import com.huigou.data.i18n.model.I18nModel;
import com.huigou.util.ClassHelper;

/**
 * 国际化处理后台返回数据切面
 * xml aop配置使用
 * 
 * @author xx
 */

public class I18nModelAspect extends I18nAspect {
    private List<I18nModel> i18ns;

    private String keyName;

    public List<I18nModel> getI18ns() {
        return i18ns;
    }

    public void setI18ns(List<I18nModel> i18ns) {
        this.i18ns = i18ns;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

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
        return this.process(result, keyName, i18ns);
    }

}
