package com.huigou.uasp.bpm.cmd;

import java.util.List;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.uasp.bpm.ProcessAction;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.domain.model.RuntimeTaskExtension;
import com.huigou.util.Constants;
import com.huigou.util.SDO;

/**
 * 回退
 */
public class BackTaskCmd extends FlowControlCmdBase {

    private String destActivityId;

    private String backProcUnitHandlerId;

    private ActApplication actApplication;

    public BackTaskCmd(String taskId, String destActivityId, String backProcUnitHandlerId, ActApplication actApplication) {
        super(taskId, actApplication);
        this.destActivityId = destActivityId;
        this.actApplication = actApplication;
        this.backProcUnitHandlerId = backProcUnitHandlerId;
        flowControlCmd = ProcessAction.BACK;
    }

    /**
     * 回退任务
     * 
     * @return
     */
    public Integer execute(CommandContext commandContext) {
        Assert.hasText(taskId, "任务ID不能为空。");

        RuntimeTaskExtension runtimeTaskExtension = this.actApplication.loadRuntimeTaskExtension(taskId);
        Assert.notNull(runtimeTaskExtension, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "运行时任务扩展"));

        Assert.isTrue(!actApplication.existsReplenishTask(runtimeTaskExtension.getBusinessKey()), "当前流程存在打回的任务，不能回退。");
        
        ActivityImpl destActivityImpl = this.findActivitiImpl(taskId, destActivityId);
        Assert.notNull(destActivityImpl, String.format("没有找到目标环节ID“%s”对应的环节。", destActivityId));

        TaskEntity task = Context.getCommandContext().getTaskEntityManager().findTaskById(taskId);
        Assert.notNull(task, String.format("没有找到任务ID“%s”对应的任务。", taskId));

        String procInstId = task.getProcessInstanceId();



        // 判断当前环节任务是否有已完成
        /*
         * String previousId = runtimeTaskExtension.getPreviousId();
         * List<String> hiTaskInstIds = findCompletedHiTaskListByPreviousId(procInstId, previousId);
         * if (hiTaskInstIds.size() > 0) {
         * throw new ApplicationException("当前环节已有任务已完成，不能回退。");
         * }
         */

        SDO localSdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        localSdo.putProperty("procInstId", procInstId);
        localSdo.putProperty("destActivityId", destActivityId);
        localSdo.putProperty("sourceTaskDescription", task.getDescription());
        localSdo.putProperty("backProcUnitHandlerId", backProcUnitHandlerId);

        // 查找所有并行任务节点，同时回退
        List<Task> tasks = this.findTaskListByKey(procInstId, task.getTaskDefinitionKey());
        for (Task item : tasks) {
            if (!item.getId().equalsIgnoreCase(taskId)) {
                localSdo.putProperty("mainBackTask", false);
                this.getTaskService().complete(item.getId());
            }
        }

        localSdo.putProperty("mainBackTask", true);
        this.doControlCmd(taskId, null, destActivityImpl.getId());
        return 0;
    }
}
