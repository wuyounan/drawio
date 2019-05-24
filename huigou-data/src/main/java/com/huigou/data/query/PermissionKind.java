package com.huigou.data.query;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 权限类型
 * <xs:enumeration value="fullId"/>
 * <xs:enumeration value="personId"/>
 * <xs:enumeration value="psmId"/>
 * <xs:enumeration value="deptId"/>
 * <xs:enumeration value="organId"/>
 * <xs:enumeration value="define"/>
 * <xs:enumeration value="data"/>
 * 
 * @author xx
 */
public enum PermissionKind {
    PERSON_ID("personId", "userId", "用户ID"),
    USER_ID("userId", "userId", "用户ID"),
    PSM_ID("psmId", "personMemberId", "用户成员ID"),
    DEPT_ID("deptId", "deptId", "部门ID"),
    ORGAN_ID("organId", "orgId", "组织ID"),
    FULL_ID("fullId", "null", "组织路径"),
    DEFINE("define", "null", "自定义"),
    DATA("data", "null", "数据管理权限");

    private final String id;

    /**
     * 从Operator中取值属性
     */
    private final String propertyName;

    private final String displayName;

    private PermissionKind(String id, String propertyName, String displayName) {
        this.id = id;
        this.propertyName = propertyName;
        this.displayName = displayName;
    }

    public String toString() {
        return String.valueOf(this.id);
    }

    public String getId() {
        return this.id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PermissionKind findById(String id) {
        for (PermissionKind item : PermissionKind.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static Map<String, String> getChooseData() {
        Map<String, String> result = new LinkedHashMap<String, String>(6);
        for (PermissionKind item : PermissionKind.values()) {
            if (item != USER_ID && item != DEFINE && item != DATA) {
                result.put(item.getId(), item.getDisplayName());
            }
        }
        return result;
    }

}
