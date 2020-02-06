package com.huigou.report.cboard.application;

import java.util.Map;

import net.sf.json.JSONArray;

public interface CboardApplication {

    String getAggregateDataUrl();
    
    String getCboardServerUrl();

    String getCBoardCorpId();

    String getCBoardCorpSecret();

    JSONArray getAggregateData(String definitionId, String tableId, Map<String, Object> cascadeParams);
}
