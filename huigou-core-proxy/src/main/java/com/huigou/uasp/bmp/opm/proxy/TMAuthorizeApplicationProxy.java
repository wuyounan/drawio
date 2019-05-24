package com.huigou.uasp.bmp.opm.proxy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huigou.context.TmspmConifg;
import com.huigou.data.domain.service.CommonDomainService;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.uasp.bmp.opm.application.TMAuthorizeApplication;
import com.huigou.uasp.bmp.opm.domain.model.access.TMAuthorize;
import com.huigou.uasp.bmp.opm.impl.TMAuthorizeApplicationImpl;
import com.huigou.uasp.bmp.opm.repository.org.RoleRepository;
import com.huigou.uasp.bmp.opm.repository.org.TMAuthorizeRepository;
import com.huigou.uasp.bmp.securitypolicy.application.ApplicationSystemApplication;
import com.huigou.util.SDO;

@Service("tmAuthorizeApplicationProxy")
public class TMAuthorizeApplicationProxy {

    @Autowired
    private CommonDomainService commonDomainService;

    @Autowired
    private SQLExecutorDao sqlExecutorDao;

    @Autowired
    private TMAuthorizeRepository tmAuthorizeRepository;

    @Autowired
    private ApplicationSystemApplication applicationSystemApplication;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TmspmConifg tmspmConifg;

    @Autowired
    private CoreApplicationFactory coreApplicationFactory;

    private TMAuthorizeApplication tmAuthorizeApplication;

    void initProperties(TMAuthorizeApplicationImpl tmAuthorizeApplicationImpl) {
        tmAuthorizeApplicationImpl.setCommonDomainService(commonDomainService);
        tmAuthorizeApplicationImpl.setSqlExecutorDao(sqlExecutorDao);
        tmAuthorizeApplicationImpl.setTMAuthorizeRepository(tmAuthorizeRepository);
        tmAuthorizeApplicationImpl.setApplicationSystemApplication(applicationSystemApplication);
        tmAuthorizeApplicationImpl.setRoleRepository(roleRepository);
        tmAuthorizeApplicationImpl.setTmspmConifg(tmspmConifg);
    }

    private TMAuthorizeApplication getTMAuthorizeApplication() {
        if (tmAuthorizeApplication == null) {
            //synchronized (TMAuthorizeApplicationProxy.class) {
                if (tmAuthorizeApplication == null) {
                    TMAuthorizeApplicationImpl tmAuthorizeApplicationImpl = coreApplicationFactory.getTMAuthorizeApplication();
                    tmAuthorizeApplication = tmAuthorizeApplicationImpl;
                }
            //}
        }
        return tmAuthorizeApplication;
    }

    @Transactional
    public void saveTMAuthorizes(List<TMAuthorize> tmAuthorizes, String subordinationId, String systemId, String roleKindId) {
        getTMAuthorizeApplication().saveTMAuthorizes(tmAuthorizes, subordinationId, systemId, roleKindId);

    }

    public Map<String, Object> queryTMAuthorizes(String subordinationId, String managerId) {
        return getTMAuthorizeApplication().queryTMAuthorizes(subordinationId, managerId);
    }

    public Map<String, Object> queryDelegationOrgs(SDO params) {
        return getTMAuthorizeApplication().queryDelegationOrgs(params);
    }

}
