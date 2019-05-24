package com.huigou.data.datamanagement;

import java.util.LinkedHashMap;
import java.util.Map;

import com.huigou.util.StringUtil;

/**
 * 数据操作类型
 * 
 * @author xx
 */
public enum DataFieldSymbolKind {
    EQ("=", "等于"), LIKE("like", "LIKE"), HALF_LIKE("half_like", "HALF_LIKE"), IN("in", "IN"), GT_EQ(">=", "大于等于"), LT_EQ("<=", "小于等于"), GT(">", "大于"), LT("<",
                                                                                                                                                          "小于");

    private final String id;

    private final String displayName;

    private DataFieldSymbolKind(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static DataFieldSymbolKind findById(String id) {
        if (StringUtil.isBlank(id)) {
            return null;
        }
        for (DataFieldSymbolKind item : DataFieldSymbolKind.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new LinkedHashMap<String, String>(6);
        for (DataFieldSymbolKind item : DataFieldSymbolKind.values()) {
            result.put(item.getId(), item.getDisplayName());
        }
        return result;
    }

}
