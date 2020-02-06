package com.huigou.report.cboard.application;

import java.util.List;
import java.util.Map;

import com.huigou.report.cboard.domain.model.CboardDefinition;
import com.huigou.report.cboard.domain.query.CboardEntryQueryRequest;
import com.huigou.report.common.domain.query.ReportDefinitionQueryRequest;

public interface CboardDefinitionApplication {

    String QUERY_XML_FILE_PATH = "/config/mcs/report/cboardDefinition.xml";

    String ENTITY_NAME = "cboardDefinition";

    String UI_PARAM_ENTITY_NAME = "uiParam";

    String TABLE_ENTITY_NAME = "table";

    String saveCboardDefinition(CboardDefinition cboardDefinition);

    CboardDefinition loadCboardDefinition(String id);
    
    CboardDefinition loadCboardDefinitionByCode(String code);
    
    void deleteCboardDefinitions(List<String> ids);

    Map<String, Object> slicedQueryCboardDefinitions(ReportDefinitionQueryRequest queryRequest);

    void updateCboardDefinitionsSequence(Map<String, Integer> params);

    void updateCboardDefinitionsStatus(List<String> ids, Integer status);

    void moveCboardDefinitions(List<String> ids, String folderId);

    Integer getCboardDefinitionNextSequence(String folderId);

    void deleteUIParams(String definitionId, List<String> ids);

    void deleteTables(String definitionId, List<String> ids);

    Map<String, Object> queryUIParams(CboardEntryQueryRequest queryRequest);

    Map<String, Object> queryTables(CboardEntryQueryRequest queryRequest);
}
