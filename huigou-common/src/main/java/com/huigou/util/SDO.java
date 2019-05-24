package com.huigou.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.huigou.context.Operator;
import com.huigou.domain.IdentifiedEntity;
import com.huigou.domain.QueryRequest;
import com.huigou.exception.ApplicationException;

/**
 * 服务数据对象
 * 
 * @author Gelard
 */
public class SDO implements Serializable {

    private final static List<String> NOT_STRIPXSS_KEY_WHITELIST = new ArrayList<String>();
    static {
        // 导出时表头
        NOT_STRIPXSS_KEY_WHITELIST.add(Constants.EXPORT_HEAD);
        // 审批手写输入数据
        NOT_STRIPXSS_KEY_WHITELIST.add("opinion30");
        NOT_STRIPXSS_KEY_WHITELIST.add("opinion64");
    }

    private static final long serialVersionUID = 4533581924804423336L;

    private Map<String, Object> properties = new HashMap<String, Object>(4);

    private Operator operator;

    private boolean needDecode = false;

    public SDO() {

    }

    public SDO(boolean needDecode) {
        this.needDecode = needDecode;
    }

    public SDO(String data) {
        this.parseJSONString(data);
    }

    public SDO(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> propertyMap) {
        this.properties = propertyMap;
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public void putProperty(String key, Object value) {
        if (value != null && value instanceof String && needDecode) {
            value = StringUtil.decode((String) value);
            // 使用白名单控制不需要过滤的参数
            if (!NOT_STRIPXSS_KEY_WHITELIST.contains(key)) {
                // 为解决xss攻击 这里增加过滤方法
                value = StringUtil.stripXSS((String) value);
            }
        }
        properties.put(key, value);
    }

    public void removeProperty(String key) {
        properties.remove(key);
    }

    public void clearProperty() {
        properties.clear();
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key, Class<T> type) {
        if (properties.get(key) == null) {
            return null;
        }
        if (properties.get(key).getClass() == type) {
            return (T) properties.get(key);
        }
        if (ClassHelper.isInterface(properties.get(key).getClass(), type)) {
            return (T) properties.get(key);
        }
        if (!ClassHelper.isBaseType(type) && ClassHelper.isSubClass(properties.get(key).getClass(), type)) {
            return (T) properties.get(key);
        }
        String value = properties.get(key).toString();
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

    public String[] getStringArray(String key) {
        List<String> list = this.getStringList(key);
        if (list == null) {
            return null;
        }
        return list.toArray(new String[list.size()]);
    }

    public List<String> getStringList(String key) {
        String jsonStr = this.getProperty(key, String.class);
        if (StringUtil.isBlank(jsonStr)) {
            return null;
        }
        return JSONUtil.toList(jsonStr, String.class);
    }

    public List<Integer> getIntegerList(String key) {
        String jsonStr = this.getProperty(key, String.class);
        if (StringUtil.isBlank(jsonStr)) {
            return null;
        }
        return JSONUtil.toList(jsonStr, Integer.class);
    }

    public Map<String, Integer> getStringMap(String key) {
        Map<String, Object> m = getObjectMap(key);
        Map<String, Integer> result = new HashMap<String, Integer>(m.size());
        for (String item : m.keySet()) {
            result.put(ClassHelper.convert(item, String.class), ClassHelper.convert(m.get(item), Integer.class));
        }
        return result;
    }

    public Integer getInteger(String key) {
        return this.getProperty(key, Integer.class);
    }

    public Long getLong(String key) {
        return this.getProperty(key, Long.class);
    }

    public Long[] getLongArray(String key) {
        List<Long> list = this.getList(key, Long.class);
        if (list == null) {
            return null;
        }
        return list.toArray(new Long[list.size()]);
    }

    public Map<Long, Long> getLongMap(String key) {
        Map<String, Object> m = getObjectMap(key);
        Map<Long, Long> result = new HashMap<Long, Long>(m.size());
        for (String item : m.keySet()) {
            result.put(ClassHelper.convert(item, Long.class), ClassHelper.convert(m.get(item), Long.class));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Object> getList(String key) {
        Object obj = this.getProperty(key);
        if (obj == null) return null;
        if (ClassHelper.isInterface(obj.getClass(), List.class)) {
            return (List<Object>) obj;
        }
        if (obj instanceof String) {
            String jsonStr = ClassHelper.convert(obj, String.class);
            if (StringUtil.isBlank(jsonStr)) return null;
            return JSONUtil.toList(jsonStr);
        }
        return null;
    }

    @SuppressWarnings({ "unchecked" })
    public <T> List<T> getList(String key, Class<T> clazz) {
        List<T> beanList = new ArrayList<T>();
        List<Object> list = getList(key);
        if (list == null || list.size() == 0) {
            return beanList;
        }
        boolean isAbstractEntity = ClassHelper.isInterface(clazz, IdentifiedEntity.class);
        Map<String, Object> m = null;
        Object obj = null;
        for (Object o : list) {
            m = (Map<String, Object>) o;
            obj = ClassHelper.fromMap(clazz, m);
            if (isAbstractEntity) {
                IdentifiedEntity abstractEntity = (IdentifiedEntity) obj;
                abstractEntity.setUpdateFields_(m.keySet());
            }
            beanList.add((T) obj);
        }
        return beanList;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getObjectMap(String key) {
        Object obj = this.getProperty(key);
        if (obj == null) return null;
        if (ClassHelper.isInterface(obj.getClass(), Map.class)) {
            return (Map<String, Object>) obj;
        }
        if (obj instanceof String) {
            String jsonStr = ClassHelper.convert(obj, String.class);
            if (StringUtil.isBlank(jsonStr)) return null;
            return JSONUtil.toMap(jsonStr);
        }
        return null;
    }

    public void parseJSONString(String jsonStr) {
        if (StringUtil.isBlank(jsonStr)) {
            return;
        }
        Map<String, Object> map = JSONUtil.toMap(jsonStr);
        this.setProperties(map);
    }

    public String toString() {
        return JSONUtil.toString(properties);
    }

    /**
     * SDO中数据转换为对象
     * 
     * @author
     * @param cls
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T toObject(Class<T> cls) {
        Object obj = null;
        try {
            obj = cls.newInstance();
            Set<String> set = this.getProperties().keySet();
            for (String key : set) {
                ClassHelper.setProperty(obj, key, this.getProperty(key));
            }
            if (ClassHelper.isInterface(cls, IdentifiedEntity.class)) {
                IdentifiedEntity abstractEntity = (IdentifiedEntity) obj;
                abstractEntity.setUpdateFields_(set);
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return (T) obj;
    }

    /**
     * 获取查询请求对象
     * 
     * @param cls
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T toQueryRequest(Class<T> cls) {
        if (!ClassHelper.isInterface(cls, QueryRequest.class)) {
            throw new ApplicationException("错误的class类型。");
        }
        QueryRequest obj = null;

        try {
            obj = (QueryRequest) cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ApplicationException(e);
        }

        Set<String> set = properties.keySet();
        Object value = null;
        for (String key : set) {
            value = properties.get(key);
            if (value != null && !value.toString().equals("")) {
                ClassHelper.setProperty(obj, key, value);
            }
        }
        obj.initPageModel(this.getProperties());
        obj.setOperator(operator);
        return (T) obj;
    }

    /**
     * 改变键名称
     * 
     * @author
     * @param oldName
     * @param newName
     *            void
     */
    public void changKeyName(String oldName, String newName) {
        Object value = this.getProperty(oldName);
        this.removeProperty(oldName);
        this.putProperty(newName, value);
    }

    public String getParentId() {
        return this.getString(Constants.PARENTID);
    }

    public String getId() {
        return this.getString(Constants.ID);
    }

    public String getBizId() {
        return this.getString(Constants.BIZID);
    }

    public List<String> getIds() {
        return this.getStringList("ids");
    }
}
