package com.huigou.report.cubesviewer.application.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.report.common.domain.query.ReportDefinitionQueryRequest;
import com.huigou.report.cubesviewer.application.CubesViewerDefinitionApplication;
import com.huigou.report.cubesviewer.domain.model.CubesViewerDefinition;
import com.huigou.report.cubesviewer.repository.CubesViewerDefinitionRepository;
import com.huigou.uasp.bmp.common.application.BaseApplication;

@Service("cubesViewerDefinitionApplication")
public class CubesViewerDefinitionApplicationImpl extends BaseApplication implements CubesViewerDefinitionApplication {
	@Autowired
	private CubesViewerDefinitionRepository cubesViewerDefinitionRepository;

	@Override
	@Transactional
	public String saveCubesViewerDefinition(CubesViewerDefinition cubesViewerDefinition) {
		Assert.notNull(cubesViewerDefinition, CommonDomainConstants.OBJECT_NOT_NULL);
        
		if (!cubesViewerDefinition.isNew()) {
			cubesViewerDefinition = (CubesViewerDefinition) this.commonDomainService.loadAndFillinProperties(cubesViewerDefinition);
		}
		cubesViewerDefinition = (CubesViewerDefinition) this.commonDomainService.saveBaseInfoWithFolderEntity(cubesViewerDefinition, cubesViewerDefinitionRepository);
		return cubesViewerDefinition.getId();
	}

	@Override
	public CubesViewerDefinition loadCubesViewerDefinition(String id) {
		Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
		CubesViewerDefinition cubesViewerDefinition = this.cubesViewerDefinitionRepository.findOne(id);
		return cubesViewerDefinition;
	}

	@Override
	@Transactional
	public void deleteCubesViewerDefinitions(List<String> ids) {
		checkIdsNotEmpty(ids);
		List<CubesViewerDefinition> objs = this.cubesViewerDefinitionRepository.findAll(ids);
		this.cubesViewerDefinitionRepository.delete(objs);
	}

	@Override
	public Map<String, Object> slicedQueryCubesViewerDefinitions(ReportDefinitionQueryRequest queryRequest) {
		QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, ENTITY_NAME);
		return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
	}

	@Override
	@Transactional
	public void updateCubesViewerDefinitionsSequence(Map<String, Integer> params) {
		Assert.isTrue(params.size() > 0, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "params"));
		this.commonDomainService.updateSequence(CubesViewerDefinition.class, params);
	}

	@Override
	@Transactional
	public void updateCubesViewerDefinitionsStatus(List<String> ids, Integer status) {
		checkIdsNotEmpty(ids);
		Assert.notNull(status, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, CommonDomainConstants.STATUS_FIELD_NAME));
		this.commonDomainService.updateStatus(CubesViewerDefinition.class, ids, status);
	}

	@Override
	@Transactional
	public void moveCubesViewerDefinitions(List<String> ids, String folderId) {
		checkIdsNotEmpty(ids);
		Assert.hasText(folderId, CommonDomainConstants.FOLDER_ID_NOT_BLANK);
		this.commonDomainService.moveForFolder(CubesViewerDefinition.class, ids, folderId);
	}

	@Override
	@Transactional
	public Integer getCubesViewerDefinitionNextSequence(String folderId) {
		Assert.hasText(folderId, CommonDomainConstants.FOLDER_ID_NOT_BLANK);
		return this.commonDomainService.getNextSequence(CubesViewerDefinition.class, CommonDomainConstants.FOLDER_ID_FIELD_NAME, folderId);
	}

}
