package com.huigou.uasp.bpm;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理人合并类型
 * 
 * @author xx
 */
public enum MergeHandlerKind {
    NOT_MERGE(0, "不合并"), ADJACENT(1, "相邻合并"), BEHIND(2, "向后合并");

    private final int id;

    private final String displayName;

    private MergeHandlerKind(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static MergeHandlerKind fromId(Integer id) {
        if (id == null) {
            return ADJACENT;
        }
        switch (id.intValue()) {
        case 0:
            return NOT_MERGE;
        case 1:
            return ADJACENT;
        case 2:
            return BEHIND;
        default:
            return ADJACENT;
        }
    }

    public int getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>(MergeHandlerKind.values().length);
        for (MergeHandlerKind c : MergeHandlerKind.values()) {
            map.put(String.valueOf(c.getId()), c.getDisplayName());
        }
        return map;
    }
}
