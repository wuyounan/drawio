package com.huigou.uasp.bmp.opm.proxy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.data.domain.service.CommonDomainService;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.uasp.bmp.opm.application.SysFunctionApplication;
import com.huigou.uasp.bmp.opm.domain.model.resource.SysFunction;
import com.huigou.uasp.bmp.opm.impl.SysFunctionApplicationImpl;
import com.huigou.uasp.bmp.opm.repository.org.PermissionRepository;
import com.huigou.uasp.bmp.opm.repository.org.ResourceOperationRepository;
import com.huigou.uasp.bmp.opm.repository.org.SysFunctionRepository;

@Service("sysFunctionApplicationProxy")
public class SysFunctionApplicationProxy {

    @Autowired
    private CommonDomainService commonDomainService;

    @Autowired
    private SQLExecutorDao sqlExecutorDao;

    @Autowired
    private SysFunctionRepository sysFunctionRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ResourceOperationRepository resourceOperationRepository;

    @Autowired
    private CoreApplicationFactory coreApplicationFactory;

    private SysFunctionApplication sysFunctionApplication;

    void initProperties(SysFunctionApplicationImpl sysFunctionApplicationImpl) {
        sysFunctionApplicationImpl.setCommonDomainService(commonDomainService);
        sysFunctionApplicationImpl.setSqlExecutorDao(sqlExecutorDao);
        sysFunctionApplicationImpl.setPermissionRepository(permissionRepository);
        sysFunctionApplicationImpl.setResourceOperationRepository(resourceOperationRepository);
        sysFunctionApplicationImpl.setSysFunctionRepository(sysFunctionRepository);
    }

    private SysFunctionApplication getSysFunctionApplication() {
        if (sysFunctionApplication == null) {
            // synchronized (SysFunctionApplicationProxy.class) {
            if (sysFunctionApplication == null) {
                SysFunctionApplicationImpl sysFunctionApplicationImpl = coreApplicationFactory.getSysFunctionApplication();
                sysFunctionApplication = sysFunctionApplicationImpl;
            }
            // }
        }
        return sysFunctionApplication;
    }

    @Transactional
    public String insertSysFunction(SysFunction sysFunction) {
        return getSysFunctionApplication().insertSysFunction(sysFunction);
    }

    @Transactional
    public String updateSysFunction(SysFunction sysFunction) {
        return getSysFunctionApplication().updateSysFunction(sysFunction);
    }

    public SysFunction loadSysFunction(String id) {
        return getSysFunctionApplication().loadSysFunction(id);
    }

    @Transactional
    public void deleteSysFunctions(List<String> ids) {
        getSysFunctionApplication().deleteSysFunctions(ids);
    }

    @Transactional
    public void updateSysFunctionsStatus(List<String> ids, Integer status) {
        getSysFunctionApplication().updateSysFunctionsStatus(ids, status);
    }

    public Map<String, Object> querySysFunctions(ParentAndCodeAndNameQueryRequest queryRequest) {
        return getSysFunctionApplication().querySysFunctions(queryRequest);
    }

    @Transactional
    public Integer getSysFunctionNextSequence(String parentId) {
        return getSysFunctionApplication().getSysFunctionNextSequence(parentId);
    }

    @Transactional
    public void updateSysFunctionsSequence(Map<String, Integer> params) {
        getSysFunctionApplication().updateSysFunctionsSequence(params);
    }

    @Transactional
    public void buildPermission(String fullId) {
        getSysFunctionApplication().buildPermission(fullId);
    }

    @Transactional
    public void moveFunctions(String parentId, List<String> ids) {
        getSysFunctionApplication().moveFunctions(parentId, ids);
    }

}
