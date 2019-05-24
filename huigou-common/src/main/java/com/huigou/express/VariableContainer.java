package com.huigou.express;

import java.util.HashMap;
import java.util.Map;

import com.huigou.util.StringUtil;

/**
 * 表达式上下文变量容器 使用本地线程对象，传递上下文变量映射表
 * 
 * @author gongmm
 */
public class VariableContainer {

    private static ThreadLocal<Map<String, Object>> variableMapThreadLocal = new ThreadLocal<Map<String, Object>>();

    public static Map<String, Object> getVariableMap() {
        Map<String, Object> variableMap = variableMapThreadLocal.get();
        if (variableMap == null) {
            variableMap = new HashMap<String, Object>();
            variableMapThreadLocal.set(variableMap);
        }
        return variableMap;
    }

    public static void setVariableMap(Map<String, Object> variableMap) {
        removeVariableMap();
        if (variableMap != null) {
            variableMapThreadLocal.set(variableMap);
        }
    }

    public static void putVariableMap(Map<String, Object> map) {
        if (map != null) {
            getVariableMap().putAll(map);
        }
    }

    public static void removeVariableMap() {
        Map<String, Object> variableMap = variableMapThreadLocal.get();
        if (variableMap != null) {
            variableMap.clear();
        }
        variableMapThreadLocal.set(null);
        variableMapThreadLocal.remove();
    }

    public static void addVariable(String key, Object object) {
        if (object != null) {
            getVariableMap().put(key, object);
        }
    }

    public static Object getVariable(String key) {
        if (!StringUtil.isBlank(key)) {
            return getVariableMap().get(key);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getVariable(String key, Class<T> cls) {
        if (!StringUtil.isBlank(key)) {
            return (T) getVariableMap().get(key);
        } else {
            return null;
        }
    }

    public static Object removeVariable(String key) {
        return getVariableMap().remove(key);
    }

}
