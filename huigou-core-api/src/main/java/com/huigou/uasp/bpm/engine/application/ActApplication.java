package com.huigou.uasp.bpm.engine.application;

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;

import com.huigou.context.OrgUnit;
import com.huigou.uasp.bpm.HandleResult;
import com.huigou.uasp.bpm.ProcessStatus;
import com.huigou.uasp.bpm.TaskStatus;
import com.huigou.uasp.bpm.engine.domain.model.HistoricProcessInstanceExtension;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceExtension;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceRelation;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandler;
import com.huigou.uasp.bpm.engine.domain.model.RuntimeTaskExtension;
import com.huigou.uasp.bpm.engine.domain.query.TaskDetail;

public interface ActApplication {

    /**
     * 保存任务扩展信息
     *
     * @param runtimeTaskExtension 运行时任务
     */
    void saveTaskExtension(RuntimeTaskExtension runtimeTaskExtension);

    /**
     * 保存任务扩展信息
     *
     * @param runtimeTaskExtension    运行时任务
     * @param initiatorPersonMemberId 发起人ID
     */
    void saveTaskExtension(RuntimeTaskExtension runtimeTaskExtension, String initiatorPersonMemberId);

    /**
     * 保存任务扩展信息
     *
     * @param runtimeTaskExtension 运行时任务扩展
     * @param procUnitHandler      流程环节处理人
     */
    void saveTaskExtension(RuntimeTaskExtension runtimeTaskExtension, ProcUnitHandler procUnitHandler);

    /**
     * 更新任务扩展状态
     * <p>
     * 包括运行时和历史任务
     *
     * @param taskId     任务ID
     * @param taskStatus 任务状态
     */
    void updateTaskExtensionStatus(String taskId, TaskStatus taskStatus);

    /**
     * 更新扩展任务状态为暂缓
     *
     * @param taskId 任务ID
     */
    void updateTaskExtensionSleepingStatus(String taskId);

    /**
     * 更新历史任务扩展是否计时
     *
     * @param taskId     任务ID
     * @param needTiming 是否计时
     */
    void updateTaskExtensionNeedTiming(String taskId, Integer needTiming);

    /**
     * 更新任务扩展处理意见
     *
     * @param taskId  任务ID
     * @param result  处理结果
     * @param opinion 处理意见
     */
    void updateTaskExtensionHandleResult(String taskId, HandleResult result, String opinion);

    /**
     * 更新Historic任务扩展处理意见
     *
     * @param taskId
     * @param result
     * @param opinion
     */
    void updateHistoricTaskExtensionHandleResult(String taskId, HandleResult result, String opinion);

    /**
     * 更新任务处理人
     *
     * @param taskId          任务ID
     * @param orgUnit         组织单元
     * @param updateStartTime 更新开始时间
     */
    void updateTaskHanlder(String taskId, OrgUnit orgUnit, boolean updateStartTime);

    /**
     * 更新历史任务扩展状态
     *
     * @param taskId     任务ID
     * @param taskStatus 任务状态
     */
    void updateHistoricTaskInstanceExtensionStatus(String taskId, TaskStatus taskStatus);

    /**
     * 更新历史任务扩展为结束状态
     *
     * @param taskId       任务ID
     * @param taskStatus   任务状态
     * @param deleteReason 删除原因
     */
    void updateHistoricTaskInstanceExtensionEnded(String taskId, TaskStatus taskStatus, String deleteReason);

    /**
     * 从数据库中读取一条运行任务扩展信息
     *
     * @param id 任务ID
     * @return
     */
    RuntimeTaskExtension loadRuntimeTaskExtension(String id);

    /**
     * 从数据库中读取一条历史任务扩展信息
     *
     * @param id 任务ID
     * @return
     */
    HistoricTaskInstanceExtension loadHistoricTaskInstanceExtension(String id);

    /**
     * 查询历史任务实例
     *
     * @param ids 任务ID列表
     * @return
     */
    List<HistoricTaskInstanceExtension> queryHistoricTaskInstanceExtensions(List<String> ids);

    /**
     * 查询未完成的协作历史任务
     *
     * @param bizId 业务ID
     */
    List<RuntimeTaskExtension> queryCoordinationTaskInstances(String bizId);

