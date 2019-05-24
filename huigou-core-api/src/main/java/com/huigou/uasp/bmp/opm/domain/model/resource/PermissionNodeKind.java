package com.huigou.uasp.bmp.opm.domain.model.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionResourceKind;

/**
 * 节点类型
 * 
 * @author gongmm
 */

public enum PermissionNodeKind {
    FOLDER("folder", "文件夹"),
    FUN("fun", "功能"),
    PERMISSION("permission", "权限"),
    UI_ELEMENT("uiElement", "界面元素"),
    REMIND("remind", "消息提醒"),
    BUSINESSCLASS("businessclass", "业务分类");

    private final String id;

    private final String displayName;

    private PermissionNodeKind(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getId() {
        return this.id;
    }

    public static Map<String, String> getAllMap() {
        Map<String, String> map = new LinkedHashMap<String, String>(PermissionNodeKind.values().length);
        for (PermissionNodeKind c : PermissionNodeKind.values()) {
            map.put(String.valueOf(c.getId()), c.getDisplayName());
        }
        return map;
    }

    public static Map<String, String> getMap(PermissionResourceKind resourceKind) {
        Map<String, String> map = new LinkedHashMap<String, String>(PermissionNodeKind.values().length);
        if (resourceKind == PermissionResourceKind.FUN) {
            map.put(FOLDER.getId(), FOLDER.getDisplayName());
            map.put(FUN.getId(), FUN.getDisplayName());
            map.put(PERMISSION.getId(), PERMISSION.getDisplayName());
            map.put(UI_ELEMENT.getId(), UI_ELEMENT.getDisplayName());
        } else if (resourceKind == PermissionResourceKind.REMIND) {
            map.put(FOLDER.getId(), FOLDER.getDisplayName());
            map.put(REMIND.getId(), REMIND.getDisplayName());
        } else if (resourceKind == PermissionResourceKind.BUSINESSCLASS) {
            map.put(FOLDER.getId(), FOLDER.getDisplayName());
            map.put(BUSINESSCLASS.getId(), BUSINESSCLASS.getDisplayName());
        }
        return map;
    }
}
