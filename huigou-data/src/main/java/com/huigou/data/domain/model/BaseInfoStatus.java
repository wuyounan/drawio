package com.huigou.data.domain.model;

import java.util.HashMap;
import java.util.Map;

import com.huigou.exception.ApplicationException;

/**
 * 基础数据状态
 * 
 * @author Gerald
 */
public enum BaseInfoStatus {

    DELETED(-1, "删除"), DISABLED(0, "禁用"), ENABLED(1, "启用");

    private final int id;

    private final String displayName;

    private BaseInfoStatus(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String toString() {
        return String.valueOf(this.id);
    }

    public int getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static BaseInfoStatus fromId(int id) {
        switch (id) {
        case 0:
            return DISABLED;
        case 1:
            return ENABLED;
        case -1:
            return DELETED;
        default:
            throw new ApplicationException(String.format("无效的状态“%s”。", new Object[] { Integer.valueOf(id) }));
        }
    }

    public static Map<Integer, String> getData() {
        Map<Integer, String> result = new HashMap<Integer, String>(2);
        for (BaseInfoStatus item : BaseInfoStatus.values()) {
            if (item != DELETED) {
                result.put(item.getId(), item.getDisplayName());
            }
        }
        return result;
    }

    public static boolean isDeleted(int id) {
        return DELETED.getId() == id;
    }

}
