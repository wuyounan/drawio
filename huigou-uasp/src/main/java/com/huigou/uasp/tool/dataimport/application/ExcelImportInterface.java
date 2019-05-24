package com.huigou.uasp.tool.dataimport.application;

/**
 * EXCEL 导入解析完成执行接口
 * 
 * @author xx
 */
public interface ExcelImportInterface {
    /**
     * 业务数据导入操作
     * 
     * @param batchNumber
     */
    void executeImport(String batchNumber);
}
