package com.huigou.uasp.bmp.opm.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.huigou.context.Operator;
import com.huigou.context.RoleKind;
import com.huigou.context.TmspmConifg;
import com.huigou.data.domain.model.BaseInfoAbstractEntity;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.domain.ValidStatus;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.annotation.ControllerMethodMapping;
import com.huigou.uasp.bmp.bizClassification.application.BizClassificationApplication;
import com.huigou.uasp.bmp.opm.application.AccessQueryApplication;
import com.huigou.uasp.bmp.opm.domain.model.access.Permission;
import com.huigou.uasp.bmp.opm.domain.model.access.Role;
import com.huigou.uasp.bmp.opm.domain.model.access.UIElementPermission;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionNodeKind;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionResourceKind;
import com.huigou.uasp.bmp.opm.domain.query.AuthorizationsQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.AuthorizedPermissionsByOrgFullIdQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.PermissionsByRoleIdQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.PermissionsQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.RolesQueryRequestQueryRequest;
import com.huigou.uasp.bmp.opm.proxy.AccessApplicationProxy;
import com.huigou.uasp.bmp.opm.proxy.SysFunctionApplicationProxy;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.uasp.tool.remind.application.MessageRemindApplication;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

@Controller
@ControllerMapping("access")
public class AccessController extends CommonController {

    private static final String SELECT_ROLE_DIALOG = "SelectRoleDialog";

    private static final String ROLE_PAGE = "Role";

    private static final String ROLE_DETAIL_PAGE = "RoleDetail";

    private static final String QUERY_PERMISSON_FOR_FUNCTION_PAGE = "QueryPermissonForFunction";

    private static final String QUERY_PERMISSON_FOR_ORG_PAGE = "QueryPermissonForOrg";

    private static final String QUERY_PERMISSON_FOR_ROLE_PAGE = "QueryPermissonForRole";

    private static final String AUTHORIZATION_PAGE = "Authorization";

    private static final String PERMISSION_PAGE = "Permission";

    private static final String PERMISSION_DETAIL_PAGE = "PermissionDetail";

    @Autowired
    private AccessApplicationProxy accessApplication;

    @Autowired
    private SysFunctionApplicationProxy sysFunctionApplication;

    @Autowired
    private MessageRemindApplication messageRemindApplication;

    @Autowired
    private BizClassificationApplication bizClassificationApplication;

    @Autowired
    private AccessQueryApplication accessQueryApplication;

    @Autowired
    private TmspmConifg tmspmConifg;

    @Override
    protected String getPagePath() {
        return "/system/opm/permission/";
    }

    @RequiresPermissions("Role:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到角色列表页面")
    public String forwardRole() {
        this.putAttribute("enableTspm", this.tmspmConifg.isEnableTspm());
        this.putAttribute("isUseTspm", this.tmspmConifg.isUseTspm());
        return forward(ROLE_PAGE);
    }

    @RequiresPermissions("TenantRole:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到租户角色列表页面")
    public String forwardTenantRole() {
        this.putAttribute("enableTspm", this.tmspmConifg.isEnableTspm());
        this.putAttribute("isUseTspm", this.tmspmConifg.isUseTspm());
        return forward(ROLE_PAGE);
    }

