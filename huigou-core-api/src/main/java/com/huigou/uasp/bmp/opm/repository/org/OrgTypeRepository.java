package com.huigou.uasp.bmp.opm.repository.org;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.huigou.uasp.bmp.opm.domain.model.org.OrgType;

/**
 * 组织类别资料库
 * 
 * @author Gerald
 */
@Repository
public interface OrgTypeRepository extends JpaRepository<OrgType, String>, JpaSpecificationExecutor<OrgType> {

    OrgType findByOrgKindIdAndCode(String orgKindId, String code);

}
