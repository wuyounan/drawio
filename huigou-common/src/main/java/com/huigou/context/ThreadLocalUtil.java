package com.huigou.context;

import java.util.HashMap;
import java.util.Map;

import com.huigou.util.StringUtil;

/**
 * 使用本地线程对象，上下文变量映射表
 * 
 * @author gongmm
 */
public class ThreadLocalUtil {

    public static String OPERATOR_KEY = "operator";

    //private static String APPROVAL_PARAMETER_KEY = "_approvalParameter_";

    //public final static String EXCEPTION_KEY = "_exception_";

    public final static String BIZ_CLASS_NAME_KEY = "_bizClassName_";

    public final static String PROC_APPROVAL_OPERATOR_KEY = "_procApprovalOperator_";
    
    private static ThreadLocal<Map<String, Object>> mapThreadLocal = new ThreadLocal<Map<String, Object>>();


    public static Map<String, Object> getVariableMap() {
        Map<String, Object> variableMap = mapThreadLocal.get();
        if (variableMap == null) {
            variableMap = new HashMap<String, Object>();
            mapThreadLocal.set(variableMap);
        }
        return variableMap;
    }

    public static void setVariableMap(Map<String, Object> variableMap) {
        removeVariableMap();
        if (variableMap != null) {
            mapThreadLocal.set(variableMap);
        }
    }

    public static void removeVariableMap() {
        Map<String, Object> variableMap = mapThreadLocal.get();
        if (variableMap != null) {
            variableMap.clear();
        }
        mapThreadLocal.set(null);
        mapThreadLocal.remove();
    }

    public static void putVariable(String key, Object object) {
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

    public static Operator getOperator() {
        return getVariable(OPERATOR_KEY, Operator.class);
    }
    
    public static String getPersonMemberId() {
        return getVariable(OPERATOR_KEY, Operator.class).getPersonMemberId();
    }
    
    public static String getOperatorTenantId() {
        return getVariable(OPERATOR_KEY, Operator.class).getTenantId();
    }

    public static void putOperator(Operator operator) {
        if (operator != null) {
            getVariableMap().put(OPERATOR_KEY, operator);
        }
    }

}
