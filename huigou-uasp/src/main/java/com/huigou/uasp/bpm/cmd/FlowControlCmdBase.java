package com.huigou.uasp.bpm.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.util.StringUtil;

/**
 * 流程控制命令基类
 */
public class FlowControlCmdBase implements Command<Integer> {

    // private static Logger log = LoggerFactory.getLogger(FlowControlCmdBase.class);

    protected String taskId;

    protected ActApplication actApplication;

    protected String flowControlCmd;

    protected static Object lock = new Object();

    public FlowControlCmdBase(String taskId, ActApplication actApplication) {
        this.taskId = taskId;
        this.actApplication = actApplication;
    }

    /**
     * 根据任务ID获得任务实例
     * 
     * @param taskId
     *            任务ID
     * @return
     * @throws Exception
     */
    protected TaskEntity findTaskById(String taskId) {
        TaskEntity task = Context.getCommandContext().getTaskEntityManager().findTaskById(taskId);
        if (task == null) {
            throw new ApplicationException("未找到任务实例。");
        }
        return task;
    }

    /**
     * 根据任务ID获取对应的流程实例
     * 
     * @param taskId
     *            任务ID
     * @return
     * @throws Exception
     */
    protected ProcessInstance findProcessInstanceByTaskId(String taskId) {
        // 找到流程实例
        ProcessInstance processInstance = Context.getProcessEngineConfiguration().getRuntimeService().createProcessInstanceQuery()
                                                 .processInstanceId(findTaskById(taskId).getProcessInstanceId()).singleResult();

        if (processInstance == null) {
            throw new ApplicationException("流程实例未找到 。");
        }
        return processInstance;
    }

    protected TaskService getTaskService() {
        return Context.getProcessEngineConfiguration().getTaskService();
    }

    protected List<Task> findTaskListByKey(String processInstanceId, String key) {
        return getTaskService().createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(key).list();
    }

    /**
     * 根据流程实例ID和前任务id值查询所有同级运行任务集合
     * 
     * @param processInstanceId
     * @param previousId
     * @return
     */
    protected List<String> findTaskListByPreviousId(String processInstanceId, String previousId) {        
        return this.actApplication.queryRuTaskIdsByProcessInstanceAndPreviousId(processInstanceId, previousId);
    }

    /**
     * 根据流程实例ID和前驱任务id值查询所有同级历史任务集合
     * 
     * @param processInstanceId
     *            实例id
     * @param previousId
     *            前驱任务id
     * @return
     */
    protected List<String> findCompletedHiTaskListByPreviousId(String processInstanceId, String previousTaskId) {
        return this.actApplication.queryHiTaskIdsByProcessInstanceAndPreviousId(processInstanceId, previousTaskId);
    }


    /**
     * 抓回任务
     */
    public Integer execute(CommandContext commandContext) {
        return 0;
    }

    /**
     * 执行控制命令
     * 
     * @param taskId
     *            任务id
     * @param variables
     *            变量
     * @param activityId
     *            目标环节id
     */
    protected void doControlCmd(String taskId, Map<String, Object> variables, String activityId) {
        if (variables == null) {
            variables = new HashMap<String, Object>(1);
        }
        // variables.put("cmd", flowControlCmd);
        // 跳转节点为空，默认提交操作
        if (StringUtil.isBlank(activityId)) {
            getTaskService().complete(taskId, variables);
        } else {// 流程转向操
            turnTransition(taskId, activityId, variables);
        }
    }

    /**
     * 流程转向操作
     * TODO 需要处理并发问题
     * 
     * @param taskId
     *            当前任务ID
     * @param activityId
     *            目标节点任务ID
     * @param variables
     *            流程变量
     * @throws Exception
     */
    protected void turnTransition(String taskId, String activityId, Map<String, Object> variables) {
        // 当前节点
        ActivityImpl currentActivity = findActivitiImpl(taskId, null);
        // 目标节点
        ActivityImpl destinationActivity = findActivitiImpl(taskId, activityId);

        synchronized (lock) {
            // 清空当前流向
            List<PvmTransition> oriPvmTransitionList = clearTransition(currentActivity);
            // 创建新流向
            TransitionImpl newTransition = currentActivity.createOutgoingTransition();
            try {
                // 设置新流向的目标节点
                newTransition.setDestination(destinationActivity);
                getTaskService().complete(taskId, variables);
            } finally {
                // 删除目标节点新流入

                destinationActivity.getIncomingTransitions().remove(newTransition);
                // 还原以前流向
                restoreTransition(currentActivity, oriPvmTransitionList);
            }
        }
    }

    /**
     * 还原指定活动节点流向
     * 
     * @param activityImpl
     *            活动节点
     * @param oriPvmTransitionList
     *            原有节点流向集合
     */
    protected void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
        // 清空现有流向
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        pvmTransitionList.clear();
        // 还原以前流向
        for (PvmTransition pvmTransition : oriPvmTransitionList) {
            pvmTransitionList.add(pvmTransition);
        }
    }

   

    /**
     * 清空指定活动节点流向
     * 
     * @param activityImpl
     *            活动节点
     * @return 节点流向集合
     */
    protected List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
        // 存储当前节点所有流向临时变量
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
        // 获取当前节点所有流向，存储到临时变量，然后清空
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        for (PvmTransition pvmTransition : pvmTransitionList) {
            oriPvmTransitionList.add(pvmTransition);
        }
        pvmTransitionList.clear();

        return oriPvmTransitionList;
    }

    /**
     * 根据任务ID获取流程定义
     * 
     * @param taskId
     *            任务ID
     * @return
     * @throws Exception
     */
    protected ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) {
        // 取得流程定义
        RepositoryService repositoryService = Context.getProcessEngineConfiguration().getRepositoryService();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(this.findTaskById(taskId)
                                                                                                                                                           .getProcessDefinitionId());

        if (processDefinition == null) {
            throw new ApplicationException("流程定义未找到!");
        }

        return processDefinition;
    }

    /**
     * 根据任务ID和节点ID获取活动节点 <br>
     * 
     * @param taskId
     *            任务ID
     * @param activityId
     *            活动节点ID <br>
     *            如果为null或""，则默认查询当前活动节点 <br>
     *            如果为"end"，则查询结束节点 <br>
     * @return
     * @throws Exception
     */
    protected ActivityImpl findActivitiImpl(String taskId, String activityId) {
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);
        // 获取当前活动节点ID
        if (StringUtil.isBlank(activityId)) {
            activityId = findTaskById(taskId).getTaskDefinitionKey();
        }
        // 根据流程定义，获取该流程实例的结束节点
        if (activityId.toUpperCase().equals("END")) {
            for (ActivityImpl activityImpl : processDefinition.getActivities()) {
                List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
                if (pvmTransitionList.isEmpty()) {
                    return activityImpl;
                }
            }
        }
        // 根据节点ID，获取对应的活动节点
        ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityId);

        return activityImpl;
    }
}
