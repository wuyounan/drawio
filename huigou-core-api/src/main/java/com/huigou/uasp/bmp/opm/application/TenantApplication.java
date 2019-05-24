package com.huigou.uasp.bmp.opm.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.uasp.bmp.opm.domain.model.org.Tenant;

/**
 * 租户应用
 * 
 * @author gongmm
 */
public interface TenantApplication {

    static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/tenant.xml";

    /**
     * 保存租户
     * 
     * @param tenant
     *            租户实体
     */
    String saveTenant(Tenant tenant);

    /**
     * 删除租户
     * 
     * @param ids
     *            租户ID列表
     */
    void deleteTenants(List<String> ids);

    /**
     * 更新租户状态
     * 
     * @param ids
     * @param status
     */
    void updateTenantStatus(List<String> ids, Integer status);

    /**
     * 从数据库读取一个租户数据
     *
     * @param id
     *            租户ID
     */
    Tenant loadTenant(String id);

    /**
     * 分页查询租户信息
     * 
     * @param queryRequest
     *            查询请求
     */
    Map<String, Object> slicedQueryTenants(FolderAndCodeAndNameQueryRequest queryRequest);

    /**
     * 根据组织ID生成组织
     * @param tenantId
     *            租户ID
     * @param orgId
     *            组织ID
     */
    void buildOrgStructureByOrgId(String tenantId, String orgId);

    /**
     * 生成默认组织
     * 
     * @param tenantId
     *            租户ID
     */
    void buildDefaultOrgStructure(String tenantId);

}
