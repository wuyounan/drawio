package com.huigou.uasp.bpm.engine.application.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.util.*;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.OrgUnit;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.QueryParameter;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.fn.impl.ProcessFun;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bpm.ActivityKind;
import com.huigou.uasp.bpm.HandleResult;
import com.huigou.uasp.bpm.ProcessStatus;
import com.huigou.uasp.bpm.TaskKind;
import com.huigou.uasp.bpm.TaskScope;
import com.huigou.uasp.bpm.TaskStatus;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.application.ProcUnitHandlerApplication;
import com.huigou.uasp.bpm.engine.application.WorkflowApplication;
import com.huigou.uasp.bpm.engine.domain.dto.BackTaskInfo;
import com.huigou.uasp.bpm.engine.domain.model.HistoricProcessInstanceExtension;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceExtension;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceRelation;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandler;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandlerManuscript;
import com.huigou.uasp.bpm.engine.domain.model.RuntimeTaskExtension;
import com.huigou.uasp.bpm.engine.domain.query.TaskDetail;
import com.huigou.uasp.bpm.engine.repository.HistoricProcInstanceExtensionRepository;
import com.huigou.uasp.bpm.engine.repository.HistoricTaskInstanceExtensionRepository;
import com.huigou.uasp.bpm.engine.repository.HistoricTaskInstanceRelationRepository;
import com.huigou.uasp.bpm.engine.repository.RuntimeTaskExtensionRepository;
import com.huigou.uasp.bpm.managment.application.ProcDefinitionApplication;

@Service("actApplication")
public class ActApplicationImpl extends BaseApplication implements ActApplication {

    /**
     * 工作流引擎服务
     */
    @Autowired
    private WorkflowApplication workflowService;

    @Autowired
    private ProcUnitHandlerApplication procUnitHandlerApplication;

    @Autowired
    private RuntimeTaskExtensionRepository runtimeTaskExtensionRepository;

    @Autowired
    private HistoricTaskInstanceExtensionRepository historicTaskInstExtensionRepository;

    @Autowired
    private HistoricTaskInstanceRelationRepository historicTaskInstRelationRepository;

    @Autowired
    private HistoricProcInstanceExtensionRepository historicProcInstExtensionRepository;

    @Autowired
    private ProcDefinitionApplication procDefinitionApplication;

    @Autowired
    private ProcessFun processFun;

