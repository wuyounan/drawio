package com.huigou.uasp.bmp.opm.domain.model.access;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.Creator;

/**
 * 角色权限
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_OPRolePermission")
public class RolePermission extends AbstractEntity {

    private static final long serialVersionUID = 2709901375882241845L;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "permission_id")
    private String permissionId;

    @Embedded
    private Creator creator;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

}
