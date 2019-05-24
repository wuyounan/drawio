package com.huigou.data.datamanagement;

import java.util.LinkedHashMap;
import java.util.Map;

import com.huigou.util.StringUtil;

/**
 * 数据资源类别
 * 
 * @author xx
 */
public enum DataResourceKind {
    ORG("org", "组织机构"), DICTIONARY("dictionary", "数据字典"), ENUMKIND("enum", "枚举"), DEFINE("define", "自定义选择"), INPUT("input", "手工输入");

    private final String id;

    private final String displayName;

    private DataResourceKind(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static DataResourceKind findById(String id) {
        if (StringUtil.isBlank(id)) {
            return null;
        }
        for (DataResourceKind item : DataResourceKind.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new LinkedHashMap<String, String>(6);
        for (DataResourceKind item : DataResourceKind.values()) {
            result.put(item.getId(), item.getDisplayName());
        }
        return result;
    }

}
