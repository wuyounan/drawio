package com.huigou.uasp.bmp.common.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.huigou.uasp.annotation.ViewField;

public class ViewUtils {
    static Map<String, Map<String, ViewField>> fieldBuffer = new HashMap<String, Map<String, ViewField>>();

    static Map<String, List<String>> fieldNameBuffer = new HashMap<String, List<String>>();

    static public List<String> viewProperties(Class<?> type, String view) {
        if (!type.isAnnotationPresent(Entity.class)) throw new RuntimeException(type + " is not a persistence type!");
        String key = view + "@" + type.getName();
        List<String> rst = fieldNameBuffer.get(key);
        if (rst != null) return rst;
        rst = new ArrayList<String>();
        Map<String, ViewField> viewfs = new HashMap<String, ViewField>();
        search(type, null, rst, viewfs, view);
        buffer(key, rst, viewfs);
        return rst;
    }

    static public List<String> viewIntersection(Class<?> type, Collection<String> views) {
        return null;
        /*
         * if (views.isEmpty()) return new ArrayList<String>();
         * Collection<String> rst = null;
         * for (String v : views) {
         * List<String> view = viewProperties(type, v);
         * if (rst == null) rst = view;
         * else
         * rst = CollectionUtils.intersection(rst, view);
         * }
         * return new ArrayList<String>(rst);
         */
    }

    static void buffer(String key, List<String> fields, Map<String, ViewField> viewfs) {
        fieldBuffer.put(key, viewfs);
        fieldNameBuffer.put(key, fields);
    }

    static void search(Class<?> type, String prefix, List<String> fields, Map<String, ViewField> viewfs, String view) {
        Field[] fs = type.getDeclaredFields();
        for (Field f : fs) {
            String name = concact(prefix, f);
            if (f.isAnnotationPresent(Id.class)) {
                fields.add(name);
                continue;
            }
            ViewField vf = get(f, view);
            if (vf != null) {
                if (vf.all()) {
                    fields.add(name);
                    viewfs.put(name, vf);
                } else
                    search(f.getType(), name, fields, viewfs, view);
            }
        }
        Class<?> p = (Class<?>) type.getGenericSuperclass();
        if (!type.getGenericSuperclass().equals(Object.class)) search(p, prefix, fields, viewfs, view);
    }

    static ViewField get(Field f, String view) {
        if (!f.isAnnotationPresent(ViewProperty.class)) return null;
        ViewProperty dp = f.getAnnotation(ViewProperty.class);
        ViewField[] vs = dp.viewFields();
        for (ViewField vf : vs) {
            if (vf.view().equals(view)) return vf;
        }
        return null;
    }

    static String concact(String prefix, Field f) {
        if (prefix == null) return f.getName();
        return prefix + "." + f.getName();
    }
}
