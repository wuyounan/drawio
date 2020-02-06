package com.huigou.report.cboard.application.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.report.cboard.application.CboardDefinitionApplication;
import com.huigou.report.cboard.domain.model.CboardDefinition;
import com.huigou.report.cboard.domain.query.CboardEntryQueryRequest;
import com.huigou.report.cboard.repository.CboardDefinitionRepository;
import com.huigou.report.common.domain.query.ReportDefinitionQueryRequest;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.util.Constants;

@Service("cboardDefinitionApplication")
public class CboardDefinitionApplicationImpl extends BaseApplication implements CboardDefinitionApplication {
    @Autowired
    private CboardDefinitionRepository cboardDefinitionRepository;

    @Override
    @Transactional
    public String saveCboardDefinition(CboardDefinition cboardDefinition) {
        Assert.notNull(cboardDefinition, CommonDomainConstants.OBJECT_NOT_NULL);

        if (!cboardDefinition.isNew()) {
            cboardDefinition = (CboardDefinition) this.commonDomainService.loadAndFillinProperties(cboardDefinition);
        }

        cboardDefinition.buildDetails();

        cboardDefinition = (CboardDefinition) this.commonDomainService.saveBaseInfoWithFolderEntity(cboardDefinition, cboardDefinitionRepository);
        return cboardDefinition.getId();
    }

    @Override
    public CboardDefinition loadCboardDefinition(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        CboardDefinition cboardDefinition = this.cboardDefinitionRepository.findOne(id);
        return cboardDefinition;
    }

    @Override
    public CboardDefinition loadCboardDefinitionByCode(String code) {
        Assert.hasText(code, CommonDomainConstants.CODE_NOT_BLANK);
        return this.cboardDefinitionRepository.findByCode(code);
    }

    @Override
    @Transactional
    public void deleteCboardDefinitions(List<String> ids) {
        checkIdsNotEmpty(ids);
        List<CboardDefinition> objs = this.cboardDefinitionRepository.findAll(ids);
        for (CboardDefinition obj : objs) {
            Assert.state(obj.getUIParams().size() == 0, String.format("“%s”存在界面参数定义，不能删除。", obj.getName()));
            Assert.state(obj.getTables().size() == 0, String.format("“%s”存在表格定义，不能删除。", obj.getName()));
        }
        this.cboardDefinitionRepository.delete(objs);
    }

    @Override
    public Map<String, Object> slicedQueryCboardDefinitions(ReportDefinitionQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, ENTITY_NAME);
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    @Transactional
    public void updateCboardDefinitionsSequence(Map<String, Integer> params) {
        Assert.isTrue(params.size() > 0, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "params"));
        this.commonDomainService.updateSequence(CboardDefinition.class, params);
    }

    @Override
    @Transactional
    public void updateCboardDefinitionsStatus(List<String> ids, Integer status) {
        checkIdsNotEmpty(ids);
        Assert.notNull(status, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, CommonDomainConstants.STATUS_FIELD_NAME));
        this.commonDomainService.updateStatus(CboardDefinition.class, ids, status);
    }

    @Override
    @Transactional
    public void moveCboardDefinitions(List<String> ids, String folderId) {
        checkIdsNotEmpty(ids);
        Assert.hasText(folderId, CommonDomainConstants.FOLDER_ID_NOT_BLANK);
        this.commonDomainService.moveForFolder(CboardDefinition.class, ids, folderId);
    }

    @Override
    @Transactional
    public Integer getCboardDefinitionNextSequence(String folderId) {
        Assert.hasText(folderId, CommonDomainConstants.FOLDER_ID_NOT_BLANK);
        return this.commonDomainService.getNextSequence(CboardDefinition.class, CommonDomainConstants.FOLDER_ID_FIELD_NAME, folderId);
    }

    private CboardDefinition checkDeleteCboardDefinitionParamsAndReturn(String definitionId, List<String> ids) {
        Assert.hasText(definitionId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "definitionId"));
        this.checkIdsNotEmpty(ids);
        CboardDefinition cboardDefinition = this.cboardDefinitionRepository.findOne(definitionId);
        Assert.state(cboardDefinition != null, String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_ID, definitionId, "Cboard定义"));
        return cboardDefinition;
    }

    @Override
    @Transactional
    public void deleteUIParams(String definitionId, List<String> ids) {
        CboardDefinition cboardDefinition = checkDeleteCboardDefinitionParamsAndReturn(definitionId, ids);
        cboardDefinition.removeDetails(cboardDefinition.getUIParams(), ids);
        this.cboardDefinitionRepository.save(cboardDefinition);
    }

    @Override
    @Transactional
    public void deleteTables(String definitionId, List<String> ids) {
        CboardDefinition cboardDefinition = checkDeleteCboardDefinitionParamsAndReturn(definitionId, ids);
        cboardDefinition.removeDetails(cboardDefinition.getTables(), ids);
        this.cboardDefinitionRepository.save(cboardDefinition);
    }

    private void checkCboardDefinitionEntryQueryRequest(CboardEntryQueryRequest queryRequest) {
        Assert.notNull(queryRequest, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "queryRequest"));
        queryRequest.checkConstraints();
    }

    @Override
    public Map<String, Object> queryUIParams(CboardEntryQueryRequest queryRequest) {
        checkCboardDefinitionEntryQueryRequest(queryRequest);
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, UI_PARAM_ENTITY_NAME);
        return this.sqlExecutorDao.executeQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Map<String, Object> queryTables(CboardEntryQueryRequest queryRequest) {
        checkCboardDefinitionEntryQueryRequest(queryRequest);
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, TABLE_ENTITY_NAME);

        String sql = queryDescriptor.getSqlByName("selectTableColumnsJson");

        String tableColumnsJson;

        Map<String, Object> data = this.sqlExecutorDao.executeQuery(queryDescriptor, queryRequest);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) data.get(Constants.ROWS);
        for (Map<String, Object> item : list) {
            tableColumnsJson = this.sqlExecutorDao.getSqlQuery().getJDBCDao().loadClob(sql, item.get("id"));
            item.put("tableColumnsJson", tableColumnsJson);
        }

        return data;
    }

}