package com.huigou.uasp.bmp.opm.application;

import java.util.Map;

import com.huigou.uasp.bmp.opm.domain.query.AuthorizedPermissionsByOrgFullIdQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.PermissionsQueryRequest;

/**
 * 功能权限查询
 * 
 * @author xx
 */
public interface AccessQueryApplication {
    /**
     * 查询文件配置地址
     */
    static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/opm.xml";

    /**
     * 查询角色继承
     * 
     * @param queryRequest
     * @return
     */
    Map<String, Object> slicedQueryRolesByOrgFullId(AuthorizedPermissionsByOrgFullIdQueryRequest queryRequest);

    /**
     * 查询组织节点具备的功能
     * 
     * @param queryRequest
     * @return
     */
    Map<String, Object> slicedQueryAuthorizedPermissionsByOrgFullId(AuthorizedPermissionsByOrgFullIdQueryRequest queryRequest);

    /**
     * 根据权限id查询具有权限的角色
     * 
     * @param queryRequest
     * @return
     */
    Map<String, Object> slicedQueryRolesByPermission(PermissionsQueryRequest queryRequest);

    /**
     * 根据权限查询授权信息
     * 
     * @param queryRequest
     * @return
     */
    Map<String, Object> slicedQueryRoleAuthorizeByPermission(PermissionsQueryRequest queryRequest);

    /**
     * 查询具有权限的用户
     * 
     * @param queryRequest
     * @return
     */
    Map<String, Object> slicedQueryPersonsByPermission(PermissionsQueryRequest queryRequest);

    /**
     * 根据角色查询授权信息
     * 
     * @param queryRequest
     * @return
     */
    Map<String, Object> slicedQueryRoleByAuthorize(PermissionsQueryRequest queryRequest);

    /**
     * 根据角色权限的用户
     * 
     * @param queryRequest
     * @return
     */
    Map<String, Object> slicedQueryPersonAsRoleAuthorize(PermissionsQueryRequest queryRequest);
}
