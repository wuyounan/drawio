package com.huigou.uasp.log.domain.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.huigou.context.RoleKind;
import com.huigou.data.domain.query.QueryAbstractRequest;
import com.huigou.uasp.bmp.opm.domain.model.access.TMAuthorize;
import com.huigou.uasp.bmp.opm.repository.org.TMAuthorizeRepository;
import com.huigou.uasp.log.domain.model.LogStatus;
import com.huigou.util.StringPool;
import com.huigou.util.StringUtil;

/**
 * 操作日志查询
 * 
 * @author gongmm
 */
public class OperationLogQueryRequest extends QueryAbstractRequest {

    private String operatorRoleKindId;

    private String roleKindId;

    private String appName;

    private String fullId;

    private String statusId;

    private String description;

    private String logType;

    private String operationType;

    private String ip;

    private String personMemberId;

    private String personMemberName;

    private Date beginDate;

    private Date endDate;

    public String getOperatorRoleKindId() {
        return operatorRoleKindId;
    }

    public void setOperatorRoleKindId(String operatorRoleKindId) {
        this.operatorRoleKindId = operatorRoleKindId;
    }

    public String getRoleKindId() {
        return roleKindId;
    }

    public void setRoleKindId(String roleKindId) {
        this.roleKindId = roleKindId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public boolean allowQuery() {
        return StringUtil.isNotBlank(operatorRoleKindId) && StringUtil.isNotBlank(personMemberId);
    }

    public Map<String, String> getLogQueryCriteria(TMAuthorizeRepository tmAuthorizeRepository, boolean isEnableTspm) {
        // String targetLogType = "";
        String targetRoleKindId = "";
        String targetStatusId = "";
        String targetAppId = "";
        String targetFullId = "";

        Map<String, String> data = new HashMap<String, String>();
        if (isEnableTspm) {
            List<TMAuthorize> tmAuthorizes = tmAuthorizeRepository.findByManagerIdAndRoleKindId(personMemberId, roleKindId);
            Assert.state(tmAuthorizes != null, "没有三员授权，不能查询三员日期。");

            List<String> fullIds = new ArrayList<String>();
            List<String> appIds = new ArrayList<String>();

            for (TMAuthorize tmAuthorize : tmAuthorizes) {
                if (fullIds.indexOf(tmAuthorize.getSubordinationFullId()) == -1) {
                    fullIds.add(tmAuthorize.getSubordinationFullId());
                }
                if (appIds.indexOf(tmAuthorize.getSystemId()) == -1) {
                    appIds.add(tmAuthorize.getSystemId());
                }
                /*
                 * if (targetFullId.indexOf(tmAuthorize.getSubordinationFullId()) == -1) {
                 * targetFullId += tmAuthorize.getSubordinationFullId() + ",";
                 * }
                 * if (targetAppId.indexOf(tmAuthorize.getSystemId()) == -1) {
                 * targetAppId += tmAuthorize.getSystemId() + ",";
                 * }
                 */
            }

            if (fullIds.size() > 0) {
                targetFullId = String.join(StringPool.COMMA, fullIds);// targetFullId.substring(0, targetFullId.length() - 1);
            }

            if (appIds.size() > 0) {
                targetAppId = String.join(StringPool.COMMA, appIds);// targetAppId.substring(0, targetAppId.length() - 1);
            }
            if (roleKindId.equals(RoleKind.ADMINISTRATOR.getId())) {
                targetStatusId = String.valueOf(LogStatus.FAILURE.getId());
                targetRoleKindId = StringPool.AT;
            } else if (roleKindId.equals(RoleKind.SECURITY_GUARD.getId())) {
                // targetRoleKindId = RoleKind.ADMINISTRATOR.getId();
                targetRoleKindId = RoleKind.COMMON.getId();
            } else if (roleKindId.equals(RoleKind.AUDITOR.getId())) {
                // targetRoleKindId = String.format("%s,%s,%s", RoleKind.ADMINISTRATOR.getId(), RoleKind.SECURITY_GUARD.getId(), RoleKind.COMMON.getId());
                targetRoleKindId = String.format("%s,%s,%s", RoleKind.ADMINISTRATOR.getId(), RoleKind.SECURITY_GUARD.getId(), RoleKind.AUDITOR.getId());
            }
        }

        // 调整规则：审计员查看三员日志，安全员查看除三员以外的普通用户的日志

        data.put("targetRoleKindId", targetRoleKindId);
        data.put("targetStatusId", targetStatusId);
        // data.put("targetLogType", targetLogType);
        data.put("targetAppId", targetAppId);
        data.put("targetFullId", targetFullId);

        return data;
    }
}
