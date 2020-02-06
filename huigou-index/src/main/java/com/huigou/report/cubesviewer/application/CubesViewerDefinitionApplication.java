package com.huigou.report.cubesviewer.application;

import java.util.List;
import java.util.Map;

import com.huigou.report.common.domain.query.ReportDefinitionQueryRequest;
import com.huigou.report.cubesviewer.domain.model.CubesViewerDefinition;

public interface CubesViewerDefinitionApplication {

    String QUERY_XML_FILE_PATH = "/config/mcs/report/cubesViewerDefinition.xml";

    String ENTITY_NAME = "cubesViewerDefinition";

    /**
     * 保存CubesViewer定义
     * 
     * @param cubesViewerDefinition
     *            CubesViewer定义实体
     * @return CubesViewer定义ID
     */
    String saveCubesViewerDefinition(CubesViewerDefinition cubesViewerDefinition);

    /**
     * 加载CubesViewer定义
     * 
     * @param id
     *            CubesViewer定义ID
     * @return CubesViewer定义实体
     */
    CubesViewerDefinition loadCubesViewerDefinition(String id);

    /**
     * 删除CubesViewer定义实体
     * 
     * @param ids
     *            CubesViewer定义ID集合
     */
    void deleteCubesViewerDefinitions(List<String> ids);

    /**
     * 分页查询CubesViewer定义实体
     * 
     * @param queryRequest
     *            CubesViewer定义查询条件
     * @return CubesViewer定义实体集合
     */
    Map<String, Object> slicedQueryCubesViewerDefinitions(ReportDefinitionQueryRequest queryRequest);

    /**
     * 保存排序号
     * 
     * @param params
     */
    void updateCubesViewerDefinitionsSequence(Map<String, Integer> params);

    /**
     * 修改状态
     * 
     * @param ids
     *            CubesViewer定义ID集合
     * @param status
     *            状态
     */
    void updateCubesViewerDefinitionsStatus(List<String> ids, Integer status);

    /**
     * 移动CubesViewer定义
     * 
     * @param ids
     *            CubesViewer定义ID集合
     * @param folderId
     *            分类ID
     */
    void moveCubesViewerDefinitions(List<String> ids, String folderId);

    /**
     * 获取CubesViewer定义下一排序号
     * 
     * @param folderId
     *            分类ID
     * @return 排序号
     */
    Integer getCubesViewerDefinitionNextSequence(String folderId);
}
