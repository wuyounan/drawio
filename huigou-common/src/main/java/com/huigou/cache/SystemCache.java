package com.huigou.cache;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.huigou.cache.service.ICache;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;

/**
 * 单态模式保存系统常量使用ehcache缓存数据
 * 
 * @ClassName: Singleton
 * @author xx
 * @version V1.0
 */
public class SystemCache {

    private ICache icache;

    private String startTime;// 服务启动时间

    static class SingletonHolder {
        static SystemCache instance = new SystemCache();
    }

    public static SystemCache getInstance() {
        return SingletonHolder.instance;
    }

    private final static String APPLICATION_SYSTEM = "application_system";

    private final static String CONTEXT_PATH = "sys_context_path";

    private final static String REAL_PATH = "sys_real_path";

    private final static String PARAMETER = "sys_parameter";

    private final static String DICTIONARY = "sys_dictionary";

    private final static String THREE_MEMBER_PERMISSION = "three_member_permission";

    public final static String UPLOAD_FILE_TYPE = "uploadFileType";

    public final static String UPLOAD_PATH = "uploadPath";

    public final static String SYSTEM_TMPDIR = "systemTmpdir";

    public final static String OTHER_KEY = "otherKey";

    public final static String I18N_PROPERTIES = "i18n_properties";

    public static String getStartTime() {
        return getInstance().startTime;
    }

    public static void setStartTime(String startTime) {
        getInstance().startTime = startTime;
    }

    public static void setCache(ICache icache) {
        getInstance().icache = icache;
    }

    private void put(String cacheKey, Serializable obj) {
        icache.put(cacheKey, obj);
    }

    private Object get(String cacheKey) {
        if (icache == null) return null;
        return icache.get(cacheKey);
    }

    private <T> T get(String cacheKey, Class<T> cls) {
        if (icache == null) return null;
        return icache.get(cacheKey, cls);
    }

    private void remove(String key) {
        icache.remove(key, ".");
    }

    public static String getContextPath() {
        String contextPath = getInstance().get(CONTEXT_PATH, String.class);
        return StringUtil.isBlank(contextPath) ? "" : contextPath;
    }

    public static void setContextPath(String contextPath) {
        getInstance().put(CONTEXT_PATH, contextPath);
    }

    public static String getRealPath() {
        return getInstance().get(REAL_PATH, String.class);
    }

    public static void setRealPath(String realPath) {
        getInstance().put(REAL_PATH, realPath);
    }

    public static <T> T getParameter(String code, Class<T> cls) {
        String key = new StringBuffer(PARAMETER).append(".").append(code.toUpperCase()).toString();
        return getInstance().get(key, cls);
    }

    public static void setParameter(String code, Serializable Object) {
        String key = new StringBuffer(PARAMETER).append(".").append(code.toUpperCase()).toString();
        getInstance().put(key, Object);
    }

    public static void removeParameter() {
        getInstance().remove(PARAMETER);
    }

    public static Map<String, DictionaryDesc> getDictionary(String code, String... types) {
        String key = new StringBuffer(DICTIONARY).append(".").append(code.toUpperCase()).toString();
        @SuppressWarnings("unchecked")
        Map<String, DictionaryDesc> dictionary = getInstance().get(key, Map.class);
        if (null != dictionary && dictionary.size() > 0) {
            if (types != null) {
                Arrays.sort(types);
            }

            Map<String, DictionaryDesc> map = new LinkedHashMap<String, DictionaryDesc>(dictionary.size());
            for (Map.Entry<String, DictionaryDesc> entry : dictionary.entrySet()) {
                DictionaryDesc dm = entry.getValue();
                if (StringUtil.isBlank(dm.getTypeId()) || types == null || types.length == 0 || Arrays.binarySearch(types, dm.getTypeId()) > -1) {
                    map.put(dm.getValue(), dm);
                }
            }
            return map;
        }
        return null;
    }

    public static void setDictionary(String code, Serializable Object) {
        String key = new StringBuffer(DICTIONARY).append(".").append(code.toUpperCase()).toString();
        getInstance().put(key, Object);
    }

    public static void removeDictionary() {
        getInstance().remove(DICTIONARY);
    }

    public static String getThreeMemberPermission(String key) {
        String cachKey = new StringBuffer(THREE_MEMBER_PERMISSION).append(".").append(key.toUpperCase()).toString();
        return getInstance().get(cachKey, String.class);
    }

    public static void setThreeMemberPermission(String key, String roleKindIds) {
        String cachKey = new StringBuffer(THREE_MEMBER_PERMISSION).append(".").append(key.toUpperCase()).toString();
        getInstance().put(cachKey, roleKindIds);
    }

    public static void removeThreeMemberPermission() {
        getInstance().remove(THREE_MEMBER_PERMISSION);
    }

    public static void putOther(String key, Serializable obj) {
        String cachKey = new StringBuffer(OTHER_KEY).append(".").append(key.toUpperCase()).toString();
        getInstance().put(cachKey, obj);
    }

    public static Object getOther(String key) {
        String cachKey = new StringBuffer(OTHER_KEY).append(".").append(key.toUpperCase()).toString();
        return getInstance().get(cachKey);
    }

    public static void removeOther(String key) {
        String cachKey = new StringBuffer(OTHER_KEY).append(".").append(key.toUpperCase()).toString();
        getInstance().remove(cachKey);
    }

    public static String getDictionaryDetailText(String code, Object value) {
        if (value == null) return null;
        String v = ClassHelper.convert(value, String.class);
        if (StringUtil.isBlank(v)) return null;
        Map<String, DictionaryDesc> map = getDictionary(code);
        if (map != null) {
            DictionaryDesc model = map.get(v);
            if (model != null) {
                return model.getName();
            }
            return "";
        }
        return null;
    }

    public static void setApplicationSystem(Serializable Object) {
        getInstance().put(APPLICATION_SYSTEM, Object);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, ApplicationSystemDesc> getApplicationSystem() {
        return getInstance().get(APPLICATION_SYSTEM, Map.class);
    }

    /**
     * 国际化信息加入缓存中
     * 
     * @param code
     * @param Object
     */
    public static void setI18nProperties(String code, Serializable Object) {
        String key = new StringBuffer(I18N_PROPERTIES).append(".").append(code.toUpperCase()).toString();
        getInstance().put(key, Object);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getI18nProperties(String code) {
        String key = new StringBuffer(I18N_PROPERTIES).append(".").append(code.toUpperCase()).toString();
        return getInstance().get(key, Map.class);
    }

    public static void removeI18nProperties() {
        getInstance().remove(I18N_PROPERTIES);
    }
}