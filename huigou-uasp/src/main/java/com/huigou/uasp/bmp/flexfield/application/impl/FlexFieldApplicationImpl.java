package com.huigou.uasp.bmp.flexfield.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.cache.DictUtil;
import com.huigou.context.MessageSourceContext;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.flexfield.application.FlexFieldApplication;
import com.huigou.uasp.bmp.flexfield.domain.model.FlexFieldBizGroup;
import com.huigou.uasp.bmp.flexfield.domain.model.FlexFieldBizGroupField;
import com.huigou.uasp.bmp.flexfield.domain.model.FlexFieldDefinition;
import com.huigou.uasp.bmp.flexfield.domain.model.FlexFieldStorage;
import com.huigou.uasp.bmp.flexfield.domain.query.FlexFieldBizGroupsQueryRequest;
import com.huigou.uasp.bmp.flexfield.domain.query.FlexFieldDefinitionsQueryRequest;
import com.huigou.uasp.bmp.flexfield.repository.FlexFieldBizGroupFieldRepository;
import com.huigou.uasp.bmp.flexfield.repository.FlexFieldBizGroupRepository;
import com.huigou.uasp.bmp.flexfield.repository.FlexFieldDefinitionRepository;
import com.huigou.uasp.bmp.flexfield.repository.FlexFieldStorageRepository;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.JSONUtil;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

@Service("flexFieldApplication")
public class FlexFieldApplicationImpl extends BaseApplication implements FlexFieldApplication {

    @Autowired
    private FlexFieldDefinitionRepository flexFieldDefinitionRepository;

    @Autowired
    private FlexFieldBizGroupRepository flexFieldBizGroupRepository;

    @Autowired
    private FlexFieldBizGroupFieldRepository flexFieldBizGroupFieldRepository;

    @Autowired
    private FlexFieldStorageRepository flexFieldStorageRepository;

