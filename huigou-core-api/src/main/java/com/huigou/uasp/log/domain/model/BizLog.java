package com.huigou.uasp.log.domain.model;

import java.util.Date;

/**
 * 日志实体接口
 * 
 * @author gongmm
 */
public interface BizLog {
    
    public static final Integer DESCRIPTION_MAX_LENGTH = 680;

    String getId();

    void setId(String id);

    String getAppId();

    void setAppId(String appId);

    String getAppCode();

    void setAppCode(String appCode);

    String getAppName();

    void setAppName(String appName);

    String getOrganId();

    void setOrganId(String organId);

    String getOrganName();

    void setOrganName(String organName);

    String getDeptId();

    void setDeptId(String deptId);

    String getDeptName();

    void setDeptName(String deptName);

    String getPositionId();

    void setPositionId(String positionId);

    String getPositionName();

    void setPositionName(String positionName);

    String getPersonMemberId();

    void setPersonMemberId(String personMemberId);

    String getPersonMemberName();

    void setPersonMemberName(String personMemberName);

    String getFullId();

    void setFullId(String fullId);

    String getFullName();

    void setFullName(String fullName);

    String getRoleKindId();

    void setRoleKindId(String roleKindId);

    String getRoleKindName();

    void setRoleKindName(String roleKindName);

    Date getBeginDate();

    void setBeginDate(Date beginDate);

    Date getEndDate();

    void setEndDate(Date endDate);

    String getIp();

    void setIp(String ip);

    String getMac();

    void setMac(String mac);

    String getClassName();

    void setClassName(String className);

    String getMethodName();

    void setMethodName(String methodName);

    String getLogType();

    void setLogType(String logType);

    String getOperateName();

    void setOperateName(String operateName);

    int getStatusId();

    void setStatusId(int statusId);

    String getStatusName();

    void setStatusName(String statusName);

    /**
     * 简要描述
     * @return
     */
    String getDescription();

    void setDescription(String description);

    String getPersonSecurityLevelId();

    void setPersonSecurityLevelId(String personSecurityLevelId);

    String getPersonSecurityLevelName();

    void setPersonSecurityLevelName(String personSecurityLevelName);

    String getResourceSecurityLevelId();

    void setResourceSecurityLevelId(String resourceSecurityLevelId);

    String getResourceSecurityLevelName();

    void setResourceSecurityLevelName(String resourceSecurityLevelName);

    String getMachineSecurityLevelId();

    void setMachineSecurityLevelId(String machineSecurityLevelId);

    String getMachineSecurityLevelName();

    void setMachineSecurityLevelName(String machineSecurityLevelName);

    Long getVersion();

    void setVersion(Long version);

    void setBizLogDetail(BizLogDetail bizLogDetail);

    BizLogDetail getBizLogDetail();
}
