package com.huigou.uasp.bmp.opm.application;

import java.util.List;
import java.util.Map;

import com.huigou.context.OrgUnit;
import com.huigou.data.query.model.SQLModel;

public interface PermissionBuilder {

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
     * 根据功能ID或编码查询当前用户的界面元素权限
     * 
     * @author
     * @param function
     * @param operator
     * @param isId
     *            void
     */
    List<Map<String, Object>> queryUIElementPermissionsByFunction(String function, String personId, boolean isId);

    /**
     * 查询当前处理环节的界面元素权限
     * 
     * @param procUnitHandlerId
     *            流程环节ID
     * @return
     */
    List<Map<String, Object>> queryUIElementPermissionsByProcUnitHandlerId(String procUnitHandlerId);

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

}
