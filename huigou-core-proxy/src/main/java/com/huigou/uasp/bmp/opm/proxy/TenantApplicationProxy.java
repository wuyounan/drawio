package com.huigou.uasp.bmp.opm.proxy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.service.CommonDomainService;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.data.repository.GeneralRepository;
import com.huigou.uasp.bmp.opm.application.TenantApplication;
import com.huigou.uasp.bmp.opm.domain.model.org.Tenant;
import com.huigou.uasp.bmp.opm.impl.TenantApplicationImpl;
import com.huigou.uasp.bmp.opm.repository.org.TenantRepository;

@Service("tenantApplicationProxy")
public class TenantApplicationProxy {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private CommonDomainService commonDomainService;

    @Autowired
    private SQLExecutorDao sqlExecutorDao;

    @Autowired
    private GeneralRepository generalRepository;

    @Autowired
    private CoreApplicationFactory coreApplicationFactory;

    private TenantApplication tenantApplication;
    
    void initProperties(TenantApplicationImpl tenantApplicationImpl){
        tenantApplicationImpl.setTenantRepository(tenantRepository);
        tenantApplicationImpl.setGeneralRepository(generalRepository);
        tenantApplicationImpl.setSqlExecutorDao(sqlExecutorDao);
        tenantApplicationImpl.setCommonDomainService(commonDomainService);
    }

    public TenantApplication getTenantApplication() {
        if (tenantApplication == null) {
           // synchronized (TenantApplicationProxy.class) {
                if (tenantApplication == null) {
                    TenantApplicationImpl tenantApplicationImpl = coreApplicationFactory.getTenantApplication();
                    tenantApplication = tenantApplicationImpl;
                }
          //  }
        }
        return tenantApplication;
    }

    public String saveTenant(Tenant tenant) {
        return getTenantApplication().saveTenant(tenant);
    }

    @Transactional
    public void deleteTenants(List<String> ids) {
        getTenantApplication().deleteTenants(ids);
    }

    @Transactional
    public void updateTenantStatus(List<String> ids, Integer status) {
        getTenantApplication().updateTenantStatus(ids, status);
    }

    public Tenant loadTenant(String id) {
        return getTenantApplication().loadTenant(id);
    }

    public Map<String, Object> slicedQueryTenants(FolderAndCodeAndNameQueryRequest queryRequest) {
        return getTenantApplication().slicedQueryTenants(queryRequest);
    }

    @Transactional
    public void buildOrgStructureByOrgId(String tenantId, String orgId) {
        getTenantApplication().buildOrgStructureByOrgId(tenantId, orgId);
    }

    @Transactional
    public void buildDefaultOrgStructure(String tenantId) {
        getTenantApplication().buildDefaultOrgStructure(tenantId);
    }

}
