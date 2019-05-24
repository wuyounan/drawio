package com.huigou.uasp.log.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 日志实体 ORCL
 * 
 * @author yuanwf
 */
@Entity
@Table(name = "SA_OperationLog")
public class DBBizLog extends AbstractEntity implements BizLog {

    private static final long serialVersionUID = 3276831710702768067L;

    /**
     * 系统ID
     */
    @Column(name = "app_Id")
    private String appId;

    /**
     * 系统编码
     */
    @Column(name = "app_Code")
    private String appCode;

    /**
     * 系统名字
     */
    @Column(name = "app_Name")
    private String appName;

    /**
     * 机构ID
     */
    @Column(name = "organ_Id")
    private String organId;

    /**
     * 机构名称
     */
    @Column(name = "organ_Name")
    private String organName;

    /**
     * 部门ID
     */
    @Column(name = "dept_Id")
    private String deptId;

    /**
     * 部门名称
     */
    @Column(name = "dept_Name")
    private String deptName;

    /**
     * 岗位ID
     */
    @Column(name = "position_Id")
    private String positionId;

    /**
     * 岗位名称
     */
    @Column(name = "position_Name")
    private String positionName;

    /**
     * 人员ID
     */
    @Column(name = "person_Member_Id")
    private String personMemberId;

    /**
     * 人员名称
     */
    @Column(name = "person_Member_Name")
    private String personMemberName;

    /**
     * 全路径ID
     */
    @Column(name = "full_Id")
    private String fullId;

    /**
     * 全路径名称
     */
    @Column(name = "full_Name")
    private String fullName;

    /**
     * 角色ID
     */
    @Column(name = "Role_Kind_Id")
    private String roleKindId;

    /**
     * 角色名称
     */
    @Column(name = "Role_Kind_Name")
    private String roleKindName;

    /**
     * 开始时间
     */
    @Column(name = "Begin_Date")
    private Date beginDate;

    /**
     * 结束时间
     */
    @Column(name = "End_Date")
    private Date endDate;

    /**
     * IP
     */
    @Column(name = "IP")
    private String ip;

    /**
     * MAC
     */
    @Column(name = "MAC")
    private String mac;

    /**
     * 类名
     */
    @Column(name = "class_Name")
    private String className;

    /**
     * 方法名
     */
    @Column(name = "method_Name")
    private String methodName;

    /**
     * 日志类型
     */
    @Column(name = "log_Type")
    private String logType;

    /**
     * 操作名称
     */
    @Column(name = "operate_Name")
    private String operateName;

    /**
     * 状态ID
     */
    @Column(name = "status_Id")
    private int statusId;

    /**
     * 状态名称
     */
    @Column(name = "status_Name")
    private String statusName;

    /**
     * 简要描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 人员密级ID
     */
    @Column(name = "Person_Security_Level_Id")
    private String personSecurityLevelId;

    /**
     * 人员密级名称
     */
    @Column(name = "Person_Security_Level_Name")
    private String personSecurityLevelName;

    /**
     * 资源密级ID
     */
    @Column(name = "Resource_Security_Level_Id")
    private String resourceSecurityLevelId;

    /**
     * 资源密级Name
     */
    @Column(name = "Resource_Security_Level_Name")
    private String resourceSecurityLevelName;

    /**
     * 机器密级ID
     */
    @Column(name = "Machine_Security_Level_Id")
    private String machineSecurityLevelId;

    /**
     * 机器密级名称
     */
    @Column(name = "Machine_Security_Level_Name")
    private String machineSecurityLevelName;

    @Transient
    private BizLogDetail bizLogDetail;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
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

    public void setPersonMemberName(String personMemberName) {
        this.personMemberName = personMemberName;
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

    public String getRoleKindName() {
        return roleKindName;
    }

    public void setRoleKindName(String roleKindName) {
        this.roleKindName = roleKindName;
    }

    public String getRoleKindId() {
        return roleKindId;
    }

    public void setRoleKindId(String roleKindId) {
        this.roleKindId = roleKindId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPersonSecurityLevelId() {
        return personSecurityLevelId;
    }

    public void setPersonSecurityLevelId(String personSecurityLevelId) {
        this.personSecurityLevelId = personSecurityLevelId;
    }

    public String getPersonSecurityLevelName() {
        return personSecurityLevelName;
    }

    public void setPersonSecurityLevelName(String personSecurityLevelName) {
        this.personSecurityLevelName = personSecurityLevelName;
    }

    public String getResourceSecurityLevelId() {
        return resourceSecurityLevelId;
    }

    public void setResourceSecurityLevelId(String resourceSecurityLevelId) {
        this.resourceSecurityLevelId = resourceSecurityLevelId;
    }

    public String getResourceSecurityLevelName() {
        return resourceSecurityLevelName;
    }

    public void setResourceSecurityLevelName(String resourceSecurityLevelName) {
        this.resourceSecurityLevelName = resourceSecurityLevelName;
    }

    public String getMachineSecurityLevelId() {
        return machineSecurityLevelId;
    }

    public void setMachineSecurityLevelId(String machineSecurityLevelId) {
        this.machineSecurityLevelId = machineSecurityLevelId;
    }

    public String getMachineSecurityLevelName() {
        return machineSecurityLevelName;
    }

    public void setMachineSecurityLevelName(String machineSecurityLevelName) {
        this.machineSecurityLevelName = machineSecurityLevelName;
    }

    @Override
    public BizLogDetail getBizLogDetail() {
        return this.bizLogDetail;
    }

    @Override
    public void setBizLogDetail(BizLogDetail bizLogDetail) {
        this.bizLogDetail = bizLogDetail;
    }
}
