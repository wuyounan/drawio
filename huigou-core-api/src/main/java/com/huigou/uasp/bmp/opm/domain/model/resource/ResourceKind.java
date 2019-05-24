package com.huigou.uasp.bmp.opm.domain.model.resource;

/**
 * 资源类别
 * 
 * @author gongmm
 */
public enum ResourceKind {
    FUN("fun", "功能权限"), UI_ELEMENT("uiElement", "界面元素");

    private final String id;

    private final String displayName;

    private ResourceKind(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getId() {
        return this.id;
    }

}
