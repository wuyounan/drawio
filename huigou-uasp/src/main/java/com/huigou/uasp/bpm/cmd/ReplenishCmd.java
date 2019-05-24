package com.huigou.uasp.bpm.cmd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.springframework.util.Assert;

import com.huigou.context.ThreadLocalUtil;
import com.huigou.uasp.bpm.CooperationModelKind;
import com.huigou.uasp.bpm.MessageSendModel;
import com.huigou.uasp.bpm.MessageSenderManager;
import com.huigou.uasp.bpm.ProcessAction;
import com.huigou.uasp.bpm.TaskKind;
import com.huigou.uasp.bpm.TaskScope;
import com.huigou.uasp.bpm.TaskStatus;
import com.huigou.uasp.bpm.configuration.domain.model.TaskExecuteMode;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.application.ProcUnitHandlerApplication;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceExtension;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandler;
import com.huigou.uasp.bpm.engine.domain.model.RuntimeTaskExtension;
import com.huigou.uasp.bpm.event.ProcessEventContext;
import com.huigou.util.Constants;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;
import com.huigou.util.Util;

/**
 * 打回命令
 * <p>
 * 打回的作用是补充、完善信息，修改错误数据，加减处理人员。
 * 
 * @author gongmm
 */
public class ReplenishCmd implements Command<Integer> {

    /**
     * 当前任务ID
     */
    private String currentTaskId;

    /**
     * 打回任务ID
     */
    private String backToTaskId;

    /**
     * 流程环节处理人ID
     */
    private String procUnitHandlerId;

    private ActApplication actApplication;

    private MessageSenderManager messageSenderManager;

    private ProcUnitHandlerApplication procUnitHandlerService;

    public ReplenishCmd(String backToTaskId, String currentTaskId, String procUnitHandlerId, ActApplication actApplication,
                        ProcUnitHandlerApplication procUnitHandlerService, MessageSenderManager messageSenderManager) {
        this.backToTaskId = backToTaskId;
        this.currentTaskId = currentTaskId;
        this.procUnitHandlerId = procUnitHandlerId;
        this.actApplication = actApplication;
        this.procUnitHandlerService = procUnitHandlerService;
        this.messageSenderManager = messageSenderManager;
    }

    private TaskService getTaskService() {
        return Context.getProcessEngineConfiguration().getTaskService();
    }

    /**
     * 创建打回任务
     */
    private RuntimeTaskExtension createReplenishTask(HistoricTaskInstanceEntity historicTaskInstanceEntity) {
        HistoricTaskInstanceExtension hiTaskInstExtension = actApplication.loadHistoricTaskInstanceExtension(backToTaskId);

        TaskEntity task = (TaskEntity) getTaskService().newTask();

        task.setNameWithoutCascade(historicTaskInstanceEntity.getName());
        task.setTaskDefinitionKeyWithoutCascade(historicTaskInstanceEntity.getTaskDefinitionKey());
        task.setDescriptionWithoutCascade("打回：" + historicTaskInstanceEntity.getDescription());
        task.setPriorityWithoutCascade(50);
        task.setProcessInstanceId(historicTaskInstanceEntity.getProcessInstanceId());
        task.setFormKeyWithoutCascade(historicTaskInstanceEntity.getFormKey());

        getTaskService().saveTask(task);

        HistoricTaskInstanceEntity historicTaskInstance = Context.getCommandContext().getDbSqlSession()
                                                                 .findInCache(HistoricTaskInstanceEntity.class, task.getId());
        historicTaskInstance.setProcessInstanceId(task.getProcessInstanceId());

        RuntimeTaskExtension runtimeTaskExtension = new RuntimeTaskExtension();

        runtimeTaskExtension.fromDelegateTask((DelegateTask) task, TaskScope.TASK, TaskKind.REPLENISH, hiTaskInstExtension.getId());
        runtimeTaskExtension.setGenerateReason(ProcessAction.REPLENISH);

        runtimeTaskExtension.setProcessDefinitionId(historicTaskInstanceEntity.getProcessDefinitionId());
        runtimeTaskExtension.setProcessInstanceId(historicTaskInstanceEntity.getProcessInstanceId());

        runtimeTaskExtension.setBusinessKey(hiTaskInstExtension.getBusinessKey());
        runtimeTaskExtension.setBusinessCode(hiTaskInstExtension.getBusinessCode());
        runtimeTaskExtension.setProcUnitHandlerId(hiTaskInstExtension.getProcUnitHandlerId());
        runtimeTaskExtension.setNeedTiming(1);

        runtimeTaskExtension.setExecutorFullId(hiTaskInstExtension.getExecutorFullId());
        runtimeTaskExtension.setExecutorFullName(hiTaskInstExtension.getExecutorFullName());
        
        return runtimeTaskExtension;
    }

