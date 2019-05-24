package com.huigou.uasp.bpm.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.springframework.util.Assert;

import com.huigou.context.OrgUnit;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.exception.ApplicationException;
import com.huigou.express.ExpressManager;
import com.huigou.uasp.bmp.opm.OpmUtil;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.application.ProcUnitHandlerApplication;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandler;
import com.huigou.uasp.bpm.event.ProcessEventContext;
import com.huigou.util.Constants;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 协审
 * 
 * @author gongmm
 */
public class AssistCmd implements Command<Object> {
    /**
     * 添加
     */
    public static String OPERATE_ADD = "add";

    /**
     * 删除
     */
    public static String OPERATE_REMOVE = "remove";

    private String operateType;

    private String activityId;

    private List<String> executorIds;

    private List<Integer> sendMessages;

    private String processInstanceId;

    private String collectionElementVariableName;

    private CommandContext commandContext;

    private String taskId;

    private String bizId;

    private ProcUnitHandlerApplication procUnitHandlerApplication;

    private String procUnitHandlerId;

    //private ActApplication actApplication;

    public AssistCmd(String bizId, String taskId, String procUnitHandlerId, String operateType, List<String> executorIds, List<Integer> sendMessages,
                     ProcUnitHandlerApplication procUnitHandlerApplication, ActApplication actApplication) {
        this.operateType = operateType;
        this.executorIds = executorIds;
        this.sendMessages = sendMessages;
        this.bizId = bizId;
        this.taskId = taskId;
        this.procUnitHandlerId = procUnitHandlerId;
        this.procUnitHandlerApplication = procUnitHandlerApplication;
        //sthis.actApplication = actApplication;
    }

    public Object execute(CommandContext commandContext) {
        this.commandContext = commandContext;

        if (!StringUtil.isBlank(this.taskId)) {
            TaskEntity taskEntity = commandContext.getTaskEntityManager().findTaskById(taskId);
            activityId = taskEntity.getExecution().getActivityId();
            processInstanceId = taskEntity.getProcessInstanceId();
            this.collectionElementVariableName = "assignee";
            if (operateType.equalsIgnoreCase(OPERATE_ADD)) {
                addInstance(taskEntity);
            } else if (operateType.equalsIgnoreCase(OPERATE_REMOVE)) {
                removeInstance();
            }
        }
        return null;
    }

    /**
     * 添加协审
     */
    @SuppressWarnings("unchecked")
    public void addInstance(TaskEntity task) {
        List<OrgUnit> orgUnits = null;
        try {
            String express = "findPersonMembersInOrg(org,true)";
            Map<String, Object> variables = new HashMap<String, Object>(1);

            variables.put("org", executorIds);

            orgUnits = (List<OrgUnit>) ExpressManager.evaluate(express, variables);
        } catch (Exception e) {
            throw new ApplicationException("执行函数“findPersonMembersInOrg”出错。");
        }

        Assert.isTrue(orgUnits.size() > 0, "没有找到协审人员。");

        String personMemberId = null;
        for (OrgUnit orgUnit : orgUnits) {
            personMemberId = OpmUtil.getPersonMemberIdFromFullId(orgUnit.getFullId());
            boolean isPsmInHandlers = this.procUnitHandlerApplication.isPsmInHandlers(bizId, task.getTaskDefinitionKey(), personMemberId);
            Assert.isTrue(!isPsmInHandlers, String.format("审批流程中已包含执行人“%s”，不能添加协审人员。", orgUnit.getFullName()));
        }

        SDO localSdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        localSdo.putProperty("sourceTaskDescription", task.getDescription());

        String addProcUnitHandlerId;
        int sequence = 1, index;
        Integer sendMessage;

        for (OrgUnit orgUnit : orgUnits) {
            // 处理人表中添加协审人
            personMemberId = OpmUtil.getPersonMemberIdFromFullId(orgUnit.getFullId());
            index = executorIds.indexOf(personMemberId);
            if (index > -1) {
                sendMessage = this.sendMessages.get(index);
            } else {
                sendMessage = 0;
            }

            addProcUnitHandlerId = this.procUnitHandlerApplication.saveAssistant(procUnitHandlerId, personMemberId, sendMessage, sequence++);

            // 流程中添加协审人
            if (isParallel()) {
                addParallelInstance(addProcUnitHandlerId);
            } else {
                addSequentialInstance();
            }
        }
    }

    /**
     * <li>减签
     */
    public void removeInstance() {
        if (isParallel()) {
            removeParallelInstance();
        } else {
            removeSequentialInstance();
        }
    }

    /**
     * <li>添加一条并行实例
     */
    private void addParallelInstance(String addProcUnitHandlerId) {
        ExecutionEntity parentExecutionEntity = commandContext.getExecutionEntityManager().findExecutionById(processInstanceId).findExecution(activityId);

        ActivityImpl activity = getActivity();

        ExecutionEntity execution = parentExecutionEntity.createExecution();
        execution.setActive(true);
        execution.setConcurrent(true);
        execution.setScope(false);

        if (getActivity().getProperty("type").equals("subProcess")) {
            ExecutionEntity extraScopedExecution = execution.createExecution();
            extraScopedExecution.setActive(true);
            extraScopedExecution.setConcurrent(false);
            extraScopedExecution.setScope(true);
            execution = extraScopedExecution;
        }

        setLoopVariable(parentExecutionEntity, "nrOfInstances", (Integer) parentExecutionEntity.getVariableLocal("nrOfInstances") + 1);
        setLoopVariable(parentExecutionEntity, "nrOfActiveInstances", (Integer) parentExecutionEntity.getVariableLocal("nrOfActiveInstances") + 1);
        setLoopVariable(execution, "loopCounter", parentExecutionEntity.getExecutions().size() + 1);
        setLoopVariable(execution, collectionElementVariableName, addProcUnitHandlerId.toString());
        execution.executeActivity(activity);
    }

