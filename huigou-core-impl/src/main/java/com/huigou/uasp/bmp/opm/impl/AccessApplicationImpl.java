package com.huigou.uasp.bmp.opm.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.RoleKind;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.context.TmspmConifg;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.Creator;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.data.domain.query.QueryParameter;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.opm.TreeNodeKind;
import com.huigou.uasp.bmp.opm.application.AccessApplication;
import com.huigou.uasp.bmp.opm.application.OrgApplication;
import com.huigou.uasp.bmp.opm.application.PermissionBuilder;
import com.huigou.uasp.bmp.opm.application.TenantApplication;
import com.huigou.uasp.bmp.opm.domain.model.access.Permission;
import com.huigou.uasp.bmp.opm.domain.model.access.Role;
import com.huigou.uasp.bmp.opm.domain.model.access.RolePermission;
import com.huigou.uasp.bmp.opm.domain.model.access.UIElementPermission;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.Tenant;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionNodeKind;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionResourceKind;
import com.huigou.uasp.bmp.opm.domain.model.resource.ResourceKind;
import com.huigou.uasp.bmp.opm.domain.model.resource.ResourceOperation;
import com.huigou.uasp.bmp.opm.domain.model.resource.SysFunction;
import com.huigou.uasp.bmp.opm.domain.query.AuthorizationDesc;
import com.huigou.uasp.bmp.opm.domain.query.AuthorizationsQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.AuthorizedPermissionsByOrgFullIdQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.PermissionsByRoleIdQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.RolesQueryRequestQueryRequest;
import com.huigou.uasp.bmp.opm.repository.org.PermissionRepository;
import com.huigou.uasp.bmp.opm.repository.org.ResourceOperationRepository;
import com.huigou.uasp.bmp.opm.repository.org.RolePermissionRepository;
import com.huigou.uasp.bmp.opm.repository.org.RoleRepository;
import com.huigou.uasp.bmp.opm.repository.org.SysFunctionRepository;
import com.huigou.uasp.bmp.opm.repository.org.UIElementPermissionRepository;
import com.huigou.util.ClassHelper;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

public class AccessApplicationImpl extends BaseApplication implements AccessApplication {

    private OrgApplication orgApplication;

    private TenantApplication tenantApplication;

    private RoleRepository roleRepository;

    private PermissionRepository permissionRepository;

    private RolePermissionRepository rolePermissionRepository;

    private ResourceOperationRepository resourceOperationRepository;

    private SysFunctionRepository sysFunctionRepository;

    private PermissionBuilder permissionBuilder;

    private UIElementPermissionRepository uiElementPermissionRepository;

    private TmspmConifg tmspmConifg;

    public void setOrgApplication(OrgApplication orgApplication) {
        this.orgApplication = orgApplication;
    }

