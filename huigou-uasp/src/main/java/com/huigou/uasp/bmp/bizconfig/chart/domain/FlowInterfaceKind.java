package com.huigou.uasp.bmp.bizconfig.chart.domain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xx
 */
public enum FlowInterfaceKind {
    INPUT("input", "输入"), OUTPUT("output", "输出");

    private final String id;

    private final String displayName;

    private FlowInterfaceKind(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static FlowInterfaceKind findById(String id) {
        for (FlowInterfaceKind item : FlowInterfaceKind.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new LinkedHashMap<String, String>(6);
        for (FlowInterfaceKind item : FlowInterfaceKind.values()) {
            result.put(String.valueOf(item.getId()), item.getDisplayName());
        }
        return result;
    }

}
