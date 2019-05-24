package com.huigou.data.excel.exporter;

import java.util.Map;

import org.dom4j.Element;

/**
 * Excel 导出接口
 * 
 * @author xx
 */
public interface IExcelExport {
    /**
     * 设置表头
     * 
     * @author 
     * @param root
     * @throws
     */
    public void setHeadRoot(Element root);

    /**
     * 设置需要导出的数据
     * 
     * @author 
     * @param datas
     * @throws
     */
    public void setDatas(Map<String, Object> datas);

    /**
     * 执行导出
     * 
     * @author 
     * @return
     * @throws Exception
     * @throws
     */
    public String expExcel() throws Exception;
}
