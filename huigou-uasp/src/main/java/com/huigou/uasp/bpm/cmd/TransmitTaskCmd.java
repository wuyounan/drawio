package com.huigou.uasp.bpm.cmd;

import java.util.List;

import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.OrgUnit;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.exception.ApplicationException;
import com.huigou.express.ExpressManager;
import com.huigou.uasp.bpm.ActivityKind;
import com.huigou.uasp.bpm.ProcessAction;
import com.huigou.uasp.bpm.TaskKind;
import com.huigou.uasp.bpm.TaskScope;
import com.huigou.uasp.bpm.TaskStatus;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.application.ProcUnitHandlerApplication;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceExtension;
import com.huigou.uasp.bpm.engine.domain.model.RuntimeTaskExtension;
import com.huigou.uasp.bpm.event.ProcessEventContext;
import com.huigou.util.Constants;
import com.huigou.util.SDO;

/**
 * 交办任务
 * <p>
 * 把当前任务（taskId）交办给谁（executorId）
 * <p>
 * procUnitHandlerID
 * <p>
 * 环节处理人id（更新procUnitHandler的处理人信息 oldExecutor to newExecutor）
 */
public class TransmitTaskCmd extends FlowControlCmdBase {

    private String procUnitHandlerId;

    private String taskId;

    private String executorId;

    private String catalogId;

    private Integer sendMessage;

    private CommandContext commandContext;

    private ProcUnitHandlerApplication procUnitHandlerService;

    public void setProcUnitHandlerId(String procUnitHandlerId) {
        this.procUnitHandlerId = procUnitHandlerId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public void setTaskScope(String catalogId) {
        this.catalogId = catalogId;
    }

    public void setSendMessage(Integer sendMessage) {
        this.sendMessage = sendMessage;
    }

    public Integer getSendMessage() {
        return sendMessage;
    }

    public void setProcUnitHandlerService(ProcUnitHandlerApplication procUnitHandlerService) {
        this.procUnitHandlerService = procUnitHandlerService;
    }

    public TransmitTaskCmd(String taskId, ActApplication actApplication) {
        super(taskId, actApplication);
    }

    /**
     * 返回当前节点对象
     * 
     * @return
     */
    protected ActivityImpl getActivity(String processInstanceId, String activityId) {
        return commandContext.getExecutionEntityManager().findExecutionById(processInstanceId).getProcessDefinition().findActivity(activityId);
    }

    /**
     * 判断任务是否存在
     * 判断任务的状态
     * 交办人员判断
     * 更改处理人，给交办人生成新的任务，完成当前任务
     */
    @SuppressWarnings("unchecked")
    public Integer execute(CommandContext commandContext) {
        boolean isProcessTask = catalogId.equals(TaskScope.PROCESS);

        TaskEntity task = commandContext.getTaskEntityManager().findTaskById(taskId);

        Assert.notNull(task, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "任务"));

        this.commandContext = commandContext;

        List<OrgUnit> orgUnits = null;
        try {
            orgUnits = (List<OrgUnit>) ExpressManager.evaluate("findPersonMembersInOrg('" + executorId + "', true)");
        } catch (Exception e) {
            throw new ApplicationException("没有找到处理人。");
        }

        Assert.isTrue(orgUnits.size() == 1, "交办只能选择一个处理人。");

        this.commandContext = commandContext;
        OrgUnit orgUnit = orgUnits.get(0);

        SDO localSdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);