    /**
     * <li>给串行实例集合中添加一个审批人
     */
    private void addSequentialInstance() {
        /*
         * ExecutionEntity execution = getActivieExecutions().get(0);
         * if (getActivity().getProperty("type").equals("subProcess")) {
         * if (!execution.isActive() && execution.isEnded() && ((execution.getExecutions() == null) || (execution.getExecutions().size() == 0))) {
         * execution.setActive(true);
         * }
         * }
         * Collection<String> col = (Collection<String>) execution.getVariable(collectionVariableName);
         * col.add(executors);
         * execution.setVariable(collectionVariableName, col);
         * setLoopVariable(execution, "nrOfInstances", (Integer) execution.getVariableLocal("nrOfInstances") + 1);
         */
    }

    /**
     * <li>移除一条并行实例
     */
    private void removeParallelInstance() {
        List<ExecutionEntity> executions = getActiveExecutions();

        ProcUnitHandler procUnitHandler = procUnitHandlerApplication.loadProcUnitHandler(procUnitHandlerId);
        if (procUnitHandler == null) {
            throw new ApplicationException("未找到要删除的协审人员。");
        }

        if (procUnitHandler.getStatus() == 1) {
            throw new ApplicationException("协审人员已完成当前任务，不能删除。");
        }

        procUnitHandlerApplication.deleteProcUnitHandler(procUnitHandlerId);

        for (ExecutionEntity executionEntity : executions) {
            String executionVariableAssignee = (String) executionEntity.getVariableLocal(collectionElementVariableName);

            if ((executionVariableAssignee != null) && executionVariableAssignee.equals(procUnitHandlerId.toString())) {
                executionEntity.remove();

                ExecutionEntity parentConcurrentExecution = executionEntity.getParent();

                if (getActivity().getProperty("type").equals("subProcess")) {
                    parentConcurrentExecution = parentConcurrentExecution.getParent();
                }

                setLoopVariable(parentConcurrentExecution, "nrOfInstances", (Integer) parentConcurrentExecution.getVariableLocal("nrOfInstances") - 1);
                setLoopVariable(parentConcurrentExecution, "nrOfActiveInstances",
                                (Integer) parentConcurrentExecution.getVariableLocal("nrOfActiveInstances") - 1);

                List<TaskEntity> taskEntities = executionEntity.getTasks();
                for (TaskEntity taskEntity : taskEntities) {
                    ProcessEventContext.addCompletedTask(taskEntity.getId());
                }

                break;
            }
        }
    }

    /**
     * <li>冲串行列表中移除未完成的用户(当前执行的用户无法移除)
     */
    private void removeSequentialInstance() {
        /*
         * ExecutionEntity executionEntity = getActivieExecutions().get(0);
         * Collection<String> col = (Collection<String>) executionEntity.getVariable(collectionVariableName);
         * log.info("移除前审批列表 : {}", col.toString());
         * col.remove(executorI);
         * executionEntity.setVariable(collectionVariableName, col);
         * setLoopVariable(executionEntity, "nrOfInstances", (Integer) executionEntity.getVariableLocal("nrOfInstances") - 1);
         * // 如果串行要删除的人是当前active执行,
         * if (executionEntity.getVariableLocal(collectionElementVariableName).equals(executorI)) {
         * throw new ActivitiException("当前正在执行的实例,无法移除!");
         * }
         * log.info("移除后审批列表 : {}", col.toString());
         */
    }

    /**
     * <li>获取活动的执行 , 子流程的活动执行是其孩子执行(并行多实例情况下) <li>串行情况下获取的结果数量为1
     */
    protected List<ExecutionEntity> getActiveExecutions() {
        List<ExecutionEntity> activeExecutions = new ArrayList<ExecutionEntity>();
        ActivityImpl activity = getActivity();
        List<ExecutionEntity> executions = getChildExecutionByProcessInstanceId();

        for (ExecutionEntity execution : executions) {
            if (execution.isActive() && (execution.getActivityId().equals(activityId) || activity.contains(execution.getActivity()))) {
                activeExecutions.add(execution);
            }
        }

        return activeExecutions;
    }

    /**
     * 获取流程实例根的所有子执行
     * 
     * @return
     */
    protected List<ExecutionEntity> getChildExecutionByProcessInstanceId() {
        return commandContext.getExecutionEntityManager().findChildExecutionsByProcessInstanceId(processInstanceId);
    }

    /**
     * 返回当前节点对象
     * 
     * @return
     */
    protected ActivityImpl getActivity() {
        return this.getProcessDefinition().findActivity(activityId);
    }

    /**
     * 判断节点多实例类型是否是并发
     * 
     * @return
     */
    protected boolean isParallel() {
        return getActivity().getProperty("multiInstance").equals("parallel");
    }

    /**
     * 返回流程定义对象
     * 
     * @return
     */
    protected ProcessDefinitionImpl getProcessDefinition() {
        return this.getProcessInstanceEntity().getProcessDefinition();
    }

    /**
     * 返回流程实例的根执行对象
     * 
     * @return
     */
    protected ExecutionEntity getProcessInstanceEntity() {
        return commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);
    }

    /**
     * 添加本地变量
     * 
     * @param execution
     * @param variableName
     * @param value
     */
    protected void setLoopVariable(ActivityExecution execution, String variableName, Object value) {
        execution.setVariableLocal(variableName, value);
    }
}
