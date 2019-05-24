package com.huigou.uasp.bmp.opm.application;

import java.util.List;

import com.huigou.data.domain.model.TreeEntity;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionResourceKind;

/**
 * 权限编辑
 * 
 * @author xx
 */
public interface PermissionApplication {
    /**
     * 创建新权限
     * 
     * @param treeEntitys
     */
    void buildPermission(List<? extends TreeEntity> treeEntitys, PermissionResourceKind resourceKind);

    /**
     * 修改权限
     * 
     * @param treeEntity
     * @param oldName
     * @param oldFullName
     */
    void updatePermission(TreeEntity treeEntity, String oldName, String oldFullName, PermissionResourceKind resourceKind);

    Integer countByResourceId(String resourceId);

    /**
     * 移动权限
     * 
     * @param parentResourceId
     * @param resourceIds
     * @param resourceKind
     */
    void movePermission(String parentResourceId, List<String> resourceIds, PermissionResourceKind resourceKind);
}
