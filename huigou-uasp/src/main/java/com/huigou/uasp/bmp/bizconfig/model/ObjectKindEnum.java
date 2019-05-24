package com.huigou.uasp.bmp.bizconfig.model;

import java.util.LinkedHashMap;
import java.util.Map;

public enum ObjectKindEnum {
    // 流程相关
    FLOW_DOMAIN("flowDomain", "流程域", "流程", "em_business_process"),
    FLOW("flow", "流程", "流程", "em_business_process"),
    // 活动相关
    ACTIVITY("activity", "活动", "活动", "em_process_node"),
    RULE("rule", "规则", "规则", "em_process_node"),
    INTERFACE("interface", "接口", "接口", "em_process_node"),
    SHADOW("shadow", "影子", "影子", "em_process_node");

    private final String id;

    private final String displayName;

    private final String ownerName;

    private final String tableName;

    private ObjectKindEnum(String id, String displayName, String ownerName, String tableName) {
        this.id = id;
        this.displayName = displayName;
        this.ownerName = ownerName;
        this.tableName = tableName;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getTableName() {
        return tableName;
    }

    public static ObjectKindEnum findById(String id) {
        for (ObjectKindEnum item : ObjectKindEnum.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = new LinkedHashMap<String, String>(ObjectKindEnum.values().length);
        for (ObjectKindEnum c : ObjectKindEnum.values()) {
            map.put(c.getId(), c.getDisplayName());
        }
        return map;
    }

    public static Map<String, String> getFlowNodeKind() {
        Map<String, String> map = new LinkedHashMap<String, String>(3);
        map.put(ACTIVITY.getId(), ACTIVITY.getDisplayName());
        map.put(RULE.getId(), RULE.getDisplayName());
        map.put(INTERFACE.getId(), INTERFACE.getDisplayName());
        map.put(SHADOW.getId(), SHADOW.getDisplayName());
        return map;
    }

}