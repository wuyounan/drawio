package com.huigou.uasp.bpm.engine.application.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.cache.DictUtil;
import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.OrgUnit;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.common.BooleanKind;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.opm.OpmUtil;
import com.huigou.uasp.bmp.opm.application.OrgApplication;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgNodeKind;
import com.huigou.uasp.bmp.opm.proxy.OrgApplicationProxy;
import com.huigou.uasp.bmp.opm.repository.org.OrgRepository;
import com.huigou.uasp.bpm.ActivityKind;
import com.huigou.uasp.bpm.ApprovalParameter;
import com.huigou.uasp.bpm.ApprovalParameterUtil;
import com.huigou.uasp.bpm.BatchAdvanceParameter;
import com.huigou.uasp.bpm.CooperationModelKind;
import com.huigou.uasp.bpm.MessageSenderManager;
import com.huigou.uasp.bpm.ProcessAction;
import com.huigou.uasp.bpm.ProcessTaskContants;
import com.huigou.uasp.bpm.TaskKind;
import com.huigou.uasp.bpm.TaskStatus;
import com.huigou.uasp.bpm.ToDoTaskKind;
import com.huigou.uasp.bpm.ViewTaskKind;
import com.huigou.uasp.bpm.cmd.AddCommentCmd;
import com.huigou.uasp.bpm.cmd.AssistCmd;
import com.huigou.uasp.bpm.cmd.BackTaskCmd;
import com.huigou.uasp.bpm.cmd.CheckConstraintsCmd;
import com.huigou.uasp.bpm.cmd.CompleteMendTaskCmd;
import com.huigou.uasp.bpm.cmd.CompleteReplenishTaskCmd;
import com.huigou.uasp.bpm.cmd.FindBackActivityCmd;
import com.huigou.uasp.bpm.cmd.FindProcessInstanceActiveActivitiesCmd;
import com.huigou.uasp.bpm.cmd.GetBpmnDelegateClassCanonicalNameCmd;
import com.huigou.uasp.bpm.cmd.MakeACopyForCmd;
import com.huigou.uasp.bpm.cmd.NewCoordinationTaskCmd;
import com.huigou.uasp.bpm.cmd.NotifyTaskCmd;
import com.huigou.uasp.bpm.cmd.QueryHandlersCmd;
import com.huigou.uasp.bpm.cmd.RecallProcessInstanceCmd;
import com.huigou.uasp.bpm.cmd.ReplenishCmd;
import com.huigou.uasp.bpm.cmd.SaveBizDataCmd;
import com.huigou.uasp.bpm.cmd.TransmitTaskCmd;
import com.huigou.uasp.bpm.cmd.WithdrawTaskCmd;
import com.huigou.uasp.bpm.configuration.application.ApprovalRuleApplication;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRule;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleElement;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandler;
import com.huigou.uasp.bpm.configuration.domain.model.TaskExecuteMode;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.application.ProcApprovalRuleParseService;
import com.huigou.uasp.bpm.engine.application.ProcUnitHandlerApplication;
import com.huigou.uasp.bpm.engine.application.WorkflowApplication;
import com.huigou.uasp.bpm.engine.domain.model.CoordinationTask;
import com.huigou.uasp.bpm.engine.domain.model.HistoricProcessInstanceExtension;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceExtension;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnit;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandler;
import com.huigou.uasp.bpm.engine.domain.model.RuntimeTaskExtension;
import com.huigou.uasp.bpm.engine.domain.model.TaskCollection;
import com.huigou.uasp.bpm.engine.domain.query.ProcunitHandlerQueryRequest;
import com.huigou.uasp.bpm.engine.domain.query.TaskDetail;
import com.huigou.uasp.bpm.engine.repository.TaskCollectionRepository;
import com.huigou.uasp.bpm.event.ProcessEvent;
import com.huigou.uasp.bpm.event.ProcessEventContext;
import com.huigou.uasp.bpm.event.ProcessEventSupport;
import com.huigou.uasp.bpm.managment.domain.model.ProcDefinition;
import com.huigou.uasp.bpm.managment.repository.ProcDefinitionRespository;
import com.huigou.util.ClassHelper;
import com.huigou.util.CommonUtil;
import com.huigou.util.Constants;
import com.huigou.util.DateRange;
import com.huigou.util.DateUtil;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;
import com.huigou.util.Util;

/**
 * 工作流服务实现类
 * 
 * @author gongmm
 */
@Service("workflowApplication")
public class WorkflowApplicationImpl extends BaseApplication implements WorkflowApplication {

    @Autowired
    private OrgApplicationProxy orgApplication;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private FormService formService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ActApplication actApplication;

    @Autowired
    private ProcUnitHandlerApplication procUnitHandlerApplication;

    @Autowired
    private ApprovalRuleApplication approvalRuleApplication;

    @Autowired
    private ProcDefinitionRespository procDefinitionRespository;

    @Autowired
    private TaskCollectionRepository taskCollectionRepository;

    @Autowired
    private ProcApprovalRuleParseService procApprovalRuleParseService;

    @Autowired(required = false)
    private ProcessEventSupport processEventSupport;

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    protected MessageSenderManager messageSenderManager;

    // private static int LICENSE_INDEX = 0;

    public OrgApplication getOrgApplication() {
        return this.orgApplication.getOrgApplication();
    }

    public ActApplication getActApplication() {
        return actApplication;
    }

    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public RepositoryService getRepositoryService() {
        return this.repositoryService;
    }

    public void setFormService(FormService formService) {
        this.formService = formService;
    }

    public FormService getFormService() {
        return this.formService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public RuntimeService getRunTimeService() {
        return this.runtimeService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public TaskService getTaskService() {
        return this.taskService;
    }

    public void setManagementService(ManagementService managementService) {
        this.managementService = managementService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public HistoryService getHistoryService() {
        return this.historyService;
    }

    @Override
    public ProcUnitHandlerApplication getProcUnitHandlerService() {
        return procUnitHandlerApplication;
    }

    @Override
    @Transactional
    public void deploy(String procId, String fileName) {
        Assert.hasText(procId, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        Assert.hasText(fileName, MessageSourceContext.getMessage(MessageConstants.FILE_NAME_NOT_BLANK));

        ProcDefinition procDefinition = this.procDefinitionRespository.findOne(procId);
        Assert.notNull(procDefinition, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, procId, "流程定义"));

        Deployment deployment = repositoryService.createDeployment().addClasspathResource(fileName).deploy();

        ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();

        procDefinition.setProcId(processDefinition.getKey());
        procDefinition.setProcName(processDefinition.getName());

        this.procDefinitionRespository.save(procDefinition);
    }

    private ProcessInstance internalStartProcessInstanceByKey(String operate, String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        Assert.hasText(processDefinitionKey, "流程模板ID不能为空。");

        ProcessInstance pi = this.runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);

        ExecutionEntity executionEntity = (ExecutionEntity) pi;

        List<TaskEntity> tasks = executionEntity.getTasks();
        Assert.notEmpty(tasks, "启动流程出错，未生成任务。");

        Task task = tasks.get(0);

        // Task task = this.taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        // if (task == null) {
        // throw new ApplicationException("启动流程出错，未生成任务。");
        // }

        ApprovalParameterUtil.getApprovalParameter().setTaskId(task.getId());

        if ("advance".equalsIgnoreCase(operate)) {
            this.doAdvance(task.getId(), variables);
        }

        fireProcessEvent(ProcessEvent.AFTER_STARTUP_PROCESS_EVENT);

        return pi;
    }

    @Override
    @Transactional
    public ProcessInstance startAndAdvanceProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        return internalStartProcessInstanceByKey("advance", processDefinitionKey, businessKey, variables);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ProcessInstance startAndAdvanceProcessInstanceByKeyInNewTransaction(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        return internalStartProcessInstanceByKey("advance", processDefinitionKey, businessKey, variables);
    }

    @Override
    @Transactional
    public ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        SDO params = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        String operate = ClassHelper.convert(params.getProperty(ProcessAction.PROCESS_ACTION), String.class, "save");

        return internalStartProcessInstanceByKey(operate, processDefinitionKey, businessKey, variables);
    }

    @Override
    @Transactional
    public ProcessInstance startProcessInstanceByKey(String processDefinitionKey, Map<String, Object> variables) {
        return startProcessInstanceByKey(processDefinitionKey, null, variables);
    }

    @Override
    @Transactional
    public void saveBizData(String bizId, String taskId) {
        SaveBizDataCmd cmd = new SaveBizDataCmd(bizId, taskId);
        ((TaskServiceImpl) taskService).getCommandExecutor().execute(cmd);
    }

    @Override
    public Boolean showQueryHandlers(String bizId, String processDefinitionKey, String procUnitId, Integer groupId, String procUnitHandlerId) {
        if (StringUtil.isNotBlank(procUnitHandlerId)) {
            ProcUnitHandler procUnitHandler = this.getProcUnitHandlerService().loadProcUnitHandler(procUnitHandlerId);
            Assert.notNull(procUnitHandler, "审批人信息未找到!");
            if (CooperationModelKind.ASSISTANT.equals(procUnitHandler.getCooperationModelId())) {
                return false;
            }
        }
        // 当前组的协作模式
        boolean isPreempt = false;
        if (!CommonUtil.isIntegerNull(groupId)) {
            TaskExecuteMode taskExecuteMode = this.procUnitHandlerApplication.getTaskExecuteMode(bizId, procUnitId, groupId);
            isPreempt = TaskExecuteMode.PREEMPT == taskExecuteMode;
        }

        // 流程实例的当前环节是否审批完成
        boolean isTheLastHandler = true;
        if (StringUtil.isNotBlank(bizId) && !isPreempt) {
            isTheLastHandler = this.procUnitHandlerApplication.countChiefNotApprove(bizId, procUnitId) <= 1;
        }

        if (isTheLastHandler) {
            ProcDefinition procDefinition = this.procDefinitionRespository.findByProcIdAndCode(processDefinitionKey, procUnitId);
            if (procDefinition != null) {
                return procDefinition.getPreviewHandler() == 1;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public Map<String, Object> queryHandlers(String bizId, String taskId) {
        QueryHandlersCmd cmd = new QueryHandlersCmd(taskId);
        return ((TaskServiceImpl) taskService).getCommandExecutor().execute(cmd);
    }

    private void checkTaskWaitingStatus(String taskId) {
        RuntimeTaskExtension runtimeTaskExtension = actApplication.loadRuntimeTaskExtension(taskId);
        Assert.notNull(runtimeTaskExtension, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "运行时任务扩展"));
        Assert.isTrue(runtimeTaskExtension.isToDoStatus(), String.format("任务状态[%s]不为“未执行”，不能流转。", taskId));
    }

    @Override
    @Transactional
    public void advance(String taskId, Map<String, Object> variables) {
        this.doAdvance(taskId, variables);

        fireProcessEvent(ProcessEvent.AFTER_COMPLETE_PROCESS_TASK_EVENT);
    }

    private void doAdvance(String taskId, Map<String, Object> variables) {
        checkTaskWaitingStatus(taskId);
        findBpmnDelegateClassCanonicalName(taskId);
        taskService.complete(taskId, variables);
    }

    @Override
    @Transactional
    public void batchAdvance(List<BatchAdvanceParameter> batchAdvanceParameters) {
        Assert.notEmpty(batchAdvanceParameters, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "batchAdvanceParameters"));
        ApprovalParameter approvalParameter;
        for (BatchAdvanceParameter batchAdvanceParameter : batchAdvanceParameters) {
            approvalParameter = batchAdvanceParameter.toApprovalParameter();
            approvalParameter.setProcessAction(ProcessAction.ADVANCE);
            ThreadLocalUtil.putVariable(ApprovalParameter.APPROVAL_PARAMETER_KEY, approvalParameter);
            Map<String, Object> variables = new HashMap<String, Object>();
            // TODO 后期提供支持 完成协同任务
            this.doAdvance(approvalParameter.getTaskId(), variables);
        }
        fireProcessEvent(ProcessEvent.AFTER_COMPLETE_PROCESS_TASK_EVENT);
    }

    private void internalCompleteTask(String taskId, TaskStatus taskStatus) {
        taskService.complete(taskId);
        actApplication.deleteRuntimeTaskExtension(taskId);
        actApplication.updateHistoricTaskInstanceExtensionEnded(taskId, taskStatus, taskStatus.getId());
    }

    @Override
    @Transactional
    public void completeTask(String taskId) {
        this.internalCompleteTask(taskId, TaskStatus.COMPLETED);

        this.addCompletedNotProcessTask(taskId);
        this.fireProcessEvent(ProcessEvent.AFTER_COMPLETE_NOT_PROCESS_TASK_EVENT);
    }

    @Override
    @Transactional
    public void deleteTask(String taskId) {
        Assert.hasText(taskId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "taskId"));
        this.internalCompleteTask(taskId, TaskStatus.DELETED);

        ProcessEventContext.addDeletedTask(taskId);

        fireProcessEvent(ProcessEvent.AFTER_DELETE_NOT_PROCESS_TASK_EVENT);
    }

    private void doUpdateTaskDescription(String taskId, String description) {
        Assert.hasText(taskId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "taskId"));
        Assert.hasText(taskId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "description"));
        // TODO move to actApplication
        String sql = "update act_ru_task t set t.description_=? where t.id_=?";
        this.sqlExecutorDao.executeUpdate(sql, description, taskId);
        sql = "update act_hi_taskinst t set t.description_=? where t.id_=?";
        this.sqlExecutorDao.executeUpdate(sql, description, taskId);

