package com.huigou.report.cboard.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.report.cboard.application.CboardDefinitionApplication;
import com.huigou.report.cboard.domain.model.CboardDefinition;
import com.huigou.report.cboard.domain.model.CboardDefinitionTable;
import com.huigou.report.cboard.domain.model.CboardDefinitionUIParam;
import com.huigou.report.cboard.domain.query.CboardEntryQueryRequest;
import com.huigou.report.common.domain.query.ReportDefinitionQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("cboardDefinition")
public class CboardDefinitionController extends CommonController {

    private static final String CBOARD_DEFINITION_PAGE = "cboardDefinition";

    private static final String CBOARD_DEFINITION_DETAIL_PAGE = "cboardDefinitionDetail";

    @Autowired
    private CboardDefinitionApplication application;

    protected String getPagePath() {
        return "/report/cboard/";
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardCboardDefinition() {
        return forward(CBOARD_DEFINITION_PAGE);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "")
    public String showInsertCboardDefinition() {
        String folderId = this.getSDO().getString(CommonDomainConstants.FOLDER_ID_FIELD_NAME);
        putAttribute(CommonDomainConstants.SEQUENCE_FIELD_NAME, this.application.getCboardDefinitionNextSequence(folderId));
        return forward(CBOARD_DEFINITION_DETAIL_PAGE, this.getSDO());
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.VIEW, description = "")
    public String loadCboardDefinitionByCode() {
        SDO sdo = this.getSDO();
        String code = sdo.getString(CommonDomainConstants.CODE_FIELD_NAME);
        CboardDefinition cboardDefinition = this.application.loadCboardDefinitionByCode(code);
        return this.toResult(cboardDefinition);
    }

    private CboardDefinition internalBuildCboardDefinition() {
        SDO sdo = this.getSDO();
        CboardDefinition boardDefinition = sdo.toObject(CboardDefinition.class);

        List<CboardDefinitionUIParam> inputUIParams = sdo.getList("uiParamData", CboardDefinitionUIParam.class);
        List<CboardDefinitionTable> inputTables = sdo.getList("tableData", CboardDefinitionTable.class);

        boardDefinition.setInputUIParams_(inputUIParams);
        boardDefinition.setInputTables_(inputTables);
        boardDefinition.addUpdateFields_(CboardDefinition.INPUT_UI_PARAM_FIELD_NAME, CboardDefinition.INPUT_TABLE_FIELD_NAME);

        return boardDefinition;
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "")
    public String insertCboardDefinition() {
        CboardDefinition cboardDefinition = internalBuildCboardDefinition();
        String id = this.application.saveCboardDefinition(cboardDefinition);
        return success(id);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.VIEW, description = "")
    public String showUpdateCboardDefinition() {
        String id = this.getSDO().getId();
        CboardDefinition cboardDefinition = this.application.loadCboardDefinition(id);
        return forward(CBOARD_DEFINITION_DETAIL_PAGE, cboardDefinition);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
    public String updateCboardDefinition() {
        CboardDefinition cboardDefinition = internalBuildCboardDefinition();
        this.application.saveCboardDefinition(cboardDefinition);
        return success(cboardDefinition.getId());
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "")
    public String deleteCboardDefinitions() {
        List<String> ids = this.getSDO().getIds();
        this.application.deleteCboardDefinitions(ids);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
    public String updateCboardDefinitionsStatus() {
        SDO sdo = getSDO();
        List<String> ids = sdo.getIds();
        Integer status = sdo.getInteger(CommonDomainConstants.STATUS_FIELD_NAME);
        this.application.updateCboardDefinitionsStatus(ids, status);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
    public String updateCboardDefinitionsSequence() {
        Map<String, Integer> params = getSDO().getStringMap("data");
        this.application.updateCboardDefinitionsSequence(params);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.MOVE, description = "")
    public String moveCboardDefinitions() {
        SDO sdo = getSDO();
        List<String> ids = sdo.getIds();
        String folderId = this.getSDO().getString(CommonDomainConstants.FOLDER_ID_FIELD_NAME);
        this.application.moveCboardDefinitions(ids, folderId);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String slicedQueryCboardDefinitions() {
        SDO sdo = getSDO();
        ReportDefinitionQueryRequest queryRequest = sdo.toQueryRequest(ReportDefinitionQueryRequest.class);
        Map<String, Object> data = this.application.slicedQueryCboardDefinitions(queryRequest);
        return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "")
    public String deleteUIParams() {
        SDO sdo = this.getSDO();
        String definitionId = sdo.getString("definitionId");
        List<String> ids = sdo.getIds();
        this.application.deleteUIParams(definitionId, ids);
        return this.success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "")
    public String deleteTables() {
        SDO sdo = this.getSDO();
        String definitionId = sdo.getString("definitionId");
        List<String> ids = sdo.getIds();
        this.application.deleteTables(definitionId, ids);
        return this.success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryUIParams() {
        SDO sdo = getSDO();
        CboardEntryQueryRequest queryRequest = sdo.toQueryRequest(CboardEntryQueryRequest.class);
        Map<String, Object> data = this.application.queryUIParams(queryRequest);
        return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryTables() {
        SDO sdo = getSDO();
        CboardEntryQueryRequest queryRequest = sdo.toQueryRequest(CboardEntryQueryRequest.class);
        Map<String, Object> data = this.application.queryTables(queryRequest);
        return toResult(data);
    }

}
