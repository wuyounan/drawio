package com.huigou.report.cubesviewer.controller;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.annotation.ControllerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huigou.report.common.domain.query.ReportDefinitionQueryRequest;
import com.huigou.report.cubesviewer.application.CubesViewerDefinitionApplication;
import com.huigou.report.cubesviewer.domain.model.CubesViewerDefinition;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("cubesViewerDefinition")
public class CubesViewerDefinitionController extends CommonController {
	private static final String CUBES_VIEW_DEFINITION_LIST_PAGE = "cubesViewerDefinitionList";
	private static final String CUBES_VIEW_DEFINITION_DETAIL_PAGE = "cubesViewerDefinitionDetail";
	@Autowired
	private CubesViewerDefinitionApplication application;

	protected String getPagePath() {
		return "/report/cubesviewer/";
	}

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
	public String forwardListCubesViewerDefinition() {
		return forward(CUBES_VIEW_DEFINITION_LIST_PAGE);
	}

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "")
	public String showInsertCubesViewerDefinition() {
		String folderId = this.getSDO().getString(CommonDomainConstants.FOLDER_ID_FIELD_NAME);
		putAttribute(CommonDomainConstants.SEQUENCE_FIELD_NAME, this.application.getCubesViewerDefinitionNextSequence(folderId));
		return forward(CUBES_VIEW_DEFINITION_DETAIL_PAGE, this.getSDO());
	}

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "")
	public String insertCubesViewerDefinition() {
		CubesViewerDefinition cubesViewerDefinition = this.getSDO().toObject(CubesViewerDefinition.class);
		String id = this.application.saveCubesViewerDefinition(cubesViewerDefinition);
		return success(id);
	}

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.VIEW, description = "")
	public String showUpdateCubesViewerDefinition() {
		String id = this.getSDO().getId();
		CubesViewerDefinition cubesViewerDefinition = this.application.loadCubesViewerDefinition(id);
		return forward(CUBES_VIEW_DEFINITION_DETAIL_PAGE, cubesViewerDefinition);
	}

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
	public String updateCubesViewerDefinition() {
		CubesViewerDefinition cubesViewerDefinition = this.getSDO().toObject(CubesViewerDefinition.class);
		this.application.saveCubesViewerDefinition(cubesViewerDefinition);
		return success();
	}

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "")
	public String deleteCubesViewerDefinitions() {
		List<String> ids = this.getSDO().getIds();
		this.application.deleteCubesViewerDefinitions(ids);
		return success();
	}

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
	public String updateCubesViewerDefinitionsStatus() {
		SDO sdo = getSDO();
		List<String> ids = sdo.getIds();
		Integer status = sdo.getInteger(CommonDomainConstants.STATUS_FIELD_NAME);
		this.application.updateCubesViewerDefinitionsStatus(ids, status);
		return success();
	}

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
	public String updateCubesViewerDefinitionsSequence() {
		Map<String, Integer> params = getSDO().getStringMap("data");
		this.application.updateCubesViewerDefinitionsSequence(params);
		return success();
	}

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.MOVE, description = "")
	public String moveCubesViewerDefinitions() {
		SDO sdo = getSDO();
		List<String> ids = sdo.getIds();
		String folderId = this.getSDO().getString(CommonDomainConstants.FOLDER_ID_FIELD_NAME);
		this.application.moveCubesViewerDefinitions(ids, folderId);
		return success();
	}

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
	public String slicedQueryCubesViewerDefinitions() {
		SDO sdo = getSDO();
		ReportDefinitionQueryRequest queryRequest = sdo.toQueryRequest(ReportDefinitionQueryRequest.class);
		Map<String, Object> data = this.application.slicedQueryCubesViewerDefinitions(queryRequest);
		return toResult(data);
	}

}
