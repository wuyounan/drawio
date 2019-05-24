package com.huigou.uasp.bpm.engine.domain.query;

import java.sql.Date;

/**
 * 任务明细
 * 
 * @author gongmm
 */
public class TaskDetail {
    private String id;

    private String kindId;

    private String statusId;

    private String processDefinitionKey;

    private String taskDefKey;

    private String bizId;

    private String bizCode;

    private String procInstId;

    private String procUnitHandlerId;

    private Date procInstEndTime;

    private String procInstStatusId;

    private String executorPersonId;

    private String applicantPersonMemberId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKindId() {
        return kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcUnitHandlerId() {
        return procUnitHandlerId;
    }

    public void setProcUnitHandlerId(String procUnitHandlerId) {
        this.procUnitHandlerId = procUnitHandlerId;
    }

    public Date getProcInstEndTime() {
        return procInstEndTime;
    }

    public void setProcInstEndTime(Date procInstEndTime) {
        this.procInstEndTime = procInstEndTime;
    }

    public String getProcInstStatusId() {
        return procInstStatusId;
    }

    public void setProcInstStatusId(String procInstStatusId) {
        this.procInstStatusId = procInstStatusId;
    }

    public String getExecutorPersonId() {
        return executorPersonId;
    }

    public void setExecutorPersonId(String executorPersonId) {
        this.executorPersonId = executorPersonId;
    }

    public String getApplicantPersonMemberId() {
        return applicantPersonMemberId;
    }

    public void setApplicantPersonMemberId(String applicantPersonMemberId) {
        this.applicantPersonMemberId = applicantPersonMemberId;
    }

    @Override
    public String toString() {
        return "TaskDetail [id=" + id + ", kindId=" + kindId + ", statusId=" + statusId + ", processDefinitionKey=" + processDefinitionKey + ", taskDefKey="
               + taskDefKey + ", bizId=" + bizId + ", bizCode=" + bizCode + ", procInstId=" + procInstId + ", procUnitHandlerId=" + procUnitHandlerId
               + ", executorPersonId=" + executorPersonId + ", applicantPersonMemberId=" + applicantPersonMemberId + ", procInstEndTime=" + procInstEndTime
               + "]";
    }

}
