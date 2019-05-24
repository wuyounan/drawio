package com.huigou.uasp.bmp.opm.proxy;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.cache.service.ICache;
import com.huigou.data.jdbc.SQLQuery;
import com.huigou.uasp.bmp.opm.LicenseChecker;
import com.huigou.uasp.bmp.opm.impl.AccessApplicationImpl;
import com.huigou.uasp.bmp.opm.impl.AgentApplicationImpl;
import com.huigou.uasp.bmp.opm.impl.AuthenticationApplicationImpl;
import com.huigou.uasp.bmp.opm.impl.ManagementApplicationImpl;
import com.huigou.uasp.bmp.opm.impl.OrgApplicationImpl;
import com.huigou.uasp.bmp.opm.impl.OrgTemplateApplicationImpl;
import com.huigou.uasp.bmp.opm.impl.OrgTypeApplicationImpl;
import com.huigou.uasp.bmp.opm.impl.PermissionApplicationImpl;
import com.huigou.uasp.bmp.opm.impl.PermissionBuilderImpl;
import com.huigou.uasp.bmp.opm.impl.StandardLicenseChecker;
import com.huigou.uasp.bmp.opm.impl.SysFunctionApplicationImpl;
import com.huigou.uasp.bmp.opm.impl.TMAuthorizeApplicationImpl;
import com.huigou.uasp.bmp.opm.impl.TenantApplicationImpl;
import com.huigou.uasp.bmp.opm.impl.UIElementApplicationImpl;
import com.huigou.uasp.bmp.opm.impl.UserGroupApplicationImpl;

@Service("coreApplicationFactory")
public class CoreApplicationFactory {

    private boolean created = false;

    @Resource(name = "permissionCache")
    private ICache icache;

    @Autowired
    private SQLQuery sqlQuery;

    @Autowired
    private OrgApplicationProxy orgApplicationProxy;

    @Autowired
    private TenantApplicationProxy tenantApplicationProxy;

    @Autowired
    private AccessApplicationProxy accessApplicationProxy;

    @Autowired
    private PermissionBuilderProxy permissionBuilderProxy;

    @Autowired
    private PermissionApplicationProxy permissionApplicationProxy;

    @Autowired
    private AgentApplicationProxy agentApplicationProxy;

    @Autowired
    private AuthenticationApplicationProxy authenticationApplicationProxy;

    @Autowired
    private ManagementApplicationProxy managementApplicationProxy;

    @Autowired
    private OrgTemplateApplicationProxy orgTemplateApplicationProxy;

    @Autowired
    private OrgTypeApplicationProxy orgTypeApplicationProxy;

    @Autowired
    private SysFunctionApplicationProxy sysFunctionApplicationProxy;

    @Autowired
    private TMAuthorizeApplicationProxy tmAuthorizeApplicationProxy;

    @Autowired
    private UIElementApplicationProxy uiElementApplicationProxy;

    @Autowired
    private UserGroupApplicationProxy userGroupApplicationProxy;

    private AccessApplicationImpl accessApplicationImpl;

    private AgentApplicationImpl agentApplicationImpl;

    private AuthenticationApplicationImpl authenticationApplicationImpl;

    private ManagementApplicationImpl managementApplicationImpl;

    private OrgApplicationImpl orgApplicationImpl;

    private TenantApplicationImpl tenantApplicationImpl;

    private OrgTemplateApplicationImpl orgTemplateApplicationImpl;

    private OrgTypeApplicationImpl orgTypeApplicationImpl;

    private PermissionBuilderImpl permissionBuilderImpl;

    private SysFunctionApplicationImpl sysFunctionApplicationImpl;

    private TMAuthorizeApplicationImpl tmAuthorizeApplicationImpl;

    private UIElementApplicationImpl uiElementApplicationImpl;

    private UserGroupApplicationImpl userGroupApplicationImpl;

    private PermissionApplicationImpl permissionApplicationImpl;

    public AccessApplicationImpl getAccessApplication() {
        createImpl();
        return accessApplicationImpl;
    }

    public AgentApplicationImpl getAgentApplication() {
        createImpl();
        return agentApplicationImpl;
    }

    public AuthenticationApplicationImpl getAuthenticationApplication() {
        createImpl();
        return authenticationApplicationImpl;
    }

    public ManagementApplicationImpl getManagementApplication() {
        createImpl();
        return managementApplicationImpl;
    }

    public OrgApplicationImpl getOrgApplication() {
        createImpl();
        return orgApplicationImpl;
    }

    public TenantApplicationImpl getTenantApplication() {
        createImpl();
        return tenantApplicationImpl;
    }

