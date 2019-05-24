package com.huigou.uasp.tool.dataimport.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.EmptyQueryRequest;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.data.domain.query.QueryPageRequest;
import com.huigou.uasp.tool.dataimport.domain.model.ExcelImportLog;
import com.huigou.uasp.tool.dataimport.domain.model.ExcelImportStatus;
import com.huigou.uasp.tool.dataimport.domain.model.ExcelImportTemplate;

/**
 * Excel导入
 * 
 * @author gongmm
 */
public interface ExcelImportApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/dataimport.xml";

    /**
     * 保存Excel导入模板
     * 
     * @param excelImportTemplate
     *            Excel导入模板对象
     * @return
     */
    String saveExcelImportTemplate(ExcelImportTemplate excelImportTemplate);

    /**
     * 加载Excel导入模板
     * 
     * @param id
     *            Excel导入模板ID
     * @return
     */
    ExcelImportTemplate loadExcelImportTemplate(String id);

    /**
     * 加载Excel导入模板
     * 
     * @param id
     * @return
     */
    ExcelImportTemplate loadExcelImportTemplateByCode(String code);

    /**
     * 删除Excel导入模板
     * 
     * @param ids
     *            Excel导入模板ID列表
     */
    void deleteExcelImportTemplates(List<String> ids);

    /**
     * 删除Excel导入模板明细
     * 
     * @param excelImportTemplateId
     *            Excel导入模板ID列表
     * @param ids
     *            Excel导入模板明细ID列表
     */
    void deleteExcelImportTemplateDetails(String templateId, List<String> ids);

    /**
     * 移动Excel导入模板
     * 
     * @param ids
     *            Excel导入模板ID
     * @param folderId
     *            文件夹ID
     */
    void moveExcelImportTemplates(List<String> ids, String folderId);

    /**
     * 更细Excel导入模板状态
     * 
     * @param ids
     *            Excel导入模板ID
     * @param status
     *            状态
     */
    void updateExcelImportTemplateStatus(List<String> ids, Integer status);

    /**
     * 分页查询Excel导入模板明细
     * 
     * @param templateId
     *            Excel导入模板ID
     * @return
     */
    Map<String, Object> queryExcelImportTemplateDetails(String templateId, EmptyQueryRequest queryRequest);

    /**
     * 查询导入结果头
     * 
     * @param templateId
     * @return
     */
    Map<String, Object> queryExcelImportGridHead(String templateId);

    /**
     * 分页查询Excel导入模板
     * 
     * @param queryModel
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryExcelImportTemplates(FolderAndCodeAndNameQueryRequest queryRequest);

    /**
     * 构建Excel表头
     * 
     * @param templateId
     *            Excel模板ID
     * @return
     */
    String buildExcelHeadByTemplateId(String templateId);

    /**
     * 保存Excel导入日志
     * 
     * @param excelImportLog
     * @return
     */
    String saveExcelImportLog(ExcelImportLog excelImportLog);

    /**
     * 统计Excel导入
     * 
     * @param templetId
     *            Excel导入模板ID
     * @param batchNumber
     *            批次号
     * @param excelImportStatus
     *            导入状态
     * @return
     */
    long countExcelImport(String templateId, String batchNumber, ExcelImportStatus excelImportStatus);

    /**
     * 导入
     * 
     * @param templateId
     *            模板ID
     * @param batchNumber
     *            批次号
     * @param fileName
     *            文件名
     * @return
     */
    String doImport(String templateId, String batchNumber, String fileName) throws Exception;

    /**
     * 分页查询Excel导入明细
     * 
     * @param templateId
     *            模板ID
     * @param batchNumber
     *            批次号
     * @param status
     *            状态
     * @return
     */
    Map<String, Object> slicedQueryExcelImportDetails(String templateId, String batchNumber, ExcelImportStatus status, QueryPageRequest pageRequest);

    /**
     * 分页查询Excel导入日志
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    public Map<String, Object> slicedQueryExcelImportLogs(ParentIdQueryRequest queryRequest);

    /**
     * 删除导入临时表数据
     * 
     * @param templateId
     *            Excel模板ID
     * @param batchNumber
     *            批次号
     */
    void deleteTemporaryData(String templateId, String batchNumber);

}
