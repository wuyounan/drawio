package com.huigou.data.domain.query;

import java.util.Date;

import com.huigou.util.DateUtil;

/**
 * 流程单据抽象查询请求
 * 
 * @author xx
 */

public class FlowBillSuperQueryRequest extends QueryAbstractRequest {

    private Date fillinBeginDate;

    private Date fillinEndDate;

    private String billCode;

    private Integer status;

    private String fullId;

    private String organId;

    private String organName;

    private String deptId;

    private String deptName;

    private String positionId;

    private String positionName;

    private String personId;

    private String personMemberId;

    private String personMemberName;

    public Date getFillinBeginDate() {
        return DateUtil.getDateTimeBegin(fillinBeginDate);
    }

    public void setFillinBeginDate(Date fillinBeginDate) {
        this.fillinBeginDate = fillinBeginDate;
    }

    public Date getFillinEndDate() {
        return DateUtil.getDateTimeEnd(fillinEndDate);
    }

    public void setFillinEndDate(Date fillinEndDate) {
        this.fillinEndDate = fillinEndDate;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
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

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
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

}
