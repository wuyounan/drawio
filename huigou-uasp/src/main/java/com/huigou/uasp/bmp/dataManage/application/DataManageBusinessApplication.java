package com.huigou.uasp.bmp.dataManage.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagebusiness;
import com.huigou.uasp.bmp.dataManage.domain.model.OpdatamanagebusinessField;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagebusinessFieldQueryRequest;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagebusinessQueryRequest;

/**
 * 数据管理权限业务类型
 * 
 * @author xx
 * @date 2018-09-27 12:04
 */
public interface DataManageBusinessApplication {
    /**
     * 查询文件配置地址
     */
    static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/dataManagement.xml";

    /**
     * 新增 数据管理权限业务类型
     * 
     * @author xx
     * @param params
     */
    String insertOpdatamanagebusiness(Opdatamanagebusiness opdatamanagebusiness);

    /**
     * 修改 数据管理权限业务类型
     * 
     * @param newOpdatamanagebusiness
     * @return
     */
    String updateOpdatamanagebusiness(Opdatamanagebusiness newOpdatamanagebusiness);

    /**
     * 加载 数据管理权限业务类型
     * 
     * @author xx
     * @return SDO
     */
    Opdatamanagebusiness loadOpdatamanagebusiness(String id);

    /**
     * 删除 数据管理权限业务类型
     * 
     * @author xx
     */
    void deleteOpdatamanagebusiness(List<String> ids);

    /**
     * 查询 数据管理权限业务类型
     * 
     * @author xx
     * @return SDO
     */
    Map<String, Object> slicedQueryOpdatamanagebusiness(OpdatamanagebusinessQueryRequest queryRequest);

    List<Map<String, Object>> queryDatamanagebusiness(OpdatamanagebusinessQueryRequest queryRequest);

    /**
     * 修改数据管理权限业务类型序号
     * 
     * @param map
     */
    void updateOpdatamanagebusinessSequence(Map<String, Integer> map);

    /**
     * 移动数据管理权限业务类型
     * 
     * @param parentId
     * @param ids
     */
    void moveOpdatamanagebusiness(String parentId, List<String> ids);

    /**
     * 保存 数据管理权限业务过滤字段定义
     * 
     * @author xx
     * @param params
     */
    void saveOpdatamanagebusinessField(OpdatamanagebusinessField field);

    /**
     * 加载 数据管理权限业务过滤字段定义
     * 
     * @author xx
     * @return SDO
     */
    OpdatamanagebusinessField loadOpdatamanagebusinessField(String id);

    /**
     * 删除 数据管理权限业务过滤字段定义
     * 
     * @author xx
     */
    void deleteOpdatamanagebusinessField(List<String> ids);

    /**
     * 查询 数据管理权限业务过滤字段定义
     * 
     * @author xx
     * @return SDO
     */
    Map<String, Object> queryOpdatamanagebusinessField(OpdatamanagebusinessFieldQueryRequest queryRequest);

}
