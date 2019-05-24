package com.huigou.uasp.bpm.cmd;

import org.activiti.engine.impl.cmd.NeedsActiveExecutionCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

/**
 * 删除流程实例
 */
public class DeleteProcessInstanceCmd extends NeedsActiveExecutionCmd<Void> {

    private static final long serialVersionUID = 1L;

    protected String taskId;

    public DeleteProcessInstanceCmd(String taskId) {
        super(taskId);
        this.taskId = taskId;
    }

    @Override
    protected Void execute(CommandContext commandContext, ExecutionEntity executionEntity) {
        // executionEntity.d
        return null;
    }

}
