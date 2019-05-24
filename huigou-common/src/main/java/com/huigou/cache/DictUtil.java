package com.huigou.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.huigou.context.ThreadLocalUtil;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.ListUtil;
import com.huigou.util.StringUtil;

/**
 * 获取数据库字典key value
 * 
 * @ClassName: DictUtil
 * @author xx
 * @version V1.0
 */
public class DictUtil {

    /**
     * 获取数据字典map
     * 
     * @param code
     * @param type
     *            过滤类型
     * @return
     */
    public static Map<String, String> getDictionary(String code, String type) {
        String[] s = code.split("[.]");
        String[] filters = StringUtil.isBlank(type) ? null : type.split(",");
        Map<String, DictionaryDesc> dictionary = SystemCache.getDictionary(s[s.length - 1], filters);
        if (null != dictionary && dictionary.size() > 0) {
            Map<String, String> map = new LinkedHashMap<String, String>(dictionary.size());
            for (Map.Entry<String, DictionaryDesc> entry : dictionary.entrySet()) {
                map.put(entry.getValue().getValue(), entry.getValue().getName());
            }
            return map;
        } else {
            return new HashMap<String, String>();
        }
    }

    /**
     * 获取数据字典map
     * 
     * @param code
     * @return
     */
    public static Map<String, String> getDictionary(String code) {
        return getDictionary(code, null);
    }

    /**
     * 获取数据字典列表
     * 
     * @param code
     * @return
     */
    public static List<Map<String, Object>> getDictionaryList(String code) {
        String[] s = code.split("[.]");
        Map<String, DictionaryDesc> dictionary = SystemCache.getDictionary(s[s.length - 1]);
        if (null != dictionary && dictionary.size() > 0) {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(dictionary.size());
            for (Map.Entry<String, DictionaryDesc> entry : dictionary.entrySet()) {
                list.add(ClassHelper.toMap(entry.getValue()));
            }
            return list;
        } else {
            return new ArrayList<Map<String, Object>>();
        }
    }

    /**
     * 获取数据组字典显示值
     * 
     * @author
     * @param code
     * @param value
     * @return
     * @throws
     */
    @SuppressWarnings("unchecked")
    public static String getDictionaryDetailText(String code, Object value) {
        if (value == null) return null;
        String v = ClassHelper.convert(value, String.class);
        if (StringUtil.isBlank(v)) return null;
        Map<String, Object> map = ThreadLocalUtil.getVariable(Constants.DICTIONARY_MAP, Map.class);
        if (map != null) {
            Map<String, String> model = (Map<String, String>) map.get(code);
            if (model != null) {
                return getText(model, v);
            }
        } else {
            map = new HashMap<>();
        }
        String textView = null;
        Map<String, String> dictionary = getDictionary(code);
        if (dictionary != null && dictionary.size() > 0) {
            textView = getText(dictionary, v);
        }
        map.put(code, dictionary);
        // 提高效率加入到线程局部变量中
        ThreadLocalUtil.putVariable(Constants.DICTIONARY_MAP, map);
        return textView;
    }

    /**
     * 根据字典值获取显示值
     * 
     * @param map
     * @param value
     * @return
     */
    private static String getText(Map<String, String> map, String value) {
        if (map == null || map.size() == 0) {
            return null;
        }
        if (StringUtil.isBlank(value)) {
            return null;
        }
        String t = map.get(value);
        if (StringUtil.isNotBlank(t)) {
            return t;
        }
        // 处理逗号分隔字符串
        String[] vs = value.split(",");
        List<String> vl = new ArrayList<>(vs.length);
        for (String v : vs) {
            t = map.get(v);
            if (StringUtil.isNotBlank(t)) {
                vl.add(t);
            }
        }
        if (vl.size() > 0) {
            return ListUtil.join(vl, ",");
        }
        return null;
    }

    /**
     * 根据显示只获取字典值
     * 
     * @param code
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String getDictionaryDetailValue(String code, Object obj) {
        if (obj == null) return null;
        String text = ClassHelper.convert(obj, String.class);
        if (StringUtil.isBlank(text)) return null;
        Map<String, Object> map = ThreadLocalUtil.getVariable(Constants.DICTIONARY_MAP, Map.class);
        if (map != null) {
            Map<String, String> model = (Map<String, String>) map.get(code);
            if (model != null) {
                return getValue(model, text);
            }
        } else {
            map = new HashMap<>();
        }
        String value = null;
        Map<String, String> dictionary = getDictionary(code);
        if (dictionary != null && dictionary.size() > 0) {
            value = getValue(dictionary, text);
        }
        map.put(code, dictionary);
        // 提高效率加入到线程局部变量中
        ThreadLocalUtil.putVariable(Constants.DICTIONARY_MAP, map);
        return value;
    }

    /**
     * 根据现实值获取字典值
     * 
     * @param map
     * @param value
     * @return
     */
    private static String getValue(Map<String, String> map, String text) {
        if (map == null || map.size() == 0) {
            return null;
        }
        if (StringUtil.isBlank(text)) {
            return null;
        }
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value.equals(text.trim())) {
                return key;
            }
        }
        // 处理逗号分隔字符串
        String[] ts = text.replaceAll("，", ",").split(",");
        List<String> tl = new ArrayList<>(ts.length);
        for (String t : ts) {
            mapLoop: for (String key : map.keySet()) {
                if (map.get(key).equals(t)) {
                    tl.add(key);
                    break mapLoop;
                }
            }
        }
        if (tl.size() > 0) {
            return ListUtil.join(tl, ",");
        }
        return null;
    }

}
