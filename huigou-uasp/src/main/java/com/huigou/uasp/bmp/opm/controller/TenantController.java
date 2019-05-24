package com.huigou.uasp.bmp.opm.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.fn.impl.OrgFun;
import com.huigou.uasp.bmp.opm.domain.model.org.Tenant;
import com.huigou.uasp.bmp.opm.domain.query.TenantDesc;
import com.huigou.uasp.bmp.opm.proxy.TenantApplicationProxy;
import com.huigou.uasp.client.CommonController;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("tenant")
public class TenantController extends CommonController {

    private static final String TENANT_PAGE = "Tenant";

    private static final String TENANT_DETAIL_PAGE = "TenantDetail";

    private static final String TENANT_BASE_MANAGEMENT_CODE = "tenant";

    @Autowired
    private TenantApplicationProxy tenantApplication;

    @Autowired
    private OrgFun orgFun;

    protected String getPagePath() {
        return "/system/opm/organization/";
    }

    @RequiresPermissions("Tenant:query")
    public String forward() {
        return forward(TENANT_PAGE);
    }

    @RequiresPermissions("Tenant:create")
    public String showInsertTenant() {
        return forward(TENANT_DETAIL_PAGE);
    }

    @RequiresPermissions("Tenant:query")
    public String showUpdateTenant() {
        SDO sdo = this.getSDO();
        String id = sdo.getString(ID_KEY_NAME);
        Tenant tenant = tenantApplication.loadTenant(id);
        return forward(TENANT_DETAIL_PAGE, tenant);
    }

    @RequiresPermissions("Tenant:create")
    public String insertTenant() {
        SDO sdo = this.getSDO();
        Tenant tenant = sdo.toObject(Tenant.class);
        String id = tenantApplication.saveTenant(tenant);
        return success(id);
    }

    @RequiresPermissions("Tenant:delete")
    public String deleteTenant() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList(IDS_KEY_NAME);
        tenantApplication.deleteTenants(ids);
        return success();
    }

    @RequiresPermissions("Tenant:update")
    public String updateTenant() {
        SDO sdo = this.getSDO();
        Tenant tenant = sdo.toObject(Tenant.class);
        tenantApplication.saveTenant(tenant);
        return success();
    }

    @RequiresPermissions("Tenant:update")
    public String updateTenantStatus() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList(IDS_KEY_NAME);
        Integer status = sdo.getInteger(STATUS_KEY_NAME);
        this.tenantApplication.updateTenantStatus(ids, status);
        return success();
    }

    @RequiresPermissions("Tenant:query")
    public String slicedQueryTenants() {
        SDO sdo = this.getSDO();
        FolderAndCodeAndNameQueryRequest queryRequest = sdo.toQueryRequest(FolderAndCodeAndNameQueryRequest.class);
        Map<String, Object> data = tenantApplication.slicedQueryTenants(queryRequest);
        return this.toResult(data);
    }

    @RequiresPermissions("Tenant:update")
    public String buildOrgStructureByOrgId() {
        SDO sdo = this.getSDO();
        String tenantId = sdo.getString("tenantId");
        String orgId = sdo.getString("orgId");
        tenantApplication.buildOrgStructureByOrgId(tenantId, orgId);
        return success();
    }

    @RequiresPermissions("Tenant:update")
    public String buildDefaultOrgStructure() {
        SDO sdo = this.getSDO();
        String tenantId = sdo.getString("tenantId");
        tenantApplication.buildDefaultOrgStructure(tenantId);
        return success();
    }

    public String findSubordinationTenants() {
        List<TenantDesc> tenantDescs = orgFun.findSubordinationTenants(this.getOperator().getUserId(), TENANT_BASE_MANAGEMENT_CODE);
        return this.toResult(tenantDescs);
    }

}
