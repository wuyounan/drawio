package com.huigou.uasp.bpm.configuration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRule;

public interface ApprovalRuleRepository extends JpaRepository<ApprovalRule, String> {

    int countByParentId(String parentId);

    @Query(name = "approvalRule.countChildrenForRuleConfig", value = "select count(*) from ApprovalRule t where orgId = :orgId and procId = :procId and procUnitId = :procUnitId and t.status = 1 and parentId = :parentId")
    int countChildrenForRuleConfig(@Param("orgId") String orgId, @Param("procId") String procId, @Param("procUnitId") String procUnitId,
                                   @Param("parentId") String parentId);

    @Query(name = "approvalRule.countApprovalRuleElements", value = "select count(e) from ApprovalRule t join t.approvalRuleElements e where t.id = ?1")
    int countApprovalRuleElements(String approvalRuleId);

    @Query(name = "approvalRule.countApprovalRuleHandlers", value = "select count(h) from ApprovalRule t join t.approvalRuleHandlers h where t.id = ?1")
    int countApprovalRuleHandlers(String approvalRuleId);

    @Query(name = "approvalRule.findApprovalRules", value = "select o from ApprovalRule o where procId = ?1 and procUnitId = ?2 and parentId = ?3 and status = 1 order by priority")
    List<ApprovalRule> findApprovalRules(String procId, String procUnitId, String parentId);
}
