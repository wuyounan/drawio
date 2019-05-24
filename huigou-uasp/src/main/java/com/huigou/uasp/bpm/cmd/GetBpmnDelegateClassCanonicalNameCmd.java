package com.huigou.uasp.bpm.cmd;

import java.util.List;

import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.bpmn.listener.DelegateExpressionTaskListener;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.huigou.context.ThreadLocalUtil;
import com.huigou.uasp.bpm.TaskListenerExt;
import com.huigou.util.ApplicationContextWrapper;
import com.huigou.util.StringUtil;

public class GetBpmnDelegateClassCanonicalNameCmd implements Command<Void> {

    private String taskId;

    public GetBpmnDelegateClassCanonicalNameCmd(String taskId) {
        this.taskId = taskId;
    }

    public Void execute(CommandContext commandContext) {
        TaskEntity task = commandContext.getTaskEntityManager().findTaskById(taskId);

        if (task != null && task.getTaskDefinition() != null) {
            List<TaskListener> taskEventListeners = task.getTaskDefinition().getTaskListener(TaskListenerExt.EVENTNAME_CREATE);
            if (taskEventListeners != null && taskEventListeners.size() > 0) {
                TaskListener taskListener = taskEventListeners.get(0);
                if (taskListener instanceof DelegateExpressionTaskListener) {
                    String beanName = ((DelegateExpressionTaskListener) taskListener).getExpressionText();

                    if (StringUtil.isNotBlank(beanName)) {
                        beanName = beanName.trim();
                        beanName = beanName.substring(2, beanName.length() - 1);
                        Object object = ApplicationContextWrapper.getBean(beanName);
                        ThreadLocalUtil.putVariable(ThreadLocalUtil.BIZ_CLASS_NAME_KEY, object.getClass().getCanonicalName());
                    }
                }
            }
        }
        return null;
    }

}
