package com.huigou.uasp.bpm.configuration.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.CodeAndNameQueryRequest;
import com.huigou.data.domain.query.EmptyQueryRequest;
import com.huigou.data.domain.query.QueryParameter;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.domain.ValidStatus;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.configuration.domain.model.CommonTree;
import com.huigou.uasp.bmp.opm.repository.org.OrgRepository;
import com.huigou.uasp.bpm.CooperationModelKind;
import com.huigou.uasp.bpm.configuration.application.ApprovalRuleApplication;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalElement;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalHandlerKind;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRule;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleElement;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandler;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandlerAssist;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandlerGroup;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandlerUIElmentPermission;
import com.huigou.uasp.bpm.configuration.domain.model.ProcApprovalElement;
import com.huigou.uasp.bpm.configuration.domain.model.TaskExecuteMode;
import com.huigou.uasp.bpm.configuration.domain.query.ProcApprovalElementDesc;
import com.huigou.uasp.bpm.configuration.repository.ApprovalElementRepository;
import com.huigou.uasp.bpm.configuration.repository.ApprovalHandlerKindRepository;
import com.huigou.uasp.bpm.configuration.repository.ApprovalRuleRepository;
import com.huigou.uasp.bpm.configuration.repository.ProcApprovalElementRepository;
import com.huigou.uasp.bpm.engine.domain.model.LimitTime;
import com.huigou.uasp.bpm.managment.domain.model.ProcDefinition;
import com.huigou.uasp.bpm.managment.repository.ProcDefinitionRespository;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.StringUtil;

@Service("approvalRuleApplication")
public class ApprovalRuleApplicationImpl extends BaseApplication implements ApprovalRuleApplication {

    @Autowired
    private ApprovalElementRepository approvalElementRepository;

    @Autowired
    private ProcApprovalElementRepository procApprovalElementRepository;

    @Autowired
    private ApprovalHandlerKindRepository approvalHandlerKindRepository;

    @Autowired
    private ApprovalRuleRepository approvalRuleRepository;

    @Autowired
    private ProcDefinitionRespository procDefinitionRespository;

    @Autowired
    private OrgRepository orgRepository;