    /**
     * 查询历史任务
     *
     * @param bizId      业务ID
     * @param procUnitId 环境ID
     * @return
     */
    List<HistoricTaskInstanceExtension> queryHistoricTaskInstanceByBizAndProcUnitId(String bizId, String procUnitId);

    /**
     * @param taskId
     */
    /**
     * 删除运行时任务扩展信息
     *
     * @param taskId 任务ID
     */
    void deleteRuntimeTaskExtension(String taskId);

    /**
     * 查询审批历史记录
     *
     * @param bizId 业务ID
     * @return
     */
    Map<String, Object> queryApprovalHistoryByBizId(String bizId);

    /**
     * 查询审批历史记录
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    Map<String, Object> queryApprovalHistoryByProcessInstanceId(String processInstanceId);

    /**
     * 查询回退任务
     *
     * @param bizCode    业务编码
     * @param procUnitId 流程环节ID
     * @param groupId    组ID
     * @return
     */
    Map<String, Object> queryBackTasksByBizCode(String bizCode, String procUnitId, Integer groupId);

    /**
     * 查询流程申请人
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    Map<String, Object> queryApplicantByProcessInstanceId(String processInstanceId);

    /**
     * 查询申请人任务
     *
     * @param bizId 业务ID
     * @return
     */
    HistoricTaskInstanceExtension queryApplicantTask(String bizId);

    /**
     * 查询申请人任务
     *
     * @param bizCode 业务编码
     * @return
     */
    HistoricTaskInstanceExtension queryApplicantTaskByBizCode(String bizCode);

    /**
     * 查询任务明细
     *
     * @param taskId 任务ID
     * @return
     */
    TaskDetail queryTaskDetail(String taskId);

    /**
     * 查询流程申请人
     *
     * @param bizId 业务ID
     * @return
     */
    Map<String, Object> queryApplicantByBizId(String bizId);

    /**
     * 从数据库中读取一条运行任务
     *
     * @param taskId 任务ID
     * @deprecated 已被 {@link #queryByRuntimeTaskById(String)} 替代。
     */
    @Deprecated
    Map<String, Object> loadRuntimeTaskById(String taskId);

    /**
     * 从数据库中读取一条运行任务
     *
     * @param taskId 任务ID
     * @since 1.1.3
     */
    RuntimeTaskExtension queryByRuntimeTaskById(String taskId);

    /**
     * 根据bizId从数据库中读取一条运行任务
     *
     * @param bizId
     * @return
     */
    Map<String, Object> loadRuntimeTaskByBizId(String bizId);

    /**
     * 从数据库中读取一条历史任务
     *
     * @param taskId 任务id
     * @return
     */
    Map<String, Object> loadHistoricTaskById(String taskId);

    /**
     * 根据业务编码查询任务数据
     *
     * @param bizCode 业务编码
     * @return
     */
    Map<String, Object> queryHiTaskInstsByBizCode(String bizCode);

    /**
     * 根据业务编码和类别ID查询任务数据
     *
     * @param bizCode 业务编码
     * @param kindId  类别ID
     * @return
     */
    Map<String, Object> queryHiTaskInstsByBizCodeAndKindId(String bizCode, String kindId);

    /**
     * 保存任务关联
     *
     * @param bizId                    业务ID
     * @param historicTaskInstRelation 任务关联对象
     */
    void saveHiTaskinstRelations(String bizId, List<HistoricTaskInstanceRelation> historicTaskInstRelations);

    /**
     * 删除任务关联
     *
     * @param ids 任务关联ID列表
     */
    void deleteHiTaskinstRelations(List<String> ids);

    /**
     * 查询任务关联
     *
     * @param taskId 任务ID
     * @param bizId  业务ID
     * @return
     */
    Map<String, Object> queryHiTaskinstRelations(String taskId, String bizId);

    // void saveHiProcInstExtension(String processInstanceId, String businessKey,
    // String initiatorPersonMemberId, String description, ProcessStatus
    // processStatus);

    /**
     * 加载流程实例扩展
     *
     * @param id 流程实例扩展ID
     * @return
     */
    HistoricProcessInstanceExtension loadHistoricProcessInstanceExtension(String id);

