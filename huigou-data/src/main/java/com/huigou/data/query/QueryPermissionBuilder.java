package com.huigou.data.query;

import java.util.List;

import com.huigou.cache.service.ICache;
import com.huigou.context.OrgUnit;
import com.huigou.data.datamanagement.DataManageFieldsGroup;
import com.huigou.data.datamanagement.DataManageResourceGroup;
import com.huigou.data.query.model.SQLModel;

public interface QueryPermissionBuilder {
    static final String DATA_MANAGEMENT_XML_FILE_PATH = "config/uasp/query/bmp/dataManagement.xml";

    /**
     * 获取缓存对象
     * 
     * @return
     */
    ICache getCache();

    /**
     * 查找管理的组织
     * 
     * @param personId
     *            人员ID
     * @param fullIds
     *            ID全路径
     * @param manageType
     *            管理权限编码
     * @return
     */
    List<OrgUnit> findSubordinations(String personId, List<String> fullIds, String manageType);

    /**
     * 应用业务管理权限
     * 
     * @param sql
     *            SQL
     * @param manageType
     *            基础管理权限类别
     * @return
     */
    SQLModel applyManagementPermission(String sql, String manageType);

    /**
     * 树查询权限控制
     * 
     * @author
     * @param sql
     * @param manageType
     * @return SDO
     */
    /**
     * 为树型控件应用业务管理权限
     * 
     * @param sql
     *            SQL
     * @param manageType
     *            基础管理权限类别
     * @return
     */
    SQLModel applyManagementPermissionForTree(String sql, String manageType);

    /**
     * 当前用户是否具有管理权限
     * 
     * @param manageType
     *            基础管理权限类别
     * @param fullId
     *            给管理的ID全路径
     * @return
     */
    boolean hasManagementPermission(String manageType, String fullId);

    /**
     * 清空缓存
     */
    void removeCache();

    /**
     * 根据类别清空缓存
     * 
     * @param kind
     *            缓存类别
     */
    void removeCacheByKind(String kind);

    /**
     * 根据权限类别编码及人员ID查询数据权限资源
     * 
     * @param personId
     * @param code
     * @return
     */
    List<DataManageResourceGroup> findDataManagementByCode(String code, String personId);

    /**
     * 根据业务类别编码查询权限控制字段
     * 
     * @param code
     * @return
     */
    DataManageFieldsGroup findDataManageFieldsByCode(String code);

}
