package com.huigou.uasp.bmp.opm.proxy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.data.domain.service.CommonDomainService;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.uasp.bmp.opm.application.OrgTemplateApplication;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgTemplate;
import com.huigou.uasp.bmp.opm.impl.OrgTemplateApplicationImpl;
import com.huigou.uasp.bmp.opm.repository.org.OrgTemplateRepository;
import com.huigou.uasp.bmp.opm.repository.org.OrgTypeRepository;

@Service("orgTemplateApplicationProxy")
public class OrgTemplateApplicationProxy {

    private OrgTemplateApplication orgTemplateApplication;

    @Autowired
    private CommonDomainService commonDomainService;

    @Autowired
    private SQLExecutorDao sqlExecutorDao;

    @Autowired
    private OrgTemplateRepository orgTemplateRepository;

    @Autowired
    private OrgTypeRepository orgTypeRepository;
    
    @Autowired
    private CoreApplicationFactory coreApplicationFactory;
    
    void initProperties(OrgTemplateApplicationImpl orgTemplateApplicationImpl){
        orgTemplateApplicationImpl.setCommonDomainService(commonDomainService);
        orgTemplateApplicationImpl.setSqlExecutorDao(sqlExecutorDao);
        orgTemplateApplicationImpl.setOrgTemplateRepository(orgTemplateRepository);
        orgTemplateApplicationImpl.setOrgTypeRepository(orgTypeRepository);
    }

    private OrgTemplateApplication getOrgTemplateApplication() {
        if (orgTemplateApplication == null) {
            //synchronized (OrgTemplateApplicationProxy.class) {
                if (orgTemplateApplication == null) {
                    OrgTemplateApplicationImpl orgTemplateApplicationImpl = coreApplicationFactory.getOrgTemplateApplication();
                    
                    orgTemplateApplication = orgTemplateApplicationImpl;
                }
           // }
        }
        return orgTemplateApplication;
    }

    @Transactional
    public void insertOrgTemplates(String parentId, List<String> orgTypeIds) {
        getOrgTemplateApplication().insertOrgTemplates(parentId, orgTypeIds);
    }

    public OrgTemplate loadOrgTemplate(String id) {
        return getOrgTemplateApplication().loadOrgTemplate(id);
    }

    @Transactional
    public void deleteOrgTemplates(List<String> ids) {
        getOrgTemplateApplication().deleteOrgTemplates(ids);
    }

    @Transactional
    public void updateOrgTemplateSequence(Map<String, Integer> params) {
        getOrgTemplateApplication().updateOrgTemplateSequence(params);
    }

    public Map<String, Object> queryOrgTemplates(ParentAndCodeAndNameQueryRequest queryRequest) {
        return getOrgTemplateApplication().queryOrgTemplates(queryRequest);
    }

}