    public OrgTemplateApplicationImpl getOrgTemplateApplication() {
        createImpl();
        return orgTemplateApplicationImpl;
    }

    public OrgTypeApplicationImpl getOrgTypeApplication() {
        createImpl();
        return orgTypeApplicationImpl;
    }

    public PermissionBuilderImpl getPermissionBuilder() {
        createImpl();
        return permissionBuilderImpl;
    }

    public SysFunctionApplicationImpl getSysFunctionApplication() {
        createImpl();
        return sysFunctionApplicationImpl;
    }

    public TMAuthorizeApplicationImpl getTMAuthorizeApplication() {
        createImpl();
        return tmAuthorizeApplicationImpl;
    }

    public UIElementApplicationImpl getUIElementApplication() {
        createImpl();
        return uiElementApplicationImpl;
    }

    public UserGroupApplicationImpl getUserGroupApplication() {
        createImpl();
        return userGroupApplicationImpl;
    }

    public PermissionApplicationImpl getPermissionApplication() {
        createImpl();
        return permissionApplicationImpl;
    }

    private void createImpl() {
        if (!created) {
            synchronized (CoreApplicationFactory.class) {
                if (!created) {
                    created = true;
                    accessApplicationImpl = new AccessApplicationImpl();
                    agentApplicationImpl = new AgentApplicationImpl();
                    authenticationApplicationImpl = new AuthenticationApplicationImpl();
                    managementApplicationImpl = new ManagementApplicationImpl();
                    orgApplicationImpl = new OrgApplicationImpl();
                    tenantApplicationImpl = new TenantApplicationImpl();
                    orgTemplateApplicationImpl = new OrgTemplateApplicationImpl();
                    orgTypeApplicationImpl = new OrgTypeApplicationImpl();
                    permissionBuilderImpl = new PermissionBuilderImpl();
                    sysFunctionApplicationImpl = new SysFunctionApplicationImpl();
                    tmAuthorizeApplicationImpl = new TMAuthorizeApplicationImpl();
                    uiElementApplicationImpl = new UIElementApplicationImpl();
                    userGroupApplicationImpl = new UserGroupApplicationImpl();
                    permissionApplicationImpl = new PermissionApplicationImpl();
                    // licenseChecker = new StandardLicenseChecker();
                    initProperties();
                }
            }
        }
    }

    private void initProperties() {
        accessApplicationImpl.setOrgApplication(orgApplicationImpl);
        accessApplicationImpl.setTenantApplication(tenantApplicationImpl);
        accessApplicationImpl.setPermissionBuilder(permissionBuilderImpl);

        authenticationApplicationImpl.setAccessApplication(accessApplicationImpl);
        authenticationApplicationImpl.setOrgApplication(orgApplicationImpl);

        managementApplicationImpl.setPermissionBuilder(permissionBuilderImpl);
        orgApplicationImpl.setPermissionBuilder(permissionBuilderImpl);
        tenantApplicationImpl.setOrgApplication(orgApplicationImpl);
        permissionBuilderImpl.setAccessApplication(accessApplicationImpl);
        permissionBuilderImpl.setIcache(icache);
        tmAuthorizeApplicationImpl.setOrgApplication(orgApplicationImpl);
        tmAuthorizeApplicationImpl.setAccessApplication(accessApplicationImpl);

        sysFunctionApplicationImpl.setPermissionApplication(permissionApplicationImpl);

        accessApplicationProxy.initProperties(accessApplicationImpl);
        agentApplicationProxy.initProperties(agentApplicationImpl);
        // authenticationApplicationProxy.initProperties();
        managementApplicationProxy.initProperties(managementApplicationImpl);
        orgApplicationProxy.initProperties(orgApplicationImpl);
        orgTemplateApplicationProxy.initProperties(orgTemplateApplicationImpl);
        orgTypeApplicationProxy.initProperties(orgTypeApplicationImpl);
        permissionBuilderProxy.initProperties(permissionBuilderImpl);
        permissionApplicationProxy.initProperties(permissionApplicationImpl);
        sysFunctionApplicationProxy.initProperties(sysFunctionApplicationImpl);
        tenantApplicationProxy.initProperties(tenantApplicationImpl);
        tmAuthorizeApplicationProxy.initProperties(tmAuthorizeApplicationImpl);
        uiElementApplicationProxy.initProperties(uiElementApplicationImpl);
        userGroupApplicationProxy.initProperties(userGroupApplicationImpl);
    }

    public LicenseChecker getLicenseChecker() {
        return StandardLicenseChecker.getInstance();
    }

}
