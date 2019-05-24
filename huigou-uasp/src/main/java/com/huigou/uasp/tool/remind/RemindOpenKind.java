package com.huigou.uasp.tool.remind;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 页面打开方式 0 新窗口 1 弹出
 * 
 * @author xx
 */
public enum RemindOpenKind {
    OPEN(0, "新窗口"), DIALOG(2, "弹出");

    private final int id;

    private final String displayName;

    private RemindOpenKind(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static RemindOpenKind findById(int id) {
        for (RemindOpenKind item : RemindOpenKind.values()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return OPEN;
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new LinkedHashMap<String, String>(6);
        for (RemindOpenKind item : RemindOpenKind.values()) {
            result.put(String.valueOf(item.getId()), item.getDisplayName());
        }
        return result;
    }

}
