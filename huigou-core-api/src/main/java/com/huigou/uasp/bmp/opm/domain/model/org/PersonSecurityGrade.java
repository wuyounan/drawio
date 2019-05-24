package com.huigou.uasp.bmp.opm.domain.model.org;

import java.util.HashMap;
import java.util.Map;

import com.huigou.util.StringUtil;

/**
 * 人员安全等级
 * 
 * @author gongmm
 */
public enum PersonSecurityGrade {
    NON_SECRET("NON_SECRET", "非密"), COMMON("COMMON", "一般"), IMPORTANCE("IMPORTANCE", "重要"), CORE("CORE", "核心");

    private final String id;

    private final String displayName;

    private PersonSecurityGrade(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static PersonSecurityGrade fromId(String id) {
        if (StringUtil.isBlank(id)) {
            return null;
        }
        for (PersonSecurityGrade item : PersonSecurityGrade.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        throw new IllegalArgumentException(String.format("无人员涉密等级“%s”。", new Object[] { Integer.valueOf(id) }));
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new HashMap<String, String>(5);
        for (PersonSecurityGrade item : PersonSecurityGrade.values()) {
            result.put(item.getId(), item.getDisplayName());
        }
        return result;
    }
}
