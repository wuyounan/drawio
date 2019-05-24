package com.huigou.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

/**
 * 序列化对象中属性值为空时的处理
 * 
 * @author xiexin
 */
public class BeanNullSerializerModifier extends BeanSerializerModifier {

    private JsonSerializer<Object> _nullArrayJsonSerializer = new JsonSerializer<Object>() {
        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            if (value == null) {
                jgen.writeStartArray();
                jgen.writeEndArray();
            } else {
                jgen.writeObject(value);
            }
        }
    };

    private JsonSerializer<Object> _nullMapJsonSerializer = new JsonSerializer<Object>() {
        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            if (value == null) {
                jgen.writeStartObject();
                jgen.writeEndObject();
            } else {
                jgen.writeObject(value);
            }
        }
    };

    private JsonSerializer<Object> _nullObjectJsonSerializer = new JsonSerializer<Object>() {
        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            if (value == null) {
                jgen.writeString("");
            } else {
                jgen.writeObject(value);
            }
        }
    };

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        // 循环所有的beanPropertyWriter
        for (int i = 0; i < beanProperties.size(); i++) {
            BeanPropertyWriter writer = beanProperties.get(i);
            // 判断字段的类型，如果是array，list，set则注册nullSerializer
            if (isArrayType(writer)) {
                // 给writer注册一个自己的nullSerializer
                writer.assignNullSerializer(_nullArrayJsonSerializer);
            } else if (isMapType(writer)) {
                writer.assignNullSerializer(_nullMapJsonSerializer);
            } else if (isBaseType(writer)) {
                writer.assignNullSerializer(_nullObjectJsonSerializer);
            }
        }
        return beanProperties;
    }

    /**
     * array，list，set
     * 
     * @param writer
     * @return
     */
    protected boolean isArrayType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getPropertyType();
        return clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class);

    }

    /**
     * map
     * 
     * @param writer
     * @return
     */
    protected boolean isMapType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getPropertyType();
        return clazz.isAssignableFrom(Map.class);
    }

    /**
     * 基础数据类型
     * 
     * @param writer
     * @return
     */
    protected boolean isBaseType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getPropertyType();
        return ClassHelper.isBaseType(clazz);
    }

}