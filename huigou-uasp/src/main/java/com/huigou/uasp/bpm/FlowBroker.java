package com.huigou.uasp.bpm;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.OrgUnit;
import com.huigou.context.PersonMember;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.context.User;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.fn.impl.OrgFun;
import com.huigou.uasp.bmp.fn.impl.ProcessFun;
import com.huigou.uasp.bmp.opm.OpmUtil;
import com.huigou.uasp.bmp.opm.domain.model.agent.Agent;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.Person;
import com.huigou.uasp.bmp.opm.proxy.AgentApplicationProxy;
import com.huigou.uasp.bmp.opm.proxy.OrgApplicationProxy;
import com.huigou.uasp.bpm.configuration.application.ApprovalRuleApplication;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRule;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandler;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandlerAssist;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandlerGroup;
import com.huigou.uasp.bpm.configuration.domain.model.HandlerKind;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.application.HandlerParseService;
import com.huigou.uasp.bpm.engine.application.ProcApprovalRuleParseService;
import com.huigou.uasp.bpm.engine.application.ProcUnitHandlerApplication;
import com.huigou.uasp.bpm.engine.application.WorkflowApplication;
import com.huigou.uasp.bpm.engine.domain.model.ApproveNotPassedHandleKind;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceExtension;
import com.huigou.uasp.bpm.engine.domain.model.LimitTime;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandler;
import com.huigou.uasp.bpm.engine.domain.model.RuntimeTaskExtension;
import com.huigou.uasp.bpm.event.ProcessEventContext;
import com.huigou.uasp.bpm.managment.application.ProcDefinitionApplication;
import com.huigou.uasp.bpm.managment.domain.model.ProcDefinition;
import com.huigou.util.ClassHelper;
import com.huigou.util.CommonUtil;
import com.huigou.util.Constants;
import com.huigou.util.DateUtil;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;
import com.huigou.util.Util;

public class FlowBroker extends BaseApplication implements TaskListener, ExecutionListener {

    private static final long serialVersionUID = -6575998537083218220L;

    private static final String PREEMPT_CANCEL_VARIABLE = "preemptCancel";
    /**
     * @since 1.1.3
     */
    private static final String LIMIT_HANDLER_CANCEL_VARIABLE = "limitHandlerCancel";

    private static final String CHIEF_APPROVE_PASSED_VARIABLE = "chiefApprovePassed";

    protected static final String APPROVE_FINISHED_VARIABLE = "approveFinished";

    protected static final String APPROVE_PASSED_VARIABLE = "approvePassed";

    private static final String ABORT_TASK_ID_VARIABLE = "_abortTaskId_";

    private static final String APPROVE_NOT_PASSED_BACK_KEY = "approveNotPassedBack";

    private static final String PROC_UNIT_SEQUENCE_FIELD = "procUnitSequence";

    private static final String GROUP_ID_FIELD = "groupId";

    private static final String SEQUENCE_FIELD = "sequence";

    private static final String SEND_MESSAGE_VARIABLE = "_sendMessage_";

    private static final String PROC_UNIT_HANDLER_ID = "_procUnitHandlerId_";

    protected static final String FIRE_PROC_UNIT_HANDLER_ID = "_fireEventProcUnitHandlerId_";

    private static final String HAS_SELECTION_FIELD = "hasSelection";

    private static final String HAS_GATEWAY_MANUAL_FIELD = "hasGatewayManual";

    private static final String PROC_APPROVAL_RULE_FULL_NAME_FIELD = "procApprovalRuleFullName";

    private static final String QUERY_HANDLER_SHOW_FIELD = "queryHandlerShowField";

    private static final String IS_MAKE_A_COPY_FOR = "isMakeACopyFor";

    @Resource
    protected OrgApplicationProxy orgApplication;

    @Resource
    protected ProcUnitHandlerApplication procUnitHandlerApplication;

    @Resource
    protected WorkflowApplication workflowService;

    @Resource
    protected ProcApprovalRuleParseService procApprovalRuleParseService;

    @Resource
    protected ActApplication actApplication;

    @Autowired
    protected AgentApplicationProxy agentApplication;

    @Resource
    protected OrgFun orgFun;

    @Resource
    private ProcessFun processFun;

    @Resource
    private HandlerParseService handlerParseService;

    @Resource
    private ApprovalRuleApplication approvalRuleApplication;

    @Autowired
    private ProcDefinitionApplication procDefinitionApplication;

    @Autowired
    protected MessageSenderManager messageSenderManager;

    /**
     * 流程类别
     */
    private ProcessKind flowKind = ProcessKind.APPROVAL;

    public ProcessKind getFlowKind() {
        return flowKind;
    }

    public void setFlowKind(ProcessKind flowKind) {
        this.flowKind = flowKind;
    }

    /**
     * 得到业务和审批数据
     *
     * @return 业务和审批数据
     */
    protected SDO getBizAndApprovalData() {
        return ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
    }

    /**
     * 得到业务实体
     *
     * @param cls
     * @return
     */
    protected <T> T getBizEntity(Class<T> cls) {
        SDO sdo = this.getBizAndApprovalData();
        if (sdo != null) {
            return (T) sdo.toObject(cls);
        }
        return null;
    }

    /**
     * 得到业务实体明细
     *
     * @param cls
     * @param key
     * @return
     */
    protected <T> List<T> getBizEntities(Class<T> cls, String key) {
        SDO sdo = this.getBizAndApprovalData();
        if (sdo != null) {
            return sdo.getList(key, cls);
        }
        return null;
    }

    /**
     * 得到任务ID
     *
     * @return 任务ID
     */
    protected String getNewTaskId() {
        return ClassHelper.convert(this.getBizAndApprovalData().getProperty("newTaskId"), String.class);
    }

    protected String getTaskId() {
        return getApprovalParameter().getTaskId();
    }

    protected String getBizId() {
        return getApprovalParameter().getBizId();
    }

    protected String getBizCode() {
        return getApprovalParameter().getBizCode();
    }

    protected Integer getCurrentHandleGroupId() {
        return getApprovalParameter().getCurrentHandleGroupId();
    }

    protected String getCurrentHandleId() {
        return getApprovalParameter().getCurrentHandleId();
    }

    /**
     * 设置任务ID
     *
     * @param taskId 任务ID
     */
    protected void setNewTaskId(String taskId) {
        getBizAndApprovalData().putProperty("newTaskId", taskId);
    }

    /**
     * 得到申请人
     *
     * @return 申请人
     */
    protected String getApplicantName() {
        return ClassHelper.convert(this.getBizAndApprovalData().getProperty("personMemberName"), String.class);
    }

    /**
     * 得到填单日期
     *
     * @return 填单日期
     */
    protected String getFillInDate() {
        Date fillInDate = ClassHelper.convert(this.getBizAndApprovalData().getProperty("fillinDate"), Date.class);
        return DateUtil.getDateFormat(3, fillInDate);
    }

    protected Operator getOperator() {
        return ThreadLocalUtil.getOperator();
    }

    protected String getOrgFullId() {
        return ClassHelper.convert(this.getBizAndApprovalData().getProperty("fullId"), String.class);
    }

    protected boolean loopApprovalFinished(DelegateTask delegateTask) {
        return ClassHelper.convert(delegateTask.getVariable(APPROVE_FINISHED_VARIABLE), Boolean.class, false);
    }

    protected boolean getCurrentGroupChiefApprovePassed(DelegateTask delegateTask) {
        return ClassHelper.convert(delegateTask.getVariable(CHIEF_APPROVE_PASSED_VARIABLE), Boolean.class, false);
    }

    protected boolean isSpecifiedProcUnit(DelegateTask delegateTask, String procUnitName) {
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        return taskDefinitionKey.toLowerCase().contains(procUnitName);
    }

    /**
     * 当前任务是否为审批环节
     *
     * @param delegateTask 代理任务
     * @return 当前任务是否为审批环节
     */
    protected boolean isApprovalProcUnit(DelegateTask delegateTask) {
        return isSpecifiedProcUnit(delegateTask, ActivityKind.APPROVE);
    }

    /**
     * 当前任务是否为审批环节
     *
     * @param procUnitId 环节id
     * @return 当前任务是否为审批环节
     */
    protected boolean isApprovalProcUnit(String procUnitId) {
        return ActivityKind.APPROVE.equalsIgnoreCase(procUnitId);
    }

    /**
     * currentGroupChiefApprovePassed
     * 流程通知事件处理
     * <ul>
     * <li>流程实例启动事件：调用<code>onStart</code>保存业务数据和历史流程实例扩展数据。</li>
     * <li>流程实例结束事件：
     * <ul>
     * <li>删除流程实例：更新历史流程实例扩展状态，回调<code>onDeleteProcessInstance</code></li>
     * <li>终止流程实例：更新历史流程实例扩展状态，回调<code>onAbortProcessInstance</code></li>
     * <li>正常结束流程实例：更新历史流程实例扩展状态，回调<code>onEnd</code></li>
     * </ul>
     * <li>流程实例撤销事件</li>
     * </ul>
     */
    @Override
    public void notify(DelegateExecution delegateExecution) {
        String eventName = delegateExecution.getEventName();
        try {
            switch (eventName) {
                case ExecutionListener.EVENTNAME_START:
                    getApprovalParameter().setTaskId("");
                    this.onStart(delegateExecution);
                    break;
                case ExecutionListener.EVENTNAME_END:
                    // 1 parentExecution
                    // 2 childExecution1 parent(1)
                    // 3 childExecution2 parent(1)
                    // 终止流程 调用顺序 （3 2） 1
                    if (StringUtil.isNotBlank(delegateExecution.getParentId())) {
                        return;
                    }
                    String processInstanceId = delegateExecution.getProcessInstanceId();
                    String currentActivityId = delegateExecution.getCurrentActivityId();

                    if (getApprovalParameter().isSpecifiedProcessAction(ProcessAction.DELETE_PROCESS_INSTANCE)) { // 删除流程实例
                        this.actApplication.updateHistoricProcessInstanceExtensionEnded(processInstanceId, ProcessStatus.DELETED,
                                ProcessAction.DELETE_PROCESS_INSTANCE, currentActivityId);
                        onDeleteProcessInstance(delegateExecution);
                    } else if (getApprovalParameter().isSpecifiedProcessAction(ProcessAction.ABORT_PROCESS_INSTANCE)) {// 终止流程实例
                        if (getApprovalParameter().getProcUnitId() != null) {// 在流程处理页面中终止，否则在维护页面中终止
                            if (isAbortSaveBizData()) {
                                this.saveBizAndApprovalData();
                            } else {
                                this.updateProcUnitHandlerResult(this.getTaskId());
                            }
                        }
                        this.actApplication.updateHistoricProcessInstanceExtensionEnded(processInstanceId, ProcessStatus.ABORTED,
                                ProcessAction.ABORT_PROCESS_INSTANCE, currentActivityId);
                        this.onAbortProcessInstance(delegateExecution);
                    } else {
                        this.actApplication.updateHistoricProcessInstanceExtensionEnded(processInstanceId, ProcessStatus.COMPLETED, null, currentActivityId);
                        this.onEnd(delegateExecution);
                    }
                    break;
                case ExecutionListenerExt.EVENTNAME_RECALL_PROCESS_INSTANCE:
                    onRecallProcessInstance(delegateExecution);
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage(), ex);
        }
    }

    /**
     * 任务通知事件
     * <ul>
     * <li>创建任务事件</li>
     * <li>分配处理人事件</li>
     * <li>完成任务事件</li>
     * <li>删除任务事件</li>
     * <li>保存业务数据事件</li>
     * <li>预览处理人事件</li>
     * </ul>
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        try {
            switch (eventName) {
                case TaskListenerExt.EVENTNAME_CREATE:
                    this.onCreate(delegateTask);
                    break;
                case TaskListenerExt.EVENTNAME_ASSIGNMENT:
                    this.onAssignment(delegateTask);
                    break;
                case TaskListenerExt.EVENTNAME_COMPLETE:
                    this.onComplete(delegateTask);
                    break;
                case TaskListenerExt.EVENTNAME_DELETE:
                    this.onDelete(delegateTask);
                    break;
                case TaskListenerExt.EVENTNAME_SAVE_BIZ_DATA:
                    // 只保存处理人数据
                    if (getApprovalParameter().getOnlySaveHandlerData()) {
                        this.updateProcUnitHandlerResult();
                    } else {
                        this.onSaveBizData(delegateTask);
                    }
                    break;
                case TaskListenerExt.EVENTNAME_QUERY_HANDLERS:
                    this.onQueryHandlers(delegateTask);
                    break;
                case TaskListenerExt.EVENTNAME_CHECK_CONSTRAINTS:
                    this.onCheckConstraints(delegateTask);
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage(), ex);
        }
    }

    /**
     * 审批是否完成
     *
     * @param procUnitId 流程环节ID
     * @return 审批是否完成
     */
    protected boolean approveFinished(String procUnitId) {
        return procUnitHandlerApplication.checkChiefApproveFinished(getApprovalParameter().getBizId(), procUnitId);
    }

    /**
     * 当前审批人审批是否通过
     * <p>
     * 不为主审，或者审批通过：审批为"主审", 审批意见为"同意", "已阅"
     *
     * @return 当前审批人审批是否通过
     */
    protected boolean approvePassed() {
        return !CooperationModelKind.CHIEF.equalsIgnoreCase(getApprovalParameter().getCurrentHandleCooperationModelId())
                || getApprovalParameter().getHandleResult() != HandleResult.DISAGREE.getId();
    }

    /**
     * 当前组主审人审批是否通过
     *
     * @return 当前组主审人审批是否通过
     */
    protected boolean currentGroupChiefApprovePassed(String procUnitId) {
        int groupId = getCurrentHandleGroupId();
        if (this.procUnitHandlerApplication.checkCurrentGroupChiefApprovePassed(getApprovalParameter().getBizId(), procUnitId, groupId)) {
            return true;
        }
        ProcUnitHandler procUnitHandler = procUnitHandlerApplication.queryProcUnitHandlers(getApprovalParameter().getBizId(), procUnitId, groupId).get(0);
        return isLimitHandler(procUnitId, groupId, procUnitHandler.getLimitHandler());
    }

    private String getTaskDescriptionPrefix() {
        String result = "";
        if (getApprovalParameter().isWithdrawProcessAction()) {
            result = "回收";
        } else if (getApprovalParameter().isTransmitProcessAction()) {
            result = getOperator().getPersonMemberName() + "转交给您的任务";
        } else if (getApprovalParameter().isAssistProcessAction()) {
            result = "协审";
        } else if (getApprovalParameter().isBackProcessAction()) {
            result = "回退";
        } else if (getApprovalParameter().isRecallProcessInstanceProcessAction()) {
            result = "撤销";
        }
        return result;
    }

    private String getTaskDescription() {
        return this.getBizAndApprovalData().getProperty("sourceTaskDescription", String.class);
    }

    /**
     * 设置默认任务主题
     *
     * @param delegateTask 代理任务
     */
    protected void setTaskDescription(DelegateTask delegateTask) {

    }

