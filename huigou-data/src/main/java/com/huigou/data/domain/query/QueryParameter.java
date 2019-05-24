package com.huigou.data.domain.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huigou.util.ClassHelper;
import com.huigou.util.JSONUtil;
import com.huigou.util.StringUtil;

/**
 * 查询参数
 * 
 * @author gongmm
 */
public class QueryParameter {

    private Map<String, Object> params;

    public QueryParameter(Map<String, Object> params) {
        this.params = params;
    }

    public QueryParameter() {
        this.params = new HashMap<String, Object>();
    }

    public void addParameters(Object... parameterValues) {

        if (parameterValues.length % 2 != 0) {
            throw new RuntimeException("无效的产生个数。 ");
        }

        for (int i = 0; i < parameterValues.length; ++i) {
            String parameterName = null;
            if (parameterValues[i] instanceof String) {
                parameterName = (String) parameterValues[i];
            } else {
                throw new RuntimeException("Expected a String as the parameter name, not a " + parameterValues[i].getClass().getSimpleName());
            }
            ++i;
            params.put(parameterName, parameterValues[i]);
        }
    }

    public static Map<String, Object> buildParameters(Object... parameterValues) {

        Map<String, Object> result = new HashMap<String, Object>();

        if (parameterValues.length % 2 != 0) {
            throw new RuntimeException("无效的产生个数。 ");
        }

        for (int i = 0; i < parameterValues.length; ++i) {
            String parameterName = null;
            if (parameterValues[i] instanceof String) {
                parameterName = (String) parameterValues[i];
            } else {
                throw new RuntimeException("Expected a String as the parameter name, not a " + parameterValues[i].getClass().getSimpleName());
            }
            ++i;
            result.put(parameterName, parameterValues[i]);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key, Class<T> type) {
        if (params.get(key) == null) {
            return null;
        }
        if (params.get(key).getClass() == type) {
            return (T) params.get(key);
        }
        if (ClassHelper.isInterface(params.get(key).getClass(), type)) {
            return (T) params.get(key);
        }
        if (!ClassHelper.isBaseType(type) && ClassHelper.isSubClass(params.get(key).getClass(), type)) {
            return (T) params.get(key);
        }
        String value = params.get(key).toString();
        if (type == String.class) {
            return (T) value;
        }
        if (value.equals("")) {
            return null;
        }
        return (T) ClassHelper.convert(value, type);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key, Class<T> type, Object defaultValue) {
        T result = this.getProperty(key, type);
        if (result == null) {
            return (T) defaultValue;
        }
        if (type == String.class && result.equals("")) {
            return (T) defaultValue;
        }
        return result;
    }

    public String getString(String key) {
        return this.getProperty(key, String.class, "");
    }

    public List<String> getStringList(String key) {
        String jsonStr = this.getProperty(key, String.class);
        if (StringUtil.isBlank(jsonStr)) {
            return null;
        }
        return JSONUtil.toList(jsonStr, String.class);
    }

    public Integer getInteger(String key) {
        return this.getProperty(key, Integer.class, 0);
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

}