    private String getQuerySqlByName(String name) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "approvalRule");
        return queryDescriptor.getSqlByName(name);
    }

    @Override
    @Transactional
    public String saveApprovalElement(ApprovalElement approvalElement) {
        Assert.notNull(approvalElement, "参数approvalElement不能为空。");
        approvalElement = this.commonDomainService.loadAndFillinProperties(approvalElement, ApprovalElement.class);
        approvalElement = (ApprovalElement) this.commonDomainService.saveBaseInfoEntity(approvalElement, approvalElementRepository);
        return approvalElement.getId();
    }

    @Override
    public ApprovalElement loadApprovalElement(String id) {
        this.checkIdNotBlank(id);
        return this.approvalElementRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteApprovalElements(List<String> ids) {
        this.checkIdsNotEmpty(ids);
        List<ApprovalElement> approvalElements = this.approvalElementRepository.findAll(ids);

        ProcApprovalElement procApprovalElement;
        for (ApprovalElement approvalElement : approvalElements) {
            procApprovalElement = procApprovalElementRepository.findFirstByApprovalElement(approvalElement);
            if (procApprovalElement != null) {
                throw new ApplicationException(MessageSourceContext.getMessage(MessageConstants.OBJECT_REFERENCED_BY_WHO, approvalElement.getName(),
                        procApprovalElement.getProcDefinition().getName()));
            }
        }

        this.approvalElementRepository.delete(approvalElements);
    }

    @Override
    public Map<String, Object> slicedQueryApprovalElements(CodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "approvalRule");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    @Transactional
    public Integer getApprovalElementNextSequence() {
        return this.commonDomainService.getNextSequence(ApprovalElement.class);
    }

    @Override
    @Transactional
    public void updateApprovalElementsSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(ApprovalElement.class, params);

    }

    @Override
    @Transactional
    public String saveApprovalHandlerKind(ApprovalHandlerKind approvalHandlerKind) {
        Assert.notNull(approvalHandlerKind, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "approvalHandlerKind"));
        approvalHandlerKind = this.commonDomainService.loadAndFillinProperties(approvalHandlerKind, ApprovalHandlerKind.class);
        approvalHandlerKind = (ApprovalHandlerKind) this.commonDomainService.saveBaseInfoEntity(approvalHandlerKind, approvalHandlerKindRepository);
        return approvalHandlerKind.getId();
    }

    @Override
    public ApprovalHandlerKind loadApprovalHandlerKind(String id) {
        this.checkIdNotBlank(id);
        return this.approvalHandlerKindRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteApprovalHandlerKinds(List<String> ids) {
        this.checkIdsNotEmpty(ids);

        List<ApprovalHandlerKind> approvalHandlerKinds = this.approvalHandlerKindRepository.findAll(ids);
        long count;
        for (ApprovalHandlerKind approvalHandlerKind : approvalHandlerKinds) {
            // TODO
            count = 0L;// this.approvalRuleRepository.countByApprovalRuleHandlers_ApprovalHandlerKind(approvalHandlerKind);
            Assert.isTrue(count == 0L, String.format("%s被使用，不能删除。", approvalHandlerKind.getName()));
        }

        this.approvalHandlerKindRepository.delete(approvalHandlerKinds);
    }

    @Override
    public Map<String, Object> slicedQueryApprovalHandlerKinds(CodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "approvalRule");
        QueryModel mode = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest, "queryApprovalHandlerKinds");
        mode.putDictionary("dataSourceId", ApprovalHandlerKind.DataSource.getData());
        return this.sqlExecutorDao.executeSlicedQuery(mode);
    }

    @Override
    @Transactional
    public Integer getApprovalHandlerKindNextSequence() {
        return this.commonDomainService.getNextSequence(ApprovalHandlerKind.class);
    }

    @Override
    @Transactional
    public void updateApprovalHandlerKindsSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(ApprovalHandlerKind.class, params);
    }

    @Override
    @Transactional
    public void saveProcApprovalElement(String procId, String procUnitId, List<String> elementIds) {
        Assert.hasText(procId, "流程ID不能为空。");
        Assert.hasText(procUnitId, "流程环节ID不能为空。");
        Assert.notEmpty(elementIds, "审批要素不能为空。");

        ProcDefinition procDefinition = this.procDefinitionRespository.findByProcIdAndCode(procId, procUnitId);
        Assert.notNull(procDefinition, String.format("未找到ID“%s”对应的流程数据。", procUnitId));

        List<ApprovalElement> approvalElements = this.approvalElementRepository.findAll(elementIds);
        Assert.isTrue(elementIds.size() == approvalElements.size(), "审批要素ID列表中存在无效的ID。");

        List<ProcApprovalElementDesc> procApprovalElementDescs = this.queryProcApprovalElements(procDefinition.getProcId(), procDefinition.getCode());
        List<ProcApprovalElement> procApprovalElements = new ArrayList<ProcApprovalElement>(elementIds.size());
        boolean added;

        ProcApprovalElement procApprovalElement;
        int count = procApprovalElementDescs.size();
        for (ApprovalElement approvalElement : approvalElements) {
            added = false;
            for (ProcApprovalElementDesc item : procApprovalElementDescs) {
                if (item.getElementId().equalsIgnoreCase(approvalElement.getId())) {
                    added = true;
                    break;
                }
            }
            if (!added) {
                procApprovalElement = new ProcApprovalElement();
                procApprovalElement.setProcDefinition(procDefinition);
                procApprovalElement.setApprovalElement(approvalElement);
                procApprovalElement.setSequence(++count);
                // procApprovalElement.setVersion(this.commonDomainService.getVersionNextId());

                procApprovalElements.add(procApprovalElement);
            }
        }
        if (procApprovalElements.size() > 0) {
            this.procApprovalElementRepository.save(procApprovalElements);
        }
    }

    @Override
    @Transactional
    public void deleteProcApprovalElements(List<String> ids) {
        this.checkIdsNotEmpty(ids);
        List<ProcApprovalElement> procApprovalElements = this.procApprovalElementRepository.findAll(ids);
        this.procApprovalElementRepository.delete(procApprovalElements);
    }

    @Override
    @Transactional
    public void updateProcApprovalElementSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(ProcApprovalElement.class, params);
    }

    @Override
    public List<ProcApprovalElementDesc> queryProcApprovalElements(String procId, String procUnitId) {
        String sql = this.getQuerySqlByName("queryProcApprovalElements");

        Map<String, Object> params = new HashMap<String, Object>(2);

        params.put("procId", procId);
        params.put("procUnitId", procUnitId);

        @SuppressWarnings("unchecked")
        List<ProcApprovalElementDesc> result = this.generalRepository.query(sql, params);
        return result;
    }

    private void checkApprovalRuleConstraints(ApprovalRule approvalRule) {
        String sql = this.getQuerySqlByName("checkDuplicate");

        Map<String, Object> params = new HashMap<String, Object>(5);
        params.put("id", approvalRule.isNew() ? "@" : approvalRule.getId());
        params.put("parentId", approvalRule.getParentId());
        params.put("name", approvalRule.getName().toUpperCase());
        params.put("procId", approvalRule.getProcId());
        params.put("procUnitId", approvalRule.getProcUnitId());

        @SuppressWarnings("unchecked")
        List<ApprovalRule> duplicateApprovalRules = this.generalRepository.query(sql, params);

        if (duplicateApprovalRules.size() > 0) {
            approvalRule.checkConstraints(duplicateApprovalRules.get(0));
        }
    }

    @Override
    @Transactional
    public String insertApprovalRule(ApprovalRule approvalRule) {
        Assert.notNull(approvalRule, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "approvalRule"));

        checkApprovalRuleConstraints(approvalRule);
        approvalRule.setCode(approvalRule.getName());
        approvalRule.buildRuleScopes();

        approvalRule = (ApprovalRule) this.commonDomainService.saveTreeEntity(approvalRule, this.approvalRuleRepository, null, false, false);
        return approvalRule.getId();
    }

    @Override
    @Transactional
    public void updateApprovalRule(ApprovalRule approvalRule) {
        Assert.notNull(approvalRule, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "approvalRule"));

        ApprovalRule dbApprovalRule = this.loadApprovalRule(approvalRule.getId());
        Assert.notNull(dbApprovalRule,
                MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, approvalRule.getId(), approvalRule.getClass().getName()));
        String oldName = dbApprovalRule.getName();
        dbApprovalRule.fromEntity(approvalRule);
        dbApprovalRule.buildRuleScopes();

        checkApprovalRuleConstraints(dbApprovalRule);
        this.commonDomainService.saveTreeEntity(dbApprovalRule, this.approvalRuleRepository, oldName, false, false);
    }

    @Override
    public ApprovalRule loadApprovalRule(String id) {
        this.checkIdNotBlank(id);
        return this.approvalRuleRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteApprovalRules(List<String> ids) {
        List<ApprovalRule> approvalRules = this.approvalRuleRepository.findAll(ids);
        int count;
        for (ApprovalRule approvalRule : approvalRules) {
            count = this.approvalRuleRepository.countByParentId(approvalRule.getId());
            Assert.isTrue(count == 0, MessageSourceContext.getMessage(MessageConstants.CAN_NOT_DELETE_HAS_CHILDREN, approvalRule.getName()));
            count = approvalRuleRepository.countApprovalRuleElements(approvalRule.getId());
            Assert.isTrue(count == 0, String.format("审批规则“%s”有审批要素，不能删除。", approvalRule.getName()));
            count = approvalRuleRepository.countApprovalRuleHandlers(approvalRule.getId());
            Assert.isTrue(count == 0, String.format("审批规则“%s”有审批处理人，不能删除。", approvalRule.getName()));
        }
        this.approvalRuleRepository.delete(approvalRules);
    }

    @Override
    @Transactional
    public void moveApprovalRules(String id, String parentId) {
        this.checkIdNotBlank(id);
        this.checkParentIdNotBlank(parentId);

        ApprovalRule approvalRule = this.approvalRuleRepository.findOne(id);
        Assert.notNull(approvalRule, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, id, "审批规则数据。"));

        ApprovalRule parent = this.approvalRuleRepository.findOne(parentId);
        Assert.notNull(approvalRule, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, parentId, "审批规则数据。"));

        String oldFullName = approvalRule.getFullName();
        String oldFullId = approvalRule.getFullId();
        approvalRule.buildFullIdAndName(parent);
        approvalRule.setParentId(parentId);

        this.approvalRuleRepository.save(approvalRule);
        String jpql = this.getQuerySqlByName("updateChildrenFullName");
        jpql = String.format(jpql, CommonTree.class.getSimpleName());

        Map<String, Object> params = new HashMap<String, Object>(3);

        params.put("fullId", oldFullId + "/%");
        params.put("oldFullId", oldFullId);
        params.put("oldFullName", oldFullName);
        params.put("newFullId", approvalRule.getFullId());
        params.put("newFullName", approvalRule.getFullName());

        this.generalRepository.updateByNativeSql(jpql, params);
    }

    @Override
    public Map<String, Object> queryApprovalRules(String orgId, String procId, String procUnitId, String procUnitName, String parentId) {
        String sql = this.getQuerySqlByName("queryApprovalRules");

        if (parentId.equalsIgnoreCase("0")) {
            int childrenCount = (int) this.approvalRuleRepository.countChildrenForRuleConfig(orgId, procId, procUnitId, ApprovalRule.ROOT_ID);
            Map<String, Object> root = new HashMap<String, Object>(8);
            root.put("parentId", 0);
            root.put("id", 1);
            root.put("procId", procId);
            root.put("procName", StringUtil.decode(procUnitName));
            root.put("name", procUnitName);
            root.put("status", ValidStatus.ENABLED.getId());
            root.put("nodeKindId", ApprovalRule.NodeKind.CATEGORY.getId());
            root.put("hasChildren", childrenCount);
            root.put("isexpand", false);

            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>(1);

            data.add(root);

            Map<String, Object> rows = new HashMap<String, Object>();
            rows.put(Constants.ROWS, data);
            return rows;
        }

        QueryModel queryModel = new QueryModel();
        queryModel.setSql(sql);
        queryModel.putParam("orgId", orgId);
        queryModel.putParam("procId", procId);
        queryModel.putParam("procUnitId", procUnitId);
        queryModel.putParam("parentId", parentId);
        queryModel.setSortFieldName("priority");
        queryModel.setSortOrder("asc");
        return this.sqlExecutorDao.executeQuery(queryModel);

    }

    @Override
    public List<ApprovalRule> queryApprovalRules(String procId, String procUnitId, String parentId) {
        Assert.hasLength(procId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "procId"));
        Assert.hasLength(procUnitId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "procUnitId"));
        Assert.hasLength(parentId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "parentId"));
        return this.approvalRuleRepository.findApprovalRules(procId, procUnitId, parentId);
    }

    @Override
    public void saveApprovalRuleElements(String approvalRuleId, List<ApprovalRuleElement> approvalRuleElements) {
        Assert.hasText(approvalRuleId, "参数approvalRuleId不能为空。");
        Assert.notEmpty(approvalRuleElements, "参数approvalRuleElements不能为空。");

        ApprovalRule approvalRule = this.loadApprovalRule(approvalRuleId);
        Assert.notNull(approvalRule, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, approvalRuleId, "审批规则"));

        approvalRule.setInputApprovalRuleElements_(approvalRuleElements);
        approvalRule.buildRuleElements();
        this.approvalRuleRepository.save(approvalRule);
    }

    @Override
    public void deleteApprovalRuleElements(String approvalRuleId, List<String> ids) {
        Assert.notNull(approvalRuleId, "参数approvalRuleId不能为空。");
        this.checkIdsNotEmpty(ids);
        ApprovalRule approvalRule = this.approvalRuleRepository.findOne(approvalRuleId);
        Assert.notNull(approvalRule, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, approvalRuleId, "审批规则"));

        approvalRule.removeDetails(approvalRule.getApprovalRuleElements(), ids);

        this.approvalRuleRepository.save(approvalRule);
    }

    @Override
    public Map<String, Object> queryApprovalRuleElements(String approvalRuleId, EmptyQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "approvalRule");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest, "queryApprovalRuleElements");
        queryModel.putParam("approvalRuleId", approvalRuleId);
        Map<String, Object> data = sqlExecutorDao.executeQuery(queryModel);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rows = (List<Map<String, Object>>) data.get(Constants.ROWS);
        for (Map<String, Object> row : rows) {
            // 操作符
            String foperator = ClassHelper.convert(row.get("foperator"), String.class);
            String foperatorName = ApprovalRuleElement.OperatorKind.fromId(foperator).getDisplayName();
            row.put("foperatorName", foperatorName);
        }

        return data;
    }

    @Override
    public List<Map<String, Object>> queryApprovalRuleElementsForFlowChart(String approvalRuleId) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "approvalRuleElement");
        String sql = queryDescriptor.getSqlByName("queryForFlowChart");
        return this.sqlExecutorDao.queryToListMap(sql, approvalRuleId);
    }

    @Override
    @Transactional
    public void saveApprovalRuleHandlers(String approvalRuleId, List<ApprovalRuleHandler> approvalRuleHandlers) {
        Assert.hasText(approvalRuleId, "参数approvalRule不能为空。");
        Assert.notEmpty(approvalRuleHandlers, "参数approvalRuleHandlers不能为空。");

        ApprovalRule approvalRule = this.loadApprovalRule(approvalRuleId);
        Assert.notNull(approvalRule, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, approvalRuleId, "审批规则"));

        approvalRule.setInputApprovalRulHandlers_(approvalRuleHandlers);
        approvalRuleHandlers = approvalRule.getInputApprovalRulHandlers_();
        approvalRule.addApprovalRuleHandlers(approvalRuleHandlers);
        this.approvalRuleRepository.save(approvalRule);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveApprovalRuleHandler(String approvalRuleId, ApprovalRuleHandler approvalRuleHandler, TaskExecuteMode taskExecuteMode,
                                        List<ApprovalRuleHandlerAssist> assistants, List<ApprovalRuleHandlerAssist> ccs,
                                        List<ApprovalRuleHandlerUIElmentPermission> fieldAuthorizations) {
        saveApprovalRuleHandler(approvalRuleId, approvalRuleHandler, taskExecuteMode, null, assistants, ccs, fieldAuthorizations);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void saveApprovalRuleHandler(String approvalRuleId, ApprovalRuleHandler approvalRuleHandler, TaskExecuteMode taskExecuteMode, Integer limitHandler, List<ApprovalRuleHandlerAssist> assistants, List<ApprovalRuleHandlerAssist> ccs, List<ApprovalRuleHandlerUIElmentPermission> fieldAuthorizations) {
        Assert.hasText(approvalRuleId, "参数approvalRuleId不能为空。");
        Assert.notNull(approvalRuleHandler, "参数approvalRuleHandler不能为空。");

        ApprovalRule approvalRule = this.loadApprovalRule(approvalRuleId);

        ApprovalRuleHandler sourceApprovalRuleHandler = approvalRule.findApprovalRuleHandler(approvalRuleHandler.getId());

        ApprovalRuleHandlerGroup approvalRuleHandlerGroup = approvalRule.findApprovalRuleHandlerGroup(sourceApprovalRuleHandler.getGroupId());
        sourceApprovalRuleHandler.fromEntity(approvalRuleHandler);

        if(taskExecuteMode == null && limitHandler == null) {
            if (approvalRuleHandlerGroup != null) {
                approvalRule.getApprovalRuleHandlerGroups().remove(approvalRuleHandlerGroup);
            }
        }

        if (taskExecuteMode == null) {
            if (approvalRuleHandlerGroup != null) {
                approvalRuleHandlerGroup.setTaskExecuteMode(null);
            }
        } else {
            if (approvalRuleHandlerGroup == null) {
                approvalRuleHandlerGroup = new ApprovalRuleHandlerGroup();
                approvalRule.getApprovalRuleHandlerGroups().add(approvalRuleHandlerGroup);
            }
            approvalRuleHandlerGroup.setGroupId(sourceApprovalRuleHandler.getGroupId());
            approvalRuleHandlerGroup.setTaskExecuteMode(taskExecuteMode);
        }

        if(limitHandler == null) {
            if(approvalRuleHandlerGroup != null) {
                approvalRuleHandlerGroup.setLimitHandler(null);
            }
        } else {
            if (approvalRuleHandlerGroup == null) {
                approvalRuleHandlerGroup = new ApprovalRuleHandlerGroup();
                approvalRule.getApprovalRuleHandlerGroups().add(approvalRuleHandlerGroup);
            }
            approvalRuleHandlerGroup.setGroupId(sourceApprovalRuleHandler.getGroupId());
            approvalRuleHandlerGroup.setLimitHandler(limitHandler);
        }


        // 协审 抄送
        sourceApprovalRuleHandler.getAssists().clear();
        if (assistants != null) {
            sourceApprovalRuleHandler.getAssists().addAll(assistants);
        }
        if (ccs != null) {
            sourceApprovalRuleHandler.getAssists().addAll(ccs);
        }
        // 权限字段
        sourceApprovalRuleHandler.getUIElmentPermissions().clear();
        if (fieldAuthorizations != null) {
            sourceApprovalRuleHandler.getUIElmentPermissions().addAll(fieldAuthorizations);
        }
        this.commonDomainService.saveTreeEntity(approvalRule, this.approvalRuleRepository, approvalRule.getName(), false);
    }

    @Override
    public void deleteApprovalRuleHandlers(String approvalRuleId, List<String> ids) {
        Assert.hasText(approvalRuleId, "参数approvalRuleId不能为空。");
        this.checkIdsNotEmpty(ids);
        ApprovalRule approvalRule = this.approvalRuleRepository.findOne(approvalRuleId);

        Assert.notNull(approvalRule, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, approvalRuleId, "审批规则"));
        approvalRule.removeDetails(approvalRule.getApprovalRuleHandlers(), ids);
        // this.commonDomainService.deleteDetailEntities(approvalRule.getApprovalRuleHandlers(), ids);
        // 删除流程审批人时同步修改 wf_procunithandler 中 approval_rule_handler_id 为空
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "approvalRule");
        String sql = queryDescriptor.getSqlByName("queryProcunithandlerByIdAndNotEnd");
        for (String id : ids) {
            List<String> procunithandlerIds = this.sqlExecutorDao.queryToList(sql, String.class, id);
            if (procunithandlerIds != null && procunithandlerIds.size() > 0) {
                List<Object[]> updateIds = new ArrayList<>(procunithandlerIds.size());
                for (String procunithandlerId : procunithandlerIds) {
                    updateIds.add(new Object[]{procunithandlerId});
                }
                if (updateIds.size() > 0) {
                    this.sqlExecutorDao.batchUpdate("update wf_procunithandler t set t.approval_rule_handler_id='' where t.id=?", updateIds);
                }
            }
        }
        this.approvalRuleRepository.save(approvalRule);
    }

    @Override
    public Map<String, Object> queryApprovalRuleHandlers(String approvalRuleId, EmptyQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "approvalRule");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest, "queryApprovalRuleHandlers");
        queryModel.putParam("approvalRuleId", approvalRuleId);
        return sqlExecutorDao.executeQuery(queryModel);
    }

    private Map<String, Object> internalQueryApprovalRuleHanderChildren(String approvalRuleHandlerId, String cooperationModelKind) {
        QueryModel queryModel = new QueryModel();
        queryModel.setSql(this.getQuerySqlByName("queryHandlerChildren"));
        queryModel.putParam("approvalRuleHandlerId", approvalRuleHandlerId);
        queryModel.putParam("kindId", cooperationModelKind);
        return sqlExecutorDao.executeQuery(queryModel);
    }

    @Override
    public Map<String, Object> queryAssistantHandlers(String approvalRuleHandlerId) {
        return internalQueryApprovalRuleHanderChildren(approvalRuleHandlerId, CooperationModelKind.ASSISTANT);
    }

    @Override
    public Map<String, Object> queryCCHandlers(String approvalRuleHandlerId) {
        return internalQueryApprovalRuleHanderChildren(approvalRuleHandlerId, CooperationModelKind.CC);
    }

    @Override
    public Map<String, Object> queryUIElementPermissions(String approvalRuleHandlerId) {
        QueryModel queryModel = new QueryModel();
        queryModel.setSql(this.getQuerySqlByName("queryUIElementPermissions"));
        queryModel.putParam("approvalRuleHandlerId", approvalRuleHandlerId);
        return sqlExecutorDao.executeQuery(queryModel);
    }

    @Override
    public LimitTime getApprovalRuleHandlerLimitTime(String procId, String procUnitId, String approvalRuleHandlerId) {
        LimitTime result = new LimitTime(0, 0);
        // 从规则中读取是计时信息
        if (StringUtil.isNotBlank(approvalRuleHandlerId)) {
            ApprovalRuleHandler approvalRuleHandler = this.loadApprovalRuleHandler(approvalRuleHandlerId);
            if (approvalRuleHandler != null) {
                result.setNeedTiming(approvalRuleHandler.getNeedTiming());
                if (result.getNeedTiming() == 1) {
                    result.setLimitTime(approvalRuleHandler.getLimitTime());
                    return result;
                }
            }
        }
        // 从环节中读取计时信息
        ProcDefinition procUnitDefinition = this.procDefinitionRespository.findByProcIdAndCode(procId, procUnitId);
        if (procUnitDefinition != null && procUnitDefinition.getNeedTiming() == 1) {
            result.setNeedTiming(procUnitDefinition.getNeedTiming());
            if (result.getNeedTiming() == 1) {
                result.setLimitTime(procUnitDefinition.getLimitTime());
                return result;
            }
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ApprovalRuleHandler loadApprovalRuleHandler(String approvalRuleHandlerId) {
        // 审批规则有可能删除
        String sql = this.getQuerySqlByName("queryApprovalHandlerById");
        List<ApprovalRuleHandler> approvalRuleHandlers;
        approvalRuleHandlers = this.generalRepository.query(sql, QueryParameter.buildParameters("id", approvalRuleHandlerId));
        if (approvalRuleHandlers.size() == 1) {
            return approvalRuleHandlers.get(0);
        }
        return null;
    }

    @Override
    public Long countApprovalRule() {
        return this.approvalRuleRepository.count();
    }

    @Override
    public Map<String, Object> queryApprovalRuleApply(String procId, String procUnitId, Map<String, Object> bizProcessParams) {
        throw new NotImplementedException();
    }

    @Override
    @Transactional
    public void deleteApprovalRuleScopes(String approvalRuleId, List<String> ids) {
        Assert.notNull(approvalRuleId, "参数approvalRuleId不能为空。");
        this.checkIdsNotEmpty(ids);
        ApprovalRule approvalRule = this.approvalRuleRepository.findOne(approvalRuleId);
        Assert.notNull(approvalRule, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, approvalRuleId, "审批规则"));
        approvalRule.removeDetails(approvalRule.getApprovalRuleScopes(), ids);
        this.approvalRuleRepository.save(approvalRule);
    }

    @Override
    public Map<String, Object> queryApprovalRuleScopes(String approvalRuleId, EmptyQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "approvalRule");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest, "queryApprovalRuleScopes");
        queryModel.putParam("approvalRuleId", approvalRuleId);
        Map<String, Object> data = sqlExecutorDao.executeQuery(queryModel);
        return data;
    }

    @Override
    public Map<String, Object> synApprovalRule(String orgId, String procId, Long approveRuleId) {
        throw new NotImplementedException();
    }

}
