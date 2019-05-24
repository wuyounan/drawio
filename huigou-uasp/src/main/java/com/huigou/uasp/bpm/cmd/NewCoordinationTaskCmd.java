package com.huigou.uasp.bpm.cmd;

import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.util.Assert;

import com.huigou.context.OrgUnit;
import com.huigou.exception.ApplicationException;
import com.huigou.express.ExpressManager;
import com.huigou.uasp.bpm.MessageSendModel;
import com.huigou.uasp.bpm.MessageSenderManager;
import com.huigou.uasp.bpm.TaskScope;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.domain.model.CoordinationTask;
import com.huigou.uasp.bpm.engine.domain.model.RuntimeTaskExtension;
import com.huigou.uasp.bpm.event.ProcessEventContext;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;

/**
 * 新建协同任务命令
 * 
 * @author gongmm
 */
public class NewCoordinationTaskCmd implements Command<Void> {

    private CoordinationTask coordinationTask;

    private ActApplication actApplication;

    private MessageSenderManager messageSenderManager;

    private TaskService getTaskService() {
        return Context.getProcessEngineConfiguration().getTaskService();
    }

    public NewCoordinationTaskCmd(CoordinationTask coordinationTask, ActApplication actApplication, MessageSenderManager messageSenderManager) {
        this.coordinationTask = coordinationTask;
        this.actApplication = actApplication;
        this.messageSenderManager = messageSenderManager;
    }

    public Void execute(CommandContext commandContext) {
        Assert.notNull(coordinationTask, "参数coordinationTask不能为空。");

        String procInstId = null;

        if (this.coordinationTask.getAdditiveData() != null && this.coordinationTask.getAdditiveData().get("procInstId") != null) {
            procInstId = ClassHelper.convert(this.coordinationTask.getAdditiveData().get("procInstId"), String.class);
        }

        for (String receiver : coordinationTask.getExecutorIds()) {
            try {
                @SuppressWarnings("unchecked")
                List<OrgUnit> orgUnits = (List<OrgUnit>) ExpressManager.evaluate("findPersonMembersInOrg('" + receiver + "', true)");

                for (OrgUnit orgUnit : orgUnits) {
                    TaskEntity task = (TaskEntity) getTaskService().newTask();
                    if (!StringUtil.isBlank(procInstId)) {
                        task.setProcessInstanceId(procInstId);
                    }

                    task.setTaskDefinitionKeyWithoutCascade(coordinationTask.getProcUnitId());
                    task.setNameWithoutCascade(coordinationTask.getProcUnitName());
                    task.setDescriptionWithoutCascade(coordinationTask.getDescription());
                    task.setPriorityWithoutCascade(50);
                    task.setOwnerWithoutCascade(orgUnit.getFullId());
                    task.setAssigneeWithoutCascade(orgUnit.getFullId());

                    getTaskService().saveTask(task);

                    RuntimeTaskExtension runtimeTaskExtension = new RuntimeTaskExtension();
                    runtimeTaskExtension.fromDelegateTask((DelegateTask) task, TaskScope.TASK, coordinationTask.getTaskKind(), task.getId());
                    runtimeTaskExtension.setExecutorUrl(coordinationTask.getFormKey());
                    runtimeTaskExtension.setBusinessKey(coordinationTask.getBusinessKey());
                    runtimeTaskExtension.setBusinessCode(coordinationTask.getBusinessCode());
                    runtimeTaskExtension.setProcUnitHandlerId(coordinationTask.getProcUnitHandlerId());

                    runtimeTaskExtension.setExecutorFullId(orgUnit.getFullId());
                    runtimeTaskExtension.setExecutorFullName(orgUnit.getFullName());

                    if (StringUtil.isBlank(coordinationTask.getInitiatorPersonMemberId())) {
                        this.actApplication.saveTaskExtension(runtimeTaskExtension);
                    } else {
                        this.actApplication.saveTaskExtension(runtimeTaskExtension, coordinationTask.getInitiatorPersonMemberId());
                    }

                    ProcessEventContext.addNewNotProcessTask(task.getId());
                    // 协同任务发送通知消息
                    MessageSendModel messageSendModel = new MessageSendModel(runtimeTaskExtension);
                    messageSendModel.setSenderId(runtimeTaskExtension.getCreatorPersonId());
                    messageSendModel.setReceiverId(runtimeTaskExtension.getExecutorPersonId());
                    messageSendModel.setIsSend(true);
                    this.messageSenderManager.send(messageSendModel);
                }
            } catch (Exception e) {
                throw new ApplicationException(e.getMessage(), e);
            }
        }
        return null;
    }
}
