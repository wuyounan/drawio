package com.huigou.data.datamanagement;

import java.util.LinkedHashMap;
import java.util.Map;

import com.huigou.util.StringUtil;

/**
 * 数据权限组织机构选择类别
 * 
 * @author xx
 */
public enum DataManageChooseOrgKind {
    CHOOSE("choose", "手工选择"), MANAGETYPE("manageType", "业务管理权限"), ORGFUN("orgfun", "组织机构函数");

    private String id;

    private String name;

    private DataManageChooseOrgKind(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static DataManageChooseOrgKind findById(String id) {
        if (StringUtil.isBlank(id)) {
            return CHOOSE;
        }
        for (DataManageChooseOrgKind item : DataManageChooseOrgKind.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return CHOOSE;
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new LinkedHashMap<String, String>(6);
        for (DataManageChooseOrgKind item : DataManageChooseOrgKind.values()) {
            result.put(item.getId(), item.getName());
        }
        return result;
    }

}