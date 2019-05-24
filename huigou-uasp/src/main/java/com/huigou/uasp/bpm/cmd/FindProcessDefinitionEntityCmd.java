package com.huigou.uasp.bpm.cmd;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;

public class FindProcessDefinitionEntityCmd implements Command<ProcessDefinitionEntity> {

    private String processDefinitionId;

    public FindProcessDefinitionEntityCmd(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public ProcessDefinitionEntity execute(CommandContext commandContext) {
        ProcessDefinitionEntity processDefinitionEntity = Context.getProcessEngineConfiguration().getDeploymentManager()
                                                                 .findDeployedProcessDefinitionById(processDefinitionId);

        if (processDefinitionEntity == null) {
            throw new IllegalArgumentException("cannot find processDefinition : " + processDefinitionId);
        }

        return processDefinitionEntity;
    }
}
