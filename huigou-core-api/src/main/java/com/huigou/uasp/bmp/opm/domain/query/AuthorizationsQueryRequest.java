package com.huigou.uasp.bmp.opm.domain.query;

import com.huigou.data.domain.query.QueryAbstractRequest;

public class AuthorizationsQueryRequest extends QueryAbstractRequest {

    private String orgId;

    private String roleCode;

    private String roleName;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
