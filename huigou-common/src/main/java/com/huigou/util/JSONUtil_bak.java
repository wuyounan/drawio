package com.huigou.util;


/**
 * json 转换工具
 * 
 * @author xx
 */
public class JSONUtil_bak {

//    /**
//     * 对象转换为JSON字符串
//     * 
//     * @param obj
//     * @return
//     */
//    public static String toString(Object obj) {
//        JSONObject jo = new JSONObject(obj);
//        return jo.toString();
//    }
//
//    /**
//     * 列表转换为JSON字符串
//     * 
//     * @param collection
//     * @return
//     */
//    public static String toString(Collection<?> collection) {
//        JSONArray ja = new JSONArray(collection);
//        return ja.toString();
//    }
//
//    /**
//     * json字符串转换为对象
//     * 
//     * @param json
//     * @return
//     */
//    public static Object fromObject(String json) {
//        Object obj = JSONObject.fromObject(json);
//        return obj;
//    }
//
//    /**
//     * json数组
//     * 
//     * @param json
//     * @param cls
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    public static <T> List<T> toCollection(String json, Class<T> cls) {
//        JSONArray jsonArray = JSONArray.fromObject(json);
//        return (List<T>) JSONArray.toCollection(jsonArray, cls);
//    }
//
//    /**
//     * JSON字符串转换为map
//     * 
//     * @param json
//     * @return
//     */
//    public static Map<String, Object> toMap(String json) {
//        JSONObject jsonObject = JSONObject.fromObject(json);
//        Map<String, Object> map = new HashMap<String, Object>(jsonObject.size());
//        parseJSON(jsonObject, map);
//        return map;
//    }
//
//    /**
//     * JSON字符串转化为list
//     * 
//     * @param json
//     * @return
//     */
//    public static List<Object> toList(String json) {
//        JSONArray jsonArray = JSONArray.fromObject(json);
//        List<Object> list = new ArrayList<Object>(jsonArray.size());
//        parseJSON(jsonArray, list);
//        return list;
//    }
//
//    /**
//     * 解析JSON
//     * 
//     * @Title: parseJSON
//     * @author
//     * @Description: 首先得到key,判断key的type 有三种情况：
//     *               1 如aaa:"test"，则看出jsonObject.get(key).getClass()即"test"
//     *               的type为String,直接放到返回的map里
//     *               2 如里仍然是键值对，则递归调用populate,返回一个map到上一级的map里
//     *               3 如果是键对应数组，则调用populate Array这个方法
//     * @param @param jsonObject
//     * @param @param map
//     * @return void
//     * @throws
//     */
//    private static void parseJSON(JSONObject jsonObject, Map<String, Object> map) {
//        for (Iterator<?> iterator = jsonObject.entrySet().iterator(); iterator.hasNext();) {
//            String value = String.valueOf(iterator.next());
//            String key = value.substring(0, value.indexOf("="));
//            if (jsonObject.get(key).getClass().equals(JSONObject.class)) {
//                HashMap<String, Object> _map = new HashMap<String, Object>();
//                map.put(key, _map);
//                parseJSON(jsonObject.getJSONObject(key), _map);
//            } else if (jsonObject.get(key).getClass().equals(JSONArray.class)) {
//                JSONArray jsonArray = jsonObject.getJSONArray(key);
//                if (jsonArray != null && jsonArray.size() > 0) {
//                    List<Object> list = new ArrayList<Object>();
//                    map.put(key, list);
//                    parseJSON(jsonArray, list);
//                } else {
//                    map.put(key, null);
//                }
//            } else {
//                map.put(key, jsonObject.get(key));
//            }
//        }
//    }
//
//    /**
//     * 解析JSONArray
//     * 
//     * @param jsonArray
//     * @param list
//     */
//    private static void parseJSON(JSONArray jsonArray, List<Object> list) {
//        for (int i = 0; i < jsonArray.size(); i++) {
//            if (jsonArray.get(i).getClass().equals(JSONArray.class)) {
//                JSONArray _jsonArray = jsonArray.getJSONArray(i);
//                if (_jsonArray != null && _jsonArray.size() > 0) {
//                    List<Object> _list = new ArrayList<Object>();
//                    list.add(_list);
//                    parseJSON(_jsonArray, _list);
//                }
//            } else if (jsonArray.get(i).getClass().equals(JSONObject.class)) {
//                HashMap<String, Object> _map = new HashMap<String, Object>();
//                list.add(_map);
//                parseJSON(jsonArray.getJSONObject(i), _map);
//            } else {
//                list.add(jsonArray.get(i));
    // }
    // }
    // }
}