    private String getQuerySqlByName(String name) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery("config/uasp/query/bmp/bpm.xml", "taskInstExtension");
        return queryDescriptor.getSqlByName(name);
    }

    private void fillTaskExtensionApplicantPersonMemberInfo(RuntimeTaskExtension runtimeTaskExtension) {
        // 申请人
        if (StringUtil.isNotBlank(runtimeTaskExtension.getBusinessKey())) {
            HistoricProcessInstanceExtension historicProcInstExtension = this.historicProcInstExtensionRepository.findByBusinessKey(runtimeTaskExtension.getBusinessKey());
            if (historicProcInstExtension != null) {
                runtimeTaskExtension.setApplicantPersonMemberId(historicProcInstExtension.getApplicantPersonMemberId());
                runtimeTaskExtension.setApplicantPersonMemberName(historicProcInstExtension.getApplicantPersonMemberName());
            }
        }
    }

    @Override
    @Transactional
    public void saveTaskExtension(RuntimeTaskExtension runtimeTaskExtension) {
        Assert.notNull(runtimeTaskExtension, "参数runtimeTaskExtension不能为空。");
        // 申请人
        fillTaskExtensionApplicantPersonMemberInfo(runtimeTaskExtension);
        // 创建人
        Operator operator = getOperator();
        runtimeTaskExtension.assignCreatorInfo(operator);
        // 执行人
        runtimeTaskExtension.assignExecutor();

        this.internalSaveTaskExtension(runtimeTaskExtension);
    }

    @Override
    @Transactional
    public void saveTaskExtension(RuntimeTaskExtension runtimeTaskExtension, String initiatorPersonMemberId) {
        Assert.notNull(runtimeTaskExtension, "参数runtimeTaskExtension不能为空。");
        Assert.notNull(initiatorPersonMemberId, "参数initiatorPersonMemberId不能为空。");
        // 申请人
        fillTaskExtensionApplicantPersonMemberInfo(runtimeTaskExtension);

        Org initiatorPersonMember = this.workflowService.getOrgApplication().loadEabledOrg(initiatorPersonMemberId);
        runtimeTaskExtension.assignCreatorInfo(initiatorPersonMember);

        runtimeTaskExtension.assignExecutor();

        this.internalSaveTaskExtension(runtimeTaskExtension);
    }

    @Override
    @Transactional
    public void saveTaskExtension(RuntimeTaskExtension runtimeTaskExtension, ProcUnitHandler procUnitHandler) {
        Assert.notNull(runtimeTaskExtension, "参数runtimeTaskExtension不能为空。");

        Operator operator = getOperator();
        Assert.notNull(operator, "环境参数operator不能为空。");

        fillTaskExtensionApplicantPersonMemberInfo(runtimeTaskExtension);

        // 创建人
        if (operator.getLoginUser() == null || operator.getPersonMemberFullIds().size() == 1 || procUnitHandler == null) {
            runtimeTaskExtension.assignCreatorInfo(operator);
        } else {
            runtimeTaskExtension.assignCreatorInfo(procUnitHandler);
        }
        // 执行人、拥有人
        runtimeTaskExtension.assignExecutor(runtimeTaskExtension.getAgent());

        this.internalSaveTaskExtension(runtimeTaskExtension);
    }

    private void internalSaveTaskExtension(RuntimeTaskExtension runtimeTaskExtension) {
        runtimeTaskExtension.setStatusId(TaskStatus.READY.getId());
        runtimeTaskExtension.setStatusName(TaskStatus.READY.getDisplayName());

        HistoricTaskInstanceExtension historicTaskInstExtension = new HistoricTaskInstanceExtension(runtimeTaskExtension);
        // 增加版本号保存
        QueryDescriptor queryDescriptor = sqlExecutorDao.getQuery("config/uasp/query/bmp/common.xml", "common");
        long version = sqlExecutorDao.getSqlQuery().getJDBCDao().queryToLong(String.format(queryDescriptor.getSqlByName("nextVersion")));
        runtimeTaskExtension.setVersion(version);
        runtimeTaskExtensionRepository.save(runtimeTaskExtension);
        historicTaskInstExtension.setVersion(version);
        historicTaskInstExtensionRepository.save(historicTaskInstExtension);
    }

    private void internalUpdateRuntimeTaskExtensionStatus(String taskId, TaskStatus taskStatus) {
        RuntimeTaskExtension runtimeTaskExtension = this.runtimeTaskExtensionRepository.findOne(taskId);
        Assert.notNull(runtimeTaskExtension, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "运行时任务扩展"));

        runtimeTaskExtension.setStatusId(taskStatus.getId());
        runtimeTaskExtension.setStatusName(taskStatus.getDisplayName());
        if (taskStatus == TaskStatus.SLEEPING) {
            Date currentDate = CommonUtil.getCurrentDateTime();
            runtimeTaskExtension.setSleepedDate(currentDate);
        }
        this.runtimeTaskExtensionRepository.save(runtimeTaskExtension);
    }

    private void internalUpdateHistoricTaskInstExtensionStatus(String taskId, TaskStatus taskStatus) {
        HistoricTaskInstanceExtension historicTaskInstExtension = this.historicTaskInstExtensionRepository.findOne(taskId);
        Assert.notNull(historicTaskInstExtension, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "历史任务扩展"));

        historicTaskInstExtension.setStatusId(taskStatus.getId());
        historicTaskInstExtension.setStatusName(taskStatus.getDisplayName());

        if (taskStatus == TaskStatus.SLEEPING) {
            Date currentDate = CommonUtil.getCurrentDateTime();
            historicTaskInstExtension.setSleepedDate(currentDate);
        }
        this.historicTaskInstExtensionRepository.save(historicTaskInstExtension);
    }

    private void internalUpdateTaskExtensionStatus(String taskId, TaskStatus taskStatus) {
        internalUpdateRuntimeTaskExtensionStatus(taskId, taskStatus);
        internalUpdateHistoricTaskInstExtensionStatus(taskId, taskStatus);
    }

    @Override
    @Transactional
    public void updateTaskExtensionStatus(String taskId, TaskStatus taskStatus) {
        Assert.hasText(taskId, "参数taskId不能为空。");
        Assert.notNull(taskStatus, "参数taskStatus不能为空。");

        internalUpdateTaskExtensionStatus(taskId, taskStatus);
    }

    @Override
    @Transactional
    public void updateTaskExtensionSleepingStatus(String taskId) {
        Assert.hasText(taskId, "参数taskId不能为空。");
        internalUpdateTaskExtensionStatus(taskId, TaskStatus.SLEEPING);
    }

    @Override
    @Transactional
    public void updateTaskExtensionNeedTiming(String taskId, Integer needTiming) {
        checkTaskIdNotNull(taskId);

        RuntimeTaskExtension runtimeTaskExtension = this.runtimeTaskExtensionRepository.findOne(taskId);

        HistoricTaskInstanceExtension historicTaskInstExtension = this.historicTaskInstExtensionRepository.findOne(taskId);
        Assert.notNull(historicTaskInstExtension, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "历史时任务扩展"));

        if (runtimeTaskExtension != null) {
            runtimeTaskExtension.setNeedTiming(needTiming);
            this.runtimeTaskExtensionRepository.save(runtimeTaskExtension);
        }

        historicTaskInstExtension.setNeedTiming(needTiming);
        this.historicTaskInstExtensionRepository.save(historicTaskInstExtension);
    }

    @Override
    @Transactional
    public void updateTaskExtensionHandleResult(String taskId, HandleResult result, String opinion) {
        checkTaskIdNotNull(taskId);
        Assert.notNull(result, "参数result不能为空。");

        RuntimeTaskExtension runtimeTaskExtension = this.runtimeTaskExtensionRepository.findOne(taskId);
        Assert.notNull(runtimeTaskExtension, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "运行时任务扩展"));

        HistoricTaskInstanceExtension historicTaskInstExtension = this.historicTaskInstExtensionRepository.findOne(taskId);
        Assert.notNull(historicTaskInstExtension, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "历史时任务扩展"));

        runtimeTaskExtension.setResult(result.getId());
        runtimeTaskExtension.setOpinion(opinion);

        historicTaskInstExtension.setResult(result.getId());
        historicTaskInstExtension.setOpinion(opinion);

        this.runtimeTaskExtensionRepository.save(runtimeTaskExtension);
        this.historicTaskInstExtensionRepository.save(historicTaskInstExtension);
    }

    @Override
    @Transactional
    public void updateHistoricTaskExtensionHandleResult(String taskId, HandleResult result, String opinion) {
        checkTaskIdNotNull(taskId);
        Assert.notNull(result, "参数result不能为空。");
        HistoricTaskInstanceExtension historicTaskInstExtension = this.historicTaskInstExtensionRepository.findOne(taskId);
        Assert.notNull(historicTaskInstExtension, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "历史时任务扩展"));
        historicTaskInstExtension.setResult(result.getId());
        historicTaskInstExtension.setOpinion(opinion);
        this.historicTaskInstExtensionRepository.save(historicTaskInstExtension);
    }

    @Override
    @Transactional
    public void updateTaskHanlder(String taskId, OrgUnit orgUnit, boolean updateStartTime) {
        Assert.hasText(taskId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "taskId"));
        Assert.notNull(orgUnit, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "orgUnit"));

        HistoricTaskInstanceExtension historicTaskInstanceExtension = this.historicTaskInstExtensionRepository.findOne(taskId);
        Assert.state(historicTaskInstanceExtension != null, String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "任务"));

        Task task = this.workflowService.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        boolean isExecutingTask = task != null;
        if (isExecutingTask) {
            task.setAssignee(orgUnit.getFullId());
            this.workflowService.getTaskService().saveTask(task);

            RuntimeTaskExtension runtimeTaskExtension = this.runtimeTaskExtensionRepository.findOne(taskId);
            Assert.state(runtimeTaskExtension != null, String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "任务扩展"));
            runtimeTaskExtension.buildExecutorInfo(orgUnit);
            this.runtimeTaskExtensionRepository.save(runtimeTaskExtension);
        }

        String sql = this.getQuerySqlByName("updateTaskInstAssignee");
        this.sqlExecutorDao.executeUpdate(sql, orgUnit.getFullId(), taskId);

        sql = this.getQuerySqlByName("updateActInstAssignee");
        this.sqlExecutorDao.executeUpdate(sql, orgUnit.getFullId(), taskId);

        historicTaskInstanceExtension.buildExecutorInfo(orgUnit);
        this.historicTaskInstExtensionRepository.save(historicTaskInstanceExtension);

        if (updateStartTime) {
            Date startTime = Context.getProcessEngineConfiguration().getClock().getCurrentTime();
            if (isExecutingTask) {
                sql = this.getQuerySqlByName("updateRuTaskInstStartTime");
                this.sqlExecutorDao.executeUpdate(sql, startTime, taskId);
                sql = this.getQuerySqlByName("updateRuTaskInstExtensionStartTime");
                this.sqlExecutorDao.executeUpdate(sql, startTime, taskId);
            }

            sql = this.getQuerySqlByName("updateHiTaskInstStartTime");
            this.sqlExecutorDao.executeUpdate(sql, startTime, taskId);
            sql = this.getQuerySqlByName("updateHiTaskInstExtensionStartTime");
            this.sqlExecutorDao.executeUpdate(sql, startTime, taskId);
        }
    }

    /**
     * 检查任务ID不能为空
     *
     * @param taskId 任务ID
     */
    private void checkTaskIdNotNull(String taskId) {
        Util.check(!StringUtil.isBlank(taskId), "任务ID不能为空,请刷新页面后重试。");
    }

    @Override
    @Transactional
    public void updateHistoricTaskInstanceExtensionStatus(String taskId, TaskStatus taskStatus) {
        checkTaskIdNotNull(taskId);
        Assert.notNull(taskStatus, "参数taskStatus不能为空。");
        this.internalUpdateHistoricTaskInstExtensionStatus(taskId, taskStatus);
    }

    @Override
    public void updateHistoricTaskInstanceExtensionEnded(String taskId, TaskStatus taskStatus, String deleteReason) {
        checkTaskIdNotNull(taskId);
        Assert.notNull(taskStatus, "参数taskStatus不能为空。");

        HistoricTaskInstanceExtension historicTaskInstanceExtension = this.historicTaskInstExtensionRepository.findOne(taskId);
        Assert.notNull(historicTaskInstanceExtension, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, taskId, "历史任务扩展"));

        historicTaskInstanceExtension.setStatusId(taskStatus.getId());
        historicTaskInstanceExtension.setStatusName(taskStatus.getDisplayName());

        historicTaskInstanceExtension.markEnded(deleteReason);
        this.historicTaskInstExtensionRepository.save(historicTaskInstanceExtension);
    }

    @Override
    public RuntimeTaskExtension loadRuntimeTaskExtension(String id) {
        checkTaskIdNotNull(id);
        return this.runtimeTaskExtensionRepository.findOne(id);
    }

    @Override
    public HistoricTaskInstanceExtension loadHistoricTaskInstanceExtension(String id) {
        checkTaskIdNotNull(id);
        return this.historicTaskInstExtensionRepository.findOne(id);
    }

    @Override
    public List<HistoricTaskInstanceExtension> queryHistoricTaskInstanceExtensions(List<String> ids) {
        this.checkIdsNotEmpty(ids);
        return this.historicTaskInstExtensionRepository.findAll(ids);
    }

    @Override
    public List<RuntimeTaskExtension> queryCoordinationTaskInstances(String bizId) {
        Assert.hasText(bizId, "参数bizId不能为空。");

        List<String> kindIds = new ArrayList<String>(2);

        kindIds.add(TaskKind.TASK);
        kindIds.add(TaskKind.REPLENISH);

        return this.runtimeTaskExtensionRepository.findCoordinationTaskInstances(bizId, TaskScope.TASK, kindIds);
    }

    @Override
    public List<HistoricTaskInstanceExtension> queryHistoricTaskInstanceByBizAndProcUnitId(String bizId, String procUnitId) {
        Assert.hasText(bizId, "参数bizId不能为空。");
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "procUnitId"));
        return this.historicTaskInstExtensionRepository.findByBusinessKeyAndTaskDefinitionKey(bizId, procUnitId);
    }

    @Override
    @Transactional
    public void deleteRuntimeTaskExtension(String taskId) {
        this.checkTaskIdNotNull(taskId);
        this.runtimeTaskExtensionRepository.delete(taskId);
    }

    @Override
    public Map<String, Object> queryApprovalHistoryByBizId(String bizId) {
        Assert.hasText(bizId, "参数bizId不能为空。");

        QueryModel queryModel = new QueryModel();
        queryModel.setSql(this.getQuerySqlByName("queryProcUnitHandlersByBizId"));
        queryModel.putParam("businessKey", bizId);
        Map<String, Object> result = this.sqlExecutorDao.executeQuery(queryModel);
        boolean supportManuscript = Boolean.valueOf(SystemCache.getParameter("wf.approval.supportManuscript", String.class));
        List<ProcUnitHandlerManuscript> procUnitHandlerManuscripts = null;
        if (supportManuscript) {
            procUnitHandlerManuscripts = this.procUnitHandlerApplication.queryProcUnitHandlerManuscriptsByBizId(bizId);
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> procUnitHandlers = (List<Map<String, Object>>) result.get(Constants.ROWS);

        String procUnitHandlerId, handler;
        Integer groupId;
        for (Map<String, Object> map : procUnitHandlers) {
            procUnitHandlerId = ClassHelper.convert(map.get("procUnitHandlerId"), String.class);
            groupId = ClassHelper.convert(map.get("groupId"), Integer.class, -1);
            handler = String.format("%s.%s", map.get("executorDeptName"), map.get("executorPersonMemberName"));
            map.put("handler", handler);
            map.put("groupId", groupId);
            // 手写
            if (supportManuscript) {
                for (ProcUnitHandlerManuscript procUnitHandlerManuscript : procUnitHandlerManuscripts) {
                    if (procUnitHandlerManuscript.getProcUnitHandlerId().equalsIgnoreCase(procUnitHandlerId)) {
                        map.put("manuscript", procUnitHandlerManuscript.getOpinion64());
                        break;
                    }
                }
            }
        }

        return result;
    }

    @Override
    public Map<String, Object> queryApprovalHistoryByProcessInstanceId(String processInstanceId) {
        Assert.hasText(processInstanceId, "参数processInstanceId不能为空");
        QueryModel queryModel = new QueryModel();
        queryModel.setSql(this.getQuerySqlByName("queryApprovalHistoryByProcInstId"));
        queryModel.putParam("procInstId", processInstanceId);
        return this.sqlExecutorDao.executeQuery(queryModel);
    }

    @Override
    public Map<String, Object> queryBackTasksByBizCode(String bizCode, String procUnitId, Integer groupId) {
        Assert.hasText(bizCode, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "bizCode"));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "procUnitId"));
        Assert.notNull(groupId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "groupId"));

        // 申请环节不能回退
        Assert.state(!ActivityKind.isApplyActivity(procUnitId), "申请环节不能回退。");

        String sql = this.getQuerySqlByName("queryBackTasksByBizCode");
        List<BackTaskInfo> backTasks = this.sqlExecutorDao.queryToList(sql, BackTaskInfo.class, bizCode, bizCode);
        if (backTasks.size() == 0) {
            Map<String, Object> result = new HashMap<String, Object>(1);
            result.put(Constants.ROWS, backTasks);
            return result;
        }

        Integer currentProcUnitProcessDefinitionGroupId = Integer.MAX_VALUE;
        for (BackTaskInfo backTaskInfo : backTasks) {
            if (backTaskInfo.getTaskDefKey().equals(procUnitId)) {
                currentProcUnitProcessDefinitionGroupId = backTaskInfo.getProcessDefinitionGroupId();
                break;
            }
        }
        // 移去当前组及后面的任务, 审批环节1(N11,N12,..,N1n),审批环节2(N21,N22,..,N2n),...,审批环节2(Nn1,Nn2,..,Nnn)
        // 当前环节i,审批分组号j, 删除审批环节的任务p(sp>i,p为流程环节序号), 删除当前环节i的任务（g>j,g为分组号)
        Iterator<BackTaskInfo> iterator;
        BackTaskInfo currentItem, nextItem;
        if (groupId > 0) {
            iterator = backTasks.iterator();
            while (iterator.hasNext()) {
                currentItem = iterator.next();
                // 删除流程回退删除的处理人
                if (StringUtil.isNotBlank(currentItem.getProcUnitHandlerId()) && currentItem.getGroupId().equals(0)) {
                    iterator.remove();
                } else if (currentItem.getProcessDefinitionGroupId() > currentProcUnitProcessDefinitionGroupId
                        || (currentItem.getProcessDefinitionGroupId().equals(currentProcUnitProcessDefinitionGroupId) && currentItem.getGroupId() >= groupId)) {
                    iterator.remove();
                }
            }
        }

        // 删除同一环节、同一分组，相同处理人
        List<BackTaskInfo> removedBackTaskInfos = new ArrayList<BackTaskInfo>();
        for (int i = 0; i < backTasks.size(); i++) {
            currentItem = backTasks.get(i);
            for (int j = i + 1; j < backTasks.size(); j++) {
                nextItem = backTasks.get(j);
                if (currentItem.getTaskDefKey().equals(nextItem.getTaskDefKey()) && currentItem.getGroupId().equals(nextItem.getGroupId())
                        && currentItem.getExecutorFullName().equals(nextItem.getExecutorFullName())) {
                    removedBackTaskInfos.add(nextItem);
                }
            }
        }
        if (removedBackTaskInfos.size() > 0) {
            backTasks.removeAll(removedBackTaskInfos);
        }

        Map<String, Object> result = new HashMap<String, Object>(1);
        result.put(Constants.ROWS, backTasks);
        return result;
    }

    @Override
    public Map<String, Object> queryApplicantByProcessInstanceId(String processInstanceId) {
        String sql = this.getQuerySqlByName("queryApplicantByProcInstId");
        return this.sqlExecutorDao.queryOneToMap(sql, processInstanceId);
    }

    @Override
    public Map<String, Object> queryApplicantByBizId(String bizId) {
        String sql = this.getQuerySqlByName("queryApplicantByBizId");
        return this.sqlExecutorDao.queryOneToMap(sql, bizId);
    }

    @Override
    public HistoricTaskInstanceExtension queryApplicantTask(String bizId) {
        Assert.hasText(bizId, "参数bizId不能为空。");
        return this.historicTaskInstExtensionRepository.findApplicantTask(bizId);
    }

    @Override
    public HistoricTaskInstanceExtension queryApplicantTaskByBizCode(String bizCode) {
        Assert.hasText(bizCode, "参数bizCode不能为空。");
        return this.historicTaskInstExtensionRepository.findApplicantTaskByBizCode(bizCode);
    }

    @Override
    public TaskDetail queryTaskDetail(String taskId) {
        Assert.hasText(taskId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "taskId"));
        String sql = this.getQuerySqlByName("queryTaskDetail");
        return this.sqlExecutorDao.queryToObject(sql, TaskDetail.class, taskId);
    }

    @Override
    public Map<String, Object> loadRuntimeTaskById(String taskId) {
        String sql = this.getQuerySqlByName("loadRuntimeTaskById");
        return this.sqlExecutorDao.queryToMap(sql, taskId);
    }

    @Override
    public Map<String, Object> loadRuntimeTaskByBizId(String bizId) {
        String sql = this.getQuerySqlByName("loadRuntimeTaskByBizId");
        return this.sqlExecutorDao.queryOneToMap(sql, bizId);
    }

    @Override
    public Map<String, Object> loadHistoricTaskById(String taskId) {
        String sql = this.getQuerySqlByName("loadHistoricTaskById");
        return this.sqlExecutorDao.queryToMap(sql, taskId);
    }

    @Override
    public Map<String, Object> queryHiTaskInstsByBizCode(String bizCode) {
        Assert.hasText(bizCode, "参数bizCode不能为空。");
        QueryModel queryModel = new QueryModel();
        queryModel.setSql(this.getQuerySqlByName("queryHiTaskInstByBizCode"));
        queryModel.putParam("bizCode", bizCode);
        return this.sqlExecutorDao.executeQuery(queryModel);
    }

    @Override
    public Map<String, Object> queryHiTaskInstsByBizCodeAndKindId(String bizCode, String kindId) {
        Assert.hasText(bizCode, "参数bizCode不能为空。");
        Assert.hasText(bizCode, "参数kindId不能为空。");

        QueryModel queryModel = new QueryModel();
        queryModel.setSql(this.getQuerySqlByName("queryHiTaskInstByBizCodeAndKindId"));
        queryModel.putParam("bizCode", bizCode);
        queryModel.putParam("kindId", kindId);

        return this.sqlExecutorDao.executeQuery(queryModel);
    }

    @Override
    @Transactional
    public void saveHiTaskinstRelations(String bizId, List<HistoricTaskInstanceRelation> historicTaskInstRelations) {
        Assert.hasText(bizId, "参数bizId不能为空。");
        this.historicTaskInstRelationRepository.deleteByBusinessKey(bizId);
        if (historicTaskInstRelations.size() > 0) {
            this.historicTaskInstRelationRepository.save(historicTaskInstRelations);
        }
    }

    @Override
    @Transactional
    public void deleteHiTaskinstRelations(List<String> ids) {
        this.checkIdsNotEmpty(ids);
        List<HistoricTaskInstanceRelation> historicTaskInstRelations = this.historicTaskInstRelationRepository.findAll(ids);

        Assert.isTrue(ids.size() == historicTaskInstRelations.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "任务关联"));

        this.historicTaskInstRelationRepository.delete(historicTaskInstRelations);
    }

    @Override
    public Map<String, Object> queryHiTaskinstRelations(String taskId, String bizId) {
        Assert.isTrue(StringUtil.isNotBlank(taskId) || !StringUtil.isBlank(bizId), "参数taskId或bizId不能为空。");

        QueryModel queryModel = new QueryModel();
        queryModel.setSql(this.getQuerySqlByName("taskInstRelationQuery"));
        if (StringUtil.isNotBlank(taskId)) {
            queryModel.addCriteria("  and r.task_id = :taskId ");
            queryModel.putParam("taskId", taskId);
        }

        if (StringUtil.isNotBlank(bizId)) {
            queryModel.addCriteria(" and r.business_key = :businessKey ");
            queryModel.putParam("businessKey", bizId);
        }
        return this.sqlExecutorDao.executeQuery(queryModel);
    }

    @Override
    public HistoricProcessInstanceExtension loadHistoricProcessInstanceExtension(String id) {
        Assert.hasText(id, "参数id不能为空。");
        return this.historicProcInstExtensionRepository.findOne(id);
    }

    @Override
    public HistoricProcessInstanceExtension loadHistoricProcessInstanceExtensionByProcInstId(String processInstanceId) {
        Assert.hasText(processInstanceId, "参数processInstanceId不能为空。");
        return this.historicProcInstExtensionRepository.findByProcessInstanceId(processInstanceId);
    }

    @Override
    @Transactional
    public void saveHistoricProcessInstanceExtension(HistoricProcessInstanceEntity historicProcessInstanceEntity, String initiatorPersonMemberId,
                                                     String description, ProcessStatus processStatus) {
        Assert.notNull(historicProcessInstanceEntity, "参数historicProcessInstanceEntity不能为空。");
        // 流程公用流程模板，逆向操作可能引起其他流程走向错误。
        long count = this.historicProcInstExtensionRepository.countByBusinessKey(historicProcessInstanceEntity.getBusinessKey());
        if (count > 0) {
            return;
        }

        HistoricProcessInstanceExtension historicProcInstExtension = new HistoricProcessInstanceExtension();

        historicProcInstExtension.setId(historicProcessInstanceEntity.getId());
        String processDefinitionId = historicProcessInstanceEntity.getProcessDefinitionId();
        historicProcInstExtension.setProcessDefinitionId(processDefinitionId);
        historicProcInstExtension.setProcessDefinitionKey(processFun.getProcessKeyFromProcessDefinitionId(processDefinitionId));

        historicProcInstExtension.setProcessInstanceId(historicProcessInstanceEntity.getProcessInstanceId());
        historicProcInstExtension.setBusinessKey(historicProcessInstanceEntity.getBusinessKey());
        historicProcInstExtension.setDescription(description);
        historicProcInstExtension.setStatusId(processStatus.getId());
        historicProcInstExtension.setStatusName(processStatus.getDisplayName());
        historicProcInstExtension.setStartTime(historicProcessInstanceEntity.getStartTime());
        historicProcInstExtension.setStartUserId(historicProcessInstanceEntity.getStartUserId());
        historicProcInstExtension.setStartActivityId(historicProcessInstanceEntity.getStartActivityId());

        historicProcInstExtension.setSuperProcessInstanceId(historicProcessInstanceEntity.getSuperProcessInstanceId());
        historicProcInstExtension.setTenantId(historicProcessInstanceEntity.getTenantId());
        historicProcInstExtension.setName(historicProcessInstanceEntity.getName());

        if (StringUtil.isNotBlank(initiatorPersonMemberId)) {
            Org personMember = this.workflowService.getOrgApplication().loadOrg(initiatorPersonMemberId);
            historicProcInstExtension.assignApplicant(personMember);
        } else {
            Operator operator = this.getOperator();
            historicProcInstExtension.assignApplicant(operator);
        }

        this.historicProcInstExtensionRepository.save(historicProcInstExtension);
    }

    @Override
    @Transactional
    public void updateHistoricProcessInstanceExtensionStatus(String processInstanceId, ProcessStatus processStatus) {
        Assert.hasText(processInstanceId, "参数processInstanceId不能为空。");
        HistoricProcessInstanceExtension historicProcessInstanceExtension = this.historicProcInstExtensionRepository.findByProcessInstanceId(processInstanceId);
        Assert.notNull(historicProcessInstanceExtension, String.format("未找到流程实例ID“%s”对应的流程实例扩展数据。", processInstanceId));
        historicProcessInstanceExtension.updateStatus(processStatus);
        this.historicProcInstExtensionRepository.save(historicProcessInstanceExtension);
    }

    @Override
    @Transactional
    public void updateHistoricProcessInstanceExtensionEnded(String processInstanceId, ProcessStatus processStatus, String deleteReason, String endActivityId) {
        Assert.hasText(processInstanceId, "参数processInstanceId不能为空。");
        Assert.notNull(processStatus, "参数processStatus不能为空。");

        HistoricProcessInstanceExtension historicProcessInstanceExtension = this.historicProcInstExtensionRepository.findByProcessInstanceId(processInstanceId);
        Assert.notNull(historicProcessInstanceExtension, String.format("未找到流程实例ID“%s”对应的流程实例扩展数据。", processInstanceId));
        historicProcessInstanceExtension.updateStatus(processStatus);
        historicProcessInstanceExtension.markEnded(deleteReason);
        historicProcessInstanceExtension.setEndActivityId(endActivityId);
        this.historicProcInstExtensionRepository.save(historicProcessInstanceExtension);
    }

    @Override
    public HistoricTaskInstanceExtension queryRecentHiTaskInstExtensionByProcUnitHandlerId(String procUnitHandlerId) {
        Assert.hasText(procUnitHandlerId, "参数procUnitHandlerId不能为空。");

        return this.historicTaskInstExtensionRepository.findFirstByProcUnitHandlerIdOrderByVersionDesc(procUnitHandlerId);
    }

    @Override
    public List<HistoricTaskInstanceExtension> queryHiTaskInstExtensionByProcUnitHandlerId(String procUnitHandlerId) {
        Assert.hasText(procUnitHandlerId, "参数procUnitHandlerId不能为空。");

        return this.historicTaskInstExtensionRepository.findByProcUnitHandlerIdOrderByVersion(procUnitHandlerId);
    }

    @Override
    public Map<String, Object> queryOperatorTrackingTasks() {
        QueryModel queryModel = new QueryModel();
        queryModel.setSql(this.getQuerySqlByName("queryTrackingTasks"));
        queryModel.setPageIndex(1);
        queryModel.setPageSize(10);
        queryModel.setSortFieldName("startTime");
        queryModel.setSortOrder("desc");
        queryModel.putParam("personId", getOperator().getUserId() + "@%");
        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> queryHiTaskIdsByProcessInstanceAndPreviousId(String processInstanceId, String previousTaskId) {
        String sql = this.getQuerySqlByName("queryHiTaskIdsByProcInstAndPreviousId");
        Map<String, Object> params = QueryParameter.buildParameters("procInstId", processInstanceId, "previousId", previousTaskId);
        return this.generalRepository.query(sql, params);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<HistoricTaskInstanceExtension> queryNotCompleteHiTaskInstExtensionsByProcInstId(String processInstanceId) {
        String sql = this.getQuerySqlByName("queryNotCompleteHiTaskInstExtensionsByProcInstId");
        Map<String, Object> params = QueryParameter.buildParameters("procInstId", processInstanceId);
        return this.generalRepository.query(sql, params);
    }

    @SuppressWarnings("unchecked")
    @Override
    public java.util.List<String> queryRuTaskIdsByProcessInstanceAndPreviousId(String processInstanceId, String previousTaskId) {
        String sql = this.getQuerySqlByName("queryRuTaskIdsByProcInstAndPreviousId");
        Map<String, Object> params = QueryParameter.buildParameters("procInstId", processInstanceId, "previousId", previousTaskId);
        return this.generalRepository.query(sql, params);
    }

    @Override
    public List<String> queryRuAssistantTaskIds(String bizId, String chiefId) {
        String sql = this.getQuerySqlByName("queryAssistantTaskIds");
        return this.sqlExecutorDao.queryToList(sql, String.class, bizId, chiefId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public java.util.List<String> queryRuTaskIdsByProcessInstanceId(String processInstanceId) {
        String sql = this.getQuerySqlByName("queryRuTaskIdsByProcInstId");
        Map<String, Object> params = QueryParameter.buildParameters("procInstId", processInstanceId);
        return this.generalRepository.query(sql, params);
    }

    @Override
    public long countReadyProcTasksByBizCode(String bizCode) {
        Assert.hasText(bizCode, MessageSourceContext.getMessage(MessageConstants.BIZ_CODE_NOT_BLANK));
        return this.runtimeTaskExtensionRepository.countReadyProcTasksByBusinessCode(bizCode);
    }

    @Override
    public Integer countWaitedAssistantTask(String bizId, String chiefId) {
        String sql = this.getQuerySqlByName("countWaitedAssistantTask");
        return this.sqlExecutorDao.queryToInt(sql, bizId, chiefId);
    }

    @Override
    public boolean existsReplenishTask(String bizId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        long count = this.runtimeTaskExtensionRepository.countReplenishTasksByBusinessKey(bizId);
        return count > 0;
    }

    @Override
    public boolean checkVisitTaskByBizIdAndPersonId(String bizId, String personId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_CODE_NOT_BLANK));
        String sql = this.getQuerySqlByName("countTaskinstByBusinessKey");
        Integer count = this.sqlExecutorDao.queryToInt(sql, bizId);
        // 不存在任务的用户允许访问
        if (count == 0) {
            return true;
        }
        sql = this.getQuerySqlByName("countTaskinstByBusinessKeyAndPersonId");
        count = this.sqlExecutorDao.queryToInt(sql, bizId, personId);
        // 传入的人员不再任务处理人中
        if (count == 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkVisitTaskStatusByTaskId(String taskId) {
        // 根据任务id查询任务
        TaskDetail task = this.queryTaskDetail(taskId);
        if (task == null) {// 未找到任务不执行操作
            return true;
        }
        return TaskStatus.isToDoStatus(task.getStatusId());
    }

}
