package com.huigou.uasp.bpm.engine.application;

import java.util.List;
import java.util.Map;

import com.huigou.context.OrgUnit;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bpm.HandleResult;
import com.huigou.uasp.bpm.configuration.domain.model.TaskExecuteMode;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandler;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandlerBase.Status;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandlerManuscript;

/**
 * 流程环节处理人
 * 
 * @author gongmm
 */
public interface ProcUnitHandlerApplication {

    /**
     * 保存流程环节处理人
     * 
     * @param procUnitHandler
     *            流程环节处理人
     * @return 流程环节处理人ID
     */
    String saveProcUnitHandler(ProcUnitHandler procUnitHandler);

    /**
     * 保存环节处理人手写数据
     * 
     * @param bizId
     *            业务ID
     * @param procUnitHandlerId
     *            环节处理人ID
     * @param height
     *            高度
     * @param opinion30
     *            base30意见
     * @param opinion64
     *            base64意见
     * @param String
     * @return
     */
    String saveProcUnitHandlerManuscript(String bizId, String procUnitHandlerId, Integer height, String opinion30, String opinion64);

    /**
     * 加载环节处理人手写数据
     * 
     * @param procUnitHandlerId
     *            环节处理人ID
     * @return
     */
    ProcUnitHandlerManuscript loadProcUnitHandlerManuscript(String procUnitHandlerId);

    /**
     * 查询环节处理人手写数据
     * 
     * @param bizId
     *            业务ID
     * @return
     */
    List<ProcUnitHandlerManuscript> queryProcUnitHandlerManuscriptsByBizId(String bizId);

    /**
     * 保存流程环节处理人
     * 
     * @param procUnitHandlers
     *            流程环节处理人列表
     * @return
     */
    void saveProcUnitHandlers(List<ProcUnitHandler> procUnitHandlers);

    /**
     * 保存流程环节处理人
     * 
     * @param procUnitHandlers
     *            流程环节处理人列表
     * @param updateExecutionTimes
     *            更新执行次数
     * @return
     */
    void saveProcUnitHandlers(List<ProcUnitHandler> procUnitHandlers, boolean updateExecutionTimes);

    /**
     * 批量保存流程环节处理人
     * 
     * @param procUnitHandlers
     *            流程环节处理ID列表
     */
    void batchSaveProcUnitHandlers(List<ProcUnitHandler> procUnitHandlers);

    /**
     * 删除流程环节处理人
     * 
     * @param ids
     *            流程环节处理人ID列表
     */
    void deleteProcUnitHandlers(List<String> ids);

    /**
     * 删除流程环节处理人
     * 
     * @param id
     *            流程环节处理人ID
     */
    void deleteProcUnitHandler(String id);

    /**
     * 根据业务id删除流程环节处理人
     * 
     * @param bizId
     *            业务ID
     */
    void deleteProcUnitHandlersByBizId(String bizId);

    /**
     * 删除流程环节处理人
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     */
    void deleteProcUnitHandlersByBizAndProcUnitId(String bizId, String procUnitId);

    /**
     * 删除流程环节处理人
     * 
     * @param bizId
     *            业务ID
     * @param groupId
     *            组ID
     */
    void deleteProcUnitHandlersByBizAndGroupId(String bizId, Integer groupId);

    /**
     * @param previousId
     */
    /**
     * 删除回收后继处理人
     * 
     * @param bizId
     *            业务ID
     * @param taskId
     *            任务ID
     */
    void deleteWithdrawSucceedingHandlers(String bizId, String taskId);

    /**
     * 复制流程环节处理人
     * 
     * @param id
     *            环节处理人ID
     * @return
     */
    String copyProcUnitHandler(String id);

    String copyProcUnitHandler(String id, String bizId, String bizCode, String procUnitId, Integer groupId);

    /**
     * 复制流程环节处理人
     * 
     * @param bizId
     *            业务ID
     * @param sourceProcUnitId
     *            源流程环节ID
     * @param destProcUnitId
     *            目标流程环节ID
     * @param destProcUnitName
     *            目标流程环节名称
     */
    void copyProcUnitHandlersByBizAndProcUnitId(String bizId, String sourceProcUnitId, String destProcUnitId, String destProcUnitName);

    /**
     * 得到当前组的任务执行模式
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            审批环节
     * @param groupId
     *            组ID
     * @return
     */
    TaskExecuteMode getTaskExecuteMode(String bizId, String procUnitId, Integer groupId);

    /**
     * 根据业务编码查询流程环节处理人
     * 
     * @param bizCode
     *            业务编码
     * @return
     */
    Map<String, Object> queryProcunitHandlersByBizCode(String bizCode);

    /**
     * @param bizId
     * @return
     */
    /**
     * 对流程环节处理人分组
     * 
     * @param bizId
     *            业务ID
     * @param processDefinitionKey
     *            流程定义Key
     * @return
     */
    List<Map<String, Object>> groupProcUnitHandlers(String bizId, String processDefinitionKey);