    public void setTenantApplication(TenantApplication tenantApplication) {
        this.tenantApplication = tenantApplication;
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public void setRolePermissionRepository(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public void setResourceOperationRepository(ResourceOperationRepository resourceOperationRepository) {
        this.resourceOperationRepository = resourceOperationRepository;
    }

    public void setSysFunctionRepository(SysFunctionRepository sysFunctionRepository) {
        this.sysFunctionRepository = sysFunctionRepository;
    }

    public void setPermissionBuilder(PermissionBuilder permissionBuilder) {
        this.permissionBuilder = permissionBuilder;
    }

    public void setUiElementPermissionRepository(UIElementPermissionRepository uiElementPermissionRepository) {
        this.uiElementPermissionRepository = uiElementPermissionRepository;
    }

    public void setTmspmConifg(TmspmConifg tmspmConifg) {
        this.tmspmConifg = tmspmConifg;
    }

    private void applyTspmEditRule(Role role, String operationKind) {
        if (tmspmConifg.isUseTspm() && tmspmConifg.isEnableTspm()) {
            if (RoleKind.isTspm(role.getKindId())) {
                if (operationKind.equals("delete")) {
                    throw new ApplicationException(String.format("三员管理已启用，不能删除“%s”。", role.getName()));
                } else if (operationKind.equals("edit")) {
                    throw new ApplicationException(String.format("三员管理已启用，不能修改“%s”。", role.getName()));
                } else if (operationKind.equals("allocate")) {
                    throw new ApplicationException(String.format("三员管理已启用，不能为“%s”分配权限。", role.getName()));
                }
            }
        }
    }

    @Override
    public String saveRole(Role role) {
        Assert.notNull(role, "参数role不能为空。");

        role = this.commonDomainService.loadAndFillinProperties(role, Role.class);

        role.checkConstraints();

        // 租户维护的角色，需设置和验证租户
        if (!role.isGlobalRole()) {
            Tenant tenant;
            if (role.isNew()) {
                tenant = this.tenantApplication.loadTenant(ThreadLocalUtil.getOperatorTenantId());
            } else {
                tenant = this.tenantApplication.loadTenant(role.getTenantId());
            }
            Assert.notNull(tenant, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, role.getTenantId(), "租户"));
            role.setTenantId(tenant.getId());
        }

        applyTspmEditRule(role, "edit");

        String oldName = null;
        Role dbRole;
        Long count;
        if (!role.isNew()) {
            dbRole = this.loadRole(role.getId());
            Assert.notNull(dbRole, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, role.getId(), "角色"));
            // 叶节点 --> 分类，需检查是否分配权限，和被授权
            if (!dbRole.getNodeKindId().equals(role.getNodeKindId()) && role.getNodeKindId().equals(TreeNodeKind.LIMB.getId())) {

                count = rolePermissionRepository.countByRoleId(role.getId());
                Assert.isTrue(count.equals(0l), String.format("角色“%s”已有分配了权限，不能修改为文件夹。", role.getName()));

                String sql = "select count(0) from SA_OPAuthorize where role_id = :roleId";
                count = this.generalRepository.coungByNativeSql(sql, QueryParameter.buildParameters("roleId", dbRole.getId()));
                Assert.isTrue(count.equals(0l), String.format("角色“%s”已有被授权，不能修改为文件夹。", role.getName()));
            }
            oldName = dbRole.getName();
            dbRole.fromEntity(role);
        } else {
            dbRole = role;
        }

        dbRole = (Role) this.commonDomainService.saveTreeEntity(dbRole, roleRepository, oldName);
        return dbRole.getId();
    }

    @Override
    public Role loadRole(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.roleRepository.findOne(id);
    }