    @RequiresPermissions(value = { "Role:create", "TenantRole:create" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到角色明细页面")
    public String showRoleDetail() {
        SDO params = this.getSDO();
        String parentId = params.getString(PARENT_ID_KEY_NAME);
        Integer sequence = accessApplication.getRoleNextSequence(parentId);
        params.putProperty(STATUS_KEY_NAME, ValidStatus.ENABLED.getId());
        params.putProperty(SEQUENCE_KEY_NAME, sequence);
        this.putAttribute("roleKindList", RoleKind.getData(tmspmConifg.isUseTspm()));
        return forward(ROLE_DETAIL_PAGE, params);
    }

    @RequiresPermissions(value = { "Role:update", "TenantRole:update" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改角色明细页面")
    public String loadRole() {
        SDO params = this.getSDO();
        String id = params.getString(ID_KEY_NAME);
        Role role = this.accessApplication.loadRole(id);
        this.putAttribute("roleKindList", RoleKind.getData(tmspmConifg.isUseTspm()));
        return forward(ROLE_DETAIL_PAGE, role);
    }

    @RequiresPermissions(value = { "Role:create", "TenantRole:create" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加角色")
    public String insertRole() {
        SDO params = this.getSDO();
        Role role = params.toObject(Role.class);
        String id = this.accessApplication.saveRole(role);
        return success(id);
    }

    @RequiresPermissions(value = { "Role:update", "TenantRole:update" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改角色")
    public String updateRole() {
        SDO sdo = this.getSDO();

        Role role = sdo.toObject(Role.class);
        this.accessApplication.saveRole(role);

        return success();
    }

    @RequiresPermissions(value = { "Role:delete", "TenantRole:delete" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除角色")
    public String deleteRole() {
        List<String> ids = this.getSDO().getStringList(IDS_KEY_NAME);
        this.accessApplication.deleteRoles(ids);
        return success();
    }

    @RequiresPermissions("Role:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动角色")
    public String moveRoles() {
        SDO params = this.getSDO();
        String parentId = params.getString(PARENT_ID_KEY_NAME);
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        this.accessApplication.moveRoles(ids, parentId);
        return success();
    }

    @RequiresPermissions("TenantRole:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动角色")
    public String moveTenantRoles() {
        SDO params = this.getSDO();
        String parentId = params.getString(PARENT_ID_KEY_NAME);
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        this.accessApplication.moveTenantRoles(ids, parentId);
        return success();
    }

    @RequiresPermissions(value = { "Role:update", "TenantRole:update" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改角色 排序号")
    public String updateRolesSequence() {
        SDO params = this.getSDO();
        Map<String, Integer> data = params.getStringMap(DATA_KEY_NAME);
        accessApplication.updateRolesSequence(data);
        return success();
    }

    @RequiresPermissions(value = { "Role:query", "TenantRole:query", "Authorization:query" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询角色")
    public String queryRoles() {
        SDO params = this.getSDO();
        String parentId = params.getString(PARENT_ID_KEY_NAME);
        String tenantKindId = params.getString("tenantKindId");
        List<Map<String, Object>> data = accessApplication.queryRoles(tenantKindId, parentId);
        return toResult(data);
    }

    @RequiresPermissions(value = { "Role:query", "TenantRole:query", "Authorization:query" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "分页查询角色")
    public String slicedQueryRoles() {
        SDO params = this.getSDO();
        RolesQueryRequestQueryRequest queryRequest = params.toQueryRequest(RolesQueryRequestQueryRequest.class);
        Map<String, Object> data = accessApplication.slicedQueryRoles(queryRequest);
        return toResult(data);
    }

    // @RequiresPermissions(value = { "Role:query", "TenantRole:query" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到角色窗口页面")
    public String showSelectRoleDialog() {
        return forward(SELECT_ROLE_DIALOG, this.getSDO().getProperties());
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到权限功能页面")
    public String forwardQueryPermissionForFunction() {
        return forward(QUERY_PERMISSON_FOR_FUNCTION_PAGE);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到权限组织页面")
    public String forwardQueryPermissionForOrg() {
        return forward(QUERY_PERMISSON_FOR_ORG_PAGE);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到权限角色页面")
    public String forwardQueryPermissionForRole() {
        return forward(QUERY_PERMISSON_FOR_ROLE_PAGE);
    }

    @RequiresPermissions("Authorization:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到授权列表页面")
    public String forwardAuthorize() {
        return this.forward(AUTHORIZATION_PAGE);
    }

    @RequiresPermissions("Authorization:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "分配角色")
    public String allocateRoles() {
        SDO params = this.getSDO();
        String orgId = params.getString("orgId");
        List<String> roleIds = params.getStringList("roleIds");
        this.accessApplication.allocateRoles(orgId, roleIds);
        return success();
    }

    @RequiresPermissions("Authorization:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "取消分配角色")
    public String deallocateRoles() {
        SDO params = this.getSDO();
        String orgId = params.getString("orgId");
        List<String> roleIds = params.getStringList("roleIds");
        accessApplication.deallocateRoles(orgId, roleIds);
        return success();
    }

    @RequiresPermissions("Authorization:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询授权")
    public String queryAuthorizations() {
        SDO params = this.getSDO();
        AuthorizationsQueryRequest queryRequest = params.toQueryRequest(AuthorizationsQueryRequest.class);
        Map<String, Object> data = accessApplication.queryAuthorizations(queryRequest);
        return this.toResult(data);
    }

    @RequiresPermissions("Role:allocate")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到分配功能页面")
    public String showAssignFunctionDialog() {
        List<Map<String, Object>> list = accessApplication.queryPermissionsByParentId(Permission.ROOT_ID);
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (Map<String, Object> m : list) {
            map.put(m.get("id").toString(), m.get("name").toString());
        }
        this.putAttribute("permissionsList", map);
        return forward("AssignFunction", this.getSDO().getProperties());
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询功能权限")
    public String queryPermissionsByParentId() {
        // 平台未使用
        SDO params = this.getSDO();
        String parentId = params.getString(PARENT_ID_KEY_NAME);
        List<Map<String, Object>> permissions = this.accessApplication.queryPermissionsByParentId(parentId);
        return this.toResult(permissions);
    }

    @RequiresPermissions("Role:allocate")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询功能权限")
    public String queryAllPermissionsByParentId() {
        SDO params = this.getSDO();
        String parentId = params.getString(PARENT_ID_KEY_NAME);
        List<Map<String, Object>> permissions = this.accessApplication.queryPermissionsByFullId(parentId);
        return this.toResult(permissions);
    }

    @RequiresPermissions("Role:allocate")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询功能权限")
    public String queryAllocatedPermissions() {
        SDO params = this.getSDO();
        String roleId = params.getString("roleId");
        String parentId = params.getString(PARENT_ID_KEY_NAME);
        List<Permission> permissions = this.accessApplication.queryAllocatedPermissions(roleId, parentId);
        return this.toResult(permissions);
    }

    @RequiresPermissions("Role:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询功能权限")
    public String slicedQueryPermissionsByRoleId() {
        SDO params = this.getSDO();
        PermissionsByRoleIdQueryRequest queryRequest = params.toQueryRequest(PermissionsByRoleIdQueryRequest.class);
        Map<String, Object> data = this.accessApplication.slicedQueryPermissionsByRoleId(queryRequest);
        return this.toResult(data);

    }

    @RequiresPermissions("Authorization:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询已授权的权限")
    public String slicedQueryAuthorizedPermissionsByOrgFullId() {
        SDO params = this.getSDO();
        AuthorizedPermissionsByOrgFullIdQueryRequest queryRequest = params.toQueryRequest(AuthorizedPermissionsByOrgFullIdQueryRequest.class);
        Map<String, Object> data = this.accessQueryApplication.slicedQueryAuthorizedPermissionsByOrgFullId(queryRequest);
        return this.toResult(data);
    }

    @RequiresPermissions("Role:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查继承的角色")
    public String slicedQueryRolesByOrgFullId() {
        SDO params = this.getSDO();
        AuthorizedPermissionsByOrgFullIdQueryRequest queryRequest = params.toQueryRequest(AuthorizedPermissionsByOrgFullIdQueryRequest.class);
        Map<String, Object> data = this.accessQueryApplication.slicedQueryRolesByOrgFullId(queryRequest);
        return this.toResult(data);
    }

    @RequiresPermissions("Role:allocate")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "分配功能权限")
    public String allocateFunPermissions() {
        SDO params = this.getSDO();
        String roleId = params.getString("roleId");
        String parentId = params.getString(PARENT_ID_KEY_NAME);
        List<String> permissionIds = params.getStringList("permissionIds");
        this.accessApplication.allocateFunPermissions(roleId, parentId, permissionIds);
        return this.success();

    }

    @RequiresPermissions("Role:allocate")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "取消分配功能权限")
    public String deallocateFunPermissions() {
        SDO params = this.getSDO();
        String roleId = params.getString("roleId");
        List<String> permissionIds = params.getStringList("ids");
        this.accessApplication.deallocateFunPermissions(roleId, permissionIds);
        return this.success();

    }

    @RequiresPermissions("Permission:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到权限列表页面")
    public String forwardPermission() {
        this.accessApplication.saveInitPermissionResourceKind();
        return forward(PERMISSION_PAGE);
    }

    @RequiresPermissions("Permission:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加权限明细页面")
    public String showPermissionDetail() {
        SDO params = this.getSDO();
        String resourceKindId = params.getString("resourceKindId");
        PermissionResourceKind resourceKind = PermissionResourceKind.findById(resourceKindId);
        Assert.notNull(resourceKind, "错误的资源类型");
        String parentId = params.getString(PARENT_ID_KEY_NAME);
        Integer sequence = this.accessApplication.getPermissionNextSequence(parentId);
        params.putProperty(STATUS_KEY_NAME, ValidStatus.ENABLED.getId());
        params.putProperty(SEQUENCE_KEY_NAME, sequence);
        this.putAttribute("permissionNodeKind", PermissionNodeKind.getMap(resourceKind));
        return forward(PERMISSION_DETAIL_PAGE, params);
    }

    @RequiresPermissions("Permission:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改权限明细页面")
    public String loadPermission() {
        SDO params = this.getSDO();
        String id = params.getString(ID_KEY_NAME);
        Permission permission = this.accessApplication.loadPermission(id);
        PermissionResourceKind resourceKind = PermissionResourceKind.findById(permission.getResourceKindId());
        Assert.notNull(resourceKind, "错误的资源类型");
        if (permission.isFunctionResource()) {
            BaseInfoAbstractEntity entity = sysFunctionApplication.loadSysFunction(permission.getResourceId());
            this.putAttribute("resourceName", entity.getName());

            if (permission.isPermission() && StringUtil.isNotBlank(permission.getOperationId())) {
                BaseInfoAbstractEntity operation = this.accessApplication.loadResourceOperation(permission.getOperationId());
                this.putAttribute("operationName", operation.getName());
            }
        } else if (permission.isRemindResource()) {
            if (!permission.isFirstLevel()) {
                BaseInfoAbstractEntity entity = messageRemindApplication.load(permission.getResourceId());
                this.putAttribute("resourceName", entity.getName());
            }
        } else if (permission.isBusinessClassResource()) {
            if (!permission.isFirstLevel()) {
                BaseInfoAbstractEntity entity = bizClassificationApplication.loadBizClassification(permission.getResourceId());
                this.putAttribute("resourceName", entity.getName());
            }
        }
        this.putAttribute("permissionNodeKind", PermissionNodeKind.getMap(resourceKind));
        return forward(PERMISSION_DETAIL_PAGE, permission);
    }

    @RequiresPermissions("Permission:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加权限")
    public String insertPermission() {
        SDO params = this.getSDO();
        Permission permission = params.toObject(Permission.class);
        this.accessApplication.savePermission(permission);
        return success();
    }

    @RequiresPermissions("Permission:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改权限")
    public String updatePermission() {
        SDO params = this.getSDO();
        Permission permission = params.toObject(Permission.class);
        this.accessApplication.updatePermission(permission);
        return success();
    }

    @RequiresPermissions("Permission:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除权限")
    public String deletePermissions() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        this.accessApplication.deletePermissions(ids);
        return success();
    }

    @RequiresPermissions("Permission:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改权限状态")
    public String updatePermissionsStatus() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        Integer status = params.getInteger(STATUS_KEY_NAME);
        this.accessApplication.updatePermissionsStatus(ids, status);
        return success();
    }

    @RequiresPermissions("Permission:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改权限排序号")
    public String updatePermissionsSequence() {
        SDO params = this.getSDO();
        this.accessApplication.updatePermissionsSequence(params.getStringMap(DATA_KEY_NAME));
        return success();
    }

    @ControllerMethodMapping("/menu")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "查询人员功能权限")
    public String loadPersonFunPermissions() {
        SDO params = this.getSDO();
        Operator operator = params.getOperator();
        String parentId = params.getString(PARENT_ID_KEY_NAME);
        List<Map<String, Object>> rows = accessApplication.queryPersonFunctions(operator.getUserId(), parentId);
        return this.toResult(rows);
    }

    @RequiresPermissions("Permission:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询权限")
    public String queryPermissions() {
        SDO params = this.getSDO();
        String parentId = params.getString(PARENT_ID_KEY_NAME);
        FolderAndCodeAndNameQueryRequest queryRequest = params.toQueryRequest(FolderAndCodeAndNameQueryRequest.class);
        queryRequest.setFolderId(parentId);
        Map<String, Object> data = this.accessApplication.queryPermissions(queryRequest);
        return this.toResult(data);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到界面元素权限页面")
    public String showUIElementPermission() {
        SDO params = this.getSDO();
        String permissionId = params.getString("permissionId");
        this.putAttribute("permissionId", permissionId);

        Map<String, Object> operations = accessApplication.queryUIElementOperations();
        this.putAttribute("uiElementOperations", operations);

        return forward("UIElementPermission");
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.SAVE, description = "保存界面元素权限")
    public String saveUIElementPermissions() {
        SDO params = this.getSDO();
        List<UIElementPermission> uiElementPermissions = params.getList("detailData", UIElementPermission.class);
        this.accessApplication.saveUIElementPermissions(uiElementPermissions);
        return success();
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除界面元素权限")
    public String deleteUIElementPermissions() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.accessApplication.deleteUIElementPermissions(ids);
        return success();
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询界面元素权限")
    public String slicedQueryUIElementPermissions() {
        SDO params = this.getSDO();
        String permissionId = params.getString("permissionId");
        ParentIdQueryRequest queryRequest = params.toQueryRequest(ParentIdQueryRequest.class);
        queryRequest.setParentId(permissionId);
        Map<String, Object> data = this.accessApplication.slicedQueryUIElementPermissions(queryRequest);
        return this.toResult(data);
    }

    @ControllerMethodMapping({ "/authenticationManageType", "/authenticationManageType" })
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "管理权限鉴权")
    public String authenticationManageType() throws Exception {
        SDO sdo = this.getSDO();
        boolean flag = accessApplication.authenticationManageType(sdo);
        return toResult(flag);
    }

    /**
     * 根据角色查询权限
     * 
     * @return
     */
    public String forwardRolePermission() {
        SDO sdo = this.getSDO();
        sdo.putProperty("singlePerson", "1");
        return forward("RolePermission", sdo);
    }

    /**
     * 根据权限查询引用的角色及人员
     * 
     * @return
     */
    public String forwardPermissionRoleAndPerson() {
        SDO sdo = this.getSDO();
        sdo.putProperty("singlePerson", "1");
        return forward("PermissionRoleAndPerson", sdo);
    }

    /**
     * 根据权限id查询具有权限的角色
     * 
     * @return
     */
    public String slicedQueryRolesByPermission() {
        SDO params = this.getSDO();
        PermissionsQueryRequest queryRequest = params.toQueryRequest(PermissionsQueryRequest.class);
        Map<String, Object> data = this.accessQueryApplication.slicedQueryRolesByPermission(queryRequest);
        return this.toResult(data);
    }

    /**
     * 根据权限查询授权信息
     * 
     * @return
     */
    public String slicedQueryRoleAuthorizeByPermission() {
        SDO params = this.getSDO();
        PermissionsQueryRequest queryRequest = params.toQueryRequest(PermissionsQueryRequest.class);
        Map<String, Object> data = this.accessQueryApplication.slicedQueryRoleAuthorizeByPermission(queryRequest);
        return this.toResult(data);
    }

    /**
     * 查询具有权限的用户
     * 
     * @return
     */
    public String slicedQueryPersonsByPermission() {
        SDO params = this.getSDO();
        PermissionsQueryRequest queryRequest = params.toQueryRequest(PermissionsQueryRequest.class);
        Map<String, Object> data = this.accessQueryApplication.slicedQueryPersonsByPermission(queryRequest);
        return this.toResult(data);
    }

    /**
     * 根据角色查询授权信息
     * 
     * @return
     */
    public String slicedQueryRoleByAuthorize() {
        SDO params = this.getSDO();
        PermissionsQueryRequest queryRequest = params.toQueryRequest(PermissionsQueryRequest.class);
        Map<String, Object> data = this.accessQueryApplication.slicedQueryRoleByAuthorize(queryRequest);
        return this.toResult(data);
    }

    /**
     * 根据角色权限的用户
     * 
     * @return
     */
    public String slicedQueryPersonAsRoleAuthorize() {
        SDO params = this.getSDO();
        PermissionsQueryRequest queryRequest = params.toQueryRequest(PermissionsQueryRequest.class);
        Map<String, Object> data = this.accessQueryApplication.slicedQueryPersonAsRoleAuthorize(queryRequest);
        return this.toResult(data);
    }
}