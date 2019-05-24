package com.huigou.uasp.bpm.cmd;

import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.huigou.uasp.bpm.TaskKind;
import com.huigou.uasp.bpm.TaskScope;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.domain.model.RuntimeTaskExtension;
import com.huigou.util.ClassHelper;

/**
 * 催办任务
 * 
 * @author gongmm
 */
public class RemindTaskCmd implements Command<Void> {

    private String taskId;


    private ActApplication actService;

    //private static String REMIND_TASK_DESCRIPTION_FORMAT = "%s在%s向您发起催办事项：%s";

    private TaskService getTaskService() {
        return Context.getProcessEngineConfiguration().getTaskService();
    }

    public RemindTaskCmd(String taskId, String[] receivers, ActApplication actService) {
        this.taskId = taskId;
        this.actService = actService;
    }

    @SuppressWarnings("null")
    public Void execute(CommandContext commandContext) {
        HistoricTaskInstanceEntity historicTaskInstanceEntity = commandContext.getHistoricTaskInstanceEntityManager().findHistoricTaskInstanceById(taskId);
        String processInstanceId = historicTaskInstanceEntity.getProcessInstanceId();
        List<ExecutionEntity> executionEntityList = commandContext.getExecutionEntityManager().findChildExecutionsByParentExecutionId(processInstanceId);

        List<Map<String, Object>> executors = null;
        String executorFullId = null, executorFullName = null;

        for (ExecutionEntity executionEntity : executionEntityList) {
            if (executionEntity.isActive()) {
               // executors = actService.queryRuntimeExecutorsByExecutionId(executionEntity.getId());
                for (Map<String, Object> executor : executors) {
                	executorFullId = ClassHelper.convert(executor.get("fullId"), String.class);
                	executorFullName = ClassHelper.convert(executor.get("fullName"), String.class);

                      

                    TaskEntity task = (TaskEntity) getTaskService().newTask();

                    task.setNameWithoutCascade(historicTaskInstanceEntity.getName());
                    // task.setDescriptionWithoutCascade("通知任务：" + historicTaskInstanceEntity);
                    task.setPriorityWithoutCascade(50);
                    task.setOwnerWithoutCascade(executorFullId);
                    task.setAssigneeWithoutCascade(executorFullName);

                    getTaskService().saveTask(task);

                    RuntimeTaskExtension runtimeTaskExtension = new RuntimeTaskExtension(task.getId(), TaskScope.TASK, TaskKind.NOTICE);
                    runtimeTaskExtension.setExecutorFullId(executorFullId);
                    runtimeTaskExtension.setExecutorFullName(executorFullName); 

                    actService.saveTaskExtension(runtimeTaskExtension);
                }
            }
        }
        return null;
    }
}
