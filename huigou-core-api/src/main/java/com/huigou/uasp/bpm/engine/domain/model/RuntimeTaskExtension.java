package com.huigou.uasp.bpm.engine.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.util.Assert;

import com.huigou.annotation.JsonIgnore;
import com.huigou.context.Operator;
import com.huigou.context.OrgUnit;
import com.huigou.uasp.bmp.common.BooleanKind;
import com.huigou.uasp.bmp.opm.OpmUtil;
import com.huigou.uasp.bmp.opm.domain.model.agent.Agent;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bpm.ProcessDefinitionUtil;
import com.huigou.uasp.bpm.TaskStatus;
import com.huigou.util.StringUtil;

/**
 * 运行时任务扩展
 * 
 * @author gongmm
 */
@Entity
@Table(name = "ACT_RU_Task_Extension")
public class RuntimeTaskExtension extends TaskExtension {

	private static final long serialVersionUID = -6824345073754612419L;

	/**
	 * 委托类型
	 */
	@Column(name = "delegation_")
	private String delegation;

	/**
	 * 是否挂起
	 */
	@Column(name = "suspension_state_")
	private Integer suspensionState;

	@Transient
	private Agent agent;

	@Transient
	private String initiatorPersonMemberId;

	@Transient
	private ProcUnitHandler procUnitHandler;

	@Transient
	private String approvalRuleHandlerId;

	public String getDelegation() {
		return delegation;
	}

	public void setDelegation(String delegation) {
		this.delegation = delegation;
	}

	public Integer getSuspensionState() {
		return suspensionState;
	}

