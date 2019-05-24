package com.huigou.uasp.bpm.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bpm.ExecutionListenerExt;
import com.huigou.uasp.bpm.ProcessAction;
import com.huigou.uasp.bpm.TaskKind;
import com.huigou.uasp.bpm.TaskScope;
import com.huigou.uasp.bpm.TaskStatus;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceExtension;
import com.huigou.uasp.bpm.engine.domain.model.RuntimeTaskExtension;
import com.huigou.uasp.bpm.event.ProcessEventContext;
import com.huigou.util.StringUtil;

/**
 * 撤销流程
 */
public class RecallProcessInstanceCmd extends FlowControlCmdBase {

    private static Object lock = new Object();

    private String processInstanceId;

    private CommandContext commandContext;

    public RecallProcessInstanceCmd(String taskId, ActApplication actApplication) {
        super(taskId, actApplication);
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    protected TaskService getTaskService() {
        return Context.getProcessEngineConfiguration().getTaskService();
    }

    private void buildTemplateRevision(List<ExecutionEntity> executions, String parentId, Integer level) {
        for (ExecutionEntity executionEntity : executions) {
            if (StringUtil.isNotBlank(executionEntity.getParentId()) && executionEntity.getParentId().equals(parentId)) {
                executionEntity.setRevision(level);
                buildTemplateRevision(executions, executionEntity.getId(), level + 1);
            }
        }
    }

    public Integer execute(CommandContext commandContext) {
        Assert.hasText(processInstanceId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "processInstanceId"));
        Assert.hasText(taskId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "taskId"));

        this.commandContext = commandContext;

        HistoricProcessInstanceEntity historicProcessInstanceEntity = commandContext.getHistoricProcessInstanceEntityManager()
                                                                                    .findHistoricProcessInstance(processInstanceId);
        Assert.state(historicProcessInstanceEntity.getEndTime() == null, MessageSourceContext.getMessage("common.job.error.recall.over"));

