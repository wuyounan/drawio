package com.huigou.uasp.tool.remind;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 替换类别 0 顺序替换 1名称替换 2存在则显示
 * 
 * @author xx
 */
public enum RemindReplaceKind {
    ORDER(0, "顺序替换"), NAME(1, "名称替换"), EXIST(2, "存在则显示");

    private final int id;

    private final String displayName;

    private RemindReplaceKind(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static RemindReplaceKind findById(int id) {
        for (RemindReplaceKind item : RemindReplaceKind.values()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return ORDER;
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new LinkedHashMap<String, String>(6);
        for (RemindReplaceKind item : RemindReplaceKind.values()) {
            result.put(String.valueOf(item.getId()), item.getDisplayName());
        }
        return result;
    }

}
