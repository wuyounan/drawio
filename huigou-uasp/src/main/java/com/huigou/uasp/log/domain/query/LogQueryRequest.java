package com.huigou.uasp.log.domain.query;

import java.util.Date;

import com.huigou.data.domain.query.QueryAbstractRequest;

public class LogQueryRequest extends QueryAbstractRequest {
    private String operatorRoleKindId;

    private String personMemberId;

    private String fullId;

    private String personName;

    private Date beginDate;

    private Date endDate;

    private String ip;

    public String getOperatorRoleKindId() {
        return operatorRoleKindId;
    }

    public void setOperatorRoleKindId(String operatorRoleKindId) {
        this.operatorRoleKindId = operatorRoleKindId;
    }

    public String getPersonMemberId() {
        return personMemberId;
    }

    public void setPersonMemberId(String personMemberId) {
        this.personMemberId = personMemberId;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
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

}
