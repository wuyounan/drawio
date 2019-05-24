package com.huigou.uasp.bpm.engine.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.task.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.context.OrgUnit;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.proxy.OrgApplicationProxy;
import com.huigou.uasp.bpm.CooperationModelKind;
import com.huigou.uasp.bpm.HandleResult;
import com.huigou.uasp.bpm.TaskStatus;
import com.huigou.uasp.bpm.configuration.application.ApprovalRuleApplication;
import com.huigou.uasp.bpm.configuration.domain.model.TaskExecuteMode;
import com.huigou.uasp.bpm.engine.application.ProcUnitHandlerApplication;
import com.huigou.uasp.bpm.engine.application.WorkflowApplication;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandler;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandlerBase.Status;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandlerCache;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandlerGroup;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandlerManuscript;
import com.huigou.uasp.bpm.engine.repository.ProcUnitHandlerCacheRepository;
import com.huigou.uasp.bpm.engine.repository.ProcUnitHandlerManuscriptRepository;
import com.huigou.uasp.bpm.engine.repository.ProcUnitHandlerRepository;
import com.huigou.util.ClassHelper;
import com.huigou.util.CommonUtil;
import com.huigou.util.StringUtil;
import com.huigou.util.Util;

@Service("procUnitHandlerApplication")
public class ProcUnitHandlerApplicationImpl extends BaseApplication implements ProcUnitHandlerApplication {

    @Autowired
    private ProcUnitHandlerRepository procUnitHandlerRepository;

    @Autowired
    private WorkflowApplication workflowApplication;

    @Autowired
    private OrgApplicationProxy orgApplication;

    @Autowired
    private ProcUnitHandlerCacheRepository procUnitHandlerCacheRepository;

    @Autowired
    private ProcUnitHandlerManuscriptRepository procUnitHandlerManuscriptRepository;

    @Autowired
    private ApprovalRuleApplication approvalRuleApplication;

