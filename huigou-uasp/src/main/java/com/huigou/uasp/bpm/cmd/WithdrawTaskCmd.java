package com.huigou.uasp.bpm.cmd;

import java.util.List;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceExtension;
import com.huigou.util.Constants;
import com.huigou.util.SDO;

/**
 * 回收任务
 */
public class WithdrawTaskCmd extends FlowControlCmdBase {

    private String previousId;

    public WithdrawTaskCmd(String taskId, String previousId, ActApplication actApplication) {
        super(taskId, actApplication);
        this.previousId = previousId;
    }

    /**
     * 回收任务
     */
    public Integer execute(CommandContext commandContext) {
        HistoricTaskInstanceEntity previousTask = Context.getCommandContext().getHistoricTaskInstanceEntityManager().findHistoricTaskInstanceById(previousId);
        if (previousTask == null) {
            throw new ApplicationException(String.format("未找到任务ID“%s”对应的任务。", previousId));
        }

        ActivityImpl destActivityImpl = this.findActivitiImpl(taskId, previousTask.getTaskDefinitionKey());
        if (destActivityImpl == null) {
            throw new ApplicationException(String.format("没有找到目标环节ID“%s”对应的环节。", previousTask.getTaskDefinitionKey()));
        }

        TaskEntity task = Context.getCommandContext().getTaskEntityManager().findTaskById(taskId);
        if (task == null) {
            throw new ApplicationException(String.format("未找到任务ID“%s”对应的任务。", taskId));
        }

        String piId = task.getProcessInstanceId();

        // 判断下一环节的任务是否已完成
        List<String> hiTaskInstIds = findCompletedHiTaskListByPreviousId(piId, previousId);
        if (hiTaskInstIds.size() > 0) {
            // 下一环节已有任务已完成，不能回收。
            throw new ApplicationException(MessageSourceContext.getMessage("common.job.error.withdrawTask.next"));
        }
        
        HistoricTaskInstanceExtension hiTaskInstExtension = actApplication.loadHistoricTaskInstanceExtension(previousId);

        Assert.isTrue(!actApplication.existsReplenishTask(hiTaskInstExtension.getBusinessKey()), "当前流程存在打回的任务，不能回收。");

        
        SDO localSdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        localSdo.putProperty("procInstId", piId);
        localSdo.putProperty("destActivityId", previousTask.getTaskDefinitionKey());
        localSdo.putProperty("businessCode", hiTaskInstExtension.getBusinessCode());
        // 抓回环节的环节处理人ID
        localSdo.putProperty("procUnitHandlerId", hiTaskInstExtension.getProcUnitHandlerId());
        localSdo.putProperty("sourceTaskDescription", previousTask.getDescription());
        // 查找所有并行任务节点，同时回收
        List<String> taskIds = findTaskListByPreviousId(piId, previousId);
        for (String item : taskIds) {
            if (!item.equalsIgnoreCase(taskId)) {
                localSdo.putProperty("mainWithdrawTask", false);
                this.getTaskService().complete(item);
            }
        }
        localSdo.putProperty("mainWithdrawTask", true);
        this.doControlCmd(taskId, null, destActivityImpl.getId());
        return 0;
    }
}
