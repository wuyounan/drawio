package com.huigou.uasp.bmp.dataManage.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagedetail;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagedetailresource;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagement;
import com.huigou.uasp.bmp.dataManage.domain.query.DataManagePermissionsQueryRequest;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagedetailQueryRequest;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagedetailresourceQueryRequest;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagementQueryRequest;

/**
 * 数据权限授权
 * 
 * @author xx
 * @date 2018-09-05 17:15
 */
public interface DataManagementApplication {
    /**
     * 查询文件配置地址
     */
    static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/dataManagement.xml";

    Integer countDatamanagedetailByDataManageId(String dataManageId);

    /**
     * 根据权限类型查询可用维度资源类型
     * 
     * @param id
     * @return
     */
    Map<String, String> queryDataManageResourcekindByTypeId(String id);

    /**
     * 保存 数据管理权限取值定义
     * 
     * @author xx
     * @param params
     */
    String saveOpdatamanagedetail(Opdatamanagedetail opdatamanagedetail);

    /**
     * 加载 数据管理权限取值定义
     * 
     * @author xx
     * @return SDO
     */
    Opdatamanagedetail loadOpdatamanagedetail(String id);

    /**
     * 删除 数据管理权限取值定义
     * 
     * @author xx
     */
    void deleteOpdatamanagedetail(List<String> ids);

    /**
     * 查询 数据管理权限取值定义
     * 
     * @author xx
     * @return SDO
     */
    Map<String, Object> slicedQueryOpdatamanagedetail(OpdatamanagedetailQueryRequest queryRequest);

    /**
     * 保存 数据管理权限包含维度资源
     * 
     * @author xx
     * @param params
     */
    void saveOpdatamanagedetailresource(Opdatamanagedetailresource opdatamanagedetailresource);

    /**
     * 删除 数据管理权限包含维度资源
     * 
     * @author xx
     */
    void deleteOpdatamanagedetailresource(List<String> ids);

    /**
     * 查询 数据管理权限包含维度资源
     * 
     * @author xx
     * @return SDO
     */
    Map<String, Object> queryOpdatamanagedetailresource(OpdatamanagedetailresourceQueryRequest queryRequest);

    /**
     * 保存 数据管理权限授权
     * 
     * @author xx
     * @param params
     */
    void saveOpdatamanagement(String managerId, List<Opdatamanagement> datas);

    /**
     * 删除 数据管理权限授权
     * 
     * @author xx
     */
    void deleteOpdatamanagement(List<String> ids);

    /**
     * 查询 数据管理权限授权
     * 
     * @author xx
     * @return SDO
     */
    Map<String, Object> queryOpdatamanagement(OpdatamanagementQueryRequest queryRequest);

    /**
     * 查询数据管理权限继承数据
     * 
     * @param queryRequest
     * @return
     */
    Map<String, Object> slicedQueryDataManagementByOrgFullId(OpdatamanagementQueryRequest queryRequest);

    /**
     * 查询继承权限包含资源
     * 
     * @param queryRequest
     * @return
     */
    Map<String, Object> slicedQueryDataManageResourceByOrgFullId(OpdatamanagementQueryRequest queryRequest);

    /**
     * 根据资源定义ID查询授权情况
     * 
     * @param queryRequest
     * @return
     */
    Map<String, Object> slicedQueryDataManageByDetailId(DataManagePermissionsQueryRequest queryRequest);

    /**
     * 查询具有数据权限的用户
     * 
     * @param queryRequest
     * @return
     */
    @Deprecated
    Map<String, Object> slicedQueryPersonAsDataManage(DataManagePermissionsQueryRequest queryRequest);

}