        if (isProcessTask) {// 流程任务
            // 申请环节
            Assert.state(!ActivityKind.isApplyActivity(task.getTaskDefinitionKey()), "申请环节不能交办。");

            ProcessInstance pi = findProcessInstanceByTaskId(taskId);

            String bizId = pi.getBusinessKey();
            boolean handlerExists = this.procUnitHandlerService.isPsmInHandlers(bizId, task.getTaskDefinitionKey(), executorId);
            if (handlerExists) {
                throw new ApplicationException("交办人员已在审批人员列表中，不能交办。");
            }

            localSdo.putProperty("sourceTaskDescription", task.getDescription());

            this.procUnitHandlerService.updateProcUnitHandlerOrgData(procUnitHandlerId, orgUnit);

            ExecutionEntity parentExecutionEntity = commandContext.getExecutionEntityManager().findExecutionById(pi.getId())
                                                                  .findExecution(task.getTaskDefinitionKey());

            ActivityImpl activity = getActivity(pi.getId(), task.getTaskDefinitionKey());

            ExecutionEntity execution = parentExecutionEntity.createExecution();

            if (activity.getProperty("type").equals("subProcess")) {
                ExecutionEntity extraScopedExecution = execution.createExecution();
                extraScopedExecution.setActive(true);
                extraScopedExecution.setConcurrent(false);
                extraScopedExecution.setScope(true);
                execution = extraScopedExecution;
            }

            parentExecutionEntity.setVariableLocal("nrOfInstances", (Integer) parentExecutionEntity.getVariableLocal("nrOfInstances") + 1);
            parentExecutionEntity.setVariableLocal("nrOfActiveInstances", (Integer) parentExecutionEntity.getVariableLocal("nrOfActiveInstances") + 1);

            execution.setVariableLocal("loopCounter", parentExecutionEntity.getExecutions().size() + 1);
            execution.setVariableLocal("assignee", procUnitHandlerId);

            execution.setActive(true);
            execution.setConcurrent(true);
            execution.setScope(false);

            execution.executeActivity(activity);

            getTaskService().complete(taskId, null);
        } else {// 协同任务
            try {
                HistoricTaskInstanceExtension historicTaskInstanceExtension = this.actApplication.loadHistoricTaskInstanceExtension(taskId);
                Assert.state(!historicTaskInstanceExtension.isSpecifiedGenerateReason(ProcessAction.REPLENISH), "打回任务不能交办。");

                task.setTaskDefinitionKeyWithoutCascade(null);
                task.complete(null, false);

                actApplication.deleteRuntimeTaskExtension(taskId);
                actApplication.updateHistoricTaskInstanceExtensionStatus(taskId, TaskStatus.COMPLETED);

                // 完全自由流的支持
                // if (StringUtil.isNotBlank(procUnitHandlerId)) {
                // this.procUnitHandlerService.updateProcUnitHandlerOrgData(procUnitHandlerId, orgNodeData);
                // }

                TaskEntity newTask = (TaskEntity) getTaskService().newTask();

                newTask.setNameWithoutCascade(task.getName());
                newTask.setDescriptionWithoutCascade(localSdo.getOperator().getPersonMemberName() + "交办给您的任务：" + task.getDescription());
                newTask.setPriorityWithoutCascade(50);
                newTask.setOwnerWithoutCascade(orgUnit.getFullId());
                newTask.setAssigneeWithoutCascade(orgUnit.getFullId());

                getTaskService().saveTask(newTask);

                RuntimeTaskExtension runtimeTaskExtension = new RuntimeTaskExtension();
                runtimeTaskExtension.fromDelegateTask(newTask, TaskScope.TASK, TaskKind.TASK, taskId);

                runtimeTaskExtension.setExecutorUrl(historicTaskInstanceExtension.getExecutorUrl());
                runtimeTaskExtension.setBusinessKey(historicTaskInstanceExtension.getBusinessKey());
                runtimeTaskExtension.setBusinessCode(historicTaskInstanceExtension.getBusinessCode());
                runtimeTaskExtension.setProcUnitHandlerId(procUnitHandlerId);

                runtimeTaskExtension.setExecutorFullId(orgUnit.getFullId());
                runtimeTaskExtension.setExecutorFullName(orgUnit.getFullName());

                actApplication.saveTaskExtension(runtimeTaskExtension);

                ProcessEventContext.addCompletedTask(historicTaskInstanceExtension.getId());
                ProcessEventContext.addNewNotProcessTask(newTask.getId());

            } catch (Exception e) {
                throw new ApplicationException(e.getMessage(), e);
            }
        }
        return 0;
    }
}