	public void setSuspensionState(Integer suspensionState) {
		this.suspensionState = suspensionState;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public String getInitiatorPersonMemberId() {
		return initiatorPersonMemberId;
	}

	public void setInitiatorPersonMemberId(String initiatorPersonMemberId) {
		this.initiatorPersonMemberId = initiatorPersonMemberId;
	}

	public ProcUnitHandler getProcUnitHandler() {
		return procUnitHandler;
	}

	public void setProcUnitHandler(ProcUnitHandler procUnitHandler) {
		this.procUnitHandler = procUnitHandler;
	}

	public String getApprovalRuleHandlerId() {
		return approvalRuleHandlerId;
	}

	public void setApprovalRuleHandlerId(String approvalRuleHandlerId) {
		this.approvalRuleHandlerId = approvalRuleHandlerId;
	}

	public RuntimeTaskExtension() {
		this.setNeedTiming(BooleanKind.NO.getId());
		this.setLimitTime(0);
	}

	public RuntimeTaskExtension(String taskId, String catalogId, String taskKindId) {
		this();
		Assert.hasText(taskId, "参数taskId不能为空。");
		Assert.hasText(catalogId, "参数catalogId不能为空。");
		Assert.hasText(taskKindId, "参数taskKindId不能为空。");

		this.setId(taskId);
		this.setCatalogId(catalogId);
		this.setKindId(taskKindId);
	}

	/**
	 * 是否待办状态
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isToDoStatus() {
		return this.getStatus() == TaskStatus.READY || this.getStatus() == TaskStatus.SLEEPING || this.getStatus() == TaskStatus.EXECUTING;
	}

//	 public void fromDelegateTask(DelegateTask delegateTask, String taskScope, String kindId) {
//	     fromDelegateTask(delegateTask, taskScope, kindId, null);
//	 }
	
	public void fromDelegateTask(DelegateTask delegateTask, String taskScope, String kindId, String previousId) {
		this.setId(delegateTask.getId());

		this.setProcessDefinitionId(delegateTask.getProcessDefinitionId());
		if (StringUtil.isNotBlank(delegateTask.getProcessDefinitionId())) {
			String processDefinitionKey = ProcessDefinitionUtil.getProcessDefinitionKeyFromId(delegateTask.getProcessDefinitionId());
			this.setProcessDefinitionKey(processDefinitionKey);
		}
		this.setTaskDefinitionKey(delegateTask.getTaskDefinitionKey());
		this.setName(delegateTask.getName());

		if (delegateTask.getExecution() != null) {
			this.setProcessInstanceId(delegateTask.getProcessInstanceId());
			this.setExecutionId(delegateTask.getExecutionId());
			this.setBusinessKey(delegateTask.getExecution().getProcessBusinessKey());
		}

		this.setPreviousId(previousId);
		this.setCategory(delegateTask.getCategory());
		this.setCatalogId(taskScope);
		this.setKindId(kindId);

		this.setFormKey(delegateTask.getFormKey());
		this.setExecutorUrl(delegateTask.getFormKey());
		this.setDescription(delegateTask.getDescription());
		this.setPriority(delegateTask.getPriority());
		this.setStartTime(delegateTask.getCreateTime());
		this.setDueDate(delegateTask.getDueDate());

		this.setTenantId(delegateTask.getTenantId());
		this.setSuspensionState(((TaskEntity) delegateTask).getSuspensionState());
	}

	public void assignCreatorInfo(Org org) {
		Assert.notNull(org, "参数org不能为空。");

		this.setCreatorFullId(org.getFullId());
		this.setCreatorFullName(org.getFullName());

		this.setCreatorOgnId(org.getOrgId());
		this.setCreatorOgnName(org.getOrgName());

		this.setCreatorDeptId(org.getDeptId());
		this.setCreatorDeptName(org.getDeptName());

		this.setCreatorPosId(org.getPositionId());
		this.setCreatorPosName(org.getPositionName());

		this.setCreatorPersonMemberId(org.getId());
		this.setCreatorPersonMemberName(org.getName());

		this.setCreatorPersonId(OpmUtil.getPersonIdFromPersonMemberId(this.getCreatorPersonMemberId()));
	}

	public void assignCreatorInfo(Operator operator) {
		Assert.notNull(operator, "参数operator不能为空。");

		this.setCreatorFullId(operator.getFullId());
		this.setCreatorFullName(operator.getFullName());

		this.setCreatorOgnId(operator.getOrgId());
		this.setCreatorOgnName(operator.getOrgName());

		this.setCreatorDeptId(operator.getDeptId());
		this.setCreatorDeptName(operator.getDeptName());

		this.setCreatorPosId(operator.getPositionId());
		this.setCreatorPosName(operator.getPositionName());

		this.setCreatorPersonMemberId(operator.getPersonMemberId());
		this.setCreatorPersonMemberName(operator.getPersonMemberName());

		this.setCreatorPersonId(OpmUtil.getPersonIdFromPersonMemberId(this.getCreatorPersonMemberId()));
	}

	public void assignCreatorInfo(ProcUnitHandler procUnitHandler) {
		Assert.notNull(procUnitHandler, "参数procUnitHandler不能为空。");

		this.setCreatorFullId(procUnitHandler.getFullId());
		this.setCreatorFullName(procUnitHandler.getFullName());

		this.setCreatorOgnId(procUnitHandler.getOrgId());
		this.setCreatorOgnName(procUnitHandler.getOrgName());

		this.setCreatorDeptId(procUnitHandler.getDeptId());
		this.setCreatorDeptName(procUnitHandler.getDeptName());

		this.setCreatorPosId(procUnitHandler.getPositionId());
		this.setCreatorPosName(procUnitHandler.getPositionName());

		this.setCreatorPersonMemberId(procUnitHandler.getHandlerId());
		this.setCreatorPersonMemberName(procUnitHandler.getHandlerName());
	}

	private void setOwnerInfo(OrgUnit orgUnit) {
		this.setOwnerFullId(orgUnit.getFullId());
		this.setOwnerFullName(orgUnit.getFullName());

		this.setOwnerOgnId(orgUnit.getAttributeValue("orgId"));
		this.setOwnerOgnName(orgUnit.getAttributeValue("orgName"));

		this.setOwnerDeptId(orgUnit.getAttributeValue("deptId"));
		this.setOwnerDeptName(orgUnit.getAttributeValue("deptName"));

		this.setOwnerPosId(orgUnit.getAttributeValue("posId"));
		this.setOwnerPosName(orgUnit.getAttributeValue("posName"));

		this.setOwnerPersonMemberId(orgUnit.getAttributeValue("psmId"));
		this.setOwnerPersonMemberName(orgUnit.getAttributeValue("psmName"));
	}

	public void assignExecutor() {
		checkExecutorInfo();
		OrgUnit orgUnit = new OrgUnit(getExecutorFullId(), getExecutorFullName());

		setOwnerInfo(orgUnit);

		this.setExecutorOgnId(orgUnit.getAttributeValue("orgId"));
		this.setExecutorOgnName(orgUnit.getAttributeValue("orgName"));

		this.setExecutorDeptId(orgUnit.getAttributeValue("deptId"));
		this.setExecutorDeptName(orgUnit.getAttributeValue("deptName"));

		this.setExecutorPosId(orgUnit.getAttributeValue("posId"));
		this.setExecutorPosName(orgUnit.getAttributeValue("posName"));

		this.setExecutorPersonMemberId(orgUnit.getAttributeValue("psmId"));
		this.setExecutorPersonMemberName(orgUnit.getAttributeValue("psmName"));

		this.setExecutorPersonId(OpmUtil.getPersonIdFromPersonMemberId(this.getExecutorPersonMemberId()));
	}

	public void assignExecutor(Agent agent) {
		checkExecutorInfo();
		OrgUnit orgUnit = new OrgUnit(getExecutorFullId(), getExecutorFullName());
		setOwnerInfo(orgUnit);
		if (agent != null) {
			Org agentHandler = agent.getAgent();

			this.setExecutorFullId(agentHandler.getFullId());
			this.setExecutorFullName(agentHandler.getFullName());

			this.setExecutorOgnId(agentHandler.getOrgId());
			this.setExecutorOgnName(agentHandler.getOrgName());

			this.setExecutorDeptId(agentHandler.getDeptId());
			this.setExecutorDeptName(agentHandler.getDeptName());

			this.setExecutorPosId(agentHandler.getPositionId());
			this.setExecutorPosName(agentHandler.getPositionName());

			this.setExecutorPersonMemberId(agentHandler.getId());
			this.setExecutorPersonMemberName(agentHandler.getName());

			this.setExecutorPersonId(OpmUtil.getPersonIdFromPersonMemberId(this.getExecutorPersonMemberId()));
		} else {
			this.setExecutorFullId(orgUnit.getFullId());
			this.setExecutorFullName(orgUnit.getFullName());

			this.setExecutorOgnId(orgUnit.getAttributeValue("orgId"));
			this.setExecutorOgnName(orgUnit.getAttributeValue("orgName"));

			this.setExecutorDeptId(orgUnit.getAttributeValue("deptId"));
			this.setExecutorDeptName(orgUnit.getAttributeValue("deptName"));

			this.setExecutorPosId(orgUnit.getAttributeValue("posId"));
			this.setExecutorPosName(orgUnit.getAttributeValue("posName"));

			this.setExecutorPersonMemberId(orgUnit.getAttributeValue("psmId"));
			this.setExecutorPersonMemberName(orgUnit.getAttributeValue("psmName"));

			this.setExecutorPersonId(OpmUtil.getPersonIdFromPersonMemberId(this.getExecutorPersonMemberId()));
		}
	}

	public void checkExecutorInfo() {
		Assert.hasText(this.getExecutorFullId(), "执行人ID全路径不能为空。");
		Assert.hasText(this.getExecutorFullName(), "执行人名称全路径不能为空。");
	}
	
}
