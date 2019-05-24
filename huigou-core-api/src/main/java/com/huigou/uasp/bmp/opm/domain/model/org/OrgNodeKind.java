package com.huigou.uasp.bmp.opm.domain.model.org;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * 组织节点类型
 * 
 * @author gongmm
 */
public enum OrgNodeKind {
    ROOT("根组织", 0), OGN("机构", 1), FLD("组织分类", 2), PRJ("项目组织", 3), DPT("部门", 4), POS("岗位", 5), PSM("人员", 6);

    private final String displayName;

    private final int level;

    private OrgNodeKind(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getLevel() {
        return this.level;
    }

    public static OrgNodeKind fromValue(String value) {
        Assert.hasText(value, "参数value不能为空。");

        for (OrgNodeKind orgNodeKind : OrgNodeKind.values()) {
            if (orgNodeKind.name().equalsIgnoreCase(value)) {
                return orgNodeKind;
            }
        }
        
        throw new IllegalArgumentException("无效的组织类型“%s”。 " + value);
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new HashMap<String, String>(6);
        for (OrgNodeKind item : OrgNodeKind.values()) {
            if (!item.equals(ROOT)) {
                result.put(item.name().toLowerCase(), item.getDisplayName());
            }
        }
        return result;
    }

}
