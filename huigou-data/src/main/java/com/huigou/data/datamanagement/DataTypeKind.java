package com.huigou.data.datamanagement;

import java.util.LinkedHashMap;
import java.util.Map;

import com.huigou.util.StringUtil;

/**
 * 数据字段类型
 * 
 * @author xx
 */
public enum DataTypeKind {
    STRING("java.lang.String", "String"),
    INTEGER("java.lang.Integer", "Integer"),
    LONG("java.lang.Long", "Long"),
    DATE("java.util.Date", "Date"),
    BIGDECIMAL("java.math.BigDecimal", "BigDecimal");

    private final String id;

    private final String displayName;

    private DataTypeKind(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static DataTypeKind findById(String id) {
        if (StringUtil.isBlank(id)) {
            return null;
        }
        for (DataTypeKind item : DataTypeKind.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new LinkedHashMap<String, String>(6);
        for (DataTypeKind item : DataTypeKind.values()) {
            result.put(item.getId(), item.getDisplayName());
        }
        return result;
    }

}
