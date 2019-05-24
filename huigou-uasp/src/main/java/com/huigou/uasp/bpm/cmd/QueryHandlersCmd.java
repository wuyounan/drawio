package com.huigou.uasp.bpm.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.bpmn.listener.DelegateExpressionTaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.delegate.TaskListenerInvocation;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.huigou.context.ThreadLocalUtil;
import com.huigou.uasp.bpm.TaskListenerExt;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;

/**
 * 查询处理人
 */
public class QueryHandlersCmd implements Command<Map<String, Object>> {

    private String taskId;

    public QueryHandlersCmd(String taskId) {
        this.taskId = taskId;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> execute(CommandContext commandContext) {
        // findTaskById is already check taskId not null
        TaskEntity task = commandContext.getTaskEntityManager().findTaskById(taskId);
        if (task == null) {
            throw new ActivitiException(String.format("未找到任务ID“%s”对应的任务。 ", taskId));
        }

        Map<String, Object> result = null;

        if (task.getTaskDefinition() != null) {
            List<TaskListener> taskEventListeners = task.getTaskDefinition().getTaskListener(TaskListenerExt.EVENTNAME_QUERY_HANDLERS);
            if (taskEventListeners == null) {
                taskEventListeners = task.getTaskDefinition().getTaskListener(TaskListenerExt.EVENTNAME_CREATE);
            }

            if (taskEventListeners != null) {
                for (TaskListener taskListener : taskEventListeners) {
                    if (taskListener instanceof DelegateExpressionTaskListener) {
                        task.setEventName(TaskListenerExt.EVENTNAME_QUERY_HANDLERS);
                        try {
                            Context.getProcessEngineConfiguration().getDelegateInterceptor()
                                   .handleInvocation(new TaskListenerInvocation((TaskListener) taskListener, task));
                            result = (Map<String, Object>) ThreadLocalUtil.getVariable("queryHandlers");
                            // 合并的数据不加入返回
                            List<Map<String, Object>> handlers = (List<Map<String, Object>>) result.get(Constants.ROWS);
                            if (handlers != null && handlers.size() > 0) {
                                List<Map<String, Object>> rows = new ArrayList<>(handlers.size());
                                Integer status = 0;
                                for (Map<String, Object> m : handlers) {
                                    status = ClassHelper.convert(m.get("status"), Integer.class, 0);
                                    if (status != -1) {
                                        rows.add(m);
                                    }
                                }
                                result.put(Constants.ROWS, rows);
                            }
                            break;
                        } catch (Exception e) {
                            throw new ActivitiException("调用获取处理人出错: " + e.getMessage(), e);
                        }
                    }
                }
            }
        }

        return result;
    }
}
