package com.huigou.uasp.bmp.opm.domain.query;

import com.huigou.data.domain.query.QueryAbstractRequest;
import com.huigou.util.StringUtil;

public class PermissionsByRoleIdQueryRequest extends QueryAbstractRequest {

    private String roleId;

    private String keyword;

    private String onlyFunction;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOnlyFunction() {
        return onlyFunction;
    }

    public void setOnlyFunction(String onlyFunction) {
        this.onlyFunction = onlyFunction;
    }

    public String getNodeKindId() {
        if (StringUtil.isBlank(onlyFunction)) {
            return "";
        }
        if (onlyFunction.equals("1")) {
            return "fun";
        }
        return "";
    }
}
