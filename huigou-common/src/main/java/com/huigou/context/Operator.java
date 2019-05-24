package com.huigou.context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.util.Assert;

import com.huigou.cache.SystemCache;
import com.huigou.util.Util;

/**
 * 操作员
 * 一个操作员对应一个人员 人员可以有对个岗位，
 * 对于一人多岗，在登录时选择相应的岗位
 * Operator-->(User [ pos1,pos2,pos3]);
 * 1、只列出对于岗位的权限
 * 2、任务中心列出人员的所有任务，但只能处理对于岗位的任务
 * 
 * @author gongmm
 */
public class Operator implements Serializable {

    private static final long serialVersionUID = -4620852653286742649L;

    private static final String SHOW_ORGAN_NAME_KEY = "operator.fullDisplayName.showOrganName";

    private static final String SHOW_POSITION_NAME_KEY = "operator.fullDisplayName.showPositionName";

    private User user = null;

    private Date loginDate = null;

    private String fullId;

    private String fullName;

    private String fullCode;

    private String orgId;

    private String orgCode;

    private String orgName;

    private String deptId;

    private String deptCode;

    private String deptName;

    private String positionId;

    private String positionCode;

    private String positionName;

    private String personMemberId;

    private String personMemberCode;

    private String personMemberName;

    private String ip;

    /**
     * 组织机构类型
     */
    private String orgAdminKind;

    /**
     * 部门类别
     */
    private String deptKind;

    /**
     * 区域类别
     */
    private String areaKind;

    /**
     * 角色类别
     * <p>
     * 不使用三员管理，角色类别为COMMON, 使用三员，取三员角色，没有三员角色，设置为COMMON
     */
    private RoleKind roleKind;

    public String getFullCode() {
        return fullCode;
    }

    public void setFullCode(String fullCode) {
        this.fullCode = fullCode;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPersonMemberId() {
        return personMemberId;
    }

    public void setPersonMemberId(String personMemberId) {
        this.personMemberId = personMemberId;
    }

    public String getPersonMemberName() {
        return personMemberName;
    }

    public void setPersonMemberName(String personName) {
        this.personMemberName = personName;
    }

    public String getPersonMemberCode() {
        return personMemberCode;
    }

    public void setPersonMemberCode(String personMemberCode) {
        this.personMemberCode = personMemberCode;
    }

    public String getFullDisplayName() {
        boolean showOrganName = Boolean.valueOf(SystemCache.getParameter(SHOW_ORGAN_NAME_KEY, String.class));
        boolean showPositionName = Boolean.valueOf(SystemCache.getParameter(SHOW_POSITION_NAME_KEY, String.class));
        
        String result = "";
        if (showOrganName) {
            result += String.format("%s.", this.orgName);
        }
        result += String.format("%s.", this.deptName);
        if (showPositionName) {
            result += String.format("%s.", this.positionName);
        }
        result +=  this.personMemberName;
        
        return result;
    }

    public Operator() {

    }

    public Operator(User person, Date loginDate) {
        Util.check(person != null, "登录的user不能为空。");
        this.user = person;
        this.loginDate = loginDate;
    }

    /**
     * 得到人员编码
     */
    public String getCode() {
        return this.user.getCode();
    }

    public String getUserId() {
        return this.user.getId();
    }

    /**
     * 得到登录人员的名称
     */
    public String getName() {
        return this.user.getName();
    }

    public void setOrgAdminKind(String orgAdminKind) {
        this.orgAdminKind = orgAdminKind;
    }

    public String getOrgAdminKind() {
        return this.orgAdminKind;
    }

    public String getDeptKind() {
        return deptKind;
    }

    public void setDeptKind(String deptKind) {
        this.deptKind = deptKind;
    }

    public String getAreaKind() {
        return areaKind;
    }

    public void setAreaKind(String areaKind) {
        this.areaKind = areaKind;
    }

    public Date getLoginDate() {
        return this.loginDate;
    }

    public User getLoginUser() {
        return this.user;
    }

    public void fillPersonMemberFullIds(List<String> fullIds) {
        this.user.getPersonMemberFullIds().addAll(fullIds);
    }

    public List<String> getPersonMemberFullIds() {
        return user.getPersonMemberFullIds();
    }

    public void fillRoleIds(List<String> roles) {
        this.user.getRoleIds().addAll(roles);
    }

    public List<String> getRoleIds() {
        return user.getRoleIds();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public RoleKind getRoleKind() {
        return roleKind;
    }

    public void setRoleKind(RoleKind roleKind) {
        this.roleKind = roleKind;
    }

    public void setLoginPerson(User user) {
        this.user = user;
    }

    public String getLoginName() {
        return this.user.getLoginName();
    }

    public static Operator newInstance(PersonMember personMember) {
        Assert.notNull(personMember, "参数personMember不能为空。");

        User user = new User(personMember);
        Operator operator = new Operator(user, new Date());
        operator.setOrgContext(personMember);

        return operator;
    }

    public void setOrgContext(PersonMember personMember) {
        Assert.notNull(personMember, "参数personMember不能为空。");

        this.setFullId(personMember.getFullId());
        this.setFullName(personMember.getFullName());
        this.setFullCode(personMember.getFullCode());

        this.setOrgId(personMember.getOrgId());
        this.setOrgCode(personMember.getOrgCode());
        this.setOrgName(personMember.getOrgName());

        this.setDeptId(personMember.getDeptId());
        this.setDeptCode(personMember.getDeptCode());
        this.setDeptName(personMember.getDeptName());

        this.setPositionId(personMember.getPositionId());
        this.setPositionCode(personMember.getPositionCode());
        this.setPositionName(personMember.getPositionName());

        this.setPersonMemberId(personMember.getId());
        this.setPersonMemberCode(personMember.getCode());
        this.setPersonMemberName(personMember.getName());
    }

    public String getTenantId() {
        return this.user.getTenantId();
    }

    public void setTenantId(String tenantId) {
        this.user.setTenantId(tenantId);
    }

    public String getTenantName() {
        return this.user.getTenantName();
    }

    public void setTenantName(String tenantName) {
        this.user.setTenantName(tenantName);
    }

    public String getRootOrgFullId() {
        return this.user.getRootOrgFullId();
    }

    public void setRootOrgFullId(String rootOrgFullId) {
        this.user.setRootOrgFullId(rootOrgFullId);
    }

    public String getRootOrgId() {
        return this.user.getRootOrgId();
    }

    public void setRootOrgId(String rootOrgId) {
        this.user.setRootOrgId(rootOrgId);
    }

    public boolean hasRootOrgPermission() {
        return "orgRoot".equals(getRootOrgId());
    }

    @Override
    public String toString() {
        return this.user.getId();
    }

    public boolean isCurrentOperator(String personMemberId) {
        Assert.hasText(personMemberId, "参数personMemberId不能为空。");
        return this.getPersonMemberId().equals(personMemberId);
    }

}
