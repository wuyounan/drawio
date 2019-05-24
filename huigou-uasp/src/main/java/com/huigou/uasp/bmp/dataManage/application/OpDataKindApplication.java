package com.huigou.uasp.bmp.dataManage.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.dataManage.domain.model.Opdatakind;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatakindQueryRequest;

/**
 * 数据管理权限维度定义
 * 
 * @author xx
 * @date 2018-09-04 10:52
 */
public interface OpDataKindApplication {
    /**
     * 查询文件配置地址
     */
    static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/dataManagement.xml";

    /**
     * 保存 数据管理权限维度定义
     * 
     * @author xx
     * @param params
     */
    String saveOpdatakind(Opdatakind opdatakind);

    /**
     * 加载 数据管理权限维度定义
     * 
     * @author xx
     * @return SDO
     */
    Opdatakind loadOpdatakind(String id);

    /**
     * 删除 数据管理权限维度定义
     * 
     * @author xx
     */
    void deleteOpdatakind(String id);

    /**
     * 查询 数据管理权限维度定义
     * 
     * @author xx
     * @return SDO
     */
    Map<String, Object> slicedQueryOpdatakind(OpdatakindQueryRequest queryRequest);

    /**
     * 更新排序号
     * 
     * @param map
     */
    void updateOpdatakindSequence(Map<String, Integer> map);

    /**
     * 查询全部资源维度
     * 
     * @return
     */
    List<Opdatakind> findAll();

    /**
     * 根据ID查询资源维度定义
     * 
     * @param id
     * @return
     */
    Map<String, Object> findById(String id);

}
