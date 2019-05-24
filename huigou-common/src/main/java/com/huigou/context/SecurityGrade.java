package com.huigou.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.huigou.util.StringUtil;

/**
 * 用于指定人员、计算机和业务数据的密级
 * 
 * @author gongmm
 */
public enum SecurityGrade {
    NONE("NONE", ""),
    NON_SECRET("NON_SECRET", "非密"),
    INTERIOR("INTERIOR", "内部"),
    SECRET("SECRET", "秘密"),
    CONFIDENTIALITY("CONFIDENTIALITY", "机密"),
    TOP_SECRET("TOP_SECRET", "绝密");

    private final String id;

    private final String displayName;

    private SecurityGrade(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static SecurityGrade fromId(String id) {
        if (StringUtil.isBlank(id)) {
            return NONE;
        }

        for (SecurityGrade securityGrade : SecurityGrade.values()) {
            if (securityGrade.id.equalsIgnoreCase(id)) {
                return securityGrade;
            }
        }

        throw new IllegalArgumentException(String.format("无效的密级类型“%s”。", id));
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new LinkedHashMap<String, String>(5);
        for (SecurityGrade item : SecurityGrade.values()) {
            if (item != NONE) {
                result.put(item.getId(), item.getDisplayName());
            }
        }
        return result;
    }

}
