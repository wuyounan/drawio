package com.huigou.uasp.bpm.engine.domain.query;

import java.util.Date;
import java.util.Map;

import com.huigou.data.domain.query.QueryAbstractRequest;
import com.huigou.util.ClassHelper;
import com.huigou.util.DateRange;

/**
 * 流程处理人查询
 * 
 * @author xx
 */
public class ProcunitHandlerQueryRequest extends QueryAbstractRequest {

    protected String administrativeOrgFullId;

    protected String procFullId;

    protected String executorPersonMemberName;

    protected Integer dateRange;

    protected Date startDate;

    protected Date endDate;

    protected String searchContent;

    protected String applicantPersonNemberName;

    protected Integer isNotTask;

    public String getAdministrativeOrgFullId() {
        return administrativeOrgFullId;
    }

    public void setAdministrativeOrgFullId(String administrativeOrgFullId) {
        this.administrativeOrgFullId = administrativeOrgFullId;
    }

    public String getProcFullId() {
        return procFullId;
    }

    public void setProcFullId(String procFullId) {
        this.procFullId = procFullId;
    }

    public String getExecutorPersonMemberName() {
        return executorPersonMemberName;
    }

    public void setExecutorPersonMemberName(String executorPersonMemberName) {
        this.executorPersonMemberName = executorPersonMemberName;
    }

    public Integer getDateRange() {
        return dateRange;
    }

    public void setDateRange(Integer dateRange) {
        this.dateRange = dateRange;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public String getApplicantPersonNemberName() {
        return applicantPersonNemberName;
    }

    public void setApplicantPersonNemberName(String applicantPersonNemberName) {
        this.applicantPersonNemberName = applicantPersonNemberName;
    }

    public Integer getIsNotTask() {
        if (isNotTask == null) {
            return null;
        }
        if (isNotTask == 0) {
            return null;
        }
        return isNotTask;
    }

    public void setIsNotTask(Integer isNotTask) {
        this.isNotTask = isNotTask;
    }

    // 日期查询条件解析
    public void setDateRangeData() {
        DateRange dateRange = DateRange.fromId(this.dateRange);
        switch (dateRange) {
        case CUSTOM:
            break;
        case ALL:
            this.setStartDate(null);
            this.setEndDate(null);
            break;
        default:
            Map<String, Object> dateBetween = DateRange.getDataRange(dateRange);
            this.setStartDate(ClassHelper.convert(dateBetween.get("startDate"), Date.class));
            this.setEndDate(ClassHelper.convert(dateBetween.get("endDate"), Date.class));
        }
    }

}
