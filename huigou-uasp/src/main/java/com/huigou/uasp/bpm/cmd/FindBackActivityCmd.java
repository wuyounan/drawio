package com.huigou.uasp.bpm.cmd;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
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
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.exception.ApplicationException;
import com.huigou.util.StringUtil;

/**
 * 查询回退环节
 * 
 * @author gongmm
 */
public class FindBackActivityCmd implements Command<List<ActivityImpl>> {
    private String taskId;

    /**
     * 当前回退的环节
     */
    private ActivityImpl currentBackActivity;

    public FindBackActivityCmd(String taskId) {
        this.taskId = taskId;
    }

    /**
     * 根据任务ID获得任务实例
     * 
     * @param taskId
     *            任务ID
     * @return
     * @throws Exception
     */
    private TaskEntity findTaskById(String taskId) throws Exception {
        TaskEntity task = Context.getCommandContext().getTaskEntityManager().findTaskById(taskId);
        Assert.notNull(task, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "任务"));        
        return task;
    }

    /**
     * 根据任务ID获取流程定义
     * 
     * @param taskId
     *            任务ID
     * @return
     * @throws Exception
     */
    private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) throws Exception  {
        // 取得流程定义
        RepositoryService repositoryService = Context.getProcessEngineConfiguration().getRepositoryService();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(this.findTaskById(taskId)
                                                                                                                                                           .getProcessDefinitionId());
        if (processDefinition == null) {
            throw new ApplicationException("流程定义未找到。");
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
    private ActivityImpl findActivitiImpl(String taskId, String activityId) throws Exception {
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

    /**
     * 迭代循环流程树结构，查询当前节点可驳回的任务节点
     * 
     * @param taskId
     *            当前任务ID
     * @param currentActivity
     *            当前活动节点
     * @param resultList
     *            存储回退节点集合
     * @param tempList
     *            临时存储节点集合（存储一次迭代过程中的同级userTask节点）
     * @return 回退节点集合
     */
    private List<ActivityImpl> iteratorBackActivity(String taskId, ActivityImpl currentActivity, List<ActivityImpl> resultList, List<ActivityImpl> tempList) throws Exception {
        // 查询流程定义，生成流程树结构
        ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);

        // 当前节点的流入来源
        List<PvmTransition> incomingTransitions = currentActivity.getIncomingTransitions();
        // 条件分支节点集合，userTask节点遍历完毕，迭代遍历此集合，查询条件分支对应的userTask节点
        List<ActivityImpl> exclusiveGateways = new ArrayList<ActivityImpl>();
        // 并行节点集合，userTask节点遍历完毕，迭代遍历此集合，查询并行节点对应的userTask节点
        List<ActivityImpl> parallelGateways = new ArrayList<ActivityImpl>();
        // 遍历当前节点所有流入路径
        for (PvmTransition pvmTransition : incomingTransitions) {
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
            ActivityImpl activityImpl = transitionImpl.getSource();
            String type = (String) activityImpl.getProperty("type");
            /**
             * 并行节点配置要求：<br>
             * 必须成对出现，且要求分别配置节点ID为:XXX_start(开始)，XXX_end(结束)
             */
            if ("parallelGateway".equals(type)) {// 并行路线
                String gatewayId = activityImpl.getId();
                String gatewayType = gatewayId.substring(gatewayId.lastIndexOf("_") + 1);
                if ("START".equals(gatewayType.toUpperCase())) {// 并行起点，停止递归
                    return resultList;
                } else {// 并行终点，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点
                    parallelGateways.add(activityImpl);
                }
            } else if ("startEvent".equals(type)) {// 开始节点，停止递归
                return resultList;
            } else if ("userTask".equals(type)) {// 用户任务
                /**
                 * 循环审批
                 */
                if (!currentBackActivity.getId().equalsIgnoreCase(activityImpl.getId())) tempList.add(activityImpl);
            } else if ("exclusiveGateway".equals(type)) {// 分支路线，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点
                currentActivity = transitionImpl.getSource();
                exclusiveGateways.add(currentActivity);
            }
        }

        /**
         * 迭代条件分支集合，查询对应的userTask节点
         */
        for (ActivityImpl activityImpl : exclusiveGateways) {
            iteratorBackActivity(taskId, activityImpl, resultList, tempList);
        }

        /**
         * 迭代并行集合，查询对应的userTask节点
         */
        for (ActivityImpl activityImpl : parallelGateways) {
            iteratorBackActivity(taskId, activityImpl, resultList, tempList);
        }

        /**
         * 根据同级userTask集合，过滤最近发生的节点
         */
        currentActivity = filterNewestActivity(processInstance, tempList);
        if (currentActivity != null) {
            // 查询当前节点的流向是否为并行终点，并获取并行起点ID
            String id = findParallelGatewayId(currentActivity);
            if (StringUtil.isBlank(id)) {// 并行起点ID为空，此节点流向不是并行终点，符合驳回条件，存储此节点
                resultList.add(currentActivity);
            } else {// 根据并行起点ID查询当前节点，然后迭代查询其对应的userTask任务节点
                currentActivity = findActivitiImpl(taskId, id);
            }
            // 清空本次迭代临时集合
            tempList.clear();
            // 执行下次迭代
            iteratorBackActivity(taskId, currentActivity, resultList, tempList);
        }
        return resultList;
    }

    /**
     * 根据当前节点，查询输出流向是否为并行终点，如果为并行终点，则拼装对应的并行起点ID
     * 
     * @param activityImpl
     *            当前节点
     * @return
     */
    private String findParallelGatewayId(ActivityImpl activityImpl) {
        List<PvmTransition> incomingTransitions = activityImpl.getOutgoingTransitions();
        for (PvmTransition pvmTransition : incomingTransitions) {
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
            activityImpl = transitionImpl.getDestination();
            String type = (String) activityImpl.getProperty("type");
            if ("parallelGateway".equals(type)) {// 并行路线
                String gatewayId = activityImpl.getId();
                String gatewayType = gatewayId.substring(gatewayId.lastIndexOf("_") + 1);
                if ("END".equals(gatewayType.toUpperCase())) {
                    return gatewayId.substring(0, gatewayId.lastIndexOf("_")) + "_start";
                }
            }
        }
        return null;
    }

    /**
     * 根据流入任务集合，查询最近一次的流入任务节点
     * 
     * @param processInstance
     *            流程实例
     * @param activityList
     *            流入任务集合
     * @return
     */
    private ActivityImpl filterNewestActivity(ProcessInstance processInstance, List<ActivityImpl> activityList) {
        while (activityList.size() > 0) {
            ActivityImpl activity1 = activityList.get(0);
            HistoricActivityInstance activityInstance1 = findHistoricUserTask(processInstance, activity1.getId());
            if (activityInstance1 == null) {
                activityList.remove(activity1);
                continue;
            }

            if (activityList.size() > 1) {
                ActivityImpl activity2 = activityList.get(1);
                HistoricActivityInstance activityInstance2 = findHistoricUserTask(processInstance, activity2.getId());
                if (activityInstance2 == null) {
                    activityList.remove(activity2);
                    continue;
                }

                if (activityInstance1.getEndTime().before(activityInstance2.getEndTime())) {
                    activityList.remove(activity1);
                } else {
                    activityList.remove(activity2);
                }
            } else {
                break;
            }
        }
        if (activityList.size() > 0) {
            return activityList.get(0);
        }
        return null;
    }

    /**
     * 查询指定任务节点的最新记录
     * 
     * @param processInstance
     *            流程实例
     * @param activityId
     * @return
     */
    private HistoricActivityInstance findHistoricUserTask(ProcessInstance processInstance, String activityId) {
        HistoricActivityInstance result = null;
        // 查询当前流程实例审批结束的历史节点
        List<HistoricActivityInstance> historicActivityInstances = Context.getProcessEngineConfiguration().getHistoryService()
                                                                          .createHistoricActivityInstanceQuery().activityType("userTask")
                                                                          .processInstanceId(processInstance.getId()).activityId(activityId).finished()
                                                                          .orderByHistoricActivityInstanceEndTime().desc().list();

        if (historicActivityInstances.size() > 0) {
            result = historicActivityInstances.get(0);
        }

        return result;
    }

    /**
     * 根据任务ID获取对应的流程实例
     * 
     * @param taskId
     *            任务ID
     * @return
     * @throws Exception
     */
    private ProcessInstance findProcessInstanceByTaskId(String taskId) throws Exception {
        // 找到流程实例
        ProcessInstance processInstance = Context.getProcessEngineConfiguration().getRuntimeService().createProcessInstanceQuery()
                                                 .processInstanceId(findTaskById(taskId).getProcessInstanceId()).singleResult();

        if (processInstance == null) {
            throw new Exception("流程实例未找到!");
        }
        return processInstance;
    }

    /**
     * 反向排序list集合，便于驳回节点按顺序显示
     * 
     * @param list
     * @return
     */
    private List<ActivityImpl> reverseList(List<ActivityImpl> list) {
        List<ActivityImpl> result = new ArrayList<ActivityImpl>();
        // 由于迭代出现重复数据，排除重复
        for (int i = list.size() - 1; i >= 0; i--) {
            if (!result.contains(list.get(i))) result.add(list.get(i));
        }
        return result;
    }

    public List<ActivityImpl> execute(CommandContext commandContext) {
        List<ActivityImpl> result = null;
        try {
            currentBackActivity = findActivitiImpl(taskId, null);
            result = iteratorBackActivity(taskId, currentBackActivity, new ArrayList<ActivityImpl>(), new ArrayList<ActivityImpl>());
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
        return reverseList(result);
    }
}