    /**
     * 对流程环节处理人分组
     * 
     * @param bizId
     *            业务ID
     * @param approvalProcUnitId
     *            查询处理人列表环节ID
     * @param procUnitId
     *            当前处理环节ID
     * @param currentTaskId
     *            当前任务ID
     * @param personId
     *            人员ID
     * @return
     */
    List<Map<String, Object>> groupProcUnitHandlers(String bizId, String approvalProcUnitId, String procUnitId, String currentTaskId, String personId);

    /**
     * 查询环节处理人
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            环节ID
     * @param groupId
     *            分组ID
     * @return
     */
    List<ProcUnitHandler> queryProcUnitHandlers(String bizId, String procUnitId, Integer groupId);

    /**
     * 查询流程环节处理人和任务信息
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @return
     */
    List<Map<String, Object>> queryProcUnitHandlersAndTaskInsts(String bizId, String procUnitId);

    /**
     * 加载流程环节处理人
     * 
     * @param id
     *            流程环节处理人ID
     * @return
     */
    ProcUnitHandler loadProcUnitHandler(String id);

    /**
     * 查询下一组流程环节处理人
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @param groupId
     *            分组ID
     * @return
     */
    List<ProcUnitHandler> queryNextGroupProcUnitHandlers(String bizId, String procUnitId, Integer groupId);

    /**
     * 根据业务ID和组ID查询环节处理人ID列表
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @param groupId
     *            分组ID
     * @return
     */
    List<String> queryProcUnitHandlerIds(String bizId, String procUnitId, Integer groupId);

    /**
     * 查询审批规则ID
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @return
     */
    String queryApprovalRuleId(String bizId, String procUnitId);

    /**
     * 查询已完成的环节处理人
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @return
     */
    List<ProcUnitHandler> queryCompletedProcUnitHandlers(String bizId, String procUnitId);

    /**
     * 查询已完成的环节处理人
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @param groupId
     *            分组ID
     * @return
     */
    List<ProcUnitHandler> queryCompletedProcUnitHandlers(String bizId, String procUnitId, Integer groupId);

    /**
     * 查询抄送人员
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @param chiefId
     *            主审人ID
     * @return
     */
    List<ProcUnitHandler> queryCCProcUnitHandlers(String bizId, String procUnitId, String chiefId);

    /**
     * 查询抄送人员
     * 
     * @param bizId
     *            业务ID
     * @return
     */
    List<ProcUnitHandler> queryCCProcUnitHandlersByBizId(String bizId);

    /**
     * 查询初始化补审处理人
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @return
     */
    List<ProcUnitHandler> queryInitialMendProcUnitHandlers(String bizId, String procUnitId);

    /**
     * 人员是否在审批人员列表中
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @param personMemberId
     *            人员成员ID
     * @return
     */
    boolean isPsmInHandlers(String bizId, String procUnitId, String personMemberId);

    /**
     * 更新处理人冗余数据
     * 
     * @param id
     *            流程环节处理人ID
     * @param orgUnit
     *            组织节点数据
     */
    void updateProcUnitHandlerOrgData(String id, OrgUnit orgUnit);

    /**
     * 转交
     * 
     * @param id
     *            流程环节处理人ID
     * @param sendMessage
     *            发送消息
     * @param orgNodeData
     *            组织节点数据
     */
    ProcUnitHandler transmit(String id, Integer sendMessage, OrgUnit orgUnit);

    /**
     * 保存协审人员
     * 
     * @param chiefProcUnitHandlerId
     *            主审人ID
     * @param personMember
     *            协审人
     */
    String saveAssistant(String chiefProcUnitHandlerId, Org personMember, Integer sendMessage, Integer index);

    /**
     * 保存协审人员
     * 
     * @param chiefProcUnitHandlerId
     *            主审人ID
     * @param personMember
     *            协审人
     * @param sendMessage
     *            发送即时消息
     * @param index
     *            顺序号
     */
    String saveAssistant(String chiefProcUnitHandlerId, String personMemberId, Integer sendMessage, Integer index);

    /**
     * 查询协审人员
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @param chiefProcUnitHandlerId
     *            主审人ID
     * @return
     */
    List<ProcUnitHandler> queryAssistantHandlers(String bizId, String procUnitId, String chiefProcUnitHandlerId);

    /**
     * 查询协审对用的主审人员
     * 
     * @param assistantProcUnitHandlerId
     *            协审环节处理人Id
     * @return
     */
    ProcUnitHandler queryChiefHandler(String assistantProcUnitHandlerId);

    /**
     * 查询加签处理人员
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @return
     */
    Map<String, Object> queryCounterSignHandlers(String bizId, String procUnitId);

