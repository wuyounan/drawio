package com.huigou.uasp.bmp.opm.proxy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huigou.data.domain.query.EmptyQueryRequest;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.data.domain.service.CommonDomainService;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.uasp.bmp.opm.application.ManagementApplication;
import com.huigou.uasp.bmp.opm.domain.model.management.BaseManagementType;
import com.huigou.uasp.bmp.opm.domain.model.management.BizManagementType;
import com.huigou.uasp.bmp.opm.domain.query.BizManagementTypesQueryRequest;
import com.huigou.uasp.bmp.opm.impl.ManagementApplicationImpl;
import com.huigou.uasp.bmp.opm.repository.managment.BaseManagementTypeRepository;
import com.huigou.uasp.bmp.opm.repository.managment.BizManagementRepository;
import com.huigou.uasp.bmp.opm.repository.managment.BizManagementTypeRepository;
import com.huigou.uasp.bmp.opm.repository.org.OrgRepository;

@Service("managementApplicationProxy")
public class ManagementApplicationProxy implements ManagementApplication {

    private ManagementApplication managementApplication;

    @Autowired
    private BaseManagementTypeRepository baseManagementTypeRepository;

    @Autowired
    private BizManagementTypeRepository bizManagementTypeRepository;

    @Autowired
    private BizManagementRepository bizManagementRepository;

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private CommonDomainService commonDomainService;

    @Autowired
    private SQLExecutorDao sqlExecutorDao;

    @Autowired
    private CoreApplicationFactory coreApplicationFactory;

    void initProperties(ManagementApplicationImpl managementApplicationImpl) {
        managementApplicationImpl.setBaseManagementTypeRepository(baseManagementTypeRepository);
        managementApplicationImpl.setBizManagementTypeRepository(bizManagementTypeRepository);
        managementApplicationImpl.setBizManagementRepository(bizManagementRepository);
        managementApplicationImpl.setOrgRepository(orgRepository);
        managementApplicationImpl.setSqlExecutorDao(sqlExecutorDao);
        managementApplicationImpl.setCommonDomainService(commonDomainService);
    }

    private ManagementApplication getManagementApplication() {
        if (managementApplication == null) {
            // synchronized (ManagementApplicationProxy.class) {
            if (managementApplication == null) {
                ManagementApplicationImpl managementApplicationImpl = coreApplicationFactory.getManagementApplication();
                managementApplication = managementApplicationImpl;
            }
            // }
        }
        return managementApplication;
    }

    @Override
    @Transactional
    public String saveBaseManagementType(BaseManagementType baseManagementType, String bizManagementTypeId) {
        return getManagementApplication().saveBaseManagementType(baseManagementType, bizManagementTypeId);
    }

    @Override
    @Transactional
    public void deleteBaseManagementTypes(List<String> ids) {
        getManagementApplication().deleteBaseManagementTypes(ids);
    }

    @Override
    @Transactional
    public Integer getBaseManagementTypeNextSequence(String folderId) {
        return getManagementApplication().getBaseManagementTypeNextSequence(folderId);
    }

    @Override
    @Transactional
    public void updateBaseManagementTypeSequence(Map<String, Integer> params) {
        getManagementApplication().updateBaseManagementTypeSequence(params);
    }

    @Override
    @Transactional
    public void moveBaseManagementTypes(List<String> ids, String folderId) {
        getManagementApplication().moveBaseManagementTypes(ids, folderId);
    }

    @Override
    public BaseManagementType loadBaseManagementType(String id) {
        return getManagementApplication().loadBaseManagementType(id);
    }

    @Override
    public Map<String, Object> slicedQueryBaseManagementTypes(FolderAndCodeAndNameQueryRequest queryRequest) {
        return getManagementApplication().slicedQueryBaseManagementTypes(queryRequest);
    }

    @Override
    @Transactional
    public String saveBizManagementType(BizManagementType bizManagementType) {
        return getManagementApplication().saveBizManagementType(bizManagementType);
    }

    @Override
    @Transactional
    public void deleteBizManagementTypes(List<String> ids) {
        getManagementApplication().deleteBizManagementTypes(ids);
    }

    @Override
    @Transactional
    public Integer getBizManagementTypeNextSequence(String parentId) {
        return getManagementApplication().getBizManagementTypeNextSequence(parentId);
    }

    @Override
    @Transactional
    public void updateBizManagementTypeSequence(Map<String, Integer> params) {
        getManagementApplication().updateBizManagementTypeSequence(params);
    }

    @Override
    @Transactional
    public void moveBizManagementTypes(List<String> ids, String parentId) {
        getManagementApplication().moveBizManagementTypes(ids, parentId);
    }

    @Override
    public BizManagementType loadBizManagementType(String id) {
        return getManagementApplication().loadBizManagementType(id);
    }

    @Override
    public List<Map<String, Object>> queryBizManagementTypes(String parentId) {
        return getManagementApplication().queryBizManagementTypes(parentId);
    }

    @Override
    public Map<String, Object> slicedQueryBizManagementTypes(ParentAndCodeAndNameQueryRequest queryRequest) {
        return getManagementApplication().slicedQueryBizManagementTypes(queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryBizManagementTypes(BizManagementTypesQueryRequest queryRequest) {
        return getManagementApplication().slicedQueryBizManagementTypes(queryRequest);
    }

    @Override
    @Transactional
    public void allocateManagers(List<String> managerIds, String manageTypeId, String subordinationId) {
        getManagementApplication().allocateManagers(managerIds, manageTypeId, subordinationId);
    }

    @Override
    @Transactional
    public void allocateSubordinations(String managerId, String manageTypeId, List<String> subordinationIds) {
        getManagementApplication().allocateSubordinations(managerId, manageTypeId, subordinationIds);
    }

    @Override
    @Transactional
    public void deleteBizManagements(List<String> ids) {
        getManagementApplication().deleteBizManagements(ids);
    }

    @Override
    public Map<String, Object> slicedQueryBizManagementsByManagerId(String managerId, String manageTypeId, EmptyQueryRequest queryRequest) {
        return getManagementApplication().slicedQueryBizManagementsByManagerId(managerId, manageTypeId, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryBizManagementsBySubordinationId(String subordinationId, String manageTypeId, EmptyQueryRequest queryRequest) {
        return getManagementApplication().slicedQueryBizManagementsBySubordinationId(subordinationId, manageTypeId, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryOrgAllocatedBizManagementTypeForManager(String orgFullId, EmptyQueryRequest queryRequest) {
        return getManagementApplication().slicedQueryOrgAllocatedBizManagementTypeForManager(orgFullId, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryOrgAllocatedBizManagementTypeForSubordination(String orgFullId, EmptyQueryRequest queryRequest) {
        return getManagementApplication().slicedQueryOrgAllocatedBizManagementTypeForSubordination(orgFullId, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryBizManagementForManager(String orgFullId, String manageTypeId, EmptyQueryRequest queryRequest) {
        return getManagementApplication().slicedQueryBizManagementForManager(orgFullId, manageTypeId, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryBizManagementForSubordination(String orgFullId, String manageTypeId, EmptyQueryRequest queryRequest) {
        return getManagementApplication().slicedQueryBizManagementForSubordination(orgFullId, manageTypeId, queryRequest);
    }

    @Override
    public void removePermissionCache() {
        getManagementApplication().removePermissionCache();
    }

    @Override
    @Transactional
    public void quoteBizManagement(String sourceOrgId, String destOrgId) {
        getManagementApplication().quoteBizManagement(sourceOrgId, destOrgId);
    }

}