    /**
     * 处理设置任务处理人信息（非申请环节和审批环节）
     *
     * @param delegateTask         代理任务
     * @param runtimeTaskExtension 运行时任务扩展数据
     */
    protected void doOtherSetCurrentTaskHandler(DelegateTask delegateTask, RuntimeTaskExtension runtimeTaskExtension) {

    }

    /**
     * 应用代理规则
     *
     * @param delegateTask    代理任务
     * @param procUnitHandler 环节处理人
     * @return
     */
    protected Agent applyAgentRule(DelegateTask delegateTask, ProcUnitHandler procUnitHandler) {
        Agent agent = this.agentApplication.loadValidAgent(procUnitHandler.getHandlerId());
        if (agent != null) {
            boolean canApplyAgent;
            if (agent.getProcAgentKind() == Agent.ProcAgentKind.ALL) {
                canApplyAgent = true;
            } else {
                String procId = processFun.getProcessKeyFromProcessDefinitionId(delegateTask.getProcessDefinitionId());
                ProcDefinition procDefinition = this.procDefinitionApplication.loadProcDefinitionByProcId(procId);
                Assert.notNull(procDefinition, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, procId, "流程定义"));
                canApplyAgent = agent.isDetailsContains(procDefinition.getId());
            }

            if (canApplyAgent) {
                // 修改公共流程任务处理表中任务处理人为代理人
                Org org = this.orgApplication.loadOrg(agent.getAgent().getId());
                OrgUnit orgUnit = new OrgUnit(org.getFullId(), org.getFullName());
                this.procUnitHandlerApplication.updateProcUnitHandlerOrgData(procUnitHandler.getId(), orgUnit);
                return agent;
            }
        }
        return null;
    }

    /**
     * 填充申请人扩展数据
     *
     * @param runtimeTaskExtension 运行时任务扩展数据
     */
    private void fillApplicantExtendData(RuntimeTaskExtension runtimeTaskExtension) {
        SDO sdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        String processInstanceId = sdo.getProperty(ProcessTaskContants.PROC_INST_ID, String.class);
        Map<String, Object> data = this.actApplication.queryApplicantByProcessInstanceId(processInstanceId);
        Assert.isTrue(data.size() > 0, "未找到起始环节执行人。");

        String executorFullId = ClassHelper.convert(data.get(ProcessTaskContants.EXECUTOR_FULL_ID), String.class);
        String executorFullName = ClassHelper.convert(data.get(ProcessTaskContants.EXECUTOR_FULL_NAME), String.class);
        String businessCode = ClassHelper.convert(data.get(ProcessTaskContants.BUSINESS_CODE), String.class);

        runtimeTaskExtension.setExecutorFullId(executorFullId);
        runtimeTaskExtension.setExecutorFullName(executorFullName);
        runtimeTaskExtension.setBusinessCode(businessCode);

    }

    /**
     * 设置任务处理人信息
     * <p>
     * 1、设置默认主题
     * <p>
     * 2、设置任务的出人
     * <p>
     * 3、设置扩展属性的处理人
     *
     * @param delegateTask         代理任务
     * @param runtimeTaskExtension 运行时任务扩展数据
     */
    protected void setCurrentTaskHandler(DelegateTask delegateTask, RuntimeTaskExtension runtimeTaskExtension) {
        String processDefinitionId = delegateTask.getExecution().getProcessDefinitionId();
        ProcessDefinition pd = getRepositoryService().getProcessDefinition(processDefinitionId);

        String taskDescription = null;
        String procUnitHandlerId;

        ApprovalParameter approvalParameter = getApprovalParameter();

        if (this.isApplyProcUnit(delegateTask)) {
            // 回退 抓回
            if (approvalParameter.isBackProcessAction() || approvalParameter.isWithdrawProcessAction()) {
                taskDescription = pd.getName();
                // 起始环节执行人
                fillApplicantExtendData(runtimeTaskExtension);
                if (approvalParameter.isBackProcessAction()) {
                    delegateTask.setVariable(SEND_MESSAGE_VARIABLE, 1);
                }
            } else {
                taskDescription = pd.getName();
                String startModel = (String) delegateTask.getVariable("startModel");
                startModel = StringUtil.isBlank(startModel) ? ProcessStartModel.MANUAL.getId() : startModel;
                if (startModel.equalsIgnoreCase(ProcessStartModel.AUTOMATIC.getId())) {
                    // 自动模式 设置启动人
                    Object initiatorPersonMemberIdVariable = delegateTask.getVariable(ProcessTaskContants.INITIATOR_PERSONMEMBER_ID);
                    if (initiatorPersonMemberIdVariable != null) {
                        String initiatorPersonMemberId = ClassHelper.convert(initiatorPersonMemberIdVariable, String.class);
                        runtimeTaskExtension.setInitiatorPersonMemberId(initiatorPersonMemberId);
                        delegateTask.setAssignee(initiatorPersonMemberId);
                    } else {
                        Object executorFullIdVariable = delegateTask.getVariable(ProcessTaskContants.EXECUTOR_FULL_ID);
                        String executorFullId = ClassHelper.convert(executorFullIdVariable, String.class);

                        Object executorFullNameVariable = delegateTask.getVariable(ProcessTaskContants.EXECUTOR_FULL_NAME);
                        String executorFullName = ClassHelper.convert(executorFullNameVariable, String.class);

                        delegateTask.setAssignee(executorFullId);
                        runtimeTaskExtension.setExecutorFullId(executorFullId);
                        runtimeTaskExtension.setExecutorFullName(executorFullName);
                    }
                } else {
                    // 审批不通过回退
                    boolean approveNotPassedBack = ClassHelper.convert(delegateTask.getVariable(APPROVE_NOT_PASSED_BACK_KEY), Boolean.class, false);
                    if (approveNotPassedBack) {
                        fillApplicantExtendData(runtimeTaskExtension);
                        delegateTask.setVariable(APPROVE_NOT_PASSED_BACK_KEY, "");
                        delegateTask.setVariable(SEND_MESSAGE_VARIABLE, 1);
                    } else {
                        delegateTask.setAssignee(getOperator().getFullId());
                        runtimeTaskExtension.setExecutorFullId(getOperator().getFullId());
                        runtimeTaskExtension.setExecutorFullName(getOperator().getFullName());
                    }
                }
            }
            delegateTask.setDescription(taskDescription);
        } else if (isApprovalProcUnit(delegateTask)) {
            if (approvalParameter.isTransmitProcessAction() || approvalParameter.isAssistProcessAction() || approvalParameter.isBackProcessAction()
                    || approvalParameter.isReplenishProcessAction() || approvalParameter.isWithdrawProcessAction()
                    || approvalParameter.isRecallProcessInstanceProcessAction()) {
                // 转交、协审、 回退等没有传UI数据
                HistoricTaskInstanceExtension hiTaskInstExtension = this.actApplication.loadHistoricTaskInstanceExtension(approvalParameter.getTaskId());
                runtimeTaskExtension.setBusinessCode(hiTaskInstExtension.getBusinessCode());

                taskDescription = getTaskDescription();
            }

            // 当前环节的处理人
            procUnitHandlerId = delegateTask.getAssignee();

            ProcUnitHandler procUnitHandler = procUnitHandlerApplication.loadProcUnitHandler(procUnitHandlerId);
            if (procUnitHandler == null) {
                throw new ApplicationException("流程流转时，未找到审批人。");
            }
            runtimeTaskExtension.setProcUnitHandler(procUnitHandler);
            runtimeTaskExtension.setProcUnitHandlerId(procUnitHandlerId);
            runtimeTaskExtension.setSubProcUnitName(procUnitHandler.getSubProcUnitName());
            // 检查人员状态
            String personId = OpmUtil.getPersonIdFromPersonMemberId(procUnitHandler.getHandlerId());
            orgApplication.checkPersonIsEnabled(personId);
            // 应用代理规则 applyAgentRule
            Agent agent = applyAgentRule(delegateTask, procUnitHandler);
            delegateTask.setAssignee(procUnitHandler.getFullId());
            runtimeTaskExtension.setExecutorFullId(procUnitHandler.getFullId());
            runtimeTaskExtension.setExecutorFullName(procUnitHandler.getFullName());
            runtimeTaskExtension.setProcUnitHandlerId(procUnitHandlerId);
            runtimeTaskExtension.setSubProcUnitName(procUnitHandler.getSubProcUnitName());
            runtimeTaskExtension.setCooperationModelId(procUnitHandler.getCooperationModelId());

            if (approvalParameter.isAdvanceProcessAction() || approvalParameter.isReplenishProcessAction()) {
                taskDescription = String.format("%s%s(%s) %s", procUnitHandler.getSubProcUnitName(), getApplicantName(), getFillInDate(), pd.getName());
            }
            // 代理
            if (agent != null) {
                runtimeTaskExtension.setAgent(agent);
                taskDescription += String.format("(“%s”代理“%s”处理)", agent.getAgent().getName(), agent.getClient().getName());
                // 更新委托人
                this.procUnitHandlerApplication.updateProcUnitHandlerClient(procUnitHandlerId, agent.getClient().getId(), agent.getClient().getName());
            }
            delegateTask.setDescription(taskDescription);
            // 处理人对应的流程规则处理人，用于取是否计时
            // if (procUnitHandler.getApprovalRuleHandler() != null) {
            runtimeTaskExtension.setApprovalRuleHandlerId(procUnitHandler.getApprovalRuleHandlerId());
            // }
            delegateTask.setVariable(SEND_MESSAGE_VARIABLE, procUnitHandler.getSendMessage());
            delegateTask.setVariable(PROC_UNIT_HANDLER_ID, procUnitHandler.getId());
        } else {
            doOtherSetCurrentTaskHandler(delegateTask, runtimeTaskExtension);
        }
    }

    /**
     * 更新环节处理人的处理结果
     * <ul>
     * <li>1、前提条件：不是申请环节（ 审批环节或者业务环节），流程环节处理人ID不能为空；</li>
     * <li>2、处理逻辑：
     * <ul>
     * <li>2.1、更新流程环节处理人的 <tt>结果（result）、意见（opinion）、处理时间（handle_time）、状态（status）</tt>属性；</li>
     * <li>2.2、更新扩展任务的<tt>结果（result）、意见（opinion）</tt>属性；</li>
     * </ul>
     * </li>
     * </ul>
     */
    private void updateProcUnitHandlerResult() {
        updateProcUnitHandlerResult(getApprovalParameter().getTaskId());
    }

    private void updateProcUnitHandlerResult(String taskId) {
        // 申请环节，不保存审批数据
        if (getApprovalParameter().getProcUnitId().equalsIgnoreCase(ActivityKind.APPLY)) {
            return;
        }
        String procUnitHandlerId = getApprovalParameter().getCurrentHandleId();
        if (StringUtil.isBlank(procUnitHandlerId)) {
            return;
        }
        String opinion = getApprovalParameter().getHandleOpinion();
        // 更新处理结果
        this.procUnitHandlerApplication.updateProcUnitHandlerResult(procUnitHandlerId, getApprovalParameter().getProcUnitHandlerResult(), opinion,
                getApprovalParameter().getProcUnitHandlerStatus());
        String processAction = getApprovalParameter().getProcessAction();
        if (processAction != null && processAction.equalsIgnoreCase(ProcessAction.ABORT_PROCESS_INSTANCE)) {
            // 终止流程时运行时runtimeTaskExtension已不存在 只更新 historicTaskInstExtension
            this.actApplication.updateHistoricTaskExtensionHandleResult(taskId, getApprovalParameter().getProcUnitHandlerResult(), opinion);
        } else {
            // 更新任务处理意见
            this.actApplication.updateTaskExtensionHandleResult(taskId, getApprovalParameter().getProcUnitHandlerResult(), opinion);
        }
    }

    /**
     * 保存业务和审批人数据
     * <ul>
     * <li>1、前提条件：</li>
     * <li>2、处理逻辑：
     * <ul>
     * <li>2.1、更新审批人信息；</li>
     * <li>2.2、业务实现类覆盖此方法， 保存业务数据；</li>
     * </ul>
     * </li>
     * </ul>
     *
     * @return 业务主键ID
     */
    protected String saveBizAndApprovalData() {
        updateProcUnitHandlerResult();
        return null;
    }

    /**
     * 保存业务和审批人数据
     * <p>
     * 与<code>saveBizAndApprovalData()</code>不同，可以通过本方法设置任务属性。
     *
     * @param delegateTask 代理任务
     * @return
     * @see #saveBizAndApprovalData()
     */
    protected String saveBizAndApprovalData(DelegateTask delegateTask) {
        return saveBizAndApprovalData();
    }

    /**
     * 流程启动事件
     * <ul>
     * <li>保存业务单据，更新业务主键</li>
     * <li>保存历史流程实例扩展表</li>
     * </ul>
     *
     * @param delegateExecution 代理执行
     */
    protected void onStart(DelegateExecution delegateExecution) {
        String startModel = (String) delegateExecution.getVariable("startModel");
        startModel = StringUtil.isBlank(startModel) ? ProcessStartModel.MANUAL.getId() : startModel;

        if (ProcessStartModel.isManual(startModel)) {
            String id = this.saveBizAndApprovalData();
            Assert.hasText(id, "保存业务数据出错。");
            ((ExecutionEntity) delegateExecution).updateProcessBusinessKey(id);
        }

        String processInstanceId = delegateExecution.getProcessInstanceId();
        HistoricProcessInstanceEntity historicProcessInstanceEntity = Context.getCommandContext().getHistoricProcessInstanceEntityManager()
                .findHistoricProcessInstance(processInstanceId);

        // 流程实例扩展信息
        String initiatorPersonMemberId = ClassHelper.convert(delegateExecution.getVariable(ProcessTaskContants.INITIATOR_PERSONMEMBER_ID), String.class, "");
        this.actApplication.saveHistoricProcessInstanceExtension(historicProcessInstanceEntity, initiatorPersonMemberId, getProcessDescription(),
                ProcessStatus.EXECUTING);
    }

    /**
     * 流程结束事件
     *
     * @param delegateExecution 代理执行
     */
    protected void onEnd(DelegateExecution delegateExecution) {
        boolean isMakeACopyFor = ClassHelper.convert(delegateExecution.getVariable(IS_MAKE_A_COPY_FOR), Boolean.class, false);
        if (isMakeACopyFor) {
            return;
        }
        // 流程结束给申请人发送抄送
        String bizId = delegateExecution.getProcessBusinessKey();
        Map<String, Object> applicant = this.actApplication.queryApplicantByBizId(bizId);// .getApplicantFullId(bizId);
        String applicantFullId = null;
        if (applicant.size() > 0) {
            applicantFullId = ClassHelper.convert(applicant.get("executorFullId"), String.class);
        }
        if (!StringUtil.isBlank(applicantFullId) && !StringUtil.isBlank(getApprovalParameter().getTaskId())) {
            ThreadLocalUtil.putVariable(Constants.MAKEACOPYFORPREFIX, "办结提醒:");
            List<String> executorIds = new ArrayList<String>(1);
            executorIds.add(applicantFullId);
            this.workflowService.makeACopyFor(getApprovalParameter().getTaskId(), executorIds);
            delegateExecution.setVariable(IS_MAKE_A_COPY_FOR, true);
        }
    }

    /**
     * 流程撤销事件
     *
     * @param delegateExecution 代理执行实例
     */
    protected void onRecallProcessInstance(DelegateExecution delegateExecution) {

    }

    /**
     * 删除流程实例事件
     *
     * @param delegateExecution 代理执行
     */
    protected void onDeleteProcessInstance(DelegateExecution delegateExecution) {
    }

    /**
     * 终止流程实例事件
     *
     * @param delegateExecution 代理执行
     */
    protected void onAbortProcessInstance(DelegateExecution delegateExecution) {
        boolean isMakeACopyFor = ClassHelper.convert(delegateExecution.getVariable(IS_MAKE_A_COPY_FOR), Boolean.class, false);
        if (isMakeACopyFor) {
            return;
        }
        // 流程终止给申请人发送抄送
        String bizId = delegateExecution.getProcessBusinessKey();
        Map<String, Object> applicant = this.actApplication.queryApplicantByBizId(bizId);
        if (applicant != null && applicant.size() > 0) {
            String applicantPersonMemberId = ClassHelper.convert(applicant.get("applicantPersonMemberId"), String.class);
            if (StringUtil.isBlank(applicantPersonMemberId)) {
                return;
            }
            // 申请人自己终止的不发送
            String applicantPersonId = OpmUtil.getPersonIdFromPersonMemberId(applicantPersonMemberId);
            Operator operator = this.getOperator();
            if (operator != null && operator.getUserId().equals(applicantPersonId)) {
                return;
            }
            String applicantFullId = ClassHelper.convert(applicant.get("executorFullId"), String.class);
            if (!StringUtil.isBlank(applicantFullId) && !StringUtil.isBlank(getApprovalParameter().getTaskId())) {
                List<String> executorIds = new ArrayList<String>(1);
                executorIds.add(applicantFullId);
                this.workflowService.makeACopyFor(getApprovalParameter().getTaskId(), executorIds);
                delegateExecution.setVariable(IS_MAKE_A_COPY_FOR, true);
            }
        }
    }

    protected RepositoryService getRepositoryService() {
        return workflowService.getRepositoryService();
    }

    /**
     * 得到流程标题
     *
     * @return
     */
    protected String getProcessDescription() {
        return "";
    }

    /**
     * 申请环节特殊处理 手工启动的流程
     * <p>
     * 若流程为手工模式保存业务单据，更新流程的业务主键。保存流程实例扩展数据。
     *
     * @param delegateTask
     *            代理任务
     */
    // protected void handleApplyProcUnit(DelegateTask delegateTask) {
    // String startModel = (String) delegateTask.getVariable("startModel");
    // startModel = startModel == null ? ProcessStartModel.MANUAL.getId() : startModel;
    //
    // if (ProcessStartModel.isManual(startModel)) {
    // String id = this.saveBizAndApprovalData(delegateTask);
    // Assert.hasText(id, "保存业务数据出错。");
    // ((ExecutionEntity) delegateTask.getExecution()).updateProcessBusinessKey(id);
    // }
    //
    // String processInstanceId = delegateTask.getProcessInstanceId();
    //
    // HistoricProcessInstanceEntity historicProcessInstanceEntity = Context.getCommandContext().getHistoricProcessInstanceEntityManager()
    // .findHistoricProcessInstance(processInstanceId);
    //
    // // 流程实例扩展信息
    // String initiatorPersonMemberId = ClassHelper.convert(delegateTask.getVariable(ProcessTaskContants.INITIATOR_PERSONMEMBER_ID), String.class, "");
    // this.actApplication.saveHistoricProcessInstanceExtension(historicProcessInstanceEntity, initiatorPersonMemberId, getProcessDescription(),
    // ProcessStatus.EXECUTING);
    // }

    /**
     * 设置任务扩展数据默认值
     *
     * @param delegateTask         代理任务
     * @param runtimeTaskExtension 运行时任务数据
     */
    private void setTaskExtendData(DelegateTask delegateTask, RuntimeTaskExtension runtimeTaskExtension) {
        runtimeTaskExtension.setGenerateReason(getApprovalParameter().getProcessAction());
        if (getApprovalParameter().isAdvanceProcessAction() || getApprovalParameter().isSaveProcessAction()) {
            runtimeTaskExtension.setBusinessCode(this.getBizCode());
        }
    }

    /**
     * 设置任务是否计时
     *
     * @param delegateTask         代理任务
     * @param runtimeTaskExtension 运行时任务数据
     */
    protected void setTaskLimitTime(DelegateTask delegateTask, RuntimeTaskExtension runtimeTaskExtension) {
        if (!this.isApplyProcUnit(delegateTask)) {
            String processDefinitionId = delegateTask.getProcessDefinitionId();
            if (!StringUtil.isBlank(processDefinitionId)) {
                String procId = ProcessDefinitionUtil.getProcessDefinitionKeyFromId(processDefinitionId);
                String procUnitId = delegateTask.getTaskDefinitionKey();
                LimitTime limitTime = this.approvalRuleApplication.getApprovalRuleHandlerLimitTime(procId, procUnitId,
                        runtimeTaskExtension.getApprovalRuleHandlerId());
                runtimeTaskExtension.setNeedTiming(limitTime.getNeedTiming());
                runtimeTaskExtension.setLimitTime(limitTime.getLimitTime());
            }
        }
    }

    /**
     * 修正扩展数据
     *
     * @param delegateTask         代理人员
     * @param runtimeTaskExtension 任务扩展
     */

    protected void reviseExtendData(DelegateTask delegateTask, RuntimeTaskExtension runtimeTaskExtension) {
    }

    /**
     * 创建任务前事件
     * <ul>
     * <li>设置任务主题
     * <li>设置任务处理页URL
     * <li>设置任务分类（大类）
     * <li>设置任务类别
     * <li>设置任务创建人信息
     * <li>设置任务执行人信息
     * </ul>
     *
     * @param delegateTask 代理任务
     */
    protected void onBeforeCreate(DelegateTask delegateTask) {
        RuntimeTaskExtension runtimeTaskExtension = new RuntimeTaskExtension();
        // 设置扩展数据默认值
        setTaskExtendData(delegateTask, runtimeTaskExtension);
        // 设置任务处理人
        setCurrentTaskHandler(delegateTask, runtimeTaskExtension);
        // 设置任务限时
        setTaskLimitTime(delegateTask, runtimeTaskExtension);
        // 设置主题
        setTaskDescription(delegateTask);
        // 统一设置任务描述前缀
        String taskDescriptionPrefix = getTaskDescriptionPrefix();
        if (!StringUtil.isBlank(taskDescriptionPrefix)) {
            String taskDescription = delegateTask.getDescription();
            taskDescription = String.format("%s:%s", taskDescriptionPrefix, taskDescription);
            delegateTask.setDescription(taskDescription);
        }
        // 修订扩展数据
        reviseExtendData(delegateTask, runtimeTaskExtension);

        runtimeTaskExtension.fromDelegateTask(delegateTask, TaskScope.PROCESS, TaskKind.TASK, this.getTaskId());

        if (StringUtil.isNotBlank(runtimeTaskExtension.getInitiatorPersonMemberId())) {
            // 指定发起人申请环节
            actApplication.saveTaskExtension(runtimeTaskExtension, runtimeTaskExtension.getInitiatorPersonMemberId());
        } else {
            ProcUnitHandler procUnitHandler = null;
            if (StringUtil.isNotBlank(getApprovalParameter().getCurrentHandleId())) {
                this.procUnitHandlerApplication.loadProcUnitHandler(getApprovalParameter().getCurrentHandleId());
            }

            actApplication.saveTaskExtension(runtimeTaskExtension, procUnitHandler);
        }
        setNewTaskId(delegateTask.getId());

        ProcessEventContext.addNewProcessTask(delegateTask.getId());

        sendMessage(delegateTask, runtimeTaskExtension);
    }

    protected void sendMessage(DelegateTask delegateTask, RuntimeTaskExtension runtimeTaskExtension) {
        String processKey = processFun.getProcessKeyFromProcessDefinitionId(delegateTask.getProcessDefinitionId());
        String procUnitHandlerId = ClassHelper.convert(delegateTask.getVariable(PROC_UNIT_HANDLER_ID), String.class);
        MessageSendModel messageSendModel = new MessageSendModel(runtimeTaskExtension);
        boolean isSend = this.isSendMessage(delegateTask);

        Operator operator = this.getOperator();
        if (operator == null) {
            SDO sdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
            operator = sdo.getOperator();
        }

        messageSendModel.setSenderId(runtimeTaskExtension.getCreatorPersonId());
        messageSendModel.setReceiverId(runtimeTaskExtension.getExecutorPersonId());
        messageSendModel.setIsSend(isSend);
        messageSenderManager.send(messageSendModel);
        if (isSend) {
            Person person = orgApplication.loadPerson(runtimeTaskExtension.getExecutorPersonId());
            String urlFormat = "isReadOnly=false&taskKindId=task&procInstId=%s&bizId=%s&bizCode=%s&processDefinitionKey=%s&procUnitId=%s&taskId=%s&procUnitHandlerId=%s&taskStatusId=%s";
            String url = String.format(urlFormat, delegateTask.getProcessInstanceId(), delegateTask.getExecution().getProcessBusinessKey(), this.getBizCode(),
                    processKey, delegateTask.getTaskDefinitionKey(), delegateTask.getId(), procUnitHandlerId, TaskStatus.READY.getId());
            String executorUrl = runtimeTaskExtension.getExecutorUrl();
            // String webBasePath = Singleton.getParameter("webBasePath",
            // String.class);
            // Assert.hasText(webBasePath, "系统参数“webBasePath”未设置。");
            if (executorUrl.indexOf("?") > -1) {
                url = String.format("%s&%s", executorUrl, url);
            } else {
                url = String.format("%s?%s", executorUrl, url);
            }
            messageSenderManager.send(delegateTask.getExecution().getProcessBusinessKey(), delegateTask.getDescription(), url, this.getOperator()
                            .getLoginName(),
                    person.getLoginName());
        }
    }

    private boolean isSendMessage(DelegateTask delegateTask) {
        Object sendMessage = delegateTask.getVariable(SEND_MESSAGE_VARIABLE);
        delegateTask.removeVariable(SEND_MESSAGE_VARIABLE);

        if (sendMessage == null) {
            return false;
        }

        return sendMessage.equals(1);
    }

    // private boolean getApproveNotPassedBack(DelegateTask delegateTask) {
    // return ClassHelper.convert(delegateTask.getVariable(APPROVE_NOT_PASSED_BACK_KEY), Boolean.class, false);
    // }

    /**
     * 创建任务
     * <ul>
     * <li>申请环节需保存业务和审批数据，给流程赋业务ID
     * <li>给任务赋默认值
     * </ul>
     *
     * @param delegateTas 代理任务
     */
    @Transactional
    public void onCreate(DelegateTask delegateTask) {
        onBeforeCreate(delegateTask);
    }

    /**
     * 给任务赋处理人事件
     *
     * @param delegateTask 代理任务
     */
    public void onAssignment(DelegateTask delegateTask) {

    }

    protected String getNextProcUnitId(DelegateExecution delegateExecution) {
        return "";
    }

    protected String getNextProcUnitId(DelegateTask delegateTask) {
        String nextProcUnitId = getApprovalParameter().getManualProcUnitId();
        if (StringUtil.isNotBlank(nextProcUnitId)) {
            return nextProcUnitId;
        }
        return "Approve";
    }

    /**
     * 是否抢占取消任务
     *
     * @param delegateTask 任务代理
     * @return
     */
    private boolean isPreemptCanceledTask(DelegateTask delegateTask) {
        Object object = delegateTask.getVariable(PREEMPT_CANCEL_VARIABLE);
        if (object != null) {
            return delegateTask.getVariable(PREEMPT_CANCEL_VARIABLE, Boolean.class);
        }
        return false;
    }

    /**
     * @since 1.1.3
     */
    private boolean isLimitHandlerCanceledTask(DelegateTask delegateTask) {
        Object object = delegateTask.getVariable(LIMIT_HANDLER_CANCEL_VARIABLE);
        if (object != null) {
            return delegateTask.getVariable(LIMIT_HANDLER_CANCEL_VARIABLE, Boolean.class);
        }
        return false;
    }

    /**
     * 处理任务协作模式
     *
     * @param delegateTask
     */
    private void handleTaskCooperationModelKind(DelegateTask delegateTask) {
        ApprovalParameter approvalParameter = getApprovalParameter();
        String procUnitHandlerId = approvalParameter.getCurrentHandleId();

        ProcUnitHandler procUnitHandler = procUnitHandlerApplication.loadProcUnitHandler(procUnitHandlerId);

        Assert.notNull(procUnitHandler, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, procUnitHandlerId, "流程环节处理人"));

        if (!procUnitHandler.isChief()) {
            return;
        }

        // 协审规则
        String procId = processFun.getProcessKeyFromProcessDefinitionId(delegateTask.getProcessDefinitionId());
        ProcDefinition procDefinition = this.procDefinitionApplication.loadProcDefinitionByProcAndProcUnitId(procId, delegateTask.getTaskDefinitionKey());
        boolean assistantMustApprove = procDefinition != null && procDefinition.getAssistantMustApprove() != null
                && procDefinition.getAssistantMustApprove().equals(1);

        Integer count = this.actApplication.countWaitedAssistantTask(approvalParameter.getBizId(), procUnitHandler.getId());
        Assert.isTrue(count == 0, "协审任务已打回，正在等待处理，不能流转。");
        // C1A1C2A2..CnAn
        if (assistantMustApprove) {
            count = this.procUnitHandlerApplication.countAssistantNotApproveByChiefId(approvalParameter.getBizId(), procUnitHandler.getId());
            Assert.isTrue(count == 0, "还有协审人员没有审批，不能流转。");
        }

        if (procUnitHandler.isPreempt()) {
            // Ci 完成 C1A1..Ci-1Ai-1 Ci+1Ai+1...CnAn的未完成的任务系统自动完成
            // 系统完成其他人的任务（包括协审任务）
            delegateTask.setVariable(CHIEF_APPROVE_PASSED_VARIABLE, false);
            List<String> taskIds = this.actApplication.queryRuTaskIdsByProcessInstanceId(delegateTask.getProcessInstanceId());
            for (String taskId : taskIds) {
                if (!delegateTask.getId().equals(taskId)) {
                    Map<String, Object> variables = new HashMap<>(1);
                    variables.put(PREEMPT_CANCEL_VARIABLE, true);
                    this.workflowService.getTaskService().complete(taskId, variables, true);
                }
            }
            this.procUnitHandlerApplication.updateOtherProcUnitHandlersResultSystemComplete(this.getBizId(), delegateTask.getTaskDefinitionKey(),
                    this.getCurrentHandleGroupId(), procUnitHandlerId, null);
        } else {
            // @since 1.1.3
            if (isLimitHandler(procUnitHandler.getProcUnitId(), procUnitHandler.getGroupId(), procUnitHandler.getLimitHandler())) {
                // 系统完成其他人的任务（包括协审任务）
                Map<String, Object> variables = new HashMap<>(1);
                variables.put(LIMIT_HANDLER_CANCEL_VARIABLE, true);
                this.actApplication.queryRuTaskIdsByProcessInstanceId(delegateTask.getProcessInstanceId())
                        .stream()
                        .filter(taskId -> !taskId.equals(delegateTask.getId()))
                        .forEach(taskId -> workflowService.getTaskService().complete(taskId, variables, true));
                this.procUnitHandlerApplication.updateOtherProcUnitHandlersResultSystemComplete(this.getBizId(), delegateTask.getTaskDefinitionKey(),
                        this.getCurrentHandleGroupId(), procUnitHandlerId, null);
                return;
            }
            // 主审完成后，查询协审任务添加到通知事件环境中
            List<String> taskIds = this.actApplication.queryRuAssistantTaskIds(this.getBizId(), procUnitHandlerId);
            for (String taskId : taskIds) {
                ProcessEventContext.addCompletedTask(taskId);
            }
        }
    }

    /**
     * @since 1.1.3
     */
    private boolean isLimitHandler(String procUnitId, Integer groupId, Integer limitHandler) {
        if (limitHandler == null || limitHandler < 1) {
            return false;
        }
        int procUnitHandlerCount = (int) procUnitHandlerApplication.queryProcUnitHandlers(getBizId(), procUnitId, groupId)
                .stream()
                .filter(handler -> CooperationModelKind.isChief(handler.getCooperationModelId()))
                .count();
        // 已完成的环节处理人（主审）
        int completedChiefHandlerCount = (int) procUnitHandlerApplication.queryCompletedProcUnitHandlers(getBizId(), procUnitId, groupId)
                .stream()
                .filter(handler -> CooperationModelKind.isChief(handler.getCooperationModelId()))
                .count();
        int totalChiefHandlers = procUnitHandlerCount + completedChiefHandlerCount;
        if (totalChiefHandlers < 2) {
            return false;
        }
        if(totalChiefHandlers <= limitHandler) {
            // 主审总人数须大于最少审批人数
            return false;
        }
        return Objects.equals(limitHandler, completedChiefHandlerCount);
    }

    /**
     * 任务完成前事件处理
     *
     * @param delegateTask 代理任务
     */
    protected void onBeforeComplete(DelegateTask delegateTask) {
        if (isApprovalProcUnit(delegateTask)) {
            if (getApprovalParameter().isAdvanceProcessAction()) {
                handleTaskCooperationModelKind(delegateTask);
            }

            Boolean currentGroupChiefApprovePassed = this.currentGroupChiefApprovePassed(delegateTask.getTaskDefinitionKey());
            delegateTask.setVariable(CHIEF_APPROVE_PASSED_VARIABLE, currentGroupChiefApprovePassed);
            delegateTask.setVariable(APPROVE_FINISHED_VARIABLE, this.approveFinished(delegateTask.getTaskDefinitionKey()));
            delegateTask.setVariable(APPROVE_PASSED_VARIABLE, this.approvePassed());

            if (!approvePassed()) {
                switch (getApproveNotPassedHandleKind()) {
                    case CONTINUE:
                        delegateTask.setVariable(APPROVE_PASSED_VARIABLE, true);
                        break;
                    case BACK_TO_APPLY:
                        // 并行任务的处理
                        delegateTask.setVariable(APPROVE_NOT_PASSED_BACK_KEY, true);
                        delegateTask.setVariable("startModel", "");
                        break;
                    case ABORT:
                        // parent execution record handlerList
                        // child1 execution record (nrOfInstances、nrOfCompletedInstances、 nrOfActiveInstances)
                        // child2 execution record (loopCounter、assignee)
                        // child3 execution record (loopCounter、assignee)
                        List<String> handlers = new ArrayList<String>(1);
                        delegateTask.setVariable("handlerList", handlers);
                        break;
                    default:
                        break;
                }
            }
        } else {
            delegateTask.setVariable(APPROVE_FINISHED_VARIABLE, false);
            delegateTask.setVariable(CHIEF_APPROVE_PASSED_VARIABLE, false);
            delegateTask.setVariable(APPROVE_PASSED_VARIABLE, false);
        }
    }

    /**
     * 任务完成后事件处理
     *
     * @param delegateTask 代理任务
     */
    protected void onAfterComplete(DelegateTask delegateTask) {

    }

    /**
     * 填充下一环节的审批人
     * <p>
     * 基类只处理申请环节提交后，计算审批环节处理人
     *
     * @param delegateTask 代理任务
     */
    @Deprecated
    protected void calculateNextProcUnitHandlers(DelegateTask delegateTask) {
        Boolean needApprove = ClassHelper.convert(delegateTask.getVariable("needApprove"), Boolean.class, true);
        if (this.isApplyProcUnit(delegateTask) && needApprove) {
            doCalculateNextProcUnitHandlers(delegateTask);
        }
    }

    /**
     * 更新合并状态
     */
    private void updateMergingStatus(Map<String, Object> handler) {
        if (ClassHelper.convert(handler.get("merged"), String.class, "0").endsWith("1")) {
            handler.put("status", "-1");
        }
    }

    private void buildUniqueHandler(Map<String, Integer> uniqueHandlers, Map<String, Object> handler) {
        String handlerId = ClassHelper.convert(handler.get("handlerId"), String.class);
        String personId = OpmUtil.getPersonIdFromPersonMemberId(handlerId);
        String handlerName = ClassHelper.convert(handler.get("handlerName"), String.class);
        String handerInfo = String.format("%s,%s", personId, handlerName);
        boolean merged = ClassHelper.convert(handler.get("merged"), Integer.class, 0).equals(1);
        if (!uniqueHandlers.containsKey(handerInfo)) {
            uniqueHandlers.put(handerInfo, merged ? 0 : 1);
        } else if (!merged) {
            Integer count = ClassHelper.convert(uniqueHandlers.get(handerInfo), Integer.class, 0);
            uniqueHandlers.put(handerInfo, ++count);
        }
    }

    /**
     * 验证预览提交规则
     *
     * @param handlers
     */
    @SuppressWarnings("unchecked")
    private void checkQueryAdvance(List<Object> handlers) {
        String handleKindCode, subProcUnitName;
        List<Map<String, Object>> selectionHandlers;
        String approvalRuleHandlerId;
        ApprovalRuleHandler approvalRuleHandler;
        Map<String, Object> handler;
        Map<String, Integer> uniqueHandlers = new HashMap<String, Integer>(handlers.size());
        for (int i = 0, l = handlers.size(); i < l; i++) {
            handler = (Map<String, Object>) handlers.get(i);
            handleKindCode = ClassHelper.convert(handler.get("handleKindCode"), String.class);

            if (HandlerKind.SEGMENTATION.equalsById(handleKindCode)) {
                continue;
            }

            if (HandlerKind.isSelection(handleKindCode)) {
                selectionHandlers = (List<Map<String, Object>>) handler.get("handlerData");
                // 判断是否必经环节
                approvalRuleHandlerId = ClassHelper.convert(handler.get("approvalRuleHandlerId"), String.class);
                approvalRuleHandler = this.approvalRuleApplication.loadApprovalRuleHandler(approvalRuleHandlerId);

                if (approvalRuleHandler != null && approvalRuleHandler.getMustPass().equals(1)) {
                    subProcUnitName = ClassHelper.convert(handler.get("subProcUnitName"), String.class);
                    Util.check(selectionHandlers != null && selectionHandlers.size() > 0, String.format("“%s”没有选择处理人。", subProcUnitName));
                }
                if (selectionHandlers != null && selectionHandlers.size() > 0) {
                    for (int j = 0, len = selectionHandlers.size(); j < len; j++) {
                        buildUniqueHandler(uniqueHandlers, selectionHandlers.get(j));
                    }
                }
            } else {
                buildUniqueHandler(uniqueHandlers, handler);
            }
        }
    }

    protected List<ProcUnitHandler> buildProcUnitHandlers(List<Map<String, Object>> handlerList) {
        List<ProcUnitHandler> procUnitHandlers = new ArrayList<ProcUnitHandler>(handlerList.size());
        for (Map<String, Object> handler : handlerList) {

            ProcUnitHandler procUnitHandler = new ProcUnitHandler();

            try {
                BeanUtils.populate(procUnitHandler, handler);
            } catch (IllegalAccessException | InvocationTargetException e) {
                new ApplicationException(e.getMessage(), e);
            }

            String approvalRuleHandlerId = ClassHelper.convert(handler.get("approvalRuleHandlerId"), String.class);

            if (StringUtil.isNotBlank(approvalRuleHandlerId)) {
                procUnitHandler.setApprovalRuleHandlerId(approvalRuleHandlerId);
                // ApprovalRuleHandler approvalRuleHandler = this.approvalRuleApplication.loadApprovalRuleHandler(approvalRuleHandlerId);
                // procUnitHandler.setApprovalRuleHandler(approvalRuleHandler);
            }

            procUnitHandlers.add(procUnitHandler);
        }
        return procUnitHandlers;
    }

    private boolean isExclusiveGatewayManual() {
        return getApprovalParameter().getGatewayManual();
    }

    private void buildManualProcUnitId(VariableScope variableScope) {
        if (isExclusiveGatewayManual()) {
            variableScope.setVariable("_manualProcUnitId", this.getBizAndApprovalData().getProperty(ProcessFun.MANUAL_SELECT_FLAG));
        }
    }

    private boolean isSelectEndFlowNode() {
        if (isExclusiveGatewayManual()) {
            List<Object> inputHandlers = this.getBizAndApprovalData().getList("procUnitHandlers");
            for (Object item : inputHandlers) {
                @SuppressWarnings("unchecked")
                Map<String, Object> m = (Map<String, Object>) item;
                Object isEndFlowNode = m.get("isEndFlowNode");
                if (isEndFlowNode != null && isEndFlowNode.toString().equals("true")) {
                    return true;
                }
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private void calculateNextProcUnitHandlersForQueryAdvance(VariableScope variableScope, String processDefinitionKey, String nextProcUnitId) {

        List<Object> inputHandlers = this.getBizAndApprovalData().getList("procUnitHandlers");
        List<Map<String, Object>> handlerList = new ArrayList<>(inputHandlers.size());

        checkQueryAdvance(inputHandlers);

        String handleKindCode;
        List<Map<String, Object>> selectionHandlerList;

        for (Object item : inputHandlers) {
            Map<String, Object> m = (Map<String, Object>) item;

            updateMergingStatus(m);
            handleKindCode = ClassHelper.convert(m.get("handleKindCode"), String.class);
            if (HandlerKind.isSelection(handleKindCode)) {
                selectionHandlerList = (List<Map<String, Object>>) m.get("handlerData");
                if (selectionHandlerList != null) {
                    for (Map<String, Object> selectionHandler : selectionHandlerList) {
                        selectionHandler.put("bizId", m.get("bizId"));
                        selectionHandler.put("bizCode", m.get("bizCode"));
                        selectionHandler.put("procUnitId", m.get("procUnitId"));
                        selectionHandler.put("procUnitName", m.get("procUnitName"));
                        selectionHandler.put("handleKindId", m.get("handleKindId"));
                        selectionHandler.put("approvalRuleHandlerId", m.get("approvalRuleHandlerId"));
                        selectionHandler.put("approvalRuleId", m.get("approvalRuleId"));
                        selectionHandler.put("sendMessage", m.get("sendMessage"));

                        selectionHandler.put("cooperationModelId", m.get("cooperationModelId"));
                        selectionHandler.put("groupId", m.get("groupId"));
                        selectionHandler.put("status", 0);

                        if (m.get("taskExecuteMode") != null) {
                            selectionHandler.put("taskExecuteMode", m.get("taskExecuteMode"));
                        }
                        updateMergingStatus(selectionHandler);
                        handlerList.add(selectionHandler);
                    }
                }
            } else {
                handlerList.add(m);
            }
        }
        // 合并处理人
        this.mergeHandler(processDefinitionKey, nextProcUnitId, handlerList);
        // this.excludeSameHandler(handlerList);
        this.reviseNextProcUnitHandlers(handlerList);

        if (this.getFlowKind() != ProcessKind.FREE && StringUtil.isNotBlank(this.getBizId())) {
            this.procUnitHandlerApplication.deleteProcUnitHandlersByBizAndProcUnitId(this.getBizId(), nextProcUnitId);
        }

        List<ProcUnitHandler> procUnitHandlers = buildProcUnitHandlers(handlerList);
        this.procUnitHandlerApplication.saveProcUnitHandlers(procUnitHandlers);
    }

    /**
     * 构建流程审批操作员
     * <p>
     * 当前审批人为一人多岗的情况，用当前环节的处理人，构建操作员
     */
    private void buildProcApprovalOperator() {
        if (StringUtil.isNotBlank(this.getCurrentHandleId())) {
            if (!this.getOperator().isCurrentOperator(this.getCurrentHandleId())) {
                ProcUnitHandler procUnitHandler = this.procUnitHandlerApplication.loadProcUnitHandler(this.getCurrentHandleId());
                Assert.notNull(procUnitHandler, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, this.getCurrentHandleId(), "环节处理人"));

                Org personMemberEntity = this.orgApplication.loadEabledOrg(procUnitHandler.getHandlerId());
                PersonMember personMember = OpmUtil.toPersonMember(personMemberEntity);
                User user = new User(personMember);

                Operator procApprovalOperator = new Operator(user, new Date());
                procApprovalOperator.setOrgContext(personMember);
                orgFun.setOperatorExtensionProperties(procApprovalOperator);

                ThreadLocalUtil.putVariable(ThreadLocalUtil.PROC_APPROVAL_OPERATOR_KEY, procApprovalOperator);
            }
        }
    }

    /**
     * 修正下一环节处理人
     *
     * @param handlerList 环节处理人
     */
    protected void reviseNextProcUnitHandlers(List<Map<String, Object>> handlerList) {

    }

    private void calculateNextProcUnitHandlersForAdvance(String processDefinitionKey, List<ActivityImpl> outgoingUserTasks, String bizId) {
        Map<String, Object> bizParams = getProcessBizParams(bizId);

        int sequence = 1, lastGroupId = 0;
        String procUnitHandlerChiefId = "";
        buildProcApprovalOperator();
        for (ActivityImpl item : outgoingUserTasks) {
            ApprovalRule approvalRule = procApprovalRuleParseService.execute(processDefinitionKey, item.getId(), bizParams);

            Assert.notNull(approvalRule, "未找到审批规则。");

            List<ApprovalRuleHandler> approvalRuleHandlers = approvalRule.getApprovalRuleHandlers();
            if (approvalRuleHandlers == null || approvalRuleHandlers.size() == 0) {
                throw new ApplicationException(String.format("审批规则“%s”未设置处理人。", approvalRule.getName()));
            }

            checkApprovalRuleHandlerNotNull(approvalRule);

            List<Map<String, Object>> handlerList = new ArrayList<Map<String, Object>>(approvalRuleHandlers.size());
            List<Map<String, Object>> chiefHandlerList = new ArrayList<Map<String, Object>>(approvalRuleHandlers.size());

            List<Map<String, Object>> additionalHandlers = getAdditionalHandlers();
            if (additionalHandlers != null) {
                addAdditionalHandlers(bizId, chiefHandlerList, additionalHandlers, item.getId());
            }

            ApprovalRuleHandler chiefHandler;

            for (int i = 0; i < approvalRuleHandlers.size(); i++) {
                chiefHandler = approvalRuleHandlers.get(i);

                if (chiefHandler.getOrgUnits().size() == 0) {
                    continue;
                }

                if (lastGroupId != chiefHandler.getGroupId()) {
                    lastGroupId = chiefHandler.getGroupId();
                    sequence = 1;
                }
                // 主审人
                for (OrgUnit orgUnit : chiefHandler.getOrgUnits()) {
                    Map<String, Object> chiefHandlerData = buildProcUnitHandler(approvalRule, chiefHandler, bizId, item.getId(), "", orgUnit, sequence);

                    procUnitHandlerChiefId = CommonUtil.createGUID();
                    chiefHandlerData.put("id", procUnitHandlerChiefId);

                    chiefHandlerList.add(chiefHandlerData);

                    sequence++;
                }
                // 协审人 、 抄送人
                ApprovalRuleHandlerAssist assistHandler;
                for (int j = 0; j < chiefHandler.getAssists().size(); j++) {
                    assistHandler = chiefHandler.getAssists().get(j);
                    for (OrgUnit orgUnit : assistHandler.getOrgUnits()) {
                        Map<String, Object> assistantHandlerData = this.buildProcUnitHandlerAssists(approvalRule, chiefHandler, assistHandler, bizId,
                                item.getId(), orgUnit, sequence, j + 1,
                                assistHandler.getKindId(), procUnitHandlerChiefId);

                        handlerList.add(assistantHandlerData);
                    }
                }

            }
            // 排除相同人员
            this.mergeHandler(processDefinitionKey, item.getId(), chiefHandlerList);
            // excludeSameHandler(chiefHandlerList);
            excludeSpecifiedHandler(bizId, chiefHandlerList);
            handlerList.addAll(chiefHandlerList);

            this.reviseNextProcUnitHandlers(handlerList);

            // 自由流不删除
            if (this.getFlowKind() != ProcessKind.FREE && StringUtil.isNotBlank(this.getBizId())) {
                this.procUnitHandlerApplication.deleteProcUnitHandlersByBizAndProcUnitId(getBizId(), item.getId());
            }

            List<ProcUnitHandler> procUnitHandlers = buildProcUnitHandlers(handlerList);
            this.procUnitHandlerApplication.saveProcUnitHandlers(procUnitHandlers);
        }
    }

    private void checkLicense() {
        // if (ProtectionManager.getCount() % 30 == 0) {
        // ProtectionManager.checkPoint(ProtectionManager.getFlowFlag(),
        // this.approvalRuleApplication.countApprovalRule());
        // }
    }

    /**
     * 计算下一环节处理人
     *
     * @param delegateTask 代理任务
     */
    protected void doCalculateNextProcUnitHandlers(DelegateTask delegateTask) {
        checkLicense();
        String processDefinitionKey = processFun.getProcessApprovalDefinitionKey(delegateTask.getProcessDefinitionId());
        if (getApprovalParameter().isQueryAdvanceProcessAction()) {
            String nextProcUnitId = this.getNextProcUnitId(delegateTask);
            calculateNextProcUnitHandlersForQueryAdvance(delegateTask, processDefinitionKey, nextProcUnitId);
            return;
        }

        List<ActivityImpl> outgoingUserTasks = findOutgoingUserTasks(delegateTask);
        calculateNextProcUnitHandlersForAdvance(processDefinitionKey, outgoingUserTasks, delegateTask.getExecution().getProcessBusinessKey());
    }

    protected void doCalculateNextProcUnitHandlers(DelegateExecution delegateExecution) {
        checkLicense();
        String processDefinitionKey = processFun.getProcessApprovalDefinitionKey(delegateExecution.getProcessDefinitionId());
        if (getApprovalParameter().isQueryAdvanceProcessAction()) {
            String nextProcUnitId = this.getNextProcUnitId(delegateExecution);
            calculateNextProcUnitHandlersForQueryAdvance(delegateExecution, processDefinitionKey, nextProcUnitId);
            return;
        }
        List<ActivityImpl> outgoingUserTasks = findOutgoingUserTasks(delegateExecution);
        calculateNextProcUnitHandlersForAdvance(processDefinitionKey, outgoingUserTasks, delegateExecution.getProcessBusinessKey());
    }

    protected boolean getOtherProcUnitHandleCompleted(DelegateTask delegateTask) {
        return false;
    }

    /**
     * 填充下一组的处理人
     * <p>
     * 1、当前环节没有审批完，获取当前环节的下一组审批人
     * <p>
     * 2、当前环节已审批完成，获取下一环节第一组审批人
     *
     * @param delegateTask 代理任务
     */
    protected void fillNextGroupHandlers(DelegateTask delegateTask) {
        Boolean needApprove = ClassHelper.convert(delegateTask.getVariable("needApprove"), Boolean.class, true);
        Boolean currentGroupChiefApprovePassed = ClassHelper.convert(delegateTask.getVariable(CHIEF_APPROVE_PASSED_VARIABLE), Boolean.class, false);

        if ((this.isApplyProcUnit(delegateTask) && needApprove) || (this.isApprovalProcUnit(delegateTask) && currentGroupChiefApprovePassed)
                || getOtherProcUnitHandleCompleted(delegateTask)) {
            // 环节处理完成后，发抄送任务
            if (this.isApprovalProcUnit(delegateTask)) {
                doMakeACopyFor(delegateTask);
            }
            // 填充下下一环节处理人
            doFillNextGroupHandlers(delegateTask, getNextProcUnitId(delegateTask));
        }
    }

    /**
     * 抄送
     */
    protected void doMakeACopyFor(DelegateTask delegateTask) {
        String bizId = delegateTask.getExecution().getProcessBusinessKey();
        String currentProcUnitHandlerId = getApprovalParameter().getCurrentHandleId();
        List<ProcUnitHandler> ccProcUnitHandlers = this.procUnitHandlerApplication.queryCCProcUnitHandlers(bizId, delegateTask.getTaskDefinitionKey(),
                currentProcUnitHandlerId);

        if (ccProcUnitHandlers.size() > 0) {
            List<String> ccIds = new ArrayList<String>(ccProcUnitHandlers.size());
            for (ProcUnitHandler cc : ccProcUnitHandlers) {
                ccIds.add(cc.getFullId());
            }
            this.workflowService.makeACopyFor(delegateTask.getId(), ccIds);
        }
    }

    /**
     * 保存逻辑处理
     *
     * @param delegateTask 代理任务
     */
    protected void onSave(DelegateTask delegateTask) {
    }

    /**
     * 提交事件
     *
     * @param delegateTask 代理任务
     */
    protected void onAdvance(DelegateTask delegateTask) {

    }

    /**
     * 抓回逻辑处理
     *
     * @param delegateTask   代理任务
     * @param destActivityId 目标环境id
     */
    protected void onWithdraw(DelegateTask delegateTask, String destActivityId) {

    }

    protected void onBack(DelegateTask delegateTask, String destActivityId) {

    }

    @Deprecated
    protected void onAbort(DelegateTask delegateTask) {

    }

    protected void onTransmit(DelegateTask delegateTask) {

    }

    private String getDestActivityId() {
        return ClassHelper.convert(this.getBizAndApprovalData().getProperty("destActivityId"), String.class);
    }

    /**
     * 审批不同意处理类别
     *
     * @return
     */
    protected ApproveNotPassedHandleKind getApproveNotPassedHandleKind() {
        String kind = SystemCache.getParameter("ApproveNotPassedHandleKind", String.class);
        if (StringUtil.isBlank(kind)) {
            return ApproveNotPassedHandleKind.ABORT;
        }
        if (kind.equalsIgnoreCase("back")) {
            return ApproveNotPassedHandleKind.BACK_TO_APPLY;
        }
        if (kind.equalsIgnoreCase("continue")) {
            return ApproveNotPassedHandleKind.CONTINUE;
        }
        return ApproveNotPassedHandleKind.ABORT;
    }

    private boolean fireEvent(DelegateTask delegateTask) {
        boolean result = false;
        ApprovalParameter approvalParameter = getApprovalParameter();

        switch (approvalParameter.getProcessAction()) {
            case ProcessAction.WITHDRAW:// 回收
                boolean mainWithdrawTask = this.getBizAndApprovalData().getProperty("mainWithdrawTask", Boolean.class);
                delegateTask.setVariable(CHIEF_APPROVE_PASSED_VARIABLE, mainWithdrawTask);

                if (mainWithdrawTask) {
                    fillWithdrawTaskProcUnitHandlers(delegateTask);
                    onWithdraw(delegateTask, getDestActivityId());
                }
                // 删除审批人的审批信息
                // this.procUnitHandlerApplication.deleteProcUnitHandlersByBizId(this.getBizId());
                result = true;
                break;
            case ProcessAction.BACK:// 回退
                // 主回退任务
                boolean mainBackTask = this.getBizAndApprovalData().getProperty("mainBackTask", Boolean.class);
                delegateTask.setVariable(CHIEF_APPROVE_PASSED_VARIABLE, mainBackTask);

                if (mainBackTask) {
                    if (this.isBackSaveBizData()) {
                        this.saveBizAndApprovalData(delegateTask);
                    } else {
                        this.updateProcUnitHandlerResult();
                    }
                    fillBackProcUnitHandlers(delegateTask);
                    onBack(delegateTask, this.getDestActivityId());
                }
                result = true;
                break;
            case ProcessAction.REPLENISH:// 打回
                delegateTask.setVariable(CHIEF_APPROVE_PASSED_VARIABLE, false);

                Boolean isOtherPreemptModelTask = delegateTask.getVariableLocal("otherPreemptModelTask", Boolean.class);
                if (isOtherPreemptModelTask == null) {
                    isOtherPreemptModelTask = false;
                }
                if (Boolean.FALSE.equals(isOtherPreemptModelTask)) {
                    if (this.isBackSaveBizData()) {
                        this.saveBizAndApprovalData(delegateTask);
                    } else {
                        this.updateProcUnitHandlerResult();
                    }
                }

                result = true;
                break;
            case ProcessAction.TRANSMIT:
                delegateTask.setVariable(CHIEF_APPROVE_PASSED_VARIABLE, false);

                if (!StringUtil.isBlank(this.getBizAndApprovalData().getProperty(ProcessTaskContants.BIZ_ID, String.class, ""))) {
                    this.saveBizAndApprovalData(delegateTask);
                }
                onTransmit(delegateTask);
                result = true;
                break;
            case ProcessAction.ADVANCE:
            case ProcessAction.QUERY_ADVANCE:
                if (isPreemptCanceledTask(delegateTask)) {
                    result = true;
                    break;
                }
                if (isLimitHandlerCanceledTask(delegateTask)) {
                    result = true;
                    break;
                }
                String bizId = approvalParameter.getBizId();
                if (StringUtil.isNotBlank(bizId)) {
                    if (approvalParameter.isQueryAdvanceProcessAction() || approvalParameter.getOnlyAdvance()) {
                        this.updateProcUnitHandlerResult();
                    } else {
                        saveBizAndApprovalData(delegateTask);
                    }

                    // TODO
                    // this.procUnitHandlerApplication.getHistoricProcUnitHandlerService().updateHistoricProcUnitHandlerInstVersion(bizId,
                    // this.getProcUnitId());
                }

                boolean isApproveNotPassedAbort = this.isApprovalProcUnit(delegateTask) && !this.approvePassed()
                        && (getApproveNotPassedHandleKind() == ApproveNotPassedHandleKind.ABORT);
                if (isApproveNotPassedAbort) {
                    List<String> handlers = new ArrayList<String>(1);
                    delegateTask.setVariable("handlerList", handlers);

                    String procUnitHandlerId = approvalParameter.getCurrentHandleId();
                    ProcUnitHandler procUnitHandler = procUnitHandlerApplication.loadProcUnitHandler(procUnitHandlerId);

                    Assert.notNull(procUnitHandler, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, procUnitHandlerId, "流程环节处理人"));

                    delegateTask.setVariable("isPreempt", procUnitHandler.isPreempt());
                    delegateTask.setVariable("preemptTaskId", delegateTask.getId());

                    this.workflowService.deleteProcessInstance(delegateTask.getProcessInstanceId(), "approveNotPass");
                    this.onAbortProcessInstance(delegateTask.getExecution());

                    if (procUnitHandler.isPreempt()) {
                        this.procUnitHandlerApplication.updateOtherProcUnitHandlersResultSystemComplete(this.getBizId(), delegateTask.getTaskDefinitionKey(),
                                this.getCurrentHandleGroupId(), procUnitHandlerId, null);
                    }

                    result = true;
                    break;
                }

                onAdvance(delegateTask);
                break;
        }

        if (result) {
            ProcessEventContext.addCompletedTask(delegateTask.getId());
        }

        return result;
    }

    /**
     * 任务结束事件
     * <ul>
     * <li>保存业务数据
     * <li>结束前事件
     * <li>计算并填充流出节点的处理人
     * </ul>
     *
     * @param delegateTask 代理人
     */
    public void onComplete(DelegateTask delegateTask) {
        if (fireEvent(delegateTask)) {
            return;
        }

        onBeforeComplete(delegateTask);

        if (!approvePassed() && getApproveNotPassedHandleKind() == ApproveNotPassedHandleKind.ABORT) {
            return;
        }

        buildManualProcUnitId(delegateTask);
        if (isSelectEndFlowNode()) {
            return;
        }

        String nextProcUnitId = this.getNextProcUnitId(delegateTask);
        boolean isLeaveCurrentProcUnit = !delegateTask.getTaskDefinitionKey().equalsIgnoreCase(nextProcUnitId);
        if (isLeaveCurrentProcUnit) {
            getApprovalParameter().setCurrentHandleGroupId(0);
        }
        // 审批流和业务流通过审批人规则计算审批人
        switch (this.getFlowKind()) {
            case APPROVAL:
            case BUSINESS:
            case FREE:
                Boolean needApprove = ClassHelper.convert(delegateTask.getVariable("needApprove"), Boolean.class, true);
                if ((this.isApplyProcUnit(delegateTask) && needApprove) || (loopApprovalFinished(delegateTask) && isLeaveCurrentProcUnit)) {
                    if (this.getFlowKind() == ProcessKind.FREE) {
                        RuntimeTaskExtension runtimeTaskExtension = this.actApplication.loadRuntimeTaskExtension(this.getTaskId());
                        if (ProcessAction.RECALL_PROCESS_INSTANCE.equals(runtimeTaskExtension.getGenerateReason())) {
                            // 撤销自由流程,删除处理人
                            this.procUnitHandlerApplication.deleteProcUnitHandlersByBizId(this.getBizId());
                        } else if (ProcessAction.WITHDRAW.equals(runtimeTaskExtension.getGenerateReason())) {
                            // 回收任务，删除后继处理人(A--(B,C,D)-->A'; A'-->B-->A
                            HistoricTaskInstanceExtension historicTaskInstanceExtension = this.actApplication.loadHistoricTaskInstanceExtension(runtimeTaskExtension.getPreviousId());
                            this.procUnitHandlerApplication.deleteWithdrawSucceedingHandlers(this.getBizId(), historicTaskInstanceExtension.getPreviousId());
                        }
                    }
                    doCalculateNextProcUnitHandlers(delegateTask);
                }
                break;
            default:
                break;
        }
        fillNextGroupHandlers(delegateTask);

        ProcessEventContext.addCompletedTask(delegateTask.getId());

        onAfterComplete(delegateTask);
    }

    /**
     * 填充回退环节处理人
     */
    protected void fillBackProcUnitHandlers(DelegateTask delegateTask) {
        if ((this.isApplyProcUnit(getDestActivityId()) && this.getFlowKind() == ProcessKind.FREE)) {
            this.procUnitHandlerApplication.deleteProcUnitHandlersByBizAndGroupId(delegateTask.getExecution().getProcessBusinessKey(), 0);
        } else if (this.isApprovalProcUnit(getDestActivityId())) {
            SDO sdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
            String backProcUnitHandlerId = sdo.getString("backProcUnitHandlerId");

            Assert.hasText(backProcUnitHandlerId, "回退环节处理人ID不能为空。");

            List<String> handlers = new ArrayList<String>(1);
            handlers.add(String.valueOf(backProcUnitHandlerId));
            delegateTask.setVariable("handlerList", handlers);
            ProcUnitHandler procUnitHandler = this.procUnitHandlerApplication.loadProcUnitHandler(backProcUnitHandlerId);
            Assert.notNull(procUnitHandler, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, backProcUnitHandlerId, "审批处理人"));
            // 更新回退处理人的处理状态=未处理
            this.procUnitHandlerApplication.updateProcUnitHandlerStatus(procUnitHandler, ProcUnitHandler.Status.READY);

            if (this.getFlowKind() == ProcessKind.FREE) {
                // 删除后续审批人的审批信息
                this.procUnitHandlerApplication.deleteProcUnitHandlersByBizAndGroupId(procUnitHandler.getBizId(), procUnitHandler.getGroupId());
            } else {
                // 更新后续组的处理状态=未处理
                this.procUnitHandlerApplication.updateSucceedingProcUnitHandlersStatus(procUnitHandler, ProcUnitHandler.Status.READY);
            }
            delegateTask.setVariable(FIRE_PROC_UNIT_HANDLER_ID, procUnitHandler.getId());
        }
    }

    protected void fillWithdrawTaskProcUnitHandlers(DelegateTask delegateTask) {
        if (!ActivityKind.APPLY.equalsIgnoreCase(getDestActivityId())) {
            SDO sdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
            String procUnitHandlerId = sdo.getString("procUnitHandlerId");
            ProcUnitHandler procUnitHandler = procUnitHandlerApplication.loadProcUnitHandler(procUnitHandlerId);

            List<String> handlers = new ArrayList<String>(1);
            handlers.add(procUnitHandler.getId().toString());
            delegateTask.setVariable("handlerList", handlers);

            this.procUnitHandlerApplication.updateProcUnitHandlerStatus(procUnitHandler, ProcUnitHandler.Status.READY);
            delegateTask.setVariable(FIRE_PROC_UNIT_HANDLER_ID, procUnitHandler.getId());
        }
    }

    private List<String> buildNextActivityGroupHandlers(String bizId, String nextProcUnitId, Integer currentGroupId) {
        List<ProcUnitHandler> procUnitHandlers = procUnitHandlerApplication.queryNextGroupProcUnitHandlers(bizId, nextProcUnitId, currentGroupId);
        List<String> handlers = new ArrayList<String>(procUnitHandlers.size());
        for (ProcUnitHandler procUnitHandler : procUnitHandlers) {
            handlers.add(procUnitHandler.getId().toString());
        }
        return handlers;
    }

    /**
     * 填充流程环节handlerList变量
     * <p>
     * 取出下一组审批人列表，放入到handlerList中
     *
     * @param delegateTask   代理任务
     * @param nextProcUnitId 下一环节ID
     */
    protected void fillHandlerListVariable(DelegateTask delegateTask, String nextProcUnitId) {
        List<String> handlers = buildNextActivityGroupHandlers(delegateTask.getExecution().getProcessBusinessKey(), nextProcUnitId, getCurrentHandleGroupId());
        Assert.isTrue(handlers.size() > 0, " 填充handlerList变量出错，未找到处理人。");
        delegateTask.setVariable("handlerList", handlers);
    }

    protected void fillHandlerListVariable(DelegateExecution delegateExecution, String nextProcUnitId) {
        List<String> handlers = buildNextActivityGroupHandlers(delegateExecution.getProcessBusinessKey(), nextProcUnitId, getCurrentHandleGroupId());
        delegateExecution.setVariable("handlerList", handlers);
    }

    /**
     * 填充下一组处理人
     * <p>
     * 1、当前环节没有审批完，获取当前环节的下一组审批人
     * <p>
     * 2、当前环节已审批完成，获取下一环节第一组审批人
     *
     * @param delegateTask   代理任务
     * @param nextProcUnitId 下一环节ID
     */
    protected void doFillNextGroupHandlers(DelegateTask delegateTask, String nextProcUnitId) {
        if (StringUtil.isNotBlank(nextProcUnitId)) {
            if (!loopApprovalFinished(delegateTask)
                    || (loopApprovalFinished(delegateTask) && !delegateTask.getTaskDefinitionKey().equalsIgnoreCase(nextProcUnitId))) {
                fillHandlerListVariable(delegateTask, nextProcUnitId);
            }
        }
    }

    protected boolean isAssistTask(DelegateTask delegateTask) {
        HistoricTaskInstanceExtension historicTaskInstExtension = this.actApplication.loadHistoricTaskInstanceExtension(delegateTask.getId());
        String cooperationModelId = historicTaskInstExtension.getCooperationModelId();
        return CooperationModelKind.ASSISTANT.equals(cooperationModelId);
    }

    /**
     * 终止保存业务数据
     *
     * @return
     */
    protected boolean isAbortSaveBizData() {
        return false;
    }

    protected boolean isBackSaveBizData() {
        return getApprovalParameter().getBackSaveBizData();
    }

    /**
     * 任务删除事件
     *
     * @param delegateTask 代理任务
     */
    protected void onDelete(DelegateTask delegateTask) {
        // 审批未通过终止，删除流程实例的时候，任务已删除。
        if (delegateTask.hasVariable(ABORT_TASK_ID_VARIABLE) && delegateTask.getVariable(ABORT_TASK_ID_VARIABLE).toString().equals(delegateTask.getId())) {
            return;
        }

        TaskStatus status = TaskStatus.COMPLETED;
        String deleteReason = null;
        ApprovalParameter approvalParameter = getApprovalParameter();

        switch (approvalParameter.getProcessAction()) {
            case ProcessAction.ADVANCE:
            case ProcessAction.QUERY_ADVANCE:
                boolean isApproveNotPassedAbort = this.isApprovalProcUnit(delegateTask) && !this.approvePassed()
                        && (getApproveNotPassedHandleKind() == ApproveNotPassedHandleKind.ABORT);
                if (isApproveNotPassedAbort) {
                    // 审批未通过 终止
                    status = TaskStatus.ABORTED;
                    boolean isPreempt = delegateTask.getVariable("isPreempt", Boolean.class);
                    String preemptTaskId = delegateTask.getVariable("preemptTaskId", String.class);
                    if (isPreempt && !delegateTask.getId().equals(preemptTaskId)) {
                        status = TaskStatus.CANCELED;
                    }
                    // boolean deleteProcessInstance = ClassHelper.convert(delegateTask.getVariable("deleteProcessInstance"), Boolean.class, false);
                    // if (!deleteProcessInstance) {
                    // delegateTask.setVariable("deleteProcessInstance", true);
                    // delegateTask.setVariable(ABORT_TASK_ID_VARIABLE, delegateTask.getId());
                    // this.workflowService.deleteProcessInstance(delegateTask.getProcessInstanceId(), "approveNotPass");
                    // }
                    // if (!deleteProcessInstance) {
                    // this.onAbortProcessInstance(delegateTask.getExecution());
                    // }
                } else if (this.isAssistTask(delegateTask)) {
                    if (delegateTask.getId().equals(this.getApprovalParameter().getTaskId())) {
                        status = TaskStatus.COMPLETED;
                    } else {
                        status = TaskStatus.CANCELED;
                    }
                } else if (isPreemptCanceledTask(delegateTask)) {// 抢占取消任务
                    status = TaskStatus.CANCELED;
                }
                break;
            case ProcessAction.TRANSMIT:
                status = TaskStatus.TRANSMITED;
                break;
            case ProcessAction.BACK:
            case ProcessAction.REPLENISH:
                status = TaskStatus.RETURNED;

                Boolean isOtherPreemptModelTask = delegateTask.getVariableLocal("otherPreemptModelTask", Boolean.class);
                if (approvalParameter.getProcessAction().equals(ProcessAction.REPLENISH) && Boolean.TRUE.equals(isOtherPreemptModelTask)) {
                    status = TaskStatus.CANCELED;
                }
                break;
            case ProcessAction.ASSIST:
                // 删除协审人员
                status = TaskStatus.ABORTED;
                break;
            case ProcessAction.DELETE_PROCESS_INSTANCE:
                deleteReason = ProcessAction.DELETE_PROCESS_INSTANCE;
                status = TaskStatus.DELETED;
                break;
            case ProcessAction.RECALL_PROCESS_INSTANCE:
                deleteReason = ProcessAction.RECALL_PROCESS_INSTANCE;
                status = TaskStatus.CANCELED;
                break;
            case ProcessAction.WITHDRAW:
                deleteReason = ProcessAction.WITHDRAW;
                status = TaskStatus.CANCELED;
                break;
            case ProcessAction.ABORT_PROCESS_INSTANCE:
                status = TaskStatus.ABORTED;
                break;
            default:
                throw new ApplicationException(String.format("无效的流程命令“%s”。", approvalParameter.getProcessAction()));
        }

        if (deleteReason == null) {
            deleteReason = status.getId();
        }

        this.actApplication.updateHistoricTaskInstanceExtensionEnded(delegateTask.getId(), status, deleteReason);
        this.actApplication.deleteRuntimeTaskExtension(delegateTask.getId());
    }

    /**
     * 保存业务数据
     *
     * @param delegateTask 代理任务
     */
    protected String onSaveBizData(DelegateTask delegateTask) {
        return saveBizAndApprovalData(delegateTask);
    }

    /**
     * 检查审批规则处理人不能为空
     */
    private void checkApprovalRuleHandlerNotNull(ApprovalRule approvalRule) {
        // 检查审批人是否为空
        List<ApprovalRuleHandler> approvalRuleHandlers = approvalRule.getApprovalRuleHandlers();
        ApprovalRuleHandler approvalRuleHandler;
        List<ApprovalRuleHandler> ignoredHandlers = new ArrayList<ApprovalRuleHandler>();
        String errorMsgFormat = "审批环节“%s”，审批规则名称“%s”，未找到处理人。";
        for (int i = 0; i < approvalRuleHandlers.size(); i++) {
            approvalRuleHandler = approvalRuleHandlers.get(i);
            if (approvalRuleHandler.isSelection()) {
                continue;
            }

            // if (approvalRuleHandler.getHandlerKind() != HandlerKind.SEGMENTATION && approvalRuleHandler.getOrgUnits().size() == 0 && !ignoreNotFoundHandler()) {
            if (approvalRuleHandler.getOrgUnits().size() == 0) {
                if (approvalRuleHandler.isMustPassed() && !ignoreNotFoundHandler()) {
                    throw new ApplicationException(String.format(errorMsgFormat, approvalRuleHandler.getDescription(), approvalRule.getName()));
                } else {
                    ignoredHandlers.add(approvalRuleHandler);
                }
            }
        }

        errorMsgFormat = "审批规则名称“%s”，未找到处理人。";
        Assert.state(approvalRuleHandlers.size() > ignoredHandlers.size(), String.format(errorMsgFormat, approvalRule.getName()));

    }

    /**
     * 是否排序处理人
     * <p>
     * 用于预览处理人
     *
     * @return
     */
    protected boolean isSortHandlers() {
        return false;
    }

    /**
     * 生成任务执行模型
     *
     * @param approvalRule 审批规则
     * @param groupId      分组ID
     * @param handler      处理人
     */
    private void buildTaskExecuteMode(ApprovalRule approvalRule, Integer groupId, Map<String, Object> handler) {
        ApprovalRuleHandlerGroup approvalRuleHandlerGroup = approvalRule.findApprovalRuleHandlerGroup(groupId);
        if (approvalRuleHandlerGroup != null && approvalRuleHandlerGroup.getTaskExecuteMode() != null) {
            handler.put("taskExecuteMode", approvalRuleHandlerGroup.getTaskExecuteMode());
        }
    }

    /**
     * 生成环节预览处理人
     *
     * @param processDefinitionKey 流程定义ID
     * @param procUnitId           流程环节ID
     * @param procUnitName         流程环节名称
     * @param hasGatewayManual     是否有选择网关
     * @param bizParams            业务参数
     * @param handlers             处理人
     * @return
     */
    protected boolean buildProcUnitHandlersForQuery(DelegateTask delegateTask, String processDefinitionKey, String procUnitId, String procUnitName,
                                                    boolean hasGatewayManual, Map<String, Object> bizParams, List<Map<String, Object>> handlers) {
        boolean hasSelection = false;
        int sequence = 1, lastGroupId = 0, index = 0;
        String procUnitHandlerChiefId = "";

        buildProcApprovalOperator();

        ApprovalRule approvalRule = procApprovalRuleParseService.execute(processDefinitionKey, procUnitId, bizParams);
        Assert.isTrue(approvalRule != null, "未找到审批规则。");
        List<ApprovalRuleHandler> approvalRuleHandlers = approvalRule.getApprovalRuleHandlers();
        Assert.isTrue(approvalRuleHandlers != null && approvalRuleHandlers.size() > 0, "审批规则未配置处理人。");

        checkApprovalRuleHandlerNotNull(approvalRule);

        // 附加处理人
        List<Map<String, Object>> additionalHandlers = getAdditionalHandlers();
        if (additionalHandlers != null) {
            addAdditionalHandlers(this.getBizId(), handlers, additionalHandlers, procUnitId);
        }
        Map<String, Object> handler;
        for (int i = 0; i < approvalRuleHandlers.size(); i++) {
            ApprovalRuleHandler chiefHandler = approvalRuleHandlers.get(i);
            switch (chiefHandler.getHandlerKind()) {
                case MANUAL_SELECTION:
                case SCOPE_SELECTION:
                    hasSelection = hasSelection || true;
                    handler = buildProcUnitHandler(approvalRule, chiefHandler, this.getBizId(), procUnitId, procUnitName, null, sequence);
                    procUnitHandlerChiefId = CommonUtil.createGUID();
                    handler.put("isMustPass", approvalRuleHandlers.get(i).getMustPass());
                    handler.put("id", procUnitHandlerChiefId);

                    if (hasGatewayManual) {
                        handler.put("showRadio", "true");
                    }

                    if (chiefHandler.getHandlerKind() == HandlerKind.SCOPE_SELECTION) {
                        chiefHandler.setHandlerKindCode(HandlerKind.MANAGER_FUN.getId());
                        List<Map<String, Object>> scopeData = new ArrayList<Map<String, Object>>(chiefHandler.getOrgUnits().size());
                        for (OrgUnit orgUnit : chiefHandler.getOrgUnits()) {
                            Map<String, Object> scopeItem = new HashMap<String, Object>();

                            scopeItem = buildProcUnitHandler(approvalRule, chiefHandler, this.getBizId(), procUnitId, procUnitName, orgUnit, sequence);
                            procUnitHandlerChiefId = CommonUtil.createGUID();
                            scopeItem.put("id", procUnitHandlerChiefId);
                            scopeData.add(scopeItem);

                            sequence++;
                        }
                        handler.put("scopeData", scopeData);
                        chiefHandler.setHandlerKindCode(HandlerKind.SCOPE_SELECTION.getId());
                    }
                    handlers.add(handler);
                    break;
                default:
                    if (chiefHandler.getOrgUnits().size() == 0) {
                        continue;
                    }

                    if (lastGroupId != chiefHandler.getGroupId()) {
                        lastGroupId = chiefHandler.getGroupId();
                        sequence = 1;
                    }
                    // 主审人
                    for (OrgUnit orgUnit : chiefHandler.getOrgUnits()) {
                        handler = buildProcUnitHandler(approvalRule, chiefHandler, this.getBizId(), procUnitId, procUnitName, orgUnit, sequence);

                        if (hasGatewayManual && index == 0) {
                            handler.put("showRadio", "true");
                        }

                        procUnitHandlerChiefId = CommonUtil.createGUID();
                        handler.put("id", procUnitHandlerChiefId);
                        handlers.add(handler);

                        sequence++;
                        index++;
                    }

                    // 协审人
                    ApprovalRuleHandlerAssist assistHandler;
                    Map<String, Object> assistHandlerData;
                    for (int j = 0; j < chiefHandler.getAssists().size(); j++) {
                        assistHandler = chiefHandler.getAssists().get(j);
                        for (OrgUnit orgUnit : assistHandler.getOrgUnits()) {
                            assistHandlerData = this.buildProcUnitHandlerAssists(approvalRule, chiefHandler, assistHandler, this.getBizId(), procUnitId, orgUnit,
                                    sequence, j + 1, assistHandler.getKindId(), procUnitHandlerChiefId);

                            handlers.add(assistHandlerData);

                        }
                    }
            }

        }

        return hasSelection;
    }

    private Map<String, Object> buildEndHandlersForQuery(String procUnitId) {
        Map<String, Object> result = new HashMap<String, Object>(5);
        result.put("bizId", this.getBizId());
        result.put("procUnitId", procUnitId);
        result.put("procUnitName", "结束");
        result.put("isEndFlowNode", "true");
        result.put("showRadio", "true");
        result.put(PROC_UNIT_SEQUENCE_FIELD, 9999);

        return result;
    }

    /**
     * 预览处理人事件
     *
     * @param delegateTask 代理任务
     */
    @SuppressWarnings("unchecked")
    protected void onQueryHandlers(DelegateTask delegateTask) {
        // 校验数据完整性
        this.onCheckConstraints(delegateTask);
        String processDefinitionKey = processFun.getProcessApprovalDefinitionKey(delegateTask.getProcessDefinitionId());
        Map<String, Object> bizParams = getProcessBizParams(this.getBizId());

        boolean hasSelection = false, currentProcUnitHasSelection;
        Map<String, Object> data = processFun.queryNextProcUnits(delegateTask);

        Map<String, FlowNode> nextProcUnits = (Map<String, FlowNode>) data.get("nextProcUnits");
        boolean hasGatewayManual = ClassHelper.convert(data.get(HAS_GATEWAY_MANUAL_FIELD), Boolean.class, false) && nextProcUnits.size() > 1;

        boolean needSortProcUnit = (hasGatewayManual && nextProcUnits.size() > 1);

        List<ProcDefinition> procUnits = null;
        if (needSortProcUnit) {
            procUnits = this.procDefinitionApplication.queryProcUnitsForSequence(processDefinitionKey);
        }

        Integer porcUnitSequence = 0;

        List<Map<String, Object>> handlers = new ArrayList<>(nextProcUnits.size());

        List<Map<String, Object>> currentProcUnitHandlers;
        for (String procUnitId : nextProcUnits.keySet()) {
            currentProcUnitHandlers = new ArrayList<>();
            if (hasGatewayManual) {
                if (nextProcUnits.get(procUnitId) instanceof EndEvent) {
                    handlers.add(buildEndHandlersForQuery(procUnitId));
                    continue;
                }
            }

            currentProcUnitHasSelection = buildProcUnitHandlersForQuery(delegateTask, processDefinitionKey, procUnitId,
                    nextProcUnits.get(procUnitId).getName(), hasGatewayManual, bizParams,
                    currentProcUnitHandlers);
            hasSelection = hasSelection || currentProcUnitHasSelection;
            // 排除相同人员
            this.mergeHandler(processDefinitionKey, procUnitId, currentProcUnitHandlers);
            // excludeSameHandler(currentProcUnitHandlers);
            // 排除指定的人员
            excludeSpecifiedHandler(this.getBizId(), currentProcUnitHandlers);

            if (needSortProcUnit) {
                for (ProcDefinition procDefinition : procUnits) {
                    if (procUnitId.equalsIgnoreCase(procDefinition.getCode())) {
                        porcUnitSequence = procDefinition.getSequence();
                        break;
                    }
                }
                for (Map<String, Object> item : currentProcUnitHandlers) {
                    item.put(PROC_UNIT_SEQUENCE_FIELD, porcUnitSequence);
                }
            }

            handlers.addAll(currentProcUnitHandlers);
        }
        Map<String, Object> result = new HashMap<String, Object>(3);

        result.put(Constants.ROWS, handlers);
        result.put(HAS_SELECTION_FIELD, hasSelection);
        result.put(HAS_GATEWAY_MANUAL_FIELD, Boolean.toString(hasGatewayManual));
        result.put(PROC_APPROVAL_RULE_FULL_NAME_FIELD, this.getBizAndApprovalData().getProperty(PROC_APPROVAL_RULE_FULL_NAME_FIELD));

        String queryHandlerShowField = SystemCache.getParameter(QUERY_HANDLER_SHOW_FIELD, String.class);
        QueryHandlerShowFieldKind queryHandlerShowFieldKind = QueryHandlerShowFieldKind.fromId(queryHandlerShowField);

        result.put(QUERY_HANDLER_SHOW_FIELD, queryHandlerShowFieldKind.getId());

        // 排序
        if (isSortHandlers()) {
            bubbleSortHandlers(handlers, GROUP_ID_FIELD);
        }

        if (needSortProcUnit) {
            bubbleSortHandlers(handlers, PROC_UNIT_SEQUENCE_FIELD);
            bubbleSortHandlersForProcUnitGroupSequence(handlers);
            if (handlers.size() > 0) {
                handlers.get(0).put("checked", true);
            }
        }

        ThreadLocalUtil.putVariable("queryHandlers", result);
    }

    /**
     * 冒泡排序处理人
     *
     * @param handlers  处理人
     * @param sortField 排序字段
     */
    private void bubbleSortHandlers(List<Map<String, Object>> handlers, String sortField) {
        Integer currentValue, nextValue;
        Map<String, Object> temp;
        for (int i = 0; i < handlers.size(); i++) {
            currentValue = ClassHelper.convert(handlers.get(i).get(sortField), Integer.class);
            for (int j = i + 1; j < handlers.size(); j++) {
                nextValue = ClassHelper.convert(handlers.get(j).get(sortField), Integer.class);
                if (currentValue > nextValue) {
                    temp = handlers.get(j);
                    handlers.set(j, handlers.get(i));
                    handlers.set(i, temp);
                    currentValue = nextValue;
                }
            }
        }
    }

    private void bubbleSortHandlersForProcUnitGroupSequence(List<Map<String, Object>> handlers) {
        Integer currentProcUnitGroupId, nextProcUnitGroupId, currentProcUnitSequence, nextProcUnitSequence, currentValue, nextValue;
        Map<String, Object> temp;
        currentProcUnitSequence = -1;
        for (int i = 0; i < handlers.size(); i++) {
            currentProcUnitSequence = ClassHelper.convert(handlers.get(i).get(PROC_UNIT_SEQUENCE_FIELD), Integer.class);

            currentProcUnitGroupId = ClassHelper.convert(handlers.get(i).get(GROUP_ID_FIELD), Integer.class, 9999) * 100;
            currentValue = ClassHelper.convert(handlers.get(i).get(SEQUENCE_FIELD), Integer.class);
            for (int j = i + 1; j < handlers.size(); j++) {
                nextProcUnitSequence = ClassHelper.convert(handlers.get(j).get(PROC_UNIT_SEQUENCE_FIELD), Integer.class);
                if (!currentProcUnitSequence.equals(nextProcUnitSequence)) {
                    i = j - 1;
                    break;
                }

                nextProcUnitGroupId = ClassHelper.convert(handlers.get(j).get(GROUP_ID_FIELD), Integer.class) * 100;
                nextValue = ClassHelper.convert(handlers.get(j).get(SEQUENCE_FIELD), Integer.class);
                if (currentProcUnitGroupId + currentValue > nextProcUnitGroupId + nextValue) {
                    temp = handlers.get(j);
                    handlers.set(j, handlers.get(i));
                    handlers.set(i, temp);
                    currentValue = nextValue;
                }
            }

        }
    }

    /**
     * 得到流程业务参数
     *
     * @param bizId 业务ID
     * @return 业务参数
     */
    protected Map<String, Object> getProcessBizParams(String bizId) {
        return null;
    }

    /**
     * 得到当前Activity
     *
     * @param processDefinitionId 流程定义ID
     * @return activity 环节ID
     */
    protected ActivityImpl getCurrentActivity(String processDefinitionId, String activityId) {
        ProcessDefinition processDefinition = getRepositoryService().getProcessDefinition(processDefinitionId);
        if (processDefinition == null) {
            throw new ActivitiException(MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_BIZ_ID, "流程实例"));
        }

        ActivityImpl activity = ((ScopeImpl) processDefinition).findActivity(activityId);
        if (activity == null) {
            throw new ActivitiException("流程定义中，未找到流程环节“" + activityId + "”");
        }
        return activity;
    }

    /**
     * 是否申请环节
     *
     * @param delegateTask 代理任务
     * @return 是否申请环节
     */
    protected boolean isApplyProcUnit(DelegateTask delegateTask) {
        ActivityImpl activity = getCurrentActivity(delegateTask.getProcessDefinitionId(), delegateTask.getTaskDefinitionKey());
        for (PvmTransition pvmTransition : activity.getIncomingTransitions()) {
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
            ActivityImpl activityImpl = transitionImpl.getSource();
            String type = (String) activityImpl.getProperty("type");
            if (ActivityKind.START_EVENT.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isApplyProcUnit(String procUnitId) {
        return ActivityKind.APPLY.equalsIgnoreCase(procUnitId);
    }

    private void iterateOutgoingUserTasks(DelegateExecution delegateExecution, ActivityImpl startActivity, ActivityImpl currentActivity,
                                          boolean includeStartActivity, List<ActivityImpl> result) {
        for (PvmTransition pvmTransition : currentActivity.getOutgoingTransitions()) {
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;

            Condition condition = (Condition) transitionImpl.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
            if (condition != null) {
                boolean conditionValue = condition.evaluate(transitionImpl.getId(), delegateExecution);
                if (!conditionValue) {
                    continue;
                }
            }

            ActivityImpl activityImpl = transitionImpl.getDestination();
            String type = (String) activityImpl.getProperty("type");

            if (ActivityKind.USER_TASK.equalsIgnoreCase(type)) {
                // 循环审批
                if (startActivity.getId().equalsIgnoreCase(activityImpl.getId())) {
                    if (includeStartActivity) {
                        result.add(activityImpl);
                    }
                } else {
                    result.add(activityImpl);
                }
            } else if (ActivityKind.EXCLUSIVE_GATEWAY.equalsIgnoreCase(type)) {
                iterateOutgoingUserTasks(delegateExecution, startActivity, activityImpl, includeStartActivity, result);
            }
        }
    }

    protected void iterateOutgoingUserTasks(DelegateExecution delegateExecution, ActivityImpl startActivity, ActivityImpl currentActivity,
                                            List<ActivityImpl> result) {
        iterateOutgoingUserTasks(delegateExecution, startActivity, currentActivity, false, result);
    }

    protected List<ActivityImpl> findOutgoingUserTasks(DelegateTask delegateTask) {
        List<ActivityImpl> result = new ArrayList<ActivityImpl>();
        ActivityImpl activity = getCurrentActivity(delegateTask.getProcessDefinitionId(), delegateTask.getTaskDefinitionKey());
        DelegateExecution delegateExecution = delegateTask.getExecution();
        iterateOutgoingUserTasks(delegateExecution, activity, activity, result);
        return result;
    }

    protected List<ActivityImpl> findOutgoingUserTasks(DelegateExecution delegateExecution) {
        List<ActivityImpl> result = new ArrayList<ActivityImpl>();
        ActivityImpl activity = getCurrentActivity(delegateExecution.getProcessDefinitionId(), delegateExecution.getCurrentActivityId());
        iterateOutgoingUserTasks(delegateExecution, activity, activity, result);
        return result;
    }

    protected ApprovalParameter getApprovalParameter() {
        ApprovalParameter approvalParameter = ThreadLocalUtil.getVariable(ApprovalParameter.APPROVAL_PARAMETER_KEY, ApprovalParameter.class);
        if (approvalParameter == null) {
            SDO params = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
            approvalParameter = ApprovalParameter.newInstance(params);
            ThreadLocalUtil.putVariable(ApprovalParameter.APPROVAL_PARAMETER_KEY, approvalParameter);
        }
        return approvalParameter;
    }

    protected Map<String, Object> internalBuildProcUnitHandler(String bizId, String procUnitId, String procUnitName, String subProcUnitId,
                                                               String subProcUnitName, String handleKindCode, String cooperationModelId, String chiefId,
                                                               OrgUnit orgUnit, String approvalRuleId, String approvalRuleHandlerId, Integer groupId,
                                                               Integer sequence, Integer assistantSequence) {
        Map<String, Object> orgInfo = new HashMap<String, Object>(8);
        Map<String, Object> result = new HashMap<String, Object>(21);

        String bizCode = getApprovalParameter().getBizCode();
        result.put("bizId", bizId);
        result.put("bizCode", bizCode);
        result.put("procUnitId", procUnitId);
        result.put("procUnitName", procUnitName);
        result.put("handleKindCode", handleKindCode);
        result.put("subProcUnitId", subProcUnitId == null ? "" : subProcUnitId);
        result.put("subProcUnitName", subProcUnitName);

        if (orgUnit != null) {
            // 范围选择 或 手工选择
            OpmUtil.buildOrgIdNameExtInfo(orgUnit.getFullId(), orgUnit.getFullName(), orgInfo);
            result.put("fullId", orgUnit.getFullId());
            result.put("fullName", orgUnit.getFullName());
            result.put("handlerId", orgInfo.get("psmId"));
            result.put("handlerName", orgInfo.get("psmName"));
            result.put("positionId", orgInfo.get("posId"));
            result.put("positionName", orgInfo.get("posName"));
            result.put("deptId", orgInfo.get("deptId"));
            result.put("deptName", orgInfo.get("deptName"));
            result.put("orgId", orgInfo.get("orgId"));
            result.put("orgName", orgInfo.get("orgName"));
        }

        result.put("cooperationModelId", cooperationModelId);
        result.put("chiefId", chiefId);
        // 审批规则处理人ID
        result.put("approvalRuleId", approvalRuleId);
        result.put("approvalRuleHandlerId", approvalRuleHandlerId);
        result.put(GROUP_ID_FIELD, groupId * 10);
        result.put("status", 0);
        result.put(SEQUENCE_FIELD, sequence);
        result.put("assistantSequence", assistantSequence);

        return result;
    }

    protected Map<String, Object> buildProcUnitHandlerAssists(ApprovalRule approvalRule, ApprovalRuleHandler approvalRuleHandler,
                                                              ApprovalRuleHandlerAssist approvalRuleHandlerAssist, String bizId, String procUnitId,
                                                              OrgUnit orgUnit, Integer sequence, int assistantSequence, String cooperationModelId,
                                                              String chiefId) {
        String approvalRuleId = approvalRule == null ? "" : approvalRule.getId();
        return internalBuildProcUnitHandler(bizId, procUnitId, "", "", approvalRuleHandlerAssist.getDescription(),
                approvalRuleHandlerAssist.getHandlerKindCode(), cooperationModelId, chiefId, orgUnit, approvalRuleId,
                approvalRuleHandler.getId(), approvalRuleHandler.getGroupId(), sequence, assistantSequence);
    }

    /**
     * 生成环节处理人
     *
     * @param approvalRule        审批规则
     * @param approvalRuleHandler 审批规则处理人
     * @param bizId               业务ID
     * @param procUnitId          流程环节ID
     * @param orgUnit             组织单元
     * @param sequence            序号
     * @return
     */
    protected Map<String, Object> buildProcUnitHandler(ApprovalRule approvalRule, ApprovalRuleHandler approvalRuleHandler, String bizId, String procUnitId,
                                                       String procUnitName, OrgUnit orgUnit, Integer sequence) {
        String approvalRuleId = approvalRule == null ? "" : approvalRule.getId();
        String approvalRuleHandlerId = approvalRuleHandler.isNew() ? "" : approvalRuleHandler.getId();

        Map<String, Object> handler = internalBuildProcUnitHandler(bizId, procUnitId, procUnitName, approvalRuleHandler.getHandlerKindId(),
                approvalRuleHandler.getDescription(), approvalRuleHandler.getHandlerKindCode(),
                CooperationModelKind.CHIEF, "", orgUnit, approvalRuleId, approvalRuleHandlerId,
                approvalRuleHandler.getGroupId(), sequence, 0);
        if (approvalRule != null) {
            buildTaskExecuteMode(approvalRule, approvalRuleHandler.getGroupId(), handler);
            buildLimitHandler(approvalRule, approvalRuleHandler, handler);
        }
        // 发送消息
        handler.put("sendMessage", approvalRuleHandler.getSendMessage());

        return handler;
    }

    /**
     * @since 1.1.3
     */
    private void buildLimitHandler(ApprovalRule approvalRule, ApprovalRuleHandler approvalRuleHandler, Map<String, Object> handler) {
        ApprovalRuleHandlerGroup approvalRuleHandlerGroup = approvalRule.findApprovalRuleHandlerGroup(approvalRuleHandler.getGroupId());
        if (approvalRuleHandlerGroup != null) {
            handler.put("limitHandler", approvalRuleHandlerGroup.getLimitHandler());
        }
    }

    /**
     * 添加附加审批人员
     * <p>
     * map.put("procUnitId", "Approve"); <br>
     * map.put("procUnitName", "交接人处理"); <br>
     * map.put("fullId", fullId); <br>
     * map.put("fullName", fullName); <br>
     * map.put("groupId", 1);
     *
     * @return 附加审批人员列表
     */
    protected List<Map<String, Object>> getAdditionalHandlers() {
        return null;
    }

    /**
     * 添加附加处理人
     *
     * @param bizId              业务ID
     * @param chiefHandlers      主审人
     * @param additionalHandlers 附加处理人
     * @param currentProcUnitId  当前流程环节ID
     */
    protected void addAdditionalHandlers(String bizId, List<Map<String, Object>> chiefHandlers, List<Map<String, Object>> additionalHandlers,
                                         String currentProcUnitId) {
        String procUnitId, procUnitName, fullId, fullName;
        int sequence = 1;
        Integer groupId;

        ApprovalRuleHandler approvalRuleHandler = new ApprovalRuleHandler();
        approvalRuleHandler.setHandlerKindCode(HandlerKind.PSM.getId());

        for (Map<String, Object> additionalHandler : additionalHandlers) {
            procUnitId = ClassHelper.convert(additionalHandler.get("procUnitId"), String.class, "");
            procUnitName = ClassHelper.convert(additionalHandler.get("procUnitName"), String.class, "");
            fullId = ClassHelper.convert(additionalHandler.get("fullId"), String.class, "");
            fullName = ClassHelper.convert(additionalHandler.get("fullName"), String.class, "");
            groupId = ClassHelper.convert(additionalHandler.get(GROUP_ID_FIELD), Integer.class, 0);

            OrgUnit orgUnit = new OrgUnit();
            orgUnit.setFullId(fullId);
            orgUnit.setFullName(fullName);

            approvalRuleHandler.setDescription(procUnitName);
            approvalRuleHandler.setGroupId(groupId);

            if (procUnitId.equalsIgnoreCase(currentProcUnitId)) {
                Map<String, Object> procUnitHandler = buildProcUnitHandler(null, approvalRuleHandler, bizId, currentProcUnitId, procUnitName, orgUnit, sequence);

                chiefHandlers.add(procUnitHandler);
                sequence++;
            }
        }
    }

    /**
     * 未找到处理人是否忽略
     *
     * @return 忽略未找到处理人
     */
    protected boolean ignoreNotFoundHandler() {
        return false;
    }

    /**
     * 通过环节ID获取环节处理人
     *
     * @param delegateTask 代理任务
     * @return 环节处理人
     */
    protected List<OrgUnit> getHandlersByProcUnitId(DelegateTask delegateTask, String procUnitId) {
        List<OrgUnit> result = new ArrayList<OrgUnit>(0);
        String processDefinitionKey = processFun.getProcessApprovalDefinitionKey(delegateTask.getProcessDefinitionId());
        ProcDefinition procDefinition = this.procDefinitionApplication.loadProcDefinitionByProcId(processDefinitionKey);

        if (procDefinition != null && StringUtil.isNotBlank(procDefinition.getApprovalBridgeProcId())) {
            processDefinitionKey = procDefinition.getApprovalBridgeProcId();
        }

        List<ActivityImpl> nextProcUnitList = this.findOutgoingUserTasks(delegateTask);

        for (int j = 0; j < nextProcUnitList.size(); j++) {
            ActivityImpl activity = nextProcUnitList.get(j);
            if (activity.getId().equalsIgnoreCase(procUnitId)) {

                ApprovalRule approvalRule = procApprovalRuleParseService.execute(processDefinitionKey, activity.getId(),
                        getProcessBizParams(delegateTask.getExecution().getProcessBusinessKey()));
                List<ApprovalRuleHandler> approvalRuleHandlers = approvalRule.getApprovalRuleHandlers();
                if (approvalRuleHandlers == null || approvalRuleHandlers.size() == 0) {
                    return result;
                }

                for (int i = 0; i < approvalRuleHandlers.size(); i++) {
                    result.addAll(approvalRuleHandlers.get(i).getOrgUnits());
                }

                return result;
            }
        }
        return result;
    }

    /**
     * 获取允许合并的子环节id
     *
     * @return
     */
    protected List<String> getCanMergeSubProcUnitId() {
        String canMergeSubProcUnitId = SystemCache.getParameter("canMergeSubProcUnitId", String.class);// 允许合并的环节id
        if (StringUtil.isBlank(canMergeSubProcUnitId)) {
            return null;
        }
        return Arrays.asList(canMergeSubProcUnitId.split(","));
    }

    /**
     * 排除相同处理人
     *
     * @param handlers
     * @param mergeHandlerKind
     */
    protected void excludeSameHandler(List<Map<String, Object>> handlers, MergeHandlerKind mergeHandlerKind) {
        if (mergeHandlerKind == MergeHandlerKind.NOT_MERGE) {
            return;
        }
        Map<String, Object> currentHandler, priorHandler;
        String currentPersonId, priorPersonId, subProcUnitId;
        Integer currentStatus, priorStatus;
        List<String> canMergeSubProcUnitIds = this.getCanMergeSubProcUnitId();
        for (int i = handlers.size() - 1; i > 0; i--) {
            currentHandler = handlers.get(i);
            currentPersonId = ClassHelper.convert(currentHandler.get("handlerId"), String.class);
            currentStatus = ClassHelper.convert(currentHandler.get("status"), Integer.class, 0);

            if (StringUtil.isBlank(currentPersonId) || currentStatus.equals(-1)) {
                continue;
            }
            currentPersonId = currentPersonId.substring(0, currentPersonId.indexOf('@'));
            handlersLoop:
            for (int j = i - 1; j >= 0; j--) {
                priorHandler = handlers.get(j);
                priorPersonId = ClassHelper.convert(priorHandler.get("handlerId"), String.class);
                priorStatus = ClassHelper.convert(priorHandler.get("status"), Integer.class, 0);
                subProcUnitId = ClassHelper.convert(priorHandler.get("subProcUnitId"), String.class, "");
                if (StringUtil.isBlank(priorPersonId) || priorStatus.equals(-1)) {
                    continue handlersLoop;
                }
                if (StringUtil.isNotBlank(subProcUnitId)) {// 存在子环节id不能合并
                    if (canMergeSubProcUnitIds == null || canMergeSubProcUnitIds.size() == 0) {
                        continue handlersLoop;
                    }
                    // 判读是否在允许合并的范围内
                    if (!canMergeSubProcUnitIds.contains(subProcUnitId)) {
                        continue handlersLoop;
                    }
                }

                priorPersonId = priorPersonId.substring(0, priorPersonId.indexOf('@'));
                if (currentPersonId.equalsIgnoreCase(priorPersonId)) {
                    priorHandler.put("status", -1);
                } else {
                    if (mergeHandlerKind == MergeHandlerKind.ADJACENT) {// 只排除相邻处理人 这里强制退出循环
                        break handlersLoop;
                    }
                }
            }
        }
    }

    /**
     * 合并处理人
     *
     * @param procId
     * @param procUnitId
     * @param handlers
     */
    private void mergeHandler(String procId, String procUnitId, List<Map<String, Object>> handlers) {
        ProcDefinition procDefinition = this.procDefinitionApplication.loadProcDefinitionByProcAndProcUnitId(procId, procUnitId);
        MergeHandlerKind mergeHandlerKind = MergeHandlerKind.ADJACENT;
        if (procDefinition != null) {
            mergeHandlerKind = MergeHandlerKind.fromId(procDefinition.getMergeHandlerKind());
        }
        this.excludeSameHandler(handlers, mergeHandlerKind);
    }

    /**
     * 排除指定人员
     *
     * @param bizId    业务ID
     * @param handlers 处理人集合
     */
    private void excludeSpecifiedHandler(String bizId, List<Map<String, Object>> handlers) {
        List<String> specifiedExcludeHandler = getSpecifiedExcludeHandler(bizId);
        if (specifiedExcludeHandler != null && specifiedExcludeHandler.size() > 0) {
            String currentPersonId, specifiedPersonId;
            String[] split;
            boolean found;
            for (Map<String, Object> handler : handlers) {
                if (ClassHelper.convert(handler.get("status"), Integer.class) == 0) {
                    currentPersonId = ClassHelper.convert(handler.get("handlerId"), String.class);
                    // 段可以没有处理人
                    if (StringUtil.isBlank(currentPersonId)) {
                        continue;
                    }
                    currentPersonId = currentPersonId.substring(0, currentPersonId.indexOf('@'));
                    found = false;
                    for (String item : specifiedExcludeHandler) {
                        split = item.split("@");
                        specifiedPersonId = split[0];
                        if (specifiedPersonId.equalsIgnoreCase(currentPersonId)) {
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        handler.put("status", -1);
                    }

                }
            }
        }
    }

    /**
     * 得到指定的排除人员列表
     *
     * @param bizId 业务id
     * @return
     */
    protected List<String> getSpecifiedExcludeHandler(String bizId) {
        return null;
    }

    /**
     * 内部抄送
     *
     * @param orgUnits    组织单元
     * @param taskId      任务ID
     * @param description 标题
     */
    private void internalMakeACopyFor(List<OrgUnit> orgUnits, String taskId, String description) {
        if (orgUnits.size() > 0) {
            List<String> orgFullIds = orgFun.orgUnitsToOrgFullIds(orgUnits);

            List<OrgUnit> personMemberUnits = orgFun.findPersonMembersInOrg(orgFullIds, true);
            List<String> personMemberFullIds = orgFun.orgUnitsToOrgFullIds(personMemberUnits);

            if (personMemberFullIds.size() > 0) {
                this.workflowService.makeACopyFor(taskId, personMemberFullIds, description);
            }
        }
    }

    /**
     * 给指定的管理权限发抄送任务
     *
     * @param orgId       组织ID
     * @param manageType  业务管理权限类别
     * @param taskId      任务ID
     * @param description 标题
     */
    protected void makeACopyForToManageType(String orgId, String manageType, String taskId, String description) {
        List<OrgUnit> orgUnits = orgFun.findManagers(orgId, manageType, false, null);
        internalMakeACopyFor(orgUnits, taskId, description);
    }

    /***
     * 给最近的管理权限抄送任务
     *
     * @param fullId
     *            fullId
     * @param manageType
     *            业务管理权限类别
     * @param taskId
     *            任务ID
     * @param description
     *            标题
     */
    protected void makeACopyForToNearestManageType(String fullId, String manageType, String taskId, String description) {
        List<OrgUnit> orgUnits = orgFun.findNearestManagers(fullId, manageType);
        internalMakeACopyFor(orgUnits, taskId, description);
    }

    /**
     * 检查约束
     *
     * @param delegateTask
     */
    protected void onCheckConstraints(DelegateTask delegateTask) {

    }

    public enum QueryHandlerShowFieldKind {
        FULL_NAME("fullName", "处理人姓名全路径"), HANDLER_NAME("handlerName", "处理人姓名");
        private final String id;

        private final String displayName;

        private QueryHandlerShowFieldKind(String id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public String getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public static QueryHandlerShowFieldKind fromId(String id) {
            if (StringUtil.isBlank(id)) {
                return FULL_NAME;
            }
            switch (id) {
                case "fullName":
                    return FULL_NAME;
                case "handlerName":
                    return HANDLER_NAME;
                default:
                    throw new IllegalStateException(String.format("无效的预览处理人显示办理人字段类型“%s”。", id));
            }
        }
    }

}
