package com.huigou.uasp.bpm.engine.domain.model;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.beanutils.BeanUtils;

import com.huigou.context.OrgUnit;
import com.huigou.domain.IdentifiedEntity;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bpm.TaskStatus;

@MappedSuperclass
public class TaskExtension implements IdentifiedEntity, Serializable {

    private static final long serialVersionUID = -36196054129956422L;

    @Id
    @Column(name = "id_")
    private String id;

    @Column(name = "catalog_id_")
    private String catalogId;

    @Column(name = "kind_id_")
    private String kindId;

    @Column(name = "creator_url_")
    private String creatorUrl;

    @Column(name = "executor_url_")
    private String executorUrl;

    @Column(name = "creator_person_member_id_")
    private String creatorPersonMemberId;

    @Column(name = "creator_person_member_name_")
    private String creatorPersonMemberName;

    @Column(name = "creator_pos_id_")
    private String creatorPosId;

    @Column(name = "creator_pos_name_")
    private String creatorPosName;

    @Column(name = "creator_dept_id_")
    private String creatorDeptId;

    @Column(name = "creator_dept_name_")
    private String creatorDeptName;

    @Column(name = "creator_ogn_id_")
    private String creatorOgnId;

    @Column(name = "creator_ogn_name_")
    private String creatorOgnName;

    @Column(name = "creator_full_id_")
    private String creatorFullId;

    @Column(name = "creator_full_name_")
    private String creatorFullName;

    @Column(name = "executor_person_member_id_")
    private String executorPersonMemberId;

    @Column(name = "executor_person_member_name_")
    private String executorPersonMemberName;

    @Column(name = "executor_pos_id_")
    private String executorPosId;

    @Column(name = "executor_pos_name_")
    private String executorPosName;

    @Column(name = "executor_dept_id_")
    private String executorDeptId;

    @Column(name = "executor_dept_name_")
    private String executorDeptName;

    @Column(name = "executor_ogn_id_")
    private String executorOgnId;

    @Column(name = "executor_ogn_name_")
    private String executorOgnName;

    @Column(name = "executor_full_id_")
    private String executorFullId;

    @Column(name = "executor_full_name_")
    private String executorFullName;

    @Column(name = "status_id_")
    private String statusId;

    @Column(name = "status_name_")
    private String statusName;

    @Column(name = "executor_names_")
    private String executorNames;

    @Column(name = "owner_person_member_id_")
    private String ownerPersonMemberId;

    @Column(name = "owner_person_member_name_")
    private String ownerPersonMemberName;

    @Column(name = "owner_pos_id_")
    private String ownerPosId;

    @Column(name = "owner_pos_name_")
    private String ownerPosName;

    @Column(name = "owner_dept_id_")
    private String ownerDeptId;

    @Column(name = "owner_dept_name_")
    private String ownerDeptName;

    @Column(name = "owner_ogn_id_")
    private String ownerOgnId;

    @Column(name = "owner_ogn_name_")
    private String ownerOgnName;

    @Column(name = "owner_full_id_")
    private String ownerFullId;

    @Column(name = "owner_full_name_")
    private String ownerFullName;

    @Column(name = "previous_id_")
    private String previousId;

    /**
     * 环节处理人ID
     */
    @Column(name = "proc_unit_handler_id_")
    private String procUnitHandlerId;

    @Column(name = "sub_proc_unit_name_")
    private String subProcUnitName;

    /**
     * 业务ID
     */
    @Column(name = "business_key_")
    private String businessKey;

    /**
     * 业务编码
     */
    @Column(name = "business_code_")
    private String businessCode;

    @Column(name = "cooperation_model_id_")
    private String cooperationModelId;

    @Column(name = "need_timing_")
    private Integer needTiming;

    @Column(name = "limit_time_")
    private Integer limitTime;

    @Column(name = "sleeped_date_")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sleepedDate;

    @Column(name = "result_")
    private Integer result;

