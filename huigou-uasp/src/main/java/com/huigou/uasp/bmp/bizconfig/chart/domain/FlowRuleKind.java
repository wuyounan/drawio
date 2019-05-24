package com.huigou.uasp.bmp.bizconfig.chart.domain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xx
 */
public enum FlowRuleKind {
    OR("OR", "或"), XOR("XOR", "异或"), AND("AND", "与");

    private final String id;

    private final String displayName;

    private FlowRuleKind(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static FlowRuleKind findById(String id) {
        for (FlowRuleKind item : FlowRuleKind.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return OR;
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new LinkedHashMap<String, String>(6);
        for (FlowRuleKind item : FlowRuleKind.values()) {
            result.put(String.valueOf(item.getId()), item.getDisplayName());
        }
        return result;
    }

}
