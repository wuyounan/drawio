package com.huigou.report.cboard.domain.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

import com.huigou.data.domain.model.CommonDomainConstants;

public enum UIParamKind {
                         BIZ_ORG_TREE_VIEW("BIZ_ORG_TREE_VIEW", "业务组织树"),
                         DATE_PICKER("DATE_PICKER", "日期控件"),
                         MONTH_PICKER("MONTH_PICKER", "年月控件"),
                         MANUAL_INPUT("MANUAL_INPUT", "手工录入");

    private String id;

    private String displayName;

    private UIParamKind(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static UIParamKind fromId(String id) {
        // 1、前置条件检查
        Assert.hasText(id, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "id"));
        // 2、转换
        for (UIParamKind item : UIParamKind.values()) {
            if (item.id.equalsIgnoreCase(id)) {
                return item;
            }
        }
        // 3、不能转换
        throw new IllegalArgumentException(String.format("无效的UIParamKind类型“%s”", id));
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = new LinkedHashMap<String, String>(values().length);
        for (UIParamKind item : UIParamKind.values()) {
            map.put(String.valueOf(item.getId()), item.getDisplayName());
        }
        return map;
    }

}
