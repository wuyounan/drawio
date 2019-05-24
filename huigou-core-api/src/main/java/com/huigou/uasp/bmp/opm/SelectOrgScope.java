package com.huigou.uasp.bmp.opm;

import com.huigou.exception.ApplicationException;
import com.huigou.util.StringUtil;

/**
 * 选择组织范围
 * 
 * @author gongmm
 */
public enum SelectOrgScope {
    ALL("all", "所有"), TENANT("tenant", "租户");

    private final String id;

    private final String displayName;

    private SelectOrgScope(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SelectOrgScope fromId(String id) {
        if (StringUtil.isBlank(id)) {
            return ALL;
        }
        id = id.toLowerCase();
        SelectOrgScope result;
        switch (id) {
        case "all":
            result = ALL;
            break;
        case "tenant":
            result = TENANT;
            break;
        default:
            throw new ApplicationException(String.format("无效的SelectOrgScope“%s”。", id));
        }
        return result;
    }
}
