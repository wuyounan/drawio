package com.huigou.uasp.bpm.engine.domain.model;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.activiti.engine.impl.context.Context;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.Assert;

import com.huigou.context.Operator;
import com.huigou.domain.IdentifiedEntity;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bpm.ProcessStatus;

@Entity
@Table(name = "ACT_HI_ProcInst_Extension")
public class HistoricProcessInstanceExtension implements IdentifiedEntity, Serializable {

    private static final long serialVersionUID = 7540803005015298833L;

    @Id
    @Column(name = "id_")
    private String id;

    @Column(name = "proc_def_id_")
    private String ProcessDefinitionId;

    @Column(name = "key_")
    private String processDefinitionKey;

    /**
     * 流程实例ID
     */
    @Column(name = "proc_inst_id_")
    private String processInstanceId;

    /**
     * 业务ID
     */
    @Column(name = "business_key_")
    private String businessKey;

    @Column(name = "applicant_person_member_id_")
    private String applicantPersonMemberId;

    @Column(name = "applicant_person_member_name_")
    private String applicantPersonMemberName;

    @Column(name = "applicant_pos_id_")
    private String applicantPosId;

    @Column(name = "applicant_pos_name_")
    private String applicantPosName;

    @Column(name = "applicant_dept_id_")
    private String applicantDeptId;

    @Column(name = "applicant_dept_name_")
    private String applicantDeptName;

    @Column(name = "applicant_org_id_")
    private String applicantOrgId;

    @Column(name = "applicant_org_name_")
    private String applicantOrgName;

    @Column(name = "applicant_full_id_")
    private String applicantFullId;

    @Column(name = "applicant_full_name_")
    private String applicantFullName;

    @Column(name = "description_")
    private String description;

    @Column(name = "status_id_")
    private String statusId;

    @Column(name = "status_name_")
    private String statusName;

    @Column(name = "start_time_")
    private Date startTime;

    @Column(name = "end_time_")
    private Date endTime;

    @Column(name = "duration_")
    private Long duration;

    @Column(name = "start_user_id_")
    private String startUserId;

    @Column(name = "start_act_id_")
    private String startActivityId;

    @Column(name = "end_act_id_")
    private String endActivityId;

    @Column(name = "super_process_instance_id_")
    private String superProcessInstanceId;

    @Column(name = "delete_reason_")
    private String deleteReason;

    @Column(name = "tenant_id_")
    private String tenantId;

    @Column(name = "name_")
    private String name;