    /**
     * 根据流程实例ID加载流程实例扩展
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    HistoricProcessInstanceExtension loadHistoricProcessInstanceExtensionByProcInstId(String processInstanceId);

    /**
     * 添加流程实例扩展信息
     *
     * @param processInstanceId       流程实例ID
     * @param businessKey             业务主键ID
     * @param initiatorPersonMemberId 申请人员ID
     * @param description             流程实例描述（标题）
     * @param processStatus           流程状态
     */
    void saveHistoricProcessInstanceExtension(HistoricProcessInstanceEntity historicProcessInstanceEntity, String initiatorPersonMemberId, String description,
                                              ProcessStatus processStatus);

    /**
     * 更新流程实例扩展状态
     *
     * @param processInstanceId 流程实例ID
     * @param processStatus     流程状态
     */
    void updateHistoricProcessInstanceExtensionStatus(String processInstanceId, ProcessStatus processStatus);

    /**
     * 更新流程实例扩展已完成
     *
     * @param processInstanceId 流程实例ID
     * @param processStatus     流程状态
     * @param deleteReason      删除原因
     * @param endActivityId     结束环节ID
     */
    void updateHistoricProcessInstanceExtensionEnded(String processInstanceId, ProcessStatus processStatus, String deleteReason, String endActivityId);

    /**
     * 根据流程环节处理人ID查询最近的历史任务实例扩展数据
     *
     * @param procUnitHandlerId 环节处理人ID
     * @return
     */
    HistoricTaskInstanceExtension queryRecentHiTaskInstExtensionByProcUnitHandlerId(String procUnitHandlerId);

    /**
     * 根据流程环节处理人ID查询最历史任务实例扩展数据
     *
     * @param procUnitHandlerId 环节处理人ID
     * @return
     */
    List<HistoricTaskInstanceExtension> queryHiTaskInstExtensionByProcUnitHandlerId(String procUnitHandlerId);

    /**
     * 查询操作员的跟踪任务
     *
     * @return
     */
    Map<String, Object> queryOperatorTrackingTasks();

    /**
     * 查询完成的历史任务ID集合
     *
     * @param processInstanceId 流程实例ID
     * @param previousTaskId    前驱任务ID
     */
    List<String> queryHiTaskIdsByProcessInstanceAndPreviousId(String processInstanceId, String previousTaskId);

    /**
     * 查询运行时任务ID集合
     *
     * @param processInstanceId 流程实例ID
     * @param previousTaskId    前驱任务ID
     * @return 任务ID集合
     */
    List<String> queryRuTaskIdsByProcessInstanceAndPreviousId(String processInstanceId, String previousTaskId);

    /**
     * 查询主审的协审人任务ID列表
     *
     * @param bizId   业务ID
     * @param chiefId 主审ID
     * @return
     */
    List<String> queryRuAssistantTaskIds(String bizId, String chiefId);

    /**
     * 查询运行时任务ID集合
     *
     * @param processInstanceId 流程实例ID
     * @return 任务ID集合
     */
    List<String> queryRuTaskIdsByProcessInstanceId(String processInstanceId);

    List<HistoricTaskInstanceExtension> queryNotCompleteHiTaskInstExtensionsByProcInstId(String processInstanceId);

    /**
     * 统计待办流程任务数
     *
     * @param bizCode 业务编码
     * @return
     */
    long countReadyProcTasksByBizCode(String bizCode);

    /**
     * 统计等待的协审任务书
     *
     * @param bizId   业务ID
     * @param chiefId 主审ID
     * @return
     */
    Integer countWaitedAssistantTask(String bizId, String chiefId);

    /**
     * 流程中存在打回任务数
     *
     * @param bizId 业务ID
     * @return
     */
    boolean existsReplenishTask(String bizId);

    /**
     * 根据流程ID人员ID判断是否允许访问任务单据
     *
     * @param bizId
     * @param personId
     * @return
     */
    boolean checkVisitTaskByBizIdAndPersonId(String bizId, String personId);

    /**
     * 根据任务判断任务是否为可执行状态
     *
     * @param taskId
     * @return
     */
    boolean checkVisitTaskStatusByTaskId(String taskId);

}
