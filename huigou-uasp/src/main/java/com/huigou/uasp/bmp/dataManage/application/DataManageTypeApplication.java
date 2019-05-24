package com.huigou.uasp.bmp.dataManage.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagetype;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagetypeQueryRequest;

/**
 * 数据管理权限类别定义
 * 
 * @author xx
 * @date 2018-09-04 11:58
 */
public interface DataManageTypeApplication {

    /**
     * 查询文件配置地址
     */
    static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/dataManagement.xml";

    /**
     * 新增保存数据管理权限类型
     * 
     * @param opdatamanagetype
     * @return
     */
    String insertOpdatamanagetype(Opdatamanagetype opdatamanagetype);

    /**
     * 保存 数据管理权限类型
     * 
     * @author xx
     * @param params
     */
    String updateOpdatamanagetype(Opdatamanagetype opdatamanagetype);

    /**
     * 加载 数据管理权限类型
     * 
     * @author xx
     * @return SDO
     */
    Opdatamanagetype loadOpdatamanagetype(String id);

    /**
     * 删除 数据管理权限类型
     * 
     * @author xx
     */
    void deleteOpdatamanagetype(List<String> ids);

    /**
     * 查询 数据管理权限类型
     * 
     * @author xx
     * @return SDO
     */
    Map<String, Object> slicedQueryOpdatamanagetype(OpdatamanagetypeQueryRequest queryRequest);

    /**
     * 按层级查询
     * 
     * @param parentId
     * @return
     */
    List<Map<String, Object>> queryDatamanagetypekind(OpdatamanagetypeQueryRequest queryRequest);

    /**
     * 更新排序号
     * 
     * @param map
     */
    void updateOpdatamanagetypeSequence(Map<String, Integer> map);

    /**
     * 移动权限
     * 
     * @param parentId
     * @param ids
     */
    void moveOpdatamanagetype(String parentId, List<String> ids);

    /**
     * 统计资源类别使用
     * 
     * @param kindId
     * @return
     */
    Long countDatamanagetypekindById(String kindId);

    /**
     * 保存数据权限中资源类别
     * 
     * @param id
     * @param kindIds
     */
    void saveDatamanagetypekinds(String id, List<String> kindIds);

    /**
     * 删除已选择的资源类别
     * 
     * @param ids
     */
    void deleteDatamanagetypekinds(List<String> ids);

    /**
     * 查询已选择的资源类别
     * 
     * @param queryRequest
     * @return
     */
    Map<String, Object> queryOpdatamanagetypekind(ParentIdQueryRequest queryRequest);

    /**
     * 根据权限类型查询可用维度资源类型
     * 
     * @param id
     * @return
     */
    List<Map<String, Object>> queryOpdatamanagetypekindByTypeId(String id);

}
