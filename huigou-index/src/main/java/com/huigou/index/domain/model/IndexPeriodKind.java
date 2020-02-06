package com.huigou.index.domain.model;

import java.util.HashMap;
import java.util.Map;

public enum IndexPeriodKind {

    YEAR("YEAR", "年"), QUARTER("QUARTER", "季"), MONTH("MONTH", "月"), WEEK("WEEK", "周"), DAY("DAY", "日"), NONE("NULL", "null");

    private String id;

    private String displayName;

    private IndexPeriodKind(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static IndexPeriodKind fromId(String id) {
        if (id == null) {
            return NONE;
        }
        for (IndexPeriodKind indexPeriodKind : values()) {
            if (indexPeriodKind.getId().equalsIgnoreCase(id)) {
                return indexPeriodKind;
            }
        }
        throw new IllegalArgumentException(String.format("无效的指标周期类型: %s", id));
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>(values().length);
        for (IndexPeriodKind c : values()) {
            map.put(String.valueOf(c.getId()), c.getDisplayName());
        }
        return map;
    }

}
