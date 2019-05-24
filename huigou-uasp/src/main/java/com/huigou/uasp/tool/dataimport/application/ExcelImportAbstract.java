package com.huigou.uasp.tool.dataimport.application;

import com.huigou.uasp.bmp.common.application.BaseApplication;

/**
 * 导出excel 抽象类 可自定义excle模板表头，指定表头所占行数
 * 
 * @author xx
 */
public abstract class ExcelImportAbstract extends BaseApplication implements ExcelImportInterface {
    /**
     * 默认表头占一行
     * 
     * @return
     */
    public int getHeadRowspan(String code) {
        return 1;
    }

    /**
     * 返回表头xml
     * 
     * @return 返回null使用默认表头
     */
    public String getHeadXml(String code) {
        return null;
    }

}
