package com.huigou.uasp.bmp.opm.proxy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.RoleKind;
import com.huigou.context.TmspmConifg;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.data.domain.service.CommonDomainService;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.repository.GeneralRepository;
import com.huigou.uasp.bmp.opm.application.AccessApplication;
import com.huigou.uasp.bmp.opm.domain.model.access.Permission;
import com.huigou.uasp.bmp.opm.domain.model.access.Role;
import com.huigou.uasp.bmp.opm.domain.model.access.UIElementPermission;
import com.huigou.uasp.bmp.opm.domain.model.resource.ResourceOperation;
import com.huigou.uasp.bmp.opm.domain.query.AuthorizationsQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.AuthorizedPermissionsByOrgFullIdQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.PermissionsByRoleIdQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.RolesQueryRequestQueryRequest;
import com.huigou.uasp.bmp.opm.impl.AccessApplicationImpl;
import com.huigou.uasp.bmp.opm.repository.org.PermissionRepository;
import com.huigou.uasp.bmp.opm.repository.org.ResourceOperationRepository;
import com.huigou.uasp.bmp.opm.repository.org.RolePermissionRepository;
import com.huigou.uasp.bmp.opm.repository.org.RoleRepository;
import com.huigou.uasp.bmp.opm.repository.org.SysFunctionRepository;
import com.huigou.uasp.bmp.opm.repository.org.UIElementPermissionRepository;
import com.huigou.uasp.bmp.opm.repository.org.UIElementRepository;
import com.huigou.util.SDO;

@Service("accessApplicationProxy")
public class AccessApplicationProxy {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private ResourceOperationRepository resourceOperationRepository;

    @Autowired
    private SysFunctionRepository sysFunctionRepository;

    @Autowired
    private UIElementRepository uiElementRepository;

    @Autowired
    private UIElementPermissionRepository uiElementPermissionRepository;

    @Autowired
    private TmspmConifg tmspmConifg;

    @Autowired
    private CommonDomainService commonDomainService;

    @Autowired
    private SQLExecutorDao sqlExecutorDao;

    @Autowired
    private GeneralRepository generalRepository;

    @Autowired
    private CoreApplicationFactory coreApplicationFactory;

    private AccessApplication accessApplication;

    void initProperties(AccessApplicationImpl accessApplicationImpl) {
        accessApplicationImpl.setPermissionRepository(permissionRepository);
        accessApplicationImpl.setResourceOperationRepository(resourceOperationRepository);
        accessApplicationImpl.setRolePermissionRepository(rolePermissionRepository);
        accessApplicationImpl.setRoleRepository(roleRepository);
        accessApplicationImpl.setSysFunctionRepository(sysFunctionRepository);
        accessApplicationImpl.setTmspmConifg(tmspmConifg);
        accessApplicationImpl.setUiElementPermissionRepository(uiElementPermissionRepository);
        accessApplicationImpl.setCommonDomainService(commonDomainService);
        accessApplicationImpl.setSqlExecutorDao(sqlExecutorDao);
        accessApplicationImpl.setGeneralRepository(generalRepository);
    }

    public AccessApplication getAccessApplication() {
        // if (accessApplication == null) {
        // synchronized (AccessApplicationProxy.class) {
        if (accessApplication == null) {
            AccessApplicationImpl accessApplicationImpl = coreApplicationFactory.getAccessApplication();
            accessApplication = accessApplicationImpl;
        }
        // }
        // }
        return accessApplication;
    }

    @Transactional
    public String saveRole(Role role) {
        return getAccessApplication().saveRole(role);
    }

    public Role loadRole(String id) {
        return getAccessApplication().loadRole(id);
    }

    @Transactional
    public void deleteRoles(List<String> ids) {
        getAccessApplication().deleteRoles(ids);
    }

    @Transactional
    public void updateRolesSequence(Map<String, Integer> params) {
        getAccessApplication().updateRolesSequence(params);
    }

    @Transactional
    public void moveRoles(List<String> ids, String parentId) {
        getAccessApplication().moveRoles(ids, parentId);
    }

    @Transactional
    public void moveTenantRoles(List<String> ids, String parentId) {
        getAccessApplication().moveTenantRoles(ids, parentId);
    }

    public List<Map<String, Object>> queryRoles(String tenantKindId, String parentId) {
        return getAccessApplication().queryRoles(tenantKindId, parentId);
    }

    public Map<String, Object> slicedQueryRoles(RolesQueryRequestQueryRequest queryRequest) {
        return getAccessApplication().slicedQueryRoles(queryRequest);
    }

    public Integer getRoleNextSequence(String parentId) {
        return getAccessApplication().getRoleNextSequence(parentId);
    }

    @Transactional
    public void saveInitPermissionResourceKind() {
        getAccessApplication().saveInitPermissionResourceKind();
    }

    @Transactional
    public String savePermission(Permission permission) {
        return getAccessApplication().savePermission(permission);
    }

    @Transactional
    public void updatePermission(Permission permission) {
        getAccessApplication().updatePermission(permission);
    }

    @Transactional
    public void updatePermissionsStatus(List<String> ids, Integer status) {
        getAccessApplication().updatePermissionsStatus(ids, status);
    }

    public Permission loadPermission(String id) {
        return getAccessApplication().loadPermission(id);
    }

