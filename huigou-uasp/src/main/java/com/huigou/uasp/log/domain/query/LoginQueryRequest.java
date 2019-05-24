package com.huigou.uasp.log.domain.query;

import java.util.Date;

import com.huigou.data.domain.query.QueryAbstractRequest;

public class LoginQueryRequest extends QueryAbstractRequest {

    public enum QueryKind {
        INVALID_LOGIN_NAME, NORMAL
    }

    private String operatorRoleKindId;

    private String personMemberId;

    private String fullId;

    private String loginName;

    private String personName;

    private Date beginDate;

    private Date endDate;

    private String ip;

    private QueryKind queryKind;

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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public QueryKind getQueryKind() {
        return queryKind;
    }

    public void setQueryKind(QueryKind queryKind) {
        this.queryKind = queryKind;
    }

    public boolean isQueryInvalidLoginName() {
        return QueryKind.INVALID_LOGIN_NAME.equals(queryKind);
    }

}
