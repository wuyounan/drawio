package com.huigou.uasp.bpm.engine.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRule;

/**
 * 流程审批规则解析服务
 * 
 * @author gongmm
 */
public interface ProcApprovalRuleParseService {
    
    /**
     * 查询作用域范围内的审批规则
     * @param procId
     * 流程ID
     * @param procUnitId
     * 流程环节ID
     * @param ownerOrgId
     * 流程实例拥有者的组织ID
     * @param includeClassification
     * 是否包含分類
     * @return
     */
    List<ApprovalRule> queryScopeApprovalRules(String procId, String procUnitId, String ownerOrgId,boolean includeClassification);

    /**
     * 流程审批规则解析执行
     * 
     * @param procId
     *            流程ID
     * @param procUnitId
     *            流程环节ID
     * @param bizParams
     *            业务参数
     * @return 审批规则
     */
    ApprovalRule execute(String procId, String procUnitId, Map<String, Object> bizParams);
}