    private String getAccessQuerySqlByName(String name) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "access");
        return queryDescriptor.getSqlByName(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deleteRoles(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));

        List<Role> roles = this.roleRepository.findAll(ids);

        if (tmspmConifg.isUseTspm() && tmspmConifg.isEnableTspm()) {
            for (Role role : roles) {
                applyTspmEditRule(role, "delete");
            }
        }

        List<AuthorizationDesc> authorizationDescs;
        AuthorizationDesc authorizationDesc;
        Long count;

        for (Role role : roles) {
            count = roleRepository.countByParentId(role.getId());
            Assert.isTrue(count.equals(0l), MessageSourceContext.getMessage(MessageConstants.CAN_NOT_DELETE_HAS_CHILDREN, role.getName()));

            String sql = this.getAccessQuerySqlByName("queryFirstAuthorizationByRoleId");
            authorizationDescs = (List<AuthorizationDesc>) this.generalRepository.query(sql, QueryParameter.buildParameters("roleId", role.getId()), 0, 1);

            if (authorizationDescs.size() == 1) {
                authorizationDesc = authorizationDescs.get(0);
                Assert.isTrue(false,
                              MessageSourceContext.getMessage(MessageConstants.OBJECT_REFERENCED_BY_WHO, role.getName(), authorizationDesc.getOrgFullName()));
            }
            count = rolePermissionRepository.countByRoleId(role.getId());
            Assert.isTrue(count.equals(0l), String.format("角色“%s”已有分配了权限，不能删除。", role.getName()));
        }
        this.roleRepository.delete(roles);
    }

    @Override
    public void updateRolesSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(Role.class, params);
    }

    @Override
    public void moveRoles(List<String> ids, String parentId) {
        Role parentRole = this.loadRole(parentId);
        Assert.notNull(parentRole, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, parentId, "角色"));
        Assert.isTrue(parentRole.isFolder(), "您不能移动角色到叶节点，请选择文件夹。");
        this.commonDomainService.moveTree(Role.class, ids, parentId);
    }

    @Override
    public void moveTenantRoles(List<String> ids, String parentId) {
        Role parentRole = this.loadRole(parentId);
        Assert.notNull(parentRole, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, parentId, "角色"));
        Assert.isTrue(!parentRole.isGlobalRole(), "您不能移动角色到通用角色树下。");
        moveRoles(ids, parentId);
    }

    @Override
    public List<Map<String, Object>> queryRoles(String tenantKindId, String parentId) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "role");
        String tenantId;
        if (Role.GLOBAL_TENANT_KIND.equals(tenantKindId)) {
            tenantId = Role.GLOBAL_TENANT_KIND;
        } else {
            tenantId = ThreadLocalUtil.getOperatorTenantId();
        }
        String sql = queryDescriptor.getSqlByName("queryRolesByParentId");
        StringBuilder sb = new StringBuilder(sql);
        // 使用三员管理，未启用三员，超级管理员可维护，启用三员后，安全员可维护
        if (tmspmConifg.isUseTspm()) {
            Operator operator = ThreadLocalUtil.getOperator();
            boolean isProcessedCondition = false;
            if (!tmspmConifg.isEnableTspm()) {
                if (operator.getRoleKind() == RoleKind.SUPER_ADMINISTRATOR) {
                    isProcessedCondition = true;
                    sb.append("and (role_kind_id in ('administrator', 'securityGuard', 'auditor') or node_kind_id = 1)");
                }
            } else {
                if (operator.getRoleKind() == RoleKind.SECURITY_GUARD) {
                    isProcessedCondition = true;
                    sb.append("and (role_kind_id = 'common' or node_kind_id = 1)");
                }
            }
            if (!isProcessedCondition) {
                sb.append("and 1 = 2");
            }
        }
        sb.append(" order by t.sequence");
        return sqlExecutorDao.queryToListMap(sb.toString(), parentId, tenantId);
    }

    @Override
    public Map<String, Object> slicedQueryRoles(RolesQueryRequestQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "role");

        if (Role.GLOBAL_TENANT_KIND.equals(queryRequest.getTenantKindId())) {
            queryRequest.setTenantId(Role.GLOBAL_TENANT_KIND);
        } else {
            queryRequest.setTenantId(ThreadLocalUtil.getOperatorTenantId());
        }

        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
     // 使用三员管理，未启用三员，超级管理员可维护，启用三员后，安全员可维护
        if (tmspmConifg.isUseTspm()) {
            Operator operator = ThreadLocalUtil.getOperator();
            boolean isProcessedCondition = false;
            if (!tmspmConifg.isEnableTspm()) {
                if (operator.getRoleKind() == RoleKind.SUPER_ADMINISTRATOR) {
                    isProcessedCondition = true;
                    queryModel.addCriteria(" and (role_kind_id in ('administrator', 'securityGuard', 'auditor') or node_kind_id = 1)");
                }
            } else {
                if (operator.getRoleKind() == RoleKind.SECURITY_GUARD) {
                    isProcessedCondition = true;
                    queryModel.addCriteria(" and (role_kind_id = 'common' or node_kind_id = 1)");
                }
            }
            if (!isProcessedCondition) {
                queryModel.addCriteria(" and 1 = 2");
            }
        }
        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }

    @Override
    public Integer getRoleNextSequence(String parentId) {
        Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
        return this.commonDomainService.getNextSequence(Role.class, CommonDomainConstants.PARENT_ID_FIELD_NAME, parentId);
    }

    @Override
    @Transactional
    public void saveInitPermissionResourceKind() {
        Map<String, String> map = PermissionResourceKind.getData();
        for (String resourceKindId : map.keySet()) {
            // 查询权限跟节点是否存在
            Integer count = permissionRepository.countByParentIdAndResourceKindId(Permission.ROOT_ID, resourceKindId);
            if (count.equals(0)) {
                // 新增一条记录
                Permission permission = new Permission();
                permission.setCode(String.format("%s:%s", resourceKindId, "root"));
                permission.setName(map.get(resourceKindId));
                permission.setParentId(Permission.ROOT_ID);
                permission.setResourceKindId(resourceKindId);
                permission.setNodeKindId(PermissionNodeKind.FOLDER.getId());
                this.savePermission(permission);
            }
        }
    }

    @Override
    public String savePermission(Permission permission) {
        permission = (Permission) this.commonDomainService.saveTreeEntity(permission, this.permissionRepository, true, false);
        return permission.getId();
    }

    @Override
    public void updatePermission(Permission permission) {
        Permission dbPermission = this.loadPermission(permission.getId());
        Assert.notNull(dbPermission,
                       MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, permission.getId(), permission.getClass().getName()));

        String oldName = dbPermission.getName();
        dbPermission.fromEntity(permission);
        this.commonDomainService.saveTreeEntity(dbPermission, permissionRepository, oldName, true, false);
    }

    @Override
    public void updatePermissionsStatus(List<String> ids, Integer status) {
        this.commonDomainService.updateStatus(Permission.class, ids, status);
    }

    @Override
    public Permission loadPermission(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.permissionRepository.findOne(id);
    }

    @Override
    public void deletePermissions(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));
        List<Permission> permissions = this.permissionRepository.findAll(ids);
        RolePermission rolePermission;
        Role role;
        Integer count;
        for (Permission permission : permissions) {
            count = permissionRepository.countByParentId(permission.getId());
            Assert.isTrue(count.equals(0), MessageSourceContext.getMessage(MessageConstants.CAN_NOT_DELETE_HAS_CHILDREN, permission.getName()));

            rolePermission = rolePermissionRepository.findFirstByPermissionId(permission.getId());
            if (rolePermission != null) {
                role = this.loadRole(rolePermission.getRoleId());
                Assert.isTrue(false, MessageSourceContext.getMessage(MessageConstants.OBJECT_REFERENCED_BY_WHO, permission.getName(), role.getName()));
            }
        }
        this.permissionRepository.delete(permissions);
    }

    @Override
    public void updatePermissionsSequence(Map<String, Integer> permissions) {
        Assert.notEmpty(permissions, "参数permissions不能为空。");
        this.commonDomainService.updateSequence(Permission.class, permissions);
    }

    @Override
    public void movePermissions(List<String> ids, String parentId) {
        this.commonDomainService.moveForTree(Permission.class, ids, CommonDomainConstants.PARENT_ID_COLUMN_NAME, parentId);
    }

    @Override
    public Map<String, Object> queryPermissions(FolderAndCodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "permission");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        queryModel.putDictionary("nodeKindId", PermissionNodeKind.getAllMap());
        return this.sqlExecutorDao.executeQuery(queryModel);
    }

    @Override
    public void saveUIElementPermissions(List<UIElementPermission> uiElementPermissions) {
        Assert.notEmpty(uiElementPermissions, "参数uiElementPermissions不能为空。");
        this.uiElementPermissionRepository.save(uiElementPermissions);
    }

    @Override
    public void deleteUIElementPermissions(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        List<UIElementPermission> uiElementPermissions = this.uiElementPermissionRepository.findAll(ids);
        this.uiElementPermissionRepository.deleteInBatch(uiElementPermissions);
    }

    @Override
    public Map<String, Object> slicedQueryUIElementPermissions(ParentIdQueryRequest queryRequest) {
        Assert.hasText(queryRequest.getParentId(), "参数permissionId不能为空。");
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "permissionQueryUIElement");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Integer getPermissionNextSequence(String parentId) {
        return this.commonDomainService.getNextSequence(Permission.class, CommonDomainConstants.PARENT_ID_FIELD_NAME, parentId);
    }

    @Override
    public void allocateRoles(String orgId, List<String> roleIds) {
        Assert.hasText(orgId, "参数orgId不能为空。");
        Assert.notEmpty(roleIds, "参数roleIds不能为空。");

        Org org = this.orgApplication.loadOrg(orgId);
        Assert.notNull(org, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, orgId, "组织"));

        List<Role> roles = this.roleRepository.findAll(roleIds);
        Assert.isTrue(roleIds.size() == roles.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "角色"));

        org.buildRoles(roles);

        this.orgApplication.updateOrg(org);
    }

    @Override
    public void deallocateRoles(String orgId, List<String> roleIds) {
        Assert.hasText(orgId, "参数orgId不能为空。");
        Assert.notEmpty(roleIds, "参数roleIds不能为空。");

        Org org = this.orgApplication.loadOrg(orgId);
        Assert.notNull(org, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, orgId, "组织"));

        org.removeRoles(roleIds);

        this.orgApplication.updateOrg(org);
    }

    @Override
    public Map<String, Object> queryAuthorizations(AuthorizationsQueryRequest queryRequest) {
        Assert.hasText(queryRequest.getOrgId(), "参数orgId不能为空。");
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "authorization");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public List<Map<String, Object>> queryPermissionsByParentId(String parentId) {
        Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "permission");
        String sql = queryDescriptor.getSqlByName("queryByParentId");
        return this.sqlExecutorDao.queryToListMap(sql, parentId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Permission> queryAllocatedPermissions(String roleId, String parentId) {
        Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
        Assert.hasText(roleId, "角色ID不能为空。");

        Permission permission = this.permissionRepository.findOne(parentId);

        String sql = this.getAccessQuerySqlByName("queryAllocatedPermissions");
        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("roleId", roleId);
        params.put("fullId", permission.getFullId() + "/%");
        // params.put("resourceKindId", ResourceKind.FUN.getId());

        return generalRepository.query(sql, params);
    }

    @Override
    public void allocateFunPermissions(String roleId, String oneLevelPermissionId, List<String> permissionIds) {
        Assert.hasText(roleId, "角色ID不能为空。");
        Assert.hasText(oneLevelPermissionId, "参数oneLevelPermissionId不能为空。");

        Role role = this.loadRole(roleId);
        Assert.notNull(role, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, roleId, "角色"));

        this.applyTspmEditRule(role, "allocate");

        Permission oneLevePermission = this.permissionRepository.findOne(oneLevelPermissionId);
        Assert.notNull(oneLevePermission, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, oneLevelPermissionId, "权限"));

        // 1、删除当前角色下的一级功能下所有权限
        String sql = this.getAccessQuerySqlByName("deletePermissionsByRoleAndOneLevePermissionId");
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("roleId", roleId);
        params.put("fullId", oneLevePermission.getFullId() + "%");

        generalRepository.update(sql, params);

        if (permissionIds == null || permissionIds.size() == 0) {
            return;
        }

        List<RolePermission> rolePermissions = new ArrayList<RolePermission>(permissionIds.size());

        // 2、添加权限
        RolePermission rolePermission;
        for (String permissionId : permissionIds) {
            rolePermission = new RolePermission();

            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermission.setCreator(Creator.newInstance());

            rolePermissions.add(rolePermission);
        }
        // 3、添加一级权限
        if (permissionIds.size() > 0) {
            rolePermission = new RolePermission();

            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(oneLevelPermissionId);
            rolePermission.setCreator(Creator.newInstance());

            rolePermissions.add(rolePermission);
        }

        this.rolePermissionRepository.save(rolePermissions);
    }

    @Override
    public void deallocateFunPermissions(String roleId, List<String> permissionIds) {
        Assert.hasText(roleId, "参数roleId不能为空。");
        Assert.notEmpty(permissionIds, "参数permissionIds不能为空。");

        this.rolePermissionRepository.deleteByRoleIdAndPermissionIds(roleId, permissionIds);
    }

    @Override
    public Map<String, Object> slicedQueryPermissionsByRoleId(PermissionsByRoleIdQueryRequest queryRequest) {
        Assert.hasText(queryRequest.getRoleId(), "参数roleId不能为空。");
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryPermissionsByRoleId");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryAuthorizedPermissionsByOrgFullId(AuthorizedPermissionsByOrgFullIdQueryRequest queryRequest) {
        queryRequest.checkConstraints();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryAuthorizedPermissionsByOrgFullId");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public List<String> queryPersonFunPermissions(String personId) {
        Assert.hasText(personId, "参数personId不能为空。");
        String sql = this.getAccessQuerySqlByName("queryPersonFunPermissions");
        return this.sqlExecutorDao.queryToList(sql, String.class, personId);
    }

    @Override
    public List<String> queryPersonRoleIds(String personId) {
        Assert.hasText(personId, "参数personId不能为空。");
        String sql = this.getAccessQuerySqlByName("queryPersonRoleIds");
        return this.sqlExecutorDao.queryToList(sql, String.class, personId);
    }

    @Override
    public RoleKind getPersonRoleKind(String personId) {
        Assert.hasText(personId, "参数personId不能为空。");
        String sql = this.getAccessQuerySqlByName("queryPersonRoleKindId");
        String roleKindId = this.sqlExecutorDao.queryToString(sql, personId);
        if (StringUtil.isBlank(roleKindId)) {
            return RoleKind.COMMON;
        }
        return RoleKind.fromId(roleKindId);
    }

    @Override
    public ResourceOperation loadResourceOperation(String id) {
        Assert.hasText(id, "参数ID不能为空。");
        return resourceOperationRepository.findOne(id);
    }

    @Override
    public List<Map<String, Object>> queryPersonFunctions(String personId, String parentId) {
        Assert.hasText(personId, "参数personId不能为空。");
        Assert.hasText(parentId, "参数parentId不能为空。");
        String sql = this.getAccessQuerySqlByName("queryPersonFunctions");
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("personId", personId);
        params.put("parentId", parentId);
        List<Map<String, Object>> result = this.sqlExecutorDao.queryToMapListByMapParam(sql, params);
        return i18n(result);
    }

    @Override
    public List<Map<String, Object>> queryPersonOneLevelAllFunctions(String personId, String parentId) {
        Assert.hasText(personId, "参数personId不能为空。");
        Assert.hasText(parentId, "参数parentId不能为空。");

        SysFunction sysFunction = this.sysFunctionRepository.findOne(parentId);

        Assert.notNull(sysFunction, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, parentId, "系统功能"));
        String sql = this.getAccessQuerySqlByName("queryPersonOneLevelAllFunctions");

        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("personId", personId);
        params.put("fullId", sysFunction.getFullId() + "/%");
        List<Map<String, Object>> result = this.sqlExecutorDao.queryToMapListByMapParam(sql, params);
        return i18n(result);
    }

    @Override
    public List<Map<String, Object>> queryPersonAllFunctions(String personId, String parentId) {
        Assert.hasText(personId, "参数personId不能为空。");
        Assert.hasText(parentId, "参数parentId不能为空。");

        SysFunction sysFunction = this.sysFunctionRepository.findOne(parentId);
        Assert.notNull(sysFunction, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, parentId, "系统功能"));
        String sql = this.getAccessQuerySqlByName("queryPersonAllFunctions");

        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("personId", personId);
        params.put("fullId", sysFunction.getFullId() + "/%");
        List<Map<String, Object>> result = this.sqlExecutorDao.queryToMapListByMapParam(sql, params);
        return i18n(result);
    }

    /**
     * 国际化数据转换
     * 
     * @param result
     * @return
     */
    private List<Map<String, Object>> i18n(List<Map<String, Object>> result) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(result.size());
        String code = "";
        // 获取国际化资源 默认key为 function.GNS.description
        for (Map<String, Object> map : result) {
            code = map.get("code").toString();
            map.put("description", MessageSourceContext.getMessageAsDefault(String.format("function.%s.description", code), (String) map.get("description")));
            map.put("name", MessageSourceContext.getMessageAsDefault(String.format("function.%s.name", code), (String) map.get("name")));
            list.add(map);
        }
        return list;
    }

    @Override
    public boolean checkPersonFunPermissions(String personId, String funcCode) {
        String sql = this.getAccessQuerySqlByName("checkPersonFunPermissions");
        int count = this.sqlExecutorDao.queryToInt(sql, personId, funcCode);
        return count > 0;
    }

    @Override
    public List<Map<String, Object>> loadPersonRole(String personId) {
        String sql = this.getAccessQuerySqlByName("loadPersonRole");
        return this.sqlExecutorDao.queryToListMap(sql, personId);
    }

    @Override
    public boolean authenticationManageType(SDO sdo) {
        String manageType = sdo.getProperty("manageType", String.class);
        String fullId = sdo.getProperty("fullId", String.class);
        return permissionBuilder.hasManagementPermission(manageType, fullId);
    }

    @Override
    public Map<String, Object> queryUIElementOperations() {
        List<ResourceOperation> resourceOperations = this.resourceOperationRepository.findCommonOperations(ResourceKind.UI_ELEMENT,
                                                                                                           ResourceOperation.COMMON_OPERATION);
        Map<String, Object> result = new HashMap<String, Object>(resourceOperations.size());

        for (ResourceOperation resourceOperation : resourceOperations) {
            result.put(resourceOperation.getCode(), resourceOperation.getName());
        }

        return result;
    }

    private boolean compareUIElementOperation(String currentOperationId, String exsistedOperationId) {
        if (currentOperationId.equals("readwrite")) {
            return true;
        }
        if (currentOperationId.equals("readonly") && exsistedOperationId.equals("noaccess")) {
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> queryUIElementPermissionsByFunction(String function, String personId, boolean isId) {
        Assert.hasText(function, "参数function不能为空。");
        Assert.notNull(personId, "参数personId不能为空。");

        String functionId = function;
        if (!isId) {
            SysFunction sysFunction = this.sysFunctionRepository.findByCode(function);
            Assert.notNull(sysFunction, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_CODE, function, "系统功能"));
            functionId = sysFunction.getId();
        }
        String sql = this.getAccessQuerySqlByName("queryUIElementPermissionByFunction");
        List<Map<String, Object>> list = this.sqlExecutorDao.queryToListMap(sql, functionId, personId);

        Map<String, Map<String, Object>> uiElements = new HashMap<>(list.size());
        String code, operationId, existedOperationId;
        Map<String, Object> existedUIElement;
        for (Map<String, Object> item : list) {
            code = ClassHelper.convert(item.get("code"), String.class);
            operationId = ClassHelper.convert(item.get("operationId"), String.class);
            existedUIElement = uiElements.get(code);
            if (existedUIElement == null) {
                uiElements.put(code, item);
            } else {
                existedOperationId = ClassHelper.convert(existedUIElement.get("operationId"), String.class);
                // 权限比较 readwrite > readonly > noaccess 比较取得权限最大数据
                if (compareUIElementOperation(operationId, existedOperationId)) {
                    // 用权限较大的替换原有的
                    uiElements.remove(code);
                    uiElements.put(code, item);
                }
            }
        }
        List<Map<String, Object>> fieldList = new ArrayList<>(uiElements.size());
        fieldList.addAll(uiElements.values());
        return fieldList;
    }

    private List<String> queryRoleKindContainsPersonIds(RoleKind roleKind) {
        String sql = this.getAccessQuerySqlByName("queryRoleKindContainsPersonIds");
        return this.sqlExecutorDao.queryToList(sql, String.class, roleKind.getId());
    }

    @Override
    public void hideSuperAdministrator() {
        List<String> personIds = queryRoleKindContainsPersonIds(RoleKind.SUPER_ADMINISTRATOR);
        // 隐藏角色
        roleRepository.hideSuperAdministrator();

        // 隐藏人员
        for (String personId : personIds) {
            orgApplication.logicDeletePerson(personId);
        }
    }

    @Override
    public void synThreeMemberPermission() {
        String sql = this.getAccessQuerySqlByName("queryThreeMemberPermssion");
        List<Map<String, Object>> threeMemeberPermissions = this.sqlExecutorDao.queryToListMap(sql);
        String lastPermssionCode = "", permssionCode, roleKindId, roleKindIds = null;

        if (threeMemeberPermissions.size() == 0) {
            return;
        }

        Map<String, Object> holder = new HashMap<String, Object>(2);
        holder.put("code", "*");
        holder.put("roleKindId", "*");
        threeMemeberPermissions.add(holder);

        lastPermssionCode = ClassHelper.convert(threeMemeberPermissions.get(0).get("code"), String.class);
        // A admin
        // B admin
        // B security
        // C admin
        for (Map<String, Object> item : threeMemeberPermissions) {
            permssionCode = ClassHelper.convert(item.get("code"), String.class);
            roleKindId = ClassHelper.convert(item.get("roleKindId"), String.class);
            if (!lastPermssionCode.equals(permssionCode)) {
                SystemCache.setThreeMemberPermission(lastPermssionCode, roleKindIds);
                lastPermssionCode = permssionCode;
                roleKindIds = roleKindId;
            } else {
                if (StringUtil.isBlank(roleKindIds)) {
                    roleKindIds = roleKindId;
                } else {
                    roleKindIds = String.format("%s,%s", roleKindIds, roleKindId);
                }
            }
        }

    }
}