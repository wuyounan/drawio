package com.huigou.uasp.bpm.configuration.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.CodeAndNameQueryRequest;
import com.huigou.data.domain.query.EmptyQueryRequest;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalElement;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalHandlerKind;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRule;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleElement;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandler;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandlerAssist;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandlerUIElmentPermission;
import com.huigou.uasp.bpm.configuration.domain.model.TaskExecuteMode;
import com.huigou.uasp.bpm.configuration.domain.query.ProcApprovalElementDesc;
import com.huigou.uasp.bpm.engine.domain.model.LimitTime;

/**
 * 流程审批规则服务
 * 
 * @author gongmm
 */
public interface ApprovalRuleApplication {
    /**
     * 查询文件配置地址
     */
    String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/bpm.xml";

    /**
     * 保存审批要素
     * 
     * @param approvalElement
     *            审批要素
     * @return 审批要素ID
     */
    String saveApprovalElement(ApprovalElement approvalElement);

    /**
     * 加载审批要素
     * 
     * @param id
     *            审批要素ID
     * @return 审批要素实体
     */
    ApprovalElement loadApprovalElement(String id);

    /**
     * 删除审批要素
     * 
     * @param ids
     *            审批要素ID列表
     */
    void deleteApprovalElements(List<String> ids);

    /**
     * 分页查询审批要素
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryApprovalElements(CodeAndNameQueryRequest queryRequest);

    /**
     * 得到审批要素排序号
     * 
     * @return
     */
    Integer getApprovalElementNextSequence();

    /**
     * 保存审批要素排序号
     * 
     * @param params
     */
    void updateApprovalElementsSequence(Map<String, Integer> params);

    /**
     * 保存审批处理人类别
     * 
     * @param approvalHandlerKind
     *            审批处理人类别
     * @return 审批处理人类别ID
     */
    String saveApprovalHandlerKind(ApprovalHandlerKind approvalHandlerKind);

    /**
     * 加载审批处理人类别
     */
    ApprovalHandlerKind loadApprovalHandlerKind(String id);

    /**
     * 删除审批处理人类别
     */
    void deleteApprovalHandlerKinds(List<String> ids);

    /**
     * 查询审批处理人类别
     */
    Map<String, Object> slicedQueryApprovalHandlerKinds(CodeAndNameQueryRequest queryRequest);

    /**
     * 得到审批处理人类别排序号
     * 
     * @return
     */
    Integer getApprovalHandlerKindNextSequence();

    /**
     * 保存审批处理人类别排序号
     * 
     * @param params
     */
    void updateApprovalHandlerKindsSequence(Map<String, Integer> params);

    /**
     * 保存流程审批要素
     * 
     * @param procId
     *            流程ID
     * @param procUnitId
     *            流程环节ID
     * @param elementIds
     *            审批要素ID列表
     */
    void saveProcApprovalElement(String procId, String procUnitId, List<String> elementIds);

    /**
     * 删除流程审批要素
     */
    void deleteProcApprovalElements(List<String> ids);

    /**
     * 更新流程审批要素配置序列号
     * 
     * @param params
     */
    void updateProcApprovalElementSequence(Map<String, Integer> params);

    /**
     * 查询流程审批要素
     * 
     * @param procId
     *            流程ID
     * @param procUnitId
     *            流程环节ID
     * @return
     */
    List<ProcApprovalElementDesc> queryProcApprovalElements(String procId, String procUnitId);

    /**
     * 添加流程审批规则
     * 
     * @param approvalRule
     * @return
     */
    String insertApprovalRule(ApprovalRule approvalRule);

    /**
     * @param approvalRule
     * @return
     */
    /**
     * 更新流程审批规则
     * 
     * @param approvalRule
     *            审批规则
     *            原名称
     */
    void updateApprovalRule(ApprovalRule approvalRule);

    /**
     * 加载流程审批规则
     * 
     * @param id
     *            流程审批规则ID
     * @return
     */
    ApprovalRule loadApprovalRule(String id);

    /**
     * 删除流程审批规则
     * 
     * @param ids
     *            审批规则ID列表
     */
    void deleteApprovalRules(List<String> ids);

    /**
     * 移动审批规则
     * 
     * @param id
     *            审批规则ID
     * @param parentId
     *            父ID
     */
    void moveApprovalRules(String id, String parentId);

    /**
     * 查询流程审批规则
     * 
     * @param orgId
     *            组织ID
     * @param procId
     *            流程ID
     * @param procUnitId
     *            流程环节ID
     * @param procUnitName
     *            流程环节名称
     * @param parentId
     *            父ID
     * @return
     */
    Map<String, Object> queryApprovalRules(String orgId, String procId, String procUnitId, String procUnitName, String parentId);

    /**
     * 查询流程审批规则
     * 
     * @param procId
     *            流程Id
     * @param procUnitId
     *            流程环节ID
     * @param parentId
     *            父ID
     * @return
     */
    List<ApprovalRule> queryApprovalRules(String procId, String procUnitId, String parentId);

