package com.huigou.uasp.bmp.opm.application;

import java.util.List;
import java.util.Map;

import com.huigou.context.RoleKind;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.uasp.bmp.opm.domain.model.access.Permission;
import com.huigou.uasp.bmp.opm.domain.model.access.Role;
import com.huigou.uasp.bmp.opm.domain.model.access.UIElementPermission;
import com.huigou.uasp.bmp.opm.domain.model.resource.ResourceOperation;
import com.huigou.uasp.bmp.opm.domain.query.AuthorizationsQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.AuthorizedPermissionsByOrgFullIdQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.PermissionsByRoleIdQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.RolesQueryRequestQueryRequest;
import com.huigou.util.SDO;

/**
 * 访问控制应用
 * 
 * @author gongmm
 */
public interface AccessApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/opm.xml";

    /**
     * 保存角色
     * 
     * @param role
     *            角色
     * @return 角色ID
     */
    String saveRole(Role role);

    /**
     * 从数据库读取一个角色数据
     * 
     * @param id
     *            角色ID
     */
    Role loadRole(String id);

    /**
     * 删除角色
     * 
     * @param ids
     *            角色ID列表
     */
    void deleteRoles(List<String> ids);

    /**
     * 更新排序号
     * 
     * @param roles
     *            角色ID和排序号的Map
     */
    void updateRolesSequence(Map<String, Integer> params);

    /**
     * 移动角色
     * 
     * @param ids
     *            角色ID列表
     * @param parentId
     *            父ID
     */
    void moveRoles(List<String> ids, String parentId);

    /**
     * 移动租户角色
     * 
     * @param ids
     *            角色ID列表
     * @param parentId
     *            父ID
     */
    void moveTenantRoles(List<String> ids, String parentId);

    /**
     * 获取角色下一个排序号
     * 
     * @param parentId
     *            父ID
     * @return 角色下一个排序号
     */
    Integer getRoleNextSequence(String parentId);

    /**
     * 查询角色数据
     * 
     * @param tenantKindId
     *            租户类别
     * @param parentId
     *            父ID
     * @return
     */
    List<Map<String, Object>> queryRoles(String tenantKindId, String parentId);

    /**
     * 分页查询角色数据
     * 
     * @param queryRequest
     *            查询 对象
     * @return
     */
    Map<String, Object> slicedQueryRoles(RolesQueryRequestQueryRequest queryRequest);

    /**
     * 初始化权限资源类型
     */
    void saveInitPermissionResourceKind();

    /**
     * 保存权限
     * 
     * @param permission
     *            权限实体
     * @return 权限ID
     */
    String savePermission(Permission permission);

    /**
     * 更新权限
     * 
     * @param permission
     *            权限实体
     */
    void updatePermission(Permission permission);

    /**
     * 加载权限
     * 
     * @param id
     *            权限ID
     * @return 权限实体
     */
    Permission loadPermission(String id);

    /**
     * 删除权限
     * <p>
     * 若权限已分配给角色，不能删除。
     * 
     * @param ids
     *            权限ID列表
     */
    void deletePermissions(List<String> ids);

    /**
     * 更新权限排序号
     * 
     * @param permissions
     *            权限ID和排序号的Map
     */
    void updatePermissionsSequence(Map<String, Integer> permissions);

    /**
     * 更新权限状态
     * 
     * @param ids
     *            权限ID列表
     * @param status
     *            状态
     */
    void updatePermissionsStatus(List<String> ids, Integer status);

    /**
     * 移动权限
     * 
     * @param ids
     *            权限ID列表
     * @param parentId
     *            父ID
     */
    void movePermissions(List<String> ids, String parentId);

    /**
     * 查询权限
     * 
     * @param queryRequest
     *            权限查询对象
     * @return 查询结果集
     */
    Map<String, Object> queryPermissions(FolderAndCodeAndNameQueryRequest queryRequest);

    /**
     * 保存界面元素权限
     * 
     * @param uiElementPermissions
     *            界面元素权限列表
     */
    void saveUIElementPermissions(List<UIElementPermission> uiElementPermissions);

    /**
     * 删除界面元素权限
     * 
     * @param ids
     *            界面元素ID列表
     */
    void deleteUIElementPermissions(List<String> ids);

    /**
     * 分页查询界面元素权限
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryUIElementPermissions(ParentIdQueryRequest queryRequest);

    /**
     * 得到权限的下一个排序号
     * 
     * @param parentId
     *            父ID
     * @return
     */
    Integer getPermissionNextSequence(String parentId);

    /**
     * 分配角色
     * 
     * @param orgId
     *            组织ID
     * @param roleIds
     *            角色ID列表
     */
    void allocateRoles(String orgId, List<String> roleIds);

    /**
     * 取消分配角色
     * 
     * @param orgId
     *            组织ID
     * @param roleIds
     *            角色ID列表
     */
    void deallocateRoles(String orgId, List<String> roleIds);

    /**
     * 查询授权
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> queryAuthorizations(AuthorizationsQueryRequest queryRequest);

    /**
     * 查询权限
     * 
     * @param parentId
     *            父ID
     * @return
     */
    List<Map<String, Object>> queryPermissionsByParentId(String parentId);

    /**
     * 根据角色ID查询功能权限
     * 
     * @param roleId
     *            角色ID
     * @param parentId
     *            父ID
     * @return
     */
    List<Permission> queryAllocatedPermissions(String roleId, String parentId);

    /**
     * 查询权限
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryPermissionsByRoleId(PermissionsByRoleIdQueryRequest queryRequest);

    /**
     * 分页查询已授权的权限
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryAuthorizedPermissionsByOrgFullId(AuthorizedPermissionsByOrgFullIdQueryRequest queryRequest);

    /**
     * 分配功能权限
     * 
     * @param roleId
     *            角色ID
     * @param oneLevelPermissionId
     *            一级权限ID
     * @param permissionIds
     *            权限ID列表
     */
    void allocateFunPermissions(String roleId, String oneLevelPermissionId, List<String> permissionIds);

    /**
     * 取消分配功能权限
     * 
     * @param roleId
     *            角色ID
     * @param permissionIds
     *            权限ID列表
     */
    void deallocateFunPermissions(String roleId, List<String> permissionIds);

    /**
     * 查询人员的功能权限
     * 
     * @param personId
     *            人员ID
     * @return
     */
    List<String> queryPersonFunPermissions(String personId);

    /**
     * 查询人员角色
     * 
     * @param personId
     *            人员ID
     * @return
     */
    List<String> queryPersonRoleIds(String personId);

    /**
     * 获取人员角色类别
     * 
     * @param personId
     *            人员ID
     * @return
     */
    RoleKind getPersonRoleKind(String personId);

    /**
     * 加载资源操作
     * 
     * @param id
     *            资源操作ID
     * @return
     *         资源操作
     */
    ResourceOperation loadResourceOperation(String id);

    /**
     * 查询人员功能权限
     * 
     * @param personId
     *            人员id
     * @param parentId
     *            父id
     * @return
     */
    List<Map<String, Object>> queryPersonFunctions(String personId, String parentId);

    /**
     * 查询人员一级下的所有功能,不包括文件夹
     * 
     * @param personId
     * @param parentId
     * @return
     */
    List<Map<String, Object>> queryPersonOneLevelAllFunctions(String personId, String parentId);

    /**
     * 查询人员一级下的所有功能
     * 
     * @param personId
     * @param parentId
     * @return
     */
    List<Map<String, Object>> queryPersonAllFunctions(String personId, String parentId);

    /**
     * 检验人员是否具有某个功能权限
     * 
     * @param personId
     *            人员ID
     * @param funcCode
     *            功能编码
     * @return
     */
    boolean checkPersonFunPermissions(String personId, String funcCode);

    /**
     * 加人员角色
     * 
     * @param personId
     *            人员id
     * @return
     */
    List<Map<String, Object>> loadPersonRole(String personId);

    /**
     * 管理权限鉴权
     * 
     * @author
     * @param
     * @return boolean
     */
    boolean authenticationManageType(SDO sdo);

    /**
     * 查询界面元素操作
     * 
     * @return
     */
    Map<String, Object> queryUIElementOperations();

    /**
     * 查询界面元素权限
     * 
     * @param function
     * @param operator
     * @param isId
     * @return
     */
    List<Map<String, Object>> queryUIElementPermissionsByFunction(String function, String personId, boolean isId);

    /**
     * 隐藏超级管理员
     */
    void hideSuperAdministrator();

    /**
     * 同步三员权限
     */
    void synThreeMemberPermission();
}
