package com.huigou.uasp.bpm.cmd;

import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.HistoricProcessInstanceQueryImpl;
import org.activiti.engine.impl.bpmn.listener.DelegateExpressionTaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.delegate.TaskListenerInvocation;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.springframework.util.Assert;

import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bpm.ActivityKind;
import com.huigou.uasp.bpm.TaskListenerExt;

/**
 * 保存业务数据
 */
public class SaveBizDataCmd implements Command<Void> {

    private String taskId;

    private String bizId;

    public SaveBizDataCmd(String bizId, String taskId) {
        this.bizId = bizId;
        this.taskId = taskId;
    }

    /**
     * 可以用TaskEntityManager直接查找获得TaskEntity，不能解决手动调整历史任务状态，修改表单数据的问题
     */
    public Void execute(CommandContext commandContext) {
        Assert.hasText(taskId, "任务ID不能为空,请刷新页面后重试。");
        Assert.hasText(bizId, "业务ID不能为空,请刷新页面后重试。");

        HistoricProcessInstanceQueryImpl historicProcessInstanceQuery = new HistoricProcessInstanceQueryImpl();
        historicProcessInstanceQuery.processInstanceBusinessKey(bizId);
        List<HistoricProcessInstance> historicProcessInstances = commandContext.getHistoricProcessInstanceEntityManager()
                                                                               .findHistoricProcessInstancesByQueryCriteria(historicProcessInstanceQuery);
        if (historicProcessInstances.size() == 0) {
            throw new ApplicationException(String.format("未找到业务ID“%s”对应的流程实例,请刷新页面后重试。", bizId));
        }

        HistoricTaskInstanceEntity hiTaskInstEntity = commandContext.getHistoricTaskInstanceEntityManager().findHistoricTaskInstanceById(taskId);

        if (hiTaskInstEntity == null) {
            throw new ApplicationException(String.format("未找到任务ID“%s”对应的任务,请刷新页面后重试。", taskId));
        }

        // 审批环节，任务已结束，不能保存。
        if (ActivityKind.isApproveActivity(hiTaskInstEntity.getTaskDefinitionKey()) && hiTaskInstEntity.getEndTime() != null) {
            throw new ApplicationException("当前任务已结束，不能保存数据。");
        }

        ExecutionEntity executionEntity = new ExecutionEntity();

        HistoricProcessInstance historicProcessInstance = historicProcessInstances.get(0);

        executionEntity.setId(historicProcessInstance.getId());
        executionEntity.setBusinessKey(historicProcessInstance.getBusinessKey());
        executionEntity.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());

        TaskEntity task = new TaskEntity();

        task.setId(hiTaskInstEntity.getId());
        task.setNameWithoutCascade(hiTaskInstEntity.getName());
        task.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());
        task.setProcessInstanceId(historicProcessInstance.getId());

        task.setTaskDefinitionKeyWithoutCascade(hiTaskInstEntity.getTaskDefinitionKey());

        // ProcessInstanceId -->ExecutionId
        task.setExecutionId(hiTaskInstEntity.getProcessInstanceId());
        TaskDefinition taskDefinition = task.getTaskDefinition();

        if (taskDefinition != null) {
            List<TaskListener> taskEventListeners = task.getTaskDefinition().getTaskListener(TaskListenerExt.EVENTNAME_SAVE_BIZ_DATA);
            if (taskEventListeners != null) {
                for (TaskListener taskListener : taskEventListeners) {
                    task.setEventName(TaskListenerExt.EVENTNAME_SAVE_BIZ_DATA);
                    try {
                        String expressionText = ((DelegateExpressionTaskListener) taskListener).getExpressionText();
                        Expression expression = (Expression) commandContext.getProcessEngineConfiguration().getExpressionManager()
                                                                           .createExpression(expressionText);

                        Object delegate = expression.getValue(executionEntity);

                        if (delegate instanceof TaskListener) {
                            Context.getProcessEngineConfiguration().getDelegateInterceptor()
                                   .handleInvocation(new TaskListenerInvocation((TaskListener) delegate, task));
                        }
                    } catch (Exception e) {
                        throw new ActivitiException("Exception while invoking TaskListener: " + e.getMessage(), e);
                    }
                }
            }
        }
        return null;
    }
}
