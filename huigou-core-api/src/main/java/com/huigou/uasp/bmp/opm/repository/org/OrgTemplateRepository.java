package com.huigou.uasp.bmp.opm.repository.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.huigou.uasp.bmp.opm.domain.model.org.OrgTemplate;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgType;

/**
 * 组织机构模板资料库
 * 
 * @author gongmm
 */
public interface OrgTemplateRepository extends JpaRepository<OrgTemplate, String>, JpaSpecificationExecutor<OrgTemplate> {

    long countByParentId(String parentId);

    List<OrgTemplate> findByParentId(String parentId);

    OrgTemplate findFirstByOrgType(OrgType orgType);
}
