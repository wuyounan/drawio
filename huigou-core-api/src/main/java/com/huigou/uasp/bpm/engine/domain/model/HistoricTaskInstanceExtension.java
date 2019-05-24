package com.huigou.uasp.bpm.engine.domain.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.activiti.engine.impl.context.Context;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.Assert;

@Entity
@Table(name = "ACT_HI_Taskinst_Extension")
public class HistoricTaskInstanceExtension extends TaskExtension {

    private static final long serialVersionUID = -2771781772368632751L;

    @Column(name = "claim_time_")
    @Temporal(TemporalType.TIMESTAMP)
    private Date claimTime;

    @Column(name = "end_time_")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "duration_")
    private Long duration;

    @Column(name = "delete_reason_")
    private String deleteReason;

    public Date getClaimTime() {
        return claimTime;
    }

    public void setClaimTime(Date claimTime) {
        this.claimTime = claimTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public HistoricTaskInstanceExtension() {

    }

    public HistoricTaskInstanceExtension(RuntimeTaskExtension runtimeTaskExtension) {
        Assert.notNull(runtimeTaskExtension, "参数runtimeTaskExtension不能为空。");

        try {
            BeanUtils.copyProperties(this, runtimeTaskExtension);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void markEnded(String deleteReason) {
        this.deleteReason = deleteReason;
        if (Context.getProcessEngineConfiguration() == null) {
            this.endTime = new Date();
        } else {
            this.endTime = Context.getProcessEngineConfiguration().getClock().getCurrentTime();
        }
		
		if (this.getStartTime() != null) {
            this.duration = endTime.getTime() - this.getStartTime().getTime();
        }
    }

    @Override
    public String toString() {
        return "HistoricTaskInstanceExtension [claimTime=" + claimTime + ", endTime=" + endTime + ", duration=" + duration + ", deleteReason=" + deleteReason
               + ", getId()=" + getId() + ", getCatalogId()=" + getCatalogId() + ", getKindId()=" + getKindId() + ", getCreatorUrl()=" + getCreatorUrl()
               + ", getExecutorUrl()=" + getExecutorUrl() + ", getCreatorPersonMemberId()=" + getCreatorPersonMemberId() + ", getCreatorPersonMemberName()="
               + getCreatorPersonMemberName() + ", getCreatorPosId()=" + getCreatorPosId() + ", getCreatorPosName()=" + getCreatorPosName()
               + ", getCreatorDeptId()=" + getCreatorDeptId() + ", getCreatorDeptName()=" + getCreatorDeptName() + ", getCreatorOgnId()=" + getCreatorOgnId()
               + ", getCreatorOgnName()=" + getCreatorOgnName() + ", getCreatorFullId()=" + getCreatorFullId() + ", getCreatorFullName()="
               + getCreatorFullName() + ", getExecutorPersonMemberId()=" + getExecutorPersonMemberId() + ", getExecutorPersonMemberName()="
               + getExecutorPersonMemberName() + ", getExecutorPosId()=" + getExecutorPosId() + ", getExecutorPosName()=" + getExecutorPosName()
               + ", getExecutorDeptId()=" + getExecutorDeptId() + ", getExecutorDeptName()=" + getExecutorDeptName() + ", getExecutorOgnId()="
               + getExecutorOgnId() + ", getExecutorOgnName()=" + getExecutorOgnName() + ", getExecutorFullId()=" + getExecutorFullId()
               + ", getExecutorFullName()=" + getExecutorFullName() + ", getStatusId()=" + getStatusId() + ", getStatusName()=" + getStatusName()
               + ", getExecutorNames()=" + getExecutorNames() + ", getOwnerPersonMemberId()=" + getOwnerPersonMemberId() + ", getOwnerPersonMemberName()="
               + getOwnerPersonMemberName() + ", getOwnerPosId()=" + getOwnerPosId() + ", getOwnerPosName()=" + getOwnerPosName() + ", getOwnerDeptId()="
               + getOwnerDeptId() + ", getOwnerDeptName()=" + getOwnerDeptName() + ", getOwnerOgnId()=" + getOwnerOgnId() + ", getOwnerOgnName()="
               + getOwnerOgnName() + ", getOwnerFullId()=" + getOwnerFullId() + ", getOwnerFullName()=" + getOwnerFullName() + ", getPreviousId()="
               + getPreviousId() + ", getProcUnitHandlerId()=" + getProcUnitHandlerId() + ", getSubProcUnitName()=" + getSubProcUnitName()
               + ", getBusinessKey()=" + getBusinessKey() + ", getBusinessCode()=" + getBusinessCode() + ", getCooperationModelId()=" + getCooperationModelId()
               + ", getNeedTiming()=" + getNeedTiming() + ", getLimitTime()=" + getLimitTime() + ", getSleepedDate()=" + getSleepedDate() + ", getResult()="
               + getResult() + ", getOpinion()=" + getOpinion() + ", getApplicantPersonMemberId()=" + getApplicantPersonMemberId()
               + ", getApplicantPersonMemberName()=" + getApplicantPersonMemberName() + ", getProcessDefinitionKey()=" + getProcessDefinitionKey()
               + ", getCreatorPersonId()=" + getCreatorPersonId() + ", getExecutorPersonId()=" + getExecutorPersonId() + ", getProcessDefinitionId()="
               + getProcessDefinitionId() + ", getTaskDefinitionKey()=" + getTaskDefinitionKey() + ", getProcessInstanceId()=" + getProcessInstanceId()
               + ", getExecutionId()=" + getExecutionId() + ", getParentTaskId()=" + getParentTaskId() + ", getName()=" + getName() + ", getDescription()="
               + getDescription() + ", getPriority()=" + getPriority() + ", getStartTime()=" + getStartTime() + ", getDueDate()=" + getDueDate()
               + ", getFormKey()=" + getFormKey() + ", getCategory()=" + getCategory() + ", getTenantId()=" + getTenantId() + ", getGenerateReason()="
               + getGenerateReason() + ", getVersion()=" + getVersion() + ", getStatus()=" + getStatus() + ", isNew()=" + isNew() + ", getClass()="
               + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }
}
