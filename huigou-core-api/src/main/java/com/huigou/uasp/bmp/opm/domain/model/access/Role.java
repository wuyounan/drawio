package com.huigou.uasp.bmp.opm.domain.model.access;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.huigou.annotation.JsonIgnore;
import com.huigou.data.domain.model.TreeWithTenantEntity;

@Entity
@Table(name = "SA_OPRole")
public class Role extends TreeWithTenantEntity {

    private static final long serialVersionUID = -3166308122362453066L;

    public static final String GLOBAL_TENANT_KIND = "global";

    /**
     * 节点类型
     */
    @Column(name = "node_Kind_Id")
    private Integer nodeKindId;

    /**
     * 角色类型
     */
    @Column(name = "role_kind_id")
    private String kindId;

    /**
     * 描述
     */
    private String description;

    @Column(name = "role_person_kind")
    private String rolePersonKind;

    @ManyToMany()
    @JoinTable(name = "SA_OPRolePermission", joinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "permission_id", referencedColumnName = "id") })
    private List<Permission> permissions;

    public Integer getNodeKindId() {
        return nodeKindId;
    }

    public void setNodeKindId(Integer nodeKindId) {
        this.nodeKindId = nodeKindId;
    }

    public String getKindId() {
        return kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRolePersonKind() {
        return rolePersonKind;
    }

    public void setRolePersonKind(String rolePersonKind) {
        this.rolePersonKind = rolePersonKind;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * 删除权限
     * 
     * @param permissionIds
     *            权限ID列表
     */
    public void removePermissions(List<String> permissionIds) {
        super.removeDetails(this.getPermissions(), permissionIds);
    }

    /**
     * 构建权限实体列表
     * 
     * @param inputPermissions
     *            权限列表
     * @return
     */
    @SuppressWarnings("unchecked")
    public void buildPermissions(List<Permission> inputPermissions) {
        this.setPermissions((List<Permission>) super.buildDetails(this.getPermissions(), inputPermissions));
    }

    @Override
    public void checkConstraints() {
        super.checkConstraints();
        Assert.hasText(getTenantId(), "租户ID不能为空。");
    }

    @JsonIgnore
    public boolean isGlobalRole() {
        return Role.GLOBAL_TENANT_KIND.equals(this.getTenantId());
    }

    @JsonIgnore
    public boolean isFolder() {
        return 1 == this.nodeKindId;
    }
}
