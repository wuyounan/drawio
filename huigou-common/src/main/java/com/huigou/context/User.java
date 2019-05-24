package com.huigou.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.huigou.domain.ValidStatus;
import com.huigou.util.Util;

public class User implements Serializable {

    private static final long serialVersionUID = -3174605523689631937L;

    public static final int AGENT_ACTIVE = 1;

    public static final int AGENT_NO_ACTIVE = 0;

    public static final int VALUE_IS_OPERATOR = 1;

    public static final int VALUE_IS_NOT_OPERATOR = 0;

    /**
     * 用户ID
     */
    private String id;

    private String name;

    private String code;

    private String mainOrgId;

    private String mainOrgFullId;

    private String mainOrgFullName;

    private String mainOrgFullCode;

    private OrgNode mainOrg;

    private String loginName;

    private Integer status;

    private String certificateNo;

    private Integer isOperator;

    private Integer IsHidden;

    private String password;

    private SecurityGrade securityGrade;

    private String tenantId;
    
    private String tenantName;

    /**
     * 根组织ID
     */
    private String rootOrgId;
    
    /**
     * 根组织ID全路径
     */
    private String rootOrgFullId;
    
    
    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getMainOrgFullId() {
        return mainOrgFullId;
    }

    public void setMainOrgFullId(String mainOrgFullId) {
        this.mainOrgFullId = mainOrgFullId;
    }

    public String getMainOrgFullName() {
        return mainOrgFullName;
    }

    public void setMainOrgFullName(String mainOrgFullName) {
        this.mainOrgFullName = mainOrgFullName;
    }

    public String getMainOrgFullCode() {
        return mainOrgFullCode;
    }

    public void setMainOrgFullCode(String mainOrgFullCode) {
        this.mainOrgFullCode = mainOrgFullCode;
    }

    public Integer getStatus() {
        return status;
    }

    public ValidStatus getStatusEnum() {
        return ValidStatus.fromId(status);
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMainOrgId(String mainOrgId) {
        this.mainOrgId = mainOrgId;
    }

    public void setMainOrg(OrgNode mainOrg) {
        this.mainOrg = mainOrg;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setPersonMemberFullIds(List<String> personMemberFullIds) {
        this.personMemberFullIds = personMemberFullIds;
    }

    private List<String> personMemberFullIds = new ArrayList<String>();

    private List<String> roleIds = new ArrayList<String>();

    public User() {
    }

    public User(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public User(String id, String code, String name, String mainOrgId) {
        this(id, code, name);
        this.mainOrgId = mainOrgId;
    }

    public User(String id, String code, String name, String mainOrgId, String loginName, String mainOrgFullId, String mainOrgFullCode, String mainOrgFullName,
                String securityGradeId) {
        this(id, code, name, mainOrgId);
        this.loginName = loginName;
        this.mainOrgFullId = mainOrgFullId;
        this.mainOrgFullName = mainOrgFullName;
        this.mainOrgFullCode = mainOrgFullCode;

        this.securityGrade = SecurityGrade.fromId(securityGradeId);
    }

    public User(PersonMember personMember) {
        Assert.notNull(personMember, "参数PersonMember不能为空。");
        id = personMember.getPersonId();
        code = personMember.getCode();
        name = personMember.getName();
        mainOrgId = personMember.getId();
        mainOrgFullId = personMember.getFullId();
        mainOrgFullCode = personMember.getFullCode();
        mainOrgFullName = personMember.getFullName();
        loginName = personMember.getLoginName();
        securityGrade = personMember.getSecurityGrade();
        tenantId = personMember.getTenantId();
    }

    public String getMainOrgId() {
        return this.mainOrgId;
    }

    public OrgNode getMainOrg() {
        return this.mainOrg;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public List<String> getPersonMemberFullIds() {
        return personMemberFullIds;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    /**
     * 验证是否启用状态
     */
    public void checkEnabledStatus() {
        Util.check(getStatusEnum() == ValidStatus.ENABLED, String.format("人员“%s”状态为“%s”。", new Object[] { getName(), getStatusEnum().getDisplayName() }));
    }

    public Integer getIsOperator() {
        return isOperator;
    }

    public void setIsOperator(Integer isOperator) {
        this.isOperator = isOperator;
    }

    public Integer getIsHidden() {
        return IsHidden;
    }

    public void setIsHidden(Integer isHidden) {
        IsHidden = isHidden;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SecurityGrade getSecurityGrade() {
        return securityGrade;
    }

    public void setSecurityGrade(SecurityGrade securityGrade) {
        this.securityGrade = securityGrade;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getRootOrgId() {
        return rootOrgId;
    }

    public void setRootOrgId(String rootOrgId) {
        this.rootOrgId = rootOrgId;
    }
    
    public String getRootOrgFullId() {
        return rootOrgFullId;
    }

    
    public void setRootOrgFullId(String rootOrgFullId) {
        this.rootOrgFullId = rootOrgFullId;
    }

}