        sql = "update act_ru_task_extension t set t.description_=? where t.id_=?";
        this.sqlExecutorDao.executeUpdate(sql, description, taskId);
        sql = "update act_hi_taskinst_extension t set t.description_=? where t.id_=?";
        this.sqlExecutorDao.executeUpdate(sql, description, taskId);
        ProcessEventContext.addUpdatedStatusTask(taskId);

    }

    @Override
    @Transactional
    public void updateTaskDescription(String taskId, String description) {
        Assert.hasText(taskId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "taskId"));
        Assert.hasText(taskId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "description"));

        this.doUpdateTaskDescription(taskId, description);
        fireProcessEvent(ProcessEvent.AFTER_UPDATE_TASK_EXTENSION_STATUS_EVENT);
    }

    @Override
    @Transactional
    public void updateTasksDescription(Map<String, Object> params) {
        String description;
        for (String taskId : params.keySet()) {
            description = ClassHelper.convert(params.get(taskId), String.class);
            this.doUpdateTaskDescription(taskId, description);
        }
        fireProcessEvent(ProcessEvent.AFTER_UPDATE_TASK_EXTENSION_STATUS_EVENT);
    }

    private void addCompletedNotProcessTask(String taskId) {
        ProcessEventContext.addCompletedTask(taskId);
    }

    @Override
    @Transactional
    public void completeMendTask(String bizId, String taskId) {
        this.saveBizData(bizId, taskId);

        CompleteMendTaskCmd cmd = new CompleteMendTaskCmd(taskId, this.actApplication);
        ((TaskServiceImpl) taskService).getCommandExecutor().execute(cmd);

        addCompletedNotProcessTask(taskId);

        fireProcessEvent(ProcessEvent.AFTER_COMPLETE_NOT_PROCESS_TASK_EVENT);
    }

    private Task checkTaskNotNull(String taskId) {
        Assert.hasText(taskId, "参数taskId不能为空。");

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_TASK_ID, taskId, "任务"));

        return task;
    }

    @Override
    @Transactional
    public void completeReplenishTask(String bizId, String taskId) {
        this.saveBizData(bizId, taskId);
        HistoricTaskInstanceExtension htie = this.actApplication.loadHistoricTaskInstanceExtension(taskId);
        ThreadLocalUtil.putVariable("replenishTaskId", taskId);
        // 打回提交前增加校验
        CheckConstraintsCmd checkCmd = new CheckConstraintsCmd(htie.getId(), this.actApplication);
        ((TaskServiceImpl) taskService).getCommandExecutor().execute(checkCmd);
        // /处理打回任务
        CompleteReplenishTaskCmd cmd = new CompleteReplenishTaskCmd(taskId, this.actApplication, this.messageSenderManager);
        ((TaskServiceImpl) taskService).getCommandExecutor().execute(cmd);

        ProcessEventContext.addCompletedTask(taskId);
        fireProcessEvent(ProcessEvent.AFTER_COMPLETE_NOT_PROCESS_TASK_EVENT);
    }

    @Override
    @Transactional
    public void abortTask(String taskId) {
        checkTaskNotNull(taskId);

        this.internalCompleteTask(taskId, TaskStatus.ABORTED);

        addCompletedNotProcessTask(taskId);
        fireProcessEvent(ProcessEvent.AFTER_ABORT_TASK);
    }

    @Override
    @Transactional
    public void abortProcessInstance(String processInstanceId) {
        Util.check(!StringUtil.isBlank(processInstanceId), "参数processInstanceId不能为空。");
        deleteProcessInstance(processInstanceId, ProcessAction.ABORT_PROCESS_INSTANCE);
    }

    @Override
    @Transactional
    public void abortProcessInstanceByBizId(String bizId) {
        Util.check(!StringUtil.isBlank(bizId), "参数bizId不能为空。");
        ProcessInstance pi = this.getRunTimeService().createProcessInstanceQuery().processInstanceBusinessKey(bizId).singleResult();
        deleteProcessInstance(pi.getProcessInstanceId(), ProcessAction.ABORT_PROCESS_INSTANCE);
    }

    @Override
    @Transactional
    public void abortProcessInstanceByBizCode(String bizCode) {
        Util.check(!StringUtil.isBlank(bizCode), "参数bizCode不能为空。");
        HistoricTaskInstanceExtension applicantTask = actApplication.queryApplicantTaskByBizCode(bizCode);
        Assert.notNull(applicantTask, String.format("未找到单据编号“%s”对用的流程。", bizCode));

        SDO sdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        sdo.putProperty(ProcessTaskContants.BIZ_ID, applicantTask.getBusinessKey());

        abortProcessInstanceByBizId(applicantTask.getBusinessKey());
    }

    @Override
    @Transactional
    public void suspendTask(String taskId) {
        checkTaskNotNull(taskId);
        // 前置状态判断

        String errorMessage = "任务状态不为“尚未处理”，不能暂停。";
        RuntimeTaskExtension ruTaskExtension = this.actApplication.loadRuntimeTaskExtension(taskId);
        ruTaskExtension.checkStatus(TaskStatus.READY, errorMessage);
        // 更新状态
        this.actApplication.updateTaskExtensionStatus(taskId, TaskStatus.SUSPENDED);

        ProcessEventContext.addUpdatedStatusTask(taskId);

        fireProcessEvent(ProcessEvent.AFTER_SUSPEND_TASK_EVENT);
    }

    @Override
    @Transactional
    public void recoverTask(String taskId) {
        checkTaskNotNull(taskId);

        String errorMessage = "任务状态不为“暂停”，不能恢复。";
        RuntimeTaskExtension ruTaskExtension = this.actApplication.loadRuntimeTaskExtension(taskId);
        ruTaskExtension.checkStatus(TaskStatus.SUSPENDED, errorMessage);

        this.actApplication.updateTaskExtensionStatus(taskId, TaskStatus.READY);

        ProcessEventContext.addUpdatedStatusTask(taskId);

        fireProcessEvent(ProcessEvent.AFTER_RECOVER_TASK_EVENT);
    }

    @Override
    @Transactional
    public void sleep(String taskId) {
        checkTaskNotNull(taskId);

        String errorMessage = "任务状态不为“尚未处理”，不能暂缓。";

        RuntimeTaskExtension ruTaskExtension = this.actApplication.loadRuntimeTaskExtension(taskId);
        ruTaskExtension.checkStatus(TaskStatus.READY, errorMessage);
        // 任务时间大于1天不能暂缓
        /*
         * String sql = "select fun_get_work_days(? , sysdate) from dual"; float
         * workDays =this.querySqlExecutorDao.queryToObject(sql,
         * Float.class, task.getCreateTime()); workDays = (float)
         * (Math.round(workDays * 100) / 100.0); if (workDays >= 1) { throw new
         * ApplicationException("当前任务已超过1天，不能暂缓。"); }
         */
        this.actApplication.updateTaskExtensionSleepingStatus(taskId);

        ProcessEventContext.addUpdatedStatusTask(taskId);

        fireProcessEvent(ProcessEvent.AFTER_SLEEP_TASK_EVENT);
    }

    @Override
    @Transactional
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        HistoricProcessInstanceExtension hpie = this.actApplication.loadHistoricProcessInstanceExtensionByProcInstId(processInstanceId);

        // List<HistoricTaskInstanceExtension> wchties = this.actApplication.queryWaitingCoordinationHistoricTaskInstanceByBizId(hpie.getBusinessKey());

        List<RuntimeTaskExtension> coordinationTaskInstances = this.actApplication.queryCoordinationTaskInstances(hpie.getBusinessKey());

        if (coordinationTaskInstances.size() > 0) {
            List<String> ids = new ArrayList<String>(coordinationTaskInstances.size());
            for (RuntimeTaskExtension runtimeTaskExtension : coordinationTaskInstances) {
                ids.add(runtimeTaskExtension.getId());
            }
            List<HistoricTaskInstanceExtension> wchties = this.actApplication.queryHistoricTaskInstanceExtensions(ids);
            for (HistoricTaskInstanceExtension wchtie : wchties) {
                ProcessEventContext.addDeletedTask(wchtie.getId());
            }
        }

        List<HistoricTaskInstanceExtension> hties = this.actApplication.queryNotCompleteHiTaskInstExtensionsByProcInstId(processInstanceId);
        for (HistoricTaskInstanceExtension htie : hties) {
            ProcessEventContext.addDeletedTask(htie.getId());
        }

        this.runtimeService.deleteProcessInstance(processInstanceId, deleteReason);

        fireProcessEvent(ProcessEvent.AFTER_ABORT_PROCESS_INSTANCE_EVENT);
    }

    @Override
    @Transactional
    public void recallProcessInstance(String processInstanceId, String taskId) {
        String applyTaskId = taskId;
        HistoricTaskInstanceExtension historicTaskInstanceExtension = actApplication.loadHistoricTaskInstanceExtension(taskId);
        // 查询申请环节对应的任务ID
        HistoricTaskInstanceExtension applicantTask = this.actApplication.queryApplicantTask(historicTaskInstanceExtension.getBusinessKey());
        applyTaskId = applicantTask.getId();
        RecallProcessInstanceCmd cmd = new RecallProcessInstanceCmd(applyTaskId, this.actApplication);
        cmd.setProcessInstanceId(processInstanceId);
        this.managementService.executeCommand(cmd);

        fireProcessEvent(ProcessEvent.AFTER_RECALL_PROCESS_INSTANCE_EVENT);
    }

    @Override
    @Transactional
    public void back(String taskId, String destActivityId, String backToProcUnitHandlerId) {
        checkTaskWaitingStatus(taskId);
        Command<Integer> cmd = new BackTaskCmd(taskId, destActivityId, backToProcUnitHandlerId, this.actApplication);
        managementService.executeCommand(cmd);

        fireProcessEvent(ProcessEvent.AFTER_BACK_EVENT);
    }

    @Override
    @Transactional
    public void backToApplyActivity(String taskId, String processInstanceId) {
        Assert.hasText(processInstanceId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "processInstanceId"));
        Map<String, Object> applicant = this.actApplication.queryApplicantByProcessInstanceId(processInstanceId);
        Assert.isTrue(applicant != null, "未找到ID“%s”对用的流程实例。");
        String destActivityId = ClassHelper.convert(applicant.get("taskDefKey"), String.class);
        back(taskId, destActivityId, null);
    }

    @Override
    public List<ActivityImpl> queryBackActivity(String taskId) {
        Command<List<ActivityImpl>> cmd = new FindBackActivityCmd(taskId);
        List<ActivityImpl> result = managementService.executeCommand(cmd);
        return result;
    }

    @Override
    @Transactional
    public void withdrawTask(String taskId, String previousId) {
        Command<Integer> cmd = new WithdrawTaskCmd(taskId, previousId, this.actApplication);
        managementService.executeCommand(cmd);

        fireProcessEvent(ProcessEvent.AFTER_WITHDRAW_TASK_EVENT);
    }

    @Override
    @Transactional
    public void withdrawTaskByBizId(String taskId, String bizId) {
        Map<String, Object> map = this.actApplication.loadRuntimeTaskByBizId(bizId);
        if (map == null || map.size() == 0) {
            // 流程任务已结束，不能回收。
            Assert.isTrue(false, MessageSourceContext.getMessage("common.job.error.withdrawTask.over"));
        }
        Operator operator = this.getOperator();
        // 判断任务创建人是否为当期用户
        String creatorPersonId = OpmUtil.getPersonIdFromPersonMemberId(ClassHelper.convert(map.get("creatorPersonMemberId"), String.class));
        // 下一环节已有任务已完成，不能回收。
        Assert.isTrue(operator.getUserId().equals(creatorPersonId), MessageSourceContext.getMessage("common.job.error.withdrawTask.next"));
        String taskDefKey = ClassHelper.convert(map.get("taskDefKey"), String.class);
        // 申请环节，不能回收。
        Assert.isTrue(!ActivityKind.APPLY.equalsIgnoreCase(taskDefKey), MessageSourceContext.getMessage("common.job.error.withdrawTask.apply"));
        String catalogId = ClassHelper.convert(map.get("catalogId"), String.class);
        // 非流程任务，不能回收。
        Assert.isTrue(catalogId.equals("process"), MessageSourceContext.getMessage("common.job.error.withdrawTask.process"));
        String withdrawTaskId = ClassHelper.convert(map.get("id"), String.class);
        // 当前执行任务不能回收!
        Assert.isTrue(!taskId.equals(withdrawTaskId), MessageSourceContext.getMessage("common.job.error.withdrawTask"));
        String previousId = ClassHelper.convert(map.get("previousId"), String.class);
        SDO sdo = new SDO();
        sdo.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.WITHDRAW);
        sdo.putProperty(ProcessTaskContants.PREVIOUS_ID, previousId);
        sdo.putProperty(ProcessTaskContants.TASK_ID, withdrawTaskId);
        ThreadLocalUtil.putVariable(Constants.SDO, sdo);
        this.withdrawTask(withdrawTaskId, previousId);
    }

    @Override
    @Transactional
    public void transmit(String catalogId, String taskId, String procUnitHandlerId, String executorId, Integer sendMessage) {
        checkTaskWaitingStatus(taskId);

        TransmitTaskCmd cmd = new TransmitTaskCmd(taskId, this.actApplication);

        cmd.setTaskId(taskId);
        cmd.setExecutorId(executorId);
        cmd.setSendMessage(sendMessage);
        cmd.setProcUnitHandlerId(procUnitHandlerId);
        cmd.setTaskScope(catalogId);
        cmd.setProcUnitHandlerService(procUnitHandlerApplication);

        this.managementService.executeCommand(cmd);

        fireProcessEvent(ProcessEvent.AFTER_TRANSMIT_EVENT);
    }

    @Override
    @Transactional
    public void assign(String taskId, String procUnitHandlerId, List<String> personMemberIds) {
        this.checkTaskNotNull(taskId);
        Assert.hasText(procUnitHandlerId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "procUnitHandlerId"));
        Assert.notEmpty(personMemberIds, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "personMemberIds"));

        ProcUnitHandler procUnitHandler = this.procUnitHandlerApplication.loadProcUnitHandler(procUnitHandlerId);
        Assert.state(procUnitHandler != null, String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_BIZ_ID, procUnitHandlerId, "环节处理人"));

        List<Org> personMembers = this.orgRepository.findAll(personMemberIds);
        Assert.state(personMemberIds.size() == personMembers.size(), String.format(CommonDomainConstants.IDS_EXIST_INVALID_ID, "人员"));
        for (Org org : personMembers) {
            Assert.state(org.getOrgKind() == OrgNodeKind.PSM, String.format("%s不为人员成员，不能交办。", org.getName()));
        }

        OrgUnit orgUnit;
        ProcUnitHandler assignedProcUnitHandler;
        List<ProcUnitHandler> procUnitHandlers = new ArrayList<ProcUnitHandler>(personMembers.size());
        for (Org psm : personMembers) {
            orgUnit = new OrgUnit(psm.getFullId(), psm.getFullName());

            assignedProcUnitHandler = procUnitHandler.clone();

            assignedProcUnitHandler.setId(CommonUtil.createGUID());
            assignedProcUnitHandler.buildOrgNodeData(orgUnit);
            assignedProcUnitHandler.setGroupId(procUnitHandler.getGroupId() + 10);
            assignedProcUnitHandler.setSubProcUnitName("交办任务");
            assignedProcUnitHandler.clearHandleOpinion();
            assignedProcUnitHandler.setExecutionTimes(1);

            procUnitHandlers.add(assignedProcUnitHandler);
        }

        this.procUnitHandlerApplication.saveProcUnitHandlers(procUnitHandlers);

        Map<String, Object> variables = new HashMap<String, Object>(0);
        this.advance(taskId, variables);
    }

    @Override
    @Transactional
    public void claim(String taskId, String personMemberId) {
        Assert.hasText(taskId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "taskId"));

        TaskDetail taskDetail = this.actApplication.queryTaskDetail(taskId);
        Assert.state(taskDetail != null, String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "任务"));

        if (StringUtil.isBlank(personMemberId)) {
            personMemberId = ThreadLocalUtil.getOperator().getPersonMemberId();
        }

        Org psm = this.orgApplication.loadEabledOrg(personMemberId);

        OrgUnit orgUnit = new OrgUnit(psm.getFullId(), psm.getFullName());

        this.actApplication.updateTaskHanlder(taskId, orgUnit, true);
        if (StringUtil.isNotBlank(taskDetail.getProcUnitHandlerId())) {
            this.procUnitHandlerApplication.updateProcUnitHandlerOrgData(taskDetail.getProcUnitHandlerId(), orgUnit);
        }

        fireProcessEvent(ProcessEvent.AFTER_CLAIM_EVENT);
    }

    @Override
    @Transactional
    public void makeACopyForEvent(String taskId, List<String> executorIds) {
        makeACopyFor(taskId, executorIds);
        // 页面执行的操作才需要 触发流程事件
        fireProcessEvent(ProcessEvent.AFTER_MAKEACOPYFOR_EVENT);
    }

    @Override
    @Transactional
    public void makeACopyFor(String taskId, List<String> executorIds) {
        makeACopyFor(taskId, executorIds, null);
    }

    @Override
    @Transactional
    public void makeACopyFor(String taskId, List<String> executorIds, String description) {
        MakeACopyForCmd cmd = new MakeACopyForCmd(taskId, description, executorIds, actApplication, messageSenderManager);
        this.managementService.executeCommand(cmd);
    }

    @Override
    @Transactional
    public void createNoticeTask(String procUnitName, String description, List<String> executorIds) {
        NotifyTaskCmd cmd = new NotifyTaskCmd(procUnitName, description, executorIds, actApplication);
        this.managementService.executeCommand(cmd);
        fireProcessEvent(ProcessEvent.AFTER_CREATE_NOTICE_TASK_EVENT);
    }

    @Override
    @Transactional
    public void createNoticeTask(String procUnitName, String description, List<String> executorIds, String formKey, String businessKey) {
        NotifyTaskCmd cmd = new NotifyTaskCmd(procUnitName, description, formKey, businessKey, executorIds, actApplication);
        this.managementService.executeCommand(cmd);
        fireProcessEvent(ProcessEvent.AFTER_CREATE_NOTICE_TASK_EVENT);
    }

    @Override
    @Transactional
    public void doCreateCoordinationTask(CoordinationTask coordinationTask) {
        Assert.notNull(coordinationTask, "参数coordinationTask不能为空。");
        NewCoordinationTaskCmd cmd = new NewCoordinationTaskCmd(coordinationTask, actApplication, messageSenderManager);
        this.managementService.executeCommand(cmd);
    }

    @Override
    @Transactional
    public void createCoordinationTask(CoordinationTask coordinationTask) {
        this.doCreateCoordinationTask(coordinationTask);
        fireProcessEvent(ProcessEvent.AFTER_CREATE_COORDINATION_TASK_EVENT);
    }

    @Override
    public List<ProcUnit> getAllProcessDefinitions() {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().active().list();
        List<ProcUnit> result = new ArrayList<ProcUnit>();
        for (ProcessDefinition item : processDefinitions) {
            ProcUnit procUnit = new ProcUnit();
            procUnit.setId(item.getId());
            procUnit.setName(item.getName());
            procUnit.setParentId("");
            procUnit.setHasChildren(true);
            procUnit.setisexpand(false);
            procUnit.setNodeKindId(ProcUnit.NodeKind.PROC.getName());
            result.add(procUnit);
        }

        return result;
    }

    @Override
    public List<ProcUnit> getUserTaskActivitiesByProcessDefinitionId(String processDefinitionId) {
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        List<ProcUnit> result = new ArrayList<ProcUnit>();

        for (ActivityImpl item : processDefinition.getActivities()) {
            if ("userTask".equals(item.getProperty("type")) || "callActivity".equals(item.getProperty("type"))) {
                ProcUnit procUnit = new ProcUnit();
                procUnit.setId(item.getId());
                procUnit.setName(ClassHelper.convert(item.getProperty("name"), String.class));
                procUnit.setParentId(processDefinition.getId());
                procUnit.setHasChildren(false);
                procUnit.setisexpand(false);
                procUnit.setNodeKindId(ProcUnit.NodeKind.PROC_UNIT.getName());
                result.add(procUnit);
            }
        }
        return result;
    }

    private String getQuerySqlByName(String name) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery("config/uasp/query/bmp/bpm.xml", "workflow");
        return queryDescriptor.getSqlByName(name);
    }

    private String getRuntimeTaskSql() {
        return getQuerySqlByName("queryRuntimeTasks");
    }

    private String getHistoryTaskSql() {
        return getQuerySqlByName("queryHistoryTasks");
    }

    private String getCollectTaskSql() {
        return getQuerySqlByName("queryCollectTasks");
    }

    @Override
    public TaskDetail queryTaskDetail(String taskId) {
        return this.actApplication.queryTaskDetail(taskId);
    }

    @Override
    public Map<String, Object> queryTasks(SDO params) {
        // checkLicense();
        Integer dateRangeId = ClassHelper.convert(params.getProperty("dateRange"), Integer.class, 1);
        String inViewTaskKinds = ClassHelper.convert(params.getProperty("viewTaskKindList"), String.class, "");
        String queryCategory = ClassHelper.convert(params.getProperty("queryCategory"), String.class, "");
        String toDoTaskKind = ClassHelper.convert(params.getProperty("toDoTaskKind"), String.class, "");

        // 管理的组织FullId
        String administrativeOrgFullId = params.getString("administrativeOrgFullId");
        // 排除指定流程
        String excludeSpecifiedProc = params.getString("excludeSpecifiedProc");
        // 流程定义Id
        String procDefineId = params.getString("procDefineId");
        // 流程对应的系统FullId
        String procFullId = params.getString("procFullId");
        // 只查询流程发起环节
        String onlyQueryApplyProcUnit = params.getString("onlyQueryApplyProcUnit");
        // 单一流程只显示一个任务
        String singleProcInstShowOneTask = params.getString("singleProcInstShowOneTask");

        List<String> taskKinds = params.getStringList("taskKinds");

        String personId = params.getProperty("operatorPersonId", String.class, "");
        if (StringUtil.isBlank(personId)) {// 获取当前登录用户ID
            personId = params.getOperator().getUserId();
        }
        StringBuilder sb = new StringBuilder();
        QueryModel queryModel = new QueryModel();
        queryModel.initPageInfo(params.getProperties());
        String sql = null;

        List<String> statuses = new ArrayList<String>();
        String statusCriteria = null;

        List<ViewTaskKind> viewTaskKinds = ViewTaskKind.getViewTaskKindsFromString(inViewTaskKinds);

        String searchContent = ClassHelper.convert(params.getProperty("searchContent"), String.class, "");

        if (Util.isNotEmptyString(searchContent)) {
            sb.append(" and (");
            // 主题
            sb.append("  te.description_ like :description");
            queryModel.putLikeParam("description", searchContent);
            // 业务编码
            sb.append(" or te.business_code_ like :bizCode");
            queryModel.putLikeParam("bizCode", searchContent);
            // 任务的创建人
            sb.append(" or te.creator_person_member_name_ like :creatorPersonNemberName");
            queryModel.putLikeParam("creatorPersonNemberName", searchContent);
            // 流程的申请人
            sb.append(" or te.applicant_person_member_name_ like :applicantPersonNemberName");
            queryModel.putLikeParam("applicantPersonNemberName", searchContent);
            sb.append(")");
        }

        String executorPersonMemberName = ClassHelper.convert(params.getProperty("executorPersonMemberName"), String.class, "");
        String creatorPersonNemberName = ClassHelper.convert(params.getProperty("creatorPersonNemberName"), String.class, "");
        String applicantPersonNemberName = ClassHelper.convert(params.getProperty("applicantPersonNemberName"), String.class, "");

        if (Util.isNotEmptyString(executorPersonMemberName)) {
            // 执行人
            sb.append(" and te.executor_person_member_name_ like :executorPersonMemberName");
            queryModel.putLikeParam("executorPersonMemberName", executorPersonMemberName);
        }

        if (Util.isNotEmptyString(creatorPersonNemberName)) {
            // 创建人
            sb.append(" and te.creator_person_member_name_ like :creatorPersonNemberName");
            queryModel.putLikeParam("creatorPersonNemberName", creatorPersonNemberName);
        }

        if (Util.isNotEmptyString(applicantPersonNemberName)) {
            // 申请人
            sb.append(" and te.applicant_person_member_name_ like :applicantPersonNemberName");
            queryModel.putLikeParam("applicantPersonNemberName", applicantPersonNemberName);
        }

        DateRange dateRange = DateRange.fromId(dateRangeId);
        switch (dateRange) {
        case CUSTOM:
            sb.append(" and te.start_time_ between :startDate and :endDate");
            queryModel.putParam("startDate", DateUtil.getDateTimeBegin(DateUtil.getDateParse(params.getProperty("startDate").toString())));
            queryModel.putParam("endDate", DateUtil.getDateTimeEnd(DateUtil.getDateParse(params.getProperty("endDate").toString())));
            break;
        case ALL:
            break;
        default:
            Map<String, Object> dateBetween = DateRange.getDataRange(dateRange);
            sb.append(" and te.start_time_ between :startDate and :endDate");
            queryModel.putParam("startDate", dateBetween.get("startDate"));
            queryModel.putParam("endDate", dateBetween.get("endDate"));
        }

        if (!StringUtil.isBlank(procFullId) && !"All".equalsIgnoreCase(procFullId)) {
            if ("NotProcTask".equalsIgnoreCase(procFullId)) {
                sb.append(" and pi.proc_full_id is null");
            } else {
                if ("true".equals(excludeSpecifiedProc)) {
                    sb.append(" and pi.proc_full_id not like :procFullId");
                } else {
                    sb.append(" and pi.proc_full_id like :procFullId");
                }
                queryModel.putParam("procFullId", procFullId + "%");
            }
        }

        if (queryCategory.equalsIgnoreCase("myTransaction")) {
            ViewTaskKind viewKind = viewTaskKinds.get(0);

            switch (viewKind) {
            case WAITING:
                sql = this.getRuntimeTaskSql();
                sb.append(" and  te.executor_person_member_id_ like  :personId");
                queryModel.putParam("personId", personId + "%");

                // 排除waited状态
                statuses.add("ready");
                statuses.add("executing");
                statuses.add("sleeping");
                statusCriteria = "  and te.status_id_ in (" + Util.arrayToString(statuses.toArray(), ":%s", ",") + ")";
                sb.append(statusCriteria);
                for (String status : statuses) {
                    queryModel.putParam(status, status);
                }

                if (!StringUtil.isBlank(toDoTaskKind)) {
                    sb.append(" and  nvl(te.need_timing_, 0) = :needTiming");
                    int needTiming = ToDoTaskKind.NEED_TIMING.equalsIgnoreCase(toDoTaskKind) ? 1 : 0;
                    queryModel.putParam("needTiming", needTiming);
                }
                break;
            case COMPLETED:
                sql = this.getHistoryTaskSql();
                statuses.add("completed");
                statuses.add("transmited");
                statuses.add("returned");
                statuses.add("aborted");
                // statuses.add("canceled");
                statuses.add("deleted");
                // 执行人
                sb.append(" and  te.executor_person_member_id_ like  :personId");
                queryModel.putParam("personId", personId + "%");

                // 状态
                statusCriteria = "  and te.status_id_ in (" + Util.arrayToString(statuses.toArray(), ":%s", ",") + ")";
                sb.append(statusCriteria);
                for (String status : statuses) {
                    queryModel.putParam(status, status);
                }

                break;
            case SUBMITED:
                sql = this.getHistoryTaskSql();
                sb.append(" and te.creator_person_member_id_ like :personId");
                queryModel.putParam("personId", personId + "%");
                break;
            case DRAFT:
                sql = this.getRuntimeTaskSql();
                sb.append(" and te.executor_person_member_id_ like :personId and te.task_def_key_ = :taskDefKey");
                queryModel.putParam("personId", personId + "%");
                queryModel.putParam("taskDefKey", "Apply");
                break;
            case INITIATE:
            case TRACKING:
                sql = this.getHistoryTaskSql();
                sb.append(" and te.creator_person_member_id_ like :personId and te.task_def_key_ = :taskDefKey and te.previous_id_ is null");
                if (viewKind == ViewTaskKind.TRACKING) {
                    sb.append(" and pi.end_time_ is null and te.status_id_ = 'completed' ");
                }
                queryModel.putParam("personId", personId + "%");
                queryModel.putParam("taskDefKey", "Apply");
                break;
            case COLLECTED:
                sql = getCollectTaskSql();
                sb.append(" and pc.person_id = :personId");
                queryModel.putParam("personId", personId);
                break;
            case PROC_INST_COMPLETED:
                sql = this.getHistoryTaskSql();
                sb.append(" and  te.executor_person_member_id_ like  :personId and pi.proc_status_id_ != 'executing'");
                queryModel.putParam("personId", personId + "%");
                // 包含流程异常结束的
                boolean includeProcCompletedException = Boolean.valueOf(params.getString("inclueProcCompletedException"));
                if (!includeProcCompletedException) {
                    sb.append(" and pi.proc_status_id_ = :procStatusId");
                    queryModel.putParam("procStatusId", "completed");
                }
                break;
            default:
                break;
            }
        } else {
            String orgCondition = null, internalStatusCriteria = null;
            boolean isSingleProcInstShowOneTask = "true".equalsIgnoreCase(singleProcInstShowOneTask);
            boolean hasCondition = false;
            sql = this.getHistoryTaskSql();

            if (viewTaskKinds.contains(ViewTaskKind.WAITING) || viewTaskKinds.contains(ViewTaskKind.COMPLETED)) {
                if (viewTaskKinds.contains(ViewTaskKind.WAITING)) {
                    statuses.add("ready");
                    statuses.add("executing");
                    if (StringUtil.isBlank(toDoTaskKind)) {
                        statuses.add("waited");
                    }
                    statuses.add("sleeping");
                }
                if (viewTaskKinds.contains(ViewTaskKind.COMPLETED)) {
                    statuses.add("completed");
                    statuses.add("transmited");
                    statuses.add("returned");
                    statuses.add("aborted");
                    // statuses.add("canceled");
                    statuses.add("deleted");
                }

                sb.append(" and (");

                hasCondition = true;

                statusCriteria = "  te.status_id_ in (" + Util.arrayToString(statuses.toArray(), ":%s", ",") + ")";
                internalStatusCriteria = "  tei.status_id_ in (" + Util.arrayToString(statuses.toArray(), ":%s_i", ",") + ")";

                if (!StringUtil.isBlank(administrativeOrgFullId)) {
                    sb.append(String.format(" (te.executor_full_id_ like  :administrativeOrgFullId and %s )", statusCriteria));
                    queryModel.putParam("administrativeOrgFullId", administrativeOrgFullId + "%");
                    if (isSingleProcInstShowOneTask) {
                        orgCondition = String.format(" and tei.executor_full_id_ like  :interalOrgId  and %s ", internalStatusCriteria);
                        queryModel.putParam("interalOrgId", administrativeOrgFullId + "%");
                    }
                } else {
                    sb.append(String.format(" ( te.executor_person_member_id_ like :personId and %s )", statusCriteria));
                    queryModel.putParam("personId", personId + "%");

                    if (isSingleProcInstShowOneTask) {
                        orgCondition = String.format(" and  tei.executor_person_member_id_ like :interalOrgId and %s", internalStatusCriteria);
                        queryModel.putParam("interalOrgId", personId + "%");
                    }
                }
                for (String status : statuses) {
                    queryModel.putParam(status, status);
                    if (isSingleProcInstShowOneTask) {
                        queryModel.putParam(String.format("%s_i", status), status);
                    }
                }
            } else if (viewTaskKinds.contains(ViewTaskKind.SUBMITED)) {
                if (hasCondition) {
                    sb.append(" or ");
                } else {
                    sb.append(" and (");
                }
                hasCondition = true;
                if (!StringUtil.isBlank(administrativeOrgFullId)) {
                    sb.append("  (te.creator_full_id_ like  :administrativeOrgFullId)");
                    queryModel.putParam("administrativeOrgFullId", administrativeOrgFullId + "%");
                    if (isSingleProcInstShowOneTask) {
                        orgCondition = " and  tei.creator_full_id_ like  :interalOrgId ";
                        queryModel.putParam("interalOrgId", administrativeOrgFullId + "%");
                    }
                } else {
                    sb.append(" (te.creator_person_member_id_ like :personId)");
                    queryModel.putParam("personId", personId + "%");
                    if (isSingleProcInstShowOneTask) {
                        orgCondition = " and  tei.creator_person_member_id_ like  :interalOrgId ";
                        queryModel.putParam("interalOrgId", personId + "%");
                    }
                }
            } else if (viewTaskKinds.contains(ViewTaskKind.DRAFT)) {
                if (hasCondition) {
                    sb.append(" or ");
                } else {
                    sb.append(" and (");
                }
                hasCondition = true;

                statuses.clear();

                statuses.add("ready");
                statuses.add("executing");
                statuses.add("sleeping");
                statuses.add("waited");

                statusCriteria = "  te.status_id_ in (" + Util.arrayToString(statuses.toArray(), ":%s", ",") + ")";
                internalStatusCriteria = "  tei.status_id_ in (" + Util.arrayToString(statuses.toArray(), ":%s", ",") + ")";

                if (!StringUtil.isBlank(administrativeOrgFullId)) {
                    sb.append("  ( te.creator_full_id_ like  :administrativeOrgFullId  and te.task_def_key_ = 'Apply' and " + statusCriteria + ")");
                    queryModel.putParam("administrativeOrgFullId", administrativeOrgFullId + "%");
                    if (isSingleProcInstShowOneTask) {
                        orgCondition = " and  tei.creator_full_id_ like  :interalOrgId and tei.task_def_key_ = 'Apply' and " + statusCriteria;
                        queryModel.putParam("interalOrgId", administrativeOrgFullId + "%");
                    }
                } else {
                    sb.append(" ( te.creator_person_member_id_ like :personId and te.task_def_key_ = 'Apply' and " + statusCriteria + ")");
                    queryModel.putParam("personId", personId + "%");
                    if (isSingleProcInstShowOneTask) {
                        orgCondition = " and  tei.creator_person_member_id_ like  :interalOrgId and tei.task_def_key_ = 'Apply' and " + statusCriteria;
                        queryModel.putParam("interalOrgId", personId + "%");
                    }
                }
                for (String status : statuses) {
                    queryModel.putParam(status, status);
                    if (isSingleProcInstShowOneTask) {
                        queryModel.putParam(String.format("%s_i", status), status);
                    }
                }

            } else if (viewTaskKinds.contains(ViewTaskKind.INITIATE)) {
                if (hasCondition) {
                    sb.append(" or ");
                } else {
                    sb.append(" and (");
                }
                hasCondition = true;
                if (!StringUtil.isBlank(administrativeOrgFullId)) {
                    sb.append("  ( te.creator_full_id_ like  :administrativeOrgFullId  and te.task_def_key_ = 'Apply' and te.previous_id_ is null)");
                    queryModel.putParam("administrativeOrgFullId", administrativeOrgFullId + "%");
                    if (isSingleProcInstShowOneTask) {
                        orgCondition = " and  tei.creator_full_id_ like  :interalOrgId and i.task_def_key_ = 'Apply'";
                        queryModel.putParam("interalOrgId", administrativeOrgFullId + "%");
                    }
                } else {
                    sb.append("  (te.creator_person_member_id_ like :personId and te.task_def_key_ = 'Apply')");
                    queryModel.putParam("personId", personId + "%");
                    if (isSingleProcInstShowOneTask) {
                        orgCondition = " and  tei.creator_person_member_id_ like  :interalOrgId and tei.task_def_key_ = 'Apply'";
                        queryModel.putParam("interalOrgId", personId + "%");
                    }
                }
            } else if (viewTaskKinds.contains(ViewTaskKind.COLLECTED)) {
                if (hasCondition) {
                    sb.append(" or ");
                } else {
                    sb.append(" and (");
                }
                hasCondition = true;
                sb.append("  (te.id_ in (select pc.task_id from WF_TaskCollection pc where pc.person_id = :personId) )");
                queryModel.putParam("personId", personId);
            } else if (viewTaskKinds.contains(ViewTaskKind.PROC_INST_COMPLETED)) {
                sb.append(" and  te.executor_person_member_id_ like  :personId and pi.proc_status_id_ != 'executing'");
                queryModel.putParam("personId", personId + "%");
                // 包含流程异常结束的
                boolean includeProcCompletedException = Boolean.valueOf(params.getString("inclueProcCompletedException"));
                if (!includeProcCompletedException) {
                    sb.append(" and pi.proc_status_id_ = :procStatusId");
                    queryModel.putParam("procStatusId", "completed");
                }
                if (isSingleProcInstShowOneTask) {
                    orgCondition = " and  tei.executor_person_member_id_ like  :interalOrgId and tei.status_id_ = 'completed'";
                    queryModel.putParam("interalOrgId", personId + "%");
                }
            }
            if (hasCondition) {
                sb.append(")");
            }

            if (!StringUtil.isBlank(procDefineId)) {
                if ("true".equals(excludeSpecifiedProc)) {
                    sb.append(" and  pi.proc_def_id_ not like :procDefineId");
                } else {
                    sb.append(" and  pi.proc_def_id_ like :procDefineId");
                }
                queryModel.putParam("procDefineId", procDefineId + "%");
            }

            if ("true".equalsIgnoreCase(onlyQueryApplyProcUnit)) {
                sb.append(" and  te.task_def_key_ = 'Apply' ");
            }

            if ("true".equalsIgnoreCase(singleProcInstShowOneTask)) {
                sb.append(" and te.version_ in (select max(tei.version_)");
                sb.append("  from act_hi_taskinst_extension tei");
                sb.append(" where pi.proc_inst_id_ = tei.proc_inst_id_");
                if (!StringUtil.isBlank(orgCondition)) {
                    sb.append(orgCondition);
                }
                if ("true".equalsIgnoreCase(onlyQueryApplyProcUnit)) {
                    sb.append(" and  tei.task_def_key_ = 'Apply' ");
                }
                sb.append(" )");
            }
        }

        if (viewTaskKinds.size() == 0) {
            sb.append(" and  (te.creator_person_member_id_ like :creatorId or te.executor_person_member_id_ like :executorId)");
            queryModel.putParam("creatorId", personId + "%");
            queryModel.putParam("executorId", personId + "%");
        }

        // 任务类别
        if (taskKinds != null && taskKinds.size() > 0) {
            sb.append("  and te.kind_id_ in (" + Util.arrayToString(taskKinds.toArray(), ":%s", ",") + ")");
            for (String taskKind : taskKinds) {
                queryModel.putParam(taskKind, taskKind);
            }
        }

        sql += sb.toString();
        queryModel.setSql(sql);
        Map<String, Object> result = this.sqlExecutorDao.executeSlicedQuery(queryModel);
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<?> queryFlowChartProcedure(String bizId) {
        Assert.hasText(bizId, "参数bizId不能为空。");
        // 从历史流程实例表中查询流程
        List<HistoricProcessInstance> historicProcessInstances = this.historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(bizId)
                                                                                    .list();
        if (historicProcessInstances.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        HistoricProcessInstance historicProcessInstance = historicProcessInstances.get(0);
        List<ProcDefinition> procUnits = this.procDefinitionRespository.findProcUnitsForSequnce(historicProcessInstance.getProcessDefinitionKey());
        if (procUnits.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        Map<String, Object> resultItem;
        List<Map<String, Object>> procUnitHandlers;
        String procUnitId;

        Map<String, Object> approvalData = this.actApplication.queryApprovalHistoryByBizId(bizId);
        List<Map<String, Object>> approvalHandlers = (List<Map<String, Object>>) approvalData.get(Constants.ROWS);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(procUnits.size());
        String statusId = null;
        for (ProcDefinition procUnit : procUnits) {
            resultItem = new HashMap<String, Object>(3);

            resultItem.put("procUnitId", procUnit.getCode());
            resultItem.put("procUnitName", procUnit.getName());

            // 回退，重复处理，不是按照环节顺序处理；
            procUnitHandlers = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> approvalHandler : approvalHandlers) {
                statusId = ClassHelper.convert(approvalHandler.get("statusId"), String.class, "");
                // 任务状态为取消
                if (statusId != null && statusId.equals(TaskStatus.CANCELED.getId())) {
                    continue;
                }
                procUnitId = ClassHelper.convert(approvalHandler.get("taskDefKey"), String.class, "");
                if (procUnit.getCode().equalsIgnoreCase(procUnitId)) {
                    procUnitHandlers.add(approvalHandler);
                }
            }

            resultItem.put("procUnitHandlers", procUnitHandlers);
            result.add(resultItem);
        }
        return result;
    }

    /**
     * rules:[
     * {
     * showAllRules:
     * nodeKindId:
     * name:
     * isChoosed:
     * priority:
     * elements:[{
     * code:
     * name:
     * operatorCode:
     * operatorName:
     * valueId:
     * valueName:
     * }
     * ],
     * handlers:[{
     * 1 approvalRuleHandlers;
     * 2 procUnitHandlers and task instance
     * }
     * ],
     * children:[
     * rules:
     * ]
     * }
     * ]
     */
    private List<Map<String, Object>> buildFlowChartHandlersForProcUnitHandler(String bizId, String procUnitId) {
        // handlers:[{group1: groupHandlers:{description:, ...,opinion: },{...}, {groupN: groupHandlers:{description:, ...,opinion: }}]
        List<Map<String, Object>> procUnitHandlers = this.procUnitHandlerApplication.queryProcUnitHandlersAndTaskInsts(bizId, procUnitId);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map<String, Object> handlerTaskMap, groupMap;
        Integer currentGroupId = -1, groupId;
        List<Map<String, Object>> groupHandlers = null;
        String statusId = null;
        for (Map<String, Object> procUnitHandler : procUnitHandlers) {
            statusId = ClassHelper.convert(procUnitHandler.get("statusId"), String.class, "");
            // 任务状态为取消
            if (statusId != null && statusId.equals(TaskStatus.CANCELED.getId())) {
                continue;
            }
            groupId = ClassHelper.convert(procUnitHandler.get("groupId"), Integer.class);
            if (!currentGroupId.equals(groupId)) {
                currentGroupId = groupId;

                groupMap = new HashMap<String, Object>(2);
                groupHandlers = new ArrayList<Map<String, Object>>();

                groupMap.put("groupId", currentGroupId);
                groupMap.put("groupHandlers", groupHandlers);

                result.add(groupMap);
            }
            handlerTaskMap = new HashMap<String, Object>(13);

            handlerTaskMap.put("description", procUnitHandler.get("subProcUnitName"));
            handlerTaskMap.put("handlerName", procUnitHandler.get("handlerName"));

            handlerTaskMap.put("executorOgnName", procUnitHandler.get("orgName"));
            handlerTaskMap.put("executorDeptName", procUnitHandler.get("deptName"));
            handlerTaskMap.put("executorPosName", procUnitHandler.get("positionName"));
            handlerTaskMap.put("executorPersonMemberName", procUnitHandler.get("handlerName"));

            handlerTaskMap.put("statusId", procUnitHandler.get("statusId"));
            handlerTaskMap.put("statusName", procUnitHandler.get("statusName"));

            handlerTaskMap.put("startTime", procUnitHandler.get("startTime"));
            handlerTaskMap.put("endTime", procUnitHandler.get("endTime"));
            handlerTaskMap.put("duration", procUnitHandler.get("duration"));

            handlerTaskMap.put("result", procUnitHandler.get("result"));
            handlerTaskMap.put("opinion", procUnitHandler.get("opinion"));

            groupHandlers.add(handlerTaskMap);
        }
        return result;
    }

    private List<Map<String, Object>> buildFlowChartHandlers(List<ApprovalRule> approvalRules, String parentId, String bizId, String choosedApprovalRuleId) {
        // handlers:[{group1: groupHandlers:{description:, ...,opinion: },{...}, {groupN: groupHandlers:{description:, ...,opinion: }}]
        // [{nodeKindId: , name: ruleName: priority: , children: []},
        // {{nodeKindId: , name: ruleName: priority: , elements: [{ }], handlers: handlers }]
        List<Map<String, Object>> rules = new ArrayList<Map<String, Object>>();

        Map<String, Object> rule = null;
        List<Map<String, Object>> children;
        List<Map<String, Object>> handlers, groupHandlers = null;
        List<Map<String, Object>> approvalRuleElements;
        Map<String, Object> handlerTaskMap, groupMap;
        Integer groupId;

        String operatorId, operatorName, valueId, valueName, value;

        for (ApprovalRule approvalRule : approvalRules) {
            if (!approvalRule.getParentId().equals(parentId)) {
                continue;
            }
            rule = new HashMap<String, Object>(5);

            rule.put("nodeKindId", approvalRule.getNodeKindId());
            rule.put("name", approvalRule.getName());
            rule.put("priority", approvalRule.getPriority());

            if (ApprovalRule.NodeKind.isCategory(approvalRule.getNodeKindId())) {
                children = buildFlowChartHandlers(approvalRules, approvalRule.getId(), bizId, choosedApprovalRuleId);
                rule.put("children", children);
                rules.add(rule);
            } else {
                // 审批要素
                approvalRuleElements = this.approvalRuleApplication.queryApprovalRuleElementsForFlowChart(approvalRule.getId());
                for (Map<String, Object> approvalRuleElement : approvalRuleElements) {
                    operatorId = ClassHelper.convert(approvalRuleElement.get("foperator"), String.class);
                    operatorName = ApprovalRuleElement.OperatorKind.fromId(operatorId).getDisplayName();

                    approvalRuleElement.put("operatorId", operatorId);
                    approvalRuleElement.put("operatorName", operatorName);
                    approvalRuleElement.remove("foperator");

                    valueId = ClassHelper.convert(approvalRuleElement.get("fvalueId"), String.class);
                    valueName = ClassHelper.convert(approvalRuleElement.get("fvalue"), String.class);

                    value = StringUtil.isBlank(valueName) ? valueId : valueName;
                    approvalRuleElement.put("value", value);

                    approvalRuleElement.remove("fvalue");
                    approvalRuleElement.remove("fvalueId");
                }
                rule.put("elements", approvalRuleElements);
                // 是否选择
                boolean isChoosed = approvalRule.getId().equals(choosedApprovalRuleId);
                rule.put("isChoosed", isChoosed);
                if (isChoosed) {
                    handlers = buildFlowChartHandlersForProcUnitHandler(bizId, approvalRule.getProcUnitId());
                    rule.put("handlers", handlers);
                } else {
                    List<ApprovalRuleHandler> approvalRuleHandlers = approvalRule.getApprovalRuleHandlers();
                    handlers = new ArrayList<Map<String, Object>>(approvalRuleHandlers.size());
                    groupId = -1;
                    for (ApprovalRuleHandler approvalRuleHandler : approvalRuleHandlers) {
                        // handlers: [{group: 10, groupHandlers:[ { description: , handlerName: }, { description: , handlerName: }]}]
                        if (!groupId.equals(approvalRuleHandler.getGroupId())) {
                            groupId = approvalRuleHandler.getGroupId();

                            groupMap = new HashMap<String, Object>(2);
                            groupHandlers = new ArrayList<Map<String, Object>>();

                            groupMap.put("groupId", groupId);
                            groupMap.put("groupHandlers", groupHandlers);

                            handlers.add(groupMap);
                        }

                        handlerTaskMap = new HashMap<String, Object>(2);

                        handlerTaskMap.put("description", approvalRuleHandler.getDescription());
                        handlerTaskMap.put("handlerName", approvalRuleHandler.getHandlerName());

                        groupHandlers.add(handlerTaskMap);
                    }

                    rule.put("handlers", handlers);
                }
                rules.add(rule);
            }
        }
        return rules;
    }

    @Override
    public Map<String, Object> queryFlowChart(String bizId, String ownerOrgId, String isShowAllRules) {
        Assert.hasText(bizId, "参数bizId不能为空。");
        // 1、流程定义
        HistoricProcessInstance historicProcessInstance = this.historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(bizId)
                                                                             .singleResult();
        if (historicProcessInstance == null) {
            return null;
        }

        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> procMap = new HashMap<String, Object>(2); // 流程信息
        Map<String, Object> procUnitMap; // 流程环节信息
        Map<String, Object> applicantTaskMap; // 申请人任务信息
        float duration;
        isShowAllRules = StringUtil.tryThese(isShowAllRules, SystemCache.getParameter("wf.chart.showAllRules", String.class), "false");
        Boolean showAllRules = ClassHelper.convert(isShowAllRules, Boolean.class);
        showAllRules = showAllRules == null ? false : showAllRules;

        String procId = historicProcessInstance.getProcessDefinitionKey();
        ProcDefinition proc = this.procDefinitionRespository.findProc(procId);
        procMap.put("code", proc.getCode());
        procMap.put("name", proc.getName());

        result.put("procDefinition", procMap);
        result.put("showAllRules", showAllRules);
        // 2、流程环节
        // start end 环节
        List<ProcDefinition> procUnits = this.procDefinitionRespository.findProcUnitsForSequnce(procId);
        String choosedApprovalRuleId = null;
        // 3、流程规则
        List<Map<String, Object>> ruleMaps = new ArrayList<Map<String, Object>>();

        List<ApprovalRule> approvalRules;

        List<Map<String, Object>> procUnitMaps = new ArrayList<Map<String, Object>>(procUnits.size() + 2);
        procUnitMap = new HashMap<String, Object>(1);

        procUnitMap.put("procUnitId", "StartEvent");
        procUnitMaps.add(procUnitMap);
        for (ProcDefinition procUnit : procUnits) {
            procUnitMap = new HashMap<String, Object>();

            procUnitMaps.add(procUnitMap);

            procUnitMap.put("procUnitId", procUnit.getCode());
            procUnitMap.put("procUnitName", procUnit.getName());
            // 申请环节
            if (ActivityKind.isApplyActivity(procUnit.getCode())) {
                HistoricTaskInstanceExtension historicTaskInstanceExtension = this.actApplication.queryApplicantTask(bizId);
                applicantTaskMap = new HashMap<String, Object>(13);

                applicantTaskMap.put("description", historicTaskInstanceExtension.getName());
                applicantTaskMap.put("handlerName", historicTaskInstanceExtension.getExecutorPersonMemberName());

                applicantTaskMap.put("executorOgnName", historicTaskInstanceExtension.getExecutorOgnName());
                applicantTaskMap.put("executorDeptName", historicTaskInstanceExtension.getExecutorDeptName());
                applicantTaskMap.put("executorPosName", historicTaskInstanceExtension.getExecutorPosName());
                applicantTaskMap.put("executorPersonMemberName", historicTaskInstanceExtension.getExecutorPersonMemberName());

                applicantTaskMap.put("statusId", historicTaskInstanceExtension.getStatusId());
                applicantTaskMap.put("statusName", historicTaskInstanceExtension.getStatusName());

                applicantTaskMap.put("startTime", historicTaskInstanceExtension.getStartTime());
                applicantTaskMap.put("endTime", historicTaskInstanceExtension.getEndTime());
                if (historicTaskInstanceExtension.getDuration() != null) {
                    duration = historicTaskInstanceExtension.getDuration() * 100 / (60 * 60 * 1000);
                    duration = Math.round(duration) / 100;
                    applicantTaskMap.put("duration", duration);
                }

                applicantTaskMap.put("result", historicTaskInstanceExtension.getResult());
                applicantTaskMap.put("opinion", historicTaskInstanceExtension.getOpinion());

                procUnitMap.put("handlers", applicantTaskMap);
                continue;
            }

            choosedApprovalRuleId = this.procUnitHandlerApplication.queryApprovalRuleId(bizId, procUnit.getCode());

            if (showAllRules) {
                if (StringUtil.isBlank(ownerOrgId)) {
                    Operator operator = OpmUtil.getBizOperator();
                    ownerOrgId = operator.getOrgId();
                }
                approvalRules = this.procApprovalRuleParseService.queryScopeApprovalRules(procUnit.getProcId(), procUnit.getCode(), ownerOrgId, true);
                ruleMaps = this.buildFlowChartHandlers(approvalRules, ProcDefinition.ROOT_ID, bizId, choosedApprovalRuleId);
            } else {
                // 1、获取审批规则
                // 2、获取处理人，通过groupId分组
                // 3、组合成规则
                ApprovalRule approvalRule = null;
                ruleMaps = new ArrayList<Map<String, Object>>(1);
                if (StringUtil.isNotBlank(choosedApprovalRuleId)) {
                    approvalRule = this.approvalRuleApplication.loadApprovalRule(choosedApprovalRuleId);
                    if (approvalRule != null) {
                        Map<String, Object> rule = new HashMap<String, Object>(4);
                        rule.put("nodeKindId", ApprovalRule.NodeKind.RULE.getId());
                        rule.put("name", approvalRule.getName());
                        rule.put("priority", 0);
                        rule.put("handlers", buildFlowChartHandlersForProcUnitHandler(bizId, procUnit.getCode()));
                        ruleMaps.add(rule);
                    }
                }
            }
            procUnitMap.put("rules", ruleMaps);
            procUnitMap.put("currentProcUnit", StringUtil.isNotBlank(choosedApprovalRuleId));
        }

        procUnitMap = new HashMap<String, Object>(1);
        procUnitMap.put("procUnitId", "EndEvent");
        procUnitMaps.add(procUnitMap);

        result.put("procUnits", procUnitMaps);
        // 4、流程处理人
        return result;
    }

    /**
     * 获取任务节点信息
     * 
     * @author
     * @param sdo
     * @return Map<String,Object>
     */
    public Map<String, Object> getProcedureInfo(String bizId, String procUnitHandlerId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select te.id_                    id,");
        sb.append("       te.task_def_key_          task_def_key,");
        sb.append("       te.name_                  name,");
        sb.append("       te.description_           description,");
        sb.append("       te.executor_full_id_,");
        sb.append("       te.executor_full_name_   executor_full_name,");
        sb.append("       te.start_time_            start_time,");
        sb.append("       te.end_time_              end_time,");
        sb.append("       te.status_id_            status_id");
        sb.append("  from act_hi_taskinst_extension te, act_hi_procinst h");
        sb.append(" where h.id_ = te.proc_inst_id_");
        sb.append("   and h.business_key_ = ?");
        sb.append("   and te.proc_unit_handler_id_=?");
        return this.sqlExecutorDao.queryToMap(sb.toString(), bizId, procUnitHandlerId);
    }

    // @Override
    // public Map<String, Object> queryApprovalHistoryByProcInstId(String
    // procInstId) {
    // return this.actService.queryApprovalHistoryByProcInstId(procInstId);
    // }

    @Override
    public Map<String, Object> queryApprovalHistoryByBizId(String bizId) {
        return this.actApplication.queryApprovalHistoryByBizId(bizId);
    }

    @Override
    public Map<String, Object> queryBackTasksByBizCode(String bizCode, String procUnitId, Integer groupId) {
        return this.actApplication.queryBackTasksByBizCode(bizCode, procUnitId, groupId);
    }

    @Override
    public Map<String, Object> queryBackProcUnit(String procInstId, String approvalRuleId, Integer groupId) {
        ApprovalRule ApprovalRule = this.approvalRuleApplication.loadApprovalRule(approvalRuleId);
        Assert.notNull(ApprovalRule, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, approvalRuleId, "审批规则"));
        Map<String, Object> data = this.actApplication.queryApprovalHistoryByProcessInstanceId(procInstId);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> tasks = (List<Map<String, Object>>) data.get(Constants.ROWS);
        List<Map<String, Object>> backProcUnit = new ArrayList<Map<String, Object>>();

        /**
         * 申请环节
         */
        Map<String, Object> findApplyTask = null;
        String procUnitId = null;
        for (int i = tasks.size() - 1; i >= 0; i--) {
            findApplyTask = tasks.get(i);
            procUnitId = findApplyTask.get("taskDefKey").toString();
            if (procUnitId.equalsIgnoreCase("apply")) {
                Map<String, Object> applyProcUnit = new HashMap<String, Object>(4);
                applyProcUnit.put("taskDefKey", findApplyTask.get("taskDefKey"));
                applyProcUnit.put("name", findApplyTask.get("name"));
                applyProcUnit.put("executorFullName", findApplyTask.get("executorFullName"));
                applyProcUnit.put("groupId", 0);
                backProcUnit.add(applyProcUnit);
                break;
            }
        }
        /**
         * 审批环节
         */
        Object handlerFullName;
        Object handlerGroupId;
        Long approvalRuleHandlerId;
        String taskStatusId;
        for (ApprovalRuleHandler approvalRuleHandler : ApprovalRule.getApprovalRuleHandlers()) {
            if (approvalRuleHandler.getGroupId() >= groupId) {
                continue;
            }
            // 环节的处理行人
            handlerFullName = null;
            handlerGroupId = null;
            for (Map<String, Object> task : tasks) {
                approvalRuleHandlerId = ClassHelper.convert(task.get("approvalRuleHandlerId"), Long.class);
                taskStatusId = ClassHelper.convert(task.get("statusId"), String.class);
                if (approvalRuleHandlerId != null && approvalRuleHandlerId.equals(approvalRuleHandler.getId()) && taskStatusId.equalsIgnoreCase("completed")) {
                    handlerFullName = task.get("executorFullName");
                    handlerGroupId = task.get("groupId");
                    break;
                }
            }

            if (handlerFullName != null) {
                Map<String, Object> approveProcUnit = new HashMap<String, Object>(4);
                approveProcUnit.put("taskDefKey", ApprovalRule.getProcUnitId());
                approveProcUnit.put("name", approvalRuleHandler.getDescription());
                approveProcUnit.put("executorFullName", handlerFullName);
                approveProcUnit.put("groupId", handlerGroupId);
                backProcUnit.add(approveProcUnit);
            }
        }

        Map<String, Object> result = new HashMap<String, Object>(1);
        result.put(Constants.ROWS, backProcUnit);
        return result;
    }

    @Override
    public Map<String, Object> queryCounterSignHandlers(String bizId, String procUnitId) {
        return this.procUnitHandlerApplication.queryCounterSignHandlers(bizId, procUnitId);
    }

    @Override
    @Transactional
    public void saveCounterSignHandlers(String bizId, String procUnitId, Long version, String currentProcUnitHandlerId, List<String> minusSignIds,
                                        List<ProcUnitHandler> countersigns) {
        this.procUnitHandlerApplication.saveCounterSignHandlers(bizId, procUnitId, version, currentProcUnitHandlerId, minusSignIds, countersigns);
    }

    @Override
    public List<ProcUnitHandler> queryAssistHandlers(String bizId, String procUnitId, String chiefId) {
        return this.procUnitHandlerApplication.queryAssistantHandlers(bizId, procUnitId, chiefId);
    }

    @Override
    @Transactional
    public void assist(String bizId, String taskId, String procUnitHandlerId, List<String> deletedIds, List<String> executorIds, List<Integer> sendMessages) {
        for (String deletedId : deletedIds) {
            AssistCmd cmd = new AssistCmd(bizId, taskId, deletedId, AssistCmd.OPERATE_REMOVE, null, sendMessages, this.procUnitHandlerApplication,
                                          this.actApplication);
            this.managementService.executeCommand(cmd);
        }

        if (executorIds.size() > 0) {
            AssistCmd cmd = new AssistCmd(bizId, taskId, procUnitHandlerId, AssistCmd.OPERATE_ADD, executorIds, sendMessages, this.procUnitHandlerApplication,
                                          this.actApplication);
            this.managementService.executeCommand(cmd);
        }

        fireProcessEvent(ProcessEvent.AFTER_ASSIST_EVENT);
    }

    @Override
    public List<Map<String, Object>> queryUIElmentPermissionsByProcUnitHandlerId(String id) {
        String chiefHandlerId = id;
        // 协审人的字段权限与主审人保存一致
        ProcUnitHandler procUnitHandler = this.procUnitHandlerApplication.loadProcUnitHandler(id);
        if (procUnitHandler != null && procUnitHandler.getCooperationModelId() == CooperationModelKind.ASSISTANT) {
            chiefHandlerId = procUnitHandler.getChiefId();
        }

        return this.procUnitHandlerApplication.queryUIElmentPermissions(chiefHandlerId);
    }

    @Override
    public Map<String, Object> queryTrackingTasks() {
        return this.actApplication.queryOperatorTrackingTasks();
    }

    @Override
    @Transactional
    public void updateHistoricTaskInstanceStatus(String taskId, String status) {
        Assert.hasText(taskId, "参数taskId不能为空。");
        Assert.hasText(status, "参数status不能为空。");
        TaskStatus taskStatus = TaskStatus.fromId(status);

        this.actApplication.updateHistoricTaskInstanceExtensionStatus(taskId, taskStatus);

        ProcessEventContext.addUpdatedStatusTask(taskId);

        fireProcessEvent(ProcessEvent.AFTER_UPDATE_HISTORIC_TASK_INSTANCE_STATUS_EVENT);
    }

    @Override
    @Transactional
    public void updateTaskExtensionStatus(String taskId, TaskStatus taskStatus) {
        this.actApplication.updateTaskExtensionStatus(taskId, taskStatus);

        ProcessEventContext.addUpdatedStatusTask(taskId);

        fireProcessEvent(ProcessEvent.AFTER_UPDATE_TASK_EXTENSION_STATUS_EVENT);
    }

    @Override
    @Transactional
    public void launchMendTask(String bizId, String procUnitId) {
        Assert.hasText(bizId, "参数bizId不能为空。");
        Assert.hasText(procUnitId, "参数procUnitId不能为空。");

        List<HistoricTaskInstance> historicTaskInstances = this.historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(bizId)
                                                                              .taskDefinitionKey(procUnitId).list();
        Assert.isTrue(historicTaskInstances.size() > 0, "在历史任务表中未找到任务。");

        List<ProcUnitHandler> mendProcUnitHandlers = this.procUnitHandlerApplication.queryInitialMendProcUnitHandlers(bizId, procUnitId);

        HistoricTaskInstance lastHiTaskInst = historicTaskInstances.get(historicTaskInstances.size() - 1);
        Map<String, Object> additiveData = new HashMap<String, Object>(1);
        additiveData.put("needTiming", BooleanKind.YES.getId());

        List<String> executorIds;
        for (ProcUnitHandler mendProcUnitHandler : mendProcUnitHandlers) {

            executorIds = new ArrayList<String>(1);
            executorIds.add(mendProcUnitHandler.getHandlerId());

            CoordinationTask coordinationTask = new CoordinationTask();
            coordinationTask.setTaskKind(TaskKind.MEND);
            coordinationTask.setProcUnitHandlerId(mendProcUnitHandler.getId());
            coordinationTask.setProcUnitId(procUnitId);
            coordinationTask.setProcUnitName(lastHiTaskInst.getName());
            coordinationTask.setDescription(lastHiTaskInst.getDescription());
            coordinationTask.setFormKey(lastHiTaskInst.getFormKey());
            coordinationTask.setBusinessKey(bizId);
            coordinationTask.setBusinessCode(mendProcUnitHandler.getBizCode());
            coordinationTask.setAdditiveData(additiveData);
            coordinationTask.setExecutorIds(executorIds);

            this.doCreateCoordinationTask(coordinationTask);

            this.procUnitHandlerApplication.updateProcUnitHandlerStatus(mendProcUnitHandler, ProcUnitHandler.Status.READY);
        }

        fireProcessEvent(ProcessEvent.AFTER_LAUNCH_MENDTASK_EVENT);
    }

    @Override
    @Transactional
    public void mend(String bizId, String procUnitId, String currentProcUnitHandlerId, Long version, List<ProcUnitHandler> countersigns) {
        this.saveCounterSignHandlers(bizId, procUnitId, version, currentProcUnitHandlerId, null, countersigns);
        this.launchMendTask(bizId, procUnitId);
    }

    @Override
    @Transactional
    public void replenish(String backToTaskId, String currentTaskId, String procUnitHandlerId) {
        this.checkTaskWaitingStatus(currentTaskId);
        ReplenishCmd cmd = new ReplenishCmd(backToTaskId, currentTaskId, procUnitHandlerId, this.actApplication, this.procUnitHandlerApplication,
                                            this.messageSenderManager);
        this.managementService.executeCommand(cmd);
        fireProcessEvent(ProcessEvent.AFTER_REPLENISH_EVENT);
    }

    @Override
    @Transactional
    public String saveComment(String procUnitHandlerId, String bizId, String message) {
        Authentication.setAuthenticatedUserId(getOperator().getName());
        AddCommentCmd cmd = new AddCommentCmd(procUnitHandlerId, bizId, message);
        Comment comment = this.managementService.executeCommand(cmd);
        return comment.getId();
    }

    @Override
    public List<Comment> queryProcUnitHandlerComments(String procUnitHandlerId) {
        return this.taskService.getTaskComments(procUnitHandlerId);
    }

    @Override
    public List<ActivityImpl> queryProcessInstanceActiveActivities(String procInstId) {
        FindProcessInstanceActiveActivitiesCmd cmd = new FindProcessInstanceActiveActivitiesCmd(procInstId);
        List<ActivityImpl> result = this.managementService.executeCommand(cmd);
        return result;
    }

    @Override
    public void handTasks(String fromPersonId, String toPsmId) {
        Assert.hasText(fromPersonId, "交接人员ID不能为空。");
        Assert.hasText(toPsmId, "接收人员成员ID不能为空。");

        // 交接人
        Org fromPsm = this.orgApplication.loadMainOrgByPersonId(fromPersonId);
        Util.check(fromPsm != null, String.format("没有查找到人员Id“%s”对应的交接人。", fromPersonId));

        // 1、接收人表
        Org toPsm = this.orgApplication.loadOrg(toPsmId);
        Util.check(toPsm != null, String.format("没有查找到人员成员Id“%s”对应的接收人。", toPsmId));

        String likeFrompersonId = String.format("%s@%%", fromPersonId);

        StringBuilder sb = new StringBuilder();

        sb.append("update wf_procunithandler t");
        sb.append("   set handler_id = ?, handler_name = ?, position_id = ?, position_name = ?,");
        sb.append("       dept_id = ?, dept_name = ?, org_id = ?, org_name = ?,full_id = ?, full_name = ?");
        sb.append(" where t.handler_id like ?");
        sb.append("   and t.status = 0");

        this.sqlExecutorDao.executeUpdate(sb.toString(), toPsm.getId(), toPsm.getName(), toPsm.getPositionId(), toPsm.getPositionName(), toPsm.getDeptId(),
                                          toPsm.getDeptName(), toPsm.getOrgId(), toPsm.getOrgName(), toPsm.getFullId(), toPsm.getFullName(), likeFrompersonId);

        String taskDescriptionPrefix = String.format("%s移交给您的任务：", fromPsm.getName());
        // 2、历史任务
        sb.delete(0, sb.length());

        sb.append("update act_hi_taskinst_extension");
        sb.append("   set executor_person_member_id_ = ?, executor_person_member_name_ = ?,");
        sb.append("       executor_pos_id_ = ?, executor_pos_name_ = ?, executor_dept_id_ = ?,");
        sb.append("       executor_dept_name_ = ?, executor_ogn_id_ = ?, executor_ogn_name_ = ?,");
        sb.append("       executor_full_id_ = ?, executor_full_name_ = ?, description_ = concat(?,description_), start_time_ = sysdate");
        sb.append(" where id_ in (select te.id_");
        sb.append("                 from act_ru_task_extension te");
        sb.append("                where te.executor_person_member_id_ like ?)");

        this.sqlExecutorDao.executeUpdate(sb.toString(), toPsm.getId(), toPsm.getName(), toPsm.getPositionId(), toPsm.getPositionName(), toPsm.getDeptId(),
                                          toPsm.getDeptName(), toPsm.getOrgId(), toPsm.getOrgName(), toPsm.getFullId(), toPsm.getFullName(),
                                          taskDescriptionPrefix, likeFrompersonId);

        sb.delete(0, sb.length());
        sb.append("update act_hi_taskinst ");
        sb.append("   set description_ = concat(?,description_), start_time_ = sysdate");
        sb.append(" where id_ in (select te.id_");
        sb.append("                 from act_ru_task_extension te");
        sb.append("                where te.executor_person_member_id_ like ?)");
        this.sqlExecutorDao.executeUpdate(sb.toString(), taskDescriptionPrefix, likeFrompersonId);

        // 3、运行时任务
        sb.delete(0, sb.length());

        sb.append("update act_ru_task ");
        sb.append("   set assignee_ = ?, description_ = concat(?,description_)");
        sb.append(" where id_ in (select te.id_");
        sb.append("                 from act_ru_task_extension te");
        sb.append("                where te.executor_person_member_id_ like ?)");

        this.sqlExecutorDao.executeUpdate(sb.toString(), toPsm.getFullId(), taskDescriptionPrefix, likeFrompersonId);

        sb.delete(0, sb.length());

        sb.append("update act_ru_task_extension");
        sb.append("   set executor_person_member_id_ = ?, executor_person_member_name_ = ?,");
        sb.append("       executor_pos_id_ = ?, executor_pos_name_ = ?, executor_dept_id_ = ?,");
        sb.append("       executor_dept_name_ = ?, executor_ogn_id_ = ?, executor_ogn_name_ = ?,");
        sb.append("       executor_full_id_ = ?, executor_full_name_ = ?, description_ = concat(?,description_), start_time_ = sysdat");
        sb.append(" where executor_person_member_id_ like ?");

        this.sqlExecutorDao.executeUpdate(sb.toString(), toPsm.getId(), toPsm.getName(), toPsm.getPositionId(), toPsm.getPositionName(), toPsm.getDeptId(),
                                          toPsm.getDeptName(), toPsm.getOrgId(), toPsm.getOrgName(), toPsm.getFullId(), toPsm.getFullName(),
                                          taskDescriptionPrefix, likeFrompersonId);

        fireProcessEvent(ProcessEvent.AFTER_HAND_TASKS_EVENT);
    }

    @Override
    @Transactional
    public void collectTask(String taskId) {
        String personId = this.getOperator().getUserId();
        List<TaskCollection> taskCollections = taskCollectionRepository.findByPersonIdAndTaskId(personId, taskId);
        if (taskCollections.size() == 0) {
            TaskCollection taskCollection = new TaskCollection();
            taskCollection.setTaskId(taskId);
            taskCollection.setPersonId(personId);
            taskCollection.setCreatedDate(new java.util.Date());
            taskCollectionRepository.save(taskCollection);
        }
    }

    @Override
    @Transactional
    public void cancelCollectionTask(String taskId) {
        String personId = this.getOperator().getUserId();
        List<TaskCollection> taskCollections = taskCollectionRepository.findByPersonIdAndTaskId(personId, taskId);
        if (taskCollections.size() > 0) {
            taskCollectionRepository.delete(taskCollections);
        }
    }

    @Override
    public Map<String, Object> queryCurrentOperatorTaskStatistics() {
        String sql = this.getQuerySqlByName("queryTaskStatistics");
        return this.sqlExecutorDao.queryToMap(sql, this.getOperator().getUserId() + "@%");
    }

    @Override
    public String findBpmnDelegateClassCanonicalName(String taskId) {
        GetBpmnDelegateClassCanonicalNameCmd cmd = new GetBpmnDelegateClassCanonicalNameCmd(taskId);
        this.managementService.executeCommand(cmd);
        return null;
    }

    @Override
    public Boolean checkAssistantPassRule(String processDefinitionKey, String taskDefinitionKey, String bizId, String procUnitHandlerId) {
        if (StringUtil.isBlank(processDefinitionKey) || StringUtil.isBlank(taskDefinitionKey) || StringUtil.isBlank(bizId)
            || StringUtil.isBlank(procUnitHandlerId)) {
            return true;
        }

        ProcDefinition procDefinition = this.procDefinitionRespository.findProcUnit(processDefinitionKey, taskDefinitionKey);
        boolean assistantMustApprove = procDefinition != null && procDefinition.getAssistantMustApprove() != null
                                       && procDefinition.getAssistantMustApprove().equals(1);
        ProcUnitHandler procUnitHandler = procUnitHandlerApplication.loadProcUnitHandler(procUnitHandlerId);
        if (CooperationModelKind.isChief(procUnitHandler.getCooperationModelId()) && assistantMustApprove) {
            Integer count = this.procUnitHandlerApplication.countAssistantNotApproveByChiefId(bizId, procUnitHandler.getId());
            return count == 0;
            // Assert.isTrue(count == 0, "还有协审人员没有审批，不能流转。");
        }
        return true;
    }

    private void fireProcessEvent(String processEventType) {
        if (processEventSupport != null) {
            ProcessEvent processEvent = new ProcessEvent(processEventType);
            processEventSupport.fireProcessEvent(processEvent);
        }
    }

    @Override
    public Map<String, Object> loadRuntimeTaskByBizId(String bizId) {
        return this.actApplication.loadRuntimeTaskByBizId(bizId);
    }

    @Override
    public Map<String, Object> slicedQueryProcunitHandler(ProcunitHandlerQueryRequest queryRequest) {
        queryRequest.setDateRangeData();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery("config/uasp/query/bmp/bpm.xml", "procunitHandlerQuery");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.putDictionary("handleResult", DictUtil.getDictionary("handleResult"));
        return this.sqlExecutorDao.executeSlicedQuery(model);
    }
}
