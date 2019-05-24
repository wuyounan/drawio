package com.huigou.uasp.bmp.bizconfig.process.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.data.domain.EntityUtil;
import com.huigou.data.domain.model.BaseInfoStatus;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.bizconfig.model.ObjectKindEnum;
import com.huigou.uasp.bmp.bizconfig.process.application.BizBusinessProcessApplication;
import com.huigou.uasp.bmp.bizconfig.process.domain.model.BpmBusinessProcess;
import com.huigou.uasp.bmp.bizconfig.process.domain.query.BusinessProcessQueryRequest;
import com.huigou.uasp.bmp.bizconfig.process.repository.BpmBusinessProcessRepository;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.util.StringUtil;

/**
 * 流程定义应用实现
 *
 * @author
 * @version V1.0
 * @ClassName: FlowsheetApplicationImpl
 * @date 2017-04-07 14:00
 */
@Service("bizBusinessProcessApplication")
public class BizBusinessProcessApplicationImpl extends BaseApplication implements BizBusinessProcessApplication {
    @Autowired
    private BpmBusinessProcessRepository bpmBusinessProcessRepository;

    @Override
    @Transactional
    public String insertBusinessProcess(BpmBusinessProcess businessProcess) {
        Assert.notNull(businessProcess, CommonDomainConstants.OBJECT_NOT_NULL);
        businessProcess.setStatus(BaseInfoStatus.ENABLED.getId());
        if (businessProcess.getIsFinal() == 1) {
            businessProcess.setObjectKindCode(ObjectKindEnum.FLOW.getId());
        } else {
            businessProcess.setIsFinal(0);
            businessProcess.setObjectKindCode(ObjectKindEnum.FLOW_DOMAIN.getId());
        }
        businessProcess.setIsFlowChart(0);
        businessProcess = (BpmBusinessProcess) this.commonDomainService.saveTreeEntity(businessProcess, bpmBusinessProcessRepository,
                                                                                       businessProcess.getName(), true);
        return businessProcess.getId();
    }

    @Override
    @Transactional
    public BpmBusinessProcess updateBusinessProcess(BpmBusinessProcess businessProcess) {
        Assert.notNull(businessProcess, CommonDomainConstants.OBJECT_NOT_NULL);
        BpmBusinessProcess oldBusinessProcess = bpmBusinessProcessRepository.findOne(businessProcess.getId());
        Assert.notNull(oldBusinessProcess, CommonDomainConstants.LOAD_OBJECT_IS_NULL);
        String oldName = oldBusinessProcess.getName();
        oldBusinessProcess.fromEntity(businessProcess);
        return (BpmBusinessProcess) this.commonDomainService.saveTreeEntity(oldBusinessProcess, bpmBusinessProcessRepository, oldName, true);
    }

    @Override
    @Transactional
    public void moveBusinessProcesses(String parentId, List<String> ids) {
        Assert.hasText(parentId, "参数parentId不能为空。");
        this.checkIdsNotEmpty(ids);
        String parentFullId = "";
        String parentFullName = "";
        String parentName = "全部";
        if (!parentId.equals(CommonDomainConstants.DEFAULT_ROOT_PARENT_ID)) {// 移动到根节点
            BpmBusinessProcess moveToParent = bpmBusinessProcessRepository.findOne(parentId);
            parentFullId = moveToParent.getFullId();
            parentFullName = moveToParent.getFullName();
            parentName = moveToParent.getName();
        }
        Assert.hasText(parentId, "参数parentId不能为空。");
        this.checkIdsNotEmpty(ids);
        String tableName = EntityUtil.getTableName(BpmBusinessProcess.class);
        String sql = this.commonDomainService.getSqlByName("updateFullIdAndName");
        sql = String.format(sql, tableName);
        List<BpmBusinessProcess> businessProcesses = bpmBusinessProcessRepository.findAll(ids);
        BpmBusinessProcess oldParent = null;
        String chlidFullId = null;
        String oldParentFullId = null;
        String oldParentFullName = null;
        String newParentFullId = null;
        String newParentFullName = null;
        for (BpmBusinessProcess businessProcess : businessProcesses) {
            chlidFullId = businessProcess.getFullId();
            Assert.isTrue(parentFullId.indexOf(chlidFullId) == -1, String.format("[%s]无法移动到[%s]", new Object[] { businessProcess.getName(), parentName }));
            oldParent = bpmBusinessProcessRepository.findOne(businessProcess.getParentId());
            if (oldParent == null) {
                oldParentFullId = businessProcess.getFullId();
                oldParentFullName = businessProcess.getFullName();
                newParentFullId = parentFullId + oldParentFullId;
                newParentFullName = parentFullName + oldParentFullName;
            } else {
                oldParentFullId = oldParent.getFullId();
                oldParentFullName = oldParent.getFullName();
                newParentFullId = parentFullId;
                newParentFullName = parentFullName;
            }
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("parentNewFullId", newParentFullId);
            param.put("parentOldFullId", oldParentFullId);
            param.put("parentNewFullName", newParentFullName);
            param.put("parentOldFullName", oldParentFullName);
            param.put("likeFullId", chlidFullId + "%");
            this.sqlExecutorDao.executeUpdateByMapParam(String.format(sql, new Object[] { tableName }), param);
        }
        sql = this.commonDomainService.getSqlByName("moveSqlByParentId");
        sql = String.format(sql, tableName, CommonDomainConstants.PARENT_ID_COLUMN_NAME);
        Map<String, Object> parameterMap = new HashMap<String, Object>(2);
        parameterMap.put(CommonDomainConstants.PARENT_ID_FIELD_NAME, parentId);
        parameterMap.put(CommonDomainConstants.IDS_FIELD_NAME, ids);
        this.generalRepository.updateByNativeSql(sql, parameterMap);
    }

