package com.huigou.uasp.bmp.opm.proxy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huigou.data.domain.service.CommonDomainService;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.uasp.bmp.opm.application.OrgTypeApplication;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgType;
import com.huigou.uasp.bmp.opm.domain.query.OrgTypeQueryRequest;
import com.huigou.uasp.bmp.opm.impl.OrgTypeApplicationImpl;
import com.huigou.uasp.bmp.opm.repository.org.OrgRepository;
import com.huigou.uasp.bmp.opm.repository.org.OrgTemplateRepository;
import com.huigou.uasp.bmp.opm.repository.org.OrgTypeRepository;

@Service("orgTypeApplicationProxy")
public class OrgTypeApplicationProxy {

    @Autowired
    private CommonDomainService commonDomainService;

    @Autowired
    private SQLExecutorDao sqlExecutorDao;

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private OrgTemplateRepository orgTemplateRepository;

    @Autowired
    private OrgTypeRepository orgTypeRepository;

    @Autowired
    private CoreApplicationFactory coreApplicationFactory;

    private OrgTypeApplication orgTypeApplication;

    void initProperties(OrgTypeApplicationImpl orgTypeApplicationImpl) {
        orgTypeApplicationImpl.setCommonDomainService(commonDomainService);
        orgTypeApplicationImpl.setOrgRepository(orgRepository);
        orgTypeApplicationImpl.setOrgTemplateRepository(orgTemplateRepository);
        orgTypeApplicationImpl.setOrgTypeRepository(orgTypeRepository);
        orgTypeApplicationImpl.setSqlExecutorDao(sqlExecutorDao);
    }

    private OrgTypeApplication getOrgTypeApplication() {
        if (orgTypeApplication == null) {
            //synchronized (OrgTypeApplicationProxy.class) {
                if (orgTypeApplication == null) {
                    OrgTypeApplicationImpl orgTypeApplicationImpl = coreApplicationFactory.getOrgTypeApplication();
                    orgTypeApplication = orgTypeApplicationImpl;
                }
            //}
        }
        return orgTypeApplication;
    }

    @Transactional
    public String saveOrgType(OrgType orgType) {
        return getOrgTypeApplication().saveOrgType(orgType);
    }

    @Transactional
    public void deleteOrgTypes(List<String> ids) {
        getOrgTypeApplication().deleteOrgTypes(ids);
    }

    @Transactional
    public void updateOrgTypeSequence(Map<String, Integer> orgTypes) {
        getOrgTypeApplication().updateOrgTypeSequence(orgTypes);
    }

    @Transactional
    public void moveOrgType(List<String> ids, String folderId) {
        getOrgTypeApplication().moveOrgType(ids, folderId);
    }

    public OrgType loadOrgType(String id) {
        return getOrgTypeApplication().loadOrgType(id);
    }

    public Map<String, Object> slicedQueryOrgTypes(OrgTypeQueryRequest queryRequest) {
        return getOrgTypeApplication().slicedQueryOrgTypes(queryRequest);
    }

    @Transactional
    public Integer getNextSequence(String folderId) {
        return getOrgTypeApplication().getNextSequence(folderId);
    }

}
