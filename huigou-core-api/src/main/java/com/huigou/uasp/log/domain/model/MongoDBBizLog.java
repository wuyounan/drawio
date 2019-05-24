package com.huigou.uasp.log.domain.model;

import java.util.Date;

import javax.persistence.Transient;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MongoDB日志实体
 * 
 * @author yuanwf
 */
@Document(collection = "SA.OperationLog")
public class MongoDBBizLog implements BizLog {

    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 系统ID
     */
    @Indexed
    private String appId;

    /**
     * 系统编码
     */
    private String appCode;

    /**
     * 系统名字
     */
    @Indexed
    private String appName;

    /**
     * 机构ID
     */
    private String organId;

    /**
     * 机构名称
     */
    private String organName;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 岗位ID
     */
    private String positionId;

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 人员ID
     */
    private String personMemberId;

    /**
     * 人员名称
     */
    @Indexed
    private String personMemberName;

    /**
     * 全路径ID
     */
    @Indexed
    private String fullId;

    /**
     * 全路径名称
     */
    private String fullName;

    /**
     * 角色ID
     */
    @Indexed
    private String roleKindId;

    /**
     * 角色名称
     */
    private String roleKindName;

    /**
     * 开始时间
     */
    @Indexed
    private Date beginDate;

    /**
     * 结束时间
     */
    @Indexed
    private Date endDate;

    /**
     * IP
     */
    @Indexed
    private String ip;

    /**
     * MAC
     */
    private String mac;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 操作名称
     */
    private String operateName;

    /**
     * 状态ID
     */
    @Indexed
    private int statusId;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 简要描述
     */
    private String description;

    /**
     * 人员密级ID
     */
    private String personSecurityLevelId;

    /**
     * 人员密级名称
     */
    private String personSecurityLevelName;

    /**
     * 资源密级ID
     */
    private String resourceSecurityLevelId;

    /**
     * 资源密级Name
     */
    private String resourceSecurityLevelName;

    /**
     * 机器密级ID
     */
    private String machineSecurityLevelId;

    /**
     * 机器密级名称
     */
    private String machineSecurityLevelName;

    /**
     * 版本号
     */
    private Long version;

    @Transient
    private BizLogDetail bizLogDetail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