    /**
     * 保存加减签处理人
     * <ol>
     * <li>前提条件：流程环节处理人实例数据库版本与加减签业务单据版本需一致，产生不一致的原因是并行处理，有两种情况： 并行环节或者申请环节与审批环节同时加减签；申请环节加减签，审批环节流转。</li>
     * <li>处理过程：有减签人员，删除环节处理人；有加签人员，需将当前处理人对应的审批规则 <tt>approvalRuleId、approvalRuleHandlerId</tt>写入到加签处理人，保存加签处理人。</li>
     * </ol>
     * 
     * @param params
     *            加减签处理人
     */
    /**
     * 保存加减签
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @param version
     *            版本号
     * @param currentProcUnitHandlerId
     *            当前环节处理人ID
     * @param minusSignIds
     *            减签ID列表
     * @param countersigns
     *            加签人员
     */
    void saveCounterSignHandlers(String bizId, String procUnitId, Long version, String currentProcUnitHandlerId, List<String> minusSignIds,
                                 List<ProcUnitHandler> countersigns);

    /**
     * 得到活动的审批环节组ID
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @return 活动的流程审批组ID
     */
    Integer getActiveProcUnitHanderGroupId(String bizId, String procUnitId);

    /**
     * 查询最后一个环节处理人
     * 
     * @param bizId
     *            业务ID
     * @return
     */
    ProcUnitHandler queryLastProcUnitHandler(String bizId);

    /**
     * 更新环节处理人状态
     * 
     * @param procUnitHandler
     *            环节处理人
     * @param status
     *            状态
     */
    void updateProcUnitHandlerStatus(ProcUnitHandler procUnitHandler, ProcUnitHandler.Status status);

    /**
     * 更新后继环节处理人状态
     * 
     * @param procUnitHandler
     *            流程环节处理人
     * @param status
     *            状态
     */
    void updateSucceedingProcUnitHandlersStatus(ProcUnitHandler procUnitHandler, ProcUnitHandler.Status status);

    /**
     * 检查主审是否审批完成
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @return
     */
    Boolean checkChiefApproveFinished(String bizId, String procUnitId);

    /**
     * 检查当前组主审是否审批完成
     * 
     * @param bizId
     * @param procUnitId
     * @param groupId
     * @return
     */
    Boolean checkCurrentGroupApproveFinished(String bizId, String procUnitId, Integer groupId);

    /**
     * 统计主审未通过人数
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @return
     */
    Integer countChiefNotApprove(String bizId, String procUnitId);

    /**
     * 统计协审未通过人数
     * 
     * @param bizId
     *            业务ID
     * @param chiefId
     *            主审ID
     * @return
     */
    Integer countAssistantNotApproveByChiefId(String bizId, String chiefId);

    /**
     * 检查当前组主审人是否审批通过
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            环节ID
     * @param groupId
     *            组ID
     * @return
     */
    Boolean checkCurrentGroupChiefApprovePassed(String bizId, String procUnitId, Integer groupId);

    /**
     * 更新环节处理人处理结果
     * 
     * @param id
     *            流程环节处理人ID
     * @param result
     *            处理结果
     * @param opinion
     *            意见
     * @param status
     *            状态
     */
    void updateProcUnitHandlerResult(String id, HandleResult result, String opinion, Status status);

    /**
     * 更新其他环节处理人的处理结果为系统完成
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            环节ID
     * @param currentGroupId
     *            当前组ID
     * @param currentProcUnitHandlerId
     *            当前环节处理人ID
     * @param opinion
     *            处理意见
     */
    void updateOtherProcUnitHandlersResultSystemComplete(String bizId, String procUnitId, Integer currentGroupId, String currentProcUnitHandlerId,
                                                         String opinion);

    /**
     * 查询历史处理人
     * 
     * @param bizCode
     *            业务编码
     * @return
     */
    Map<String, Object> queryHistoricProcUnitHandlers(String bizCode);

    /**
     * 更新环节处理人的委托人信息
     * 
     * @param id
     *            ID
     * @param clientId
     *            委托人ID
     * @param clientName
     *            委托人名称
     */
    void updateProcUnitHandlerClient(String id, String clientId, String clientName);

    /**
     * 更新协审人的主审ID
     * <p>
     * 主审打回后，会生成新的流程环节处理人
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @param oldChiefId
     *            原主审人ID
     * @param newChiefId
     *            新主审人ID
     */
    void updateProcUnitHandlerAssistantChiefId(String bizId, String procUnitId, String oldChiefId, String newChiefId);

    /**
     * 备份环节处理人
     * 
     * @param bizCode
     *            业务单据编号
     */
    void backupProcUnitHandler(String bizCode);

    /**
     * 恢复业务处理人
     * 
     * @param bizCode
     *            业务单据编号
     */
    void recoverProcUnitHandler(String bizCode);

    /**
     * 获取最大的分组ID
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     */
    Integer getMaxGrouId(String bizId, String procUnitId);

    /**
     * 获取最大的排序号
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @return
     */
    Integer getMaxSequence(String bizId, String procUnitId);

    List<Map<String, Object>> queryUIElmentPermissions(String procUnitHandlerId);

}