    @Override
    @Transactional
    public void updateBusinessProcessesSequence(String parentId, Map<String, Integer> params) {
        Assert.notNull(params, CommonDomainConstants.OBJECT_NOT_NULL);
        this.commonDomainService.updateSequence(BpmBusinessProcess.class, params);
        // 重构排序号
        List<BpmBusinessProcess> list = bpmBusinessProcessRepository.findByParentIdOrderBySequenceAsc(parentId);
        int i = 0;
        for (BpmBusinessProcess businessProces : list) {
            i++;
            businessProces.setSequence(i * 10);
        }
        bpmBusinessProcessRepository.save(list);
    }

    @Override
    @Transactional
    public void deleteBusinessProcesses(List<String> ids) {
        this.checkIdsNotEmpty(ids);
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "businessProcess");
        List<BpmBusinessProcess> businessProcesses = bpmBusinessProcessRepository.findAll(ids);
        String sql = queryDescriptor.getSqlByName("queryFlowChartProcessByFullId");
        // 校验流程下是否存在流程图
        for (BpmBusinessProcess businessProcess : businessProcesses) {
            List<Map<String, Object>> list = this.sqlExecutorDao.queryToListMap(sql, businessProcess.getFullId() + "%");
            if (list != null && list.size() > 0) {
                Assert.isTrue(false, String.format("[%s]存在流程图不能删除", businessProcess.getName()));
            }
            bpmBusinessProcessRepository.delete(businessProcess);
        }
    }

    @Override
    public BpmBusinessProcess loadBusinessProcess(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        BpmBusinessProcess businessProcess = bpmBusinessProcessRepository.findOne(id);
        Assert.notNull(businessProcess, CommonDomainConstants.LOAD_OBJECT_IS_NULL);
        businessProcess.setHasChildren(this.bpmBusinessProcessRepository.countByParentId(businessProcess.getId()));
        BpmBusinessProcess parentFunction = bpmBusinessProcessRepository.findOne(businessProcess.getParentId());
        if (parentFunction != null) {
            businessProcess.setParentName(parentFunction.getName());
        }
        return businessProcess;
    }

    @Override
    public Map<String, Object> slicedQueryBusinessProcesses(BusinessProcessQueryRequest queryRequest) {
        Assert.notNull(queryRequest, CommonDomainConstants.OBJECT_NOT_NULL);
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "businessProcess");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public List<Map<String, Object>> queryBusinessProcesses(String parentId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "businessProcess");
        String sql = queryDescriptor.getSqlByName("queryBusinessProcess");
        if (StringUtil.isBlank(parentId)) {
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("code", "root");
            root.put("name", "全部");
            root.put("fullId", "/");
            root.put("delay", false);
            root.put("isExpand", true);
            root.put(CommonDomainConstants.ID_FIELD_NAME, CommonDomainConstants.DEFAULT_ROOT_PARENT_ID);
            root.put(CommonDomainConstants.PARENT_ID_FIELD_NAME, "");
            root.put("hasChildren", bpmBusinessProcessRepository.countByParentId(CommonDomainConstants.DEFAULT_ROOT_PARENT_ID));
            list.add(root);
            parentId = CommonDomainConstants.DEFAULT_ROOT_PARENT_ID;
        }
        List<Map<String, Object>> datas = this.sqlExecutorDao.queryToListMap(sql, parentId);
        list.addAll(datas);
        return list;
    }

    @Override
    public List<Map<String, Object>> queryBusinessProcessesOnMove(String parentId, String excludeIds) {
        Assert.notNull(excludeIds, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "excludeIds"));
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "businessProcess");
        String sql = queryDescriptor.getSqlByName("queryMoveBusinessProcess");
        Map<String, Object> param = new HashMap<String, Object>();
        if (StringUtil.isBlank(parentId)) {
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("code", "root");
            root.put("name", "全部");
            root.put("fullId", "/");
            root.put("delay", false);
            root.put("isExpand", true);
            root.put(CommonDomainConstants.ID_FIELD_NAME, CommonDomainConstants.DEFAULT_ROOT_PARENT_ID);
            root.put(CommonDomainConstants.PARENT_ID_FIELD_NAME, "");
            root.put("hasChildren", bpmBusinessProcessRepository.countByParentId(CommonDomainConstants.DEFAULT_ROOT_PARENT_ID));
            list.add(root);
            parentId = CommonDomainConstants.DEFAULT_ROOT_PARENT_ID;
        }
        param.put("parentId", parentId);
        StringBuffer sb = new StringBuffer();
        String[] ids = excludeIds.split(",");
        int l = ids.length;
        for (int i = 0; i < l; i++) {
            sb.append(":excludeId").append(i);
            param.put("excludeId" + i, ids[i]);
            if (i < l - 1) {
                sb.append(",");
            }
        }
        sql = String.format(sql, sb.toString(), sb.toString());
        List<Map<String, Object>> datas = this.sqlExecutorDao.queryToMapListByMapParam(sql, param);
        list.addAll(datas);
        return list;
    }

    @Override
    public Integer getBusinessProcessNextSequence(String parentId) {
        Assert.notNull(parentId, CommonDomainConstants.PARENT_ID_NOT_BLANK);
        Integer sequence = this.bpmBusinessProcessRepository.getNextSequence(parentId);
        if (sequence == null) {
            sequence = 0;
        }
        return sequence + 1;
    }

    @Override
    @Transactional
    public void saveBusinessProcessAndAttribute(BpmBusinessProcess businessProcess) {
        Assert.notNull(businessProcess, CommonDomainConstants.OBJECT_NOT_NULL);
        Integer isFinal = businessProcess.getIsFinal();
        BpmBusinessProcess oldBusinessProcess = bpmBusinessProcessRepository.findOne(businessProcess.getId());
        Integer oldIsFinal = oldBusinessProcess.getIsFinal();
        Assert.notNull(oldBusinessProcess, CommonDomainConstants.LOAD_OBJECT_IS_NULL);
        String oldName = oldBusinessProcess.getName();
        oldBusinessProcess.fromEntity(businessProcess);
        // 是否末级流程标志改变
        if (isFinal.intValue() != oldIsFinal.intValue()) {
            if (isFinal.intValue() == 1) {// 流程域改为流程
                // 校验是否允许修改
                // 判断是否存在下级节点
                Integer count = bpmBusinessProcessRepository.countByParentId(oldBusinessProcess.getId());
                Assert.isTrue(count == 0, "该流程域下已存在流程定义不能修改为末端流程!");
                oldBusinessProcess.setObjectKindCode(ObjectKindEnum.FLOW.getId());
            } else {// 流程改为流程域
                Assert.isTrue(oldBusinessProcess.getIsFlowChart() == 0, "该流程已定义流程图,无法修改为流程域!");
                oldBusinessProcess.setObjectKindCode(ObjectKindEnum.FLOW_DOMAIN.getId());
            }
        }
        this.commonDomainService.saveTreeEntity(oldBusinessProcess, bpmBusinessProcessRepository, oldName, true);
    }

    @Override
    public BpmBusinessProcess loadBusinessProcessByCode(String code) {
        Assert.hasText(code, CommonDomainConstants.ID_NOT_BLANK);
        List<BpmBusinessProcess> datas = bpmBusinessProcessRepository.findByCode(code);
        Assert.notEmpty(datas, String.format("未找到[%s]对应的流程记录!", code));
        return datas.get(0);
    }
}
