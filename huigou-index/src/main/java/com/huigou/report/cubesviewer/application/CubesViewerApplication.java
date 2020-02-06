package com.huigou.report.cubesviewer.application;

import java.util.List;
import java.util.Map;

import com.huigou.report.cubesviewer.domain.model.CubesViewerOperatorQueryScheme;

public interface CubesViewerApplication {

    String QUERY_XML_FILE_PATH = "/config/mcs/report/cubesViewer.xml";

    String CUBES_VIEWER_OPERATOR_QUERY_SCHEME_ENTITY_NAME = "cubesViewerOperatorQueryScheme";

    String getCubesServerUrl();

    /**
     * 获取立方体定义
     * 
     * @param cubesViewerCodes
     *            立方体编码列表
     * @return
     */
    Map<String, Object> getCubesViewerDefinition(List<String> cubesViewerCodes);

    /**
     * 保存CubesViewer用户查询方案
     * 
     * @param cubesViewerOperatorQueryScheme
     *            用户查询方案实体
     */
    String saveCubesViewerOperatorQueryScheme(CubesViewerOperatorQueryScheme cubesViewerOperatorQueryScheme);

    /**
     * 更改用户查询方案名称
     * 
     * @param cubesViewerOperatorQuerySchemeId
     *            用户查询方案ID
     * @param newName
     *            新名称
     */
    void renameCubesViewerOperatorQueryScheme(String cubesViewerOperatorQuerySchemeId, String newName);

    /**
     * 删除CubesViewer用户查询方案
     * 
     * @param id
     *            CubesViewer用户查询方案ID
     */
    void deleteCubesViewerOperatorQueryScheme(String id);

    /**
     * 查询CubesViewer用户查询方案
     * 
     * @param
     *            functionCode
     *            功能编码
     */
    List<Map<String, Object>> queryCubesViewerOperatorQuerySchemesForCurrentOperator(String functionCode);

    /**
     * 调用cubesViewer服务
     * 
     * @param queryParam
     *            查询参数
     * @return
     */
    String callCubesViewerService(String cubesViewerAction, String queryString);
}
