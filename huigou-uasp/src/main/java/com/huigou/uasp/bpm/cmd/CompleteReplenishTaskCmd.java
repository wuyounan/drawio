package com.huigou.uasp.bpm.cmd;

import java.util.Date;

import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.huigou.uasp.bpm.MessageSendModel;
import com.huigou.uasp.bpm.MessageSenderManager;
import com.huigou.uasp.bpm.TaskStatus;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.domain.model.RuntimeTaskExtension;
import com.huigou.uasp.bpm.event.ProcessEventContext;
import com.huigou.util.Util;

/**
 * 完成打回任务命令
 * 
 * @author gongmm
 */
public class CompleteReplenishTaskCmd extends NeedsActiveTaskCmd<Void> {

    private static final long serialVersionUID = 1L;

    private ActApplication actApplication;

    private MessageSenderManager messageSenderManager;

    public CompleteReplenishTaskCmd(String taskId, ActApplication actApplication, MessageSenderManager messageSenderManager) {
        super(taskId);
        this.actApplication = actApplication;
        this.messageSenderManager = messageSenderManager;
    }

    protected Void execute(CommandContext commandContext, TaskEntity task) {

        RuntimeTaskExtension runtimeTaskExtension = actApplication.loadRuntimeTaskExtension(taskId);
        Util.check(runtimeTaskExtension != null, String.format("未找到ID“%s”对应的任务扩展数据。", taskId));

        Date currentTime = commandContext.getProcessEngineConfiguration().getClock().getCurrentTime();
        task.setCreateTime(currentTime);
        task.update();

        // 将TaskDefinitionKey设置为空，欺骗流程引擎，不做业务逻辑操作
        task.setTaskDefinitionKeyWithoutCascade(null);
        task.complete(null, false);

        this.actApplication.updateTaskExtensionStatus(runtimeTaskExtension.getPreviousId(), TaskStatus.READY);

        actApplication.deleteRuntimeTaskExtension(taskId);
        actApplication.updateHistoricTaskInstanceExtensionEnded(taskId, TaskStatus.COMPLETED, TaskStatus.COMPLETED.getId());
        ProcessEventContext.addUpdatedStatusTask(runtimeTaskExtension.getPreviousId());
        // 协同任务发送通知消息
        RuntimeTaskExtension previousRuntimeTaskExtension = actApplication.loadRuntimeTaskExtension(runtimeTaskExtension.getPreviousId());

        MessageSendModel messageSendModel = new MessageSendModel(previousRuntimeTaskExtension);
        messageSendModel.setSenderId(runtimeTaskExtension.getExecutorPersonId());
        messageSendModel.setReceiverId(previousRuntimeTaskExtension.getExecutorPersonId());
        messageSendModel.setIsSend(true);
        this.messageSenderManager.send(messageSendModel);
        return null;
    }
}