    @Override
    public Integer execute(CommandContext commandContext) {
        Assert.hasText(backToTaskId, "打回任务ID不能为空。");
        Assert.hasText(currentTaskId, "当前任务ID不能为空。");

        HistoricTaskInstanceEntity hiTaskInst = commandContext.getHistoricTaskInstanceEntityManager().findHistoricTaskInstanceById(backToTaskId);
        Util.check(hiTaskInst != null, String.format("未找到任务ID“%s”对应的历史任务。", backToTaskId));

        TaskEntity task = commandContext.getTaskEntityManager().findTaskById(currentTaskId);
        Util.check(task != null, String.format(String.format("未找到任务ID“%s”对应的任务。", currentTaskId)));

        /**
         * 0、生成打回任务
         * 1、复制当前处理人信息
         * 2、生成新任务
         * 3、完成当前任务
         * 更新新任务为等待状态
         */
        RuntimeTaskExtension runtimeTaskExtension = createReplenishTask(hiTaskInst);
        
        HistoricTaskInstanceExtension hiTaskInstExtension = actApplication.loadHistoricTaskInstanceExtension(currentTaskId);
        if (StringUtil.isNotBlank(procUnitHandlerId) && !ProcessAction.REPLENISH.equals(hiTaskInstExtension.getGenerateReason())) {
            // 1 给本环节生成新任务
            String newProcUnitHandlerId = procUnitHandlerService.copyProcUnitHandler(procUnitHandlerId);
            // 抢占模式打回，完成其他处理人任务
            ProcUnitHandler newProcUnitHandler = procUnitHandlerService.loadProcUnitHandler(newProcUnitHandlerId);
            // newProcUnitHandler.setStatus(0);

            boolean isPreemptModel = TaskExecuteMode.PREEMPT.equals(newProcUnitHandler.getTaskExecuteMode());
            List<Task> preemptTasks = null;
            if (isPreemptModel) {
                preemptTasks = this.getTaskService().createTaskQuery().processInstanceBusinessKey(newProcUnitHandler.getBizId()).list();
            }

            ActivityImpl activity = commandContext.getExecutionEntityManager().findExecutionById(task.getProcessInstanceId()).getProcessDefinition()
                                                  .findActivity(task.getTaskDefinitionKey());

            ExecutionEntity parentExecutionEntity = commandContext.getExecutionEntityManager().findExecutionById(task.getProcessInstanceId())
                                                                  .findExecution(task.getTaskDefinitionKey());
            ExecutionEntity execution = parentExecutionEntity.createExecution();

            parentExecutionEntity.setVariableLocal("nrOfInstances", (Integer) parentExecutionEntity.getVariableLocal("nrOfInstances") + 1);
            parentExecutionEntity.setVariableLocal("nrOfActiveInstances", (Integer) parentExecutionEntity.getVariableLocal("nrOfActiveInstances") + 1);

            execution.setVariableLocal("loopCounter", parentExecutionEntity.getExecutions().size() + 1);
            execution.setVariableLocal("assignee", newProcUnitHandlerId.toString());

            execution.setActive(true);
            execution.setConcurrent(true);
            execution.setScope(false);
            execution.executeActivity(activity);
            // 2 完成当前任务
            this.getTaskService().complete(currentTaskId);
            // 抢占模式完成其他处理人任务
            if (isPreemptModel) {
                for (Task preemptTask : preemptTasks) {
                    if (!preemptTask.getId().equals(currentTaskId)) {
                        Map<String, Object> variables = new HashMap<String, Object>(1);
                        variables.put("otherPreemptModelTask", true);
                        this.getTaskService().complete(preemptTask.getId(), variables, true);
                    }
                }
                this.procUnitHandlerService.updateOtherProcUnitHandlersResultSystemComplete(newProcUnitHandler.getBizId(), newProcUnitHandler.getProcUnitId(),
                                                                                            newProcUnitHandler.getGroupId(), newProcUnitHandlerId, null);
            }

            // new task Id
            SDO localSdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
            String newTaskId = localSdo.getProperty("newTaskId", String.class);
            this.actApplication.updateTaskExtensionStatus(newTaskId, TaskStatus.WAITED);

            if (CooperationModelKind.isChief(newProcUnitHandler.getCooperationModelId())) {
                this.procUnitHandlerService.updateProcUnitHandlerAssistantChiefId(newProcUnitHandler.getBizId(), newProcUnitHandler.getProcUnitId(),
                                                                                  procUnitHandlerId, newProcUnitHandler.getId());
            }

            runtimeTaskExtension.setPreviousId(newTaskId);
            runtimeTaskExtension.setExecutionId(execution.getId());
        } else {
            runtimeTaskExtension.setPreviousId(currentTaskId);
            runtimeTaskExtension.setExecutionId(hiTaskInstExtension.getExecutionId());
            actApplication.updateTaskExtensionStatus(this.currentTaskId, TaskStatus.WAITED);
        }
        
        actApplication.saveTaskExtension(runtimeTaskExtension);

        ProcessEventContext.addNewProcessTask(runtimeTaskExtension.getId());
        // 打回任务发送通知消息
        sendMessage(runtimeTaskExtension);
        return 0;
    }

    private void sendMessage(RuntimeTaskExtension runtimeTaskExtension) {
        MessageSendModel messageSendModel = new MessageSendModel(runtimeTaskExtension);
        messageSendModel.setSenderId(runtimeTaskExtension.getCreatorPersonId());
        messageSendModel.setReceiverId(runtimeTaskExtension.getExecutorPersonId());
        messageSendModel.setIsSend(true);
        this.messageSenderManager.send(messageSendModel);
    }
}
