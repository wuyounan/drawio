package com.huigou.uasp.bpm.engine.application;

import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;

import com.huigou.uasp.bmp.opm.application.OrgApplication;
import com.huigou.uasp.bpm.BatchAdvanceParameter;
import com.huigou.uasp.bpm.TaskStatus;
import com.huigou.uasp.bpm.engine.domain.model.CoordinationTask;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnit;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandler;
import com.huigou.uasp.bpm.engine.domain.query.ProcunitHandlerQueryRequest;
import com.huigou.uasp.bpm.engine.domain.query.TaskDetail;
import com.huigou.util.SDO;

public interface WorkflowApplication {

    /**
     * 发布流程
     * 
     * @param procId
     *            流程定义ID
     * @param fileName
     *            发布文件名称
     */
    void deploy(String procId, String fileName);

    /**
     * 启动流程
     * <p>
     * startModel 流程启动模式 automatic(自动) startModel = 自动
     * <p>
     * <code>variables.put("startModel", "automatic")</code>
     * <p>
     * <code>variables.put("executorFullId", "/C232482A7AEA42C0B53A7AE628A9B7E2.ogn/39178.ogn/39187.dpt/39188.pos/39431@39188.psm"</code>
     * <p>
     * <code>variables.put("executorFullName","/**股份有限公司/**公司/**展中心/副总经理/**艳")</code>
     * 
     * @param processDefinitionKey
     *            流程定义Key
     * @param businessKey
     *            业务ID
     * @param variables
     *            变量
     * @return
     */
    ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables);

    /**
     * 启动流程，提交下一环节
     * 
     * @param processDefinitionKey
     *            流程定义Key
     * @param businessKey
     *            业务ID
     * @param variables
     *            变量
     * @return
     */
    ProcessInstance startAndAdvanceProcessInstanceByKeyInNewTransaction(String processDefinitionKey, String businessKey, Map<String, Object> variables);

    ProcessInstance startAndAdvanceProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables);

    /**
     * 启动流程
     * 
     * @param processDefinitionKey
     *            流程定义Key
     * @param variables
     *            变量
     * @return
     */
    ProcessInstance startProcessInstanceByKey(String processDefinitionKey, Map<String, Object> variables);

    /**
     * 任务服务
     */
    TaskService getTaskService();

    /**
     * 资源服务
     * 
     * @return
     */
    RepositoryService getRepositoryService();

    /**
     * 表单服务
     * 
     * @return
     */
    FormService getFormService();

    /**
     * 运行时服务
     * 
     * @return
     */
    RuntimeService getRunTimeService();

    /**
     * 历史服务
     * 
     * @return
     */
    HistoryService getHistoryService();

    /**
     * 流程扩展服务
     * 
     * @return
     */
    ActApplication getActApplication();

    /**
     * 组织机构服务
     * 
     * @return
     */
    OrgApplication getOrgApplication();

    /**
     * 流程环节处理人服务
     * 
     * @return
     */
    ProcUnitHandlerApplication getProcUnitHandlerService();

    /**
     * 保存业务数据
     * 
     * @param bizId
     *            业务ID
     * @param taskId
     *            任务ID
     */
    void saveBizData(String bizId, String taskId);

    /**
     * 是否显示预览处理人
     * <p>
     * 流程实例的当前环节审批完成，是否预览处理人
     * <ul>
     * <li>协审不预览处理人</li>
     * <li>非抢占模式，若为最后一个处理人，从流程定义表中获取是否预览处理人</li>
     * <li>抢占模式，从流程定义表中获取是否预览处理人</li>
     * <li>TODO 不同意：回退或终止流程，不需要弹出是否预览处理人</li>
     * </ul>
     * 
     * @param procId
     *            流程定义ID
     * @param procUnitId
     *            环节ID
     * @param groupId
     *            组ID
     * @param procUnitHandlerId
     *            环节处理人ID
     * @return
     */
    Boolean showQueryHandlers(String bizId, String procId, String procUnitId, Integer groupId, String procUnitHandlerId);

    /**
     * 查询处理人
     * 
     * @param bizId
     *            业务ID
     * @param taskId
     *            任务ID
     * @return
     */
    Map<String, Object> queryHandlers(String bizId, String taskId);

    /**
     * 流程流转
     * 
     * @param taskId
     *            任务ID
     * @param variables
     *            任务变量
     */
    void advance(String taskId, Map<String, Object> variables);

    /**
     * 批量提交
     * 
     * @param batchAdvanceParameters
     *            批量提交参数列表
     */
    void batchAdvance(List<BatchAdvanceParameter> batchAdvanceParameters);

    /**
     * 完成非流程任务
     * 
     * @param taskId
     *            任务ID
     */
    void completeTask(String taskId);

    /**
     * 删除任务
     * 
     * @param taskId
     *            任务ID
     */
    void deleteTask(String taskId);

    /**
     * 修改任务标题
     * 
     * @param taskId
     *            任务ID
     * @param description
     *            标题
     */
    void updateTaskDescription(String taskId, String description);

    /**
     * 更新任务集合的标题
     * 
     * @param params
     *            任务ID、标题组成的Map
     */
    void updateTasksDescription(Map<String, Object> params);

    /**
     * 完成补审任务
     * 
     * @param bizId
     *            业务ID
     * @param taskId
     *            任务ID
     */
    void completeMendTask(String bizId, String taskId);

    /**
     * 完成打回任务
     * 
     * @param bizId
     *            业务ID
     * @param taskId
     *            任务ID
     */
    void completeReplenishTask(String bizId, String taskId);

    /**
     * 终止任务
     * 
     * @param taskId
     *            任务ID
     */
    void abortTask(String taskId);

    /**
     * 终止流程实例
     * 
     * @param processInstanceId
     *            流程实例ID
     */
    void abortProcessInstance(String processInstanceId);

    /**
     * 通过业务ID终止流程
     * 
     * @param bizId
     *            业务ID
     */
    void abortProcessInstanceByBizId(String bizId);

    /**
     * 通过业务编码终止流程
     * 
     * @param bizCode
     *            业务编码
     */
    void abortProcessInstanceByBizCode(String bizCode);

    /**
     * 暂停任务
     * 
     * @param taskId
     *            任务ID
     */
    void suspendTask(String taskId);

    /**
     * 恢复任务
     * 
     * @param taskId
     *            任务ID
     */
    void recoverTask(String taskId);

    /**
     * 暂缓任务
     * 
     * @param taskId
     *            任务ID
     */
    void sleep(String taskId);

    /**
     * 抓回任务
     * 
     * @param taskId
     *            任务ID
     * @param previousId
     *            前驱任务ID
     */
    void withdrawTask(String taskId, String previousId);

    /**
     * 根据bizId 抓回任务(根据当前登录人判断)
     * 
     * @param bizId
     */
    void withdrawTaskByBizId(String taskId, String bizId);

    /**
     * 查询回退Activity
     * 
     * @param taskId
     *            任务ID
     * @return
     */
    List<ActivityImpl> queryBackActivity(String taskId);

    /**
     * 回退任务
     * 
     * @param taskId
     *            当前任务ID
     * @param destActivityId
     *            目标环节ID
     * @param backToProcUnitHandlerId
     *            回退环节处理人ID
     */
    void back(String taskId, String destActivityId, String backToProcUnitHandlerId);

    /**
     * 回退到申请环节
     * 
     * @param taskId
     *            当前任务ID
     *            回退到申请环节
     * @param processInstanceId
     */
    void backToApplyActivity(String taskId, String processInstanceId);

    /**
     * 协审
     * 
     * @param bizId
     *            协审
     * @param taskId
     *            业务ID
     * @param procUnitHandlerId
     *            环节处理人ID
     * @param deletedIds
     *            减签人员ID列表
     * @param executorIds
     *            执行人ID列表
     * @param sendMessages
     *            发送消息
     */
    void assist(String bizId, String taskId, String procUnitHandlerId, List<String> deletedIds, List<String> executorIds, List<Integer> sendMessages);

    /**
     * 转发任务
     * 
     * @param catalogId
     *            任务类别ID
     * @param taskId
     *            任务ID
     * @param procUnitHandlerId
     *            环节处理人ID
     * @param executorId
     *            执行人ID
     * @param sendMessage
     *            发送消息
     */
    void transmit(String catalogId, String taskId, String procUnitHandlerId, String executorId, Integer sendMessage);

    /**
     * 交办
     * 
     * @param taskId
     *            任务ID
     * @param procUnitHandlerId
     *            环节处理人ID
     * @param personMemberIds
     *            人员成员ID列表
     */
    void assign(String taskId, String procUnitHandlerId, List<String> personMemberIds);

    /**
     * 领取任务
     * 
     * @param taskId
     *            任务ID
     * @param personMemberId
     *            人员成员ID
     */
    void claim(String taskId, String personMemberId);

    /**
     * 抄送 页面执行的操作才需要 触发流程事件
     * 
     * @param taskId
     *            任务ID
     * @param executorIds
     *            执行人ID列表
     */
    void makeACopyForEvent(String taskId, List<String> executorIds);

    /**
     * 抄送
     * 
     * @param taskId
     *            任务ID
     * @param executorIds
     *            执行人ID列表
     */
    void makeACopyFor(String taskId, List<String> executorIds);

    /**
     * 抄送
     * 
     * @param taskId
     *            任务ID
     * @param executorIds
     *            执行人ID列表
     * @param description
     *            主题
     */
    void makeACopyFor(String taskId, List<String> executorIds, String description);

    /**
     * 创建通知
     * 
     * @param procUnitName
     *            环节名称
     * @param description
     *            主题
     * @param executorIds
     *            处理人
     */
    void createNoticeTask(String procUnitName, String description, List<String> executorIds);

    /**
     * 创建通知
     * 
     * @param procUnitName
     *            环节名称
     * @param description
     *            主题
     * @param executorIds
     *            处理人
     * @param formKey
     *            url
     * @param businessKey
     *            业务主键
     */
    void createNoticeTask(String procUnitName, String description, List<String> executorIds, String formKey, String businessKey);

    /**
     * 创建协同任务 发送事件
     * 
     * @param coordinationTask
     *            协同任务
     */
    void createCoordinationTask(CoordinationTask coordinationTask);

    /**
     * 创建协同任务
     * 
     * @param coordinationTask
     */
    void doCreateCoordinationTask(CoordinationTask coordinationTask);

    /**
     * 显示流程图
     * 
     * @param processInstanceId
     *            流程实例ID
     */
    // void showChart(String processInstanceId);

    /**
     * 得到所有流程定义
     * 
     * @return
     */
    List<ProcUnit> getAllProcessDefinitions();

    /**
     * 得到流程的用户任务节点
     * 
     * @param processDefinitionId
     *            流程定义ID
     * @return
     */
    List<ProcUnit> getUserTaskActivitiesByProcessDefinitionId(String processDefinitionId);

    /**
     * 查询任务详细
     * 
     * @param taskId
     *            任务ID
     * @return
     *         任务详细
     */
    TaskDetail queryTaskDetail(String taskId);

    /**
     * 查询任务
     * 
     * @return
     */
    Map<String, Object> queryTasks(SDO params);

    /**
     * 查询项目框架任务 <br>
     * 1、业务id <br>
     * 2、任务状态 待办任务、中止任务、暂停任务
     * 
     * @param params
     * @return
     */
    // Map<String, Object> queryTasksForProjectArchitecture(SDO params);

    /**
     * 根据登录名查询待办任务
     * 
     * @param loginName
     *            登录名
     * @param pageIndex
     *            页码
     * @param pageSize
     *            页大小
     * @return
     */
    // Map<String, Object> queryWaitingTasksByLoginName(String loginName, int
    // pageIndex, int pageSize);

    /**
     * App查询待办任务
     * 
     * @param loginName
     *            登录名
     * @param searchContent
     *            搜索内容
     * @param pageIndex
     *            页码
     * @param pageSize
     *            页大小
     * @return
     */
    // Map<String, Object> queryWaitingTasksForAppByLoginName(String loginName,
    // String searchContent, int pageIndex, int pageSize);

    /**
     * 查询流程处理线
     * 
     * @param bizId
     *            业务ID
     * @return
     */
    List<?> queryFlowChartProcedure(String bizId);

    /**
     * 查询流程图展示数据
     * <p>
     * 查询流程图结果数据,通过wf.chart.showAllRules系统参数控制是否显示所有审批规则，true显示所有审批规则，fasle不显示审批规则，只显示处理人表的数据。 wf.chart.showAllRules为true的情况下，流程实例匹配的审批规则，处理人为处理人表的数据，否则为审批规则处理人表的数据。
     * 
     * @param bizId
     *            业务ID
     * @param ownerOrgId
     *            流程实例拥有者组织ID
     * @param isShowAllRules
     *            前台参数true显示所有审批规则，fasle不显示审批规则
     * @return
     */
    Map<String, Object> queryFlowChart(String bizId, String ownerOrgId, String isShowAllRules);

    /**
     * 获取任务节点信息
     * 
     * @param bizId
     *            业务ID
     * @param procUnitHandlerId
     *            环节处理人ID
     * @return
     */
    Map<String, Object> getProcedureInfo(String bizId, String procUnitHandlerId);

    /**
     * 通过流程实例ID查询审批历史
     * 
     * @param processInstanceId
     *            流程实例ID
     * @return
     */
    // Map<String, Object> queryApprovalHistoryByProcInstId(String
    // processInstanceId);

    /**
     * 通过业务ID查询审批历史
     * 
     * @param bizId
     *            业务ID
     * @return
     */
    Map<String, Object> queryApprovalHistoryByBizId(String bizId);

    /**
     * 查询回退环节
     * 
     * @param processInstanceId
     *            流程实例ID
     * @param approvalRuleId
     *            审批规则ID
     * @param groupId
     *            分组ID
     * @return
     */
    Map<String, Object> queryBackProcUnit(String processInstanceId, String approvalRuleId, Integer groupId);

    /**
     * 查询回退任务
     * 
     * @param bizCode
     *            业务编码
     * @param procUnitId
     *            环节id
     * @param groupId
     *            分组id
     * @return
     */
    Map<String, Object> queryBackTasksByBizCode(String bizCode, String procUnitId, Integer groupId);

    /**
     * 查询加签处理人
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @return
     */
    Map<String, Object> queryCounterSignHandlers(String bizId, String procUnitId);

    /**
     * 保存加签处理人
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程环节ID
     * @param version
     *            版本号
     * @param currentProcUnitHandlerId
     *            当前流程环节处理人ID
     * @param minusSignIds
     *            减签ID列表
     * @param countersigns
     *            加加签列表
     */
    void saveCounterSignHandlers(String bizId, String procUnitId, Long version, String currentProcUnitHandlerId, List<String> minusSignIds,
                                 List<ProcUnitHandler> countersigns);

    /**
     * 查询协审人员
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            环节ID
     * @param chiefId
     *            主审人ID
     * @return
     */
    List<ProcUnitHandler> queryAssistHandlers(String bizId, String procUnitId, String chiefId);

    /**
     * 查询当前处理环节的字段(按钮)权限
     * 
     * @param id
     *            当前环节的ID
     * @return
     */
    List<Map<String, Object>> queryUIElmentPermissionsByProcUnitHandlerId(String id);

    /**
     * 查询跟踪任务
     * 
     * @return
     */
    public Map<String, Object> queryTrackingTasks();

    /**
     * 更改历史任务状态
     * 
     * @param taskId
     *            任务ID
     * @param status
     *            状态
     */
    void updateHistoricTaskInstanceStatus(String taskId, String status);

    /**
     * 更新任务状态
     * 
     * @param taskId
     *            任务ID
     * @param taskStatus
     *            任务状态
     */
    void updateTaskExtensionStatus(String taskId, TaskStatus taskStatus);

    /**
     * 发起补审任务
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            环节id
     */
    void launchMendTask(String bizId, String procUnitId);

    /**
     * @param bizId
     * @param procUnitId
     */
    /**
     * 补审
     * 
     * @param bizId
     *            业务ID
     * @param procUnitId
     *            流程审批环节ID
     * @param currentProcUnitHandlerId
     *            当前流程审批环节ID
     * @param version
     *            版本号
     * @param countersigns
     *            补审
     */
    void mend(String bizId, String procUnitId, String currentProcUnitHandlerId, Long version, List<ProcUnitHandler> countersigns);

    /**
     * 补充
     * 
     * @param backToTaskId
     *            打回任务ID
     * @param currentTaskId
     *            当前任务ID
     * @param procUnitHandlerId
     *            环节处理人ID
     */
    void replenish(String backToTaskId, String currentTaskId, String procUnitHandlerId);

    /**
     * 删除流程实例
     * 
     * @param processInstanceId
     *            流程实例ID
     * @param deleteReason
     *            删除原因
     */
    void deleteProcessInstance(String processInstanceId, String deleteReason);

    /**
     * 撤销流程实例
     * 
     * @param processInstanceId
     *            流程实例ID
     * @param taskId
     *            任务id
     */
    void recallProcessInstance(String processInstanceId, String taskId);

    /**
     * 添加评论
     * 
     * @param procUnitHandlerId
     *            环节处理ID
     * @param bizId
     *            业务ID
     * @param message
     *            评论消息
     */
    String saveComment(String procUnitHandlerId, String bizId, String message);

    /**
     * 查询环节处理人评论
     * 
     * @param procUnitHandlerId
     *            环节处理人ID
     * @return
     */
    List<Comment> queryProcUnitHandlerComments(String procUnitHandlerId);

    /**
     * 查询流程实例活动环节
     * 
     * @param processInstanceId
     *            流程实例ID
     * @return
     */
    List<ActivityImpl> queryProcessInstanceActiveActivities(String processInstanceId);

    /**
     * 交接任务
     * 
     * @param fromPersonId
     *            交接人员ID
     * @param toPsmId
     *            接收人员成员ID
     */
    void handTasks(String fromPersonId, String toPsmId);

    /**
     * 收藏任务
     * 
     * @param taskId
     *            任务ID
     */
    void collectTask(String taskId);

    /**
     * 取消收藏任务
     * 
     * @param taskId
     *            任务ID
     */
    void cancelCollectionTask(String taskId);

    /**
     * 查询当前操作员的任务统计信息
     * 
     * @return
     */
    Map<String, Object> queryCurrentOperatorTaskStatistics();

    /**
     * 查找流程代理类全名称
     * 
     * @param taskId
     *            任务ID
     * @return
     */
    String findBpmnDelegateClassCanonicalName(String taskId);

    /**
     * 检查协审通过规则
     * 
     * @param processDefinitionKey
     *            流程定义Key
     * @param taskDefinitionKey
     *            环节定义Key
     * @param bizId
     *            业务ID
     * @param procUnitHandlerId
     *            环节处理人ID
     */
    Boolean checkAssistantPassRule(String processDefinitionKey, String taskDefinitionKey, String bizId, String procUnitHandlerId);

    /**
     * 根据业务ID查询当前正在执行的任务
     * 
     * @param bizId
     * @return
     */
    Map<String, Object> loadRuntimeTaskByBizId(String bizId);

    /**
     * 流程处理人查询
     * 
     * @param queryRequest
     * @return
     */
    Map<String, Object> slicedQueryProcunitHandler(ProcunitHandlerQueryRequest queryRequest);

}
