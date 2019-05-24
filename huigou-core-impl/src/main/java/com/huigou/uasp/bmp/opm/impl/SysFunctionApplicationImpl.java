package com.huigou.uasp.bmp.opm.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.CheckBaseInfoDuplicateParameter;
import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.opm.application.PermissionApplication;
import com.huigou.uasp.bmp.opm.application.SysFunctionApplication;
import com.huigou.uasp.bmp.opm.domain.model.access.Permission;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionNodeKind;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionResourceKind;
import com.huigou.uasp.bmp.opm.domain.model.resource.ResourceKind;
import com.huigou.uasp.bmp.opm.domain.model.resource.ResourceOperation;
import com.huigou.uasp.bmp.opm.domain.model.resource.SysFunction;
import com.huigou.uasp.bmp.opm.repository.org.PermissionRepository;
import com.huigou.uasp.bmp.opm.repository.org.ResourceOperationRepository;
import com.huigou.uasp.bmp.opm.repository.org.SysFunctionRepository;

public class SysFunctionApplicationImpl extends BaseApplication implements SysFunctionApplication {

    private SysFunctionRepository sysFunctionRepository;

    private PermissionRepository permissionRepository;

    private ResourceOperationRepository resourceOperationRepository;

    private PermissionApplication permissionApplication;

    public void setSysFunctionRepository(SysFunctionRepository sysFunctionRepository) {
        this.sysFunctionRepository = sysFunctionRepository;
    }

    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public void setResourceOperationRepository(ResourceOperationRepository resourceOperationRepository) {
        this.resourceOperationRepository = resourceOperationRepository;
    }

    public void setPermissionApplication(PermissionApplication permissionApplication) {
        this.permissionApplication = permissionApplication;
    }

    @SuppressWarnings("unchecked")
    private void checkConstraints(SysFunction sysFunction) {
        Assert.notNull(sysFunction, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "sysFunction"));

        CheckBaseInfoDuplicateParameter checkParameter = new CheckBaseInfoDuplicateParameter();
        checkParameter.setCheckParentIdAndCodeAndName(sysFunction.getParentId(), sysFunction.getId(), sysFunction.getCode(), sysFunction.getName());
        checkParameter.checkConstraints();