    @Column(name = "version_")
    private Long version;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionId() {
        return ProcessDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.ProcessDefinitionId = processDefinitionId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getApplicantPersonMemberId() {
        return applicantPersonMemberId;
    }

    public void setApplicantPersonMemberId(String applicantPersonMemberId) {
        this.applicantPersonMemberId = applicantPersonMemberId;
    }

    public String getApplicantPersonMemberName() {
        return applicantPersonMemberName;
    }

    public void setApplicantPersonMemberName(String applicantPersonMemberName) {
        this.applicantPersonMemberName = applicantPersonMemberName;
    }

    public String getApplicantPosId() {
        return applicantPosId;
    }

    public void setApplicantPosId(String applicantPosId) {
        this.applicantPosId = applicantPosId;
    }

    public String getApplicantPosName() {
        return applicantPosName;
    }

    public void setApplicantPosName(String applicantPosName) {
        this.applicantPosName = applicantPosName;
    }

    public String getApplicantDeptId() {
        return applicantDeptId;
    }

    public void setApplicantDeptId(String applicantDeptId) {
        this.applicantDeptId = applicantDeptId;
    }

    public String getApplicantDeptName() {
        return applicantDeptName;
    }

    public void setApplicantDeptName(String applicantDeptName) {
        this.applicantDeptName = applicantDeptName;
    }

    public String getApplicantOrgId() {
        return applicantOrgId;
    }

    public void setApplicantOrgId(String applicantOrgId) {
        this.applicantOrgId = applicantOrgId;
    }

    public String getApplicantOrgName() {
        return applicantOrgName;
    }

    public void setApplicantOrgName(String applicantOrgName) {
        this.applicantOrgName = applicantOrgName;
    }

    public String getApplicantFullId() {
        return applicantFullId;
    }

    public void setApplicantFullId(String applicantFullId) {
        this.applicantFullId = applicantFullId;
    }

    public String getApplicantFullName() {
        return applicantFullName;
    }

    public void setApplicantFullName(String applicantFullName) {
        this.applicantFullName = applicantFullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public String getStartActivityId() {
        return startActivityId;
    }

    public void setStartActivityId(String startActivityId) {
        this.startActivityId = startActivityId;
    }

    public String getEndActivityId() {
        return endActivityId;
    }

    public void setEndActivityId(String endActivityId) {
        this.endActivityId = endActivityId;
    }

    public String getSuperProcessInstanceId() {
        return superProcessInstanceId;
    }

    public void setSuperProcessInstanceId(String superProcessInstanceId) {
        this.superProcessInstanceId = superProcessInstanceId;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public void setVersion(Long version) {
        this.version = version;
    }

    public void markEnded(String deleteReason) {
        this.deleteReason = deleteReason;
        this.endTime = Context.getProcessEngineConfiguration().getClock().getCurrentTime();
        this.duration = endTime.getTime() - startTime.getTime();
    }

    public void updateStatus(ProcessStatus processStatus) {
        this.setStatusId(processStatus.getId());
        this.setStatusName(processStatus.getDisplayName());
    }

    public void assignApplicant(Org personMember) {
        Assert.notNull(personMember, "参数personMember不能为空。");
        this.applicantPersonMemberId = personMember.getId();
        this.applicantPersonMemberName = personMember.getName();
        this.applicantPosId = personMember.getPositionId();
        this.applicantPosName = personMember.getPositionName();
        this.applicantDeptId = personMember.getDeptId();
        this.applicantDeptName = personMember.getDeptName();
        this.applicantOrgId = personMember.getOrgId();
        this.applicantOrgName = personMember.getOrgName();
        this.applicantFullId = personMember.getFullId();
        this.applicantFullName = personMember.getFullName();
    }

    public void assignApplicant(Operator operator) {
        Assert.notNull(operator, "参数operator不能为空。");
        this.applicantPersonMemberId = operator.getPersonMemberId();
        this.applicantPersonMemberName = operator.getName();
        this.applicantPosId = operator.getPositionId();
        this.applicantPosName = operator.getPositionName();
        this.applicantDeptId = operator.getDeptId();
        this.applicantDeptName = operator.getDeptName();
        this.applicantOrgId = operator.getOrgId();
        this.applicantOrgName = operator.getOrgName();
        this.applicantFullId = operator.getFullId();
        this.applicantFullName = operator.getFullName();
    }

    @Override
    public boolean isNew() {
        return false;
    }

    public void fromMap(Map<String, Object> params) {
        try {
            BeanUtils.populate(this, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpdateFields_(Collection<String> names) {
        // TODO Auto-generated method stub

    }

//    @Override
//    public void setUpdateFields_(String... updateFields) {
//        // TODO Auto-generated method stub
//
//    }
    
    @Override
    public String toString() {
        return "HistoricProcessInstanceExtension [id=" + id + ", ProcessDefinitionId=" + ProcessDefinitionId + ", processDefinitionKey=" + processDefinitionKey
               + ", processInstanceId=" + processInstanceId + ", businessKey=" + businessKey + ", applicantPersonMemberId=" + applicantPersonMemberId
               + ", applicantPersonMemberName=" + applicantPersonMemberName + ", applicantPosId=" + applicantPosId + ", applicantPosName=" + applicantPosName
               + ", applicantDeptId=" + applicantDeptId + ", applicantDeptName=" + applicantDeptName + ", applicantOrgId=" + applicantOrgId
               + ", applicantOrgName=" + applicantOrgName + ", applicantFullId=" + applicantFullId + ", applicantFullName=" + applicantFullName
               + ", description=" + description + ", statusId=" + statusId + ", statusName=" + statusName + ", startTime=" + startTime + ", endTime=" + endTime
               + ", duration=" + duration + ", startUserId=" + startUserId + ", startActivityId=" + startActivityId + ", endActivityId=" + endActivityId
               + ", superProcessInstanceId=" + superProcessInstanceId + ", deleteReason=" + deleteReason + ", tenantId=" + tenantId + ", name=" + name
               + ", version=" + version + "]";
    }
}
