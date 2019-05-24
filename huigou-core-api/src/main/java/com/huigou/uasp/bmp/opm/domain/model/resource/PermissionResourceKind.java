package com.huigou.uasp.bmp.opm.domain.model.resource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 权限资源类别
 * 
 * @author xx
 */
public enum PermissionResourceKind {
    FUN("fun", "功能权限"), REMIND("remind", "消息提醒"), BUSINESSCLASS("businessclass", "业务分类");

    private final String id;

    private final String displayName;

    private PermissionResourceKind(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getId() {
        return this.id;
    }

    public static PermissionResourceKind findById(String id) {
        for (PermissionResourceKind item : PermissionResourceKind.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new LinkedHashMap<String, String>(6);
        for (PermissionResourceKind item : PermissionResourceKind.values()) {
            result.put(String.valueOf(item.getId()), item.getDisplayName());
        }
        return result;
    }

}