    private String getQuerySqlByName(String name) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery("config/uasp/query/bmp/bpm.xml", "procUnitHandler");
        return queryDescriptor.getSqlByName(name);
    }

    @Override
    @Transactional
    public String saveProcUnitHandler(ProcUnitHandler procUnitHandler) {
        Assert.notNull(procUnitHandler, "参数procUnitHandler不能为空。");
        procUnitHandler = this.procUnitHandlerRepository.save(procUnitHandler);
        return procUnitHandler.getId();
        // TODO 记录历史
        // List<Map<String, Object>> list = new ArrayList<Map<String,
        // Object>>(1);
        // list.add(params.getProperties());
        // this.historicProcUnitHandlerService.recordProcUnitHandlerInsertion(list);
    }

    @Override
    @Transactional
    public String saveProcUnitHandlerManuscript(String bizId, String procUnitHandlerId, Integer height, String opinion30, String opinion64) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitHandlerId, "参数procUnitHandlerId不能为空。");
        Assert.notNull(height, "参数height不能为空。");

        ProcUnitHandlerManuscript procUnitHandlerManuscript = this.procUnitHandlerManuscriptRepository.findByProcUnitHandlerId(procUnitHandlerId);
        if (procUnitHandlerManuscript == null) {
            procUnitHandlerManuscript = new ProcUnitHandlerManuscript();
            procUnitHandlerManuscript.setProcUnitHandlerId(procUnitHandlerId);
            procUnitHandlerManuscript.setBizId(bizId);
        }

        procUnitHandlerManuscript.setHeight(height);
        procUnitHandlerManuscript.setOpinion30(opinion30);
        procUnitHandlerManuscript.setOpinion64(opinion64);
        procUnitHandlerManuscript = procUnitHandlerManuscriptRepository.save(procUnitHandlerManuscript);
        return procUnitHandlerManuscript.getId();
    }

    @Override
    public ProcUnitHandlerManuscript loadProcUnitHandlerManuscript(String procUnitHandlerId) {
        Assert.hasText(procUnitHandlerId, "参数procUnitHandlerId不能为空。");
        return procUnitHandlerManuscriptRepository.findByProcUnitHandlerId(procUnitHandlerId);
    }

    @Override
    public List<ProcUnitHandlerManuscript> queryProcUnitHandlerManuscriptsByBizId(String bizId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        return this.procUnitHandlerManuscriptRepository.findByBizId(bizId);
    }

    @Override
    @Transactional
    public void saveProcUnitHandlers(List<ProcUnitHandler> procUnitHandlers) {
        saveProcUnitHandlers(procUnitHandlers, true);
    }

    @Override
    @Transactional
    public void saveProcUnitHandlers(List<ProcUnitHandler> procUnitHandlers, boolean updateExecutionTimes) {
        Assert.notEmpty(procUnitHandlers, "参数procUnitHandlers不能为空。");

        ProcUnitHandler firstProcUnitHandler = procUnitHandlers.get(0);

        Integer executionTimes = 1;
        if (updateExecutionTimes) {
            executionTimes = procUnitHandlerRepository.getExecutionTimes(firstProcUnitHandler.getBizId(), firstProcUnitHandler.getProcUnitId());
            ++executionTimes;
        }

        for (ProcUnitHandler procUnitHandler : procUnitHandlers) {
            if (procUnitHandler.isNew()) {
                procUnitHandler.setId(CommonUtil.createGUID());
            }
            /*
             * if (StringUtil.isNotBlank(procUnitHandler.getApprovalRuleHandlerId())) {
             * ApprovalRuleHandler approvalRuleHandler = approvalRuleApplication.loadApprovalRuleHandler(procUnitHandler.getApprovalRuleHandlerId());
             * procUnitHandler.setApprovalRuleHandler(approvalRuleHandler);
             * }
             */
            procUnitHandler.setExecutionTimes(executionTimes);
        }

        this.procUnitHandlerRepository.save(procUnitHandlers);
        // historicProcUnitHandlerService.recordProcUnitHandlerInsertion(handlers);
        // serviceUtil.getEntityDao().batchInsert(getProcUnitHandlerEntity(),
        // handlers);

    }

    @Override
    @Transactional
    public void batchSaveProcUnitHandlers(List<ProcUnitHandler> procUnitHandlers) {
        // TODO 记录历史
        // historicProcUnitHandlerService.recordProcUnitHandlerInsertion(handlers);
        // if (inserted){
        // this.historicProcUnitHandlerService.recordProcUnitHandlerInsertion(inserted);
        // }

        Assert.notEmpty(procUnitHandlers, "参数procUnitHandlers不能为空。");
        this.procUnitHandlerRepository.save(procUnitHandlers);
    }

    @Override
    @Transactional
    public void deleteProcUnitHandlers(List<String> ids) {
        this.checkIdsNotEmpty(ids);
        List<ProcUnitHandler> procUnitHandlers = this.procUnitHandlerRepository.findAll(ids);
        this.procUnitHandlerRepository.delete(procUnitHandlers);
    }

    @Override
    @Transactional
    public void deleteProcUnitHandler(String id) {
        List<String> ids = new ArrayList<String>(1);
        ids.add(id);

        this.deleteProcUnitHandlers(ids);
    }

    @Override
    @Transactional
    public void deleteProcUnitHandlersByBizAndProcUnitId(String bizId, String procUnitId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));

        List<ProcUnitHandler> procUnitHandlers = this.procUnitHandlerRepository.findByBizIdAndProcUnitId(bizId, procUnitId);
        if (procUnitHandlers.size() > 0) {
            // 更新历史审批人实例版本
            // this.historicProcUnitHandlerService.updateHistoricProcUnitHandlerInstVers(bizId,
            // procUnitId);
            this.procUnitHandlerRepository.delete(procUnitHandlers);
        }
    }

    @Override
    public void deleteProcUnitHandlersByBizAndGroupId(String bizId, Integer groupId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.notNull(groupId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_HANDLER_GROUP_ID_NOT_NULL));

        List<ProcUnitHandler> procUnitHandlers = this.procUnitHandlerRepository.findAllNextProcUnitHandlers(bizId, groupId);
        // 更新历史审批人实例版本
        // this.historicProcUnitHandlerService.updateHistoricProcUnitHandlerInstVers(bizId,
        // procUnitId);
        this.procUnitHandlerRepository.delete(procUnitHandlers);
    }

    @Override
    @Transactional
    public void deleteProcUnitHandlersByBizId(String bizId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        // this.historicProcUnitHandlerService.updateHistoricProcUnitHandlerInstVers(bizId,
        // "Approve");
        List<ProcUnitHandler> procUnitHandlers = this.procUnitHandlerRepository.findByBizId(bizId);
        this.procUnitHandlerRepository.delete(procUnitHandlers);

    }

    @Override
    @Transactional
    public void deleteWithdrawSucceedingHandlers(String bizId, String taskId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(taskId, "参数taskId不能为空。");
        this.procUnitHandlerRepository.deleteWithdrawSucceedingHandlers(bizId, taskId);
    }

    @Override
    @Transactional
    public String copyProcUnitHandler(String id) {
        this.checkIdNotBlank(id);

        ProcUnitHandler source = this.procUnitHandlerRepository.findOne(id);
        Assert.notNull(source, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, id, "环节处理人"));

        ProcUnitHandler dest = source.clone();
        dest.setId(CommonUtil.createGUID());
        dest.setSendMessage(0);
        // 处理人未处理

        dest = procUnitHandlerRepository.save(dest);
        return dest.getId();
    }

    @Override
    @Transactional
    public String copyProcUnitHandler(String id, String bizId, String bizCode, String procUnitId, Integer groupId) {
        this.checkIdNotBlank(id);

        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(bizCode, MessageSourceContext.getMessage(MessageConstants.BIZ_CODE_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));
        Assert.notNull(groupId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_HANDLER_GROUP_ID_NOT_NULL));

        ProcUnitHandler source = this.procUnitHandlerRepository.findOne(id);
        Assert.notNull(source, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, id, "环节处理人"));

        ProcUnitHandler dest = source.clone();
        dest.setId(CommonUtil.createGUID());
        dest.setBizId(bizId);
        dest.setBizCode(bizCode);
        dest.setGroupId(groupId);
        dest.setProcUnitId(procUnitId);

        dest = procUnitHandlerRepository.save(dest);
        return dest.getId();
    }

    @Override
    @Transactional
    public void copyProcUnitHandlersByBizAndProcUnitId(String bizId, String sourceProcUnitId, String destProcUnitId, String destProcUnitName) {
        List<ProcUnitHandler> sources = this.procUnitHandlerRepository.findByBizIdAndProcUnitId(bizId, sourceProcUnitId);
        Assert.notEmpty(sources, "未找到流程环节处理人。");
        List<ProcUnitHandler> dests = new ArrayList<ProcUnitHandler>(sources.size());
        ProcUnitHandler dest;
        for (ProcUnitHandler source : sources) {
            dest = source.clone();
            dest.setId(null);
            dest.setProcUnitId(destProcUnitId);
            dest.setProcUnitName(destProcUnitName);

            dests.add(dest);
        }
        this.procUnitHandlerRepository.save(dests);
    }

    @Override
    public Map<String, Object> queryProcunitHandlersByBizCode(String bizCode) {
        Assert.hasText(bizCode, "参数bizCode不能为空。");

        QueryModel queryModel = new QueryModel();
        queryModel.setSql(this.getQuerySqlByName("queryProcunitHandlersByBizCode"));
        queryModel.putParam("bizCode", bizCode);

        return this.sqlExecutorDao.executeQuery(queryModel);
    }

    @Override
    public TaskExecuteMode getTaskExecuteMode(String bizId, String procUnitId, Integer groupId) {
        ProcUnitHandler procUnitHandler = this.procUnitHandlerRepository.findFirstByBizIdAndProcUnitIdAndGroupId(bizId, procUnitId, groupId);
        if (procUnitHandler != null) {
            return procUnitHandler.getTaskExecuteMode();
        }
        return null;
    }

    private List<Map<String, Object>> queryProcUnitHandlersForMap(String bizId, String procUnitId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));

        String sql = this.getQuerySqlByName("queryProcUnitHandlersByBizId");
        List<Map<String, Object>> list = this.sqlExecutorDao.queryToListMap(sql, bizId, procUnitId);
        Map<String, Object> taskInfo = null;
        String procUnitHandlerId = null;
        for (Map<String, Object> map : list) {
            procUnitHandlerId = ClassHelper.convert(map.get("id"), String.class);
            taskInfo = this.queryOneTaskByBizIdAndProcUnitHandlerId(bizId, procUnitHandlerId);
            map.putAll(taskInfo);
        }
        return list;
    }

    /**
     * 根据版本号查询最近的一条任务信息
     * 
     * @param bizId
     * @param procUnitHandlerId
     * @return
     */
    private Map<String, Object> queryOneTaskByBizIdAndProcUnitHandlerId(String bizId, String procUnitHandlerId) {
        String sql = this.getQuerySqlByName("queryOneTaskByBizIdAndProcUnitHandlerId");
        return this.sqlExecutorDao.queryOneToMap(sql, bizId, procUnitHandlerId);
    }

    @Override
    public List<Map<String, Object>> groupProcUnitHandlers(String bizId, String processDefinitionKey) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(processDefinitionKey, "参数processDefinitionKey不能为空。");

        String sql = this.getQuerySqlByName("queryAllProcUnitHandlersByBizId");
        List<Map<String, Object>> procUnitHandlers = this.sqlExecutorDao.queryToListMap(sql, bizId, processDefinitionKey);

        boolean supportManuscript = Boolean.valueOf(SystemCache.getParameter("wf.approval.supportManuscript", String.class));
        List<ProcUnitHandlerManuscript> procUnitHandlerManuscripts = null;
        if (supportManuscript) {
            procUnitHandlerManuscripts = this.procUnitHandlerManuscriptRepository.findByBizId(bizId);
        }

        String procUnitHandlerId;
        String principalName, handlerName;
        Map<String, Object> taskInfo = null;
        for (Map<String, Object> map : procUnitHandlers) {
            procUnitHandlerId = ClassHelper.convert(map.get("id"), String.class);
            taskInfo = this.queryOneTaskByBizIdAndProcUnitHandlerId(bizId, procUnitHandlerId);
            map.putAll(taskInfo);
            // 手写
            if (supportManuscript) {
                for (ProcUnitHandlerManuscript procUnitHandlerManuscript : procUnitHandlerManuscripts) {
                    if (procUnitHandlerManuscript.getProcUnitHandlerId().equalsIgnoreCase(procUnitHandlerId)) {
                        map.put("manuscript", procUnitHandlerManuscript.getOpinion64());
                        break;
                    }
                }
            }

            principalName = ClassHelper.convert(map.get("principalName"), String.class, "");
            if (!StringUtil.isBlank(principalName)) {
                handlerName = ClassHelper.convert(map.get("handlerName"), String.class, "");
                handlerName = String.format("%s(代%s)", handlerName, principalName);
                map.put("handlerName", handlerName);
            }
        }

        return procUnitHandlers;
    }

    @Override
    public List<Map<String, Object>> groupProcUnitHandlers(String bizId, String approvalProcUnitId, String procUnitId, String currentTaskId, String personId) {
        List<Map<String, Object>> procUnitHandlers = queryProcUnitHandlersForMap(bizId, approvalProcUnitId);
        Map<String, ProcUnitHandlerGroup> groupMap = new LinkedHashMap<String, ProcUnitHandlerGroup>();
        // bizId --> processInstanceId
        List<Comment> comments = this.workflowApplication.getTaskService().getProcessInstanceComments(bizId);
        boolean supportManuscript = Boolean.valueOf(SystemCache.getParameter("wf.approval.supportManuscript", String.class));
        List<ProcUnitHandlerManuscript> procUnitHandlerManuscripts = null;
        if (supportManuscript) {
            procUnitHandlerManuscripts = this.procUnitHandlerManuscriptRepository.findByBizId(bizId);
        }
        int i = 0;
        String procUnitHandlerId;
        List<Map<String, Object>> procUnitHandlerComments;
        Map<String, Object> item;
        String principalName, handlerName, statusId;
        Integer result = 0;
        for (Map<String, Object> map : procUnitHandlers) {
            // 抢占模式下系统自动完成的任务数据不显示
            result = ClassHelper.convert(map.get("result"), Integer.class, 0);
            statusId = ClassHelper.convert(map.get("statusId"), String.class, "");
            // 任务状态为取消
            if (statusId != null && statusId.equals(TaskStatus.CANCELED.getId())) {
                // 结果系统自动完成
                if (result == HandleResult.SYSTEM_COMPLETE.getId()) {
                    continue;
                }
            }
            map.put("taskIndex", i);
            String groupId = ClassHelper.convert(map.get("groupId"), String.class);
            if (groupMap.containsKey(groupId)) {
                ProcUnitHandlerGroup group = groupMap.get(groupId);
                group.addHandler(map, personId, procUnitId, currentTaskId);
            } else {
                ProcUnitHandlerGroup group = new ProcUnitHandlerGroup(Integer.parseInt(groupId));
                group.addHandler(map, personId, procUnitId, currentTaskId);
                groupMap.put(groupId, group);
            }
            // 评论
            procUnitHandlerId = ClassHelper.convert(map.get("id"), String.class);
            procUnitHandlerComments = new ArrayList<Map<String, Object>>();
            for (Comment comment : comments) {
                if (comment.getTaskId().equalsIgnoreCase(procUnitHandlerId)) {
                    item = new HashMap<String, Object>(3);
                    item.put("userId", comment.getUserId());
                    item.put("time", comment.getTime());
                    item.put("fullMessage", ((CommentEntity) comment).getMessage());
                    procUnitHandlerComments.add(item);
                }
            }
            map.put("comments", procUnitHandlerComments);
            // 手写
            if (supportManuscript) {
                for (ProcUnitHandlerManuscript procUnitHandlerManuscript : procUnitHandlerManuscripts) {
                    if (procUnitHandlerManuscript.getProcUnitHandlerId().equalsIgnoreCase(procUnitHandlerId)) {
                        map.put("manuscript", procUnitHandlerManuscript.getOpinion64());
                        break;
                    }
                }
            }

            principalName = ClassHelper.convert(map.get("principalName"), String.class, "");
            if (!StringUtil.isBlank(principalName)) {
                handlerName = ClassHelper.convert(map.get("handlerName"), String.class, "");
                handlerName = String.format("%s(代%s)", handlerName, principalName);
                map.put("handlerName", handlerName);
            }

            i++;
        }
        List<Map<String, Object>> groupList = new java.util.ArrayList<Map<String, Object>>();
        Boolean showCurrentGroupAfterHandlers = SystemCache.getParameter("wf.approval.showAfterHandlers", Boolean.class);
        if (showCurrentGroupAfterHandlers == null) {
            showCurrentGroupAfterHandlers = true;
        }

        for (ProcUnitHandlerGroup g : groupMap.values()) {
            groupList.add(g.toMap());
            if (!showCurrentGroupAfterHandlers) {
                if (g.isCurrentGroup()) {
                    break;
                }
            }
        }
        return groupList;
    }

    @Override
    public ProcUnitHandler loadProcUnitHandler(String id) {
        this.checkIdNotBlank(id);
        return this.procUnitHandlerRepository.findOne(id);
    }

    @Override
    public List<ProcUnitHandler> queryNextGroupProcUnitHandlers(String bizId, String procUnitId, Integer groupId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        // Assert.hasText(procUnitId, "参数procUnitId不能为空。");
        Assert.notNull(groupId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_HANDLER_GROUP_ID_NOT_NULL));

        return this.procUnitHandlerRepository.findNextGroupProcUnitHandlers(bizId, procUnitId, groupId);
    }

    @Override
    public List<String> queryProcUnitHandlerIds(String bizId, String procUnitId, Integer groupId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.notNull(groupId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_HANDLER_GROUP_ID_NOT_NULL));
        return this.procUnitHandlerRepository.findProcUnitHandlerIds(bizId, procUnitId, groupId);
    }

    @Override
    public String queryApprovalRuleId(String bizId, String procUnitId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));
        String sql = this.getQuerySqlByName("queryProcUnitApprovalRuleId");
        return this.sqlExecutorDao.queryToString(sql, bizId, procUnitId);
    }

    @Override
    public List<ProcUnitHandler> queryCompletedProcUnitHandlers(String bizId, String procUnitId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));
        return this.procUnitHandlerRepository.findCompletedProcUnitHandlers(bizId, procUnitId);
    }

    @Override
    public List<ProcUnitHandler> queryCompletedProcUnitHandlers(String bizId, String procUnitId, Integer groupId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));
        Assert.notNull(groupId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_HANDLER_GROUP_ID_NOT_NULL));
        return this.procUnitHandlerRepository.findCompletedProcUnitHandlers(bizId, procUnitId, groupId);
    }

    @Override
    public List<ProcUnitHandler> queryProcUnitHandlers(String bizId, String procUnitId, Integer groupId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.notNull(groupId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_HANDLER_GROUP_ID_NOT_NULL));
        return this.procUnitHandlerRepository.findProcUnitHandlers(bizId, procUnitId, groupId);
    }

    @Override
    public List<Map<String, Object>> queryProcUnitHandlersAndTaskInsts(String bizId, String procUnitId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));
        String sql = this.getQuerySqlByName("queryProcUnitHandlersAndTaskInsts");
        return this.sqlExecutorDao.queryToListMap(sql, bizId, procUnitId);
    }

    @Override
    public List<ProcUnitHandler> queryCCProcUnitHandlers(String bizId, String procUnitId, String chiefId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));
        Assert.hasText(chiefId, "参数chiefId不能为空。");

        return this.procUnitHandlerRepository.findCCProcUnitHandlers(bizId, procUnitId, chiefId);
    }

    @Override
    public List<ProcUnitHandler> queryCCProcUnitHandlersByBizId(String bizId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        return this.procUnitHandlerRepository.findCCProcUnitHandlersByBizId(bizId);
    }

    @Override
    public List<ProcUnitHandler> queryInitialMendProcUnitHandlers(String bizId, String procUnitId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));

        return this.procUnitHandlerRepository.findInitialMendProcUnitHandlers(bizId, procUnitId);
    }

    @Override
    public boolean isPsmInHandlers(String bizId, String procUnitId, String personMemberId) {
        long count = this.procUnitHandlerRepository.countProcUnitHandlers(bizId, procUnitId, personMemberId);
        return count > 0;
    }

    @Override
    @Transactional
    public void updateProcUnitHandlerOrgData(String id, OrgUnit orgUnit) {
        this.checkIdNotBlank(id);
        Assert.notNull(orgUnit, "参数orgUnit不能为空。");

        ProcUnitHandler procUnitHandler = this.loadProcUnitHandler(id);
        procUnitHandler.buildOrgNodeData(orgUnit);

        this.procUnitHandlerRepository.save(procUnitHandler);
    }

    @Override
    public ProcUnitHandler transmit(String id, Integer sendMessage, OrgUnit orgUnit) {
        this.checkIdNotBlank(id);
        Assert.notNull(orgUnit, "参数orgUnit不能为空。");

        ProcUnitHandler procUnitHandler = this.loadProcUnitHandler(id);

        procUnitHandler.buildOrgNodeData(orgUnit);
        procUnitHandler.setId(null);
        procUnitHandler.setSendMessage(sendMessage);

        return this.procUnitHandlerRepository.save(procUnitHandler);
    }

    @Override
    @Transactional
    public String saveAssistant(String chiefProcUnitHandlerId, Org personMember, Integer sendMessage, Integer index) {
        this.checkIdNotBlank(chiefProcUnitHandlerId);

        Assert.notNull(personMember, "参数personMember不能为空。");

        ProcUnitHandler chiefUnitHandler = this.loadProcUnitHandler(chiefProcUnitHandlerId);
        Assert.notNull(chiefUnitHandler, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, chiefProcUnitHandlerId, "流程环节处理人"));

        OrgUnit orgUnit = new OrgUnit(personMember.getFullId(), personMember.getFullName());// OrgNodeData.buildOrgNodeData(personMember);
        ProcUnitHandler assistant = chiefUnitHandler.clone();
        assistant.setId(CommonUtil.createGUID());
        assistant.buildOrgNodeData(orgUnit);
        assistant.setSubProcUnitName(chiefUnitHandler.getSubProcUnitName() + "协审");
        assistant.setCooperationModelId(CooperationModelKind.ASSISTANT);
        assistant.setChiefId(chiefUnitHandler.getId());

        assistant.setSequence(chiefUnitHandler.getSequence() + index);

        assistant.clearHandleOpinion();

        assistant.setSendMessage(sendMessage);

        assistant = this.procUnitHandlerRepository.save(assistant);

        return assistant.getId();
    }

    @Override
    @Transactional
    public String saveAssistant(String chiefProcUnitHandlerId, String personMemberId, Integer sendMessage, Integer index) {
        Org personMember = this.orgApplication.loadOrg(personMemberId);
        return this.saveAssistant(chiefProcUnitHandlerId, personMember, sendMessage, index);
    }

    @Override
    public List<ProcUnitHandler> queryAssistantHandlers(String bizId, String procUnitId, String chiefProcUnitHandlerId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));
        Assert.hasText(chiefProcUnitHandlerId, "参数chiefProcUnitHandlerId不能为空。");

        return this.procUnitHandlerRepository.findAssistantHandlers(bizId, procUnitId, chiefProcUnitHandlerId);
    }

    @Override
    public ProcUnitHandler queryChiefHandler(String assistantProcUnitHandlerId) {
        Assert.hasText(assistantProcUnitHandlerId, "参数assistantProcUnitHandlerId不能为空。");
        return this.procUnitHandlerRepository.findChiefHandler(assistantProcUnitHandlerId);
    }

    @Override
    public Map<String, Object> queryCounterSignHandlers(String bizId, String procUnitId) {
        QueryModel queryModel = new QueryModel();
        queryModel.setSql(this.getQuerySqlByName("queryCounterSignHandlers"));
        queryModel.putParam("bizId", bizId);
        queryModel.putParam("procUnitId", procUnitId);
        return this.sqlExecutorDao.executeQuery(queryModel);
    }

    @Override
    @Transactional
    public void saveCounterSignHandlers(String bizId, String procUnitId, Long version, String currentProcUnitHandlerId, List<String> minusSignIds,
                                        List<ProcUnitHandler> counterSigns) {
        // (申请环节可以加减、后续审批人并行处理，出现任务存在，处理人已删除的情况)检查版本
        Long dbVersion = null;// this.historicProcUnitHandlerService.getHistoricProcUnitHandlerInstVersion(bizId,
                              // procUnitId);

        if (!CommonUtil.isLongNull(dbVersion)) {
            Util.check(version.equals(dbVersion), "流程处理人已更改，不能加减签。");
        }
        if (minusSignIds != null && minusSignIds.size() > 0) {
            List<ProcUnitHandler> minusSignProcUnitHandlers = this.procUnitHandlerRepository.findAll(minusSignIds);
            Assert.isTrue(minusSignIds.size() == minusSignProcUnitHandlers.size(),
                          MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "减签人员"));
            this.procUnitHandlerRepository.delete(minusSignProcUnitHandlers);
        }

        if (counterSigns.size() > 0) {
            ProcUnitHandler currentProcUnitHandler = null;
            if (StringUtil.isNotBlank(currentProcUnitHandlerId)) {
                currentProcUnitHandler = this.loadProcUnitHandler(currentProcUnitHandlerId);
                Assert.notNull(currentProcUnitHandler,
                               MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, currentProcUnitHandlerId, "流程处理人"));
            }

            Integer executionTimes = procUnitHandlerRepository.getExecutionTimes(bizId, procUnitId);
            Integer groupId;
            String subProcUnitName;
            List<ProcUnitHandler> addedList = new ArrayList<ProcUnitHandler>(counterSigns.size());

            for (ProcUnitHandler counterSign : counterSigns) {
                if (counterSign.isNew()) {
                    counterSign.setId(CommonUtil.createGUID());
                    if (currentProcUnitHandler != null) {
                        counterSign.setApprovalRuleId(currentProcUnitHandler.getApprovalRuleId());
                        counterSign.setApprovalRuleHandlerId(currentProcUnitHandler.getApprovalRuleHandlerId());
                    }
                    counterSign.setExecutionTimes(executionTimes);
                    addedList.add(counterSign);
                } else {
                    groupId = counterSign.getGroupId();
                    subProcUnitName = counterSign.getSubProcUnitName();
                    counterSign = this.procUnitHandlerRepository.findOne(counterSign.getId());
                    Assert.notNull(counterSign, MessageSourceContext.getMessage(MessageConstants.LOAD_OBJECT_IS_NULL));
                    counterSign.setGroupId(groupId);
                    counterSign.setSubProcUnitName(subProcUnitName);
                    addedList.add(counterSign);
                }
            }
            this.procUnitHandlerRepository.save(addedList);
        }

        // this.historicProcUnitHandlerService.updateHistoricProcUnitHandlerInstVersion(bizId,
        // procUnitId);
        //
        //
        // List<Object> dataParams = params.getList("detailData");
        // List<Map<String, Object>> data = this.covertToListMap(dataParams);
        //
        // List<Map<String, Object>> added = new ArrayList<Map<String,
        // Object>>(data.size());
        // List<Map<String, Object>> updated = new ArrayList<Map<String,
        // Object>>(data.size());
        //
        // boolean hasAdded = false, hasUpdated = false;
        //
        // Long procUnitHandlerId = params.getProperty("procUnitHandlerId",
        // Long.class, 0L);
        // ProcUnitHandler procUnitHandler = null;
        // if (!CommonUtil.isLongNull(procUnitHandlerId)) {
        // procUnitHandler = this.loadProcUnitHandlerById(procUnitHandlerId);
        // }
        //
        // for (Map<String, Object> item : data) {
        // if (item.get("id") == null) {
        // added.add(item);
        // hasAdded = true;
        // } else {
        // updated.add(item);
        // hasUpdated = true;
        // }
        //
        // if (procUnitHandler != null) {
        // item.put("approvalRuleId", procUnitHandler.getApprovalRuleId());
        // item.put("approvalRuleHandlerId",
        // procUnitHandler.getApprovalRuleHandlerId());
        // }
        // }
        // // 更新排序号
        // if (hasUpdated) {
        // this.serviceUtil.getEntityDao().batchUpdate(this.getProcUnitHandlerEntity(),
        // updated, null);
        // }
        // // 加签
        // if (hasAdded) {
        // this.batchInsertProcUnitHandlers(added);
        // }
        //
        // this.historicProcUnitHandlerService.updateHistoricProcUnitHandlerInstVersion(bizId,
        // procUnitId);
    }

    @Override
    public Integer getActiveProcUnitHanderGroupId(String bizId, String procUnitId) {
        String sql = this.getQuerySqlByName("getActiveProcUnitGroupId");
        return this.sqlExecutorDao.queryToInt(sql, bizId, procUnitId);
    }

    @Override
    public ProcUnitHandler queryLastProcUnitHandler(String bizId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));

        String hql = "from ProcUnitHandler o where o.bizId = :bizId and o.cooperationModelId = 'chief' and o.status = 1 order by groupId desc";

        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("bizId", bizId);

        @SuppressWarnings("unchecked")
        List<ProcUnitHandler> procUnitHandlers = (List<ProcUnitHandler>) this.generalRepository.query(hql, params, 0, 1);

        if (procUnitHandlers.size() > 0) {
            return procUnitHandlers.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    public void updateProcUnitHandlerStatus(ProcUnitHandler procUnitHandler, Status status) {
        Assert.notNull(procUnitHandler, "参数procUnitHandler不能为空。");
        procUnitHandler.setStatus(status.getId());
        this.procUnitHandlerRepository.save(procUnitHandler);
    }

    private void chekProcUnitHandlerExist(String id, ProcUnitHandler procUnitHandler) {
        Assert.notNull(procUnitHandler, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, id, "流程环节处理人"));
    }

    @Override
    @Transactional
    public void updateSucceedingProcUnitHandlersStatus(ProcUnitHandler procUnitHandler, Status status) {
        Assert.notNull(procUnitHandler, "参数procUnitHandler不能为空。");
        List<ProcUnitHandler> SucceedingProcUnitHandlers = this.procUnitHandlerRepository.findSucceedingProcUnitHandlers(procUnitHandler.getBizId(),
                                                                                                                         procUnitHandler.getGroupId());
        for (ProcUnitHandler item : SucceedingProcUnitHandlers) {
            item.setStatus(status.getId());
        }

        this.procUnitHandlerRepository.save(SucceedingProcUnitHandlers);
    }

    @Override
    public Boolean checkChiefApproveFinished(String bizId, String procUnitId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));

        Integer count = this.procUnitHandlerRepository.countChiefNotApproveByBizIdAndprocUnitId(bizId, procUnitId);
        return count.equals(0);
    }

    @Override
    public Boolean checkCurrentGroupApproveFinished(String bizId, String procUnitId, Integer groupId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));

        long count = this.procUnitHandlerRepository.countCurrentGroupChiefNotApproveByBizIdAndprocUnitId(bizId, procUnitId, groupId);
        return count == 0;
    }

    @Override
    public Integer countChiefNotApprove(String bizId, String procUnitId) {
        return this.procUnitHandlerRepository.countChiefNotApproveByBizIdAndprocUnitId(bizId, procUnitId);
    }

    @Override
    public Integer countAssistantNotApproveByChiefId(String bizId, String chiefId) {
        // TODO 验证
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(chiefId, "参数chiefId不能为空。");
        return this.procUnitHandlerRepository.countAssistantNotApproveByChiefId(bizId, chiefId);
    }

    @Override
    public Boolean checkCurrentGroupChiefApprovePassed(String bizId, String procUnitId, Integer groupId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));
        Assert.notNull(groupId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_HANDLER_GROUP_ID_NOT_NULL));

        long count = this.procUnitHandlerRepository.countCurrentGroupChiefNotApproveByBizIdAndprocUnitId(bizId, procUnitId, groupId);
        return count == 0;
    }

    @Override
    @Transactional
    public void updateProcUnitHandlerResult(String id, HandleResult result, String opinion, Status status) {
        ProcUnitHandler procUnitHandler = this.loadProcUnitHandler(id);
        chekProcUnitHandlerExist(id, procUnitHandler);

        procUnitHandler.updateHandleResult(result.getId(), opinion, status.getId());

        this.procUnitHandlerRepository.save(procUnitHandler);
    }

    @Override
    @Transactional
    public void updateOtherProcUnitHandlersResultSystemComplete(String bizId, String procUnitId, Integer currentGroupId, String currentProcUnitHandlerId,
                                                                String opinion) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));

        List<String> procUnitHandlerIds = queryProcUnitHandlerIds(bizId, procUnitId, currentGroupId);

        ProcUnitHandler procUnitHandler;

        for (String procUnitHandlerId : procUnitHandlerIds) {
            if (!procUnitHandlerId.equals(currentProcUnitHandlerId)) {
                procUnitHandler = this.loadProcUnitHandler(procUnitHandlerId);
                chekProcUnitHandlerExist(procUnitHandlerId, procUnitHandler);
                if (procUnitHandler.isChief() || !currentProcUnitHandlerId.equals(procUnitHandler.getChiefId())) {
                    procUnitHandler.updateHandleResult(HandleResult.SYSTEM_COMPLETE.getId(), opinion, ProcUnitHandler.Status.COMPLETED.getId());
                    this.procUnitHandlerRepository.save(procUnitHandler);
                }
                // updateProcUnitHandlerResult(procUnitHandlerId, HandleResult.SYSTEM_COMPLETE, opinion, ProcUnitHandler.Status.COMPLETED);
            }
        }
    }

    @Override
    public Map<String, Object> queryHistoricProcUnitHandlers(String bizCode) {
        Assert.hasText(bizCode, "参数bizCode不能为空。");
        // return this.historicProcUnitHandlerService.queryHistoricProcUnitHandler(bizCode);
        return null;
    }

    @Override
    @Transactional
    public void updateProcUnitHandlerClient(String id, String clientId, String clientName) {
        ProcUnitHandler procUnitHandler = this.loadProcUnitHandler(id);
        chekProcUnitHandlerExist(id, procUnitHandler);

        procUnitHandler.updateClient(clientId, clientName);

        this.procUnitHandlerRepository.save(procUnitHandler);
    }

    @Override
    @Transactional
    public void updateProcUnitHandlerAssistantChiefId(String bizId, String procUnitId, String oldChiefId, String newChiefId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));
        Assert.hasText(oldChiefId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "oldChiefId"));
        Assert.hasText(newChiefId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "newChiefId"));

        List<ProcUnitHandler> assistantHandlers = this.procUnitHandlerRepository.findAssistantHandlers(bizId, procUnitId, oldChiefId);
        if (assistantHandlers.size() > 0) {
            for (ProcUnitHandler assistantHandler : assistantHandlers) {
                assistantHandler.setChiefId(newChiefId);
            }
            this.procUnitHandlerRepository.save(assistantHandlers);
        }
    }

    private void checkCanBackupOrRecover(String bizCode, String errorMessage) {
        ProcUnitHandler procUnitHandler = this.procUnitHandlerRepository.findFirstByBizCode(bizCode);
        Assert.notNull(procUnitHandler, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_CODE, bizCode, "流程环节处理人"));

        HistoricProcessInstance historicProcessInstance = this.workflowApplication.getHistoryService().createHistoricProcessInstanceQuery()
                                                                                  .processInstanceBusinessKey(procUnitHandler.getBizId()).singleResult();

        Assert.notNull(historicProcessInstance,
                       MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_BIZ_ID, procUnitHandler.getBizId(), "历史流程实例"));

        Assert.isTrue(historicProcessInstance.getEndTime() == null, errorMessage);
    }

    @Override
    @Transactional
    public void backupProcUnitHandler(String bizCode) {
        Assert.hasText(bizCode, MessageSourceContext.getMessage(MessageConstants.BIZ_CODE_NOT_BLANK));

        checkCanBackupOrRecover(bizCode, "流程已结束，不能备份。");

        this.procUnitHandlerCacheRepository.deleteByBizCode(bizCode);

        List<ProcUnitHandler> procUnitHandlers = this.procUnitHandlerRepository.findByBizCode(bizCode);

        List<ProcUnitHandlerCache> procUnitHandlerCaches = ProcUnitHandlerCache.fromProcUnitHandlers(procUnitHandlers);
        this.procUnitHandlerCacheRepository.save(procUnitHandlerCaches);
    }

    @Override
    @Transactional
    public void recoverProcUnitHandler(String bizCode) {
        Assert.hasText(bizCode, MessageSourceContext.getMessage(MessageConstants.BIZ_CODE_NOT_BLANK));

        checkCanBackupOrRecover(bizCode, "流程已结束，不能恢复。");

        long count = procUnitHandlerCacheRepository.countByBizCode(bizCode);
        Assert.isTrue(count > 0, "没有备份数据，不能恢复。");

        count = this.workflowApplication.getActApplication().countReadyProcTasksByBizCode(bizCode);
        Assert.isTrue(count == 1, "恢复处理人出错，当前处理人数大于1。");

        List<ProcUnitHandlerCache> procUnitHandlerCaches = this.procUnitHandlerCacheRepository.findByBizCode(bizCode);

        List<ProcUnitHandler> procUnitHandlers = ProcUnitHandler.fromProcUnitHandlerCaches(procUnitHandlerCaches);
        this.procUnitHandlerRepository.deleteByBizCode(bizCode);
        this.procUnitHandlerRepository.save(procUnitHandlers);
    }

    @Override
    public Integer getMaxGrouId(String bizId, String procUnitId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));

        return this.procUnitHandlerRepository.getMaxGrouId(bizId, procUnitId);
    }

    @Override
    public Integer getMaxSequence(String bizId, String procUnitId) {
        Assert.hasText(bizId, MessageSourceContext.getMessage(MessageConstants.BIZ_ID_NOT_BLANK));
        Assert.hasText(procUnitId, MessageSourceContext.getMessage(MessageConstants.PROC_UNIT_ID_NOT_BLANK));
        return this.procUnitHandlerRepository.getMaxSequence(bizId, procUnitId);
    }

    @Override
    public List<Map<String, Object>> queryUIElmentPermissions(String procUnitHandlerId) {
        String sql = this.getQuerySqlByName("queryUIElmentPermissions");
        return this.sqlExecutorDao.queryToListMap(sql, procUnitHandlerId);
    }

}
