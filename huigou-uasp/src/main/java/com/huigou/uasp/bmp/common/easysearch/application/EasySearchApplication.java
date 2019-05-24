package com.huigou.uasp.bmp.common.easysearch.application;

import java.util.Map;

import com.huigou.uasp.bmp.common.easysearch.domain.model.EasySearchParse;
import com.huigou.util.SDO;

public interface EasySearchApplication {

    public void search(EasySearchParse dto, SDO sdo) throws Exception;

    /**
     * 选择对话框列表查询方法
     * 
     * @Title: comboGridSearch
     * @author xiexin
     * @param dto
     * @param sdo
     * @return
     * @throws Exception
     *             Map<String,Object>
     */
    public Map<String, Object> comboGridSearch(final EasySearchParse dto, SDO sdo);
}
