package com.huigou.uasp.bmp.codingrule.application;

import java.util.Map;

/**
 * 编码产生器
 * @author Admin
 *
 */
public interface CodingGenerator {
    
    String CUSTOM_KEY = "_custom_";
    
    /**
     * 查询文件配置地址
     */
    static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/codingRule.xml";
    
    /**
     * 获取下一个编码
     * 
     * @param bizKindId
     *            业务类别ID
     * @return
     */
    String getNextCode(String bizKindId, Map<String, Object> inputParams);

}