    @Column(name = "opinion_")
    private String opinion;

    /**
     * 申请人人员成员ID
     */
    @Column(name = "applicant_person_member_id_")
    private String applicantPersonMemberId;

    /**
     * 申请人人员成员
     */
    @Column(name = "applicant_person_member_name_")
    private String applicantPersonMemberName;

    /**
     * 流程定义ID
     */
    @Column(name = "process_definition_key_")
    private String processDefinitionKey;

    /**
     * 创建人ID
     */
    @Column(name = "creator_person_id_")
    private String creatorPersonId;

    /**
     * 执行人
     */
    @Column(name = "executor_person_id_")
    private String executorPersonId;

    /**
     * 流程定义ID
     */
    @Column(name = "proc_def_id_")
    private String processDefinitionId;

    /**
     * 环节定义的KEY
     */
    @Column(name = "task_def_key_")
    private String taskDefinitionKey;

    /**
     * 流程实例ID
     */
    @Column(name = "proc_inst_id_")
    private String processInstanceId;

    /**
     * 执行实例ID
     */
    @Column(name = "execution_id_")
    private String executionId;

    /**
     * 父任务ID
     */
    @Column(name = "parent_task_id_")
    private String parentTaskId;

    /**
     * 环节名称
     */
    @Column(name = "name_")
    private String name;

    /**
     * 主题
     */
    @Column(name = "description_")
    private String description;

    /**
     * 优先级
     */
    @Column(name = "priority_")
    private Integer priority;

    /**
     * 创建时间
     */
    @Column(name = "start_time_")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    /**
     * 到期时间
     */
    @Column(name = "due_date_")
    private Date dueDate;

    @Column(name = "form_key_")
    private String formKey;

    @Column(name = "category_")
    private String category;

    @Column(name = "tenant_id_")
    private String tenantId;

    /**
     * 产生原因
     */
    @Column(name = "generate_reason_")
    private String generateReason;