    @Transactional
    public void deletePermissions(List<String> ids) {
        getAccessApplication().deletePermissions(ids);
    }

    @Transactional
    public void updatePermissionsSequence(Map<String, Integer> permissions) {
        getAccessApplication().updatePermissionsSequence(permissions);
    }

    @Transactional
    public void movePermissions(List<String> ids, String parentId) {
        getAccessApplication().movePermissions(ids, parentId);
    }

    public Map<String, Object> queryPermissions(FolderAndCodeAndNameQueryRequest queryRequest) {
        return getAccessApplication().queryPermissions(queryRequest);
    }

    @Transactional
    public void saveUIElementPermissions(List<UIElementPermission> uiElementPermissions) {
        getAccessApplication().saveUIElementPermissions(uiElementPermissions);
    }

    @Transactional
    public void deleteUIElementPermissions(List<String> ids) {
        getAccessApplication().deleteUIElementPermissions(ids);
    }

    public Map<String, Object> slicedQueryUIElementPermissions(ParentIdQueryRequest queryRequest) {
        return getAccessApplication().slicedQueryUIElementPermissions(queryRequest);
    }

    public Integer getPermissionNextSequence(String parentId) {
        return getAccessApplication().getPermissionNextSequence(parentId);
    }

    @Transactional
    public void allocateRoles(String orgId, List<String> roleIds) {
        getAccessApplication().allocateRoles(orgId, roleIds);
    }

    public void deallocateRoles(String orgId, List<String> roleIds) {
        getAccessApplication().deallocateRoles(orgId, roleIds);
    }

    public Map<String, Object> queryAuthorizations(AuthorizationsQueryRequest queryRequest) {
        return getAccessApplication().queryAuthorizations(queryRequest);
    }

    public List<Map<String, Object>> queryPermissionsByParentId(String parentId) {
        return getAccessApplication().queryPermissionsByParentId(parentId);
    }

    public List<Map<String, Object>> queryPermissionsByFullId(String parentId) {
        Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(AccessApplication.QUERY_XML_FILE_PATH, "permission");
        String sql = queryDescriptor.getSqlByName("queryByFullId");
        Permission permission = this.permissionRepository.findOne(parentId);
        return this.sqlExecutorDao.queryToListMap(sql, permission.getFullId() + "%");
    }

    public List<Permission> queryAllocatedPermissions(String roleId, String parentId) {
        return getAccessApplication().queryAllocatedPermissions(roleId, parentId);
    }

    @Transactional
    public void allocateFunPermissions(String roleId, String oneLevelPermissionId, List<String> permissionIds) {
        getAccessApplication().allocateFunPermissions(roleId, oneLevelPermissionId, permissionIds);
    }

    @Transactional
    public void deallocateFunPermissions(String roleId, List<String> permissionIds) {
        getAccessApplication().deallocateFunPermissions(roleId, permissionIds);
    }

    public Map<String, Object> slicedQueryPermissionsByRoleId(PermissionsByRoleIdQueryRequest queryRequest) {
        return getAccessApplication().slicedQueryPermissionsByRoleId(queryRequest);
    }

    public Map<String, Object> slicedQueryAuthorizedPermissionsByOrgFullId(AuthorizedPermissionsByOrgFullIdQueryRequest queryRequest) {
        return getAccessApplication().slicedQueryAuthorizedPermissionsByOrgFullId(queryRequest);
    }

    public List<String> queryPersonFunPermissions(String personId) {
        return getAccessApplication().queryPersonFunPermissions(personId);
    }

    public List<String> queryPersonRoleIds(String personId) {
        return getAccessApplication().queryPersonRoleIds(personId);
    }

    public RoleKind getPersonRoleKind(String personId) {
        return getAccessApplication().getPersonRoleKind(personId);
    }

    public ResourceOperation loadResourceOperation(String id) {
        return getAccessApplication().loadResourceOperation(id);
    }

    public List<Map<String, Object>> queryPersonFunctions(String personId, String parentId) {
        return getAccessApplication().queryPersonFunctions(personId, parentId);
    }

    public List<Map<String, Object>> queryPersonOneLevelAllFunctions(String personId, String parentId) {
        return getAccessApplication().queryPersonOneLevelAllFunctions(personId, parentId);
    }

    public List<Map<String, Object>> queryPersonAllFunctions(String personId, String parentId) {
        return getAccessApplication().queryPersonAllFunctions(personId, parentId);
    }

    public boolean checkPersonFunPermissions(String personId, String funcCode) {
        return getAccessApplication().checkPersonFunPermissions(personId, funcCode);
    }

    public List<Map<String, Object>> loadPersonRole(String personId) {
        return getAccessApplication().loadPersonRole(personId);
    }

    public boolean authenticationManageType(SDO sdo) {
        return getAccessApplication().authenticationManageType(sdo);
    }

    public Map<String, Object> queryUIElementOperations() {
        return getAccessApplication().queryUIElementOperations();
    }

    public List<Map<String, Object>> queryUIElementPermissionsByFunction(String function, String personId, boolean isId) {
        return getAccessApplication().queryUIElementPermissionsByFunction(function, personId, isId);
    }

    @Transactional
    public void hideSuperAdministrator() {
        getAccessApplication().hideSuperAdministrator();
    }

    public void synThreeMemberPermission() {
        getAccessApplication().synThreeMemberPermission();
    }
}
