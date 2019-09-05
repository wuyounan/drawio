package com.huigou.uasp.bpm.configuration.domain.model;

/**
 * 处理人类型
 *
 * @author gongmm
 */
public enum HandlerKind {
    /**
     * 管理权限
     */
    MANAGE_AUTHORITY("ManageAuthority", " 管理权限"),
    /**
     * 岗位
     */
    POS("Pos", "岗位"),
    /**
     * 人员成员
     */
    PSM("Psm", "人员成员"),
    /**
     * 部门
     */
    DEPT("Dept", "部门"),
    /**
     * 管理人员函数
     */
    MANAGER_FUN("ManagerFun", "管理人员函数"),
    /**
     * 基础段
     */
    SEGMENTATION("Segmentation", "基础段"),
    /**
     * 手工选择
     */
    MANUAL_SELECTION("ManualSelection", "手工选择"),
    /**
     * 范围选择
     */
    SCOPE_SELECTION("ScopeSelection", "范围选择");

    private final String id;

    private final String displayName;

    HandlerKind(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static HandlerKind fromId(String id) {
        if (id.equalsIgnoreCase("ManageAuthority")) {
            return MANAGE_AUTHORITY;
        } else if (id.equalsIgnoreCase("Pos")) {
            return POS;
        } else if (id.equalsIgnoreCase("Psm")) {
            return PSM;
        } else if (id.equalsIgnoreCase("Dept")) {
            return DEPT;
        } else if (id.equalsIgnoreCase("ManagerFun")) {
            return MANAGER_FUN;
        } else if (id.equalsIgnoreCase("Segmentation")) {
            return SEGMENTATION;
        } else if (id.equalsIgnoreCase("ManualSelection")) {
            return MANUAL_SELECTION;
        } else if (id.equalsIgnoreCase("ScopeSelection")) {
            return SCOPE_SELECTION;
        } else {
            throw new RuntimeException(String.format("无效的处理人类型“%s”。", id));
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }

    public boolean equalsById(String id) {
        return this.id.equalsIgnoreCase(id);
    }

    public static boolean isSelection(String id) {
        return id.equalsIgnoreCase("ManualSelection") || id.equalsIgnoreCase("ScopeSelection");

    }

    public boolean isSelection() {
        return id.equalsIgnoreCase("ManualSelection") || id.equalsIgnoreCase("ScopeSelection");

    }
}