    @Column(name = "version_")
    private Long version;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatalogId() {
        return this.catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getKindId() {
        return this.kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
    }

    public String getCreatorUrl() {
        return this.creatorUrl;
    }

    public void setCreatorUrl(String creatorUrl) {
        this.creatorUrl = creatorUrl;
    }

    public String getExecutorUrl() {
        return this.executorUrl;
    }

    public void setExecutorUrl(String executorUrl) {
        this.executorUrl = executorUrl;
    }

    public String getCreatorPersonMemberId() {
        return this.creatorPersonMemberId;
    }

    public void setCreatorPersonMemberId(String creatorPersonMemberId) {
        this.creatorPersonMemberId = creatorPersonMemberId;
    }

    public String getCreatorPersonMemberName() {
        return this.creatorPersonMemberName;
    }

    public void setCreatorPersonMemberName(String creatorPersonMemberName) {
        this.creatorPersonMemberName = creatorPersonMemberName;
    }

    public String getCreatorPosId() {
        return this.creatorPosId;
    }

    public void setCreatorPosId(String creatorPosId) {
        this.creatorPosId = creatorPosId;
    }

    public String getCreatorPosName() {
        return this.creatorPosName;
    }

    public void setCreatorPosName(String creatorPosName) {
        this.creatorPosName = creatorPosName;
    }

    public String getCreatorDeptId() {
        return this.creatorDeptId;
    }

    public void setCreatorDeptId(String creatorDeptId) {
        this.creatorDeptId = creatorDeptId;
    }

    public String getCreatorDeptName() {
        return this.creatorDeptName;
    }

    public void setCreatorDeptName(String creatorDeptName) {
        this.creatorDeptName = creatorDeptName;
    }

    public String getCreatorOgnId() {
        return this.creatorOgnId;
    }

    public void setCreatorOgnId(String creatorOgnId) {
        this.creatorOgnId = creatorOgnId;
    }

    public String getCreatorOgnName() {
        return this.creatorOgnName;
    }

    public void setCreatorOgnName(String creatorOgnName) {
        this.creatorOgnName = creatorOgnName;
    }

    public String getCreatorFullId() {
        return this.creatorFullId;
    }

    public void setCreatorFullId(String creatorFullId) {
        this.creatorFullId = creatorFullId;
    }

    public String getCreatorFullName() {
        return this.creatorFullName;
    }

    public void setCreatorFullName(String creatorFullName) {
        this.creatorFullName = creatorFullName;
    }

    public String getExecutorPersonMemberId() {
        return this.executorPersonMemberId;
    }

    public void setExecutorPersonMemberId(String executorPersonMemberId) {
        this.executorPersonMemberId = executorPersonMemberId;
    }

    public String getExecutorPersonMemberName() {
        return this.executorPersonMemberName;
    }

    public void setExecutorPersonMemberName(String executorPersonMemberName) {
        this.executorPersonMemberName = executorPersonMemberName;
    }

    public String getExecutorPosId() {
        return this.executorPosId;
    }

    public void setExecutorPosId(String executorPosId) {
        this.executorPosId = executorPosId;
    }

    public String getExecutorPosName() {
        return this.executorPosName;
    }

    public void setExecutorPosName(String executorPosName) {
        this.executorPosName = executorPosName;
    }

    public String getExecutorDeptId() {
        return this.executorDeptId;
    }

    public void setExecutorDeptId(String executorDeptId) {
        this.executorDeptId = executorDeptId;
    }

    public String getExecutorDeptName() {
        return this.executorDeptName;
    }

    public void setExecutorDeptName(String executorDeptName) {
        this.executorDeptName = executorDeptName;
    }

    public String getExecutorOgnId() {
        return this.executorOgnId;
    }

    public void setExecutorOgnId(String executorOgnId) {
        this.executorOgnId = executorOgnId;
    }

    public String getExecutorOgnName() {
        return this.executorOgnName;
    }

    public void setExecutorOgnName(String executorOgnName) {
        this.executorOgnName = executorOgnName;
    }

    public String getExecutorFullId() {
        return this.executorFullId;
    }

    public void setExecutorFullId(String executorFullId) {
        this.executorFullId = executorFullId;
    }

    public String getExecutorFullName() {
        return this.executorFullName;
    }

    public void setExecutorFullName(String executorFullName) {
        this.executorFullName = executorFullName;
    }

    public String getStatusId() {
        return this.statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getExecutorNames() {
        return this.executorNames;
    }

    public void setExecutorNames(String executorNames) {
        this.executorNames = executorNames;
    }

    public String getOwnerPersonMemberId() {
        return this.ownerPersonMemberId;
    }

    public void setOwnerPersonMemberId(String ownerPersonMemberId) {
        this.ownerPersonMemberId = ownerPersonMemberId;
    }

    public String getOwnerPersonMemberName() {
        return this.ownerPersonMemberName;
    }

    public void setOwnerPersonMemberName(String ownerPersonMemberName) {
        this.ownerPersonMemberName = ownerPersonMemberName;
    }

    public String getOwnerPosId() {
        return this.ownerPosId;
    }

    public void setOwnerPosId(String ownerPosId) {
        this.ownerPosId = ownerPosId;
    }

    public String getOwnerPosName() {
        return this.ownerPosName;
    }

    public void setOwnerPosName(String ownerPosName) {
        this.ownerPosName = ownerPosName;
    }

    public String getOwnerDeptId() {
        return this.ownerDeptId;
    }

    public void setOwnerDeptId(String ownerDeptId) {
        this.ownerDeptId = ownerDeptId;
    }

    public String getOwnerDeptName() {
        return this.ownerDeptName;
    }

    public void setOwnerDeptName(String ownerDeptName) {
        this.ownerDeptName = ownerDeptName;
    }

    public String getOwnerOgnId() {
        return this.ownerOgnId;
    }

    public void setOwnerOgnId(String ownerOgnId) {
        this.ownerOgnId = ownerOgnId;
    }

    public String getOwnerOgnName() {
        return this.ownerOgnName;
    }

    public void setOwnerOgnName(String ownerOgnName) {
        this.ownerOgnName = ownerOgnName;
    }

    public String getOwnerFullId() {
        return this.ownerFullId;
    }

    public void setOwnerFullId(String ownerFullId) {
        this.ownerFullId = ownerFullId;
    }

    public String getOwnerFullName() {
        return this.ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public String getPreviousId() {
        return this.previousId;
    }

    public void setPreviousId(String previousId) {
        this.previousId = previousId;
    }

    public String getProcUnitHandlerId() {
        return this.procUnitHandlerId;
    }

    public void setProcUnitHandlerId(String procUnitHandlerId) {
        this.procUnitHandlerId = procUnitHandlerId;
    }

    public String getSubProcUnitName() {
        return subProcUnitName;
    }

    public void setSubProcUnitName(String subProcUnitName) {
        this.subProcUnitName = subProcUnitName;
    }

    public String getBusinessKey() {
        return this.businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getBusinessCode() {
        return this.businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getCooperationModelId() {
        return cooperationModelId;
    }

    public void setCooperationModelId(String cooperationModelId) {
        this.cooperationModelId = cooperationModelId;
    }

    public Integer getNeedTiming() {
        return needTiming;
    }

    public void setNeedTiming(Integer needTiming) {
        this.needTiming = needTiming;
    }

    public Integer getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(Integer limitTime) {
        this.limitTime = limitTime;
    }

    public Date getSleepedDate() {
        return sleepedDate;
    }

    public void setSleepedDate(Date sleepedDate) {
        this.sleepedDate = sleepedDate;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
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

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getCreatorPersonId() {
        return creatorPersonId;
    }

    public void setCreatorPersonId(String creatorPersonId) {
        this.creatorPersonId = creatorPersonId;
    }

    public String getExecutorPersonId() {
        return executorPersonId;
    }

    public void setExecutorPersonId(String executorPersonId) {
        this.executorPersonId = executorPersonId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getGenerateReason() {
        return generateReason;
    }

    public boolean isSpecifiedGenerateReason(String generateReason) {
        if (this.generateReason == null) {
            return false;
        }
        return this.generateReason.equals(generateReason);
    }

    public void setGenerateReason(String generateReason) {
        this.generateReason = generateReason;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public void setVersion(Long version) {
        this.version = version;
    }

    public TaskStatus getStatus() {
        return TaskStatus.fromId(this.getStatusId());
    }

    public void checkStatus(TaskStatus checkStatus, String errorMessage) {
        if (getStatus() != checkStatus) {
            throw new ApplicationException(errorMessage);
        }
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

    public void setUpdateFields_(Collection<String> names) {
        // TODO Auto-generated method stub
    }

    public void setUpdateFields_(String... updateFields) {
        // TODO Auto-generated method stub
    }

    public void buildExecutorInfo(OrgUnit orgUnit) {
        this.setExecutorFullId(orgUnit.getFullId());
        this.setExecutorFullName(orgUnit.getFullName());
        this.setExecutorOgnId(orgUnit.getOrgId());
        this.setExecutorOgnName(orgUnit.getOrgName());
        this.setExecutorDeptId(orgUnit.getDeptId());
        this.setExecutorDeptName(orgUnit.getDeptName());
        this.setExecutorPosId(orgUnit.getPositionId());
        this.setExecutorPosName(orgUnit.getPositionName());
        this.setExecutorPersonMemberId(orgUnit.getPersonMemberId());
        this.setExecutorPersonMemberName(orgUnit.getPersonMemberName());
        this.setExecutorPersonId(orgUnit.getAttributeValue("personId"));
    }

}
