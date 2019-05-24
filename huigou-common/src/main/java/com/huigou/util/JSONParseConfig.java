package com.huigou.util;

/**
 * JSON 对象中数据转换处理器
 * 
 * @ClassName: JsonParseConfig
 * @Description: TODO
 * @author
 * @date 2014-1-4 上午12:05:40
 * @version V1.0
 */
public class JSONParseConfig {
    /*
     * public static final JsonConfig jc = new JsonConfig();
     * private static final JsonDefaultDateValueProcessor defaultValueProcessor = new JsonDefaultDateValueProcessor();
     * static {
     * jc.registerJsonValueProcessor(java.sql.Date.class, defaultValueProcessor);
     * jc.registerJsonValueProcessor(java.util.Date.class, defaultValueProcessor);
     * jc.registerJsonValueProcessor(java.sql.Timestamp.class, defaultValueProcessor);
     * // 设置Number类型为空的默认值 json-lib默认是0
     * jc.registerDefaultValueProcessor(Number.class, new DefaultValueProcessor() {
     * @SuppressWarnings("rawtypes")
     * public Object getDefaultValue(Class type) {
     * return null;
     * }
     * });
     * jc.addIgnoreFieldAnnotation(JsonIgnore.class);
     * jc.setPropertySetStrategy(new PropertyStrategyWrapper(PropertySetStrategy.DEFAULT));
     * JSONUtils.getMorpherRegistry().registerMorpher(new DateProcessNullMorpher(new String[] {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd"}));
     * }
     * static class JsonDefaultDateValueProcessor implements JsonValueProcessor {
     * public Object processArrayValue(Object value, JsonConfig config) {
     * return DateUtil.processDate(value);
     * }
     * public Object processObjectValue(String key, Object value, JsonConfig config) {
     * return DateUtil.processDate(value);
     * }
     * }
     */

}
