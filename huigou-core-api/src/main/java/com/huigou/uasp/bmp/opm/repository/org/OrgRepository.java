package com.huigou.uasp.bmp.opm.repository.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgType;

/**
 * 组织机构资料库
 * 
 * @author gongmm
 */
public interface OrgRepository extends JpaRepository<Org, String> {

    List<Org> findByFullIdLike(String fullId);

    Org findByFullName(String fullName);

    Org findByFullId(String fullId);
    
    Org findByParentIdAndCode(String parentId, String code);
    
    @Query(name = "org.findMainOrgByPersonMemberId", value="select o from Org o join o.person p where p.mainOrgId = o.parentId and p.status = 1  and o.status = 1  and o.id = ?1")
    Org findMainOrgByPersonMemberId(String personMemberId);
    
    @Query(name = "org.findAllPersonMembersByOrgFullId", value="select o from Org o join o.person p where p.mainOrgId = o.parentId and p.status = 1  and o.status = 1  and o.fullId like ?1")
    List<Org> findAllPersonMembersByOrgFullId(String fullId);
    
    @Query(name = "org.findMainOrgByPersonId", value= "select o from Org o join o.person p where p.mainOrgId = o.parentId and p.id = ?1")
    Org findMainOrgByPersonId(String personId);

    @Query(name="org.findMainOrgByPersonName", value ="select o from Org o join o.person p where p.mainOrgId = o.parentId and p.status = 1  and o.status = 1  and p.name = ?1")
    Org findMainOrgByPersonName(String name);

    @Query(name="org.findMainOrgByLoginName", value="select o from Org o join o.person p where p.mainOrgId = o.parentId and p.status = 1  and o.status = 1  and upper(p.loginName) = ?1")
    Org findMainOrgByLoginName(String loginName);

    @Query(name="org.findOrgByLoginName", value="select o from Org o join o.person p where p.status = 1  and o.status = 1 and upper(p.loginName) = ?1")
    List<Org> findOrgByLoginName(String loginName);

    @Query(name="org.findPersonMembersByPersonId", value="select o from Org o join o.person p where o.status = 1  and p.id = ?1")
    List<Org> findPersonMembersByPersonId(String personId);
    
    @Query(name="org.findOrgStructureByParentId", value="select o from Org o where o.parentId = ?1 and orgKindId != 'psm' and o.status = 1  order by sequence")
    List<Org> findOrgStructureByParentId(String parentId);
    
    Org findFirstByOrgType(OrgType orgType);
    
    Org findFirstByOrgProperties_PropertyDefinitionId(String orgPropertyDefinitionId);
    
    @Query(name="org.findDeltaOrg", value="select o from Org o where o.version > ?1 order by version desc")
    List<Org> findDeltaOrg(Integer version);
}
