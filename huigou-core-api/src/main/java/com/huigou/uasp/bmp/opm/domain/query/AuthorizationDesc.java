package com.huigou.uasp.bmp.opm.domain.query;

import java.util.Date;

import com.huigou.cache.DictUtil;

public class AuthorizationDesc {

    private String id;

    private String orgId;

    private String orgName;

    private String orgFullId;

    private String orgFullName;

    private String roleId;

    private String roleCode;

    private String roleName;

    private String roleKindId;

    private String createdById;

    private String createdByName;

    private Date createdDate;

    private String description;
    
    public AuthorizationDesc(String orgId, String orgfullName, String roleId){
        this.orgId = orgId;
        this.roleId = roleId;
        this.orgFullName  = orgfullName;
    }

    public AuthorizationDesc(String id, String orgId, String orgName, String orgFullId, String orgFullName, String roleId, String roleCode, String roleName,
                             String roleKindId, String createdById, String createdByName, Date createdDate, String description) {
        this.id = id;
        this.orgId = orgId;
        this.orgName = orgName;
        this.orgFullId = orgFullId;
        this.orgFullName = orgFullName;
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.roleKindId = roleKindId;
        this.createdById = createdById;
        this.createdByName = createdByName;
        this.createdDate = createdDate;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgFullId() {
        return orgFullId;
    }

    public void setOrgFullId(String orgFullId) {
        this.orgFullId = orgFullId;
    }

    public String getOrgFullName() {
        return orgFullName;
    }

    public void setOrgFullName(String orgFullName) {
        this.orgFullName = orgFullName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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

    public String getRoleKindId() {
        return roleKindId;
    }

    public void setRoleKindId(String roleKindId) {
        this.roleKindId = roleKindId;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKindIdTextView() {
        return DictUtil.getDictionaryDetailText("roleKindId", this.roleKindId);
    }

}
