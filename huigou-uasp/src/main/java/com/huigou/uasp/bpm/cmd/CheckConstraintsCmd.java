package com.huigou.uasp.bpm.cmd;

import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.bpmn.listener.DelegateExpressionTaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.delegate.TaskListenerInvocation;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.util.Assert;

import com.huigou.uasp.bpm.TaskKind;
import com.huigou.uasp.bpm.TaskListenerExt;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceExtension;

/**
 * 流程业务数据检查命令
 * 
 * @author xx
 */
public class CheckConstraintsCmd implements Command<Map<String, Object>> {

    private String taskId;
    
    private ActApplication actApplication;

    public CheckConstraintsCmd(String taskId, ActApplication actApplication) {
        this.taskId = taskId;
        this.actApplication = actApplication;
    }

    public Map<String, Object> execute(CommandContext commandContext) {
        // findTaskById is already check taskId not null
        TaskEntity task = commandContext.getTaskEntityManager().findTaskById(taskId);
        Assert.state(task != null, String.format("未找到任务ID“%s”对应的任务。 ", taskId));
        
        HistoricTaskInstanceExtension htie = actApplication.loadHistoricTaskInstanceExtension(taskId);
        if (TaskKind.REPLENISH.equals(htie.getKindId())){
            task.setProcessDefinitionId(htie.getProcessDefinitionId()); 
            ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findExecutionById(htie.getExecutionId());
            task.setExecution(executionEntity);
        }
        
        if (task.getProcessDefinitionId() != null && task.getTaskDefinition() != null) {
            List<TaskListener> taskEventListeners = task.getTaskDefinition().getTaskListener(TaskListenerExt.EVENTNAME_CHECK_CONSTRAINTS);
            if (taskEventListeners == null) {
                taskEventListeners = task.getTaskDefinition().getTaskListener(TaskListenerExt.EVENTNAME_CREATE);
            }

            if (taskEventListeners != null) {
                for (TaskListener taskListener : taskEventListeners) {
                    if (taskListener instanceof DelegateExpressionTaskListener) {
                        task.setEventName(TaskListenerExt.EVENTNAME_CHECK_CONSTRAINTS);
                        try {
                            Context.getProcessEngineConfiguration().getDelegateInterceptor()
                                   .handleInvocation(new TaskListenerInvocation((TaskListener) taskListener, task));
                            break;
                        } catch (Exception e) {
                            throw new ActivitiException("调用获取处理人出错: " + e.getMessage(), e);
                        }
                    }
                }
            }
        }
        return null;
    }
}
