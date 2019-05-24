package com.huigou.uasp.bpm.cmd;

import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.huigou.uasp.bpm.TaskStatus;
import com.huigou.uasp.bpm.engine.application.ActApplication;

public class CompleteMendTaskCmd extends NeedsActiveTaskCmd<Void> {

    private static final long serialVersionUID = 1L;

    private ActApplication actService;

    public CompleteMendTaskCmd(String taskId) {
        super(taskId);
    }

    public CompleteMendTaskCmd(String taskId, ActApplication actService) {
        this(taskId);
        this.actService = actService;
    }

    protected Void execute(CommandContext commandContext, TaskEntity task) {
        // 将TaskDefinitionKey设置为空，欺骗流程引擎，不做业务逻辑操作
        task.setTaskDefinitionKeyWithoutCascade(null);

        task.complete(null, false);

        actService.deleteRuntimeTaskExtension(taskId);
        actService.updateHistoricTaskInstanceExtensionEnded(taskId, TaskStatus.COMPLETED, TaskStatus.COMPLETED.getId());

        return null;
    }
}