        List<SysFunction> duplicateEntities = (List<SysFunction>) this.commonDomainService.findDuplicateEntities(SysFunction.class, checkParameter);
        SysFunction other = null;
        if (duplicateEntities.size() > 0) {
            other = duplicateEntities.get(0);
        }
        sysFunction.checkConstraints(other);
    }

    @Override
    public String insertSysFunction(SysFunction sysFunction) {
        Assert.notNull(sysFunction, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "sysFunction"));
        sysFunction = (SysFunction) this.commonDomainService.saveTreeEntity(sysFunction, sysFunctionRepository);
        buildPermission(sysFunction.getFullId());
        return sysFunction.getId();
    }

    @Override
    public String updateSysFunction(SysFunction sysFunction) {
        SysFunction dbSysFunction = this.sysFunctionRepository.findOne(sysFunction.getId());
        Assert.notNull(dbSysFunction,
                       MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, dbSysFunction.getId(), dbSysFunction.getClass().getName()));
        String oldName = dbSysFunction.getName();
        String oldFullName = dbSysFunction.getFullName();
        dbSysFunction.fromEntity(sysFunction);

        checkConstraints(dbSysFunction);
        SysFunction parent = this.sysFunctionRepository.findOne(dbSysFunction.getParentId());
        dbSysFunction.buildFullIdAndName(parent);

        if (!dbSysFunction.getName().equals(oldName)) {
            this.commonDomainService.updateChildenFullName(SysFunction.class, dbSysFunction.getFullId(), oldFullName, dbSysFunction.getFullName());
            // 更新权限
            Permission permission = this.permissionRepository.findFunctionByResourceId(dbSysFunction.getId());
            String oldPermissionFullName = permission.getFullName();
            Permission parentPermission = permissionRepository.findOne(permission.getParentId());
            if (permission != null && parentPermission != null) {
                permission.setName(dbSysFunction.getName());
                permission.buildFullIdAndName(parentPermission);
                this.commonDomainService.updateChildenFullName(Permission.class, permission.getFullId(), oldPermissionFullName, permission.getFullName());
            }
            permissionRepository.save(permission);
        }
        this.sysFunctionRepository.save(dbSysFunction);

        return dbSysFunction.getId();
    }

    @Override
    public SysFunction loadSysFunction(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.sysFunctionRepository.findOne(id);
    }

    @Override
    public void deleteSysFunctions(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));
        List<SysFunction> sysFunctions = this.sysFunctionRepository.findAll(ids);
        Integer count = 0;
        for (SysFunction sysFunction : sysFunctions) {
            count = this.sysFunctionRepository.countByParentId(sysFunction.getId());
            Assert.isTrue(count.equals(0), MessageSourceContext.getMessage(MessageConstants.CAN_NOT_DELETE_HAS_CHILDREN, sysFunction.getName()));

            count = permissionRepository.countByResourceId(sysFunction.getId());
            Assert.isTrue(count.equals(0), String.format("功能“%s” 已生成权限，不能删除。", sysFunction.getName()));
        }
        this.sysFunctionRepository.delete(sysFunctions);
    }

    @Override
    public void updateSysFunctionsStatus(List<String> ids, Integer status) {
        this.commonDomainService.updateStatus(SysFunction.class, ids, status);
        this.permissionRepository.updateStatusByResourceId(ids, status);
    }

    @Override
    public Map<String, Object> querySysFunctions(ParentAndCodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "sysFunction");
        return this.sqlExecutorDao.executeQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Integer getSysFunctionNextSequence(String parentId) {
        Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
        return this.commonDomainService.getNextSequence(SysFunction.class, CommonDomainConstants.PARENT_ID_FIELD_NAME, parentId);
    }

    @Override
    public void updateSysFunctionsSequence(Map<String, Integer> params) {
        Assert.isTrue(params != null && params.size() > 0, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "params"));
        this.commonDomainService.updateSequence(SysFunction.class, params);
    }

    @Override
    public void buildPermission(String fullId) {
        Assert.hasText(fullId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "fullId"));
        List<SysFunction> sysFunctions = this.sysFunctionRepository.findByFullIdLikeOrderByFullId(fullId + "%");

        List<Permission> permissions = new ArrayList<Permission>();
        Permission parent = null, permission, child;
        Integer count;
        for (SysFunction sysFunction : sysFunctions) {
            count = this.permissionRepository.countByResourceId(sysFunction.getId());
            if (count == 0) {
                if (sysFunction.isRoot()) {
                    permission = sysFunction.toPermission();
                    permission = permissionRepository.save(permission);
                    permission.buildFullIdAndName(parent);
                    permissions.add(permission);
                } else {
                    parent = this.permissionRepository.findFunctionByResourceId(sysFunction.getParentId());
                    Assert.notNull(parent, String.format("功能“%s”的父节点未导入。", sysFunction.getName()));

                    permission = sysFunction.toPermission();
                    permission.setParentId(parent.getId());

                    permission = permissionRepository.save(permission);
                    permission.buildFullIdAndName(parent);

                    permissions.add(permission);
                    if (sysFunction.isFun()) {
                        List<ResourceOperation> commonOperations = resourceOperationRepository.findCommonOperations(ResourceKind.FUN,
                                                                                                                    ResourceOperation.COMMON_OPERATION);
                        for (ResourceOperation resourceOperation : commonOperations) {
                            child = sysFunction.toPermission();

                            child.setCode(String.format("%s:%s", permission.getCode(), resourceOperation.getCode()));
                            child.setName(permission.getName() + resourceOperation.getName());
                            child.setNodeKindId(PermissionNodeKind.PERMISSION.getId());
                            child.setOperationId(resourceOperation.getId());
                            child.setParentId(permission.getId());
                            child.setSequence(resourceOperation.getSequence());
                            child = permissionRepository.save(child);
                            child.buildFullIdAndName(permission);

                            permissions.add(child);
                        }
                    }
                }
            }
        }
        if (permissions.size() > 0) {
            this.permissionRepository.save(permissions);
        }
    }

    @Override
    public void moveFunctions(String parentId, List<String> ids) {
        Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
        this.commonDomainService.moveTree(SysFunction.class, ids, parentId);
        this.permissionApplication.movePermission(parentId, ids, PermissionResourceKind.FUN);
    }

}
