package com.huigou.uasp.bmp.opm.domain.query;

import org.springframework.util.Assert;

import com.huigou.data.domain.query.QueryAbstractRequest;
import com.huigou.util.StringUtil;

public class PermissionsQueryRequest extends QueryAbstractRequest {

    private String roleId;

    private String keyword;

    private String permissionId;

    private String singlePerson;

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

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getSinglePerson() {
        return singlePerson;
    }

    public void setSinglePerson(String singlePerson) {
        this.singlePerson = singlePerson;
    }

    public boolean isSingle() {
        if (StringUtil.isBlank(singlePerson)) {
            return false;
        }
        return singlePerson.equals("1");
    }

    @Override
    public void checkConstraints() {
        Assert.hasText(this.permissionId, "参数permissionId不能为空。");
    }

    public void checkRoleConstraints() {
        Assert.hasText(this.roleId, "参数roleId不能为空。");
    }
}
