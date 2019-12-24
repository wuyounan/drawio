package com.huigou.uasp.bpm.managment.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.domain.ValidStatus;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bpm.ActivityKind;
import com.huigou.uasp.bpm.MergeHandlerKind;
import com.huigou.uasp.bpm.engine.application.WorkflowApplication;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnit;
import com.huigou.uasp.bpm.managment.application.ProcDefinitionApplication;
import com.huigou.uasp.bpm.managment.domain.model.ProcDefinition;
import com.huigou.uasp.bpm.managment.repository.ProcDefinitionRespository;

import javax.persistence.criteria.Predicate;

@Service("procDefinitionApplication")
public class ProcDefinitionApplicationImpl extends BaseApplication implements ProcDefinitionApplication {

    @Autowired
    private ProcDefinitionRespository procDefinitionRespository;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private WorkflowApplication workflowService;

    @Override
    @Transactional
    public String insertProcDefinition(ProcDefinition procDefinition) {
        procDefinition = (ProcDefinition) this.commonDomainService.saveTreeEntity(procDefinition, procDefinitionRespository, true, false);
        return procDefinition.getId();
    }

    @Override
    @Transactional
    public void updateProcDefinition(ProcDefinition procDefinition) {
        Assert.notNull(procDefinition, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "procDefinition"));
        ProcDefinition dbProcDefinition = this.loadProcDefinition(procDefinition.getId());
        Assert.notNull(dbProcDefinition,
                MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, procDefinition.getId(), procDefinition.getClass().getName()));

        String oldName = dbProcDefinition.getName();
        dbProcDefinition.fromEntity(procDefinition);
        this.commonDomainService.saveTreeEntity(dbProcDefinition, procDefinitionRespository, oldName, true, false);
    }

    @Override
    public ProcDefinition loadProcDefinition(String id) {
        this.checkIdNotBlank(id);
        return this.procDefinitionRespository.findOne(id);
    }

    @Override
    public ProcDefinition loadProcDefinitionByProcId(String procId) {
        Assert.hasText(procId, "参数procId不能为空");
        ProcDefinition procDefinition = this.procDefinitionRespository.findProc(procId);
        return procDefinition;
    }

    @Override
    public ProcDefinition loadProcDefinitionByProcAndProcUnitId(String procId, String procUnitId) {
        Assert.hasText(procId, "参数procId不能为空");
        Assert.hasText(procUnitId, "参数procUnitId不能为空");
        ProcDefinition procDefinition = this.procDefinitionRespository.findProcUnit(procId, procUnitId);
        return procDefinition;
    }

    @Override
    @Transactional
    public void deleteProcDefinitions(List<String> ids) {
        this.checkIdsNotEmpty(ids);

        long childrenCount;
        List<ProcDefinition> procDefinitions = this.procDefinitionRespository.findAll(ids);
        Set<String> parentIds = new HashSet<String>(ids.size());
        for (ProcDefinition procDefinition : procDefinitions) {
            childrenCount = this.procDefinitionRespository.countByParentId(procDefinition.getId());
            Assert.isTrue(childrenCount == 0L, MessageSourceContext.getMessage(MessageConstants.CAN_NOT_DELETE_HAS_CHILDREN, procDefinition.getName()));

            parentIds.add(procDefinition.getParentId());
        }

        this.procDefinitionRespository.delete(procDefinitions);
    }

    @Override
    public Map<String, Object> queryProcDefinitions(boolean includeProcUnit, ParentAndCodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "procDefinition");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        if (!includeProcUnit) {
            queryModel.addCriteria(" and ((case when node_kind_id is null then 'folder' else node_kind_id end) in (:nodeKindId1, :nodeKindId2))");
            queryModel.putParam("nodeKindId1", "folder");
            queryModel.putParam("nodeKindId2", "proc");
        }
        queryModel.putDictionary("mergeHandlerKind", MergeHandlerKind.getMap());
        return this.sqlExecutorDao.executeQuery(queryModel);
    }


    @Override
    public Map<String, Object> findProcDefinitions(boolean includeProcUnit, ParentAndCodeAndNameQueryRequest queryRequest) {
        Specification<ProcDefinition> spec = (root, query, cb) -> {
            ArrayList<Predicate> predicates = new ArrayList<>(10);
            if (queryRequest.getParentId() != null) {
                predicates.add(cb.equal(root.get("parentId"), queryRequest.getParentId()));
            }
            if (queryRequest.getCode() != null) {
                predicates.add(cb.like(root.get("code"), String.join("", "%", queryRequest.getCode(), "%")));
            }
            if (queryRequest.getName() != null) {
                predicates.add(cb.like(root.get("name"), String.join("", "%", queryRequest.getCode(), "%")));
            }
            if (!includeProcUnit) {
                predicates.add(cb.coalesce(root.get("nodeKindId"), "folder").in("folder", "proc"));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        PageRequest pageRequest = null;
        if (queryRequest.getPageModel() != null) {
            pageRequest = new PageRequest(queryRequest.getPageModel().getPageIndex() - 1, queryRequest.getPageModel().getPageSize());
        }
        Page<ProcDefinition> page = procDefinitionRespository.findAll(spec, pageRequest);
        List<Map<String, Object>> rows = page.getContent()
                .stream()
                .map(def -> {
                    Map<String, Object> map = ClassHelper.beanToMap(def);
                    map.put("hasChildren", procDefinitionRespository.countByParentId(def.getId()));
                    map.put("mergeHandlerKindTextView", MergeHandlerKind.fromId(def.getMergeHandlerKind()).getDisplayName());
                    return map;
                })
                .collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>(3);
        result.put(Constants.ROWS, rows);
        result.put("Total", page.getTotalElements());
        result.put("page", page.getNumber() + 1);
        return result;
    }

    @Override
    public Map<String, Object> slicedQueryProcDefinitions(ParentIdQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "procDefinition");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Integer getProcDefinitionNextSequence(String parentId) {
        return this.commonDomainService.getNextSequence(ProcDefinition.class, CommonDomainConstants.PARENT_ID_FIELD_NAME, parentId);
    }

    @Override
    @Transactional
    public void updateProcDefinitionSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(ProcDefinition.class, params);

    }

    @Override
    @Transactional
    public void moveProcDefinitions(List<String> ids, String parentId) {
        this.commonDomainService.moveForTree(ProcDefinition.class, ids, CommonDomainConstants.PARENT_ID_COLUMN_NAME, parentId);
    }

    @Override
    @Transactional
    public void importProcUnits(String parentId) {
        this.checkParentIdNotBlank(parentId);

        ProcDefinition parent = this.procDefinitionRespository.findOne(parentId);
        this.checkItemNotBlank(parent.getProcId(), "流程定义未关联Activiti流程模板。");

        Assert.notNull(parent, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, parentId, "流程定义"));

        ProcessDefinition lastVersionProcessDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(parent.getProcId())
                .latestVersion().singleResult();
        Assert.notNull(lastVersionProcessDefinition, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, parent.getProcId(), "流程模板"));

        List<ProcUnit> userTaskActivities = this.workflowService.getUserTaskActivitiesByProcessDefinitionId(lastVersionProcessDefinition.getId());
        int sequence = 0;
        List<ProcDefinition> procDefinitions = new ArrayList<ProcDefinition>(userTaskActivities.size());
        ProcDefinition procDefinition;
        for (ProcUnit procUnit : userTaskActivities) {
            procDefinition = new ProcDefinition();

            procDefinition.setParentId(parentId);
            procDefinition.setCode(procUnit.getId());
            procDefinition.setName(procUnit.getName());
            procDefinition.setProcId(parent.getProcId());
            procDefinition.setProcName(parent.getProcName());
            procDefinition.setStatus(ValidStatus.ENABLED.getId());
            procDefinition.setNodeKindId("procUnit");
            procDefinition.setNeedTiming(1);
            procDefinition.setPreviewHandler(0);
            procDefinition.setSequence(++sequence);
            procDefinition.setAssistantMustApprove(1);// 协审必须审批默认为是
            procDefinition.setMergeHandlerKind(MergeHandlerKind.ADJACENT.getId());// 合并处理人默认相邻合并
            procDefinitions.add(procDefinition);
        }

        List<ProcDefinition> deleteProcDefinitions = procDefinitionRespository.findByParentId(parentId);
        procDefinitionRespository.delete(deleteProcDefinitions);
        procDefinitions = procDefinitionRespository.save(procDefinitions);
        for (ProcDefinition item : procDefinitions) {
            item.buildFullIdAndName(parent);
        }
        procDefinitionRespository.save(procDefinitions);
    }

    @Override
    @Transactional
    public void bindActivitiProcDefinition(String id, String procId) {
        this.checkIdNotBlank(id);
        Assert.hasText(procId, "参数procId不能为空。");

        List<ProcDefinition> otherBindProcDefinitions = this.procDefinitionRespository.findOtherBindProcs(id, procId);

        ProcessDefinition lastVersionProcessDefinition = findActivitiLastVersionProcessDefinition(procId);
        Assert.notNull(lastVersionProcessDefinition, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, id, "流程定义"));

        if (otherBindProcDefinitions.size() > 0) {
            Assert.isTrue(false, String.format("流程“%s”已被“%s” 连接，操作失败。", lastVersionProcessDefinition.getName(), otherBindProcDefinitions.get(0).getName()));
        }

        ProcDefinition procDefinition = this.procDefinitionRespository.findOne(id);
        Assert.notNull(procDefinition, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, id, "流程定义"));

        procDefinition.setProcId(procId);
        procDefinition.setProcName(lastVersionProcessDefinition.getName());

        this.procDefinitionRespository.save(procDefinition);
    }

    @Override
    public List<Map<String, Object>> queryOneLevelProcDefinitions() {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "procDefinition");
        String sql = queryDescriptor.getSqlByName("queryOneLevel");

        List<Map<String, Object>> result = this.sqlExecutorDao.queryToListMap(sql, ProcDefinition.ROOT_ID);

        Map<String, Object> addtion = new HashMap<String, Object>(2);
        addtion.put("fullId", "All");
        addtion.put("name", "全部");
        result.add(addtion);

        addtion = new HashMap<String, Object>(2);
        addtion.put("fullId", "NotProcTask");
        addtion.put("name", "非流程任务");
        result.add(addtion);

        return result;
    }

    private ProcessDefinition findActivitiLastVersionProcessDefinition(String processDefinitionId) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionId).latestVersion().singleResult();
    }

    @Override
    public List<Map<String, Object>> queryProcUnitsFromActiviti(String processDefinitionKey) {
        Assert.notNull(processDefinitionKey, "参数processDefinitionKey不能为空。");
        // 从流程加载子结点
        ProcessDefinition lastVersionProcessDefinition = findActivitiLastVersionProcessDefinition(processDefinitionKey);

        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(lastVersionProcessDefinition.getId());

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        if (processDefinition != null) {
            for (ActivityImpl activityItem : processDefinition.getActivities()) {
                if (ActivityKind.USER_TASK.equals(activityItem.getProperty("type"))) {
                    Map<String, Object> procUnitMap = new HashMap<String, Object>();
                    procUnitMap.put("procId", processDefinition.getId());
                    procUnitMap.put("procUnitId", activityItem.getId());
                    procUnitMap.put("name", activityItem.getProperty("name"));
                    procUnitMap.put("parentId", processDefinition.getId());
                    procUnitMap.put("hasChildren", 0);
                    procUnitMap.put("isExpand", false);
                    procUnitMap.put("nodeKindId", 2);
                    procUnitMap.put("key", processDefinitionKey);
                    result.add(procUnitMap);
                }
            }
        }
        return result;
    }

    @Override
    public List<ProcDefinition> queryProcUnitsForSequence(String processDefinitionKey) {
        Assert.hasText(processDefinitionKey, "参数processDefinitionKey不能为空。");
        return this.procDefinitionRespository.findProcUnitsForSequnce(processDefinitionKey);
    }
}