        ExecutionEntity execution = commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);
        Assert.state(execution != null, MessageSourceContext.getMessage("common.job.error.recall.notfind", processInstanceId));

        // 撤销打回、补审任务
        TaskEntity taskEntity;
        HistoricTaskInstanceEntity historicTaskInstanceEntity = commandContext.getHistoricTaskInstanceEntityManager().findHistoricTaskInstanceById(taskId);
        List<RuntimeTaskExtension> coordinationTaskInstances = this.actApplication.queryCoordinationTaskInstances(execution.getBusinessKey());
        // Assert.state(coordinationTaskInstances.size() == 0, "当前流程存在打回、补审任务，不能撤销。");

        List<HistoricTaskInstanceExtension> hties = this.actApplication.queryNotCompleteHiTaskInstExtensionsByProcInstId(processInstanceId);
        for (HistoricTaskInstanceExtension htie : hties) {
            if (htie.getTaskDefinitionKey().equals(historicTaskInstanceEntity.getTaskDefinitionKey())) {
                // 申请环节，不能撤销。
                throw new ApplicationException(MessageSourceContext.getMessage("common.job.error.recall.apply"));
            }
        }

        if (coordinationTaskInstances.size() > 0) {
            for (RuntimeTaskExtension item : coordinationTaskInstances) {
                // 更新流程环节为null
                taskEntity = commandContext.getTaskEntityManager().findTaskById(item.getId());
                taskEntity.setTaskDefinitionKeyWithoutCascade(null);
                getTaskService().saveTask(taskEntity);
                commandContext.getTaskEntityManager().deleteTask(item.getId(), TaskStatus.CANCELED.getId(), false);

                actApplication.deleteRuntimeTaskExtension(item.getId());
                actApplication.updateHistoricTaskInstanceExtensionStatus(item.getId(), TaskStatus.CANCELED);

            }
            commandContext.getDbSqlSession().flush();
        }

        commandContext.getTaskEntityManager().deleteTasksByProcessInstanceId(processInstanceId, ProcessAction.RECALL_PROCESS_INSTANCE, false);

        List<ExecutionEntity> executions = commandContext.getExecutionEntityManager().findChildExecutionsByProcessInstanceId(processInstanceId);

        if (executions.size() > 1) {

            String rootExecutionId = null;
            Map<String, Integer> oldRevisions = new HashMap<String, Integer>(executions.size());
            for (ExecutionEntity executionEntity : executions) {
                oldRevisions.put(executionEntity.getId(), executionEntity.getRevision());
                if (StringUtil.isBlank(executionEntity.getParentId())) {
                    rootExecutionId = executionEntity.getId();
                    executionEntity.setRevision(1);
                }
            }

            buildTemplateRevision(executions, rootExecutionId, 2);

            Collections.sort(executions, new Comparator<ExecutionEntity>() {
                public int compare(ExecutionEntity o1, ExecutionEntity o2) {
                    return o2.getRevision() - o1.getRevision();
                }
            });

            Integer oldRevision;
            for (ExecutionEntity executionEntity : executions) {
                oldRevision = oldRevisions.get(executionEntity.getId());
                executionEntity.setRevision(oldRevision);
            }
        }

        for (ExecutionEntity entity : executions) {
            if (!entity.getId().equalsIgnoreCase(processInstanceId)) {
                entity.remove();
            } else {
                RepositoryService repositoryService = Context.getProcessEngineConfiguration().getRepositoryService();
                ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(entity.getProcessDefinitionId());
                Assert.state(processDefinition != null, MessageSourceContext.getMessage("common.job.error.recall.notfind", processInstanceId));

                synchronized (lock) {
                    List<ExecutionListener> executionListeners = processDefinition.getExecutionListeners(ExecutionListener.EVENTNAME_START);
                    if (executionListeners.size() > 0) {
                        ExecutionListener executionListener = executionListeners.get(0);
                        processDefinition.addExecutionListener(ExecutionListenerExt.EVENTNAME_RECALL_PROCESS_INSTANCE, executionListener);
                        try {
                            entity.setEventName(ExecutionListenerExt.EVENTNAME_RECALL_PROCESS_INSTANCE);
                            executionListener.notify(entity);
                        } catch (Exception e) {
                            throw new ApplicationException(e);
                        } finally {
                            processDefinition.getExecutionListeners(ExecutionListenerExt.EVENTNAME_RECALL_PROCESS_INSTANCE).remove(0);
                        }
                    }
                }
            }
        }

        if (coordinationTaskInstances.size() > 0) {
            List<String> ids = new ArrayList<String>(coordinationTaskInstances.size());
            for (RuntimeTaskExtension runtimeTaskExtension : coordinationTaskInstances) {
                ids.add(runtimeTaskExtension.getId());
            }
            List<HistoricTaskInstanceExtension> wchties = this.actApplication.queryHistoricTaskInstanceExtensions(ids);
            for (HistoricTaskInstanceExtension wchtie : wchties) {
                ProcessEventContext.addDeletedTask(wchtie.getId());
            }
        }

        // for (RuntimeTaskExtension item : coordinationTaskInstances) {
        // chtie = this.actApplication.loadHistoricTaskInstanceExtension(item.getId());
        // ProcessEventContext.addCompletedTask(ProcessEventTaskInstance.newInstance(chtie));
        // }

        for (HistoricTaskInstanceExtension htie : hties) {
            ProcessEventContext.addCompletedTask(htie.getId());
        }

        createTaskForApplicant(execution);

        return null;
    }

    public void createTaskForApplicant(ExecutionEntity executionEntity) {
        HistoricTaskInstanceEntity historicTaskInstanceEntity = commandContext.getHistoricTaskInstanceEntityManager().findHistoricTaskInstanceById(taskId);

        ProcessDefinitionEntity processDefinitionEntity = Context.getProcessEngineConfiguration().getDeploymentManager()
                                                                 .findDeployedProcessDefinitionById(historicTaskInstanceEntity.getProcessDefinitionId());
        ActivityImpl activity = processDefinitionEntity.findActivity(historicTaskInstanceEntity.getTaskDefinitionKey());

        executionEntity.setConcurrent(false);
        executionEntity.setActivity(activity);

        TaskEntity task = TaskEntity.createAndInsert(executionEntity);

        task.setProcessDefinitionId(historicTaskInstanceEntity.getProcessDefinitionId());
        task.setAssignee(historicTaskInstanceEntity.getAssignee());
        task.setParentTaskId(historicTaskInstanceEntity.getParentTaskId());
        task.setName(historicTaskInstanceEntity.getName());
        task.setTaskDefinitionKey(historicTaskInstanceEntity.getTaskDefinitionKey());
        task.setExecutionId(historicTaskInstanceEntity.getExecutionId());
        task.setPriority(historicTaskInstanceEntity.getPriority());
        task.setProcessInstanceId(historicTaskInstanceEntity.getProcessInstanceId());
        task.setFormKey(historicTaskInstanceEntity.getFormKey());

        task.setDescription("撤销：" + historicTaskInstanceEntity.getDescription());

        HistoricTaskInstanceExtension historicTaskInstanceExtension = this.actApplication.loadHistoricTaskInstanceExtension(taskId);
        Assert.notNull(historicTaskInstanceExtension, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "任务"));

        RuntimeTaskExtension runtimeTaskExtension = new RuntimeTaskExtension();

        runtimeTaskExtension.fromDelegateTask((DelegateTask) task, TaskScope.PROCESS, TaskKind.TASK, historicTaskInstanceEntity.getId());
        runtimeTaskExtension.setGenerateReason(ProcessAction.RECALL_PROCESS_INSTANCE);

        runtimeTaskExtension.setBusinessCode(historicTaskInstanceExtension.getBusinessCode());
        runtimeTaskExtension.setPreviousId(taskId);

        Operator operator = ThreadLocalUtil.getOperator();

        runtimeTaskExtension.setExecutorFullId(operator.getFullId());
        runtimeTaskExtension.setExecutorFullName(operator.getFullName());

        actApplication.saveTaskExtension(runtimeTaskExtension);

        ProcessEventContext.addNewProcessTask(task.getId());
    }
}
