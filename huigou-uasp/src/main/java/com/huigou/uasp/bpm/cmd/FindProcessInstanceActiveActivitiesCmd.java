package com.huigou.uasp.bpm.cmd;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import com.huigou.exception.ApplicationException;
import com.huigou.util.StringUtil;

public class FindProcessInstanceActiveActivitiesCmd implements Command<List<ActivityImpl>> {

    private String processInstanceId;

    private void findActiveActivities(ExecutionEntity execution, List<ActivityImpl> result) {
        if (execution.getActivity() != null) {
            result.add(execution.getActivity());
        } else {
            for (ExecutionEntity item : execution.getExecutions()) {
                findActiveActivities(item, result);
            }
        }
    }

    public FindProcessInstanceActiveActivitiesCmd(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public List<ActivityImpl> execute(CommandContext commandContext) {
        if (StringUtil.isBlank(processInstanceId)) {
            throw new ApplicationException("流程实例id为空。");
        }

        ExecutionEntity execution = commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);
        if (execution == null) {
            throw new ApplicationException(String.format("没有找到流程实例id“%s”对应的流程。", processInstanceId));
        }

        List<ActivityImpl> result = new ArrayList<ActivityImpl>();
        findActiveActivities(execution, result);

        return result;
    }
}