    /**
     * 保存审批规则要素
     * 
     * @param approvalRuleId
     *            审批规则ID
     * @param approvalRuleElements
     *            审批规则要素
     */
    void saveApprovalRuleElements(String approvalRuleId, List<ApprovalRuleElement> approvalRuleElements);

    /**
     * 删除审批规则要素
     * 
     * @param approvalRuleId
     *            流程审批规则ID
     * @param ids
     *            审批规则要素ID列表
     */
    void deleteApprovalRuleElements(String approvalRuleId, List<String> ids);

    /**
     * 查询审批规则要素
     * 
     * @param approvalRuleId
     *            审批规则ID
     * @param queryRequest
     *            分页信息
     * @return
     */
    Map<String, Object> queryApprovalRuleElements(String approvalRuleId, EmptyQueryRequest queryRequest);

    /**
     * 为流程图查询审批规则要素
     * 
     * @param approvalRuleId
     *            审批规则ID
     * @return
     */
    List<Map<String, Object>> queryApprovalRuleElementsForFlowChart(String approvalRuleId);

    /**
     * 保存流程审批规则审批人
     * 
     * @param approvalRuleId
     *            流程审批规则ID
     * @param approvalRuleHandlers
     *            流程审批规则处理人列表
     */
    void saveApprovalRuleHandlers(String approvalRuleId, List<ApprovalRuleHandler> approvalRuleHandlers);

    /**
     * 保存流程审批规则审批人明细
     * 
     * @param approvalRuleId
     *            流程审批规则ID
     * @param approvalRuleHandler
     *            流程审批规则处理人
     * @param taskExecuteMode
     *            任务执行模式
     * @param assistants
     *            协审
     * @param ccs
     *            抄送
     * @param fieldAuthorizations
     *            字段授权
     */
    void saveApprovalRuleHandler(String approvalRuleId, ApprovalRuleHandler approvalRuleHandler, TaskExecuteMode taskExecuteMode,
                                 List<ApprovalRuleHandlerAssist> assistants, List<ApprovalRuleHandlerAssist> ccs,
                                 List<ApprovalRuleHandlerUIElmentPermission> fieldAuthorizations);

    /**
     * 删除流程审批规则审批人
     * 
     * @param approvalRule
     *            流程审批规则
     * @param ids
     *            流程审批规则审批人ID列表
     */
    void deleteApprovalRuleHandlers(String approvalRuleId, List<String> ids);

    /**
     * 查询审批规则处理人
     * 
     * @param approvalRuleId
     *            流程审批规则ID
     * @param queryRequest
     *            分页信息
     * @return
     */
    Map<String, Object> queryApprovalRuleHandlers(String approvalRuleId, EmptyQueryRequest queryRequest);

    /**
     * 查询审批环节协审人员
     * 
     * @param approvalRuleHandlerId
     *            审批规则处理人ID
     * @return
     */
    Map<String, Object> queryAssistantHandlers(String approvalRuleHandlerId);

    /**
     * 查询抄送人员
     * 
     * @param approvalRuleHandlerId
     *            审批规则处理人ID
     * @return
     */
    Map<String, Object> queryCCHandlers(String approvalRuleHandlerId);

    /**
     * 查询界面元素权限
     * 
     * @param approvalRuleHandlerId
     *            审批规则处理人ID
     * @return
     */
    Map<String, Object> queryUIElementPermissions(String approvalRuleHandlerId);

    /**
     * 获取审批处理人时间限制
     * 
     * @param procId
     *            流程ID
     * @param procUnitId
     *            流程环节ID
     * @param approvalRuleHandlerId
     *            审批规则处理人ID
     * @return
     */
    LimitTime getApprovalRuleHandlerLimitTime(String procId, String procUnitId, String approvalRuleHandlerId);

    /**
     * 加载审批规则处理人
     * 
     * @param approvalRuleHandlerId
     *            审批规则处理人ID
     * @return
     */
    ApprovalRuleHandler loadApprovalRuleHandler(String approvalRuleHandlerId);

    Long countApprovalRule();

    Map<String, Object> queryApprovalRuleApply(String procId, String procUnitId, Map<String, Object> bizProcessParams);

    /**
     * 删除审批规则适用范围
     * 
     * @param approvalRuleId
     *            流程审批规则
     * @param ids
     *            审批规则要素ID列表
     */

    void deleteApprovalRuleScopes(String approvalRuleId, List<String> ids);

    /**
     * 查询审批规则适用范围
     * 
     * @param approvalRuleId
     *            流程审批规则ID
     * @param queryRequest
     *            分页信息
     * @return
     */
    Map<String, Object> queryApprovalRuleScopes(String approvalRuleId, EmptyQueryRequest queryRequest);

    Map<String, Object> synApprovalRule(String orgId, String procId, Long approveRuleId);

}
