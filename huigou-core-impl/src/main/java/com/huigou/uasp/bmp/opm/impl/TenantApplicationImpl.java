package com.huigou.uasp.bmp.opm.impl;

import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.opm.application.OrgApplication;
import com.huigou.uasp.bmp.opm.application.TenantApplication;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.Tenant;
import com.huigou.uasp.bmp.opm.repository.org.TenantRepository;
import com.huigou.util.StringUtil;

public class TenantApplicationImpl extends BaseApplication implements TenantApplication {
    private TenantRepository tenantRepository;

    private OrgApplication orgApplication;

    public void setTenantRepository(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public void setOrgApplication(OrgApplication orgApplication) {
        this.orgApplication = orgApplication;
    }

    @Override
    public String saveTenant(Tenant tenant) {
        Assert.notNull(tenant, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        Tenant dbTenant = (Tenant) commonDomainService.loadAndFillinProperties(tenant);
        dbTenant = (Tenant) this.commonDomainService.saveBaseInfoWithFolderEntity(dbTenant, tenantRepository);
        return dbTenant.getId();
    }

    @Override
    public void deleteTenants(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));
        List<Tenant> tenants = this.tenantRepository.findAll(ids);

        for (Tenant tenant : tenants) {
            Assert.isTrue(StringUtil.isBlank(tenant.getOrgId()), String.format("租户“%s”已创建了组织，不能删除。", tenant.getName()));
        }

        tenantRepository.delete(tenants);
    }

    @Override
    public void updateTenantStatus(List<String> ids, Integer status) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));
        Assert.notNull(status, MessageSourceContext.getMessage(MessageConstants.STATUS_NOT_BLANK));
        this.commonDomainService.updateStatus(Tenant.class, ids, status);
    }

    @Override
    public Tenant loadTenant(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return tenantRepository.findOne(id);
    }

    @Override
    public Map<String, Object> slicedQueryTenants(FolderAndCodeAndNameQueryRequest queryRequest) {
        QueryDescriptor query = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "tenant");
        return this.sqlExecutorDao.executeSlicedQuery(query, queryRequest);
    }

    private Tenant checkAndGetTenant(String tenantId) {
        Tenant tenant = this.loadTenant(tenantId);
        Assert.notNull(tenant, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, tenantId, "租户"));
        Assert.isTrue(StringUtil.isBlank(tenant.getOrgId()), "当前租户已生成组织。");
        return tenant;
    }

    private void setTenantOrgInfo(Tenant tenant, Org org) {
        tenant.setOrgId(org.getId());
        tenant.setRootFullId(org.getFullId());
        tenantRepository.save(tenant);
    }

    @Override
    public void buildOrgStructureByOrgId(String tenantId, String orgId) {
        Assert.hasText(tenantId, "参数tenantId不能为空。");
        Assert.hasText(orgId, "参数orgId不能为空。");

        Tenant tenant = checkAndGetTenant(tenantId);
        Org org = orgApplication.buildOrgStructureByOrgId(orgId, tenant);
        setTenantOrgInfo(tenant, org);
    }

    @Override
    public void buildDefaultOrgStructure(String tenantId) {
        Assert.hasText(tenantId, "参数tenantId不能为空。");

        Tenant tenant = checkAndGetTenant(tenantId);
        Org org = orgApplication.buildDefaultOrgStructure(tenant);
        setTenantOrgInfo(tenant, org);
    }
}
