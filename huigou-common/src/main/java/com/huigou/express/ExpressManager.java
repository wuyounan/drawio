package com.huigou.express;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import com.huigou.util.ApplicationContextWrapper;
import com.huigou.util.LogHome;

/**
 * 表达式函数初始化 加入方法到表达式运行环境
 * 
 * @author xx
 */
public class ExpressManager {

    private static ExpressUtil expressUtil;

    private static boolean isInitialRunner = false;

    @SuppressWarnings("rawtypes")
    public static void initExpress(ExpressUtil expressUtil, LoadExpressClasses loadExpressClasses) throws InstantiationException, IllegalAccessException {
        if (isInitialRunner == true) {
            return;
        }
        synchronized (expressUtil) {
            if (isInitialRunner == true) {
                return;
            }
            ExpressManager.expressUtil = expressUtil;
            List<Object> beanNames = expressUtil.getBeanNames();
            for (Object obj : beanNames) {
                Class<?> c = obj.getClass();
                for (Method m : c.getDeclaredMethods()) {
                    if (!Modifier.isPublic(m.getModifiers())) {// 只添加public的方法
                        continue;
                    }
                    if (m.getName().startsWith("set")) {// set方法不加入
                        continue;
                    }
                    try {
                        ExpressManager.expressUtil.addFunction(m.getName(), obj, m.getName(), m.getParameterTypes());
                    } catch (Exception e) {
                        LogHome.getLog(ExpressManager.class).error(c.getName() + ":" + m.getName(), e);
                    }
                }
            }
            // 扫描包
            Set<Class<?>> classes = loadExpressClasses.scanClassSet();
            for (Class c : classes) {
                AbstractFunction function = null;
                Service service = AnnotationUtils.findAnnotation(c, Service.class);
                if (service != null) {
                    function = (AbstractFunction) ApplicationContextWrapper.getBean(service.value());
                } else {
                    function = (AbstractFunction) c.newInstance();
                }
                for (Method m : c.getDeclaredMethods()) {
                    if (!Modifier.isPublic(m.getModifiers())) {// 只添加public的方法
                        continue;
                    }
                    try {
                        ExpressManager.expressUtil.addFunction(m.getName(), function, m.getName(), m.getParameterTypes());
                    } catch (Exception e) {
                        LogHome.getLog(ExpressManager.class).error(c.getName() + ":" + m.getName(), e);
                    }
                }
            }
        }
        isInitialRunner = true;
    }

    public static Object evaluate(String expression, Map<String, Object> variables) throws Exception {
        addVariables(variables);
        return expressUtil.execute(expression, variables);
    }

    public static Object evaluate(String expression) throws Exception {
        return expressUtil.execute(expression);
    }

    public static void addVariable(String key, Object value) {
        VariableContainer.addVariable(key, value);
    }

    public static void addVariables(Map<String, Object> variables) {
        for (String key : variables.keySet()) {
            addVariable(key, variables.get(key));
        }
    }
}
