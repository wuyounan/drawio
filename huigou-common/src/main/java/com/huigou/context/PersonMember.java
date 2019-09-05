package com.huigou.context;

import java.io.Serializable;

public class PersonMember extends OrgNode implements Serializable {

    private static final long serialVersionUID = -8669681671148390563L;
    
    private String personId;
    
    private String orgId;

    private String orgCode;

    private String orgName;

    private String deptId;

    private String deptCode;

    private String deptName;

    private String positionId;

    private String positionCode;

    private String positionName;

    private String loginName;

    private SecurityGrade securityGrade;
    
    private String tenantId;

    protected User owner = null;

    protected String agentProcess = null;

    public PersonMember() {

    }

    public PersonMember(String id, String name, String code, String fullId, String fullName, String fullCode) {
        super(id, name, code, fullId, fullName, fullCode, "psm");
    }

    public PersonMember(String id, String name, String code, String fullId, String fullName, String fullCode, User user) {
        super(id, name, code, fullId, fullName, fullCode, "psm");
        this.owner = user;
    }

    /**
     * 得到人员
     * 
     * @return
     */
    public User getUser() {
        return this.owner;
    }

    /**
     * 得到岗位
     * 
     * @return
     */
    public OrgNode getPosition() {
        for (Object parent = this; parent != null; parent = ((OrgNode) parent).getParent()) {
            if ("pos".equals(((OrgNode) parent).getType())) {
                return (OrgNode) parent;
            }
        }
        return null;
    }

    /**
     * 得到部门
     * 
     * @return
     */
    public OrgNode getDept() {
        for (Object parent = this; parent != null; parent = ((OrgNode) parent).getParent()) {
            if ("dpt".equals(((OrgNode) parent).getType())) {
                return (OrgNode) parent;
            }
        }
        return null;
    }

    /**
     * 得到机构
     * 
     * @return
     */
    public OrgNode getOrgn() {
        for (Object parent = this; parent != null; parent = ((OrgNode) parent).getParent()) {
            if ("ogn".equals(((OrgNode) parent).getType())) {
                return (OrgNode) parent;
            }
        }
        return null;
    }

    public OrgNode getOrg() {
        return getParent();
    }

    public String getAgentProcess() {
        return this.agentProcess;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    @Override
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    
    @Override
    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Override
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override
    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    @Override
    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    @Override
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    @Override
    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    @Override
    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

}
