package com.huigou.uasp.bpm.cmd;

import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.OrgUnit;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.exception.ApplicationException;
import com.huigou.express.ExpressManager;
import com.huigou.uasp.bpm.MessageSendModel;
import com.huigou.uasp.bpm.MessageSenderManager;
import com.huigou.uasp.bpm.ProcessAction;
import com.huigou.uasp.bpm.TaskKind;
import com.huigou.uasp.bpm.TaskScope;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceExtension;
import com.huigou.uasp.bpm.engine.domain.model.RuntimeTaskExtension;
import com.huigou.uasp.bpm.event.ProcessEventContext;
import com.huigou.util.Constants;
import com.huigou.util.StringUtil;

/**
 * 抄送任务
 * 
 * @author gongmm
 */
public class MakeACopyForCmd implements Command<Void> {

    private String taskId;

    private List<String> executorIds;

    private ActApplication actApplication;

    private MessageSenderManager messageSenderManager;

    private String description;

    private TaskService getTaskService() {
        return Context.getProcessEngineConfiguration().getTaskService();
    }

    public MakeACopyForCmd(String taskId, String description, List<String> executorIds, ActApplication actApplication, MessageSenderManager messageSenderManager) {
        this.taskId = taskId;
        this.description = description;
        this.executorIds = executorIds;
        this.actApplication = actApplication;
        this.messageSenderManager = messageSenderManager;
    }

    public Void execute(CommandContext commandContext) {
        Assert.hasText(taskId, "任务ID不能为空。");
        Assert.notEmpty(executorIds, "抄送人不能为空。");

        HistoricTaskInstanceEntity sourceTask = commandContext.getHistoricTaskInstanceEntityManager().findHistoricTaskInstanceById(taskId);
        HistoricTaskInstanceExtension historicTaskInstanceExtension = this.actApplication.loadHistoricTaskInstanceExtension(taskId);// .loadHiTaskInstExtension(taskId);
        Assert.notNull(historicTaskInstanceExtension, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "扩展历史任务"));
        String prefix = ThreadLocalUtil.getVariable(Constants.MAKEACOPYFORPREFIX, String.class);
        if (StringUtil.isBlank(prefix)) {
            prefix = "抄送任务:";
        }
        for (String receiver : executorIds) {
            try {
                @SuppressWarnings("unchecked")
                List<OrgUnit> orgUnits = (List<OrgUnit>) ExpressManager.evaluate("findPersonMembersInOrg('" + receiver + "', true)");

                for (OrgUnit orgUnit : orgUnits) {
                    TaskEntity task = (TaskEntity) getTaskService().newTask();

                    task.setNameWithoutCascade(sourceTask.getName());
                    if (StringUtil.isBlank(description)) {
                        task.setDescriptionWithoutCascade(prefix + sourceTask.getDescription());
                    } else {
                        task.setDescriptionWithoutCascade(description);
                    }
                    task.setPriorityWithoutCascade(sourceTask.getPriority());
                    task.setOwnerWithoutCascade(orgUnit.getFullId());
                    task.setAssigneeWithoutCascade(orgUnit.getFullId());
                    task.setFormKey(sourceTask.getFormKey());

                    getTaskService().saveTask(task);

                    RuntimeTaskExtension runtimeTaskExtension = new RuntimeTaskExtension();

                    runtimeTaskExtension.fromDelegateTask((DelegateTask) task, TaskScope.TASK, TaskKind.MAKE_A_COPY_FOR, sourceTask.getId());
                    runtimeTaskExtension.setGenerateReason(ProcessAction.MAKE_A_COPYFOR);

                    runtimeTaskExtension.setExecutorUrl(historicTaskInstanceExtension.getExecutorUrl());
                    runtimeTaskExtension.setBusinessCode(historicTaskInstanceExtension.getBusinessCode());
                    runtimeTaskExtension.setBusinessKey(historicTaskInstanceExtension.getBusinessKey());
                    runtimeTaskExtension.setPreviousId(historicTaskInstanceExtension.getId());

                    runtimeTaskExtension.setExecutorFullId(orgUnit.getFullId());
                    runtimeTaskExtension.setExecutorFullName(orgUnit.getFullName());

                    actApplication.saveTaskExtension(runtimeTaskExtension);

                    ProcessEventContext.addNewNotProcessTask(task.getId());
                    // 抄送任务发送通知消息
                    MessageSendModel messageSendModel = new MessageSendModel(runtimeTaskExtension);
                    messageSendModel.setSenderId(runtimeTaskExtension.getCreatorPersonId());
                    messageSendModel.setReceiverId(runtimeTaskExtension.getExecutorPersonId());
                    messageSendModel.setIsSend(true);
                    this.messageSenderManager.send(messageSendModel);
                }
            } catch (Exception e) {
                throw new ApplicationException(e.getMessage());
            }
        }
        return null;
    }
}
