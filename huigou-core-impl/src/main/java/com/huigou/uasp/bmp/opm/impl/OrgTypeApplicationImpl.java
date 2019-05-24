package com.huigou.uasp.bmp.opm.impl;

import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.opm.application.OrgTypeApplication;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgTemplate;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgType;
import com.huigou.uasp.bmp.opm.domain.query.OrgTypeQueryRequest;
import com.huigou.uasp.bmp.opm.repository.org.OrgRepository;
import com.huigou.uasp.bmp.opm.repository.org.OrgTemplateRepository;
import com.huigou.uasp.bmp.opm.repository.org.OrgTypeRepository;

/**
 * 组织类型服务
 * 
 * @author gongmm
 */
public class OrgTypeApplicationImpl extends BaseApplication implements OrgTypeApplication {

    private OrgTypeRepository orgTypeRepository;

    private OrgTemplateRepository orgTemplateRepository;

    private OrgRepository orgRepository;

    public void setOrgTypeRepository(OrgTypeRepository orgTypeRepository) {
        this.orgTypeRepository = orgTypeRepository;
    }

    public void setOrgTemplateRepository(OrgTemplateRepository orgTemplateRepository) {
        this.orgTemplateRepository = orgTemplateRepository;
    }

    public void setOrgRepository(OrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    @Override
    public String saveOrgType(OrgType orgType) {
        Assert.notNull(orgType, "参数orgType不能为空。");
        orgType = (OrgType) commonDomainService.loadAndFillinProperties(orgType);
        orgType = (OrgType) this.commonDomainService.saveBaseInfoWithFolderEntity(orgType, orgTypeRepository);
        return orgType.getId();
    }

    @Override
    public void deleteOrgTypes(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));

        Org org;
        OrgTemplate orgTemplate;
        String errorMessage;
        List<OrgType> orgTypes = this.orgTypeRepository.findAll(ids);

        for (OrgType orgType : orgTypes) {
            orgTemplate = orgTemplateRepository.findFirstByOrgType(orgType);
            errorMessage = MessageSourceContext.getMessage(MessageConstants.OBJECT_REFERENCED_BY_WHO, orgType.getName(), "组织机构模板");
            Assert.isTrue(orgTemplate == null, errorMessage);

            org = this.orgRepository.findFirstByOrgType(orgType);
            if (org != null) {
                errorMessage = MessageSourceContext.getMessage(MessageConstants.OBJECT_REFERENCED_BY_WHO, orgType.getName(), "组织：" + org.getName());
            }
            Assert.isTrue(org == null, errorMessage);

        }
        this.orgTypeRepository.delete(orgTypes);
    }

    @Override
    public void moveOrgType(List<String> ids, String folderId) {
        this.commonDomainService.moveForFolder(OrgType.class, ids, folderId);
    }

    @Override
    public OrgType loadOrgType(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return orgTypeRepository.findOne(id);
    }

    @Override
    public Map<String, Object> slicedQueryOrgTypes(OrgTypeQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "orgType");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public void updateOrgTypeSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(OrgType.class, params);
    }

    @Override
    public Integer getNextSequence(String folderId) {
        return this.commonDomainService.getNextSequence(OrgType.class, CommonDomainConstants.FOLDER_ID_FIELD_NAME, folderId);
    }

}
