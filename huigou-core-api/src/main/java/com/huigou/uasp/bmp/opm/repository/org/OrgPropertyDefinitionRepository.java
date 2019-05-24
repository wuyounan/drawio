package com.huigou.uasp.bmp.opm.repository.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.opm.domain.model.org.OrgPropertyDefinition;

/**
 * 组织机构资料库
 * 
 * @author gongmm
 */
public interface OrgPropertyDefinitionRepository extends JpaRepository<OrgPropertyDefinition, String> {
    OrgPropertyDefinition findFirstByOrgKindIdAndName(String orgKindId, String name);

    List<OrgPropertyDefinition> findByOrgKindIdOrderBySequence(String orgKindId);
}
