package com.huigou.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.huigou.annotation.JsonIgnore;

/**
 * json 转换工具
 * 
 * @author xx
 */
@SuppressWarnings("serial")
public class JSONUtil {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
                return DateUtil.getDateParse(jp.getText());
            }
        });
        module.addSerializer(Date.class, new JsonSerializer<Date>() {
            public void serialize(Date date, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
                jg.writeString(DateUtil.processDate(date).toString());
            }
        });

        objectMapper.registerModule(module);

        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            public boolean hasIgnoreMarker(AnnotatedMember annotatedmember) {
                JsonIgnore jsonignore = (JsonIgnore) annotatedmember.getAnnotation(JsonIgnore.class);
                return jsonignore != null && jsonignore.value();
            }
        });
        // 注册nullSerializer
        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new BeanNullSerializerModifier()));
        // 日期格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 反序列化时忽略不存在的字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 如果是空对象的时候,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 允许出现特殊字符和转义符
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 允许出现单引号
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    /**
     * 对象转换为JSON字符串
     * 
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return "";
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 列表转换为JSON字符串
     * 
     * @param collection
     * @return
     */
    public static String toString(Collection<?> collection) {
        if (collection == null) {
            return "";
        }
        try {
            return objectMapper.writeValueAsString(collection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将json转成特定的cls的对象
     * 
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> T toBean(String jsonString, Class<T> cls) {
        if (StringUtil.isBlank(jsonString)) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, cls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转成map的
     * 
     * @param jsonString
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String jsonString) {
        if (StringUtil.isBlank(jsonString)) {
            return new HashMap<String, Object>();
        }
        try {
            Map<String, Object> map = objectMapper.readValue(jsonString, Map.class);
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换为带对象的MAP
     * 
     * @param jsonString
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> Map<String, T> toMap(String jsonString, Class<T> clazz) {
        if (StringUtil.isBlank(jsonString)) {
            return new HashMap<String, T>();
        }
        try {
            Map<String, Map<String, Object>> map = objectMapper.readValue(jsonString, new TypeReference<Map<String, T>>() {
            });
            Map<String, T> result = new HashMap<String, T>();
            for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
                result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("rawtypes")
    private static <T> T map2pojo(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    /**
     * 转换为对象
     * 
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> List<T> toListObject(String jsonString, Class<T> clazz) {
        try {
            List<Map<String, Object>> list = toListMap(jsonString);
            List<T> result = new ArrayList<T>();
            for (Map<String, Object> map : list) {
                result.add(map2pojo(map, clazz));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转成list中有map的
     * 
     * @param jsonString
     * @return
     */
    public static List<Map<String, Object>> toListMap(String jsonString) {
        if (StringUtil.isBlank(jsonString)) {
            return new ArrayList<Map<String, Object>>();
        }
        try {
            List<Map<String, Object>> list = objectMapper.readValue(jsonString, new TypeReference<List<Object>>() {
            });
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JSON字符串转化为list
     * 
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> List<T> toList(String jsonString, Class<T> clazz) {
        if (StringUtil.isBlank(jsonString)) {
            return new ArrayList<T>();
        }
        try {
            List<T> list = objectMapper.readValue(jsonString, new TypeReference<List<T>>() {
            });
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JSON字符串转化为list
     * 
     * @param jsonString
     * @return
     */
    public static List<Object> toList(String jsonString) {
        List<Object> list = new ArrayList<Object>();
        List<Map<String, Object>> datas = toListMap(jsonString);
        if (datas != null && datas.size() > 0) {
            for (Map<String, Object> m : datas) {
                list.add(m);
            }
        }
        return list;
    }

}
