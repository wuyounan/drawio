package com.huigou.uasp.bmp.opm.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.opm.application.OrgTemplateApplication;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgTemplate;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgType;
import com.huigou.uasp.bmp.opm.domain.query.OrgTemplateDesc;
import com.huigou.uasp.bmp.opm.repository.org.OrgTemplateRepository;
import com.huigou.uasp.bmp.opm.repository.org.OrgTypeRepository;
import com.huigou.util.Constants;

public class OrgTemplateApplicationImpl extends BaseApplication implements OrgTemplateApplication {

    private OrgTemplateRepository orgTemplateRepository;

    private OrgTypeRepository orgTypeRepository;

    public void setOrgTemplateRepository(OrgTemplateRepository orgTemplateRepository) {
        this.orgTemplateRepository = orgTemplateRepository;
    }

    public void setOrgTypeRepository(OrgTypeRepository orgTypeRepository) {
        this.orgTypeRepository = orgTypeRepository;
    }

    @Override
    public void insertOrgTemplates(String parentId, List<String> orgTypeIds) {
        Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
        Assert.notEmpty(orgTypeIds, "参数orgTypeIds不能为空。");

        OrgTemplate parent;

        List<OrgTemplate> children;

        if (OrgTemplate.ROOT_ID_VALUE.equals(parentId)) {
            parent = OrgTemplate.createRoot();
            children = this.orgTemplateRepository.findByParentId(parentId);
        } else {
            parent = orgTemplateRepository.findOne(parentId);
            children = parent.getChildren();
        }

        Assert.notNull(parent, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, parentId, "组织模板"));

        List<OrgTemplate> orgTemplates = new ArrayList<OrgTemplate>();

        OrgType orgType;
        OrgTemplate orgTemplate;
        Integer sequence = children.size();

        boolean found;

        for (String orgTypeId : orgTypeIds) {
            found = false;
            for (OrgTemplate item : children) {
                if (item.getOrgType().getId().equalsIgnoreCase(orgTypeId)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                orgType = this.orgTypeRepository.findOne(orgTypeId);
                orgTemplate = new OrgTemplate(parentId, orgType, ++sequence);
                orgTemplates.add(orgTemplate);
            }
        }

        if (orgTemplates.size() > 0) {
            this.orgTemplateRepository.save(orgTemplates);
        }
    }

    @Override
    public OrgTemplate loadOrgTemplate(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.orgTemplateRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteOrgTemplates(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));

        List<OrgTemplate> orgTemplates = orgTemplateRepository.findAll(ids);

        long childrenCount;
        Set<String> parentIds = new HashSet<String>(orgTemplates.size());
        for (OrgTemplate item : orgTemplates) {
            childrenCount = this.orgTemplateRepository.countByParentId(item.getId());
            Assert.isTrue(childrenCount == 0, MessageSourceContext.getMessage(MessageConstants.CAN_NOT_DELETE_HAS_CHILDREN, item.getOrgType().getName()));
            parentIds.add(item.getParentId());
        }

        this.orgTemplateRepository.delete(orgTemplates);
    }

    @Override
    public void updateOrgTemplateSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(OrgTemplate.class, params);
    }

    @Override
    public Map<String, Object> queryOrgTemplates(ParentAndCodeAndNameQueryRequest queryRequest) {
        if (OrgTemplate.ROOT_PARENT_ID_VALUE.equals(queryRequest.getParentId())) {
            Long childrenCount = orgTemplateRepository.countByParentId(OrgTemplate.ROOT_ID_VALUE);
            OrgTemplateDesc root = OrgTemplateDesc.createRoot(childrenCount.intValue());

            List<OrgTemplateDesc> orgTemplateDescs = new ArrayList<OrgTemplateDesc>(1);
            orgTemplateDescs.add(root);

            Map<String, Object> result = new HashMap<String, Object>(1);
            result.put(Constants.ROWS, orgTemplateDescs);
            return result;
        } else {
            QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "orgTemplate");
            return this.sqlExecutorDao.executeQuery(queryDescriptor, queryRequest);
        }
    }
}
