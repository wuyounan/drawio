package com.huigou.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huigou.exception.ApplicationException;

public enum ValidStatus {
    LOGIC_DELETE(-1, "删除"), DISABLED(0, "禁用"), ENABLED(1, "启用");

    private final Integer id;

    private final String displayName;

    private ValidStatus(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String toString() {
        return String.valueOf(this.id);
    }

    public Integer getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static ValidStatus fromId(int id) {
        switch (id) {
        case 0:
            return DISABLED;
        case 1:
            return ENABLED;
        case -1:
            return LOGIC_DELETE;
        default:
            throw new ApplicationException(String.format("无效的组织状态“%s”！", new Object[] { Integer.valueOf(id) }));
        }
    }

    public static Map<Integer, String> getData() {
        Map<Integer, String> result = new HashMap<Integer, String>(2);
        for (ValidStatus item : ValidStatus.values()) {
            if (item != LOGIC_DELETE) {
                result.put(item.getId(), item.getDisplayName());
            }
        }
        return result;
    }

    public static List<Integer> toList(Collection<ValidStatus> fromStatuses) {

        List<Integer> result = new ArrayList<Integer>(fromStatuses.size());

        for (ValidStatus item : fromStatuses) {
            result.add(item.getId());
        }
        return result;
    }

}
