package com.huigou.report.cboard.application.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.exception.ApplicationException;
import com.huigou.report.cboard.application.CboardApplication;
import com.huigou.report.cboard.application.CboardDefinitionApplication;
import com.huigou.report.cboard.domain.model.CboardDefinition;
import com.huigou.report.cboard.domain.model.CboardDefinitionTable;
import com.huigou.util.ClassHelper;
import com.huigou.util.HttpClientUtil;
import com.huigou.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("cboardApplication")
public class CboardApplicationImpl implements CboardApplication {

    public static final String CBOARD_ROWS_FIELD_NAME = "rows";

    public static final String CBOARD_COLUMNS_FIELD_NAME = "columns";

    public static final String CBOARD_FILTERS_FIELD_NAME = "filters";

    public static final String CBOARD_VALUES_FIELD_NAME = "values";

    public static final String CBOARD_QUERY_PARAMS_FIELD_NAME = "datasetQueryParams";

    public static final String CBOARD_COLUMN_NAME_FIELD_NAME = "columnName";

    public static final String CBOARD_FILTER_TYPE_FIELD_NAME = "filterType";

    @Value("${cboard.server.url}")
    private String cboardServerUrl;

    @Value("${cboard.dashboard.getAggregateData.url}")
    private String aggregateDataUrl;

    @Value("${cboard.corpId}")
    private String cBoardCorpId;

    @Value("${cboard.corpSecret}")
    private String cBoardCorpSecret;

    @Autowired
    private CboardDefinitionApplication cboardDefinitionApplication;

    @Override
    public String getAggregateDataUrl() {
        return cboardServerUrl + aggregateDataUrl;
    }

    @Override
    public String getCBoardCorpId() {
        return this.cBoardCorpId;
    }
    
    @Override
    public String getCboardServerUrl() {
        return this.cboardServerUrl;
    }

    @Override
    public String getCBoardCorpSecret() {
        return this.cBoardCorpSecret;
    }

    private JSONArray covertCboardResult(String cboardJson) {
        JSONObject cboardResult = JSONObject.fromObject(cboardJson);
        JSONArray columnList = cboardResult.getJSONArray("columnList");
        JSONArray cboardRows = cboardResult.getJSONArray("data");
        JSONObject localColumn, coveredRow;
        JSONArray localRow;
        JSONArray result = new JSONArray();
        int i;
        Object valueObjet;
        for (Object row : cboardRows) {
            localRow = (JSONArray) row;
            i = 0;
            coveredRow = new JSONObject();
            for (Object column : columnList) {
                localColumn = (JSONObject) column;
                valueObjet = localRow.get(i++);
                if (valueObjet != null && "#NULL".equals(valueObjet)) {
                    valueObjet = "";
                }
                
                coveredRow.put(localColumn.get("name"), valueObjet);
            }
            result.add(coveredRow);
        }
        return result;
    }

    @Override
    public JSONArray getAggregateData(String definitionId, String tableId, Map<String, Object> cascadeParams) {
        Assert.hasText(definitionId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "definitionId"));
        Assert.hasText(tableId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "tableId"));

        CboardDefinition cboardDefinition = this.cboardDefinitionApplication.loadCboardDefinition(definitionId);
        Assert.state(cboardDefinition != null, String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_ID, definitionId, "cboard定义"));

        CboardDefinitionTable cboardDefinitionTable = cboardDefinition.findTableById(tableId);
        Assert.state(cboardDefinitionTable != null, String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_ID, tableId, "cboard表格定义"));

        JSONObject tableColumnsJson = JSONObject.fromObject(String.format("{%s}", cboardDefinitionTable.getTableColumnsJson()));
        JSONArray tableColumns = tableColumnsJson.getJSONArray(CBOARD_COLUMNS_FIELD_NAME);

        JSONArray coveredTableColumns;
        if (tableColumns.get(0) instanceof JSONArray) {
            coveredTableColumns = new JSONArray();
            JSONArray singleTableColumn;
            for (Object object : tableColumns) {
                singleTableColumn = (JSONArray) object;
                coveredTableColumns.addAll(singleTableColumn);
            }
        } else {
            coveredTableColumns = tableColumns;

        }


        JSONObject cfg = new JSONObject();

        JSONArray rows = new JSONArray();
        JSONArray columns = new JSONArray();
        JSONArray filters = new JSONArray();
        JSONArray values = new JSONArray();

        JSONObject row;
        JSONObject localTableColumn;
        Object fieldName;
        // fromQueryParams and row data --> definition cascadeParams: {field: 'comp_id,year_month,...'}
        // cascadeParams : { comp_id: ****, year_month: ****, ...} -->H2DataSet where comp_id = ? and year_month = ? ...
        // fromQueryParams and row data --> cascadeParams.datasetQueryParams = {comp_id: ****, year_month: ... } --> hanaDataSet where comp_id = :comp_id and year_month = :year_month and ?
        // 组装查询字段
        for (Object tableColumn : coveredTableColumns) {
            localTableColumn = (JSONObject) tableColumn;

            fieldName = localTableColumn.get("field");
            if (fieldName == null || StringUtil.isBlank(ClassHelper.convert(fieldName, String.class))) {
                continue;
            }
            row = new JSONObject();
            row.put(CBOARD_COLUMN_NAME_FIELD_NAME, fieldName);
            row.put(CBOARD_FILTER_TYPE_FIELD_NAME, "eq");
            row.put(CBOARD_VALUES_FIELD_NAME, "[]");

            if (cascadeParams != null && cascadeParams.containsKey(fieldName)) {
                JSONArray rowValues = new JSONArray();
                rowValues.add(cascadeParams.get(fieldName));
                row.put(CBOARD_VALUES_FIELD_NAME, rowValues);
            }
            row.put("id", "");
            columns.add(row);
        }

        cfg.put(CBOARD_ROWS_FIELD_NAME, rows);
        cfg.put(CBOARD_COLUMNS_FIELD_NAME, columns);
        cfg.put(CBOARD_FILTERS_FIELD_NAME, filters);
        cfg.put(CBOARD_VALUES_FIELD_NAME, values);
        if (cascadeParams != null) {
            cfg.put("params", cascadeParams.get(CBOARD_QUERY_PARAMS_FIELD_NAME));
        }

        String content = "";
        try {
            content = "datasetId=" + cboardDefinitionTable.getDatasetId() + "&reload=false&cfg=" + URLEncoder.encode(cfg.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ApplicationException("转换字符串出错。");
        }

        StringEntity entity = new StringEntity(content, Consts.UTF_8);
        entity.setContentType("application/json");

        Map<String, String> headers = new HashMap<String, String>(2);

        headers.put("corpId", getCBoardCorpId());
        headers.put("corpSecret", getCBoardCorpSecret());

        headers.put(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");

        String url = this.getAggregateDataUrl();

        String result = HttpClientUtil.post(url, entity, Consts.UTF_8, headers);

        JSONArray coveredData = covertCboardResult(result);
        return coveredData;
    }
}