    private void checkFlexFieldDefinitionConstraints(FlexFieldDefinition flexFieldDefinition) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "flexFieldDefinition");
        String sql = queryDescriptor.getSqlByName("checkDuplicate");
        Map<String, Object> params = new HashMap<String, Object>(3);

        params.put("id", flexFieldDefinition.isNew() ? "@" : flexFieldDefinition.getId());
        params.put("folderId", flexFieldDefinition.getFolderId());
        params.put("fieldName", flexFieldDefinition.getFieldName().toUpperCase());

        @SuppressWarnings("unchecked")
        List<FlexFieldDefinition> duplicateFlexFieldDefinitions = this.generalRepository.query(sql, params);

        if (duplicateFlexFieldDefinitions.size() > 0) {
            flexFieldDefinition.checkConstraints(duplicateFlexFieldDefinitions.get(0));
        }
    }

    @Override
    @Transactional
    public String saveFlexFieldDefinition(FlexFieldDefinition flexFieldDefinition) {
        Assert.notNull(flexFieldDefinition, "参数flexFieldDefinition不能为空。");
        flexFieldDefinition = this.commonDomainService.loadAndFillinProperties(flexFieldDefinition, FlexFieldDefinition.class);
        checkFlexFieldDefinitionConstraints(flexFieldDefinition);
        flexFieldDefinition = this.flexFieldDefinitionRepository.save(flexFieldDefinition);
        return flexFieldDefinition.getId();
    }

    @Override
    @Transactional
    public void updateFlexFieldDefinitionSequence(Map<String, Integer> params) {
        Assert.notEmpty(params, "参数params不能为空。");
        this.commonDomainService.updateSequence(FlexFieldDefinition.class, params);
    }

    @Override
    @Transactional
    public void moveFlexFieldDefinitions(List<String> ids, String folderId) {
        this.checkIdsNotEmpty(ids);
        Assert.hasText(folderId, MessageSourceContext.getMessage(MessageConstants.FOLDER_ID_NOT_BLANK));
        this.commonDomainService.moveForFolder(FlexFieldDefinition.class, ids, folderId);
    }

    @Override
    public FlexFieldDefinition loadFlexFieldDefinition(String id) {
        this.checkIdNotBlank(id);
        return this.flexFieldDefinitionRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteFlexFieldDefinitions(List<String> ids) {
        this.checkIdsNotEmpty(ids);

        List<FlexFieldDefinition> flexFieldDefinitions = this.flexFieldDefinitionRepository.findAll(ids);
        Assert.isTrue(ids.size() == flexFieldDefinitions.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "弹性域定义"));
        int count = 0;
        for (FlexFieldDefinition flexFieldDefinition : flexFieldDefinitions) {
            count = this.flexFieldBizGroupFieldRepository.countByFlexfielddefinitionId(flexFieldDefinition.getId());
            Assert.isTrue(count == 0, MessageSourceContext.getMessage(MessageConstants.OBJECT_REFERENCED, flexFieldDefinition.getFieldName()));
        }

        flexFieldDefinitionRepository.delete(flexFieldDefinitions);
    }

    @Override
    public Map<String, Object> slicedQueryFlexFieldDefinitions(FlexFieldDefinitionsQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "flexFieldDefinition");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.putDictionary("fieldType", DictUtil.getDictionary("fieldTypeList"));
        model.putDictionary("controlType", DictUtil.getDictionary("editControlType"));
        model.putDictionary("dataSourceKindId", DictUtil.getDictionary("dataSourceKind"));
        return this.sqlExecutorDao.executeSlicedQuery(model);
    }

    @Override
    @Transactional
    public String saveFlexFieldBizGroup(FlexFieldBizGroup flexFieldBizGroup) {
        Assert.notNull(flexFieldBizGroup, "参数flexFieldBizGroup不能为空。");
        List<FlexFieldBizGroupField> detailData = flexFieldBizGroup.getDetailData();
        flexFieldBizGroup = (FlexFieldBizGroup) this.commonDomainService.loadAndFillinProperties(flexFieldBizGroup);
        flexFieldBizGroup = (FlexFieldBizGroup) this.commonDomainService.saveBaseInfoWithFolderEntity(flexFieldBizGroup, this.flexFieldBizGroupRepository);
        this.updateFlexFieldBizGroupFields(detailData);
        return flexFieldBizGroup.getId();
    }

    @Override
    public FlexFieldBizGroup loadFlexFieldBizGroup(String id) {
        this.checkIdNotBlank(id);
        return this.flexFieldBizGroupRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteFlexFieldBizGroups(List<String> ids) {
        this.checkIdsNotEmpty(ids);
        List<FlexFieldBizGroup> flexFieldBizGroups = this.flexFieldBizGroupRepository.findAll(ids);
        for (FlexFieldBizGroup g : flexFieldBizGroups) {
            this.flexFieldBizGroupFieldRepository.deleteByFlexFieldbizgroupId(g.getId());
        }
        this.flexFieldBizGroupRepository.delete(flexFieldBizGroups);
    }

    @Override
    public Map<String, Object> slicedQueryFlexFieldBizGroups(FlexFieldBizGroupsQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "flexFieldBizGroup");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    @Transactional
    public void moveFlexFieldBizGroups(List<String> ids, String folderId) {
        this.checkIdsNotEmpty(ids);
        this.checkFolderIdNotBlank(folderId);
        this.commonDomainService.moveForFolder(FlexFieldBizGroup.class, ids, folderId);
    }

    @Override
    @Transactional
    public void updateFlexFieldBizGroupSequence(Map<String, Integer> params) {
        Assert.isTrue(params.size() > 0, "参数params不能为空。");
        this.commonDomainService.updateSequence(FlexFieldBizGroup.class, params);
    }

    @Override
    @Transactional
    public String copyFlexFieldBizGroups(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        FlexFieldBizGroup group = this.flexFieldBizGroupRepository.findOne(id);
        Assert.notNull(group, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, id, "弹性域业务分组"));
        // 复制主表
        FlexFieldBizGroup newGroup = (FlexFieldBizGroup) ClassHelper.clone(group);
        int sequence = this.commonDomainService.getNextSequence(FlexFieldBizGroup.class, "folderId", group.getFolderId());
        newGroup.setId(null);
        newGroup.setCode(group.getCode() + System.currentTimeMillis());
        newGroup.setName(group.getName() + System.currentTimeMillis());
        newGroup.setSequence(sequence);
        newGroup = (FlexFieldBizGroup) this.commonDomainService.saveBaseInfoWithFolderEntity(newGroup, this.flexFieldBizGroupRepository);
        String newId = newGroup.getId();
        // 复制明细表
        List<FlexFieldBizGroupField> list = this.flexFieldBizGroupFieldRepository.findByFlexfieldbizgroupId(id);
        if (list != null && list.size() > 0) {
            List<FlexFieldBizGroupField> datas = new ArrayList<FlexFieldBizGroupField>(list.size());
            for (FlexFieldBizGroupField field : list) {
                FlexFieldBizGroupField newField = (FlexFieldBizGroupField) ClassHelper.clone(field);
                newField.setId(null);
                newField.setFlexfieldbizgroupId(newId);
                datas.add(newField);
            }
            this.flexFieldBizGroupFieldRepository.save(datas);
        }
        return newId;
    }

    @Override
    @Transactional
    public void saveFlexFieldBizGroupFields(String flexFieldBizGroupId, List<String> flexFieldDefinitionIds) {
        Assert.hasText(flexFieldBizGroupId, "参数flexFieldBizGroupId不能为空。");
        Assert.notEmpty(flexFieldDefinitionIds, "参数flexFieldDefinitionIds不能为空。");

        FlexFieldBizGroup flexFieldBizGroup = this.flexFieldBizGroupRepository.findOne(flexFieldBizGroupId);
        Assert.notNull(flexFieldBizGroup, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, flexFieldBizGroupId, "弹性域业务分组"));
        List<FlexFieldBizGroupField> fields = new ArrayList<FlexFieldBizGroupField>(flexFieldDefinitionIds.size());
        int sequence = this.commonDomainService.getNextSequence(FlexFieldBizGroupField.class, "flexfieldbizgroupId", flexFieldBizGroupId);
        for (String flexFieldDefinitionId : flexFieldDefinitionIds) {
            int count = this.flexFieldBizGroupFieldRepository.countByFlexfieldbizgroupIdAndFlexfielddefinitionId(flexFieldBizGroupId, flexFieldDefinitionId);
            if (count == 0) {
                FlexFieldDefinition flexFieldDefinition = this.flexFieldDefinitionRepository.findOne(flexFieldDefinitionId);
                FlexFieldBizGroupField groupfield = new FlexFieldBizGroupField();
                groupfield.setFlexfieldbizgroupId(flexFieldBizGroupId);
                groupfield.setFlexfielddefinitionId(flexFieldDefinitionId);
                ClassHelper.copyProperties(flexFieldDefinition, groupfield);
                groupfield.setSequence(sequence);
                fields.add(groupfield);
                sequence++;
            }
        }
        this.flexFieldBizGroupFieldRepository.save(fields);
    }

    private void updateFlexFieldBizGroupFields(List<FlexFieldBizGroupField> detailData) {
        if (detailData != null && detailData.size() > 0) {
            this.flexFieldBizGroupFieldRepository.save(detailData);
        }
    }

    @Override
    @Transactional
    public void deleteFlexFieldBizGroupFields(String flexFieldBizGroupId, List<String> ids) {
        Assert.hasText(flexFieldBizGroupId, "参数flexFieldBizGroupId不能为空。");
        this.checkIdsNotEmpty(ids);

        FlexFieldBizGroup flexFieldBizGroup = this.flexFieldBizGroupRepository.findOne(flexFieldBizGroupId);
        Assert.notNull(flexFieldBizGroup, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, flexFieldBizGroupId, "弹性域业务分组"));
        List<FlexFieldBizGroupField> objs = flexFieldBizGroupFieldRepository.findAll(ids);
        this.flexFieldBizGroupFieldRepository.delete(objs);
    }

    @Override
    public Map<String, Object> slicedQueryFlexFieldBizGroupFields(ParentIdQueryRequest queryRequest) {
        Assert.hasText(queryRequest.getParentId(), "参数flexFieldBizGroupId不能为空。");
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "flexFieldBizGroupField");
        QueryModel mode = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        mode.putDictionary("controlType", DictUtil.getDictionary("editControlType"));
        return this.sqlExecutorDao.executeSlicedQuery(mode);
    }

    @Override
    public List<Map<String, Object>> queryVisibleFields(String parentId) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "flexFieldBizGroupField");
        String sql = queryDescriptor.getSqlByName("queryVisibleFields");
        return this.sqlExecutorDao.queryToListMap(sql, parentId);
    }

    @Override
    @Transactional
    public void saveFlexFieldStorages(String bizKindId, String bizId) {
        Assert.hasText(bizKindId, "参数bizKindId不能为空。");
        Assert.hasText(bizId, "参数bizId不能为空。");
        // 先删除
        this.deleteFlexFieldStorages(bizKindId, bizId);
        // 再添加
        SDO sdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        if (sdo != null) {
            List<FlexFieldStorage> flexFieldStorages = sdo.getList(Constants.FLEX_FIELD, FlexFieldStorage.class);
            if (flexFieldStorages != null && flexFieldStorages.size() > 0) {
                for (FlexFieldStorage item : flexFieldStorages) {
                    item.setId(null);
                    item.setBizKindId(bizKindId);
                    item.setBizId(bizId);
                }
                this.flexFieldStorageRepository.save(flexFieldStorages);
            }
        }
    }

    @Override
    @Transactional
    public void deleteFlexFieldStorages(String bizKindId, String bizId) {
        Assert.hasText(bizKindId, "参数bizKindId不能为空。");
        Assert.hasText(bizId, "参数bizId不能为空。");
        this.flexFieldStorageRepository.deleteByBizKindIdAndBizId(bizKindId, bizId);
    }

    @Override
    @Transactional
    public void deleteFlexFieldStorages(String bizId) {
        Assert.hasText(bizId, "参数bizId不能为空。");
        this.flexFieldStorageRepository.deleteByBizId(bizId);
    }

    @Override
    public List<Map<String, Object>> queryFlexFieldBizGroupFieldStorage(String bizKindId, String bizId) {
        // group1 -- field11
        // -- field12
        // -- field12
        // group2 -- field21
        // -- field22
        // -- field23
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "flexFieldBizGroupField");
        String sql = queryDescriptor.getSqlByName("queryByFlexFieldBizGroupId");
        List<Map<String, Object>> result = null;
        Map<String, Object> group;
        List<Map<String, Object>> fields = null;
        List<FlexFieldBizGroup> flexFieldBizGroups = this.flexFieldBizGroupRepository.findByBizCodeAndVisible(bizKindId, 1);
        result = new ArrayList<Map<String, Object>>(flexFieldBizGroups.size());
        String flexFieldDefinitionId = null;
        String fieldNameValue = null;
        List<FlexFieldStorage> flexFieldStorages = null;
        if (StringUtil.isNotBlank(bizId)) {
            flexFieldStorages = this.flexFieldStorageRepository.findByBizKindIdAndBizIdOrderBySequence(bizKindId, bizId);
        }
        for (FlexFieldBizGroup flexFieldBizGroup : flexFieldBizGroups) {
            group = ClassHelper.toMap(flexFieldBizGroup);
            Integer cols = flexFieldBizGroup.getCols();
            Integer showModel = flexFieldBizGroup.getShowModel();
            if (cols == null || cols < 1) {
                if (showModel == 1) {
                    cols = 12;
                } else {
                    cols = 0;
                }
            }
            if (cols > 12) {
                cols = 12;
            }
            group.put("cols", cols);
            group.put("name", MessageSourceContext.getMessageAsDefault(flexFieldBizGroup.getName()));
            // 查询分组中字段定义
            List<Map<String, Object>> groupFields = this.sqlExecutorDao.queryToListMap(sql, flexFieldBizGroup.getId());
            fields = new ArrayList<Map<String, Object>>(groupFields.size());
            for (Map<String, Object> fieldMap : groupFields) {
                flexFieldDefinitionId = fieldMap.get("flexfielddefinitionId").toString();
                fieldMap.put("flexFieldDefinitionId", flexFieldDefinitionId);
                fieldMap.put("fieldValue", fieldMap.get("defaultValue"));
                fieldMap.put("description", MessageSourceContext.getMessageAsDefault(fieldMap.get("description").toString()));
                fieldMap.remove("defaultValue");
                fieldMap.remove("id");
                Integer colSpan = ClassHelper.convert(fieldMap.get("colSpan"), Integer.class, 1);
                fieldMap.put("colSpan", colSpan);
                Integer newLine = ClassHelper.convert(fieldMap.get("newLine"), Integer.class, 0);
                fieldMap.put("newLine", newLine);
                Integer visible = ClassHelper.convert(fieldMap.get("visible"), Integer.class, 1);
                fieldMap.put("visible", visible);
                // 数据类型
                String dataSourceKind = ClassHelper.convert(fieldMap.get("dataSourceKindId"), String.class, "0");
                if (dataSourceKind.equals("3")) {// 数据字段的需要从缓存中读取相应的数据
                    String dataSource = ClassHelper.convert(fieldMap.get("dataSource"), String.class, "");
                    if (!StringUtil.isBlank(dataSource)) {
                        try {
                            String[] s = dataSource.split("[|]");
                            fieldMap.put("dataSource", JSONUtil.toString(DictUtil.getDictionary(s[0], s.length > 1 ? s[1] : null)));
                        } catch (Exception e) {
                            fieldMap.put("dataSource", "");
                        }
                    }
                }
                if (flexFieldStorages != null && flexFieldStorages.size() > 0) {
                    for (FlexFieldStorage flexFieldStorage : flexFieldStorages) {
                        if (flexFieldStorage.getFlexFieldDefinitionId().equals(flexFieldDefinitionId)) {
                            if (StringUtil.isNotBlank(flexFieldStorage.getFieldValue())) {
                                fieldMap.put("fieldValue", flexFieldStorage.getFieldValue());
                            }
                            fieldMap.put("lookUpValue", flexFieldStorage.getLookUpValue());
                            fieldMap.put("storageId", flexFieldStorage.getId());
                        }
                    }
                }
                fieldNameValue = (String) fieldMap.get("lookUpValue");
                if (StringUtil.isBlank(fieldNameValue)) {
                    fieldNameValue = (String) fieldMap.get("fieldValue");
                }
                fieldMap.put("fieldNameValue", fieldNameValue);
                fields.add(fieldMap);
            }
            group.remove("details");
            group.put("fields", fields);
            result.add(group);
        }
        return result;
    }
}
