package com.huigou.uasp.bmp.opm.domain.query;

import org.springframework.util.Assert;

import com.huigou.data.domain.query.QueryAbstractRequest;
import com.huigou.uasp.bmp.opm.OpmUtil;
import com.huigou.util.StringUtil;

public class AuthorizedPermissionsByOrgFullIdQueryRequest extends QueryAbstractRequest {
    private String orgId;

    private String orgFullId;

    private String orgKindId;

    private String permissionName;

    private String onlyFunction;

    public String getOrgFullId() {
        return orgFullId;
    }

    public void setOrgFullId(String orgFullId) {
        this.orgFullId = orgFullId;
    }

    public String getOrgKindId() {
        return orgKindId;
    }

    public void setOrgKindId(String orgKindId) {
        this.orgKindId = orgKindId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public boolean isPerson() {
        if (StringUtil.isBlank(orgKindId)) {
            return false;
        }
        return orgKindId.equals("psm");
    }

    public String getPersonId() {
        if (isPerson()) {
            return OpmUtil.getPersonIdFromPersonMemberId(orgId);
        }
        return orgId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
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

    @Override
    public void checkConstraints() {
        super.checkConstraints();
        Assert.hasText(this.orgFullId, "参数orgFullId不能为空。");
    }
}
