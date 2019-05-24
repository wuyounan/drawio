package com.huigou.uasp.bmp.opm.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.data.domain.EntityUtil;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.TreeEntity;
import com.huigou.uasp.bmp.opm.application.PermissionApplication;
import com.huigou.uasp.bmp.opm.domain.model.access.Permission;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionNodeKind;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionResourceKind;
import com.huigou.uasp.bmp.opm.repository.org.PermissionRepository;
import com.huigou.util.ClassHelper;

public class PermissionApplicationImpl extends BaseApplication implements PermissionApplication {

    private PermissionRepository permissionRepository;

    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    private String getSqlByName(String name) {
        return this.commonDomainService.getSqlByName(name);
    }

    /**
     * 转换为权限对象
     * 
     * @param treeEntity
     * @param resourceKind
     * @return
     */
    private Permission entityToPermission(TreeEntity treeEntity, PermissionResourceKind resourceKind) {
        Permission permission = new Permission();
        permission.setCode(String.format("%s:%s", resourceKind.getId(), treeEntity.getCode()));
        permission.setName(treeEntity.getName());
        permission.setResourceKindId(resourceKind.getId());
        permission.setResourceId(treeEntity.getId());
        permission.setStatus(treeEntity.getStatus());
        permission.setSequence(treeEntity.getSequence());
        Object nodeKindId = ClassHelper.getProperty(treeEntity, "permissionNodeKindId");
        Assert.notNull(nodeKindId, String.format("“%s”节点权限类型为空。", treeEntity.getName()));
        permission.setNodeKindId(nodeKindId.toString());
        return permission;
    }

    @Override
    @Transactional
    public void buildPermission(List<? extends TreeEntity> treeEntitys, PermissionResourceKind resourceKind) {
        Permission parent = null, permission;
        String parentId = null;
        Integer count;
        List<Permission> permissions = new ArrayList<Permission>();
        for (TreeEntity treeEntity : treeEntitys) {
            parentId = treeEntity.getParentId();
            count = this.permissionRepository.countByResourceId(treeEntity.getId());
            if (count == 0) {
                // 父级节点是根节点
                if (parentId.equals(Permission.FUN_ROOT_ID)) {
                    List<Permission> list = permissionRepository.findByParentIdAndResourceKindId(Permission.ROOT_ID, resourceKind.getId());
                    if (list == null || list.size() == 0) {
                        Permission newRoot = new Permission();
                        newRoot.setCode(String.format("%s:%s", resourceKind.getId(), "root"));
                        newRoot.setName(resourceKind.getDisplayName());
                        newRoot.setParentId(Permission.ROOT_ID);
                        newRoot.setResourceKindId(resourceKind.getId());
                        newRoot.setNodeKindId(PermissionNodeKind.FOLDER.getId());
                        parent = (Permission) this.commonDomainService.saveTreeEntity(newRoot, this.permissionRepository);
                    } else {
                        parent = list.get(0);
                    }
                } else {
                    parent = this.permissionRepository.findByResourceIdAndResourceKindId(treeEntity.getParentId(), resourceKind.getId());
                }
                Assert.notNull(parent, String.format("“%s”的父节点未导入权限。", treeEntity.getName()));
                permission = this.entityToPermission(treeEntity, resourceKind);
                permission.setParentId(parent.getId());
                permission = permissionRepository.save(permission);
                permission.buildFullIdAndName(parent);
                permissions.add(permission);
            }
        }
        if (permissions.size() > 0) {
            this.permissionRepository.save(permissions);
        }
    }

    @Override
    @Transactional
    public void updatePermission(TreeEntity treeEntity, String oldName, String oldFullName, PermissionResourceKind resourceKind) {
        if (!treeEntity.getName().equals(oldName)) {
            // 更新权限
            Permission permission = this.permissionRepository.findByResourceIdAndResourceKindId(treeEntity.getId(), resourceKind.getId());
            if (permission != null) {
                String oldPermissionFullName = permission.getFullName();
                Permission parentPermission = permissionRepository.findOne(permission.getParentId());
                if (parentPermission != null) {
                    permission.setName(treeEntity.getName());
                    permission.buildFullIdAndName(parentPermission);
                    this.commonDomainService.updateChildenFullName(Permission.class, permission.getFullId(), oldPermissionFullName, permission.getFullName());
                }
                permissionRepository.save(permission);
            }
        }
    }

    @Override
    public Integer countByResourceId(String resourceId) {
        return permissionRepository.countByResourceId(resourceId);
    }

    /**
     * 根据资源ID查询权限
     * 
     * @param parentResourceId
     * @param resourceKind
     * @return
     */
    private Permission findPermissionByResourceId(String resourceId, PermissionResourceKind resourceKind) {
        Permission permission = null;
        if (resourceId.equals(Permission.FUN_ROOT_ID)) {
            List<Permission> list = permissionRepository.findByParentIdAndResourceKindId(Permission.ROOT_ID, resourceKind.getId());
            if (list != null && list.size() > 0) {
                permission = list.get(0);
            }
        } else {
            permission = this.permissionRepository.findByResourceIdAndResourceKindId(resourceId, resourceKind.getId());
        }
        return permission;
    }

    @Override
    public void movePermission(String parentResourceId, List<String> resourceIds, PermissionResourceKind resourceKind) {
        Permission moveToParent = null;
        if (resourceKind == PermissionResourceKind.FUN) {
            moveToParent = this.permissionRepository.findFunctionByResourceId(parentResourceId);
        } else {
            moveToParent = this.findPermissionByResourceId(parentResourceId, resourceKind);
        }
        if (moveToParent == null) {
            return;
        }
        String tableName = EntityUtil.getTableName(Permission.class);
        String parentFullId = moveToParent.getFullId();
        String chlidFullId = null;
        Permission permission;
        Permission oldParent;
        String sql = getSqlByName("updateFullIdAndName");
        List<String> ids = new ArrayList<String>(resourceIds.size());
        for (String resourceId : resourceIds) {
            if (resourceKind == PermissionResourceKind.FUN) {
                permission = this.permissionRepository.findFunctionByResourceId(resourceId);
            } else {
                permission = this.permissionRepository.findByResourceIdAndResourceKindId(resourceId, resourceKind.getId());
            }
            if (permission == null) {
                continue;
            }
            chlidFullId = permission.getFullId();
            // 校验不能循环引用
            Assert.isTrue(parentFullId.indexOf(chlidFullId) == -1, String.format("权限[%s]无法移动到[%s]", permission.getName(), moveToParent.getName()));
            oldParent = this.permissionRepository.findOne(permission.getParentId());
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("parentNewFullId", parentFullId);
            param.put("parentOldFullId", oldParent.getFullId());
            param.put("parentNewFullName", moveToParent.getFullName());
            param.put("parentOldFullName", oldParent.getFullName());
            param.put("likeFullId", chlidFullId + "%");
            this.generalRepository.updateByNativeSql(String.format(sql, tableName), param);
            ids.add(permission.getId());
        }
        if (ids.size() > 0) {
            sql = this.getSqlByName("moveSqlByParentId");
            sql = String.format(sql, tableName, CommonDomainConstants.PARENT_ID_COLUMN_NAME);
            Map<String, Object> parameterMap = new HashMap<String, Object>(2);
            parameterMap.put(CommonDomainConstants.PARENT_ID_FIELD_NAME, moveToParent.getId());
            parameterMap.put(CommonDomainConstants.IDS_FIELD_NAME, ids);
            this.generalRepository.updateByNativeSql(sql, parameterMap);
        }
    }

}
